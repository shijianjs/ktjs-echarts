# CNB 云端 AI 开发环境的工具链镜像。
#
# 设计约束：**只装工具链，不 COPY 任何源码**。
# 源码改动不应该让镜像失效，否则每次改代码都要等一次完整 docker build。
#
# 由 .cnb.yml 的 main/push 流水线构建，推送到
#   ${CNB_DOCKER_REGISTRY}/${CNB_REPO_SLUG_LOWERCASE}/npc-env:latest
#
# 只在这里加「装一次、长期不变」的东西：JDK、Node、Chromium 的系统依赖、
# NPC 运行时。Gradle 依赖、Kotlin 编译器、npm 包、Chromium 本体都不进镜像，
# 它们变化频繁，交给 .cnb.yml 里的 volumes 缓存。
FROM eclipse-temurin:21-jdk-noble

ENV DEBIAN_FRONTEND=noninteractive \
    TZ=Asia/Shanghai \
    GRADLE_USER_HOME=/root/.gradle

RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      ca-certificates curl git git-lfs jq ripgrep unzip xz-utils openssh-client \
 && rm -rf /var/lib/apt/lists/* \
 && git lfs install \
 && git config --system --add safe.directory '*'

# Node：Kotlin/JS 的 npm 安装、Playwright 驱动、NPC 运行时（cnb-cli 是 npm 包）都依赖它。
# 用 npmmirror 的二进制镜像，nodejs.org 在国内经常拉不动。
ARG NODE_VERSION=22.22.2
RUN curl -fsSL "https://npmmirror.com/mirrors/node/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.xz" \
      | tar -xJ -C /usr/local --strip-components=1 \
 && node -v && npm -v

# Chromium 的系统依赖（libnss3 / libgbm1 / libasound2t64 等）。
# 浏览器本体不预装：kotlinInstallPlaywrightBrowsers 会下载与自己版本匹配的那一份，
# 预装容易版本错配；下载目录 /root/.cache/ms-playwright 由 volumes 缓存。
RUN npx --yes playwright@1 install-deps chromium

# NPC 运行时。@cnbcool/* 用官方 registry，避免镜像站同步延迟。
RUN npm install -g --registry=https://registry.npmjs.org @cnbcool/cnb-cli skills \
 && skills add https://cnb.cool/cnb/skills/cnb-skill.git -g -y

# 上游参考源码，直接固化进镜像。
#
# 这样做的三个好处：
#   1. 不用把 apache/echarts、apache/echarts-examples 导入 CNB 再维护同步——
#      项目多了以后「要同步的仓库太多」的问题不存在；
#   2. /refs 落在镜像层，容器里改了也只在上层可写层，销毁即消失，
#      不用再靠 git clean 兜底；
#   3. 省掉每次会话 clone 的 60 秒。
#
# 代价：上游发版要重建镜像。版本**从 package.json 读**，不在这里写死——
# 写死的话 Dependabot 升级 echarts 后重建仍会用旧版本，得靠人记得同步。
# 构建上下文需要包含 package.json，见 .cnb.yml 里的 docker build 和 .dockerignore。
#
# examples 仓库 682M，大头是 public/ 里的图片和数据集。AI 只需要读示例源码，
# 所以 sparse-checkout 只取 src、common、config 和 public/examples/ts
# （456 个示例源码，2.4M；数据集和图片不取）。
COPY package.json /tmp/project-package.json
RUN ECHARTS_REF="$(node -p "require('/tmp/project-package.json').dependencies.echarts")" \
 && echo "[refs] echarts 版本：${ECHARTS_REF}" \
 && mkdir -p /refs \
 && git clone --depth 1 --branch "${ECHARTS_REF}" \
      https://github.com/apache/echarts.git /refs/echarts \
 && git clone --depth 1 --filter=blob:none --sparse \
      https://github.com/apache/echarts-examples.git /refs/echarts-examples \
 && git -C /refs/echarts-examples sparse-checkout set src common config public/examples/ts \
 && rm -rf /refs/echarts/.git /refs/echarts-examples/.git \
 && printf '%s\n' \
      "这里是只读的上游参考仓库，不属于本仓库，不要修改、不要提交。" \
      "" \
      "  /refs/echarts            apache/echarts ${ECHARTS_REF} 的完整源码" \
      "  /refs/echarts-examples   apache/echarts-examples 的 src/common/config" \
      "                           和 public/examples/ts（示例源码，不含数据集和图片）" \
      "" \
      "固化在镜像里，没有 .git，不能 fetch、不能切分支。" \
      "要跟 master 对比就自己 git clone 到 /tmp。" \
      "注意上游 AGENTS.md 描述的是 master，和这里 pin 的 tag 可能有几处对不上，" \
      "以磁盘上的实际文件为准。" \
      > /refs/README.txt \
 && du -sh /refs/*

WORKDIR /workspace
