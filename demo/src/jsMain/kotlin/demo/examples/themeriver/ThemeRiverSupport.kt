package demo.examples.themeriver

import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.ThemeRiverDataItem
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.funnel.labels
import demo.examples.matrix.names

/** One `[date, value, name]` tuple of a theme river. */
internal fun river(date: String, value: Double, name: String): ThemeRiverDataItem =
    arrayOf<OptionDataValue?>(OptionDataValue(date), OptionDataValue(value), OptionDataValue(name))

internal val days: List<String> = (8..28).map { day -> "2015/11/${day.toString().padStart(2, '0')}" }

/** Pairs the 21 values of one band with the dates of `themeRiver-basic`. */
internal fun band(name: String, values: DoubleArray): List<ThemeRiverDataItem> =
    days.mapIndexed { index, date -> river(date, values[index], name) }

internal val dqBand = doubleArrayOf(10.0, 15.0, 35.0, 38.0, 22.0, 16.0, 7.0, 2.0, 17.0, 33.0, 40.0, 32.0, 26.0, 35.0, 40.0, 32.0, 26.0, 22.0, 16.0, 22.0, 10.0)

internal val tyBand = doubleArrayOf(35.0, 36.0, 37.0, 22.0, 24.0, 26.0, 34.0, 21.0, 18.0, 45.0, 32.0, 35.0, 30.0, 28.0, 27.0, 26.0, 15.0, 30.0, 35.0, 42.0, 42.0)

internal val ssBand = doubleArrayOf(21.0, 25.0, 27.0, 23.0, 24.0, 21.0, 35.0, 39.0, 40.0, 36.0, 33.0, 43.0, 40.0, 34.0, 28.0, 26.0, 37.0, 41.0, 46.0, 47.0, 41.0)

internal val qgBand = doubleArrayOf(10.0, 15.0, 35.0, 38.0, 22.0, 16.0, 7.0, 2.0, 17.0, 33.0, 40.0, 32.0, 26.0, 35.0, 40.0, 32.0, 26.0, 22.0, 16.0, 22.0, 10.0)

internal val syBand = doubleArrayOf(10.0, 15.0, 35.0, 38.0, 22.0, 16.0, 7.0, 2.0, 17.0, 33.0, 40.0, 32.0, 26.0, 35.0, 4.0, 32.0, 26.0, 22.0, 16.0, 22.0, 10.0)

internal val ddBand = doubleArrayOf(10.0, 15.0, 35.0, 38.0, 22.0, 16.0, 7.0, 2.0, 17.0, 33.0, 4.0, 32.0, 26.0, 35.0, 40.0, 32.0, 26.0, 22.0, 16.0, 22.0, 10.0)

/** `themeRiver-basic` renders the six bands one after the other. */
internal fun basicRiverData(): Array<ThemeRiverDataItem?> =
    dataItems(*(band("DQ", dqBand) + band("TY", tyBand) + band("SS", ssBand) + band("QG", qgBand) + band("SY", syBand) + band("DD", ddBand)).toTypedArray())

/*
 * `themeRiver-lastfm` plays the 20 albums of a last.fm listening history over 20 time steps; the
 * table is the `rawData` array of the original example.
 */
internal val lastFmAlbums =
    listOf(
        "The Sea and Cake",
        "Andrew Bird",
        "Laura Veirs",
        "Brian Eno",
        "Christopher Willits",
        "Wilco",
        "Edgar Meyer",
        "Béla Fleck",
        "Fleet Foxes",
        "Kings of Convenience",
        "Brett Dennen",
        "Psapp",
        "The Bad Plus",
        "Feist",
        "Battles",
        "Avishai Cohen",
        "Rachael Yamagata",
        "Norah Jones",
        "Béla Fleck and the Flecktones",
        "Joshua Redman",
    )

internal val lastFmRaw =
    listOf(
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 49.0, 67.0, 16.0, 0.0, 19.0, 19.0, 0.0, 0.0, 1.0, 10.0, 5.0, 6.0, 1.0, 1.0, 0.0, 25.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 6.0, 3.0, 34.0, 0.0, 16.0, 1.0, 0.0, 0.0, 1.0, 6.0, 0.0, 1.0, 56.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0),
        doubleArrayOf(0.0, 8.0, 13.0, 15.0, 0.0, 12.0, 23.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 6.0, 0.0, 0.0, 1.0, 0.0, 1.0),
        doubleArrayOf(0.0, 9.0, 28.0, 0.0, 91.0, 6.0, 1.0, 0.0, 0.0, 0.0, 7.0, 18.0, 0.0, 9.0, 16.0, 0.0, 1.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 3.0, 42.0, 36.0, 21.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 16.0, 30.0, 1.0, 4.0, 62.0, 55.0, 1.0, 0.0, 0.0),
        doubleArrayOf(0.0, 7.0, 13.0, 12.0, 64.0, 5.0, 0.0, 0.0, 0.0, 8.0, 17.0, 3.0, 72.0, 1.0, 1.0, 53.0, 1.0, 0.0, 0.0, 0.0),
        doubleArrayOf(1.0, 14.0, 13.0, 7.0, 8.0, 8.0, 7.0, 0.0, 1.0, 1.0, 14.0, 6.0, 44.0, 8.0, 7.0, 17.0, 21.0, 1.0, 0.0, 0.0),
        doubleArrayOf(0.0, 6.0, 14.0, 2.0, 14.0, 1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0, 7.0, 15.0, 6.0, 3.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 9.0, 11.0, 3.0, 0.0, 8.0, 0.0, 0.0, 14.0, 2.0, 0.0, 1.0, 1.0, 1.0, 7.0, 13.0, 2.0, 1.0, 0.0, 0.0),
        doubleArrayOf(0.0, 7.0, 5.0, 10.0, 8.0, 21.0, 0.0, 0.0, 130.0, 1.0, 2.0, 18.0, 6.0, 1.0, 5.0, 1.0, 4.0, 1.0, 0.0, 7.0),
        doubleArrayOf(0.0, 2.0, 15.0, 1.0, 5.0, 5.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 4.0, 1.0, 3.0, 1.0, 17.0, 0.0, 0.0, 9.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(6.0, 27.0, 26.0, 1.0, 0.0, 11.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 2.0, 0.0, 0.0, 9.0, 1.0, 0.0, 0.0, 0.0),
        doubleArrayOf(31.0, 81.0, 11.0, 6.0, 11.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0, 0.0, 3.0, 14.0, 0.0, 0.0, 12.0),
        doubleArrayOf(19.0, 53.0, 6.0, 20.0, 0.0, 4.0, 37.0, 0.0, 30.0, 86.0, 43.0, 7.0, 5.0, 7.0, 17.0, 19.0, 2.0, 0.0, 0.0, 5.0),
        doubleArrayOf(0.0, 22.0, 14.0, 6.0, 10.0, 24.0, 18.0, 0.0, 13.0, 21.0, 5.0, 2.0, 13.0, 35.0, 7.0, 1.0, 8.0, 0.0, 0.0, 1.0),
        doubleArrayOf(0.0, 56.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 7.0, 24.0, 0.0, 17.0, 7.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 8.0),
        doubleArrayOf(18.0, 29.0, 3.0, 6.0, 11.0, 0.0, 15.0, 0.0, 12.0, 42.0, 37.0, 0.0, 3.0, 3.0, 13.0, 8.0, 0.0, 0.0, 0.0, 1.0),
        doubleArrayOf(32.0, 39.0, 37.0, 3.0, 33.0, 21.0, 6.0, 0.0, 4.0, 17.0, 0.0, 11.0, 8.0, 2.0, 3.0, 0.0, 23.0, 0.0, 0.0, 17.0),
        doubleArrayOf(72.0, 15.0, 28.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 3.0, 0.0, 35.0, 0.0, 9.0, 17.0, 1.0, 9.0, 1.0, 0.0, 8.0),
        doubleArrayOf(11.0, 15.0, 4.0, 2.0, 0.0, 18.0, 10.0, 0.0, 20.0, 3.0, 0.0, 0.0, 2.0, 0.0, 0.0, 2.0, 2.0, 30.0, 0.0, 0.0),
        doubleArrayOf(14.0, 29.0, 19.0, 3.0, 2.0, 17.0, 13.0, 0.0, 7.0, 12.0, 2.0, 0.0, 6.0, 0.0, 0.0, 1.0, 1.0, 34.0, 0.0, 1.0),
        doubleArrayOf(1.0, 1.0, 7.0, 6.0, 1.0, 1.0, 15.0, 1.0, 1.0, 2.0, 1.0, 3.0, 1.0, 1.0, 9.0, 1.0, 1.0, 25.0, 1.0, 72.0),
    )

/**
 * Flattens the last.fm table into the `[step, value, album]` tuples the series expects.
 *
 * The upstream table has more rows than album names; the original reads them as a band without a
 * name (`labels[i]` is `undefined`), so the rows without a name are dropped here.
 */
internal fun lastFmData(): Array<ThemeRiverDataItem?> =
    dataItems(
        *lastFmRaw
            .mapIndexedNotNull { album, values ->
                lastFmAlbums.getOrNull(album)?.let { name ->
                    values.mapIndexed { step, value -> river(step.toString(), value, name) }
                }
            }
            .flatten()
            .toTypedArray(),
    )

/** The hover style shared by the theme river examples. */
internal fun riverEmphasis(): EmphasisOption =
    unsafeJso {
        itemStyle =
            unsafeJso<ItemStyleOption> {
                shadowBlur = 20.0
                shadowColor = ZRColor("rgba(0, 0, 0, 0.8)")
            }
    }
