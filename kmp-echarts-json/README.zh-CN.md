# kmp-echarts-json

Kotlin Multiplatform 的类型安全 ECharts 选项 JSON 构造库。

模块把 ECharts 的选项结构建模成 `@Serializable` 的 Kotlin 类：用普通的属性赋值构造选项，再用
kotlinx.serialization 编码成字符串。实现全部位于 `commonMain`，不依赖任何 JS 专属 API。目标平台：
JVM、JS（Node.js）、Wasm/JS（Node.js）、Linux x64、Windows x64（MinGW）。

版本号跟随它建模的 ECharts 选项结构：`{echarts npm 版本}-kt.{修订号}`。

## 安装

```kotlin
dependencies {
    implementation("io.github.shijianjs:kmp-echarts-json:6.1.0-kt.1")
}
```

它与 Kotlin/JS wrapper [`ktjs-echarts`](../ktjs-echarts/README.zh-CN.md) 是两种选择：后者绑定 npm
包本身，本模块只负责生成选项载荷。选项在服务端（JVM、native 等）构造、或图表由另一个进程渲染时用
本模块；两者也可以配合使用。

## 快速开始

```kotlin
import io.github.shijianjs.kmpechartsjson.BooleanOrNumber
import io.github.shijianjs.kmpechartsjson.EChartsColor
import io.github.shijianjs.kmpechartsjson.GradientColorStop
import io.github.shijianjs.kmpechartsjson.areaStyle
import io.github.shijianjs.kmpechartsjson.categories
import io.github.shijianjs.kmpechartsjson.echartsOption
import io.github.shijianjs.kmpechartsjson.entries
import io.github.shijianjs.kmpechartsjson.legend
import io.github.shijianjs.kmpechartsjson.line
import io.github.shijianjs.kmpechartsjson.series
import io.github.shijianjs.kmpechartsjson.toJsonString
import io.github.shijianjs.kmpechartsjson.tooltip
import io.github.shijianjs.kmpechartsjson.values
import io.github.shijianjs.kmpechartsjson.xAxis
import io.github.shijianjs.kmpechartsjson.yAxis

val option = echartsOption {
    tooltip { trigger = TooltipTrigger.Axis }
    legend { entries("营收", "成本") }
    xAxis {
        type = AxisType.Category
        categories("1月", "2月", "3月")
    }
    yAxis { type = AxisType.Value }
    series {
        line {
            name = "营收"
            values(120, 200, 150)
            smooth = BooleanOrNumber.of(true)
            areaStyle {
                color = EChartsColor.LinearGradient(
                    x = 0.0,
                    y = 0.0,
                    x2 = 0.0,
                    y2 = 1.0,
                    colorStops = listOf(
                        GradientColorStop(0.0, "rgba(58, 120, 240, .4)"),
                        GradientColorStop(1.0, "rgba(58, 120, 240, 0)"),
                    ),
                )
            }
        }
    }
}

val json: String = option.toJsonString(prettyPrint = true)
```

把字符串交给 `JSON.parse`，再把结果传给 `setOption`；也可以直接生成 `JsonObject`，通过自己的传输方式
送到前端。

## 未设置的字段不会出现

ECharts 只对「缺失」的字段套用默认值，显式 `null` 与缺失并不等价。因此所有模型属性都是可空的并初始化为
`null`，共享的 `Json` 实例使用 `encodeDefaults = false` 与 `explicitNulls = false`：

```kotlin
echartsOption { }.toJsonString() // "{}"
```

序列化入口集中在 `EChartsJson`：

- `EChartsJson.format`：紧凑输出，`toJsonString()` / `EChartsJson.encode(...)` 使用它。
- `EChartsJson.pretty`：同样配置、两空格缩进。
- `EChartsJson.encodeToJsonElement(option)`：返回 `JsonObject`。

## 带类型的取值

ECharts 中允许多种 JSON 形态的取值被建模为 sealed interface 加工厂函数，形态在调用处一目了然：

| ECharts 取值 | Kotlin |
| --- | --- |
| `number \| string`（`min`、`max`、`left` 等） | `NumberOrString`，`num(0)`、`str("dataMax")` |
| `number \| string \| number[]`（`symbolSize`） | `NumberOrStringOrNumbers`，`of(10)`、`numbers(10, 20)` |
| `boolean \| string`（`darkMode`、`roam`） | `BooleanOrString.of(...)` |
| `boolean \| number`（`smooth`、`progressive`） | `BooleanOrNumber.of(...)` |
| `boolean \| [number, number]`（`boundaryGap`） | `BoundaryGap.of(...)` |
| 颜色、渐变、图案 | `EChartsColor.Css`、`LinearGradient`、`RadialGradient`、`Pattern` |

`series.data` 的条目是 `DataValue`：数字、字符串、布尔、表示断点的 `null`、数组（点与行）以及带类型的
条目。对象形态由 `DataItem` 表达，可以给单个数据点设置样式：

```kotlin
series {
    pie {
        items(
            DataItem().apply {
                name = "A"
                value = valueOf(3)
                itemStyle { color = "red".asColor() }
            },
        )
    }
}
```

## 时间取值

因为模块是全平台的，`kotlinx-datetime` 的值需要显式转换。`time` 轴接受 epoch 毫秒
（`kotlin.time.Instant`），或 ECharts 能解析的字符串：

```kotlin
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

DataValue.InstantValue(Instant.fromEpochMilliseconds(1_700_000_000_000)) // epoch 毫秒
Instant.fromEpochMilliseconds(0).asDataValue()
LocalDateTime.parse("2024-04-05T06:07:08").asDataValue() // "2024-04-05 06:07:08"
LocalDate.parse("2024-04-05").asDataValue()              // "2024-04-05"
```

## 系列类型

`SeriesType` 是 value class 而不是 enum，因此 `echarts-gl` 的 3D 系列等第三方类型也能写出来。常量覆盖
ECharts 自带的全部系列（`SeriesType.Line`、`.Bar`、`.Pie`、`.Scatter`、`.Map` 等）：

```kotlin
series {
    add(SeriesType("scatter3D")) {
        name = "3D points"
        coordinateSystem = "geo3D"
    }
}
```

`SeriesOption` 是一个扁平类，包含各系列族的可选字段。ECharts 会忽略与 `type` 无关的字段，单个类让模型
容易维护，同时每个取值仍受类型检查。

## 组件与 DSL

`echartsOption { }` DSL 为每个顶层组件提供 receiver 函数（`title`、`tooltip`、`legend`、`grid`、
`xAxis`、`yAxis`、`polar`、`radar`、`dataZoom`、`visualMap`、`toolbox`、`dataset`、`timeline`、
`graphic`、`aria` 等）。嵌套对象同样有便捷构造：`itemStyle { }`、`label { }`、`lineStyle { }`、
`areaStyle { }`、`emphasis { }`、`piece { }`、`rows(...)`、`columns(...)`、`values(...)`、
`points(...)`、`items(...)`。

所有模型同时都是普通类，DSL 只是可选的语法糖：也可以直接构造同一棵树，或两者混用。DSL 只做追加与赋值，
不做任何重绘。

## JSON 逃生口

对模块尚未建模的选项，可以用 `JsonElement` 扩展把原始 JSON 合并进来：

```kotlin
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// 顶层键，在类型化字段之后合并（冲突时 extra 优先）。
option.extra = buildJsonObject { put("myPlugin", 1) }

// 也可以直接在任意元素上操作。
val merged = option.toJsonElement()
    .with("backgroundColor", "transparent")
    .deepMerge(buildJsonObject { put("aria", buildJsonObject { put("enabled", true) }) })
```

可用：`toJsonElement()`、`with(key, value)`、`plus`（顶层 map 合并）、`deepMerge`、`prettyPrint()`、
`toEChartsJsonString(prettyPrint)`。

## 数据集来源

数据集支持行数组、命名列的对象行、按列的字典以及其他任意 JSON 值：

```kotlin
dataset {
    rows(
        listOf(textValue("1月"), valueOf(120)),
        listOf(textValue("2月"), valueOf(200)),
    )
}
```

同样的形态也可以直接构造：`datasetRows(...)`、`datasetNumbers(...)`、`datasetColumns(...)`、
`DatasetSource.Raw(element)`。

## 构建与测试

```bash
# 当前主机能运行的全部目标
./gradlew :kmp-echarts-json:build

# 单个目标
./gradlew :kmp-echarts-json:jvmTest
./gradlew :kmp-echarts-json:jsNodeTest
./gradlew :kmp-echarts-json:wasmJsNodeTest
./gradlew :kmp-echarts-json:mingwX64Test
```

测试位于 `src/commonTest`，在每个目标上原样运行。新增模块或修改 npm 依赖后刷新共享锁文件：

```bash
./gradlew kotlinUpgradePackageLock
```
