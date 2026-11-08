package demo.json.examples.shared

import demo.json.nodeItem
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.sunburst.level

/** One leaf of the flare hierarchy. */
internal fun flareLeaf(name: String, value: Number): DataItem = nodeItem(name, value)

/**
 * The flare hierarchy used by the four orientation pages.
 *
 * Mirrors the branches of the upstream `flare.json` table with the same names and values; the
 * `query` / `methods` branch keeps a representative slice of the 49 method leaves.
 */
internal fun flareTree(collapseAlternate: Boolean = false): DataItem =
    nodeItem(
        "flare",
        null,
        nodeItem(
            "data",
            null,
            nodeItem(
                "converters",
                null,
                flareLeaf("Converters", 721),
                flareLeaf("DelimitedTextConverter", 4294),
            ),
            nodeItem("DataUtil", 3322),
        ),
        nodeItem(
            "display",
            null,
            flareLeaf("DirtySprite", 8833),
            flareLeaf("LineSprite", 1732),
            flareLeaf("RectSprite", 3623),
        ),
        nodeItem("flex", null, flareLeaf("FlareVis", 4116)),
        nodeItem(
            "query",
            null,
            flareLeaf("AggregateExpression", 1616),
            flareLeaf("And", 1027),
            flareLeaf("Arithmetic", 3891),
            flareLeaf("Count", 781),
            nodeItem(
                "methods",
                null,
                flareLeaf("add", 593),
                flareLeaf("and", 330),
                flareLeaf("average", 287),
                flareLeaf("count", 277),
            ),
            flareLeaf("Query", 13896),
            flareLeaf("Sum", 791),
        ),
        nodeItem(
            "scale",
            null,
            flareLeaf("IScaleMap", 2105),
            flareLeaf("LinearScale", 1316),
            flareLeaf("LogScale", 3151),
            flareLeaf("OrdinalScale", 3770),
            flareLeaf("TimeScale", 5833),
        ),
    ).apply {
        // `tree-basic` / `tree-orient-right-left` collapse every other top-level branch on load.
        if (collapseAlternate) {
            children?.forEachIndexed { index, child ->
                if (index % 2 == 0) child.collapsed = true
            }
        }
    }

/** Branch labels of the left-to-right pages. */
internal fun SeriesOption.orientedLabels(labelPosition: String, labelAlign: String) {
    label {
        position = labelPosition
        verticalAlign = "middle"
        align = labelAlign
    }
    emphasis { focus = "descendant" }
    expandAndCollapse = true
}
