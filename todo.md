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
  `SSR` 在用例里保持关掉（2.1.0 的这个 pass 在 Chrome 下画黑帧）。
  公共辅助在 `TestSupport`：`createChart` / `nextFrames` / `row` / `bar3DItems` / `hasOwn` /
  `assertWebGlCanvas` / `cartesian3DAxes` / `textureCanvas` / `textureCanvases` / `registerSquareMap`，
  各测试文件不再自己复制。`MaterialOptionTest` 覆盖四种 shading 各自的材质（`bar3D` 一次带齐四套、
  只有选中的那套进 GPU）、`hatching` 的六张贴图与 `geo3D` / `map3D` 的 `groundPlane` 材质集（贴图画布
  用 `textureCanvases` 生成，同步加载，dispose 时没有悬挂的加载任务）；`Geo3DGlobeTest` 里 `geo3D`
  的区域覆盖写全了 `itemStyle` / `label` / `emphasis`，不再只写 `name` / `height`；
  `ViewControlTest` 用一条 `geo3D` 场景把 `ViewControlOption` 二十八个成员全部点名，另用 `grid3D`
  核对组件自己的相机默认值；`Geo3DGlobeTest` 补了 `globe` 的 `globeRadius` / `globeOuterRadius`、
  `heightTexture` / `displacementTexture` 与大气的 `innerGlowPower`，以及 `light` 的
  `main.shadowQuality` 与 `ambientCubemap` 的三个强度字段。
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
「测试覆盖」那六条复查 wrapper 的用例缺口，GL 侧已完成五批（第一批 `postEffect` / `environment` /
轴子选项，第二批对象形态数据项与 `surface.data`，第二批查出了 `-kt.10` 那个键名 bug，
第三批四套材质与 `groundPlane`、`geo3D` 区域覆盖、`map3D` 的材质与样式，第四批整套相机
`ViewControlOption` 与 `Axis3DOption` 的八个轴级字段，第五批 `globe` 的半径/两张贴图/大气与 `light`）。

缺口现在由 `node scripts/field-coverage.mjs` 按**字段**核对（旧的 `build/gl-coverage-gap.mjs` 只看类型名，
`unsafeJso {}` 推断出的接收者类型会被误报，已废弃）。当前输出：GL 575 个成员里 42 个没被用例点过名、
20 个在 `NOT_COVERED` 里；2D 2704 个成员里 763 个没被点名。下一批候选，按文件成组、不必逐条讨论：

1. 各组件的布局与索引字段：`Grid3DOption` / `Geo3DOption` / `GlobeOption` 的 `zlevel` 与 `top`、
   `Map3DSeriesOption.top`，加 `bar3D` / `scatter3D` / `lines3D` 三个系列的 `globeIndex` 和
   `bar3D.stack`——`zlevel` 决定 WebGL 层落在哪一层，是不能和 canvas 组件共用的那一个。
2. `GraphGLForceAtlas2Option` 的力导参数十二项与 `GraphGLSeriesOption.edges`、
   `Lines3DEffectOption` 的拖尾五项（`period` / `constantSpeed` / `trailColor` / `trailOpacity` /
   `spotIntensity`）。
3. `EChartsGlLiterals` 里 13 个联合类型的**工厂函数**没被调用过——`LiteralsTest` 只测了 `Xxxs` 常量对象，
   `ShadingType("lambert")` 这类自定义字符串那条路还没走。
4. 2D 那 763 项按 `options/` 分文件推进，优先系列数据项联合与 `visualMap` / `dataset` 一类结构型选项。

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
