package echarts.options

/**
 * Creates a [LineSeriesOption] with `type = "line"`.
 */
inline fun lineSeries(block: LineSeriesOption.() -> Unit = {}): LineSeriesOption =
    echartsOption<LineSeriesOption> { type = "line"; block() }

/**
 * Creates a [BarSeriesOption] with `type = "bar"`.
 */
inline fun barSeries(block: BarSeriesOption.() -> Unit = {}): BarSeriesOption =
    echartsOption<BarSeriesOption> { type = "bar"; block() }

/**
 * Creates a [PieSeriesOption] with `type = "pie"`.
 */
inline fun pieSeries(block: PieSeriesOption.() -> Unit = {}): PieSeriesOption =
    echartsOption<PieSeriesOption> { type = "pie"; block() }

/**
 * Creates a [ScatterSeriesOption] with `type = "scatter"`.
 */
inline fun scatterSeries(block: ScatterSeriesOption.() -> Unit = {}): ScatterSeriesOption =
    echartsOption<ScatterSeriesOption> { type = "scatter"; block() }

/**
 * Creates an [EffectScatterSeriesOption] with `type = "effectScatter"`.
 */
inline fun effectScatterSeries(block: EffectScatterSeriesOption.() -> Unit = {}): EffectScatterSeriesOption =
    echartsOption<EffectScatterSeriesOption> { type = "effectScatter"; block() }

/**
 * Creates a [RadarSeriesOption] with `type = "radar"`.
 */
inline fun radarSeries(block: RadarSeriesOption.() -> Unit = {}): RadarSeriesOption =
    echartsOption<RadarSeriesOption> { type = "radar"; block() }

/**
 * Creates a [MapSeriesOption] with `type = "map"`.
 */
inline fun mapSeries(block: MapSeriesOption.() -> Unit = {}): MapSeriesOption =
    echartsOption<MapSeriesOption> { type = "map"; block() }

/**
 * Creates a [TreeSeriesOption] with `type = "tree"`.
 */
inline fun treeSeries(block: TreeSeriesOption.() -> Unit = {}): TreeSeriesOption =
    echartsOption<TreeSeriesOption> { type = "tree"; block() }

/**
 * Creates a [TreemapSeriesOption] with `type = "treemap"`.
 */
inline fun treemapSeries(block: TreemapSeriesOption.() -> Unit = {}): TreemapSeriesOption =
    echartsOption<TreemapSeriesOption> { type = "treemap"; block() }

/**
 * Creates a [SunburstSeriesOption] with `type = "sunburst"`.
 */
inline fun sunburstSeries(block: SunburstSeriesOption.() -> Unit = {}): SunburstSeriesOption =
    echartsOption<SunburstSeriesOption> { type = "sunburst"; block() }

/**
 * Creates a [GraphSeriesOption] with `type = "graph"`.
 */
inline fun graphSeries(block: GraphSeriesOption.() -> Unit = {}): GraphSeriesOption =
    echartsOption<GraphSeriesOption> { type = "graph"; block() }

/**
 * Creates a [ChordSeriesOption] with `type = "chord"`.
 */
inline fun chordSeries(block: ChordSeriesOption.() -> Unit = {}): ChordSeriesOption =
    echartsOption<ChordSeriesOption> { type = "chord"; block() }

/**
 * Creates a [FunnelSeriesOption] with `type = "funnel"`.
 */
inline fun funnelSeries(block: FunnelSeriesOption.() -> Unit = {}): FunnelSeriesOption =
    echartsOption<FunnelSeriesOption> { type = "funnel"; block() }

/**
 * Creates a [GaugeSeriesOption] with `type = "gauge"`.
 */
inline fun gaugeSeries(block: GaugeSeriesOption.() -> Unit = {}): GaugeSeriesOption =
    echartsOption<GaugeSeriesOption> { type = "gauge"; block() }

/**
 * Creates a [PictorialBarSeriesOption] with `type = "pictorialBar"`.
 */
inline fun pictorialBarSeries(block: PictorialBarSeriesOption.() -> Unit = {}): PictorialBarSeriesOption =
    echartsOption<PictorialBarSeriesOption> { type = "pictorialBar"; block() }

/**
 * Creates a [ThemeRiverSeriesOption] with `type = "themeRiver"`.
 */
inline fun themeRiverSeries(block: ThemeRiverSeriesOption.() -> Unit = {}): ThemeRiverSeriesOption =
    echartsOption<ThemeRiverSeriesOption> { type = "themeRiver"; block() }

/**
 * Creates a [CustomSeriesOption] with `type = "custom"`.
 */
inline fun customSeries(block: CustomSeriesOption.() -> Unit = {}): CustomSeriesOption =
    echartsOption<CustomSeriesOption> { type = "custom"; block() }

/**
 * Creates a [BoxplotSeriesOption] with `type = "boxplot"`.
 */
inline fun boxplotSeries(block: BoxplotSeriesOption.() -> Unit = {}): BoxplotSeriesOption =
    echartsOption<BoxplotSeriesOption> { type = "boxplot"; block() }

/**
 * Creates a [CandlestickSeriesOption] with `type = "candlestick"`.
 */
inline fun candlestickSeries(block: CandlestickSeriesOption.() -> Unit = {}): CandlestickSeriesOption =
    echartsOption<CandlestickSeriesOption> { type = "candlestick"; block() }

/**
 * Creates a [HeatmapSeriesOption] with `type = "heatmap"`.
 */
inline fun heatmapSeries(block: HeatmapSeriesOption.() -> Unit = {}): HeatmapSeriesOption =
    echartsOption<HeatmapSeriesOption> { type = "heatmap"; block() }

/**
 * Creates a [ParallelSeriesOption] with `type = "parallel"`.
 */
inline fun parallelSeries(block: ParallelSeriesOption.() -> Unit = {}): ParallelSeriesOption =
    echartsOption<ParallelSeriesOption> { type = "parallel"; block() }

/**
 * Creates a [LinesSeriesOption] with `type = "lines"`.
 */
inline fun linesSeries(block: LinesSeriesOption.() -> Unit = {}): LinesSeriesOption =
    echartsOption<LinesSeriesOption> { type = "lines"; block() }

/**
 * Creates a [SankeySeriesOption] with `type = "sankey"`.
 */
inline fun sankeySeries(block: SankeySeriesOption.() -> Unit = {}): SankeySeriesOption =
    echartsOption<SankeySeriesOption> { type = "sankey"; block() }
