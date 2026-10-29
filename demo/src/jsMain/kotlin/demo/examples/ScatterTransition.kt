package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso

/*
 * Two scatter pages whose subject is the transition between shapes: `scatter-symbol-morph` walks
 * the same grid of points through ten symbols, and `scatter-aggregate-bar` folds a cloud of people
 * into two average bars and back.
 *
 * Both pages swap options on a timer, so they are `interactive` examples and the timers are owned by
 * the ChartHolder. The upstream `Math.random()` delays are replaced by [Lcg] so the animation
 * choreography is the same on every visit.
 */

private val scatterTransition = Category(Groups.SCATTER, "examples/ScatterTransition.kt")

/** The ten values of both hidden category axes; the point of a cell carries its own `[x, y]`. */
private val MORPH_AXIS: Array<OptionDataItem?> =
    numbers<OptionDataItem>(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)

/** One point per cell of the 10 × 10 grid, built row by row like the upstream loops. */
private val MORPH_POINTS: Array<ScatterDataItem?> =
    dataItems(
        *Array(100) { index ->
            ScatterDataItem(
                doubleRow((index % 10).toDouble(), (index / 10).toDouble(), 10.0),
            )
        },
    )

/** The symbols the points morph through: a plain circle and nine `path://` outlines. */
private val MORPH_SYMBOLS: Array<String> =
    arrayOf(
        "circle",
        "path://M23.6 2c-3.363 0-6.258 2.736-7.599 5.594-1.342-2.858-4.237-5.594-7.601-5.594-4.637 0-8.4 3.764-8.4 8.401 0 9.433 9.516 11.906 16.001 21.232 6.13-9.268 15.999-12.1 15.999-21.232 0-4.637-3.763-8.401-8.4-8.401z",
        "path://M16 0c-8.837 0-16 7.163-16 16s7.163 16 16 16 16-7.163 16-16-7.163-16-16-16zM22 8c1.105 0 2 1.343 2 3s-0.895 3-2 3-2-1.343-2-3 0.895-3 2-3zM10 8c1.105 0 2 1.343 2 3s-0.895 3-2 3-2-1.343-2-3 0.895-3 2-3zM16 28c-5.215 0-9.544-4.371-10-9.947 2.93 1.691 6.377 2.658 10 2.658s7.070-0.963 10-2.654c-0.455 5.576-4.785 9.942-10 9.942z",
        "path://M32 2c0-1.422-0.298-2.775-0.833-4-1.049 2.401-3.014 4.31-5.453 5.287-2.694-2.061-6.061-3.287-9.714-3.287s-7.021 1.226-9.714 3.287c-2.439-0.976-4.404-2.886-5.453-5.287-0.535 1.225-0.833 2.578-0.833 4 0 2.299 0.777 4.417 2.081 6.106-1.324 2.329-2.081 5.023-2.081 7.894 0 8.837 7.163 16 16 16s16-7.163 16-16c0-2.871-0.757-5.565-2.081-7.894 1.304-1.689 2.081-3.806 2.081-6.106zM18.003 11.891c0.064-1.483 1.413-2.467 2.55-3.036 1.086-0.543 2.16-0.814 2.205-0.826 0.536-0.134 1.079 0.192 1.213 0.728s-0.192 1.079-0.728 1.213c-0.551 0.139-1.204 0.379-1.779 0.667 0.333 0.357 0.537 0.836 0.537 1.363 0 1.105-0.895 2-2 2s-2-0.895-2-2c0-0.037 0.001-0.073 0.003-0.109zM8.030 8.758c0.134-0.536 0.677-0.862 1.213-0.728 0.045 0.011 1.119 0.283 2.205 0.826 1.137 0.569 2.486 1.553 2.55 3.036 0.002 0.036 0.003 0.072 0.003 0.109 0 1.105-0.895 2-2 2s-2-0.895-2-2c0-0.527 0.204-1.005 0.537-1.363-0.575-0.288-1.228-0.528-1.779-0.667-0.536-0.134-0.861-0.677-0.728-1.213zM16 26c-3.641 0-6.827-1.946-8.576-4.855l2.573-1.544c1.224 2.036 3.454 3.398 6.003 3.398s4.779-1.362 6.003-3.398l2.573 1.544c-1.749 2.908-4.935 4.855-8.576 4.855z",
        "path://M16 0c-8.837 0-16 7.163-16 16s7.163 16 16 16 16-7.163 16-16-7.163-16-16-16zM22 8c1.105 0 2 0.895 2 2s-0.895 2-2 2-2-0.895-2-2 0.895-2 2-2zM10 8c1.105 0 2 0.895 2 2s-0.895 2-2 2-2-0.895-2-2 0.895-2 2-2zM16.994 21.23c-0.039-0.035-0.078-0.072-0.115-0.109-0.586-0.586-0.878-1.353-0.879-2.121-0 0.768-0.293 1.535-0.879 2.121-0.038 0.038-0.076 0.074-0.115 0.109-2.704 2.453-9.006-0.058-9.006-3.23 1.938 1.25 3.452 0.306 4.879-1.121 1.172-1.172 3.071-1.172 4.243 0 0.586 0.586 0.879 1.353 0.879 2.121 0-0.768 0.293-1.535 0.879-2.121 1.172-1.172 3.071-1.172 4.243 0 1.427 1.427 2.941 2.371 4.879 1.121 0 3.173-6.302 5.684-9.006 3.23z",
        "path://M16 0c-8.837 0-16 7.163-16 16s7.163 16 16 16 16-7.163 16-16-7.163-16-16-16zM10 14c-1.105 0-2-1.343-2-3s0.895-3 2-3 2 1.343 2 3-0.895 3-2 3zM16 26c-2.209 0-4-1.791-4-4s1.791-4 4-4c2.209 0 4 1.791 4 4s-1.791 4-4 4zM22 14c-1.105 0-2-1.343-2-3s0.895-3 2-3 2 1.343 2 3-0.895 3-2 3z",
        "path://M14 18v-14c-7.732 0-14 6.268-14 14s6.268 14 14 14 14-6.268 14-14c0-2.251-0.532-4.378-1.476-6.262l-12.524 6.262zM28.524 7.738c-2.299-4.588-7.043-7.738-12.524-7.738v14l12.524-6.262z",
        "path://M10.225 24.854c1.728-1.13 3.877-1.989 6.243-2.513-0.47-0.556-0.897-1.176-1.265-1.844-0.95-1.726-1.453-3.627-1.453-5.497 0-2.689 0-5.228 0.956-7.305 0.928-2.016 2.598-3.265 4.976-3.734-0.529-2.39-1.936-3.961-5.682-3.961-6 0-6 4.029-6 9 0 3.096 1.797 6.191 4 7.432v1.649c-6.784 0.555-12 3.888-12 7.918h8.719c0.454-0.403 0.956-0.787 1.506-1.146zM24 24.082v-1.649c2.203-1.241 4-4.337 4-7.432 0-4.971 0-9-6-9s-6 4.029-6 9c0 3.096 1.797 6.191 4 7.432v1.649c-6.784 0.555-12 3.888-12 7.918h28c0-4.030-5.216-7.364-12-7.918z",
        "path://M30 10h-6v-3c0-2.761-5.373-5-12-5s-12 2.239-12 5v20c0 2.761 5.373 5 12 5s12-2.239 12-5v-3h6c1.105 0 2-0.895 2-2v-10c0-1.105-0.895-2-2-2zM5.502 8.075c-1.156-0.381-1.857-0.789-2.232-1.075 0.375-0.286 1.075-0.694 2.232-1.075 1.811-0.597 4.118-0.925 6.498-0.925s4.688 0.329 6.498 0.925c1.156 0.381 1.857 0.789 2.232 1.075-0.375 0.286-1.076 0.694-2.232 1.075-1.811 0.597-4.118 0.925-6.498 0.925s-4.688-0.329-6.498-0.925zM28 20h-4v-6h4v6z",
        "path://M24 19.999l-5.713-5.713 13.713-10.286-4-4-17.141 6.858-5.397-5.397c-1.556-1.556-3.728-1.928-4.828-0.828s-0.727 3.273 0.828 4.828l5.396 5.396-6.858 17.143 4 4 10.287-13.715 5.713 5.713v7.999h4l2-6 6-2v-4l-7.999 0z",
    )

/** How long every shape is kept before the next one is applied, in milliseconds. */
private const val MORPH_INTERVAL_MILLIS = 700

/**
 * The first option of `scatter-symbol-morph`: a rounded square per point, animated into the other
 * shapes by `universalTransition`.
 */
private fun morphFirstOption(): EChartsOption =
    unsafeJso {
        grid =
            one(
                unsafeJso<GridOption> {
                    left = StringOrDouble(0.0)
                    right = StringOrDouble(0.0)
                    top = StringOrDouble(0.0)
                    bottom = StringOrDouble(0.0)
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    show = false
                    type = AxisTypes.category
                    data = MORPH_AXIS
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    show = false
                    type = AxisTypes.category
                    data = MORPH_AXIS
                },
            )
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    typedData = MORPH_POINTS
                    symbol = SymbolShape("roundRect")
                    symbolKeepAspect = true
                    universalTransition = BooleanOrUniversalTransitionOption(true)
                    symbolSize = SymbolSize(50.0)
                },
            )
    }

/** The later options only carry the new symbol; `setOption` merges them over the first one. */
private fun morphOption(symbol: String): EChartsOption =
    unsafeJso {
        series =
            arrayOf<SeriesOption>(
                scatterSeries { this.symbol = SymbolShape(symbol) },
            )
    }

/** The whole morph sequence, in the order the timer walks it. */
private val MORPH_OPTIONS: Array<EChartsOption> =
    arrayOf(morphFirstOption(), *Array(MORPH_SYMBOLS.size) { index -> morphOption(MORPH_SYMBOLS[index]) })

/** Body height rows of `scatter-aggregate-bar`; one row is `[height, weight]`. */
private val AGGREGATE_FEMALE: Array<ScatterDataItem?> =
    dataItems(
        ScatterDataItem(doubleRow(161.2, 51.6)), ScatterDataItem(doubleRow(167.5, 59.0)), ScatterDataItem(doubleRow(159.5, 49.2)), ScatterDataItem(doubleRow(157.0, 63.0)), ScatterDataItem(doubleRow(155.8, 53.6)),
        ScatterDataItem(doubleRow(170.0, 59.0)), ScatterDataItem(doubleRow(159.1, 47.6)), ScatterDataItem(doubleRow(166.0, 69.8)), ScatterDataItem(doubleRow(176.2, 66.8)), ScatterDataItem(doubleRow(160.2, 75.2)),
        ScatterDataItem(doubleRow(172.5, 55.2)), ScatterDataItem(doubleRow(170.9, 54.2)), ScatterDataItem(doubleRow(172.9, 62.5)), ScatterDataItem(doubleRow(153.4, 42.0)), ScatterDataItem(doubleRow(160.0, 50.0)),
        ScatterDataItem(doubleRow(147.2, 49.8)), ScatterDataItem(doubleRow(168.2, 49.2)), ScatterDataItem(doubleRow(175.0, 73.2)), ScatterDataItem(doubleRow(157.0, 47.8)), ScatterDataItem(doubleRow(167.6, 68.8)),
        ScatterDataItem(doubleRow(159.5, 50.6)), ScatterDataItem(doubleRow(175.0, 82.5)), ScatterDataItem(doubleRow(166.8, 57.2)), ScatterDataItem(doubleRow(176.5, 87.8)), ScatterDataItem(doubleRow(170.2, 72.8)),
        ScatterDataItem(doubleRow(174.0, 54.5)), ScatterDataItem(doubleRow(173.0, 59.8)), ScatterDataItem(doubleRow(179.9, 67.3)), ScatterDataItem(doubleRow(170.5, 67.8)), ScatterDataItem(doubleRow(160.0, 47.0)),
        ScatterDataItem(doubleRow(154.4, 46.2)), ScatterDataItem(doubleRow(162.0, 55.0)), ScatterDataItem(doubleRow(176.5, 83.0)), ScatterDataItem(doubleRow(160.0, 54.4)), ScatterDataItem(doubleRow(152.0, 45.8)),
        ScatterDataItem(doubleRow(162.1, 53.6)), ScatterDataItem(doubleRow(170.0, 73.2)), ScatterDataItem(doubleRow(160.2, 52.1)), ScatterDataItem(doubleRow(161.3, 67.9)), ScatterDataItem(doubleRow(166.4, 56.6)),
        ScatterDataItem(doubleRow(168.9, 62.3)), ScatterDataItem(doubleRow(163.8, 58.5)), ScatterDataItem(doubleRow(167.6, 54.5)), ScatterDataItem(doubleRow(160.0, 50.2)), ScatterDataItem(doubleRow(161.3, 60.3)),
        ScatterDataItem(doubleRow(167.6, 58.3)), ScatterDataItem(doubleRow(165.1, 56.2)), ScatterDataItem(doubleRow(160.0, 50.2)), ScatterDataItem(doubleRow(170.0, 72.9)), ScatterDataItem(doubleRow(157.5, 59.8)),
        ScatterDataItem(doubleRow(167.6, 61.0)), ScatterDataItem(doubleRow(160.7, 69.1)), ScatterDataItem(doubleRow(163.2, 55.9)), ScatterDataItem(doubleRow(152.4, 46.5)), ScatterDataItem(doubleRow(157.5, 54.3)),
        ScatterDataItem(doubleRow(168.3, 54.8)), ScatterDataItem(doubleRow(180.3, 60.7)), ScatterDataItem(doubleRow(165.5, 60.0)), ScatterDataItem(doubleRow(165.0, 62.0)), ScatterDataItem(doubleRow(164.5, 60.3)),
        ScatterDataItem(doubleRow(156.0, 52.7)), ScatterDataItem(doubleRow(160.0, 74.3)), ScatterDataItem(doubleRow(163.0, 62.0)), ScatterDataItem(doubleRow(165.7, 73.1)), ScatterDataItem(doubleRow(161.0, 80.0)),
        ScatterDataItem(doubleRow(162.0, 54.7)), ScatterDataItem(doubleRow(166.0, 53.2)), ScatterDataItem(doubleRow(174.0, 75.7)), ScatterDataItem(doubleRow(172.7, 61.1)), ScatterDataItem(doubleRow(167.6, 55.7)),
        ScatterDataItem(doubleRow(151.1, 48.7)), ScatterDataItem(doubleRow(164.5, 52.3)), ScatterDataItem(doubleRow(163.5, 50.0)), ScatterDataItem(doubleRow(152.0, 59.3)), ScatterDataItem(doubleRow(169.0, 62.5)),
        ScatterDataItem(doubleRow(164.0, 55.7)), ScatterDataItem(doubleRow(161.2, 54.8)), ScatterDataItem(doubleRow(155.0, 45.9)), ScatterDataItem(doubleRow(170.0, 70.6)), ScatterDataItem(doubleRow(176.2, 67.2)),
        ScatterDataItem(doubleRow(170.0, 69.4)), ScatterDataItem(doubleRow(162.5, 58.2)), ScatterDataItem(doubleRow(170.3, 64.8)), ScatterDataItem(doubleRow(164.1, 71.6)), ScatterDataItem(doubleRow(169.5, 52.8)),
        ScatterDataItem(doubleRow(163.2, 59.8)), ScatterDataItem(doubleRow(154.5, 49.0)), ScatterDataItem(doubleRow(159.8, 50.0)), ScatterDataItem(doubleRow(173.2, 69.2)), ScatterDataItem(doubleRow(170.0, 55.9)),
        ScatterDataItem(doubleRow(161.4, 63.4)), ScatterDataItem(doubleRow(169.0, 58.2)), ScatterDataItem(doubleRow(166.2, 58.6)), ScatterDataItem(doubleRow(159.4, 45.7)), ScatterDataItem(doubleRow(162.5, 52.2)),
        ScatterDataItem(doubleRow(159.0, 48.6)), ScatterDataItem(doubleRow(162.8, 57.8)), ScatterDataItem(doubleRow(159.0, 55.6)), ScatterDataItem(doubleRow(179.8, 66.8)), ScatterDataItem(doubleRow(162.9, 59.4)),
        ScatterDataItem(doubleRow(161.0, 53.6)), ScatterDataItem(doubleRow(151.1, 73.2)), ScatterDataItem(doubleRow(168.2, 53.4)), ScatterDataItem(doubleRow(168.9, 69.0)), ScatterDataItem(doubleRow(173.2, 58.4)),
        ScatterDataItem(doubleRow(171.8, 56.2)), ScatterDataItem(doubleRow(178.0, 70.6)), ScatterDataItem(doubleRow(164.3, 59.8)), ScatterDataItem(doubleRow(163.0, 72.0)), ScatterDataItem(doubleRow(168.5, 65.2)),
        ScatterDataItem(doubleRow(166.8, 56.6)), ScatterDataItem(doubleRow(172.7, 105.2)), ScatterDataItem(doubleRow(163.5, 51.8)), ScatterDataItem(doubleRow(169.4, 63.4)), ScatterDataItem(doubleRow(167.8, 59.0)),
        ScatterDataItem(doubleRow(159.5, 47.6)), ScatterDataItem(doubleRow(167.6, 63.0)), ScatterDataItem(doubleRow(161.2, 55.2)), ScatterDataItem(doubleRow(160.0, 45.0)), ScatterDataItem(doubleRow(163.2, 54.0)),
        ScatterDataItem(doubleRow(162.2, 50.2)), ScatterDataItem(doubleRow(161.3, 60.2)), ScatterDataItem(doubleRow(149.5, 44.8)), ScatterDataItem(doubleRow(157.5, 58.8)), ScatterDataItem(doubleRow(163.2, 56.4)),
        ScatterDataItem(doubleRow(172.7, 62.0)), ScatterDataItem(doubleRow(155.0, 49.2)), ScatterDataItem(doubleRow(156.5, 67.2)), ScatterDataItem(doubleRow(164.0, 53.8)), ScatterDataItem(doubleRow(160.9, 54.4)),
        ScatterDataItem(doubleRow(162.8, 58.0)), ScatterDataItem(doubleRow(167.0, 59.8)), ScatterDataItem(doubleRow(160.0, 54.8)), ScatterDataItem(doubleRow(160.0, 43.2)), ScatterDataItem(doubleRow(168.9, 60.5)),
        ScatterDataItem(doubleRow(158.2, 46.4)), ScatterDataItem(doubleRow(156.0, 64.4)), ScatterDataItem(doubleRow(160.0, 48.8)), ScatterDataItem(doubleRow(167.1, 62.2)), ScatterDataItem(doubleRow(158.0, 55.5)),
        ScatterDataItem(doubleRow(167.6, 57.8)), ScatterDataItem(doubleRow(156.0, 54.6)), ScatterDataItem(doubleRow(162.1, 59.2)), ScatterDataItem(doubleRow(173.4, 52.7)), ScatterDataItem(doubleRow(159.8, 53.2)),
        ScatterDataItem(doubleRow(170.5, 64.5)), ScatterDataItem(doubleRow(159.2, 51.8)), ScatterDataItem(doubleRow(157.5, 56.0)), ScatterDataItem(doubleRow(161.3, 63.6)), ScatterDataItem(doubleRow(162.6, 63.2)),
        ScatterDataItem(doubleRow(160.0, 59.5)), ScatterDataItem(doubleRow(168.9, 56.8)), ScatterDataItem(doubleRow(165.1, 64.1)), ScatterDataItem(doubleRow(162.6, 50.0)), ScatterDataItem(doubleRow(165.1, 72.3)),
        ScatterDataItem(doubleRow(166.4, 55.0)), ScatterDataItem(doubleRow(160.0, 55.9)), ScatterDataItem(doubleRow(152.4, 60.4)), ScatterDataItem(doubleRow(170.2, 69.1)), ScatterDataItem(doubleRow(162.6, 84.5)),
        ScatterDataItem(doubleRow(170.2, 55.9)), ScatterDataItem(doubleRow(158.8, 55.5)), ScatterDataItem(doubleRow(172.7, 69.5)), ScatterDataItem(doubleRow(167.6, 76.4)), ScatterDataItem(doubleRow(162.6, 61.4)),
        ScatterDataItem(doubleRow(167.6, 65.9)), ScatterDataItem(doubleRow(156.2, 58.6)), ScatterDataItem(doubleRow(175.2, 66.8)), ScatterDataItem(doubleRow(172.1, 56.6)), ScatterDataItem(doubleRow(162.6, 58.6)),
        ScatterDataItem(doubleRow(160.0, 55.9)), ScatterDataItem(doubleRow(165.1, 59.1)), ScatterDataItem(doubleRow(182.9, 81.8)), ScatterDataItem(doubleRow(166.4, 70.7)), ScatterDataItem(doubleRow(165.1, 56.8)),
        ScatterDataItem(doubleRow(177.8, 60.0)), ScatterDataItem(doubleRow(165.1, 58.2)), ScatterDataItem(doubleRow(175.3, 72.7)), ScatterDataItem(doubleRow(154.9, 54.1)), ScatterDataItem(doubleRow(158.8, 49.1)),
        ScatterDataItem(doubleRow(172.7, 75.9)), ScatterDataItem(doubleRow(168.9, 55.0)), ScatterDataItem(doubleRow(161.3, 57.3)), ScatterDataItem(doubleRow(167.6, 55.0)), ScatterDataItem(doubleRow(165.1, 65.5)),
        ScatterDataItem(doubleRow(175.3, 65.5)), ScatterDataItem(doubleRow(157.5, 48.6)), ScatterDataItem(doubleRow(163.8, 58.6)), ScatterDataItem(doubleRow(167.6, 63.6)), ScatterDataItem(doubleRow(165.1, 55.2)),
        ScatterDataItem(doubleRow(165.1, 62.7)), ScatterDataItem(doubleRow(168.9, 56.6)), ScatterDataItem(doubleRow(162.6, 53.9)), ScatterDataItem(doubleRow(164.5, 63.2)), ScatterDataItem(doubleRow(176.5, 73.6)),
        ScatterDataItem(doubleRow(168.9, 62.0)), ScatterDataItem(doubleRow(175.3, 63.6)), ScatterDataItem(doubleRow(159.4, 53.2)), ScatterDataItem(doubleRow(160.0, 53.4)), ScatterDataItem(doubleRow(170.2, 55.0)),
        ScatterDataItem(doubleRow(162.6, 70.5)), ScatterDataItem(doubleRow(167.6, 54.5)), ScatterDataItem(doubleRow(162.6, 54.5)), ScatterDataItem(doubleRow(160.7, 55.9)), ScatterDataItem(doubleRow(160.0, 59.0)),
        ScatterDataItem(doubleRow(157.5, 63.6)), ScatterDataItem(doubleRow(162.6, 54.5)), ScatterDataItem(doubleRow(152.4, 47.3)), ScatterDataItem(doubleRow(170.2, 67.7)), ScatterDataItem(doubleRow(165.1, 80.9)),
        ScatterDataItem(doubleRow(172.7, 70.5)), ScatterDataItem(doubleRow(165.1, 60.9)), ScatterDataItem(doubleRow(170.2, 63.6)), ScatterDataItem(doubleRow(170.2, 54.5)), ScatterDataItem(doubleRow(170.2, 59.1)),
        ScatterDataItem(doubleRow(161.3, 70.5)), ScatterDataItem(doubleRow(167.6, 52.7)), ScatterDataItem(doubleRow(167.6, 62.7)), ScatterDataItem(doubleRow(165.1, 86.3)), ScatterDataItem(doubleRow(162.6, 66.4)),
        ScatterDataItem(doubleRow(152.4, 67.3)), ScatterDataItem(doubleRow(168.9, 63.0)), ScatterDataItem(doubleRow(170.2, 73.6)), ScatterDataItem(doubleRow(175.2, 62.3)), ScatterDataItem(doubleRow(175.2, 57.7)),
        ScatterDataItem(doubleRow(160.0, 55.4)), ScatterDataItem(doubleRow(165.1, 104.1)), ScatterDataItem(doubleRow(174.0, 55.5)), ScatterDataItem(doubleRow(170.2, 77.3)), ScatterDataItem(doubleRow(160.0, 80.5)),
        ScatterDataItem(doubleRow(167.6, 64.5)), ScatterDataItem(doubleRow(167.6, 72.3)), ScatterDataItem(doubleRow(167.6, 61.4)), ScatterDataItem(doubleRow(154.9, 58.2)), ScatterDataItem(doubleRow(162.6, 81.8)),
        ScatterDataItem(doubleRow(175.3, 63.6)), ScatterDataItem(doubleRow(171.4, 53.4)), ScatterDataItem(doubleRow(157.5, 54.5)), ScatterDataItem(doubleRow(165.1, 53.6)), ScatterDataItem(doubleRow(160.0, 60.0)),
        ScatterDataItem(doubleRow(174.0, 73.6)), ScatterDataItem(doubleRow(162.6, 61.4)), ScatterDataItem(doubleRow(174.0, 55.5)), ScatterDataItem(doubleRow(162.6, 63.6)), ScatterDataItem(doubleRow(161.3, 60.9)),
        ScatterDataItem(doubleRow(156.2, 60.0)), ScatterDataItem(doubleRow(149.9, 46.8)), ScatterDataItem(doubleRow(169.5, 57.3)), ScatterDataItem(doubleRow(160.0, 64.1)), ScatterDataItem(doubleRow(175.3, 63.6)),
        ScatterDataItem(doubleRow(169.5, 67.3)), ScatterDataItem(doubleRow(160.0, 75.5)), ScatterDataItem(doubleRow(172.7, 68.2)), ScatterDataItem(doubleRow(162.6, 61.4)), ScatterDataItem(doubleRow(157.5, 76.8)),
        ScatterDataItem(doubleRow(176.5, 71.8)), ScatterDataItem(doubleRow(164.4, 55.5)), ScatterDataItem(doubleRow(160.7, 48.6)), ScatterDataItem(doubleRow(174.0, 66.4)), ScatterDataItem(doubleRow(163.8, 67.3)),
    )

/** The same measurements for the male group. */
private val AGGREGATE_MALE: Array<ScatterDataItem?> =
    dataItems(
        ScatterDataItem(doubleRow(174.0, 65.6)), ScatterDataItem(doubleRow(175.3, 71.8)), ScatterDataItem(doubleRow(193.5, 80.7)), ScatterDataItem(doubleRow(186.5, 72.6)), ScatterDataItem(doubleRow(187.2, 78.8)),
        ScatterDataItem(doubleRow(181.5, 74.8)), ScatterDataItem(doubleRow(184.0, 86.4)), ScatterDataItem(doubleRow(184.5, 78.4)), ScatterDataItem(doubleRow(175.0, 62.0)), ScatterDataItem(doubleRow(184.0, 81.6)),
        ScatterDataItem(doubleRow(180.0, 76.6)), ScatterDataItem(doubleRow(177.8, 83.6)), ScatterDataItem(doubleRow(192.0, 90.0)), ScatterDataItem(doubleRow(176.0, 74.6)), ScatterDataItem(doubleRow(174.0, 71.0)),
        ScatterDataItem(doubleRow(184.0, 79.6)), ScatterDataItem(doubleRow(192.7, 93.8)), ScatterDataItem(doubleRow(171.5, 70.0)), ScatterDataItem(doubleRow(173.0, 72.4)), ScatterDataItem(doubleRow(176.0, 85.9)),
        ScatterDataItem(doubleRow(176.0, 78.8)), ScatterDataItem(doubleRow(180.5, 77.8)), ScatterDataItem(doubleRow(172.7, 66.2)), ScatterDataItem(doubleRow(176.0, 86.4)), ScatterDataItem(doubleRow(173.5, 81.8)),
        ScatterDataItem(doubleRow(178.0, 89.6)), ScatterDataItem(doubleRow(180.3, 82.8)), ScatterDataItem(doubleRow(180.3, 76.4)), ScatterDataItem(doubleRow(164.5, 63.2)), ScatterDataItem(doubleRow(173.0, 60.9)),
        ScatterDataItem(doubleRow(183.5, 74.8)), ScatterDataItem(doubleRow(175.5, 70.0)), ScatterDataItem(doubleRow(188.0, 72.4)), ScatterDataItem(doubleRow(189.2, 84.1)), ScatterDataItem(doubleRow(172.8, 69.1)),
        ScatterDataItem(doubleRow(170.0, 59.5)), ScatterDataItem(doubleRow(182.0, 67.2)), ScatterDataItem(doubleRow(170.0, 61.3)), ScatterDataItem(doubleRow(177.8, 68.6)), ScatterDataItem(doubleRow(184.2, 80.1)),
        ScatterDataItem(doubleRow(186.7, 87.8)), ScatterDataItem(doubleRow(171.4, 84.7)), ScatterDataItem(doubleRow(172.7, 73.4)), ScatterDataItem(doubleRow(175.3, 72.1)), ScatterDataItem(doubleRow(180.3, 82.6)),
        ScatterDataItem(doubleRow(182.9, 88.7)), ScatterDataItem(doubleRow(188.0, 84.1)), ScatterDataItem(doubleRow(177.2, 94.1)), ScatterDataItem(doubleRow(172.1, 74.9)), ScatterDataItem(doubleRow(167.0, 59.1)),
        ScatterDataItem(doubleRow(169.5, 75.6)), ScatterDataItem(doubleRow(174.0, 86.2)), ScatterDataItem(doubleRow(172.7, 75.3)), ScatterDataItem(doubleRow(182.2, 87.1)), ScatterDataItem(doubleRow(164.1, 55.2)),
        ScatterDataItem(doubleRow(163.0, 57.0)), ScatterDataItem(doubleRow(171.5, 61.4)), ScatterDataItem(doubleRow(184.2, 76.8)), ScatterDataItem(doubleRow(174.0, 86.8)), ScatterDataItem(doubleRow(174.0, 72.2)),
        ScatterDataItem(doubleRow(177.0, 71.6)), ScatterDataItem(doubleRow(186.0, 84.8)), ScatterDataItem(doubleRow(167.0, 68.2)), ScatterDataItem(doubleRow(171.8, 66.1)), ScatterDataItem(doubleRow(182.0, 72.0)),
        ScatterDataItem(doubleRow(167.0, 64.6)), ScatterDataItem(doubleRow(177.8, 74.8)), ScatterDataItem(doubleRow(164.5, 70.0)), ScatterDataItem(doubleRow(192.0, 101.6)), ScatterDataItem(doubleRow(175.5, 63.2)),
        ScatterDataItem(doubleRow(171.2, 79.1)), ScatterDataItem(doubleRow(181.6, 78.9)), ScatterDataItem(doubleRow(167.4, 67.7)), ScatterDataItem(doubleRow(181.1, 66.0)), ScatterDataItem(doubleRow(177.0, 68.2)),
        ScatterDataItem(doubleRow(174.5, 63.9)), ScatterDataItem(doubleRow(177.5, 72.0)), ScatterDataItem(doubleRow(170.5, 56.8)), ScatterDataItem(doubleRow(182.4, 74.5)), ScatterDataItem(doubleRow(197.1, 90.9)),
        ScatterDataItem(doubleRow(180.1, 93.0)), ScatterDataItem(doubleRow(175.5, 80.9)), ScatterDataItem(doubleRow(180.6, 72.7)), ScatterDataItem(doubleRow(184.4, 68.0)), ScatterDataItem(doubleRow(175.5, 70.9)),
        ScatterDataItem(doubleRow(180.6, 72.5)), ScatterDataItem(doubleRow(177.0, 72.5)), ScatterDataItem(doubleRow(177.1, 83.4)), ScatterDataItem(doubleRow(181.6, 75.5)), ScatterDataItem(doubleRow(176.5, 73.0)),
        ScatterDataItem(doubleRow(175.0, 70.2)), ScatterDataItem(doubleRow(174.0, 73.4)), ScatterDataItem(doubleRow(165.1, 70.5)), ScatterDataItem(doubleRow(177.0, 68.9)), ScatterDataItem(doubleRow(192.0, 102.3)),
        ScatterDataItem(doubleRow(176.5, 68.4)), ScatterDataItem(doubleRow(169.4, 65.9)), ScatterDataItem(doubleRow(182.1, 75.7)), ScatterDataItem(doubleRow(179.8, 84.5)), ScatterDataItem(doubleRow(175.3, 87.7)),
        ScatterDataItem(doubleRow(184.9, 86.4)), ScatterDataItem(doubleRow(177.3, 73.2)), ScatterDataItem(doubleRow(167.4, 53.9)), ScatterDataItem(doubleRow(178.1, 72.0)), ScatterDataItem(doubleRow(168.9, 55.5)),
        ScatterDataItem(doubleRow(157.2, 58.4)), ScatterDataItem(doubleRow(180.3, 83.2)), ScatterDataItem(doubleRow(170.2, 72.7)), ScatterDataItem(doubleRow(177.8, 64.1)), ScatterDataItem(doubleRow(172.7, 72.3)),
        ScatterDataItem(doubleRow(165.1, 65.0)), ScatterDataItem(doubleRow(186.7, 86.4)), ScatterDataItem(doubleRow(165.1, 65.0)), ScatterDataItem(doubleRow(174.0, 88.6)), ScatterDataItem(doubleRow(175.3, 84.1)),
        ScatterDataItem(doubleRow(185.4, 66.8)), ScatterDataItem(doubleRow(177.8, 75.5)), ScatterDataItem(doubleRow(180.3, 93.2)), ScatterDataItem(doubleRow(180.3, 82.7)), ScatterDataItem(doubleRow(177.8, 58.0)),
        ScatterDataItem(doubleRow(177.8, 79.5)), ScatterDataItem(doubleRow(177.8, 78.6)), ScatterDataItem(doubleRow(177.8, 71.8)), ScatterDataItem(doubleRow(177.8, 116.4)), ScatterDataItem(doubleRow(163.8, 72.2)),
        ScatterDataItem(doubleRow(188.0, 83.6)), ScatterDataItem(doubleRow(198.1, 85.5)), ScatterDataItem(doubleRow(175.3, 90.9)), ScatterDataItem(doubleRow(166.4, 85.9)), ScatterDataItem(doubleRow(190.5, 89.1)),
        ScatterDataItem(doubleRow(166.4, 75.0)), ScatterDataItem(doubleRow(177.8, 77.7)), ScatterDataItem(doubleRow(179.7, 86.4)), ScatterDataItem(doubleRow(172.7, 90.9)), ScatterDataItem(doubleRow(190.5, 73.6)),
        ScatterDataItem(doubleRow(185.4, 76.4)), ScatterDataItem(doubleRow(168.9, 69.1)), ScatterDataItem(doubleRow(167.6, 84.5)), ScatterDataItem(doubleRow(175.3, 64.5)), ScatterDataItem(doubleRow(170.2, 69.1)),
        ScatterDataItem(doubleRow(190.5, 108.6)), ScatterDataItem(doubleRow(177.8, 86.4)), ScatterDataItem(doubleRow(190.5, 80.9)), ScatterDataItem(doubleRow(177.8, 87.7)), ScatterDataItem(doubleRow(184.2, 94.5)),
        ScatterDataItem(doubleRow(176.5, 80.2)), ScatterDataItem(doubleRow(177.8, 72.0)), ScatterDataItem(doubleRow(180.3, 71.4)), ScatterDataItem(doubleRow(171.4, 72.7)), ScatterDataItem(doubleRow(172.7, 84.1)),
        ScatterDataItem(doubleRow(172.7, 76.8)), ScatterDataItem(doubleRow(177.8, 63.6)), ScatterDataItem(doubleRow(177.8, 80.9)), ScatterDataItem(doubleRow(182.9, 80.9)), ScatterDataItem(doubleRow(170.2, 85.5)),
        ScatterDataItem(doubleRow(167.6, 68.6)), ScatterDataItem(doubleRow(175.3, 67.7)), ScatterDataItem(doubleRow(165.1, 66.4)), ScatterDataItem(doubleRow(185.4, 102.3)), ScatterDataItem(doubleRow(181.6, 70.5)),
        ScatterDataItem(doubleRow(172.7, 95.9)), ScatterDataItem(doubleRow(190.5, 84.1)), ScatterDataItem(doubleRow(179.1, 87.3)), ScatterDataItem(doubleRow(175.3, 71.8)), ScatterDataItem(doubleRow(170.2, 65.9)),
        ScatterDataItem(doubleRow(193.0, 95.9)), ScatterDataItem(doubleRow(171.4, 91.4)), ScatterDataItem(doubleRow(177.8, 81.8)), ScatterDataItem(doubleRow(177.8, 96.8)), ScatterDataItem(doubleRow(167.6, 69.1)),
        ScatterDataItem(doubleRow(167.6, 82.7)), ScatterDataItem(doubleRow(180.3, 75.5)), ScatterDataItem(doubleRow(182.9, 79.5)), ScatterDataItem(doubleRow(176.5, 73.6)), ScatterDataItem(doubleRow(186.7, 91.8)),
        ScatterDataItem(doubleRow(188.0, 84.1)), ScatterDataItem(doubleRow(188.0, 85.9)), ScatterDataItem(doubleRow(177.8, 81.8)), ScatterDataItem(doubleRow(174.0, 82.5)), ScatterDataItem(doubleRow(177.8, 80.5)),
        ScatterDataItem(doubleRow(171.4, 70.0)), ScatterDataItem(doubleRow(185.4, 81.8)), ScatterDataItem(doubleRow(185.4, 84.1)), ScatterDataItem(doubleRow(188.0, 90.5)), ScatterDataItem(doubleRow(188.0, 91.4)),
        ScatterDataItem(doubleRow(182.9, 89.1)), ScatterDataItem(doubleRow(176.5, 85.0)), ScatterDataItem(doubleRow(175.3, 69.1)), ScatterDataItem(doubleRow(175.3, 73.6)), ScatterDataItem(doubleRow(188.0, 80.5)),
        ScatterDataItem(doubleRow(188.0, 82.7)), ScatterDataItem(doubleRow(175.3, 86.4)), ScatterDataItem(doubleRow(170.5, 67.7)), ScatterDataItem(doubleRow(179.1, 92.7)), ScatterDataItem(doubleRow(177.8, 93.6)),
        ScatterDataItem(doubleRow(175.3, 70.9)), ScatterDataItem(doubleRow(182.9, 75.0)), ScatterDataItem(doubleRow(170.8, 93.2)), ScatterDataItem(doubleRow(188.0, 93.2)), ScatterDataItem(doubleRow(180.3, 77.7)),
        ScatterDataItem(doubleRow(177.8, 61.4)), ScatterDataItem(doubleRow(185.4, 94.1)), ScatterDataItem(doubleRow(168.9, 75.0)), ScatterDataItem(doubleRow(185.4, 83.6)), ScatterDataItem(doubleRow(180.3, 85.5)),
        ScatterDataItem(doubleRow(174.0, 73.9)), ScatterDataItem(doubleRow(167.6, 66.8)), ScatterDataItem(doubleRow(182.9, 87.3)), ScatterDataItem(doubleRow(160.0, 72.3)), ScatterDataItem(doubleRow(180.3, 88.6)),
        ScatterDataItem(doubleRow(167.6, 75.5)), ScatterDataItem(doubleRow(186.7, 101.4)), ScatterDataItem(doubleRow(175.3, 91.1)), ScatterDataItem(doubleRow(175.3, 67.3)), ScatterDataItem(doubleRow(175.9, 77.7)),
        ScatterDataItem(doubleRow(175.3, 81.8)), ScatterDataItem(doubleRow(179.1, 75.5)), ScatterDataItem(doubleRow(181.6, 84.5)), ScatterDataItem(doubleRow(177.8, 76.6)), ScatterDataItem(doubleRow(182.9, 85.0)),
        ScatterDataItem(doubleRow(177.8, 102.5)), ScatterDataItem(doubleRow(184.2, 77.3)), ScatterDataItem(doubleRow(179.1, 71.8)), ScatterDataItem(doubleRow(176.5, 87.9)), ScatterDataItem(doubleRow(188.0, 94.3)),
        ScatterDataItem(doubleRow(174.0, 70.9)), ScatterDataItem(doubleRow(167.6, 64.5)), ScatterDataItem(doubleRow(170.2, 77.3)), ScatterDataItem(doubleRow(167.6, 72.3)), ScatterDataItem(doubleRow(188.0, 87.3)),
        ScatterDataItem(doubleRow(174.0, 80.0)), ScatterDataItem(doubleRow(176.5, 82.3)), ScatterDataItem(doubleRow(180.3, 73.6)), ScatterDataItem(doubleRow(167.6, 74.1)), ScatterDataItem(doubleRow(188.0, 85.9)),
        ScatterDataItem(doubleRow(180.3, 73.2)), ScatterDataItem(doubleRow(167.6, 76.3)), ScatterDataItem(doubleRow(183.0, 65.9)), ScatterDataItem(doubleRow(183.0, 90.9)), ScatterDataItem(doubleRow(179.1, 89.1)),
        ScatterDataItem(doubleRow(170.2, 62.3)), ScatterDataItem(doubleRow(177.8, 82.7)), ScatterDataItem(doubleRow(179.1, 79.1)), ScatterDataItem(doubleRow(190.5, 98.2)), ScatterDataItem(doubleRow(177.8, 84.1)),
        ScatterDataItem(doubleRow(180.3, 83.2)), ScatterDataItem(doubleRow(180.3, 83.2)),
    )

/** Seed of the transition delays; the upstream page uses `Math.random() * 400` here. */
private val AGGREGATE_DELAY = Lcg(17)

/** Mean of one column of the table, the number the summary bars show. */
private fun aggregateAverage(data: Array<ScatterDataItem?>, dimension: Int): Double {
    var total = 0.0
    for (item in data) {
        total += numberAt(item, dimension)
    }
    return total / data.size
}

/** Delay of one item of the universal transition, in milliseconds. */
private fun aggregateDelay(): UniversalTransitionOption =
    unsafeJso {
        enabled = true
        delay = { _, _ -> AGGREGATE_DELAY.next() * 400.0 }
    }

/** The cloud of the two groups; every point carries the group it belongs to. */
private fun aggregateScatterOption(): EChartsOption =
    unsafeJso {
        xAxis = one(unsafeJso<XAxisOption> { scale = true })
        yAxis = one(unsafeJso<YAxisOption> { scale = true })
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    id = "female"
                    dataGroupId = OptionId("female")
                    universalTransition = BooleanOrUniversalTransitionOption(aggregateDelay())
                    typedData = AGGREGATE_FEMALE
                },
                scatterSeries {
                    id = "male"
                    dataGroupId = OptionId("male")
                    universalTransition = BooleanOrUniversalTransitionOption(aggregateDelay())
                    typedData = AGGREGATE_MALE
                },
            )
    }

/** One summary bar: the average of a group, tagged with the group so the transition can match it. */
private fun aggregateBar(name: String, value: Double): BarDataItem =
    BarDataItem(
        unsafeJso<BarDataItemOption> {
            this.value = OptionDataValue(value)
            groupId = OptionId(name)
        },
    )

/** The two averages as bars; `seriesKey` lets the scatter groups fold into them. */
private fun aggregateBarOption(): EChartsOption =
    unsafeJso {
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    data = strings<OptionDataItem>("Female", "Male")
                },
            )
        yAxis = one(unsafeJso<YAxisOption> {})
        series =
            arrayOf<SeriesOption>(
                barSeries {
                    id = "total"
                    typedData =
                        dataItems(
                            aggregateBar("male", aggregateAverage(AGGREGATE_MALE, 0)),
                            aggregateBar("female", aggregateAverage(AGGREGATE_FEMALE, 0)),
                        )
                    universalTransition =
                        BooleanOrUniversalTransitionOption(
                            unsafeJso<UniversalTransitionOption> {
                                enabled = true
                                seriesKey = StringOrStringArray(arrayOf("female", "male"))
                                delay = { _, _ -> AGGREGATE_DELAY.next() * 400.0 }
                            },
                        )
                },
            )
    }

/** How long the cloud and the bars alternate, in milliseconds. */
private const val AGGREGATE_INTERVAL_MILLIS = 2000

val scatterTransitionEntries: List<CatalogEntry> =
    listOf(
        scatterTransition.interactive(
            "scatter-symbol-morph",
            "散点图变形动画",
            "Symbol Shape Morph",
            "同样的 10×10 个点每 700 毫秒换一种形状，`universalTransition` 把方块补形成心形、笑脸、" +
                "馅饼和飞机；后面的选项只写 `symbol`，其余字段靠 `setOption` 合并。",
            "The same hundred points change shape every 700 milliseconds; `universalTransition` morphs " +
                "the squares into hearts, smiles, pies and a plane, and every later option only " +
                "carries the new `symbol` while `setOption` merges the rest.",
        ) { chart, holder ->
            var optionIndex = 0
            chart.setOption(MORPH_OPTIONS[optionIndex])
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                optionIndex = (optionIndex + 1) % MORPH_OPTIONS.size
                chart.setOption(MORPH_OPTIONS[optionIndex])
            }, MORPH_INTERVAL_MILLIS)
        },
        scatterTransition.interactive(
            "scatter-aggregate-bar",
            "散点图聚合为柱状图动画",
            "Aggregate Scatter to Bar",
            "两组身高体重散点每两秒折叠成两根平均身高柱，再展开回散点；" +
                "`dataGroupId` 与 `seriesKey` 把散点的分组和柱子的 `groupId` 对上。",
            "Two clouds of heights and weights fold into two average bars every two seconds and " +
                "unfold again; `dataGroupId` and `seriesKey` match the scatter groups with the " +
                "`groupId` of the bars.",
        ) { chart, holder ->
            val scatter = aggregateScatterOption()
            val bars = aggregateBarOption()
            var current = scatter
            chart.setOption(current)
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                current = if (current === scatter) bars else scatter
                chart.setOption(current, true)
            }, AGGREGATE_INTERVAL_MILLIS)
        },
    )
