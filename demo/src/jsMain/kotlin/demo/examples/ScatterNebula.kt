package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadFloat32
import echarts.DataZoomType
import echarts.DataZoomTypes
import echarts.ECharts
import echarts.Orients
import echarts.options.BooleanOrString
import echarts.options.DataZoomOption
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.ItemStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.ToolboxDataZoomFeatureOption
import echarts.options.ToolboxFeature
import echarts.options.ToolboxOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.one
import echarts.options.scatterSeries
import js.buffer.ArrayBuffer
import js.objects.unsafeJso
import js.typedarrays.Float32Array

/*
 * `scatter-nebula` of `examples/ts/scatter-nebula.ts`: one million points read from an 8 MB binary
 * file of 32-bit floats.
 *
 * The points arrive as a `Float32Array` and are handed to ECharts as they are — that is upstream's
 * `data?: ... | ArrayLike<number>` branch, which needs `dimensions` and skips the per-point objects
 * of the normal data path. The file lives in `resources/data/fake-nebula.bin` (see `data/SOURCE.md`)
 * and `loadFloat32` reads it into a typed array.
 */

private val scatterNebula = Category(Groups.SCATTER, "examples/ScatterNebula.kt")

/** The handle icon both sliders of the page draw. */
private const val HANDLE_ICON =
    "path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3" +
        "c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z " +
        "M13.3,19.6H6.7v-1.4h6.6V19.6z"

/** One of the four data zooms: two drag the x range, two the y range. */
private fun nebulaZoom(
    type: DataZoomType,
    vertical: Boolean,
): DataZoomOption =
    unsafeJso {
        this.type = type
        if (vertical) orient = Orients.vertical
        if (type == DataZoomTypes.slider) {
            showDataShadow = BooleanOrString(false)
            handleIcon = HANDLE_ICON
            handleSize = StringOrDouble("80%")
        }
    }

/** The option of the page. [points] is the flat `[x0, y0, x1, y1, ...]` table. */
private fun nebulaOption(points: Float32Array<ArrayBuffer>): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    left = StringOrDouble("center")
                    text =
                        ECharts.format.addCommas(StringOrDouble((points.length / 2).toDouble())) +
                            " Points"
                    subtext = "Fake data"
                },
            )
        tooltip = one(unsafeJso<TooltipOption> {})
        toolbox =
            one(
                unsafeJso<ToolboxOption> {
                    right = StringOrDouble(20.0)
                    feature =
                        unsafeJso<ToolboxFeature> {
                            dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> {}
                        }
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    right = StringOrDouble(70.0)
                    bottom = StringOrDouble(70.0)
                },
            )
        xAxis = one(unsafeJso<XAxisOption> {})
        yAxis = one(unsafeJso<YAxisOption> {})
        dataZoom =
            arrayOf(
                nebulaZoom(DataZoomTypes.inside, vertical = false),
                nebulaZoom(DataZoomTypes.slider, vertical = false),
                nebulaZoom(DataZoomTypes.inside, vertical = true),
                nebulaZoom(DataZoomTypes.slider, vertical = true),
            )
        animation = false
        series =
            one<SeriesOption>(
                scatterSeries {
                    dimensions = arrayOf(DimensionDefinitionLoose("x"), DimensionDefinitionLoose("y"))
                    typedFlatData = points
                    symbolSize = SymbolSize(3.0)
                    itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.4 }
                    blendMode = "source-over"
                    large = true
                    largeThreshold = 500.0
                },
            )
    }

val scatterNebulaEntries: List<CatalogEntry> =
    listOf(
        scatterNebula.interactive(
            "scatter-nebula",
            "大规模星云散点图",
            "Scatter Nebula",
            "一百万点星云：8 MB 二进制浮点表直接作为 typed array 交给 large 模式的散点系列，四周各一个 dataZoom。",
            "A one-million-point nebula: an 8 MB binary float table handed to a large-mode scatter " +
                "series as a typed array, with a data zoom on each side.",
        ) { chart ->
            loadFloat32("data/fake-nebula.bin").then { points ->
                chart.setOption(nebulaOption(points))
            }
        },
    )
