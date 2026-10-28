#!/usr/bin/env bash
# 把 NPC 会话的成果推回 CNB。**只推 CNB，不碰 GitHub** —— GitHub 同步由 push 事件上
# 的 git-sync 负责，只有那条流水线持有 GitHub 凭证。
#
# 为什么需要这一步：NPC 工作模式会建分支、commit，但「推不推」并不保证。实测在任务里
# 写「不要自己 push」，改动就只留在容器里，容器销毁即消失。这里补一道兜底，
# 保证分支一定落到 CNB，进而触发同步。
#
# 只读会话直接跳过：CNB_TOKEN 没有 repo-code:rw，push 会 403。
set -euo pipefail

if [ "${CNB_NPC_ENABLE_WORKMODE:-false}" != "true" ]; then
  echo "[push] 只读会话（work_mode=${CNB_NPC_ENABLE_WORKMODE:-unset}），跳过"
  exit 0
fi

cd "${CNB_BUILD_WORKSPACE:-/workspace}"

# NPC 偶尔只改文件不提交。
if [ -n "$(git status --porcelain)" ]; then
  git config user.name  "${GIT_USER_NAME:-cnb-npc}"
  git config user.email "${GIT_USER_EMAIL:-npc@cnb.cool}"
  git add -A
  git commit -m "chore: NPC 自动提交" || true
fi

# 没有新提交就不用推，免得在 CNB 上留空分支。
if [ -n "${CNB_COMMIT:-}" ] && [ "$(git rev-parse HEAD)" = "$CNB_COMMIT" ]; then
  echo "[push] HEAD 与触发时的提交一致，没有新提交，跳过"
  exit 0
fi

BRANCH="$(git rev-parse --abbrev-ref HEAD)"
git push origin "HEAD:refs/heads/${BRANCH}"
echo "[push] 已推到 CNB 的 ${BRANCH}，接下来由 push 事件同步到 GitHub"
