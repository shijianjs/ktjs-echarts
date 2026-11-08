# kmp-echarts-json

Type-safe construction of ECharts option JSON for Kotlin Multiplatform.

The module models the ECharts option schema as `@Serializable` Kotlin classes, so an option is
built with normal property assignments and encoded with kotlinx.serialization. It is pure common
code: every declaration lives in `commonMain` and no JavaScript-only API is used. Targets: JVM,
JS (Node.js), Wasm/JS (Node.js), Linux x64 and Windows x64 (MinGW).

The version follows the ECharts option schema it models: `{echarts npm version}-kt.{revision}`.

## Installation

```kotlin
dependencies {
    implementation("io.github.shijianjs:kmp-echarts-json:6.1.0-kt.1")
}
```

This is an alternative to the Kotlin/JS wrapper [`ktjs-echarts`](../ktjs-echarts/README.md): that
module binds the npm package itself, while this one only produces the option payload as a string.
Pick this module when the option is built on the server (JVM, native, ...) or when the chart is
rendered by another process; the two can also be combined.

## Quick start

```kotlin
import io.github.shijianjs.ktjs.echarts.json.BooleanOrNumber
import io.github.shijianjs.ktjs.echarts.json.EChartsColor
import io.github.shijianjs.ktjs.echarts.json.GradientColorStop
import io.github.shijianjs.ktjs.echarts.json.areaStyle
import io.github.shijianjs.ktjs.echarts.json.categories
import io.github.shijianjs.ktjs.echarts.json.echartsOption
import io.github.shijianjs.ktjs.echarts.json.entries
import io.github.shijianjs.ktjs.echarts.json.line
import io.github.shijianjs.ktjs.echarts.json.legend
import io.github.shijianjs.ktjs.echarts.json.series
import io.github.shijianjs.ktjs.echarts.json.toJsonString
import io.github.shijianjs.ktjs.echarts.json.tooltip
import io.github.shijianjs.ktjs.echarts.json.values
import io.github.shijianjs.ktjs.echarts.json.xAxis
import io.github.shijianjs.ktjs.echarts.json.yAxis

val option = echartsOption {
    tooltip { trigger = TooltipTrigger.Axis }
    legend { entries("Revenue", "Cost") }
    xAxis {
        type = AxisType.Category
        categories("Jan", "Feb", "Mar")
    }
    yAxis { type = AxisType.Value }
    series {
        line {
            name = "Revenue"
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

Parse the string with `JSON.parse` and pass the result to `setOption`, or serialize straight to a
`JsonObject` and send that to the chart via your own transport.

## Unset properties stay absent

ECharts applies its defaults only to absent keys, and an explicit `null` is not the same as an
omitted key. Every model property is therefore nullable and initialized to `null`, and the shared
`Json` instance uses `encodeDefaults = false` plus `explicitNulls = false`:

```kotlin
echartsOption { }.toJsonString() // "{}"
```

Serialization is centralized in `EChartsJson`:

- `EChartsJson.format` — compact output used by `toJsonString()` / `EChartsJson.encode(...)`.
- `EChartsJson.pretty` — the same encoder with two-space indentation.
- `EChartsJson.encodeToJsonElement(option)` — returns the `JsonObject`.

## Typed values

Values that ECharts accepts in more than one JSON shape are modeled as sealed interfaces with
factory helpers, so the shape is visible at the call site:

| ECharts value | Kotlin |
| --- | --- |
| `number \| string` (`min`, `max`, `left`, ...) | `NumberOrString`, `num(0)`, `str("dataMax")` |
| `number \| string \| number[]` (`symbolSize`) | `NumberOrStringOrNumbers`, `of(10)`, `numbers(10, 20)` |
| `boolean \| string` (`darkMode`, `roam`) | `BooleanOrString.of(...)` |
| `boolean \| number` (`smooth`, `progressive`) | `BooleanOrNumber.of(...)` |
| `boolean \| [number, number]` (`boundaryGap`) | `BoundaryGap.of(...)` |
| colors, gradients, patterns | `EChartsColor.Css`, `LinearGradient`, `RadialGradient`, `Pattern` |

`series.data` entries are `DataValue`s: numbers, strings, booleans, `null` gaps, arrays (points and
rows) and typed items. `DataItem` covers the object form with per-point styles:

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

## Time values

Since the module is multiplatform, `kotlinx-datetime` values are converted explicitly. A `time`
axis accepts epoch milliseconds (from `kotlin.time.Instant`) or the string forms ECharts parses:

```kotlin
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

DataValue.InstantValue(Instant.fromEpochMilliseconds(1_700_000_000_000)) // epoch ms
Instant.fromEpochMilliseconds(0).asDataValue()
LocalDateTime.parse("2024-04-05T06:07:08").asDataValue() // "2024-04-05 06:07:08"
LocalDate.parse("2024-04-05").asDataValue()              // "2024-04-05"
```

## Series types

`SeriesType` is a value class, not an enum, so third-party series such as the `echarts-gl` 3D types
are emitted as well. Constants cover every series bundled with ECharts
(`SeriesType.Line`, `.Bar`, `.Pie`, `.Scatter`, `.Map`, ...):

```kotlin
series {
    add(SeriesType("scatter3D")) {
        name = "3D points"
        coordinateSystem = "geo3D"
    }
}
```

`SeriesOption` is one flat class with the optional fields of every series family. ECharts ignores
fields that do not apply to the selected `type`, and a single class keeps the model maintainable
while still type-checking every value.

## Components and the DSL

The `echartsOption { }` DSL adds small receiver functions for every top-level component
(`title`, `tooltip`, `legend`, `grid`, `xAxis`, `yAxis`, `polar`, `radar`, `dataZoom`, `visualMap`,
`toolbox`, `dataset`, `timeline`, `graphic`, `aria`, ...). Builder-style helpers keep nested
objects readable: `itemStyle { }`, `label { }`, `lineStyle { }`, `areaStyle { }`, `emphasis { }`,
`piece { }`, `rows(...)`, `columns(...)`, `values(...)`, `points(...)`, `items(...)`.

Every model is also a plain class, so the DSL is optional — construct the same tree directly or mix
both. The DSL only appends or assigns; it never re-renders anything.

## JSON escape hatch

For options this module does not model yet, the `JsonElement` helpers merge raw JSON into the
result:

```kotlin
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// Top-level keys, applied after the typed ones (extra wins on conflicts).
option.extra = buildJsonObject { put("myPlugin", 1) }

// Or work on any element.
val merged = option.toJsonElement()
    .with("backgroundColor", "transparent")
    .deepMerge(buildJsonObject { put("aria", buildJsonObject { put("enabled", true) }) })
```

Available: `toJsonElement()`, `with(key, value)`, `plus` (top-level map merge), `deepMerge`,
`prettyPrint()` and `toEChartsJsonString(prettyPrint)`.

## Dataset sources

Datasets accept row arrays, rows of named columns, named columns and any other JSON value:

```kotlin
dataset {
    rows(
        listOf(textValue("Jan"), valueOf(120)),
        listOf(textValue("Feb"), valueOf(200)),
    )
}
```

The same shapes are available directly: `datasetRows(...)`, `datasetNumbers(...)`,
`datasetColumns(...)` and `DatasetSource.Raw(element)`.

## Build and test

```bash
# All targets this host can run
./gradlew :kmp-echarts-json:build

# Individual targets
./gradlew :kmp-echarts-json:jvmTest
./gradlew :kmp-echarts-json:jsNodeTest
./gradlew :kmp-echarts-json:wasmJsNodeTest
./gradlew :kmp-echarts-json:mingwX64Test
```

The test suite is in `src/commonTest` and runs unchanged on every target. After changing the
module set or npm dependencies, refresh the shared lock file:

```bash
./gradlew kotlinUpgradePackageLock
```
