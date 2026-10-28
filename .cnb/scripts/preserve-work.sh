#!/usr/bin/env bash
# 无论 NPC 有没有跑完，都把工作区里的改动推出去。
#
# 为什么需要：npc:go 因 maxTurns 超限失败时，后面的 stage 会被 skipped，
# 改动留在容器里，容器一销毁就没了。实测有一次跑了 1h52m、500 轮后中止，
# 中间所有提交全部丢失。
#
# endStages 无论 stages 成败都会执行，用它兜底：
#   成功（CNB_PIPELINE_STATUS=success）→ 挪回 main 推上去，正常交付
#   失败 / 取消                       → 推 npc/wip-<时间戳>，保留现场，不污染 main
#
# 只读会话（没开工作模式）直接跳过：CNB_TOKEN 没有 repo-code:rw。
set -euo pipefail

if [ "${CNB_NPC_ENABLE_WORKMODE:-false}" != "true" ]; then
  echo "[preserve] 只读会话（work_mode=${CNB_NPC_ENABLE_WORKMODE:-unset}），跳过"
  exit 0
fi

cd "${CNB_BUILD_WORKSPACE:-/workspace}"

# NPC 可能改了一半就断了，先把未提交的改动收进来。
if [ -n "$(git status --porcelain)" ]; then
  git config user.name  "${GIT_USER_NAME:-cnb-npc}"
  git config user.email "${GIT_USER_EMAIL:-npc@cnb.cool}"
  git add -A
  git commit -m "wip: NPC 未完成的改动" || true
fi

if [ -n "${CNB_COMMIT:-}" ] && [ "$(git rev-parse HEAD)" = "$CNB_COMMIT" ]; then
  echo "[preserve] 没有新提交，跳过"
  exit 0
fi

CURRENT="$(git rev-parse --abbrev-ref HEAD)"
TARGET="${CNB_DEFAULT_BRANCH:-main}"
STATUS="${CNB_PIPELINE_STATUS:-unknown}"

if [ "$STATUS" = "success" ]; then
  # 正常完成：挪回默认分支推上去
  if [ "$CURRENT" != "$TARGET" ]; then
    echo "[preserve] 当前在 $CURRENT，挪到 $TARGET"
    git checkout "$TARGET"
    if ! git merge --ff-only "$CURRENT"; then
      echo "[preserve] 无法 fast-forward，改推 wip 分支" >&2
      STATUS="merge-conflict"
    fi
  fi
  if [ "$STATUS" = "success" ]; then
    git push origin "HEAD:refs/heads/${TARGET}"
    echo "[preserve] 已完成，推到 ${TARGET}，接下来由 push 事件同步到 GitHub"
    exit 0
  fi
fi

# 失败 / 取消 / 无法合并：现场留独立分支，main 保持干净
BRANCH="npc/wip-$(date +%Y%m%d-%H%M%S)"
git checkout -b "$BRANCH"
git push origin "HEAD:refs/heads/${BRANCH}"
echo "[preserve] 流水线状态 ${STATUS}，现场保留在 ${BRANCH}"
echo "[preserve] 捡回来：git fetch cnb && git checkout ${BRANCH}"
