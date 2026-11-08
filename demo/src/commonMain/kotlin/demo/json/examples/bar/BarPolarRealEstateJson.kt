package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barPolarRealEstateJsonCategory = JsonCategory("json/examples/bar/BarPolarRealEstateJson.kt")

val barPolarRealEstateJsonExample: JsonExample =
    barPolarRealEstateJsonCategory.example("bar-polar-real-estate") {
            title {
                text = "How expensive is it to rent an apartment in China?"
                subtext = "Data from https://www.numbeo.com"
            }
            legend {
                show = true
                top = str("bottom")
                entries("Range", "Average")
            }
            grid { top = num(100) }
            angleAxis {
                type = AxisType.Category
                categories(*REAL_ESTATE_CITIES)
            }
            tooltip { show = true }
            radiusAxis { }
            polar { }
            series {
                polarBar(null, REAL_ESTATE_ROWS.map { it[0] }, transparent = true, group = "Min Max")
                polarBar("Range", REAL_ESTATE_ROWS.map { it[1] - it[0] }, transparent = false, group = "Min Max")
                polarBar(
                    null,
                    REAL_ESTATE_ROWS.map { it[2] - REAL_ESTATE_BAR_HEIGHT },
                    transparent = true,
                    group = "Average",
                    layer = 10.0,
                )
                polarBar(
                    "Average",
                    REAL_ESTATE_ROWS.map { REAL_ESTATE_BAR_HEIGHT * 2.0 },
                    transparent = false,
                    group = "Average",
                    gap = "-100%",
                    layer = 10.0,
                )
            }
        }
