#!/usr/bin/env bash
# 冷启动时预热 Gradle 依赖。
#
# 为什么需要：CNB 的出口 IP 是共享的，Maven Central 会对它限流
# （`429 Too Many Requests`）。首次解析 kotlin-scripting-compiler-embeddable
# 这类依赖时经常失败一次，重跑就好——但 NPC 会话里没人替它重跑。
# 这里在 AI 动手之前先跑一遍，失败就重试。
#
# 只在缓存不存在时执行：volumes 命中时直接跳过，不拖慢常规构建。
# 故意不用 `set -e`：预热失败不应该让整条流水线失败，NPC 自己还能重试。
set -uo pipefail

GRADLE_USER_HOME="${GRADLE_USER_HOME:-/root/.gradle}"
WORKSPACE="${CNB_BUILD_WORKSPACE:-/workspace}"
cd "$WORKSPACE"

if [ -d "$GRADLE_USER_HOME/caches/modules-2" ]; then
  echo "[warmup] Gradle 依赖缓存已存在，跳过预热"
  exit 0
fi

for attempt in 1 2 3; do
  echo "[warmup] 第 $attempt 次预热 :ktjs-echarts:compileKotlinJs"
  if ./gradlew :ktjs-echarts:compileKotlinJs; then
    echo "[warmup] 预热成功"
    exit 0
  fi
  if [ "$attempt" -lt 3 ]; then
    echo "[warmup] 失败，20 秒后重试（多半是 Maven Central 限流）"
    sleep 20
  fi
done

echo "[warmup] 三次都失败，交给 NPC 自己处理" >&2
exit 0
