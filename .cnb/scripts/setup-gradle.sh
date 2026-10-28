#!/usr/bin/env bash
# 把 Gradle 侧的 CNB 环境适配装好，必须在任何 gradle 命令之前执行。
#
#   1. init 脚本：把腾讯云 Maven 镜像插到仓库列表最前面
#      （Maven Central 对 CNB 共享出口 IP 限流，会返回 429）
#   2. 重试属性：真被限流时让 Gradle 自己多试几次
#
# 两样都写在 GRADLE_USER_HOME 下，随 volumes 缓存持久化，不会污染仓库。
set -euo pipefail

GRADLE_USER_HOME="${GRADLE_USER_HOME:-/root/.gradle}"
WORKSPACE="${CNB_BUILD_WORKSPACE:-/workspace}"

mkdir -p "$GRADLE_USER_HOME/init.d"
cp "$WORKSPACE/.cnb/scripts/gradle-mirror.init.gradle" "$GRADLE_USER_HOME/init.d/"
echo "[gradle] 已安装 init.d/gradle-mirror.init.gradle"

GP="$GRADLE_USER_HOME/gradle.properties"
if ! grep -q "repository.max.retries" "$GP" 2>/dev/null; then
  cat >> "$GP" <<'EOF'

# 由 .cnb/scripts/setup-gradle.sh 写入。
# Maven Central 对 CNB 的共享出口 IP 限流（429），让 Gradle 多试几次再放弃。
systemProp.org.gradle.internal.repository.max.retries=6
systemProp.org.gradle.internal.repository.initial.backoff=1000
EOF
  echo "[gradle] 已写入重试属性"
fi
