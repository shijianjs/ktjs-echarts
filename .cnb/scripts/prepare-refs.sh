#!/usr/bin/env bash
# 把上游参考仓库准备到 /refs 下，供 NPC 对照阅读。
#
# /refs 在 NPC 工作区之外（CNB_BUILD_WORKSPACE=/workspace），所以
#   - 不会被 git status 看到，不会被误提交；
#   - 不会被 wrapper 的构建脚本扫到；
#   - 可以用 volumes 单独缓存（见 .cnb.yml）。
#
# 环境变量：
#   REFS_DIR         参考仓库根目录，默认 /refs
#   REFS_ORIGIN      参考仓库来源：cnb（默认，走 CNB 内网镜像）或 github
#   CNB_REF_TOKEN    读取 CNB 上私有镜像仓库的只读令牌；公开仓库可留空
#   REFS_ECHARTS_SLUG        echarts 在 CNB 上的路径，默认 apache/echarts
#   REFS_EXAMPLES_SLUG       echarts-examples 在 CNB 上的路径，默认 apache/echarts-examples
#
# 只取默认分支的最新一份快照（--depth 1）：NPC 只需要「上游现在长什么样」，
# 不需要历史。需要看某个历史版本时让它自己 git log / fetch 那一个 commit。
set -euo pipefail

REFS_DIR="${REFS_DIR:-/refs}"
ORIGIN="${REFS_ORIGIN:-cnb}"
HOST="${CNB_WEB_HOST:-cnb.cool}"

mkdir -p "$REFS_DIR"

cnb_url() { # $1 = owner/repo
  if [ -n "${CNB_REF_TOKEN:-}" ]; then
    printf 'https://oauth2:%s@%s/%s.git' "$CNB_REF_TOKEN" "$HOST" "$1"
  else
    printf 'https://%s/%s.git' "$HOST" "$1"
  fi
}

sync_repo() { # $1 = 本地目录名  $2 = CNB slug  $3 = GitHub slug
  local dir="$REFS_DIR/$1" url
  if [ "$ORIGIN" = "github" ]; then
    url="https://github.com/$3.git"
  else
    url="$(cnb_url "$2")"
  fi

  if [ -d "$dir/.git" ]; then
    echo "[refs] 更新 $1（$(git -C "$dir" rev-parse --short HEAD)）"
    git -C "$dir" remote set-url origin "$url"
    git -C "$dir" fetch --depth 1 --quiet origin
    # /refs 的卷是可写的（copy-on-write），NPC 在里面留下的改动会随流水线
    # 成功合并进缓存。reset + clean 把上一轮的残留清干净。
    git -C "$dir" reset --hard --quiet FETCH_HEAD
    git -C "$dir" clean -xfdq
  else
    echo "[refs] 克隆 $1"
    git clone --depth 1 --single-branch --quiet "$url" "$dir"
  fi
  echo "[refs] $1 -> $(git -C "$dir" rev-parse --short HEAD)"
}

sync_repo echarts          "${REFS_ECHARTS_SLUG:-apache/echarts}"          apache/echarts
sync_repo echarts-examples "${REFS_EXAMPLES_SLUG:-apache/echarts-examples}" apache/echarts-examples

# 写一份路径说明，NPC 读得懂，人排查时也省事。
cat > "$REFS_DIR/README.txt" <<'EOF'
这里是只读的上游参考仓库，不属于本仓库，不要修改、不要提交。

  /refs/echarts            apache/echarts，本 wrapper 对应的 npm 包源码
  /refs/echarts-examples   apache/echarts-examples，官方示例

只保留默认分支的最新一份快照（depth 1），没有历史。
EOF
