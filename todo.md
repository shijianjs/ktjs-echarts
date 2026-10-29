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
  `wrapper-revisions.properties`（当前 `ktjs-echarts=4`、`ktjs-echarts-gl=7`）。
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

### ktjs-echarts-gl（2.1.0-kt.7）

- 已建模：`grid3D` / `xAxis3D|yAxis3D|zAxis3D`、`geo3D`、`globe`、`viewControl`、`light`、`postEffect`、
  shading 与材质，`bar3D` / `line3D` / `scatter3D` / `surface` / `lines3D` 五个系列的选项、数据项联合与工厂
  （`*Series { }`），字面量（坐标系统、shading、投影、blendMode、`onGridPlane`、鼠标键、画质、globe 图层
  类型与混合目标、`lines3D` 的 pause/resume/toggle 动作名）。`geo3D` / `globe` 补了核心会读但文档没写的
  `silent`，`bar3D` 补了同样只有源码会读的 `barSize`；`GlColor` 补了返回 HDR 数组
  `[r, g, b, a]` 的回调重载（字符串回调由 `ZRColor` 提供，表达不了随 data item 变的 HDR 颜色）。
- 安装器齐全（`Bar3DChart`、`Line3DChart`、`Scatter3DChart`、`SurfaceChart`、`GlobeComponent`…）；
  `echarts-gl` 的 `sideEffects` 列不全，安装器 getter 里读取 `echarts-gl/lib/echarts-gl.js` 来加载核心模块。
- 缺：`mapbox3D`、`maptalks3D`、`polygons3D`、`map3D`、`scatterGL`、`graphGL`、`flowGL`、`linesGL`
  的选项类型（这些页面本身也多半需要地图数据或外部 JS）。

### demo（313 个示例 / 27 个家族）

- 各家族基本铺满；剩下的候选要么需要真实键鼠序列，要么需要外部 JS / GeoJSON / 图片素材
  （原因见 `demo/AGENTS.md`）。
- `gl` 分组共 34 页：`examples/Gl.kt`（`bar3d-punch-card`、`transparent-bar3d`、`bar3d-dataset`、
  `image-surface-sushuang`、`bar3d-myth`、`line3d-orthographic`、`simple-surface`、
  `sphere-parametric-surface`、`surface-wave`）、`examples/GlScatter.kt`（`scatter3d`、`scatter3d-scatter`、
  `scatter3D-dataset`）、`examples/GlSurfaceMaterial.kt`（`metal-surface`、`surface-breather`、
  `surface-mollusc-shell`、`surface-golden-rose`、`surface-theme-roses`、`parametric-surface-rose`、
  `surface-leather`）、`examples/GlGeo.kt`（`geo3d`、`geo3d-with-different-height`、六页 `globe-*`、
  `scatter3d-globe-population`）、`examples/GlLines.kt`（`lines3d-airline-on-globe`、
  `lines3d-flights-on-geo3d`、`lines3d-flights`、`lines3d-flights-gl`）与 `examples/GlBarGeo.kt`（`bar3d-global-population`、
  `global-population-bar3d-on-globe`）；`main()` 的 `useEChartsGl()` 注册 `Bar3DChart` / `Line3DChart` /
  `Lines3DChart` / `Scatter3DChart` / `SurfaceChart` / `Grid3DComponent` / `Geo3DComponent` /
  `GlobeComponent`。
- 可复用数据：`punchCardValues` / `punchCardHours` / `punchCardDays`（`CustomExtra.kt`）、
  `resources/data/life-expectancy-table.json`、`resources/data/nutrients.json`、`population.json`、
  `resources/data/gl/flights.json`；
  11 色 `visualMap` 色带在 `Gl.kt` 的 `glPalette` / `glVisualMap()` 与 `GlSurfaceMaterial.kt` 的
  `surfaceRamp()`。
- 素材：`resources/data/img/gl/` 下是 HDR 环境图、皮革贴图、地球/月球/星空纹理与 `sample.jpg`（星云
  柱的像素来源），来源记在
  `data/SOURCE.md`；`resources/data/world.json` 与 `world-population.json` 供 `geo3D` 与 globe 页面使用，
  `world.json` 的缩写地名由 `GlGeo.kt` 的 `worldRegionNames` 表翻译。

## 下一步（GL 横向铺开）

1. **把 `bar3D` / `globe` 的剩余素材页铺开**：`bar3d-voxelize-image`、`image-to-bar3d`、
   `bar3d-music-visualization`（读音频或视频，体积先看）、`globe-country-carousel`（国家高亮轮播）。
   `lines3d-taxi-routes-of-cape-town` / `bar3d-shanghai` / `scatter3d-shanghai` 需要 mapbox 或 shapefile，
   归入不做。
2. **暂不做**：`animating-contour-on-globe`（d3-array/contour/geo/timer）、`*-simplex-noise` 与
   `stacked-bar3d` / `metal-bar3d`（都从 CDN 取 `simplex-noise` 生成数据，`metal-bar3d` 还要 `pisa.hdr`）、
   `*-mapbox*` / `*-maptalks*`（页面要额外加载地图 JS）、`iron-globe` 一类依赖官方站点 `/asset/get/s/`
   纹理的示例。

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
- 后期实现的旧键名 `SSAO` / `DOF` / `SSR` 在 wrapper 里是 `ssao` / `dof` / `ssr`（`@JsName` 映射）；
  两个键都设时上游读旧键。`textureTiling` / `textureOffset` 是 `DoubleOrDoubleArray`，成对平铺必须写
  `[u, v]`。
- 上游的死字段不要照抄：`exportGL2PLY` 已从 2.1.0 删除、`realisticMaterial.baseTexture` /
  `lambertMaterial.baseTexture` 无人读取（材质只认 `detailTexture`）、`toolbox.iconStyle.normal` 是废弃
  状态写法。
- `geo3D` 的 `regions[].height` 才是单个区域的高度，文档里的 `regionHeight` 是整幅地图的默认高度；
  `aspectScale` 与 `debug.wireframe` 没写进文档但会被读取。
- `bar3D` 的 `barSize` 同样文档没写（只在 `bar3DLayout` 里读），`geo3D` / `globe` 下还会再除以
  `getScale() / 16`，所以不是数据单位。`visualMap.inRange.color` 会写进系列的 `itemStyle.color`
  （`visualStyleAccessPath`），系列上写死的颜色被色带顶掉；只映射 `colorLightness` 时才保留原色。
- `Geo3DBuilder` 构造时会给**所有** shading 建一份地面材质，包括 `"shadow"`（映射到
  `ecgl.displayShadow`）；上游只从 mapbox3D / maptalks3D 视图注册那两个 chunk，所以 wrapper 在
  `Geo3DComponent` getter 里自己注册，否则每个 `geo3D` 页面刷 4 条 missing-shader 错误。
- 纹理可以是 ECharts 实例，但那个实例必须建在 canvas 上，否则控制台警告「must init on canvas」且取不到
  内容。图层纹理走 URL 时是异步加载，销毁后再回调会在 `api.getZr()` 上崩，测试里用 canvas 纹理。
- `world.json` 的地名是缩写（`Czech Rep.` / `Korea` / `Dem. Rep. Congo`…），`world-population.json` 与官方
  站点注册的 ECharts 4.9 世界地图用全称；按名字匹配的 `regions` / 地图 `data` 必须先翻译，否则这些国家
  静默没有值。
- 目前所有 planet/地图纹理都需要 `registerMap` 先跑完，`Category.interactive` + `loadJson` 是标准写法。
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
