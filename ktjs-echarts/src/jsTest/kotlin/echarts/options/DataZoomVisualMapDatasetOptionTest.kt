package echarts.options

import echarts.AxisTypes
import echarts.createChart
import echarts.DataZoomTypes
import echarts.destroyChart
import echarts.VisualMapTypes
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zrender.ZRColor

/**
 * Tests for the structural component options of `dataZoom`, `visualMap` and `dataset`.
 *
 * These fields model the parts of the upstream `.d.ts` that go beyond the shared box-layout mixin:
 * the slider handle styling and window spans of [DataZoomOption], the piece/controller fields of
 * [VisualMapOption] and the derived-dataset pipeline fields of [DatasetOption]. Each case builds the
 * option through [unsafeJso] and reads every field back, so the typed property keeps mapping to the
 * exact JavaScript key and the union wrappers accept their forms. The last case feeds a real
 * `setOption` to prove ECharts itself stores the three components together.
 */
class DataZoomVisualMapDatasetOptionTest {
    @Test
    fun dataZoomSliderStyleAndSpanFieldsRoundTrip() {
        val dataZoom = unsafeJso<DataZoomOption> {
            type = DataZoomTypes.slider
            fillerColor = ZRColor("rgba(0, 0, 255, 0.2)")
            dataBackground = unsafeJso<DataZoomDataBackgroundOption> {
                lineStyle = unsafeJso<LineStyleOption> { width = 1.0 }
                areaStyle = unsafeJso<AreaStyleOption> { opacity = 0.3 }
            }
            selectedDataBackground = unsafeJso<DataZoomDataBackgroundOption> {
                lineStyle = unsafeJso<LineStyleOption> { width = 2.0 }
            }
            handleIcon = "M0,0"
            handleSize = StringOrDouble(20.0)
            handleStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
            moveHandleStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#eee") }
            moveHandleSize = 5.0
            labelFormatter = LabelFormatter("{value}")
            radiusAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
            angleAxisIndex = IndexOrIndexArray(0.0)
            endValue = DataZoomValue(30.0)
            minSpan = 5.0
            maxSpan = 90.0
            minValueSpan = 2.0
            maxValueSpan = 40.0
            zoomLock = true
            brushSelect = true
            showDetail = false
            preventDefaultMouseMove = true
        }

        assertEquals("rgba(0, 0, 255, 0.2)", dataZoom.fillerColor!!.unsafeCast<String>())
        assertEquals(1.0, assertNotNull(dataZoom.dataBackground).lineStyle!!.width)
        assertEquals(0.3, assertNotNull(dataZoom.dataBackground).areaStyle!!.opacity)
        assertEquals(2.0, assertNotNull(dataZoom.selectedDataBackground).lineStyle!!.width)
        assertEquals("M0,0", dataZoom.handleIcon)
        assertEquals(20.0, dataZoom.handleSize!!.unsafeCast<Double>())
        assertEquals("#fff", dataZoom.handleStyle!!.color!!.unsafeCast<String>())
        assertEquals("#eee", dataZoom.moveHandleStyle!!.color!!.unsafeCast<String>())
        assertEquals(5.0, dataZoom.moveHandleSize)
        assertEquals("{value}", dataZoom.labelFormatter!!.unsafeCast<String>())
        assertEquals(2, dataZoom.radiusAxisIndex!!.unsafeCast<Array<Double>>().size)
        assertEquals(0.0, dataZoom.angleAxisIndex!!.unsafeCast<Double>())
        assertEquals(30.0, dataZoom.endValue!!.unsafeCast<Double>())
        assertEquals(5.0, dataZoom.minSpan)
        assertEquals(90.0, dataZoom.maxSpan)
        assertEquals(2.0, dataZoom.minValueSpan)
        assertEquals(40.0, dataZoom.maxValueSpan)
        assertTrue(dataZoom.zoomLock!!)
        assertTrue(dataZoom.brushSelect!!)
        assertEquals(false, dataZoom.showDetail)
        assertTrue(dataZoom.preventDefaultMouseMove!!)
    }

    @Test
    fun visualMapPiecewiseAndControllerFieldsRoundTrip() {
        val visualMap = unsafeJso<VisualMapOption> {
            type = VisualMapTypes.piecewise
            min = 0.0
            max = 100.0
            minOpen = true
            maxOpen = true
            calculable = true
            itemWidth = 20.0
            itemHeight = 14.0
            textGap = 10.0
            seriesTargets = arrayOf(
                unsafeJso<VisualMapSeriesTarget> {
                    seriesIndex = 0.0
                    dimension = 1.0
                },
            )
            hoverLink = true
            splitNumber = 5.0
            pieces = arrayOf(
                unsafeJso<VisualPiece> {
                    min = 0.0
                    max = 50.0
                    label = "low"
                },
            )
            itemGap = 8.0
            contentColor = ZRColor("#333")
            padding = DoubleOrDoubleArray(arrayOf(5.0, 10.0))
            showLabel = false
            selectedMode = SelectionModes.single
        }

        assertEquals(true, visualMap.minOpen)
        assertEquals(true, visualMap.maxOpen)
        assertTrue(visualMap.calculable!!)
        assertEquals(20.0, visualMap.itemWidth)
        assertEquals(14.0, visualMap.itemHeight)
        assertEquals(10.0, visualMap.textGap)
        assertEquals(1, visualMap.seriesTargets!!.size)
        assertEquals(1.0, visualMap.seriesTargets!![0].dimension)
        assertTrue(visualMap.hoverLink!!)
        assertEquals(5.0, visualMap.splitNumber)
        assertEquals(1, visualMap.pieces!!.size)
        assertEquals("low", visualMap.pieces!![0].label)
        assertEquals(8.0, visualMap.itemGap)
        assertEquals("#333", visualMap.contentColor!!.unsafeCast<String>())
        assertEquals(2, visualMap.padding!!.unsafeCast<Array<Double>>().size)
        assertEquals(false, visualMap.showLabel)
        assertEquals("single", visualMap.selectedMode!!.unsafeCast<String>())
    }

    @Test
    fun derivedDatasetFieldsRoundTrip() {
        val dataset = unsafeJso<DatasetOption> {
            id = "derived"
            sourceHeader = OptionSourceHeader(true)
            fromDatasetIndex = 0.0
            fromDatasetId = "source"
            fromTransformResult = 1.0
        }

        assertEquals(true, dataset.sourceHeader!!.unsafeCast<Boolean>())
        assertEquals(0.0, dataset.fromDatasetIndex)
        assertEquals("source", dataset.fromDatasetId)
        assertEquals(1.0, dataset.fromTransformResult)
    }

    @Test
    fun chartStoresDataZoomVisualMapAndDatasetTogether() {
        val dataZoom = unsafeJso<DataZoomOption> {
            type = DataZoomTypes.slider
            xAxisIndex = IndexOrIndexArray(0.0)
            fillerColor = ZRColor("rgba(0, 0, 255, 0.2)")
            brushSelect = true
        }
        val visualMap = unsafeJso<VisualMapOption> {
            type = VisualMapTypes.continuous
            min = 0.0
            max = 300.0
            calculable = true
            seriesIndex = IndexOrIndexArray(0.0)
        }
        val dataset = unsafeJso<DatasetOption> {
            id = "prices"
            sourceHeader = OptionSourceHeader(true)
            fromDatasetIndex = 0.0
            fromDatasetId = "raw"
            fromTransformResult = 1.0
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    this.dataZoom = one(dataZoom)
                    this.visualMap = one(visualMap)
                    this.dataset = one(dataset)
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(
                        lineSeries {
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                    )
                },
            )

            val stored = chart.getOption()
            val storedDataZoom = assertNotNull(stored.dataZoom)[0].unsafeCast<DataZoomOption>()
            assertEquals(DataZoomTypes.slider, storedDataZoom.type)
            assertEquals(true, storedDataZoom.brushSelect)
            val storedVisualMap = assertNotNull(stored.visualMap)[0].unsafeCast<VisualMapOption>()
            assertEquals(true, storedVisualMap.calculable)
            val storedDataset = assertNotNull(stored.dataset)[0].unsafeCast<DatasetOption>()
            assertEquals("raw", storedDataset.fromDatasetId)
            assertEquals(1.0, storedDataset.fromTransformResult)
        } finally {
            destroyChart(chart, container)
        }
    }
}
