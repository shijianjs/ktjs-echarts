# demo

官方 [ECharts 示例库](https://github.com/apache/echarts-examples) 的 Kotlin/JS 移植站点：每一页对应
`public/examples/ts` 里的一个示例，路由 id 与官方示例 id 完全一致（`#/bar-simple` ⇄
`https://echarts.apache.org/examples/zh/editor.html?c=bar-simple`）。这一层只消费 wrapper 的公开 API，
不导入 `ktjs-echarts` 内部实现。

## 目录结构

```
demo/src/jsMain/kotlin/demo/
  Main.kt            入口：先定语言与主题，再建 shell，最后启动路由
  Language.kt        中英双语与 LanguageState（localStorage 记忆）
  Theme.kt           亮/暗主题（<html data-theme>，localStorage 记忆）
  Router.kt          hash 路由
  AppShell.kt        侧边栏目录 + 顶栏 + 内容区；页面缓存、图表随页面挂载/卸载
  Catalog.kt         Groups、CatalogEntry 与整份目录的汇总
  ui/Dom.kt          类型安全的 DOM 构造与小工具
  ui/Blocks.kt       页面骨架（page / pageSection / example）与 PageMeta / PageView
  ui/Chart.kt        ChartHolder：容器的持有者，负责 ECharts 实例的挂载、卸载与 resize
  examples/          一个图表家族一个文件，每份导出 <name>Entries
demo/src/jvmTest/kotlin/demo/e2e/
  DemoPageTest.kt    Playwright 端到端套件（点侧边栏、切语言/主题、过滤索引、抽样渲染）
  DemoOptions.kt     浏览器与断言超时设置
  DemoServer.kt      测试内静态服务器
```

## 移植约定

- **一页一示例、id 与官方一致**：路由 id 就是官方示例 id，页面上的「官方示例」链接由此拼出。
- **每个示例一族一文件**：`examples/Bar.kt`、`examples/Pie.kt`…；文件里先用
  `Category(Groups.X, "examples/X.kt")` 绑定分组与源码路径，再用 `chart(...)` /`interactive(...)`
  声明示例。文件内的声明顺序就是侧边栏顺序。
- **静态示例用 `chart { option }`**：选项在挂载时构造（懒构造，避免建页时白算数据）。
  **需要自己驱动图表的用 `interactive { chart -> }`**：定时动画、`getWidth()` 相关布局、事件等。
- **中文标题抄官方清单**（`echarts-examples/src/data/chart-list-data.js` 的 `titleCN`），
  英文标题抄 `title`；这样侧边栏与官方站点一致，也便于脚本核对。
- **数值数据原样搬运**：官方示例里的数组、表格直接写进 Kotlin；`Math.random()` 一律换成固定序列
  （见各文件的 KDoc），保证页面可复现、端到端断言稳定。
- **通配导入**：`examples/` 下的文件统一用 `import echarts.options.*` / `import echarts.*`。一个示例
  通常触及大半个选项包，逐个列举反而更容易漏；`detekt` 的规则集（只有 `ForbiddenImport`）不禁止它。
- **数据项名称与取值走联合工厂**：`name` 是 `string | number`、`value` 是 `string | number | Date`，
  所以写 `OptionId("x")`、`OptionDataValue(1.0)`；`examples/Items.kt` 收了这批常用构造器
  （`pieItem`、`radarItem`、`row`、`doubleRow`、`recordOf`、`toolbox`、`every`…）。

## 选例范围

目标是「不依赖外部资源的纯 option 示例」。用脚本按 `chart-list-data.js` 逐条判断，排除：

- 需要地图 GeoJSON / SVG 的（`map`、`geo` 分类，`registerMap`），
- 需要百度地图的（清单里 `tags: ["bmap"]`），
- 需要 `echarts-gl` 的（`public/examples/ts/gl`，wrapper 没有覆盖），
- 运行时 `$.get` / `XMLHttpRequest` / `fetch` 取数的（`ROOT_PATH`、`*.json`、`*.bin`），
- 依赖官方站点图片素材的（`image://` 图标、纹理贴图、`data:image/...` 大块 base64），
- 依赖真实键鼠序列的（拖拽、触摸、`sendInput`）。

筛选后仍有 250+ 个候选，目前已落地 **109 个**（覆盖 23 个家族），其余按批推进。

### 未移植的家族与原因

| 家族 | 情况 |
| --- | --- |
| `lines`（路径图） | 现存示例全部依赖 `flights.json` 或 `.bin` 分片 |
| `map` / `geo` | 需要 GeoJSON，按约定排除 |
| `bmap` / `echarts-gl` | 需要百度地图或 WebGL 渲染器，wrapper 没有覆盖 |
| `graphic` | `EChartsOption.graphic` 已能表达组件与元素，但官方 `graphic` 家族的多数示例还配合
  真实交互或外部素材，未成批移植 |
| `visualMap` / `dataZoom` 单列家族 | 官方清单里这几条恰好都属上面被排除的类别；能力已由其它示例覆盖 |

同样的原因，个别被移植家族内的长尾示例（大数据量、动画竞速、下钻、极坐标变体、真实指针交互）也留待
下一批，例如 `bar-race`、`bar-drilldown`、`line-easing`、`scatter-clustering`、`candlestick-sh`、
`custom-gauge`、`custom-polar-heatmap`（需要极坐标中心 `params.coordSys.cx/cy`）、`matrix-sparkline`
（25 个网格落在矩阵上，需要 `dataZoom` 覆盖全部 x 轴）。

## 移植过程中修掉的 wrapper 缺口

移植是 wrapper 的一次实测；以下缺口已在 `ktjs-echarts` 里补齐（见该模块的 KDoc）：

- `SplitLineOption.lineStyle` / `SplitAreaOption.areaStyle`：上游是
  `LineStyleOption<ZRColor | ZRColor[]>` / `AreaStyleOption<ZRColor[]>`，新增
  `SplitLineStyleOption` / `SplitAreaStyleOption` 与 `ZRColorOrArray`。
- `PieSeriesOption`：补 `animationType` / `animationTypeUpdate`（新增
  `PieAnimationTypeUpdate` 字面量）与 `BoxLayoutOptionMixin`（`left`/`top`/`width`…）。
- `FunnelSeriesOption` / `SankeySeriesOption` / `ChordSeriesOption` / `ThemeRiverSeriesOption`：
  补上游声明的 `BoxLayoutOptionMixin`。
- `ChordSeriesOption.radius`：上游允许 `[inner, outer]`，放宽为 `StringOrDoubleOrArray`。
- `DataZoomOption`：补 `BoxLayoutOptionMixin`（上游只有 slider 变体带盒子布局，wrapper 合成了一个）。
- `EmphasisFocuses`：补 `adjacency` / `ancestor` / `descendant`（sankey、chord、graph、tree 专用取值）。
- `OptionEncode`：上游是开放索引签名，补上文档列出的坐标维度 `x` / `y` / `value` / `radius` / `angle`。
- `TreeSeriesOption.orient`：上游是 `TreeOrient`（`LR` / `TB` / `RL` / `BT` / `horizontal` / `vertical`
  或 `number`），之前错用了通用的 `Orient`；新增 `TreeOrient` / `TreeOrients`。
- `MatrixOption` 系列：`MatrixDimensionOption.data` 改成上游的 `MatrixDimensionCellLooseOption[]`（单元格
  对象或纯字符串），补 `MatrixCellStyleOption`（`label` / `itemStyle` / `cursor` / `silent` / `z2`）、
  `MatrixDimensionOption.levelSize`、`MatrixBodyCornerCellOption.coord`（`MatrixCoordRangeOption[]`）、
  `coordClamp`、递归的 `children`。
- `GridOption`：补 `ComponentOption` 继承来的 `coordinateSystem` / `coord`（矩阵布局的网格需要）。
- `EChartsOption.graphic`：从 `Array<GraphicElement>` 改成上游的 `GraphicComponentLooseOption[]`
  （`GraphicComponentOption` 带 `elements`，或裸元素），元素补 `x` / `y` / `width` / `height`。
- `CustomSeriesRenderItemAPI`：补 `coord` / `size` / `layout`（新增 `CoordinateSystemDataCoord` 联合），
  `params.coordSys` 补矩形布局的 `x` / `y` / `width` / `height`；`CustomSeriesOption` 补 `itemPayload`。
- `GraphicNamespace.clipRectByRect` / `clipPointsByRect`：参数与返回值从 `JsAny` 收紧到 `RectLike`。

## 兜底说明：图表与主题

ECharts 实例在**页面可见时才创建**（`ChartHolder.mount()`），换页时 `dispose()`；容器节点本身不重建。
`ECharts.init` 用的是 ECharts 自带主题名：暗色模式传 `"dark"`，所以切换主题会重建当前页面。

## 端到端测试

```bash
./gradlew :demo:e2eTest
```

它打包 `jsBrowserDistribution`，用 Playwright 驱动 Chromium：走侧边栏、切换语言与主题、过滤索引，
并**逐页**打开目录里的每个示例，断言画布有实际尺寸且控制台无报错。早先按 1/6 抽样，结果漏掉了
`themeRiver-lastfm`（表比专辑名长，构造 option 时越界）这类只在个别页出现的问题。`jvmTest` 本身被
禁用，以免 `build` / `check` 依赖浏览器。
