package demo.json.examples.shared

import demo.json.linkItem
import demo.json.timedValue
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.dataset.row
import demo.json.nodes

/** A plain named sankey node. */
internal fun sankeyNode(name: String): DataValue = dataItem { this.name = name }

/** A sankey node with the same fill and border color, the shape of `sankey-itemstyle`. */
internal fun coloredNode(name: String, hex: String): DataValue =
    dataItem {
        this.name = name
        itemStyle {
            color = hex.asColor()
            borderColor = hex.asColor()
        }
    }

/** Six nodes and six links of the upstream basic example. */
internal val SIMPLE_NODESSankey = listOf("a", "b", "a1", "a2", "b1", "c")

/** Nodes of the vertical example. */
internal val VERTICAL_NODES = listOf("a", "b", "a1", "b1", "c", "e")

/** Nodes of the sampled `sankey-itemstyle` table: `name to color`. */
internal val ITEM_STYLE_NODES =
    listOf(
        "FRF" to "#5ba33b",
        "Colomiers" to "#efa835",
        "SuperiorCard" to "rgba(128,155,72,255)",
        "DEM" to "#4E342E",
        "München" to "#4dc0a6",
        "GBP" to "#C32D2E",
        "Reading" to "#72A376",
        "CAD" to "#40699D",
        "Shawnee" to "#F07F09",
        "Kirkby" to "#CCAF0A",
        "Roncq" to "#4D291C",
        "Peterborough" to "#7A072D",
        "Frankfurt am Main" to "#EA005E",
        "Oxford" to "#D8B25C",
        "Vista" to "rgba(106,82,134,255)",
        "Langley" to "#CD7B38",
    )

/** Twelve links of the sampled `sankey-itemstyle` table, in upstream order. */
internal val ITEM_STYLE_LINKS =
    listOf(
        linkItem("FRF", "Colomiers", 357.8399963378906),
        linkItem("SuperiorCard", "FRF", 894.5999908447266),
        linkItem("DEM", "München", 178.9199981689453),
        linkItem("GBP", "Reading", 188.52999836206436),
        linkItem("CAD", "Shawnee", 2346.919983509928),
        linkItem("GBP", "Kirkby", 753.4000000059605),
        linkItem("FRF", "Roncq", 178.9199981689453),
        linkItem("GBP", "Peterborough", 999.159998036921),
        linkItem("DEM", "Frankfurt am Main", 536.7599945068359),
        linkItem("GBP", "Oxford", 1831.3799968883395),
        linkItem("Vista", "FRF", 1789.1999816894531),
        linkItem("CAD", "Langley", 1274.8199949413538),
    )

/** First 12 days of the basic theme river. */
internal val RIVER_DAYS =
    listOf(
        "2015/11/08",
        "2015/11/09",
        "2015/11/10",
        "2015/11/11",
        "2015/11/12",
        "2015/11/13",
        "2015/11/14",
        "2015/11/15",
        "2015/11/16",
        "2015/11/17",
        "2015/11/18",
        "2015/11/19",
    )

/** Builds one `[day, value, band]` row per day. */
internal fun riverBand(label: String, values: List<Number>): List<DataValue> =
    RIVER_DAYS.indices.map { index -> timedValue(RIVER_DAYS[index], values[index], label) }

/** Builds one `[month, value, genre]` row per month, the shape of the Lastfm example. */
internal fun lastfmBand(label: String, values: List<Number>): List<DataValue> =
    values.mapIndexed { month, value ->
        DataValue.Array(listOf(valueOf(month), valueOf(value), textValue(label)))
    }
