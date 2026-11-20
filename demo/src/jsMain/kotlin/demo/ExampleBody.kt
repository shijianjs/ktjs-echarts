package demo

import demo.json.JsonExample
import demo.ui.ChartHolder
import io.github.shijianjs.ktjs.echarts.EChartsInstance
import io.github.shijianjs.ktjs.echarts.options.EChartsOption

sealed interface ExampleBody {
    /**
     * A chart fed by a typed option.
     *
     * [json], when the example also has a `kmp-echarts-json` builder, is the same option serialized by
     * that model: the page shows it and can feed the very chart with it, which is what proves the two
     * builders agree.
     */
    class Option(
        val option: () -> EChartsOption,
        val height: String? = null,
        val json: JsonExample? = null,
    ) : ExampleBody

    /** A chart the example drives itself: animation, timeline, user interaction, ... */
    class Interactive(
        val setup: (EChartsInstance, ChartHolder) -> Unit,
        val height: String? = null,
    ) : ExampleBody

    /** A page built from the KMP model: the chart consumes the JSON, both sources are shown. */
    class Model(
        val json: String,
        val kotlinSource: String,
    ) : ExampleBody

    /** A page that builds its own body instead of the shared example frame (the overview). */
    data object Custom : ExampleBody

}
