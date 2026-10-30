# ktjs-echarts-gl 模块说明

`echarts-gl` 2.1.0 的 Kotlin/JS wrapper，依赖 `ktjs-echarts` 的选项类型。通用约定在仓库根
[AGENTS.md](../AGENTS.md)；这里只记 GL 特有的上游行为与测试约定。

## 安装器与副作用

11 个 chart + 5 个 component 的安装器齐全。`echarts-gl` 的 `sideEffects` 列不全，webpack 会丢掉上游
自己的副作用 import（3D 图表静默不渲染），所以安装器 getter 显式读取 `echarts-gl/lib/echarts-gl.js`
加载核心模块（见 `EChartsGlCore.kt`）。`shadow` 材质要由 wrapper 在 `Geo3DComponent` getter 里补注册，
删掉会让每个 `geo3D` 页面刷 missing-shader 错误。

## 上游行为（用例查出的，别当 wrapper 的 bug）

- `ViewGL.setPostEffect` 先读 `DOF` / `SSAO` / `SSR` / `FXAA`，camelCase 只是回退，所以这四个键只能靠
  `@JsName` 写对；写错键名时类型化读写依然自洽，用例用 `TestSupport.hasOwn` 直接问 JavaScript。
- ECharts 会把 `axisLabel.textStyle` 展平到 `axisLabel` 本身并把 `textStyle` 置成 `null`，但
  `axisPointer.label.textStyle` 原样保留，所以 3D 轴标签的文字样式在 label 层读写。
- 数据项的样式键跟该系列的 `visualStyleAccessPath` 走（echarts `src/visual/style.ts` 的
  `dataStyleTask`）：`line3D` / `lines3D` / `linesGL` 是 `lineStyle`，其余是 `itemStyle`。
  `Bar3DSeries.js:13` 把属性名拼成了 `visualStyleAccessPathvisu`，但 echarts 的默认值本来就是
  `itemStyle`（`src/model/Series.ts:220`），所以 `bar3D` 数据项仍用 `itemStyle`。
- `map3D` 没有 `regions` 选项：`Map3DSeries.js:40` 用 `getFilledRegions(option.data, option.map)` 把
  GeoJSON 的区域名补进 `data`，所以区域高度与样式一律走数据项，`getOption()` 里能看到补出来的那条。
- `componentViewControlMixin` 的 `setView` 会被 `Geo3DView` / `Grid3DView` 每次渲染的 `control.update()`
  触发（`OrbitControl.update()` 无条件触发 `update`），把 `alpha` / `beta` / `distance` / `center` 写回
  组件选项；相机正是从这四个选项初始化、用例里又不动它，回读值才仍等于写入值。

## 材质、globe 与 light

- 材质只有 `graphicGL.setMaterialFromModel`（`util/graphicGL.js:539`）真正读的键会生效：`shading` 决定
  读哪套 `<shading>Material`；`realistic` 的 `roughness` / `metalness` 非数字时按贴图上传，此时才用
  `roughnessAdjust` / `metalnessAdjust` 当标量；`hatching` 必须给满 6 张 `hatchingTextures`（少于 6 张
  上游 `console.error`）；`textureTiling` 大于 1 时贴图边长要是 2 的整数次幂。材质集是「四套都写、
  只用一套」，`bar3D` 一条系列同时带四套即可全部核对。
- `GlobeModel.getDisplacementTexture()` 是 `displacementTexture || heightTexture`，但 `hasDisplacement()`
  要 `displacementScale > 0` 才走顶点位移；`globeCreator.js:150` 的加载回调对画布是同步的
  （`isValueImage` 认 canvas），贴图画布不会留下悬挂任务。`SceneHelper.updateAmbientCubemap` 只在
  `light.ambientCubemap.texture` 有值时才建 cubemap，且那是异步 prefilter，所以用例只写
  `exposure` / `diffuseIntensity` / `specularIntensity` 三个强度、不写那个 URL。
  `main.shadowQuality` 走 `graphicGL.getShadowResolution`（low/medium/high/ultra = 512/1024/2048/4096），
  与 `shadow` 开关无关。

## 死字段（不建模，也别往 wrapper 里补）

`graphGL` 的 `roam` / `center`、`edgeSymbol` / `edgeSymbolSize`、`initLayout` / `stopThreshold`；
hatching 材质只读 `hatchingTextures`，`paperColor` 无人读取；`map3D` 的 `itemStyle.areaColor` 与
`instancing`、mapbox3D / maptalks3D 文档里的 `shading` 都不会被读。

## 测试

- 用例约定是「选项能被 `setOption` 接受、字段能从 `getOption` 读回」：`GlSeries2DTest`、`GlSeriesTest`、
  `Bar3DSeriesTest`、`Map3DSeriesTest`、`MaterialOptionTest`、`Geo3DGlobeTest`、`ViewControlTest`、
  `ComponentLayoutTest`、`Axis3DOptionTest`、`PostEffectTest`、`DataItemOptionTest`、`LiteralsTest`；
  公共辅助在 `TestSupport`。
- `mapbox3D` / `maptalks3D` 要第三方地图 SDK 和全局 token，只做类型不做渲染用例；`SSR` 在用例里保持
  关掉（2.1.0 的这个 pass 在 Chrome 下画黑帧）。
- 字段级覆盖：`node scripts/field-coverage.mjs ktjs-echarts-gl` 最近一次为 575 个成员、未点名 0 个；
  `NOT_COVERED` 里的 20 个是安装器 getter 与 `mapbox3D` / `maptalks3D` 成员，明确不做。
