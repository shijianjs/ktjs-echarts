package echarts.options

import echarts.SeriesTypes
import js.objects.unsafeJso

/**
 * Creates a [LineSeriesOption] with `type = [SeriesTypes.line]`.
 */
inline fun lineSeries(block: LineSeriesOption.() -> Unit = {}): LineSeriesOption =
    unsafeJso {
        type = SeriesTypes.`line`
        block()
    }

/**
 * Creates a [BarSeriesOption] with `type = [SeriesTypes.bar]`.
 */
inline fun barSeries(block: BarSeriesOption.() -> Unit = {}): BarSeriesOption =
    unsafeJso {
        type = SeriesTypes.`bar`
        block()
    }

/**
 * Creates a [PieSeriesOption] with `type = [SeriesTypes.pie]`.
 */
inline fun pieSeries(block: PieSeriesOption.() -> Unit = {}): PieSeriesOption =
    unsafeJso {
        type = SeriesTypes.`pie`
        block()
    }

/**
 * Creates a [ScatterSeriesOption] with `type = [SeriesTypes.scatter]`.
 */
inline fun scatterSeries(block: ScatterSeriesOption.() -> Unit = {}): ScatterSeriesOption =
    unsafeJso {
        type = SeriesTypes.`scatter`
        block()
    }

/**
 * Creates a [EffectScatterSeriesOption] with `type = [SeriesTypes.effectScatter]`.
 */
inline fun effectScatterSeries(block: EffectScatterSeriesOption.() -> Unit = {}): EffectScatterSeriesOption =
    unsafeJso {
        type = SeriesTypes.`effectScatter`
        block()
    }

/**
 * Creates a [RadarSeriesOption] with `type = [SeriesTypes.radar]`.
 */
inline fun radarSeries(block: RadarSeriesOption.() -> Unit = {}): RadarSeriesOption =
    unsafeJso {
        type = SeriesTypes.`radar`
        block()
    }

/**
 * Creates a [MapSeriesOption] with `type = [SeriesTypes.map]`.
 */
inline fun mapSeries(block: MapSeriesOption.() -> Unit = {}): MapSeriesOption =
    unsafeJso {
        type = SeriesTypes.`map`
        block()
    }

/**
 * Creates a [TreeSeriesOption] with `type = [SeriesTypes.tree]`.
 */
inline fun treeSeries(block: TreeSeriesOption.() -> Unit = {}): TreeSeriesOption =
    unsafeJso {
        type = SeriesTypes.`tree`
        block()
    }

/**
 * Creates a [TreemapSeriesOption] with `type = [SeriesTypes.treemap]`.
 */
inline fun treemapSeries(block: TreemapSeriesOption.() -> Unit = {}): TreemapSeriesOption =
    unsafeJso {
        type = SeriesTypes.`treemap`
        block()
    }

/**
 * Creates a [SunburstSeriesOption] with `type = [SeriesTypes.sunburst]`.
 */
inline fun sunburstSeries(block: SunburstSeriesOption.() -> Unit = {}): SunburstSeriesOption =
    unsafeJso {
        type = SeriesTypes.`sunburst`
        block()
    }

/**
 * Creates a [GraphSeriesOption] with `type = [SeriesTypes.graph]`.
 */
inline fun graphSeries(block: GraphSeriesOption.() -> Unit = {}): GraphSeriesOption =
    unsafeJso {
        type = SeriesTypes.`graph`
        block()
    }

/**
 * Creates a [ChordSeriesOption] with `type = [SeriesTypes.chord]`.
 */
inline fun chordSeries(block: ChordSeriesOption.() -> Unit = {}): ChordSeriesOption =
    unsafeJso {
        type = SeriesTypes.`chord`
        block()
    }

/**
 * Creates a [FunnelSeriesOption] with `type = [SeriesTypes.funnel]`.
 */
inline fun funnelSeries(block: FunnelSeriesOption.() -> Unit = {}): FunnelSeriesOption =
    unsafeJso {
        type = SeriesTypes.`funnel`
        block()
    }

/**
 * Creates a [GaugeSeriesOption] with `type = [SeriesTypes.gauge]`.
 */
inline fun gaugeSeries(block: GaugeSeriesOption.() -> Unit = {}): GaugeSeriesOption =
    unsafeJso {
        type = SeriesTypes.`gauge`
        block()
    }

/**
 * Creates a [PictorialBarSeriesOption] with `type = [SeriesTypes.pictorialBar]`.
 */
inline fun pictorialBarSeries(block: PictorialBarSeriesOption.() -> Unit = {}): PictorialBarSeriesOption =
    unsafeJso {
        type = SeriesTypes.`pictorialBar`
        block()
    }

/**
 * Creates a [ThemeRiverSeriesOption] with `type = [SeriesTypes.themeRiver]`.
 */
inline fun themeRiverSeries(block: ThemeRiverSeriesOption.() -> Unit = {}): ThemeRiverSeriesOption =
    unsafeJso {
        type = SeriesTypes.`themeRiver`
        block()
    }

/**
 * Creates a [CustomSeriesOption] with `type = [SeriesTypes.custom]`.
 */
inline fun customSeries(block: CustomSeriesOption.() -> Unit = {}): CustomSeriesOption =
    unsafeJso {
        type = SeriesTypes.`custom`
        block()
    }

/**
 * Creates a [BoxplotSeriesOption] with `type = [SeriesTypes.boxplot]`.
 */
inline fun boxplotSeries(block: BoxplotSeriesOption.() -> Unit = {}): BoxplotSeriesOption =
    unsafeJso {
        type = SeriesTypes.`boxplot`
        block()
    }

/**
 * Creates a [CandlestickSeriesOption] with `type = [SeriesTypes.candlestick]`.
 */
inline fun candlestickSeries(block: CandlestickSeriesOption.() -> Unit = {}): CandlestickSeriesOption =
    unsafeJso {
        type = SeriesTypes.`candlestick`
        block()
    }

/**
 * Creates a [HeatmapSeriesOption] with `type = [SeriesTypes.heatmap]`.
 */
inline fun heatmapSeries(block: HeatmapSeriesOption.() -> Unit = {}): HeatmapSeriesOption =
    unsafeJso {
        type = SeriesTypes.`heatmap`
        block()
    }

/**
 * Creates a [ParallelSeriesOption] with `type = [SeriesTypes.parallel]`.
 */
inline fun parallelSeries(block: ParallelSeriesOption.() -> Unit = {}): ParallelSeriesOption =
    unsafeJso {
        type = SeriesTypes.`parallel`
        block()
    }

/**
 * Creates a [LinesSeriesOption] with `type = [SeriesTypes.lines]`.
 */
inline fun linesSeries(block: LinesSeriesOption.() -> Unit = {}): LinesSeriesOption =
    unsafeJso {
        type = SeriesTypes.`lines`
        block()
    }

/**
 * Creates a [SankeySeriesOption] with `type = [SeriesTypes.sankey]`.
 */
inline fun sankeySeries(block: SankeySeriesOption.() -> Unit = {}): SankeySeriesOption =
    unsafeJso {
        type = SeriesTypes.`sankey`
        block()
    }
