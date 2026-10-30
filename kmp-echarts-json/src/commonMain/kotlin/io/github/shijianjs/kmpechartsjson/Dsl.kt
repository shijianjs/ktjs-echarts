package io.github.shijianjs.kmpechartsjson

/** Builds an option with a receiver DSL. */
public inline fun echartsOption(block: EChartsOption.() -> Unit): EChartsOption =
    EChartsOption().apply(block)

/** Builds a color palette from CSS color strings. */
public fun colors(vararg values: String): List<EChartsColor> = values.map { it.asColor() }

/** Builds a list of number-or-string values, for example axis positions. */
public fun numbers(vararg values: Number): List<NumberOrString> = values.map(::num)

/** Builds a list of number-or-string values from strings such as `"50%"`. */
public fun strings(vararg values: String): List<NumberOrString> = values.map(::str)

/** Adds a title; calling it again appends another title. */
public fun EChartsOption.title(block: TitleOption.() -> Unit) {
    title = title.orEmpty() + TitleOption().apply(block)
}

/** Adds a tooltip; calling it again appends another tooltip. */
public fun EChartsOption.tooltip(block: TooltipOption.() -> Unit) {
    tooltip = tooltip.orEmpty() + TooltipOption().apply(block)
}

/** Adds a legend. */
public fun EChartsOption.legend(block: LegendOption.() -> Unit) {
    legend = legend.orEmpty() + LegendOption().apply(block)
}

/** Adds a grid. */
public fun EChartsOption.grid(block: GridOption.() -> Unit) {
    grid = grid.orEmpty() + GridOption().apply(block)
}

/** Adds an x axis. */
public fun EChartsOption.xAxis(block: AxisOption.() -> Unit) {
    xAxis = xAxis.orEmpty() + AxisOption().apply(block)
}

/** Adds a y axis. */
public fun EChartsOption.yAxis(block: AxisOption.() -> Unit) {
    yAxis = yAxis.orEmpty() + AxisOption().apply(block)
}

/** Adds a polar coordinate system. */
public fun EChartsOption.polar(block: PolarOption.() -> Unit) {
    polar = polar.orEmpty() + PolarOption().apply(block)
}

/** Adds a radius axis of a polar coordinate system. */
public fun EChartsOption.radiusAxis(block: AxisOption.() -> Unit) {
    radiusAxis = radiusAxis.orEmpty() + AxisOption().apply(block)
}

/** Adds an angle axis of a polar coordinate system. */
public fun EChartsOption.angleAxis(block: AxisOption.() -> Unit) {
    angleAxis = angleAxis.orEmpty() + AxisOption().apply(block)
}

/** Adds a radar coordinate system. */
public fun EChartsOption.radar(block: RadarOption.() -> Unit) {
    radar = radar.orEmpty() + RadarOption().apply(block)
}

/** Adds a single axis. */
public fun EChartsOption.singleAxis(block: AxisOption.() -> Unit) {
    singleAxis = singleAxis.orEmpty() + AxisOption().apply(block)
}

/** Adds a parallel coordinate system. */
public fun EChartsOption.parallel(block: ParallelOption.() -> Unit) {
    parallel = parallel.orEmpty() + ParallelOption().apply(block)
}

/** Adds an axis of a parallel coordinate system. */
public fun EChartsOption.parallelAxis(block: AxisOption.() -> Unit) {
    parallelAxis = parallelAxis.orEmpty() + AxisOption().apply(block)
}

/** Adds a calendar. */
public fun EChartsOption.calendar(block: CalendarOption.() -> Unit) {
    calendar = calendar.orEmpty() + CalendarOption().apply(block)
}

/** Adds a geo coordinate system. */
public fun EChartsOption.geo(block: GeoOption.() -> Unit) {
    geo = geo.orEmpty() + GeoOption().apply(block)
}

/** Adds a data zoom component. */
public fun EChartsOption.dataZoom(block: DataZoomOption.() -> Unit) {
    dataZoom = dataZoom.orEmpty() + DataZoomOption().apply(block)
}

/** Adds a visual map component. */
public fun EChartsOption.visualMap(block: VisualMapOption.() -> Unit) {
    visualMap = visualMap.orEmpty() + VisualMapOption().apply(block)
}

/** Adds a toolbox. */
public fun EChartsOption.toolbox(block: ToolboxOption.() -> Unit) {
    toolbox = toolbox.orEmpty() + ToolboxOption().apply(block)
}

/** Adds a dataset. */
public fun EChartsOption.dataset(block: DatasetOption.() -> Unit) {
    dataset = dataset.orEmpty() + DatasetOption().apply(block)
}

/** Adds a responsive media override. */
public fun EChartsOption.media(block: MediaOption.() -> Unit) {
    media = media.orEmpty() + MediaOption().apply(block)
}

/** Sets the shared axis pointer. */
public fun EChartsOption.axisPointer(block: AxisPointerOption.() -> Unit) {
    axisPointer = AxisPointerOption().apply(block)
}

/** Sets the box selection settings. */
public fun EChartsOption.brush(block: BrushOption.() -> Unit) {
    brush = BrushOption().apply(block)
}

/** Sets the timeline. */
public fun EChartsOption.timeline(block: TimelineOption.() -> Unit) {
    timeline = TimelineOption().apply(block)
}

/** Sets the graphic elements. */
public fun EChartsOption.graphic(block: GraphicOption.() -> Unit) {
    graphic = GraphicOption().apply(block)
}

/** Sets the accessibility settings. */
public fun EChartsOption.aria(block: AriaOption.() -> Unit) {
    aria = AriaOption().apply(block)
}

/** Replaces the series with the ones built by [block]. */
public fun EChartsOption.series(block: SeriesBuilder.() -> Unit) {
    series = SeriesBuilder().apply(block).build()
}

/**
 * Collects series for [EChartsOption.series].
 *
 * Each function creates one series with the matching [SeriesType]; `add` accepts a fully built
 * series, including third-party types such as the `echarts-gl` 3D series.
 */
public class SeriesBuilder {
    private val entries = mutableListOf<SeriesOption>()

    /** Adds a line series. */
    public fun line(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Line, block)

    /** Adds a bar series. */
    public fun bar(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Bar, block)

    /** Adds a pie series. */
    public fun pie(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Pie, block)

    /** Adds a scatter series. */
    public fun scatter(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Scatter, block)

    /** Adds a scatter series with ripple animation. */
    public fun effectScatter(block: SeriesOption.() -> Unit): Unit =
        add(SeriesType.EffectScatter, block)

    /** Adds a radar series. */
    public fun radar(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Radar, block)

    /** Adds a map series. */
    public fun map(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Map, block)

    /** Adds a treemap series. */
    public fun treemap(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Treemap, block)

    /** Adds a relation graph series. */
    public fun graph(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Graph, block)

    /** Adds a flight/route lines series. */
    public fun lines(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Lines, block)

    /** Adds a tree series. */
    public fun tree(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Tree, block)

    /** Adds a sunburst series. */
    public fun sunburst(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Sunburst, block)

    /** Adds a box plot series. */
    public fun boxplot(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Boxplot, block)

    /** Adds a candlestick series. */
    public fun candlestick(block: SeriesOption.() -> Unit): Unit =
        add(SeriesType.Candlestick, block)

    /** Adds a heatmap series. */
    public fun heatmap(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Heatmap, block)

    /** Adds a custom series. */
    public fun custom(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Custom, block)

    /** Adds a sankey series. */
    public fun sankey(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Sankey, block)

    /** Adds a funnel series. */
    public fun funnel(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Funnel, block)

    /** Adds a gauge series. */
    public fun gauge(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Gauge, block)

    /** Adds a pictorial bar series. */
    public fun pictorialBar(block: SeriesOption.() -> Unit): Unit =
        add(SeriesType.PictorialBar, block)

    /** Adds a theme river series. */
    public fun themeRiver(block: SeriesOption.() -> Unit): Unit =
        add(SeriesType.ThemeRiver, block)

    /** Adds a parallel coordinates series. */
    public fun parallel(block: SeriesOption.() -> Unit): Unit =
        add(SeriesType.Parallel, block)

    /** Adds a chord diagram series. */
    public fun chord(block: SeriesOption.() -> Unit): Unit = add(SeriesType.Chord, block)

    /** Adds a series of any type, including third-party extensions. */
    public fun add(type: SeriesType, block: SeriesOption.() -> Unit) {
        entries += SeriesOption(type).apply(block)
    }

    /** Adds an already configured series. */
    public fun add(series: SeriesOption) {
        entries += series
    }

    /** Returns the collected series. */
    public fun build(): List<SeriesOption> = entries.toList()
}

/** Sets the series data from numbers. */
public fun SeriesOption.values(vararg values: Number) {
    data = dataValues(*values)
}

/** Sets the series data from strings, for category or named values. */
public fun SeriesOption.values(vararg values: String) {
    data = dataValues(*values)
}

/** Sets the series data from points or prepared values. */
public fun SeriesOption.points(vararg values: DataValue) {
    data = values.toList()
}

/** Sets the series data from typed items. */
public fun SeriesOption.items(vararg items: DataItem) {
    data = items.map { it.asDataValue() }
}

/** Sets the series data from rows, used by candlestick and box plot. */
public fun SeriesOption.rows(vararg rows: List<DataValue>) {
    data = rows.map { DataValue.Array(it) }
}

/** Sets the series data from rows of numbers, used by candlestick and box plot. */
public fun SeriesOption.numberRows(vararg rows: List<Number>) {
    data = rows.map { row -> DataValue.Array(row.map { valueOf(it) }) }
}

/** Sets the category values of an axis. */
public fun AxisOption.categories(vararg names: String) {
    data = names.map { DataValue.Text(it) }
}

/** Sets the legend entries from names. */
public fun LegendOption.entries(vararg names: String) {
    data = names.map { LegendItem.Name(it) }
}

/** Adds one piece to a piecewise visual map. */
public fun VisualMapOption.piece(block: VisualMapPiece.() -> Unit) {
    pieces = pieces.orEmpty() + VisualMapPiece().apply(block)
}

/** Sets the row-oriented source of a dataset. */
public fun DatasetOption.rows(vararg rows: List<DataValue>) {
    source = datasetRows(*rows)
}

/** Sets the column-oriented source of a dataset. */
public fun DatasetOption.columns(columns: Map<String, List<DataValue>>) {
    source = datasetColumns(columns)
}

/** Sets the snapshot labels of a timeline. */
public fun TimelineOption.labels(vararg labels: String) {
    data = labels.map { DataValue.Text(it) }
}

/** Sets or edits the item style. */
public fun SeriesOption.itemStyle(block: ItemStyleOption.() -> Unit) {
    itemStyle = (itemStyle ?: ItemStyleOption()).apply(block)
}

/** Sets or edits the label. */
public fun SeriesOption.label(block: LabelOption.() -> Unit) {
    label = (label ?: LabelOption()).apply(block)
}

/** Sets or edits the line style. */
public fun SeriesOption.lineStyle(block: LineStyleOption.() -> Unit) {
    lineStyle = (lineStyle ?: LineStyleOption()).apply(block)
}

/** Sets or edits the area style. */
public fun SeriesOption.areaStyle(block: AreaStyleOption.() -> Unit) {
    areaStyle = (areaStyle ?: AreaStyleOption()).apply(block)
}

/** Sets or edits the emphasis state. */
public fun SeriesOption.emphasis(block: EmphasisOption.() -> Unit) {
    emphasis = (emphasis ?: EmphasisOption()).apply(block)
}

/** Sets or edits the item style of an emphasis state. */
public fun EmphasisOption.itemStyle(block: ItemStyleOption.() -> Unit) {
    itemStyle = (itemStyle ?: ItemStyleOption()).apply(block)
}

/** Sets or edits the label of an emphasis state. */
public fun EmphasisOption.label(block: LabelOption.() -> Unit) {
    label = (label ?: LabelOption()).apply(block)
}

/** Sets or edits the line style of an emphasis state. */
public fun EmphasisOption.lineStyle(block: LineStyleOption.() -> Unit) {
    lineStyle = (lineStyle ?: LineStyleOption()).apply(block)
}

/** Sets or edits the area style of an emphasis state. */
public fun EmphasisOption.areaStyle(block: AreaStyleOption.() -> Unit) {
    areaStyle = (areaStyle ?: AreaStyleOption()).apply(block)
}

/** Sets or edits the end label of an emphasis state. */
public fun EmphasisOption.endLabel(block: LabelOption.() -> Unit) {
    endLabel = (endLabel ?: LabelOption()).apply(block)
}

/** Sets or edits the label of a data item. */
public fun DataItem.label(block: LabelOption.() -> Unit) {
    label = (label ?: LabelOption()).apply(block)
}

/** Sets or edits the item style of a data item. */
public fun DataItem.itemStyle(block: ItemStyleOption.() -> Unit) {
    itemStyle = (itemStyle ?: ItemStyleOption()).apply(block)
}

/** Builds a text style; useful for the `rich` map of a label. */
public fun textStyleOf(block: TextStyleOption.() -> Unit): TextStyleOption =
    TextStyleOption().apply(block)

/** Sets or edits the text style of a title. */
public fun TitleOption.textStyle(block: TextStyleOption.() -> Unit) {
    textStyle = (textStyle ?: TextStyleOption()).apply(block)
}

/** Sets or edits the subtext style of a title. */
public fun TitleOption.subtextStyle(block: TextStyleOption.() -> Unit) {
    subtextStyle = (subtextStyle ?: TextStyleOption()).apply(block)
}

/** Sets or edits the text style of a legend. */
public fun LegendOption.textStyle(block: TextStyleOption.() -> Unit) {
    textStyle = (textStyle ?: TextStyleOption()).apply(block)
}

/** Sets or edits the text style of a tooltip. */
public fun TooltipOption.textStyle(block: TextStyleOption.() -> Unit) {
    textStyle = (textStyle ?: TextStyleOption()).apply(block)
}

/** Sets or edits the connector line of a series label. */
public fun SeriesOption.labelLine(block: LabelLineOption.() -> Unit) {
    labelLine = (labelLine ?: LabelLineOption()).apply(block)
}

/** Sets or edits the label of the end of a line series. */
public fun SeriesOption.endLabel(block: LabelOption.() -> Unit) {
    endLabel = (endLabel ?: LabelOption()).apply(block)
}

/** Sets or edits `markPoint` of a series. */
public fun SeriesOption.markPoint(block: MarkPointOption.() -> Unit) {
    markPoint = (markPoint ?: MarkPointOption()).apply(block)
}

/** Sets or edits `markLine` of a series. */
public fun SeriesOption.markLine(block: MarkLineOption.() -> Unit) {
    markLine = (markLine ?: MarkLineOption()).apply(block)
}

/** Sets or edits `markArea` of a series. */
public fun SeriesOption.markArea(block: MarkAreaOption.() -> Unit) {
    markArea = (markArea ?: MarkAreaOption()).apply(block)
}

/** Sets or edits the axis line of an axis. */
public fun AxisOption.axisLine(block: AxisLineOption.() -> Unit) {
    axisLine = (axisLine ?: AxisLineOption()).apply(block)
}

/** Sets or edits the tick marks of an axis. */
public fun AxisOption.axisTick(block: AxisTickOption.() -> Unit) {
    axisTick = (axisTick ?: AxisTickOption()).apply(block)
}

/** Sets or edits the tick labels of an axis. */
public fun AxisOption.axisLabel(block: AxisLabelOption.() -> Unit) {
    axisLabel = (axisLabel ?: AxisLabelOption()).apply(block)
}

/** Sets or edits the name style of an axis. */
public fun AxisOption.nameTextStyle(block: TextStyleOption.() -> Unit) {
    nameTextStyle = (nameTextStyle ?: TextStyleOption()).apply(block)
}

/** Sets or edits the split lines of an axis. */
public fun AxisOption.splitLine(block: SplitLineOption.() -> Unit) {
    splitLine = (splitLine ?: SplitLineOption()).apply(block)
}

/** Sets or edits the split areas of an axis. */
public fun AxisOption.splitArea(block: SplitAreaOption.() -> Unit) {
    splitArea = (splitArea ?: SplitAreaOption()).apply(block)
}

/** Sets or edits `inRange` of a visual map. */
public fun VisualMapOption.inRange(block: VisualMapRange.() -> Unit) {
    inRange = (inRange ?: VisualMapRange()).apply(block)
}

/** Sets or edits `outOfRange` of a visual map. */
public fun VisualMapOption.outOfRange(block: VisualMapRange.() -> Unit) {
    outOfRange = (outOfRange ?: VisualMapRange()).apply(block)
}

/** Sets or edits the day labels of a calendar. */
public fun CalendarOption.dayLabel(block: CalendarLabelOption.() -> Unit) {
    dayLabel = (dayLabel ?: CalendarLabelOption()).apply(block)
}

/** Sets or edits the month labels of a calendar. */
public fun CalendarOption.monthLabel(block: CalendarLabelOption.() -> Unit) {
    monthLabel = (monthLabel ?: CalendarLabelOption()).apply(block)
}

/** Sets or edits the year labels of a calendar. */
public fun CalendarOption.yearLabel(block: CalendarLabelOption.() -> Unit) {
    yearLabel = (yearLabel ?: CalendarLabelOption()).apply(block)
}
