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

WORKDIR /workspace
