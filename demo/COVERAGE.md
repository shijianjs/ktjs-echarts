# 示例覆盖

演示站点对官方 [ECharts 示例库](https://github.com/apache/echarts-examples) 的覆盖账本。统计对象是本地
克隆的 `echarts-examples`：清单取 `src/data/chart-list-data.js`（377 条）与
`chart-list-data-gl.js`（59 条），源码在 `public/examples/ts/`。

「已移植」按 demo 的路由 id 判断：路由 id 就是官方清单里的 id，一个页面恰好对应一条清单条目；
`bar-large` / `pie-highlight` 两个 `doc-example/` 条目按家族 id 落地，也算作已移植。每页的源码路径在
`demo/src/jsMain/kotlin/demo/examples/` 里，页面元数据和侧边栏目录会带官方链接，这里不重复列。

## 盘点

<!-- coverage:begin -->

## 统计

| 项 | 数量 |
| --- | --- |
| 官方清单 | 436 |
| 　· `chart-list-data.js` | 377 |
| 　· `chart-list-data-gl.js` | 59 |
| 已移植 | 384 页 / 385 条 |
| 明确不做 | 51 |
| 待补 | 0 |

已移植数里的「页」不含总览页（`#/overview`）；「条」把 `bar-large` / `pie-highlight` 同时算作对应 `doc-example/` 条目的移植。

## 明确不做（51）

### 百度地图（bmap）（9）

wrapper 不覆盖百度地图 SDK，按选例策略排除。

| id |
| --- |
| `effectScatter-bmap` |
| `global-wind-visualization` |
| `global-wind-visualization-2` |
| `heatmap-bmap` |
| `lines-bmap` |
| `lines-bmap-bus` |
| `lines-bmap-effect` |
| `map-bin` |
| `map-polygon` |

### 外部 JS 库生成数据（19）

数据由 ecStat / d3 在浏览器里算出来（GL 页经 CDN 的 `$.getScript` 加载 simplex-noise），站点不引这些包。

| id |
| --- |
| `bar-histogram` |
| `data-transform-aggregate` |
| `scatter-clustering` |
| `scatter-clustering-process` |
| `scatter-exponential-regression` |
| `scatter-linear-regression` |
| `scatter-logarithmic-regression` |
| `scatter-polynomial-regression` |
| `circle-packing-with-d3` |
| `animating-contour-on-globe` |
| `globe-contour-paint` |
| `map-usa-projection` |
| `bar3d-noise-modified-from-marpi-demo` |
| `bar3d-simplex-noise` |
| `flowGL-noise` |
| `metal-bar3d` |
| `scatter3d-orthographic` |
| `scatter3d-simplex-noise` |
| `stacked-bar3d` |

### 官方站点图片 / 纹理素材（10）

图标、纹理或 base64 图只存在于官方站点，按选例策略不搬运。

| id |
| --- |
| `doc-example/axisPointer-handle-image` |
| `doc-example/pictorialBar-graphicType` |
| `doc-example/pictorialBar-patternSize` |
| `doc-example/pictorialBar-repeatDirection` |
| `doc-example/pictorialBar-repeatLayout` |
| `pictorialBar-forest` |
| `pictorialBar-hill` |
| `pictorialBar-spirit` |
| `pie-pattern` |
| `iron-globe` |

### 依赖真实指针 / 触摸交互（9）

页面演示的就是拖拽、触摸或刷选本身，内容要用户操作后才出现；静态页面与 e2e 无法稳定复现。

| id |
| --- |
| `bar-breaks-brush` |
| `candlestick-brush` |
| `custom-gantt-flight` |
| `line-draggable` |
| `line-fisheye-lens` |
| `line-pen` |
| `scatter-map-brush` |
| `doc-example/geo-roam-indicator` |
| `doc-example/graph-roam-indicator` |

### 站点能力未提供（2）

canvas-vs-svg 需要先给站点加渲染器开关才有意义。

| id |
| --- |
| `doc-example/canvas-vs-svg` |
| `doc-example/canvas-vs-svg-en` |

### 运行期远端脚本（1）

示例用 `$.getScript` 拉官方站点的 timelineGDP.js 与 draggable.js。

| id |
| --- |
| `doc-example/bar-media-timeline` |

### 实例内部结构（1）

靠 getModel().getComponent('geo').coordinateSystem.regions 取区域中心，wrapper 不建模；world.json 也只有 6 个国家带 cp。

| id |
| --- |
| `globe-country-carousel` |

## 待补（0）

<!-- coverage:end -->

## 清单之外

- `public/examples/ts/gl/` 有 72 个源文件，其中 13 个没有进 `chart-list-data-gl.js`，不计入上面的 436：
  `bar3d-mapbox-view-change`、`bar3d-music-visualization`、`bar3d-on-mapbox`、`bar3d-shanghai`、
  `lines3d-flight-path-on-mapbox`、`lines3d-taxi-chengdu`、`lines3d-taxi-routes-of-cape-town`、
  `map3d-buildings-on-mapbox`、`map3d-colorful-cities`、`map3d-plastic-material-style-buildings-on-mapbox`、
  `map3d-wood-map-on-mapbox`、`scatter3d-shanghai`、`scatter3d-weibo-checkin-on-mapbox`。
  它们依赖 Mapbox / maptalks、Web Audio 或官方站点 `/asset/get/s/` 的素材，同样不做。
- `public/examples/ts/archive/` 的 6 个过渡动画示例已归档，官方清单不收录，也不计入。

## 维护

```bash
node scripts/demo-coverage.mjs           # 统计并校验分类
node scripts/demo-coverage.mjs --write   # 重新生成「盘点」一节
```

脚本默认读 `../../apache/echarts-examples`（相对仓库根目录），其他位置用 `ECHARTS_EXAMPLES` 指定。

- 新移植一页后运行 `--write`，对应 id 会自动从「待补」里消失；仍留在脚本分类里的条目会被忽略并提示。
- 「待补」已随 `geo-lines` 清零：官方清单 436 条现在要么已移植，要么在「明确不做」里。以后 `--write`
  再报出待补条目，说明上游清单新增了 id。- 新判定为「不做」的条目，加进 `scripts/demo-coverage.mjs` 的 `EXCLUDED` 并写清整组理由；没有分类的
  新 id 会让脚本报错。
- 官方清单升级后，新 id 会以同样的方式暴露出来。

选例策略（为什么是这些「不做」）见 [AGENTS.md](AGENTS.md#选例范围)；跨示例的注意事项见
[NOTES.md](NOTES.md)。
