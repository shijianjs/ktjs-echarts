# todo

跨会话进度跟踪。这里只写**当前状态、下一步和仍然生效的约定/坑**；每批完成后更新本文件，历史看
`git log`。项目约定见 `AGENTS.md`、`TYPE-MAPPING.md` 与 `demo/AGENTS.md`，不在这里重复。

## 上下文

- 仓库：`ktjs-echarts`（`echarts` 与 `echarts-gl` 的 Kotlin/JS wrapper + demo 站点）。
- 上游资料一律读本地克隆（不要抓 GitHub / raw）：
  - `S:\workspace\github\apache\echarts`（6.1.0 附近的源码树）
  - `S:\workspace\github\apache\echarts-doc`（`option-gl/` 与 `option/partial/` 是 GL 选项文档）
  - `S:\workspace\github\apache\echarts-examples`（`public/examples/ts/` 是 demo 的移植来源）
  - `S:\workspace\github\ecomfe\echarts-gl`（GL 的 `defaultOption` 与视图实现）
  - `S:\workspace\github\JetBrains\kotlin-wrappers`（写法参考）
- 锁定版本：根 `package.json`（`echarts` 6.1.0、`echarts-gl` 2.1.0）；修订号在
  `wrapper-revisions.properties`（当前 `ktjs-echarts=4`、`ktjs-echarts-gl=2`）。
- 验证：`./gradlew build`（含 detekt）、`:ktjs-echarts:jsBrowserTest`、`:ktjs-echarts-gl:jsBrowserTest`、
  `:demo:e2eTest`。

## 当前状态

### 类型建模（ktjs-echarts，6.1.0-kt.4）

已完成 10 轮收尾（联合类型、字面量、数据项类型、`typedData` 视图、回调与事件 payload 等），
**没有未完成项**。下面这些是已确认「保持现状」的决定，不要重复修：

- `LegendOption` 保留可滚动图例的分页字段：`EChartsOption.legend` 是 `Array<LegendOption>`，拆子类型后
  调用方拿不到分页字段；属有意的更宽建模。
- `LegendDataItemOption.tooltip`、`CustomSeriesRenderItemParams.itemPayload` / `context` 的值用 `JsAny`：
  上游就是 `unknown` / `Dictionary<unknown>`。
- `MapSeriesOption.markPoint` / `markLine` / `markArea`、`ComposeOption` / `RegisteredSeriesOption`、
  上游的 `*ComponentOption` 别名、`ActionInfo.update`、`Payload.type`：Kotlin 侧统一用具名类型 / 更宽类型，
  理由见各接口 KDoc。
- series 级 `itemStyle` / `label` 上游的 `Omit<...> & { ... }` 收窄不另加 `typedItemStyle`：精确类型本身
  是基类型的子接口，调用方 `unsafeJso<精确类型> { }` 即可写，读回来按需 `unsafeCast`。
- `coordinateSystem` 等关键字收窄、继承来的 `var` 收窄沿用 `@JsName` 同名字段（`TYPE-MAPPING.md` §12）。

### ktjs-echarts-gl（2.1.0-kt.2）

- 已建模：`grid3D` / `xAxis3D|yAxis3D|zAxis3D`、`viewControl`、`light`、`postEffect`、shading 与材质，
  `bar3D` / `line3D` / `scatter3D` / `surface` 四个系列的选项、数据项联合与工厂（`*Series { }`），
  字面量（坐标系统、shading、投影、blendMode、`onGridPlane`、鼠标键、画质）。
- 安装器齐全（`Bar3DChart`、`Line3DChart`、`Scatter3DChart`、`SurfaceChart`、`GlobeComponent`…）；
  `echarts-gl` 的 `sideEffects` 列不全，安装器 getter 里读取 `echarts-gl/lib/echarts-gl.js` 来加载核心模块。
- 缺：`geo3D`、`globe`、`mapbox3D`、`maptalks3D`、`lines3D`、`polygons3D`、`map3D`、`scatterGL`、
  `graphGL`、`flowGL`、`linesGL` 的选项类型（这些页面本身也多半需要地图数据或外部 JS）。

### demo（286 个示例 / 27 个家族）

- 各家族基本铺满；第三十四批之后的「无外部依赖」候选已清空，剩下的要么需要真实键鼠序列，要么需要
  外部 JS / GeoJSON / 图片素材（原因见 `demo/AGENTS.md`）。
- `gl` 分组共 7 页（`examples/Gl.kt`）：`bar3d-punch-card`、`transparent-bar3d`、`bar3d-dataset`、
  `line3d-orthographic`、`simple-surface`、`sphere-parametric-surface`、`surface-wave`；
  `main()` 里 `useEChartsGl()` 注册 `Bar3DChart` / `Line3DChart` / `SurfaceChart` / `Grid3DComponent`。
- 可复用数据：`punchCardValues` / `punchCardHours` / `punchCardDays`（`CustomExtra.kt`）、
  `resources/data/life-expectancy-table.json`（`dataset-encode1` 与 `bar3d-dataset`）。默认色带
  （11 色 `visualMap`）在 `Gl.kt` 的 `glPalette` / `glVisualMap()`。

## 下一步（GL 横向铺开）

1. **`scatter3D` 三页**（wrapper 类型已就绪，只差 demo）：`scatter3d`、`scatter3d-scatter`、
   `scatter3D-dataset`（`grid3D` + `scatter3DSeries`，注册 `Scatter3DChart`）。
2. **纹理曲面**：`metal-surface`、`surface-breather`、`surface-golden-rose`、`surface-leather`、
   `surface-mollusc-shell`、`surface-theme-roses`、`parametric-surface-rose`、`image-surface-sushuang`
   ——都要 `image://` 纹理图，先把图片放进 `resources/data/img/`（已有该目录）再搬。
3. **之后**：`geo3D` / `globe-*` 需要先补 `Geo3DOption` / `GlobeOption` 与地图数据，成本最高，放最后。
4. **暂不做**：`*-simplex-noise`（需要 `simplex-noise` 外部库）、`*-mapbox*` / `*-maptalks*`（页面要额外
   加载地图 JS）、依赖官方站点图片纹理但拿不到原图的示例。

## 仍然生效的坑

### GL

- GL 图表只画在 claygl 的 canvas 上，没有 zrender 的 canvas 层；e2e 只能用 `assertChartRendered` +
  「canvas 有 WebGL 上下文且没有 `.ecgl-nowebgl`」的断言，**不要**把 GL 路由加进 `paintedFrame` /
  `paintedPixels`。
- 安装器必须在首个 `ECharts.init` 之前 `use`（demo 在 `main()` 里统一 `useEChartsGl()`），新增图表要加到
  那个数组。
- 系列 label 用**扁平**文本属性（`GlLabelOption` = `LabelOption` 的别名）：2.1.0 用 ECharts 6 的
  `createTextStyle`，`backwardCompat` 只展平 `axisLabel.textStyle`，系列 label 不展平。
- `surface.equation.z` / `parametricEquation.*` 返回 `Double`，上游用 `'-'` 表示无效点，Kotlin 侧用
  `Double.NaN`（`SurfaceView` 遇到 NaN 顶点整片四边形不画）。
- 空的 `viewControl` / `grid3D` 不要为了「对齐上游」写出来，只在真的设了字段时写。
- `echarts-gl` 没有 TypeScript 声明，选项按 `echarts-doc` 的 `option-gl/` 与源码 `defaultOption` 建模。

### demo 通用

- 内联 option 时注意 `unsafeJso` 的隐式接收者：局部变量 / 参数 / 字段与成员同名（`width`、`style`、
  `name`、`value`…）会变成自我赋值且照样编译，改名即可。
- 轴 / 系列的 `data` 用 `strings(...)` / `numbers(...)` / `points(...)` / `items(...)`；`numbers` 收
  `DoubleArray`，自己造数组要写 `DoubleArray { }`。`one(...)` 不接可空元素类型，用
  `arrayOf<T?>(...)`。
- 数据项 `name` 是 `string | number`、`value` 是 `string | number | Date`：用 `OptionId` /
  `OptionDataValue`，helper 在 `examples/Items.kt`。
- JSON 里「看着像数字」的列可能是字符串（plotly 导出），用 `Number(...)` 转换，不要直接 `as Double`。
- 富文本里只有图片没有文字的 tag 必须给 `width`，否则图片被画进零宽盒子。
- 不要用 PowerShell 的 `Set-Content` 改源码（ANSI 落盘会写坏 UTF-8 中文），批量改写用 Node/Python。
- 回调用 `js("(params) => ...")` 时不要返回对象字面量（`=> ({...})` 会被吃掉括号），写
  `function (a) { return {...}; }`。

## 收尾清单

- `./gradlew build`、`:ktjs-echarts-gl:jsBrowserTest`、`:demo:e2eTest` 全过；截图目视至少一个新 GL 页面。
- 改了 wrapper 公开代码就提升对应模块的修订号并跑 `./gradlew kotlinUpgradePackageLock --rerun-tasks`，
  提交锁文件（增量构建下该任务会误报 UP-TO-DATE）。
- 每批一个 `feat(...)` 提交并推送到 `origin/main`。
