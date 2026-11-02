package demo.examples.scatter

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
import demo.examples.gauge.center
import demo.examples.shared.toolbox

/** The handle icon both sliders of the page draw. */
internal const val HANDLE_ICON =
    "path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3" +
        "c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z " +
        "M13.3,19.6H6.7v-1.4h6.6V19.6z"

/** One of the four data zooms: two drag the x range, two the y range. */
internal fun nebulaZoom(
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
internal fun nebulaOption(points: Float32Array<ArrayBuffer>): EChartsOption =
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
