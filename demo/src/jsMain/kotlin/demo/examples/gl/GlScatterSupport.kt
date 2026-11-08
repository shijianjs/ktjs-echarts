package demo.examples.gl

import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.shared.axisLabel
import demo.examples.shared.doubleRow
import demo.examples.shared.every
import demo.examples.shared.numberAt
import demo.examples.shared.row

/** Column indices of `nutrients.json` used by `scatter3d`. */
internal const val PROTEIN = 2

internal const val SODIUM = 4

internal const val FIBER = 5

internal const val VITAMIN_C = 6

/** `encode: { x, y, z, tooltip: [0..4] }` of a 3D series of the dataset pages. */
internal fun encode3(x: String, y: String, z: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
        this.z = OptionEncodeValue(DimensionLoose(z))
        tooltip = OptionEncodeValue(dimensionScratch())
    }

/** `encode: { x, y, tooltip: [0..4] }` of the 2D scatters next to the 3D one. */
internal fun encode2(x: String, y: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
        tooltip = OptionEncodeValue(dimensionScratch())
    }

/** The `[0, 1, 2, 3, 4]` tooltip columns every dataset page of the family shares. */
internal fun dimensionScratch(): Array<DimensionLoose> =
    Array(5) { index -> DimensionLoose(index.toDouble()) }

/** The `dataset` block of the `life-expectancy-table.json` pages. */
internal fun lifeExpectancyDataset(json: JsAny): DatasetOption =
    unsafeJso {
        dimensions =
            arrayOf(
                DimensionDefinitionLoose("Income"),
                DimensionDefinitionLoose("Life Expectancy"),
                DimensionDefinitionLoose("Population"),
                DimensionDefinitionLoose("Country"),
                DimensionDefinitionLoose(
                    unsafeJso<DimensionDefinition> {
                        name = "Year"
                        type = "ordinal"
                    },
                ),
            )
        source = json.unsafeCast<DatasetSource>()
    }

/**
 * One row of `nutrients.json` as a `scatter3D` item.
 *
 * The six values are the three coordinates followed by the two dimensions the `visualMap`
 * components read: the fiber content drives the colour, vitamin C the symbol size.
 */
internal fun nutrientItem(row: JsAny?, index: Int): Scatter3DDataItem =
    Scatter3DDataItem(
        doubleRow(
            numberAt(row, PROTEIN),
            numberAt(row, FIBER),
            numberAt(row, SODIUM),
            numberAt(row, FIBER),
            numberAt(row, VITAMIN_C),
            index.toDouble(),
        ),
    )

/**
 * One grid of the 2x2 scatter matrix, as wide as [width] and pinned to [bottom] or [top].
 */
internal fun matrixGrid(left: String, width: String, bottom: String? = null, top: String? = null): GridOption =
    unsafeJso {
        this.left = StringOrDouble(left)
        this.width = StringOrDouble(width)
        if (bottom != null) this.bottom = StringOrDouble(bottom)
        if (top != null) this.top = StringOrDouble(top)
    }

/** x axis of one panel of the scatter matrix. */
internal fun panelXAxis(index: Double, name: String, category: Boolean = false): XAxisOption =
    unsafeJso {
        type = if (category) AxisTypes.category else AxisTypes.value
        gridIndex = index
        this.name = name
        if (category) boundaryGap = BooleanOrStringArray(false)
        axisLabel =
            unsafeJso<AxisLabelOption> {
                rotate = 50.0
                interval = AxisInterval(0.0)
            }
    }

/** y axis of one panel of the scatter matrix. */
internal fun panelYAxis(index: Double, name: String): YAxisOption =
    unsafeJso {
        type = AxisTypes.value
        gridIndex = index
        this.name = name
    }
