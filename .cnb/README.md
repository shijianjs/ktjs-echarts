# CNB 云端 AI 开发环境

把 CNB 当「云端 AI 开发机」，GitHub 继续当公开仓库和社区入口。这一目录是配套的
流水线配置，本文件记录为什么这么配、怎么上线、哪些还没验证。

## 结论先说

| 问题 | 答案 |
| --- | --- |
| 能设置 `@//npc/CodeBuddy(...)` 的执行环境吗 | **不能。** NPC 事件流水线用的是 **NPC 所属仓库** 的 `.cnb.yml`；系统 NPC 属于 `npc` 仓库，镜像和挂载都改不动。要自定义环境，只能在本仓库 `.cnb/settings.yml` 定义角色，让它「属于本仓库」 |
| 能继续用 `deepseek-v4.1-flash` 吗 | 能。模型不在角色定义里，而是 `.cnb.yml` 中 `npc:go` 的 `options.model`。自建角色 + 同款模型，行为和系统 NPC 一致，环境自己定 |
| JDK / Gradle / Kotlin 编译器每次重拉吗 | 不。分三层：镜像装「装一次就不变」的（JDK 21、Node 22、Chromium 系统依赖、NPC 运行时）；`volumes` 缓存「经常变」的（Gradle 依赖、Kotlin 编译器、npm 包、Chromium 本体）；只有真跨节点才需要 `docker:cache` |
| 三个仓库的上下文怎么给 | `apache/echarts`（pin 到 `package.json` 的版本）、`apache/echarts-examples`（sparse）、`JetBrains/kotlin-wrappers`（写 wrapper 的方法论参考）全部**固化进镜像**的 `/refs`。不用把上游导入 CNB、不用维护同步、不用 volumes 缓存；镜像层天然只读，AI 改了也只在上层可写层，容器销毁即消失 |

## 文件

```
.cnb.yml                            流水线：镜像构建 / NPC 执行 / 分支同步 GitHub
.cnb/settings.yml                   NPC 角色 kotlin（人设 + 项目约定）
.cnb/npc-env.Dockerfile             工具链 + 上游源码镜像，不 COPY 项目源码
.cnb/scripts/setup-gradle.sh        装 Maven 镜像 init 脚本与重试属性
.cnb/scripts/gradle-mirror.init.gradle  腾讯云 Maven 镜像前置（仅 CNB 环境）
.cnb/scripts/warmup-gradle.sh       冷启动预热依赖，绕开 Maven Central 限流
```

## 职责怎么分

```
Issue/PR 评论 @角色
      ↓
NPC 流水线（不带任何 GitHub 凭证）
  准备环境 → 预热依赖 → npc:go 改代码 → 推到 CNB 分支
      ↓
CNB 上出现 npc/<时间戳> 分支
      ↓ 触发 push 事件
同步流水线（官方 git-sync 插件）
  CNB 分支 → GitHub 同名分支
```

两条流水线互不知道对方。NPC 那边一行 GitHub 相关的配置都没有，所以它不引用密钥仓库
（引用过的 NPC 按平台规则不可分享），也不会在「不可信事件」里塞一个对 GitHub 有写权限的
token。同步那条跑在 `push` 这个可信事件上，只管机械地推分支。

## 缓存怎么分层

镜像**不 COPY 项目源码**，所以改代码不会让镜像失效。这一点很重要：如果 Dockerfile
里写了 `COPY . .`，平台按上下文算镜像哈希时每次代码改动都会重建，等于每次重拉一遍。
镜像里唯一「跟着项目走」的是 `/refs` 的上游源码，它由 `package.json` 的版本决定，
所以 `ifModify` 同时监听 `package.json`。

| 层 | 放什么 | 变化频率 | 失效条件 |
| --- | --- | --- | --- |
| 镜像 | JDK 21、Node 22、Chromium 系统依赖、`cnb-cli` + skills、`/refs` 上游源码 | 上游发版 / 工具链升级 | 改 Dockerfile 或 `package.json` 并重建 |
| `volumes` 节点缓存 | `/root/.gradle`、`/root/.npm`、`/root/.cache/ms-playwright` | 每次依赖变更 | 换构建节点（平台默认 3 个节点轮转） |

节点缓存**不保证命中**——平台扩缩容时会换节点，实测连续几轮里换过两次。真要连依赖也
不重下，就把依赖也固化成缓存镜像：

```yaml
- name: 缓存 Gradle 依赖
  type: docker:cache
  options:
    dockerfile: .cnb/deps.Dockerfile
    versionBy: package.json
  exports:
    name: DEPS_IMAGE
```

代价是依赖一变（Dependabot 每周都在动）就要重建一次镜像，而重建本身要跑一遍完整解析。
如果 NPC 会话频率不高，`volumes` + `warmup-gradle.sh` 重试更划算；频率上来再切。

## 首次上线

1. **导入仓库到 CNB**

   ```bash
   git clone --bare https://github.com/shijianjs/ktjs-echarts.git /tmp/ktjs-echarts.git
   cd /tmp/ktjs-echarts.git && git push --mirror https://cnb.cool/shijianjs/ktjs-echarts.git
   ```

   或者在 CNB 用 Code Import（<https://cnb.cool/cnb/plugins/cnbcool/code-import>）。
   导入后把 CNB 作为 `origin` 之外的第二个 remote，本地照常用 GitHub。

2. **建密钥仓库** `cnb-secrets`（类型选「密钥仓库」，禁止本地克隆，只能网页编辑）

   在默认分支建 `github.yml`：

   ```yaml
   # 只允许 ktjs-echarts 的流水线读这个文件，且只允许 git-sync 这个插件镜像使用。
   # 声明 allow_* 后平台会忽略触发者角色，完全按规则校验。
   allow_slugs: "shijianjs/ktjs-echarts"
   allow_images: "tencentcom/git-sync"

   GITHUB_TOKEN: "github_pat_xxx"   # fine-grained，只勾目标仓库 Contents: Read and write
   GITHUB_REPO: "shijianjs/ktjs-echarts"
   ```

   `.cnb.yml` 里 `imports` 的地址是
   `https://cnb.cool/shijianjs/cnb-secrets/-/blob/main/github.yml`。
   密钥仓库默认分支不是 `main` 的话，URL 里那段要跟着改。

   **`allow_images` 不是可选项。** 同步跑在 `push` 事件上，那是可信事件，所以不需要
   `allow_events`；但 git-sync 是插件任务（`image` + `settings`），平台要求被引用的密钥
   文件显式声明 `allow_images`，否则引用会被拒。

   **如果之前配过 `allow_events`，要删掉。** 一旦文件里声明了 `allow_events`，平台就
   完全按它校验，`push` 不在列表里会直接被挡：

   ```
   event: push does not conform to allow_events of
   https://cnb.cool/shijianjs/cnb-secrets/-/blob/main/github.yml
   ```

   这条是实测踩到的——从「NPC 流水线里推 GitHub」改成「push 事件同步」之后，
   旧的两条 npc 事件声明就把 `push` 挡在门外了。

3. **先构建一次镜像**

   `.cnb.yml` 里镜像构建挂在 `main` 的 `push` 且带 `ifModify`，首次导入没有「文件变动」
   可比，不会自动触发。在 CNB 页面手动跑一次 `main` 分支的流水线，或者：

   ```bash
   git commit --allow-empty -m "chore: 触发 NPC 镜像构建" && git push
   ```

   看到 `docker.cnb.cool/shijianjs/ktjs-echarts/npc-env:latest` 推上去即可。

4. **@ 自建 NPC 验证**

   新建一个 Issue，评论：

   ```text
   @shijianjs/ktjs-echarts(kotlin) 说说现在 wrapper 覆盖了 echarts 的哪些包，还差什么
   ```

   先只问不改。这一步只验证「镜像拉得到、`/refs` 读得到、NPC 能跑命令」。

5. **再验证写代码**

   同一 Issue 里勾上「替我上班」再 @ 一次，让它改一处小东西。看构建日志：
   - 镜像是不是 `npc-env:latest`（`CNB_PIPELINE_DOCKER_IMAGE`）
   - 第二次运行时 Gradle 是否还在下载依赖（判断缓存有没有命中）
   - 末尾「同步到 GitHub」是否把分支推上去

## 为什么最后还是用官方 git-sync

第一版把推送塞在 NPC 流水线末尾，理由是「不依赖 NPC 推送是否触发 push 事件」。
代价是三条，都很实在：

1. NPC 事件属于**不可信事件**，却要在里面注入一个对 GitHub 有写权限的 token；
2. 引用密钥仓库会让这个 NPC **不可分享**（不进 NPC 榜单）；
3. 只支持 CNB → GitHub 单向，GitHub 上的改动回不来。

改成两条独立流水线后，这三条同时消失。原先担心的未知项「NPC 推送会不会触发 push 事件」
是这套方案的关键前提——NPC 推 `npc/<时间戳>` 分支后，要靠 `$: push` 的兜底规则接住它。
这条正在验证中，见下面「验证状态」。

一个副产品：NPC 流水线现在完全不带 GitHub 凭证，安全边界清晰得多。

## 首轮实测记录

跑通一轮只读验证（Issue #1）后确认的几件事，都已经落进配置：

| 现象 | 结论 | 处理 |
| --- | --- | --- |
| `./gradlew` 报 Permission denied | 仓库里 `gradlew` 一直是 `100644`（从 initial commit 起），Linux 上没有可执行位 | 已改成 `100755` 并提交。Windows 上 `core.filemode=false`，改完要 `git update-index --chmod=+x` |
| `compileKotlinJs` 首次失败，`429 Too Many Requests` | Maven Central 对 CNB 的共享出口 IP 限流，重跑即好 | 加 `warmup-gradle.sh` 预热并重试三次；另加腾讯云 Maven 镜像前置（实测接管 83%~88% 的请求） |
| `/refs` 能被 `touch` 写入 | copy-on-write 卷是 rw，不是只读挂载；流水线成功后 COW 层会合并进缓存 | 改成**固化进镜像**：`/refs` 落在镜像层，容器里改了只留在上层可写层，销毁即消失 |
| Issue 评论里没有 NPC 的回复 | NPC 把结论写在最终消息里，不会自动发评论 | 角色提示词里要求 `cnb issues comment --body-file`；实测生效，评论作者显示为 `shijianjs/ktjs-echarts(kotlin)` |
| 只读会话也触发了同步 | 没有新提交时脚本仍会推一个和 main 相同的分支 | 比较 `HEAD` 与 `CNB_COMMIT`，一致就跳过 |
| 只读模式下 `git push origin` 403 | `CNB_TOKEN` 没有 `repo-code:rw`，工作模式才给写权限 | 预期行为。等验证写代码时再确认工作模式下的推送 |

环境实测值（2026-10-28）：Temurin `21.0.12.1+1-LTS`、Node `v22.22.2`、npm `10.9.7`、
Gradle `9.4.1`（首次下载约 1 分钟）。

### Maven 镜像实测

| 项 | 结果 |
| --- | --- |
| 镜像接管比例 | 187:38、195:27，约 83%~88% 的请求走腾讯云 |
| 429 是否消失 | **没有**，但全部落在 `repo.maven.apache.org`，腾讯云侧 0 次 |
| 429 打在哪 | 只有 `org.jetbrains.kotlin-wrappers` 那组：`kotlin-browser-js`、`kotlin-cssom-core-js`、`kotlin-js-core-js`、`kotlin-js-js`、`kotlin-web` |
| 原因 | `settings.gradle.kts` 的 `exclusiveContent` 把 kotlin-wrappers 钉到 Central，init 脚本只往普通仓库列表插镜像，钉住的那个仓库对象不受影响 |
| 重试有效吗 | 有效但不彻底。`max.retries=6` 没吃满，每个构件 3 次真实 429 就放弃；重跑一次即成功（26s 失败 → 11s 成功） |

结论：镜像留着（省 83% 的请求），但别指望它消灭 429。真正压住这批 429 只有两条路——
把 kotlin-wrappers 从 `exclusiveContent` 放出来（镜像上 `.klib` 时有时无，风险自负），
或者把依赖固化进镜像。后者见上面「缓存怎么分层」。

### 工作模式跑通的那一轮

任务：统计 `/refs/echarts/src/export/` 各文件的导出数，产出 `docs/upstream-exports.md`。
冷启动节点（缓存未命中）的耗时构成：

| Stage | 耗时 | 说明 |
| --- | ---: | --- |
| Prepare | 18s | 拉代码、起容器 |
| 准备上游参考仓库 | 60s | 当时还在 clone，现在已固化进镜像，这一步没有了 |
| 预热 Gradle 依赖 | 146s | 含 Gradle 发行版、Kotlin 编译器、npm 包；热节点直接跳过 |
| NPC 执行任务 | 176s | 分析 14 个 `.ts`、写文档、commit |
| 同步到 GitHub | 4s | 推 CNB + 推 GitHub 新分支 |

结果：GitHub 上出现 `npc/<时间戳>` 分支，`main` 没被动过。工作模式下的
`CNB_TOKEN` 确实有 `repo-code:rw`，和只读模式形成对照。

NPC 自己指出的两个坑值得记下来：`charts.ts` / `option.ts` 里有被注释掉的历史导出，
正则容易多算；`option.ts` 的 `XxxComponentOption` 在 Kotlin 侧按语义并成了 `XxxOption`，
名字比对只能当线索，不能当覆盖率结论。

## 固化上游后的两个回归（已修）

把 `/refs` 从「每次 clone」改成「固化进镜像」时，第一版踩了两个坑，都是 NPC 自己查出来的：

1. **`echarts-examples` 丢了 `public/`**。sparse-checkout 只写了 `src common config`，
   而 `public/examples/ts` 里是 456 个示例源码（2.4M），`demo/AGENTS.md` 明确要求
   porter 去读它。已加回 `public/examples/ts`；`public/data` 的 256M 数据集和图片仍然不取。
2. **`ECHARTS_REF` 是 ARG 默认值，不会跟 `package.json` 同步**。上一版说「版本跟着
   package.json 走」其实不成立——Dependabot 升级后重建镜像，用的还是旧默认值。
   改成 `COPY package.json` 后用 `node -p` 读版本，构建上下文相应从 `.cnb` 改成根目录
   并加了 `.dockerignore`。

顺带清掉了 `REFS_ORIGIN` 这个已经没人读的变量。

还有一个不是 bug 但要知道的：`/refs/echarts` pin 的是 tag，master 比它新几个文件。
上游 `AGENTS.md` 描述的是 master，引用到的个别文件在 tag 里不存在。NPC 角色提示词里
已经写明「以磁盘上的实际文件为准」。

## 参考官方 NPC 配置改的三处

官方 NPC 仓库是 `npc/CodeBuddy`（`cnb git get-content --repo npc/CodeBuddy --file-path .cnb.yml`）。
对着它抄了三件事：

1. **`npc:go` 参数**：`contextWindow: 1m`、`thinkingLevel: "off"`、不设 `maxTurns`/`maxTokens`。
   之前给 `128k` + `medium` + `300`，以为更省，其实官方对 `deepseek-v4.1-flash` 就是关思考、
   给满上下文。**`off` 必须加引号**——YAML 1.1 里裸 `off` 是布尔 `false`，平台要的是字符串。
2. **提示词精简**：官方角色 prompt 只有「开干吧各位」五个字。我们原来两千字，把系统指令
   （尤其是「用 `cnb issues comment` 回复」）冲淡了。项目约定交给 `AGENTS.md`，prompt 只留
   铁律 + `/refs` 路径 + 几条禁令。
3. **`/refs` 加 kotlin-wrappers**：写 Kotlin/JS wrapper 要对着上游看 mixin 怎么拆、
   `external interface` 怎么建、生成与手写怎么分层，只有 echarts 的 TS 源码不够。
   实测 68M、77 个 `kotlin-*` 目录（72 个是真 Gradle 模块）。

顺带：`settings.gradle.kts` 去掉了 `exclusiveContent`。它把 kotlin-wrappers 钉死在 Central，
导致每次冷构建必撞 429。镜像第一次缺 klib 是 Nexus 还没回源，拉过一次就缓存了，
靠 `warmup-gradle.sh` 的重试能过，不值得为此硬钉 Central。

## 已知限制

- **NPC 不可分享**：流水线用了 `imports` 引用密钥仓库文件，按平台规则这个 NPC 不会出现在
  NPC 榜单里。自己用无所谓。
- **`/refs` 的版本是死的**：源码 pin 在 `package.json` 的 echarts 版本上，镜像里没有 `.git`，
  不能 fetch、不能切分支。要跟 master 最新对比时，让 NPC 自己 `git clone` 到 `/tmp`。
- **镜像构建要访问 GitHub**：`/refs` 是在 `docker build` 阶段 clone 的，构建节点到 GitHub
  不通就构建不出来。实测 echarts 8 秒、examples 60 秒。
- **CNB_TOKEN 的边界**：NPC 工作模式下的临时令牌只能访问当前仓库，且角色上限是
  Developer。推 GitHub 必须用密钥仓库里的独立 PAT，不能用 `CNB_TOKEN`。
- **GitHub 侧 CI 仍会跑**：CNB 推上去的分支会触发 GitHub Actions。想省额度就给 NPC
  的提交加 `[ci skip]`，或者在 GitHub 侧按分支前缀跳过。
- **PR 评论触发没实测过**：`pull_request.comment@npc` 和 `issue.comment@npc` 共用同一份
  锚点定义（`&npc-pipeline`），issue 侧已跑通，PR 侧只是入口不同。第一次真实 PR 里 @ 时
  留意一下构建日志。

## 哪些已经验证过

| 路径 | 状态 |
| --- | --- |
| Issue 评论 `@` 触发 | ✅ 多轮，含只读问答与工作模式改代码 |
| 自定义镜像 + `/refs` | ✅ JDK 21 / Node 22 / Gradle 9.4.1，三个上游仓库齐全 |
| NPC 改代码 → commit → 推 CNB | ✅ 工作模式下 `CNB_TOKEN` 有 `repo-code:rw` |
| 推 GitHub | ✅ 产生 `npc/<时间戳>` 分支，`main` 未被直接覆盖 |
| NPC 回评论 | ✅ 结论发在 Issue 里，作者显示为角色名 |
| YAML 锚点复用配置 | ✅ 两个事件共用 `&npc-pipeline`，平台解析正常 |
| PR 评论触发 | ⬜ 未实测 |
| `volumes` 缓存命中率 | ⚠️ 3 个节点轮转，冷启动约 5 分钟、热节点 30 秒 |

## 还没做的

- CNB 侧的 `main` push 只重建镜像，没跑构建和测试。要不要在 CNB 上也跑一遍全量测试，
  等看清 NPC 会话内的测试够不够用再说。
- 云原生开发环境（WebIDE / CodeBuddy Web）没配。NPC 是无状态的，每次新容器；需要人工
  长时间调试时，那是另一条路径，计费口径也不同。

## 分支与同步

- **NPC 工作模式在独立分支上改**，分支名 `npc/<时间戳>`，不直接动 `main`。只读会话
  不建分支、不提交，问完就结束。
- **分支推到 CNB 后，由 push 事件上的 git-sync 同步到 GitHub 同名分支**。同步是独立的
  一条流水线，只有它持有 GitHub 凭证；NPC 那条连一行 GitHub 配置都没有。
- **两边分支名一致**。`.cnb.yml` 里 git-sync 的 `branch` 取 `${CNB_BRANCH}`，CNB 上叫
  什么，GitHub 上就叫什么，不做改名也不建镜像分支。
- **`main` 只走人工合并**。同步只搬分支，不会替你在 GitHub 上开 PR、也不会合 `main`；
  NPC 的产物和人类提的分支一样，需要人看过再合。
