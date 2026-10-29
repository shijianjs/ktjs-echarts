# 注意事项

跨示例、跨会话仍然生效的坑。只和某一页相关的细节写在该示例源码的 KDoc 里；目录结构、移植约定与各家族
的实现细节见 [AGENTS.md](AGENTS.md)，示例盘点见 [COVERAGE.md](COVERAGE.md)。

## 通用

- **`unsafeJso` 的隐式接收者**：局部变量 / 参数 / 字段与成员同名（`width`、`style`、`name`、`value`…）
  时，`width = width` 会编译成自我赋值，页面看起来「没设上」。给局部变量改名。
- **数据 helper 的类型**：轴 / 系列的 `data` 用 `strings(...)` / `numbers(...)` / `points(...)` /
  `items(...)`；`numbers` 收 `DoubleArray`，自己造数组写 `DoubleArray { }`。`Array<T?>`（helper 的返回）
  不能直接赋给需要非空元素的字段。
- **数据项的联合类型**：`name` 是 `string | number`、`value` 是 `string | number | Date`，用
  `OptionId` / `OptionDataValue` 包装（构造器在 `examples/Items.kt`）。
- **plotly 导出的 JSON**：看着像数字的列可能是字符串，用 `Number(...)` 转换，不要直接 `as Double`。
- **富文本里只有图片的 tag**：`{Sunny|}` 这类没有文字的 tag 没有固有宽度，必须给显式 `width`，否则
  pattern 被画进零宽盒子，看起来像图标丢了。
- **`js()` 回调不要直接返回对象字面量**：`=> ({...})` 的括号会被吃掉，写
  `function (a) { return {...}; }`。
- **KDoc 里别写 `doc-example/*.js`**：Kotlin 块注释可以嵌套，`/*` 会开一个新的内层注释，文件后半段全被
  当成注释，报错位置还不在这一行。
- **`graphic` 元素定位用 `left` / `top`**：`x` / `y` 是 zrender 原始属性，只认数字，传百分比字符串会落到原点。
- **`graphic` 的 `rect` 默认填黑**：只想画框时要显式写 `fill = null`，否则整块盖成黑色（上游示例就是这么写的）。
- **typed array 当 `data` 必须配 `dimensions`**：`typedFlatData` 走的是上游 `ArrayLike<number>` 分支，
  ECharts 按 `dimensions` 的行宽切分扁平表；漏写会按每个数一个点读，画出来是一条斜线。
- **`geo` 的区域标签默认不显示**：`geo.regions[].label` 只配 `formatter` 时仍然只有 hover 才出现，
  截图里没有标签是正常的（SVG 教程原话「将鼠标 hover 到元素上能显示标签」）。
- **定时器与外部监听**：定时器一律交给 `ChartHolder.setInterval` / `setTimeout`；挂在 `window` /
  `document` 上的监听在 `onUnmount` 里移除，否则换页后还会触到已销毁的实例。
- **改源码用 UTF-8 工具**：PowerShell 的 `Set-Content` 会按 ANSI 落盘、写坏中文，批量改写用 Node / Python。
- **静态数据**：放 `resources/data/`，在 `SOURCE.md` 记来源与使用者；文件名保持上游的，方便和示例源码对照。

## GL / WebGL

- **画布数量**：GL 系列画在 claygl 插入的 canvas 上；`cartesian2d` 的 2D GL 系列（`scatterGL` /
  `linesGL` / `flowGL`）还会多一张 zrender 画布。断言按「有 WebGL 上下文的 canvas 恰好一个」数，不要按
  canvas 总数。e2e 对 GL 页只做 `assertChartRendered` + 「没有 `.ecgl-nowebgl`」，**不要**把 GL 路由加进
  `paintedFrame` / `paintedPixels`。
- **安装器要先于实例**：必须在第一个 `ECharts.init` 之前 `use`；demo 在 `main()` 里统一 `useEChartsGl()`，
  新图表要加到那个数组。`ECharts.use` 自己会去重。
- **label 用扁平属性**：echarts-gl 2.1.0 用 ECharts 6 的 `createTextStyle` 排版，读 `label.fontSize` /
  `label.color` 这些扁平属性；`backwardCompat` 只展平 `axisLabel.textStyle`，系列 label 不展平。
- **曲面无效点用 NaN**：`surface.equation.z` / `parametricEquation.*` 返回 `Double`，上游用 `'-'` 表示无效点，
  Kotlin 侧用 `Double.NaN`；`SurfaceView` 遇到 NaN 顶点会整片四边形不画。
- **后期特效的键名**：`SSAO` / `DOF` / `SSR` 在 wrapper 里是 `ssao` / `dof` / `ssr`（`@JsName` 映射），
  官方文档名也能写；两个都设时上游读旧键。`textureTiling` / `textureOffset` 是 `DoubleOrDoubleArray`，
  成对平铺必须写 `[u, v]`。
- **别照抄死字段**：`exportGL2PLY` 已从 2.1.0 删除；`realisticMaterial.baseTexture` /
  `lambertMaterial.baseTexture` 无人读取（材质只认 `detailTexture`）；`toolbox.iconStyle.normal` 是废弃的
  状态写法；`bar3D` 不读 `wireframe`。
- **`geo3D` 的高度**：`regions[].height` 才是单个区域的高度，文档里的 `regionHeight` 是整幅地图的默认高度；
  `aspectScale` 与 `debug.wireframe` 没写进文档但会被读取。
- **`bar3D.barSize`**：文档没写（只在 `bar3DLayout` 里读），`geo3D` / `globe` 下还会再除以
  `getScale() / 16`，所以不是数据单位。`visualMap.inRange.color` 会写进系列的 `itemStyle.color`，系列上
  写死的颜色被色带顶掉；只映射 `colorLightness` 时才保留原色。
- **`shadow` 材质要自己注册**：`Geo3DBuilder` 构造时会为所有 shading 建一份地面材质，包括 `"shadow"`；
  上游只从 mapbox3D / maptalks3D 视图注册那两个 chunk，所以 wrapper 在 `Geo3DComponent` getter 里补注册。
  删掉它会让每个 `geo3D` 页面刷 missing-shader 错误。
- **纹理生命周期**：纹理可以是图片 URL，也可以是一个 ECharts 实例；实例必须建在 canvas 上，否则控制台警告
  「must init on canvas」且取不到内容。URL 纹理异步加载，销毁后再回调会在 `api.getZr()` 上崩，测试里用
  canvas 纹理。
- **`world.json` 的地名是缩写**：`Czech Rep.` / `Korea` / `Dem. Rep. Congo`…，与 `world-population.json`
  的全称不同；按名字匹配的 `regions` / 地图 `data` 必须先翻译（`GlGeo.kt` 的 `worldRegionNames`），否则
  这些国家静默没有值。
- **先 `registerMap` 再给纹理**：所有地图 / 行星纹理都依赖地图注册完成；`Category.interactive` +
  `loadJson` 是标准写法。
- **空的 `viewControl` / `grid3D` 不要写**：不要为了「对齐上游」写空对象，只在真的设了字段时写。
- **SSR 在 Chrome 下会黑帧**：`echarts-gl` 2.1.0 的 SSR pass 会画出整幅黑帧（bloom / SSAO / DOF 单独开
  都正常），`bar3d-voxelize-image` 因此关掉了 `screenSpaceReflection`。
- **画白线的 GL 页要自带背景**：`graphgl-gpu-layout` 上游画白网格、靠 `theme: dark` 出黑底；本站主题由
  自己的开关决定，页面必须显式写 `backgroundColor`，否则白底白线看起来像空页（截图和 e2e 都看不出错）。
- **`graphGL` 的 `modularity` 不是核心选项**：它来自官方站单独加载的 CDN 插件 `echarts-graph-modularity`，
  本站没有，去掉后所有节点取调色板第一色；`stopThreshold` 是声明但不读的死字段。
- **上游没有 TypeScript 声明**：选项按 `echarts-doc` 的 `option-gl/` 与源码 `defaultOption` 建模。
