# CNB 云端 AI 开发环境

把 CNB 当「云端 AI 开发机」，GitHub 继续当公开仓库和社区入口。这一目录是配套的
流水线配置，本文件记录为什么这么配、怎么上线、哪些还没验证。

## 结论先说

| 问题 | 答案 |
| --- | --- |
| 能设置 `@//npc/CodeBuddy(...)` 的执行环境吗 | **不能。** NPC 事件流水线用的是 **NPC 所属仓库** 的 `.cnb.yml`；系统 NPC 属于 `npc` 仓库，镜像和挂载都改不动。要自定义环境，只能在本仓库 `.cnb/settings.yml` 定义角色，让它「属于本仓库」 |
| 能继续用 `deepseek-v4.1-flash` 吗 | 能。模型不在角色定义里，而是 `.cnb.yml` 中 `npc:go` 的 `options.model`。自建角色 + 同款模型，行为和系统 NPC 一致，环境自己定 |
| JDK / Gradle / Kotlin 编译器每次重拉吗 | 不。分三层：镜像装「装一次就不变」的（JDK 21、Node 22、Chromium 系统依赖、NPC 运行时）；`volumes` 缓存「经常变」的（Gradle 依赖、Kotlin 编译器、npm 包、Chromium 本体）；只有真跨节点才需要 `docker:cache` |
| 三个仓库的上下文怎么给 | `apache/echarts`（pin 到 `package.json` 的版本）和 `apache/echarts-examples` 直接**固化进镜像**的 `/refs`。不用把上游导入 CNB、不用维护同步、不用 volumes 缓存；镜像层天然只读，AI 改了也只在上层可写层，容器销毁即消失 |

## 文件

```
.cnb.yml                            流水线：镜像构建 / NPC 执行 / 同步 GitHub
.cnb/settings.yml                   NPC 角色 kotlin（人设 + 项目约定）
.cnb/npc-env.Dockerfile             工具链 + 上游源码镜像，不 COPY 项目源码
.cnb/scripts/setup-gradle.sh        装 Maven 镜像 init 脚本与重试属性
.cnb/scripts/gradle-mirror.init.gradle  腾讯云 Maven 镜像前置（仅 CNB 环境）
.cnb/scripts/warmup-gradle.sh       冷启动预热依赖，绕开 Maven Central 限流
.cnb/scripts/push-to-github.sh      把 NPC 的成果推回 GitHub
```

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
   # 只允许 ktjs-echarts 的 NPC 事件读这个文件。
   # 一旦声明 allow_*，平台会忽略触发者角色，完全按规则校验，所以两个事件都要列。
   allow_slugs: "shijianjs/ktjs-echarts"
   allow_events:
     - issue.comment@npc
     - pull_request.comment@npc

   GITHUB_TOKEN: "github_pat_xxx"   # fine-grained，只勾目标仓库 Contents: Read and write
   GITHUB_REPO: "shijianjs/ktjs-echarts"
   ```

   `.cnb.yml` 里 `imports` 的地址是
   `https://cnb.cool/shijianjs/cnb-secrets/-/blob/main/github.yml`。
   密钥仓库默认分支不是 `main` 的话，URL 里那段要跟着改。

   **`allow_events` 不是可选项。** `issue.comment@npc` 和 `pull_request.comment@npc`
   都在平台的「不可信事件」清单里（代码来源或触发者不可控），这类事件的流水线引用外部
   文件时，被引用文件必须显式声明 `allow_events`，否则直接不通过。少了这一段，
   「同步到 GitHub」这一步会以引用被拒失败。

   顺带说明风险边界：官方建议把密钥操作放在 `push` / `tag_push` 这类可信事件里。
   当前把 GitHub 推送放在 NPC 事件内，是试验期的取舍——能开工作模式的至少是仓库
   Developer，触发者可控，且 token 本身只勾了一个仓库的 Contents 写权限。等验证了
   「NPC 推送会触发 push 事件」之后，可以把这一步挪到 push 事件，更贴合安全模型。

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

## 用户原先列的三个未知项，怎么绕开的

原方案想用 Git Sync 插件 + push 事件串起来，那三个问题（NPC 提交是否触发同步、插件
能否同步 NPC 的分支、Token 怎么安全提供）都不用回答了——**改成同一条流水线里直接推**：

- NPC 在 `npc:go` 里改完代码、提交；
- 同一 Pipeline 的下一个 stage 执行 `push-to-github.sh`，先推 CNB 再推 GitHub；
- 分支名取当前 `HEAD`，若在默认分支上则自动改推到 `npc/<时间戳>`，避免 AI 直接覆盖 main。

链路确定，不依赖事件联动。代价是每次 NPC 会话都推一次，哪怕它只是回答问题——所以脚本
里判断了「没有新提交就只推当前分支」，空推是 no-op。

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
- **`pull_request.comment@npc` 是复制的一份**：没用 YAML 锚点，怕平台解析器对顶层
  锚点的处理不一致。改配置时两处一起改。

## 还没做的

- CNB 侧的 `main` push 只重建镜像，没跑构建和测试。要不要在 CNB 上也跑一遍全量测试，
  等看清 NPC 会话内的测试够不够用再说。
- 云原生开发环境（WebIDE / CodeBuddy Web）没配。NPC 是无状态的，每次新容器；需要人工
  长时间调试时，那是另一条路径，计费口径也不同。
