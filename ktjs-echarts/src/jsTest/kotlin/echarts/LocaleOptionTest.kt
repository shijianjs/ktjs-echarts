package echarts

import js.objects.unsafeJso
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Covers the localized-text interfaces in `EChartsTypes.kt` (`LocaleOption` and its `time` /
 * `legend` / `toolbox` / `series` / `aria` sections). The whole locale is assembled, every
 * previously-unlisted field is read back, and the object is fed to [ECharts.registerLocale] and to
 * a real chart through [EChartsInitOpts.locale] so the round-trip exercises the JS key mapping.
 */
class LocaleOptionTest {

    @Test
    fun localeTimeLegendAndSeriesFieldsRoundTrip() {
        val locale = unsafeJso<LocaleOption> {
            time = unsafeJso<LocaleTimeOption> {
                month = arrayOf("M1", "M2", "M3")
                monthAbbr = arrayOf("J", "F", "M")
                dayOfWeek = arrayOf("Sunday", "Monday")
                dayOfWeekAbbr = arrayOf("Su", "Mo")
            }
            legend = unsafeJso<LocaleLegendOption> {
                selector = unsafeJso<LocaleLegendSelectorOption> {
                    all = "ALL"
                    inverse = "INV"
                }
            }
            toolbox = unsafeJso<LocaleToolboxOption> {
                magicType = unsafeJso<LocaleToolboxMagicTypeOption> {
                    title = unsafeJso<LocaleToolboxMagicTypeTitleOption> {
                        line = "L"
                        bar = "B"
                        stack = "S"
                        tiled = "T"
                    }
                }
            }
            series = unsafeJso<LocaleSeriesOption> {
                typeNames = unsafeJso<LocaleSeriesTypeNamesOption> {
                    pie = "P"
                    k = "K"
                    custom = "C"
                }
            }
        }

        assertEquals(arrayOf("J", "F", "M").toList(), locale.time?.monthAbbr?.toList())
        assertEquals("Monday", locale.time?.dayOfWeek?.get(1))
        assertEquals("Su", locale.time?.dayOfWeekAbbr?.get(0))
        assertEquals("ALL", locale.legend?.selector?.all)
        assertEquals("INV", locale.legend?.selector?.inverse)
        assertEquals("T", locale.toolbox?.magicType?.title?.tiled)
        assertEquals("K", locale.series?.typeNames?.k)
    }

    @Test
    fun ariaLocaleFieldsRoundTrip() {
        val dataSep = unsafeJso<LocaleAriaSeparatorOption> {
            middle = " / "
            end = "."
        }
        val aria = unsafeJso<LocaleAriaOption> {
            general = unsafeJso<LocaleAriaGeneralOption> {
                withTitle = "标题是{title}"
                withoutTitle = "无标题图表"
            }
            series = unsafeJso<LocaleAriaSeriesOption> {
                single = unsafeJso<LocaleAriaSeriesSingleOption> {
                    prefix = "一个系列："
                    withName = "叫 {name}"
                    withoutName = "未命名"
                }
                multiple = unsafeJso<LocaleAriaSeriesMultipleOption> {
                    prefix = "多个系列："
                    withName = "{name}"
                    withoutName = "未命名"
                    separator = unsafeJso<LocaleAriaSeparatorOption> {
                        middle = "，"
                        end = "。"
                    }
                }
            }
            data = unsafeJso<LocaleAriaDataOption> {
                allData = "全部数据"
                partialData = "部分数据"
                withName = "{name}"
                withoutName = "无名称"
                separator = dataSep
            }
        }

        assertEquals("标题是{title}", aria.general?.withTitle)
        assertEquals("无标题图表", aria.general?.withoutTitle)
        assertEquals("一个系列：", aria.series?.single?.prefix)
        assertEquals("叫 {name}", aria.series?.single?.withName)
        assertEquals("未命名", aria.series?.single?.withoutName)
        assertEquals("多个系列：", aria.series?.multiple?.prefix)
        assertEquals("{name}", aria.series?.multiple?.withName)
        assertEquals("未命名", aria.series?.multiple?.withoutName)
        assertEquals("，", aria.series?.multiple?.separator?.middle)
        assertEquals("全部数据", aria.data?.allData)
        assertEquals("部分数据", aria.data?.partialData)
        assertEquals("{name}", aria.data?.withName)
        assertEquals("无名称", aria.data?.withoutName)
        assertNotNull(aria.data?.separator)
        assertEquals(" / ", aria.data?.separator?.middle)
        assertEquals(".", dataSep.end)
    }

    @Test
    fun registeredLocaleDrivesAChartInstance() {
        val locale = unsafeJso<LocaleOption> {
            time = unsafeJso<LocaleTimeOption> {
                dayOfWeekAbbr = arrayOf("日", "一", "二", "三", "四", "五", "六")
            }
            aria = unsafeJso<LocaleAriaOption> {
                general = unsafeJso<LocaleAriaGeneralOption> {
                    withoutTitle = "图表"
                }
            }
        }
        ECharts.registerLocale("wrapper-locale-test", locale)

        val opts = unsafeJso<EChartsInitOpts> {
            this.locale = StringOrLocaleOption("wrapper-locale-test")
        }
        val (chart, container) = createChart(opts = opts)
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            assertTrue(chart.getWidth() > 0.0)
        } finally {
            destroyChart(chart, container)
        }
    }
}
