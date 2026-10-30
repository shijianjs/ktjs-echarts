# todo

跨会话进度跟踪。只写**当前状态、下一步和仍然生效的约定**；历史看 `git log`。项目约定见 `AGENTS.md`、
`TYPE-MAPPING.md` 与 `demo/AGENTS.md`，跨页注意事项见 `demo/NOTES.md`，这里不重复。

## 上下文

- 仓库：`ktjs-echarts`（`echarts` 与 `echarts-gl` 的 Kotlin/JS wrapper + demo 站点）。
- 上游资料一律读本地克隆（不要抓 GitHub / raw）：
  - `S:\workspace\github\apache\echarts`（6.1.0 附近的源码树）
  - `S:\workspace\github\apache\echarts-doc`（`option-gl/` 与 `option/partial/` 是 GL 选项文档）
  - `S:\workspace\github\apache\echarts-examples`（`public/examples/ts/` 是 demo 的移植来源）
  - `S:\workspace\github\ecomfe\echarts-gl`（GL 的 `defaultOption` 与视图实现）
  - `S:\workspace\github\JetBrains\kotlin-wrappers`（写法参考）
- 锁定版本：根 `package.json`（`echarts` 6.1.0、`echarts-gl` 2.1.0）；修订号在
  `wrapper-revisions.properties`（当前 `ktjs-echarts=12`、`ktjs-echarts-gl=10`）。
- 验证：`./gradlew build`（含 detekt）、`:ktjs-echarts:jsBrowserTest`、`:ktjs-echarts-gl:jsBrowserTest`、
  `:demo:e2eTest`；demo 覆盖统计用 `node scripts/demo-coverage.mjs`。

## 当前状态

### ktjs-echarts（6.1.0-kt.12）

`-kt.12` 补了类目型视觉映射缺的三处声明：`VisualMapOption.selected`（`Dictionary<boolean>`，按类目名
或分段值记录选中状态），以及 `VisualOptionUnit` 的 `categorySymbol` / `categorySymbolSize` /
`categoryColor`（`symbol` / `symbolSize` / `color` 的 `@JsName` 同类视图，对应上游
`Dictionaryable<VisualOptionUnit>` 的字典形态）。上游把 `inRange` 声明成
`{ [key in BuiltinVisualProperty]?: any }`，字典只在实现里看得见：`preprocessForSpecifiedCategory`
会把没在字典里出现的类目落到 `-1` 槽，这就是官方示例用空串 `''` 当默认值的原因。
`-kt.11` 补了二进制轨迹页需要的两处扁平数据视图：`LinesSeriesOption.typedFlatData`（`data` 的
`ArrayLike<number>` 分支，`polyline` 时每行 `[count, x0, y0, ...]`，配 `dimensions`）与
`AppendDataParams.typedFlatData`（同一分支的 `appendData` 入口，分片追加百万级轨迹就靠它）。
`-kt.10` 补了两处地图批次需要的公开 API：`GeoOption` 混入 `ComponentOption` 的
`coordinateSystem` / `coordinateSystemUsage` / `coord`、`BoxLayoutOptionMixin` 与
`AnimationOptionMixin`（`geo` 要能钉进矩阵格子并关掉自身动画），以及 `PieSeriesOption.typedCenter`
（`center` 的标量分支，饼图落在 `geo` 上时用区名当地心）。`-kt.9` 给 `ScatterSeriesOption` 加了 `typedFlatData`（与 `typedData` 同一 `data` 属性的扁平视图，
`ArrayLike` 分支，配 `dimensions` 使用），`scatter-nebula` 的 8 MB 浮点表就靠它直接交给 `large` 模式。
`-kt.8` 只补了 demo 需要的两处公开 API：`ZRenderType.configLayer` / `ZRLayerConfig`（尾迹模糊）和
`TimelineOption` 的 `width` / `height` / `tooltip` / `emphasis`。类型建模 10 轮收尾（联合类型、字面量、
数据项类型、`typedData` 视图、回调与事件 payload 等）已完成，**没有未完成项**。以下是已确认「保持现状」
的决定，不要重复修：

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

### ktjs-echarts-gl（2.1.0-kt.10）

`-kt.10` 两处：`Line3DDataItemOption` 的数据项样式键名从 `itemStyle` 改成上游真正读的 `lineStyle`
（`Line3DSeries.js:11` 的 `visualStyleAccessPath: 'lineStyle'`，官方文档 `series/line3D.md` 数据项一节
同此），原来那个键写进去没人读；`Axis3DLabelOption` 改为继承 `LabelOption`，对齐 2D 的
`AxisLabelOption`——`Grid3DAxis.js:131` 直接把 axisLabel 当文字样式模型用，ECharts 又把废弃的
`textStyle` 展平到 label 本身，所以 `color` / `fontSize` 这些要在 label 层声明，调用方不必再
`unsafeCast<TextStyleOption>`。

`-kt.9` 给 `LinesGLSeriesOption` 与 `ScatterGLSeriesOption` 各加了 `typedFlatData`（与 `typedData`
同一 `data` 属性的 `ArrayLike<number>` 视图），`linesGL-ny` / `scatterGL-gps` 据此把百万级点与折线
按分片交给 `appendData`。

- 已建模：`grid3D` / `xAxis3D|yAxis3D|zAxis3D`、`geo3D`、`globe`、`viewControl`、`light`、`postEffect`、
  shading 与材质，全部 11 个系列的选项、数据项联合与工厂，以及 `mapbox3D` / `maptalks3D` 组件选项和
  2D/3D 坐标系统、投影、blendMode 等字面量。
- 安装器齐全（11 个 chart + 5 个 component）；`echarts-gl` 的 `sideEffects` 列不全，安装器 getter 里读取
  `echarts-gl/lib/echarts-gl.js` 来加载核心模块。
- 浏览器测试：`GlSeries2DTest`（`scatterGL` / `linesGL` / `flowGL` / `graphGL`）与 `Map3DSeriesTest`；
  `mapbox3D` / `maptalks3D` 要第三方地图 SDK 和全局 token，只做类型不做渲染用例。
  `PostEffectTest` 覆盖 `postEffect` 七个 pass、`temporalSuperSampling` 与 `environment`，
  `Axis3DOptionTest` 覆盖 `grid3D` 与 `xAxis3D` 共用的六个轴子选项，`DataItemOptionTest` 覆盖
  `bar3D` / `scatter3D` / `line3D` / `surface` 的对象形态数据项和 `surface` 用 `data` + `dataShape`
  的那条分支；这些文件都按「选项能被 `setOption` 接受、字段能从 `getOption` 读回」这个约定写，
  `SSR` 在用例里保持关掉（2.1.0 的这个 pass 在 Chrome 下画黑帧）。`GlSeries2DTest` 的 `graphGL` 一条场景
  把 `GraphGLForceAtlas2Option` 十七个成员全部点名（`GPU` / `steps` 等五个之外，又补了 `repulsionByDegree` /
  `linLogMode` / `strongGravityMode` / `gravityCenter` / `scaling` / `edgeWeightInfluence` / `edgeWeight` /
  `nodeWeight` / `preventOverlap` / `barnesHutTheta` / `jitterTolerence` / `dissuadeHubs`，这些键在
  `ForceAtlas2.js` 里都被布局真正读到），并同时写 `links` 与别名 `edges`（`GraphGLSeries.js:41` 取
  `option.edges || option.links`）；`GlSeriesTest` 的 `lines3D` 拖尾把 `effect` 八个成员写全，补了
  `period` / `constantSpeed` / `trailColor` / `trailOpacity` / `spotIntensity`。`LiteralsTest` 的
  `stringUnionsWrapACustomValue` 直接调 13 个 `sealed external interface` 的 `Xxx(String)` 工厂（各包一个
  不在 `Xxxs` 常量里的自定义字符串读回），补上以前只走常量 getter、从不单独走那条「包任意 JavaScript
  字符串」路径的空缺——至此 `field-coverage.mjs` 报 GL 575 个成员里未点名 **0 个**（只剩 `NOT_COVERED` 的 20 个）。
  公共辅助在 `TestSupport`：`createChart` / `nextFrames` / `row` / `bar3DItems` / `hasOwn` /
  `assertWebGlCanvas` / `cartesian3DAxes` / `textureCanvas` / `textureCanvases` / `registerSquareMap`，
  各测试文件不再自己复制。`MaterialOptionTest` 覆盖四种 shading 各自的材质（`bar3D` 一次带齐四套、
  只有选中的那套进 GPU）、`hatching` 的六张贴图与 `geo3D` / `map3D` 的 `groundPlane` 材质集（贴图画布
  用 `textureCanvases` 生成，同步加载，dispose 时没有悬挂的加载任务）；`Geo3DGlobeTest` 里 `geo3D`
  的区域覆盖写全了 `itemStyle` / `label` / `emphasis`，不再只写 `name` / `height`；
  `ViewControlTest` 用一条 `geo3D` 场景把 `ViewControlOption` 二十八个成员全部点名，另用 `grid3D`
  核对组件自己的相机默认值；`Geo3DGlobeTest` 补了 `globe` 的 `globeRadius` / `globeOuterRadius`、
  `heightTexture` / `displacementTexture` 与大气的 `innerGlowPower`，以及 `light` 的
  `main.shadowQuality` 与 `ambientCubemap` 的三个强度字段；`ComponentLayoutTest` 用两条 `geo3D`
  证明不同 `zlevel` 各占一张 WebGL 画布（`echarts-gl.js:56` 的 `getLayerGL` 按 zlevel 建 `LayerGL`），
  又把 `grid3D` / `geo3D` / `globe` 三个组件的 `left` / `top` / `width` / `height` 盒布局各自点名
  （`grid3DCreator` / `geo3DCreator` / `globeCreator` 用 `getBoxLayoutParams()` 读它们，`grid3D` 得配
  三条 `*Axis3D` 否则 `Cartesian3D.resize` 报「don't have xAxis」），`GlSeriesTest` 用两条 `globe`
  让 `bar3D` / `scatter3D` / `lines3D` 各自用 `globeIndex` 选定要贴的那颗地球（`globeCreator.js:121` 走
  echarts 的 `<mainType>Index` 引用规则），`Bar3DSeriesTest` 补了 `bar3D.stack` 的同组堆叠键名。
- 用例查出的五处上游行为，别再当 wrapper 的 bug：
  `ViewGL.setPostEffect` 先读 `DOF` / `SSAO` / `SSR` / `FXAA`，camelCase 只是回退，所以这四个键只能靠
  `@JsName` 写对——写错键名时类型化读写依然自洽，用例用 `hasOwn`（`TestSupport`）直接问 JavaScript；
  ECharts 会把 `axisLabel.textStyle` 展平到 `axisLabel` 本身并把 `textStyle` 置成 `null`，
  但 `axisPointer.label.textStyle` 原样保留，所以 3D 轴标签的文字样式在 label 层读写；
  数据项的样式键跟的是该系列的 `visualStyleAccessPath`（echarts `src/visual/style.ts` 的 `dataStyleTask`）——
  `line3D` / `lines3D` / `linesGL` 是 `lineStyle`，其余是 `itemStyle`；`Bar3DSeries.js:13` 把这个属性
  拼成了 `visualStyleAccessPathvisu`，但 echarts 的默认值本来就是 `itemStyle`（`src/model/Series.ts:220`），
  所以 `bar3D` 的数据项仍用 `itemStyle`；
  `map3D` 没有 `regions` 选项，`Map3DSeries.js:40` 用 `getFilledRegions(option.data, option.map)` 把 GeoJSON
  里的区域名补进 `data`，所以地图区域的高度与样式一律走数据项，`getOption()` 里能看到补出来的那条；
  `componentViewControlMixin` 的 `setView` 会被 `Geo3DView` / `Grid3DView` 在每次渲染的 `control.update()`
  里触发（`OrbitControl.update()` 无条件把 `_needsUpdate` 置真并 `trigger('update')`），把
  `alpha` / `beta` / `distance` / `center` 写回组件选项，所以这四个字段的回读值等于相机自己算出来的那个——
  相机正是从这四个选项初始化的、用例里又不动它，回读才仍等于写进去的值。
- 材质这一层只有 `graphicGL.setMaterialFromModel`（`util/graphicGL.js:539`）真正读的键会生效：
  `shading` 决定读哪套 `<shading>Material`，`realistic` 的 `roughness` / `metalness` 是非数字时按贴图上传、
  此时才用 `roughnessAdjust` / `metalnessAdjust` 当标量，`hatching` 必须给满 6 张 `hatchingTextures`
  （少于 6 张上游 `console.error`），`textureTiling` 大于 1 时贴图得是 2 的整数次幂边长。
  材质集是「四套都写、只用一套」的，所以 `bar3D` 一条系列同时带四套就能全部核对。
- `globe` 与 `light` 同理：`GlobeModel.getDisplacementTexture()` 是 `displacementTexture || heightTexture`，
  但 `hasDisplacement()` 要 `displacementScale > 0` 才走顶点位移，`globeCreator.js:150` 的加载回调对画布
  是同步的（`isValueImage` 认 canvas），所以贴图画布不会留下悬挂任务；`SceneHelper.updateAmbientCubemap`
  只在 `light.ambientCubemap.texture` 有值时才建 cubemap，且那是异步的 prefilter，所以用例只写
  `exposure` / `diffuseIntensity` / `specularIntensity` 三个强度、不写那个 URL。`main.shadowQuality`
  走 `graphicGL.getShadowResolution`（low/medium/high/ultra = 512/1024/2048/4096），与 `shadow` 开关无关，
  所以关掉阴影也能核对这个字段。
- 已确认的死字段（不建模）：`graphGL` 的 `roam` / `center`、`edgeSymbol` / `edgeSymbolSize`、
  `initLayout` / `stopThreshold`；hatching 材质只读 `hatchingTextures`，`paperColor` 无人读取；mapbox3D /
  maptalks3D 文档里的 `shading` 不会被读。

### demo

- 官方清单 436 条：**已移植 384 页 / 385 条，明确不做 51，待补 0**。逐条清单、理由与维护方式见
  [demo/COVERAGE.md](demo/COVERAGE.md)。
- 跨页注意事项（含 GL）见 [demo/NOTES.md](demo/NOTES.md)；移植约定、各家族做法与可复用数据见
  [demo/AGENTS.md](demo/AGENTS.md)。
- **明确不做的 51 条不再讨论**：bmap、外部 JS 库、官方站点图片素材、真实指针交互、渲染器开关、远端脚本、
  实例内部结构等，理由在 COVERAGE 里。新增排除项先更新 `scripts/demo-coverage.mjs` 的 `EXCLUDED`。

## 下一步

**移植队列已清空**（`geo-lines` 是待补清单的最后一条）。官方清单 436 条现在要么已移植、要么在
「明确不做」里，`node scripts/demo-coverage.mjs` 输出「待补 0」。已在推进的方向是按 `AGENTS.md`
「测试覆盖」那六条复查 wrapper 的用例缺口，GL 侧八批全部走完（第一批 `postEffect` / `environment` /
轴子选项，第二批对象形态数据项与 `surface.data`，第二批查出了 `-kt.10` 那个键名 bug，
第三批四套材质与 `groundPlane`、`geo3D` 区域覆盖、`map3D` 的材质与样式，第四批整套相机
`ViewControlOption` 与 `Axis3DOption` 的八个轴级字段，第五批 `globe` 的半径/两张贴图/大气与 `light`，
第六批三个组件的 `zlevel` 与盒布局、`bar3D` / `scatter3D` / `lines3D` 的 `globeIndex` 和 `bar3D.stack`，
第七批 `graphGL` 力导全十七项与 `edges` 别名、`lines3D` 拖尾全八项，第八批 `EChartsGlLiterals` 的
13 个 `Xxx(String)` 工厂函数）。

缺口现在由 `node scripts/field-coverage.mjs` 按**字段**核对（旧的 `build/gl-coverage-gap.mjs` 只看类型名，
`unsafeJso {}` 推断出的接收者类型会被误报，已废弃）。当前输出：**GL 575 个成员全部点过名，未点名 0 个**
（`NOT_COVERED` 的 20 个是安装器 getter 与要第三方 SDK/token 的 `mapbox3D` / `maptalks3D` 成员，明确不做）；
2D 已推进十七批：第一批 `EChartsLiterals.kt` 的 40 个 `Xxx(String)` 工厂与 `AnimationEasing((Double) -> Double)`
重载，第二批 `EChartsSeriesLiterals.kt` 的 30 个工厂（含 `NodeClickBehavior` 的 `String` 与 `Boolean` 两个
重载），前两批都在 `echarts/LiteralsTest` 里用不在 `Xxxs` 常量里的自定义值各包一次读回；第三批
`options/DataZoomVisualMapDatasetOptionTest`，把 `EChartsOption.kt` 里的 `DataZoomOption`（滑柄样式、窗口跨度、
`brushSelect` 等 20 项）、`VisualMapOption`（`minOpen`/`pieces`/`seriesTargets` 等 14 项）与 `DatasetOption`
（派生数据集四个字段）逐个 `unsafeJso` 设值读回，并有一例把三者一起喂给 `setOption` 再从 `getOption` 核对；
第四批 `options/ToolboxOptionTest`，覆盖 `ToolboxOption`、`ToolboxFeature`、`ToolboxFeatureOption`、
`ToolboxIconStyleOption`（十个 `text*` 字段）、`ToolboxEmphasisOption`、`SaveAsImageOption` 与 `DataViewOption`
（`optionToContent` / `contentToOption` 回调）；第五批 `options/TimelineParallelBrushOptionTest`，补
`TimelineOption` / `TimelineControlStyle` 的播放控制、`ParallelOption` 的可展开轴窗口与 `parallelAxisDefault`
（含 `ParallelAxisOption` 的 `dim` / `areaSelectStyle`）以及 `BrushOption` 的 `inBrush` / `outOfBrush` /
`brushLink`；第六批 `echarts/RuntimeOptsPayloadTest`，把驱动实例的运行时对象喂给真实方法：`EChartsInitOpts`
（`devicePixelRatio` / `useDirtyRect` / `useCoarsePointer` / `pointerSize` / `ssr`）走 `createChart` 并核对
`getDevicePixelRatio`、`SetOptionOpts`（`notMerge` / `lazyUpdate` / `replaceMerge`）验证替换、`LoadingOpts` 喂给
`showLoading`、`RenderToSVGStringOpts.useViewBox` 用 svg renderer 渲染、`DispatchActionOpts.flush` 配 `Payload`
喂给 `dispatchAction`，再加 `Payload` / `ModelFinderObject` / `SelectChangedPayload` 三字段的 `unsafeJso` 读回；
第七批 `options/CalendarGridMatrixOptionTest`，补 `CalendarOption` 的 `cellSize` / 三套 `*Label`（含 `firstDay`、
`nameMap`、`margin`）/ `splitLine`、`GridOption` 的 `outerBounds` 与两个 clamp 尺寸、`MatrixOption` 的 `corner`
/ `backgroundStyle` / `borderZ2`；第八批 `echarts/GeoTypesTest`，覆盖 `EChartsGeoTypes.kt` 的 Geo 类型：
`GeoJSON` 的 `UTF8Encoding` / `UTF8Scale`、`GeoJSONProperties.cp`、`GeoJSONGeometry.encodeOffsets`、
`GeoJSONMapInput.specialAreas`（配 `GeoSpecialArea` 的 left/top/width/height）逐个设值读回并喂 `registerMap`
再从 `getMap` 核对，加 `GeoJSONSourceInput` 的 `String` / `GeoJSON` 两参与 `GeoSVGSourceInput` 的
`String` / `Document` / `SVGElement` 三个工厂重载；第九批 `echarts/ZRenderElementTypesTest`，用
`parseGeoJSON` 得到真实区域再走 `ZRBoundingRect.intersect`，点名 `BoundingRectIntersectOpt` 的
`direction` / `bidirectional` / `touchThreshold` / `outIntersectRect`，并以 `unsafeJso` 设值读回覆盖
`ZRShapeProps`（`rx`/`ry`/`x3`~`y4`/`paths`/`pathData`）、`ZRStyleProps`（`sx`~`sHeight`/`strokeNoScale`/
`fill`~`strokeOpacity`/`lineCap`/`lineJoin`/`miterLimit`/`strokePercent`/`strokeFirst`/`lineHeight`/`tag`/
`overflow`/`ellipsis`/`truncateMinChar`/四个 `textShadow*`）、`ElementTextConfig`（`insideFill`~`outsideStroke`/
`autoOverflowArea`）、`ZRElement`（`invisible`/`ignore`/`cursor`）与 `ZRElementEvent.cancelBubble`；第十批
`echarts/LocaleOptionTest`，整套本地化文案：`LocaleOption` 的 `time`（`monthAbbr`/`dayOfWeek`/`dayOfWeekAbbr`）、
`legend.selector`、`toolbox.magicType.title.tiled`、`series.typeNames.k`，以及 `LocaleAria*` 全部
（`general.withTitle`/`withoutTitle`、`series.single`/`multiple` 的 `prefix`/`withName`/`withoutName`/`separator`、
`data` 的 `allData`/`partialData`/`withName`/`withoutName`/`separator`），组装整份 `LocaleOption` 逐个读回，
再 `registerLocale` 并用 `EChartsInitOpts.locale` 建真实例；第十一批 `options/ComponentTitleLegendTooltipAxisOptionTest`，
把 `ComponentOptions.kt` 一次清零：`TitleOption`（`sublink`/`subtextStyle`/`textVerticalAlign`）、`LegendOption`
（`inactiveBorderColor`/`pageIconColor`/`pageIconSize`/`pageTextStyle`/`pageFormatter`/`scrollDataIndex` 与
`LegendItemStyleOption`/`LegendLineStyleOption` 的 `borderCap`~`borderMiterLimit`/`areaColor`/`dashOffset`/`cap`/`join`）、
`TooltipOption`（`axisPointer`/`showContent`/`alwaysShowContent`/`show`~`hideDelay`/`enterable`/`confine`/`className`/
`transitionDuration`/`extraCssText`）与 `AxisOption`（`alignTicks`/`nameTextStyle`/`nameTruncate.placeholder`/`nameGap`/
`nameRotate`/`nameMoveOverlap`/`min`~`maxInterval`/`logBase`/`axisTick`/`splitArea`/`minorTick`/`minorSplitLine`/`axisPointer`
与 `AxisLineOption.onZero`/`AxisTickOption.alignWithLabel`/`SplitLineOption.showMin`~`MaxLine`/`AxisBreakAreaOption.zigzag*`），
逐个 `unsafeJso` 读回并把 title+legend 喂 `setOption`；第十二批 `options/CartesianSeriesOptionTest`，把
`CartesianSeriesOptions.kt` 一次清零：`SeriesOption` 基字段（`colorBy`/`labelLine`/`datasetId`/`universalTransition`/
`progressive`~`progressiveThreshold`/`legendHoverLink`/`dataGroupId`/`blendMode`/`hoverLayerThreshold`）、`LineSeriesOption`
（`clip`/`smoothMonotone`/`connectNulls`/`showAllSymbol`/`endLabel`/`triggerLineEvent`/`sampling`~`samplingThreshold`/
`stackStrategy`/`showBackground`）、`BarSeriesOption`（`stackOrder`/`barMax`~`barMinWidth`/`barCategoryGap`/`barMinHeight`/
`barMinAngle`/`realtimeSort`/`sampling`）、`Scatter`/`EffectScatter`（含 `RippleEffectOption.period`）、`Candlestick`/`Boxplot`
（`boxWidth`）/`Heatmap`（`blurSize`/`min`~`maxOpacity`）/`Parallel`（`inactive`~`activeOpacity`）/`Custom`（`itemPayload`）
与 `LinesEffectOption`、`BarGridLayoutOptionForCustomSeries`，并把 line/bar/scatter/effectScatter 四系列喂 `setOption`；
第十三批 `options/CommonTypesUnionTest`，把 `CommonTypes.kt` 一次清零：六个不透明联合的工厂重载各包一个值读回
（`TooltipMarker` 的 `String`/`RichTextTooltipMarker`、`BooleanOrDoubleOrString` 的 `Boolean`/`Double`/`String`、
`BooleanOrStringArray` 的 `Boolean`/`Array<StringOrDouble>`、`SelectionMode` 的 `Boolean`/`String`、
`OptionDataValueDate` 的 `Date`/`String`/`Double`、`ZRColorOrArray` 的 `ZRColor`/`Array<ZRColor>`），四个
`DatasetSource` 形态工厂（`datasetOriginal`/`datasetObjectRows`/`datasetKeyedColumns`/`datasetTypedArray`）逐个
构造真实输入并读回，再 `unsafeJso` 设值读回 `RichTextTooltipMarker.content`、`DimensionDefinition.displayName`、
`OptionEncode.itemId`、`DataTransformOption.print`、`SeriesOnRadarOptionMixin.radarIndex`/`radarId`、
`VisualPiece` 继承自 `VisualOptionUnit` 的 `colorLightness`/`colorHue` 与自身的 `gte`/`lte`、
`UniversalTransitionOption.divideShape`/`seriesKey`、`CoordinateSystemDataLayout.contentRect`/`matrixXYLocatorRange`；
第十四批 `options/HierarchySeriesOptionTest`，把 `HierarchySeriesOptions.kt` 一次清零：`TreeSeriesOption`
（`initialTreeDepth`/`expandAndCollapse`/`scaleLimit`）、`TreemapSeriesOption`（`squareRatio`/`leafDepth`/
`drillDownIcon`/`zoomToNodeRatio`/`nodeGap`/`nodeWidth`/`upperLabel`）、`TreemapSeriesVisualOption`（`visualDimension`/
`visualMin`/`visualMax`/`visibleMin`/`childrenVisibleMin`）、`TreemapStateOption.upperLabel`、`TreemapBreadcrumbOption.emptyItemWidth`、
`SunburstSeriesOption`（`minShowLabelAngle`/`stillShowZeroSum`/`renderLabelForZeroData`/`nodeGap`/`nodeWidth`/`percentPrecision`）
与 tree/sunburst 数据项的 `collapsed` 逐个 `unsafeJso` 设值读回，并把 tree/treemap/sunburst 三系列各带一棵真实
`data` 树喂 `setOption` 从 `getOption` 核对；第十五批 `options/RelationSeriesOptionTest`，把
`RelationSeriesOptions.kt` 一次清零：`GraphForceOption`（`repulsion`/`gravity`/`friction`/`edgeLength`/`layoutAnimation`）、
`GraphCircularOption.rotateLabel`、`GraphSeriesOption`（`layoutAnimation`/`edgeSymbolSize`/`nodeScaleRatio`/`autoCurveness`）、
`GraphEdgeItemOption.ignoreForceLayout`、`ChordSeriesOption`（`layoutIterations`/`padAngle`）、`SankeySeriesOption.layoutIterations`
与 `SankeyNodeItemOption.localX`/`localY` 逐个 `unsafeJso` 设值读回，并把带 `nodes`/`links` 的真实 graph 系列喂 `setOption`；
第十六批 `options/PartSeriesOptionTest`，把 `PartSeriesOptions.kt` 一次清零：`PieSeriesOption`（`avoidLabelOverlap`/
`selectedOffset`/`animationTypeUpdate`/`showEmptyCircle`/`emptyCircleStyle`）、`FunnelSeriesOption.minSize`/`maxSize`、
`GaugePointerOption.keepAspect`、`GaugeAnchorOption.showAbove`/`keepAspect`、`MapSeriesOption`（`boundingCoords`/
`mapValueCalculation`/`showLegendSymbol`）与 `PictorialBarSeriesSymbolOption`（`symbolRepeat`/`symbolClip`/`symbolPatternSize`）
逐个 `unsafeJso` 设值读回，并把带真实数据的 pie/funnel/gauge 三系列喂 `setOption`；
第十七批 `options/PartDataItemOptionTest`，把 `PartDataItemOptions.kt` 一次清零：`PieLabelRotate` 的
`Double`/`Boolean`/`String` 三工厂重载、`PieLabelAlignTo(String)` 与 `PieLabelOption`（`alignTo`/`edgeDistance`/
`bleedMargin`/`distanceToLabelLine`）逐个包值读回，`GaugeDataItem` 的 `OptionDataValueNumeric`/`GaugeDataItemOption`
两重载、`RadarSeriesDataItem` 的数组/对象两重载、`PictorialBarDataItem` 的 `OptionDataValue`/数组/对象三重载也各自包值读回。
几批结构型用例都顺带点名了共享基接口与嵌套样式字段，剩 2704 个成员里 92 个没被点名。

2D 剩余按 `options/` 与 `ECharts*` 分文件推进，缺口最大的几个文件：
`EChartsOption.kt`、`CustomElementOptions.kt`、`StyleOptions.kt`
与 `EChartsTypes.kt`（`ActionInfo` / `EventQuery` / `TransitionSeriesFinderList` / `SetOptionTransition` 等联合），
再往上是各系列数据项联合与 `AxisPointer*` / `GeoOption`（`nameMap`/`boundingCoords`/`scaleLimit`/`nameProperty`）
一类坐标轴与地理选项。已清零的文件不再列（`ComponentOptions.kt`、`EChartsGeoTypes.kt`、`ZRenderElementTypes.kt`、
`CartesianSeriesOptions.kt`、`CommonTypes.kt`、`HierarchySeriesOptions.kt`、`RelationSeriesOptions.kt`、
`PartSeriesOptions.kt`、`PartDataItemOptions.kt` 本轮起 0 缺口）。

`--limit N` 控制每个文件列出的条数，`--show-covered` 反查已覆盖的名字；判据是「名字在 jsTest 全文里出现过」，
所以 `show` 这类到处都有的成员名几乎总是已覆盖，这个口径用来找**从没出现过的字段**，不是逐用例的覆盖率。
带 `@JsName` 的键按其 JS 名核对，裸声明同一个 JS 键的成员（`coordinateSystem` 与 `typedCoordinateSystem`）
不重复计缺口。

已否决的方向：把 COVERAGE「清单之外」那 13 个 mapbox / maptalks / Web Audio GL 源文件与 6 个归档示例并入
`EXCLUDED`——`scripts/demo-coverage.mjs` 对不在官方清单里的 id 直接 `fail`，这些条目本来就不进账本，
继续留在 COVERAGE 的「清单之外」一节用文字说明。

新增/移植页面后跑 `node scripts/demo-coverage.mjs --write` 更新 COVERAGE。

## 收尾清单

- `./gradlew build`、`:ktjs-echarts-gl:jsBrowserTest`、`:demo:e2eTest` 全过；截图目视至少一个新 GL 页面。
- 补完一批用例跑 `node scripts/field-coverage.mjs <模块>` 确认「未点名」数量下降，新增的「明确不做」进
  脚本里的 `NOT_COVERED` 并写理由。
- 改了 wrapper 公开代码就提升对应模块的修订号，并跑
  `./gradlew kotlinUpgradePackageLock --rerun-tasks` 提交锁文件（增量构建下该任务会误报 UP-TO-DATE）。
- 每批一个 `feat(...)` 提交并推送到 `origin/main`。
