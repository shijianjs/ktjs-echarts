# ktjs-echarts-gl

[`echarts-gl`](https://www.npmjs.com/package/echarts-gl) 2.1.0 的 Kotlin/JS wrapper。它是
[`echarts`](https://www.npmjs.com/package/echarts) 6 的扩展包，提供 3D 与 WebGL 系列。

`echarts-gl` 只发布纯 JavaScript，没有 TypeScript 声明，所以这里的声明是依据上游源码和
`option-gl` 文档手写的，而不是生成的。

## 安装

把版本换成需要的那一个（`{上游版本}-kt.{修订号}`）：

```kotlin
dependencies {
    implementation("io.github.shijianjs:ktjs-echarts-gl:2.1.0-kt.1")
}
```

模块以 `api` 作用域依赖 `ktjs-echarts`，核心 wrapper 会自动带上。

## 快速开始：3D 柱状图

在第一次 `init` 之前把安装器注册到核心 `ECharts` 对象上，然后设置 `grid3D` 和 `bar3D`：

```kotlin
import echarts.AxisTypes
import echarts.ECharts
import echarts.options.OptionDataValue
import echarts.options.one
import echartsgl.Bar3DChart
import echartsgl.Grid3DComponent
import echartsgl.options.Axis3DOption
import echartsgl.options.Bar3DDataItem
import echartsgl.options.EChartsGlOption
import echartsgl.options.Grid3DOption
import echartsgl.options.bar3DSeries
import js.objects.unsafeJso
import web.dom.document
import web.html.HtmlTagName

ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))

val element = document.createElement(HtmlTagName.div).apply {
    style.width = "600px"
    style.height = "400px"
}
document.body.append(element)

val chart = ECharts.init(element)
chart.setOption(
    unsafeJso<EChartsGlOption> {
        grid3D = one(
            unsafeJso<Grid3DOption> {
                boxWidth = 100.0
                boxHeight = 100.0
                boxDepth = 100.0
                viewControl = unsafeJso {
                    alpha = 30.0
                    beta = 40.0
                    distance = 250.0
                }
            },
        )
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        series = one(
            bar3DSeries {
                typedData = arrayOf(
                    Bar3DDataItem(
                        arrayOf(
                            OptionDataValue(1.0),
                            OptionDataValue(2.0),
                            OptionDataValue(3.0),
                        ),
                    ),
                )
            },
        )
    },
)
```

系列画在 canvas 之上的 WebGL 图层里，所以容器需要有尺寸、浏览器需要支持 WebGL；不支持时
`echarts-gl` 会显示自己的「Sorry, your browser does not support WebGL」占位。

## 安装器

图表安装器（`echarts-gl/charts`）：

| 值 | `series.type` | 说明 |
| --- | --- | --- |
| `Bar3DChart` | `bar3D` | `grid3D`、`geo3D` 或 `globe` 上的柱体 |
| `Line3DChart` | `line3D` | 同样的坐标系上的折线 |
| `Scatter3DChart` | `scatter3D` | 带符号的散点 |
| `Lines3DChart` | `lines3D` | 按坐标连线，可带尾迹动画 |
| `Polygons3DChart` | `polygons3D` | `geo3D` 上拉伸的多边形 |
| `SurfaceChart` | `surface` | 由 `[x, y, z]` 网格构成的曲面 |
| `Map3DChart` | `map3D` | 已注册地图的区域，画在 `geo3D` / `mapbox3D` / `maptalks3D` 上 |
| `ScatterGLChart` | `scatterGL` | 面向超大数据量的 WebGL 2D 散点 |
| `GraphGLChart` | `graphGL` | 布局在 GPU 上运行的关系图 |
| `FlowGLChart` | `flowGL` | 沿向量场运动的粒子，例如风场 |
| `LinesGLChart` | `linesGL` | 面向大量线段的 WebGL 2D 折线 |

组件安装器（`echarts-gl/components`）：`Grid3DComponent`（`grid3D` 与 `xAxis3D` / `yAxis3D` /
`zAxis3D`）、`Geo3DComponent`（`geo3D`）、`GlobeComponent`（`globe`）、`Mapbox3DComponent`
（`mapbox3D`）和 `Maptalks3DComponent`（`maptalks3D`）。Mapbox 与 maptalks 版本要求页面自己加载
对应库，`echarts-gl` 只负责往它们的 canvas 里渲染。

## Option

`EChartsGlOption` 在核心 `EChartsOption` 之上增加了 `grid3D`、`xAxis3D`、`yAxis3D` 与 `zAxis3D`。
GL 专属的 option 类型都在 `echartsgl.options` 里：

- `Grid3DOption` — 布局（`left` / `top` / `width` / `height`）、`boxWidth` / `boxHeight` /
  `boxDepth`、`environment`，以及嵌套的 `viewControl`、`light`、`postEffect` 与
  `temporalSuperSampling`。
- `Axis3DOption` — 三个轴共用一个类型：类型、范围、`name*`、分割线与分割区域、轴线、刻度、标签
  与 axis pointer。
- `ViewControlOption` — 投影方式、相机角度与距离、自动旋转、阻尼与鼠标按键绑定。
- `LightOption` — 主光源、环境光与环境 cubemap 光。
- `PostEffectOption` — bloom、景深、SSAO、SSR、FXAA、颜色校正与描边。上游的 `DOF`、`SSAO`、
  `SSR`、`FXAA` 别名以 `dof`、`ssao`、`ssr`、`fxaa` 暴露。
- `ShadingOption` — `shading` 以及 `realisticMaterial`、`lambertMaterial`、`colorMaterial` 与
  `hatchingMaterial`，由所有会给表面着色的系列与地理组件实现。
- `Bar3DSeriesOption`，用 `bar3DSeries { }` 工厂创建：柱体几何（`bevelSize`、`bevelSmoothness`、
  `minHeight`、`stack`、`onGridPlane`）、`grid3DIndex` / `geo3DIndex` / `globeIndex`，以及
  `typedData`、`typedItemStyle`、`typedLabel`、`typedEmphasis`、`typedCoordinateSystem`、
  `typedBlendMode` 这些带类型的视图。
- `Bar3DDataItem` — 一条数据：`[x, y, z]`（或 `[lng, lat, value]`）行、单个值，或带逐项样式、
  标签与 emphasis 的 `Bar3DDataItemOption`。
- 其余系列形态相同：`scatterGLSeries { }`、`linesGLSeries { }`、`flowGLSeries { }`、
  `graphGLSeries { }`、`polygons3DSeries { }`、`map3DSeries { }` 分别创建
  `ScatterGLSeriesOption`、`LinesGLSeriesOption`、`FlowGLSeriesOption`、`GraphGLSeriesOption`、
  `Polygons3DSeriesOption` 与 `Map3DSeriesOption`，都带类型化的 `data` 视图。
- `Mapbox3DOption` 与 `Maptalks3DOption` 配置两个地图服务组件。它们要求页面自己加载地图库并设置
  全局 token，token 不是选项字段。

枚举值都是类型化的常量而不是裸字符串：`CoordinateSystems2D`、`CoordinateSystems3D`、
`ShadingTypes`、`GridPlaneTypes`、`GlBlendModes`、`ProjectionTypes`、`QualityTypes`、
`AutoRotateDirections`、`MouseButtons`、`FlowGLParticleTypes`、`GraphGLFocusNodeAdjacencyOns`，
系列类型用 `EChartsGlSeriesTypes`。

## 为什么 wrapper 要自己引入核心模块

每个图表、组件安装器都会 `import` `echarts-gl/lib/echarts-gl.js` 以触发副作用：该模块注册
post-update 钩子，把 WebGL 图层插进 zrender 的 painter，并挂载 `echarts.graphicGL`。上游的
`sideEffects` 列表只匹配 `lib/chart`、`lib/component` 下一层的文件，不匹配再往下的安装器文件，
于是开启 `sideEffects` 优化的打包器（webpack 默认开启）会把这条 import 丢掉，图表什么都画不出来。

因此，读取任一安装器时 wrapper 会自行加载核心模块，无论应用自己的 import 有多细，核心模块都会
留在 bundle 里。代价是它无法被 tree shaking 掉——相对 claygl 和 zrender 只是几 KB。

## 构建与测试

```bash
./gradlew :ktjs-echarts-gl:jsBrowserTest
```

新增或升级 npm 依赖后，刷新共享的锁文件：

```bash
./gradlew kotlinUpgradePackageLock
```
