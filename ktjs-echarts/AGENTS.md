# ktjs-echarts 模块说明

`echarts` 6.1.0 的 Kotlin/JS wrapper。通用约定（类型映射、测试覆盖、版本与发布）在仓库根
[AGENTS.md](../AGENTS.md) 与 [TYPE-MAPPING.md](../TYPE-MAPPING.md)，这里只记本模块特有的建模取舍和
上游行为。

## 已确认的建模取舍（不要重复修）

类型建模已完成，下面这些看起来「可以更精确」的地方是有意为之：

- `LegendOption` 保留可滚动图例的分页字段（`pageIconColor`、`pageTextStyle`、`scrollDataIndex` 等）：
  `EChartsOption.legend` 是 `Array<LegendOption>`，拆出子类型后调用方拿不到分页字段。
- `LegendDataItemOption.tooltip`、`CustomSeriesRenderItemParams.itemPayload` / `context` 用 `JsAny`：
  上游就是 `unknown` / `Dictionary<unknown>`。
- `MapSeriesOption` 的 `markPoint` / `markLine` / `markArea`、`ComposeOption` /
  `RegisteredSeriesOption`、上游的 `*ComponentOption` 别名、`ActionInfo.update`、`Payload.type` 统一用
  具名类型或更宽类型，理由见各接口 KDoc。
- series 级 `itemStyle` / `label` 上游的 `Omit<...> & { ... }` 收窄不另加 `typedItemStyle`：精确类型本身
  是基类型的子接口，调用方 `unsafeJso<精确类型> { }` 即可写，读回来按需 `unsafeCast`。
- `coordinateSystem` 等关键字收窄、继承来的 `var` 收窄沿用 `@JsName` 同名字段（TYPE-MAPPING §12）。

## 同一 `data` 属性的类型化视图

上游一个 `data` 属性可能是对象数组、值数组或扁平 `ArrayLike<number>`。Kotlin 侧保持同一个 JS 属性，
另加类型化视图，不拆成多个字段：

- `ScatterSeriesOption.typedFlatData`、`LinesSeriesOption.typedFlatData`、`AppendDataParams.typedFlatData`：
  扁平表必须配 `dimensions` 使用（`polyline` 时每行 `[count, x0, y0, ...]`），百万级数据靠
  `appendData` 分片追加。
- `PieSeriesOption.typedCenter`：`center` 的标量分支（`string | number`），饼图落在 `geo` 上时用区名当地心。
- `GeoOption` 混入 `ComponentOption` 的 `coordinateSystem` / `coordinateSystemUsage` / `coord`、
  `BoxLayoutOptionMixin` 与 `AnimationOptionMixin`：`geo` 要能钉进矩阵格子并关掉自身动画。

## 类目型视觉映射的字典形态

上游把 `inRange` 声明成 `{ [key in BuiltinVisualProperty]?: any }`，字典只在实现里看得见，所以
`VisualMapOption.selected`（`Dictionary<boolean>`）与 `VisualOptionUnit` 的 `categorySymbol` /
`categorySymbolSize` / `categoryColor`（`symbol` / `symbolSize` / `color` 的 `@JsName` 同类视图）负责表达
字典形态。`preprocessForSpecifiedCategory` 会把没在字典里出现的类目落到 `-1` 槽，这是官方示例用空串
`''` 当默认值的原因。移植侧用法见 [demo/NOTES.md](../demo/NOTES.md)。

## 测试

- 上游单元测试的翻译状态见根 [AGENTS.md](../AGENTS.md) 的「上游单元测试的翻译状态」。
- 字段级覆盖用 `node scripts/field-coverage.mjs ktjs-echarts` 核对（最近一次：2704 个成员全部被用例
  点名，未点名 0 个）。`src/jsMain` 新增声明后重跑该脚本找新缺口；明确不做的成员写进脚本的
  `NOT_COVERED` 并附理由。
