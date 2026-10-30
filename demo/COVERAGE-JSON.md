# JSON 模式示例覆盖

demo 的顶栏可以在 Kotlin 与 JSON 两种代码源之间切换。Kotlin 模式运行
`demo/src/jsMain/kotlin/demo/examples/` 里用 ktjs-echarts 类型化 API 构建的配置；JSON 模式运行
`demo/src/commonMain/kotlin/demo/json/examples/` 里用纯 KMP 的 `kmp-echarts-json` 模型构建、再序列化成
JSON 后交给 `setOption` 的配置。

两条链路共享同一个官方示例 id 与路由，所以同一张图表可以并排比较两种写法；源码链接也会随模式切换到对应
文件。

统计由 `node scripts/json-coverage.mjs --write` 生成：

<!-- coverage-json:begin -->

## 统计

| 项 | 数量 |
| --- | --- |
| 静态页面（`chart(...)`） | 243 |
| 已提供 JSON 示例 | 190 |
| 明确不做 | 53 |
| 交互页面（`interactive(...)`，不适用） | 141 |
| JSON 示例总数 | 190 |

目标集合是 `chart(...)` 静态页面：顶栏的 Kotlin/JSON 开关只对它们生效。`interactive(...)` 页面由回调、定时器或用户操作驱动，没有 JSON 对应物。

## 明确不做（53）

### GL / 3D（18）

`echarts-gl` 的 3D 选项没有建模，JSON 模型只覆盖内置 2D 系列。

| id |
| --- |
| `bar3d-punch-card` |
| `globe-atmosphere` |
| `globe-displacement` |
| `globe-echarts-gl-hello-world` |
| `globe-layers` |
| `globe-moon` |
| `graphgl-gpu-layout` |
| `line3d-orthographic` |
| `metal-surface` |
| `parametric-surface-rose` |
| `simple-surface` |
| `sphere-parametric-surface` |
| `surface-breather` |
| `surface-golden-rose` |
| `surface-mollusc-shell` |
| `surface-theme-roses` |
| `surface-wave` |
| `transparent-bar3d` |

### custom 系列回调（8）

`series.type = "custom"` 必须提供 `renderItem` 函数，JSON 无法表达回调。

| id |
| --- |
| `custom-bar-trend` |
| `custom-calendar-icon` |
| `custom-cartesian-polygon` |
| `custom-error-bar` |
| `custom-error-scatter` |
| `custom-polar-heatmap` |
| `custom-profit` |
| `cycle-plot` |

### matrix 组件（11）

ECharts 6 的 `matrix` 组件（x/y 轴、单元格合并）尚未建模。

| id |
| --- |
| `matrix-confusion` |
| `matrix-correlation-heatmap` |
| `matrix-correlation-scatter` |
| `matrix-covariance` |
| `matrix-graph` |
| `matrix-grid-layout` |
| `matrix-pie` |
| `matrix-simple` |
| `matrix-mini-bar-data-collection` |
| `matrix-sparkline` |
| `doc-example/matrix-graph-box` |

### 时间轴 / 动画循环（6）

页面由 `timeline` 或 `setInterval` 持续驱动，静态 JSON 只能画出第一帧。

| id |
| --- |
| `doc-example/mix-timeline-all` |
| `doc-example/timeline-dynamic-series` |
| `mix-timeline-finance` |
| `graphic-loading` |
| `graphic-stroke-animation` |
| `watermark` |

### 函数 / 图形回调（3）

数据或图形由函数生成，没有 JSON 形式：`line-function` 用闭包算曲线，`line-graphic` 用 graphic 元素拼图。

| id |
| --- |
| `line-function` |
| `line-graphic` |
| `bar-animation-delay` |

### 坐标轴断点（3）

`axis.breaks` / `breakArea` / `breakLabelLayout` 尚未建模，页面核心就是断轴。

| id |
| --- |
| `bar-breaks-simple` |
| `intraday-breaks-1` |
| `intraday-breaks-2` |

### 官方站点图片 / 纹理（1）

图表依赖官方站点托管的图片或纹理资源，站点不搬运。

| id |
| --- |
| `pie-rich-text` |

### SVG 地图资源（3）

页面先注册 SVG 地图并依赖 `geo` 的 SVG 布局，站点没有这些地图数据。

| id |
| --- |
| `doc-example/geo-svg-label-basic` |
| `doc-example/geo-svg-layout-basic` |
| `doc-example/geo-svg-named-basic` |

## 待补（0）

每个静态页面要么有 JSON 示例，要么在上面列了理由；出现新的 `chart(...)` 页面而没有对应示例时脚本会报错。

<!-- coverage-json:end -->

## 约定

- JSON 示例的 id 必须与 demo 的 `chart("id")` 路由一致，不能只覆盖 `interactive("id")` 页面。
- 示例只使用 `kmp-echarts-json` 已声明的字段；没有建模的选项要么补模型，要么在文件头注明省略。
- 数据必须是确定性的：上游示例里的 `Math.random()` 一律换成固定值，保证每次访问画出同一张图。
- 无法 JSON 化的页面（GL/3D、custom `renderItem`、`matrix` 组件、时间轴/动画循环、图片或地图资源、
  坐标轴断点）在脚本的 `EXCLUDED` 里登记并写明理由。
