#!/usr/bin/env bash
# 把 NPC 会话的成果推回 GitHub。
#
# 放在 npc:go 之后、同一条流水线里，而不是靠「NPC 推送会不会触发 push 事件」
# 这种尚未验证的行为——同一条流水线里推，链路是确定的。
#
# 需要由密钥仓库 imports 注入：
#   GITHUB_TOKEN   对目标仓库有写权限的 PAT（fine-grained，只勾 Contents: Read and write）
# 可选：
#   GITHUB_REPO    默认 shijianjs/ktjs-echarts
#   GIT_USER_NAME / GIT_USER_EMAIL   兜底提交用的作者信息
set -euo pipefail

: "${GITHUB_TOKEN:?缺少 GITHUB_TOKEN：检查 .cnb.yml 里这一步的 imports 是否指向密钥仓库}"

GITHUB_REPO="${GITHUB_REPO:-shijianjs/ktjs-echarts}"
WORKSPACE="${CNB_BUILD_WORKSPACE:-/workspace}"
cd "$WORKSPACE"

# NPC 偶尔只改文件不提交，这里兜底一次。
if [ -n "$(git status --porcelain)" ]; then
  git config user.name  "${GIT_USER_NAME:-cnb-npc}"
  git config user.email "${GIT_USER_EMAIL:-npc@cnb.cool}"
  git add -A
  git commit -m "chore: NPC 自动提交" || true
fi

BRANCH="$(git rev-parse --abbrev-ref HEAD)"

# NPC 不应直接改默认分支。真出现了就把它挪到独立分支，
# 免得一次 AI 提交直接覆盖 GitHub 上的 main。
if [ "$BRANCH" = "HEAD" ] || [ "$BRANCH" = "${CNB_DEFAULT_BRANCH:-main}" ]; then
  BRANCH="npc/$(date +%Y%m%d-%H%M%S)"
  git checkout -b "$BRANCH"
  echo "[push] 当前在默认分支上，改推到 $BRANCH"
fi

# 先推 CNB（origin 的凭据由平台注入，NPC 工作模式下有写权限）。
git push origin "HEAD:refs/heads/$BRANCH"

# 再推 GitHub。用 token 内联到 URL，避免把凭据写进 .git/config 的 remote。
git remote remove github >/dev/null 2>&1 || true
git remote add github "https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPO}.git"
git push github "HEAD:refs/heads/$BRANCH"

echo "[push] 已同步：https://github.com/${GITHUB_REPO}/tree/${BRANCH}"
