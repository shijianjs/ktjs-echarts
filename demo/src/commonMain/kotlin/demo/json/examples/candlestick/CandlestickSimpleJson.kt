package demo.json.examples.candlestick

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.rowsOf

private val candlestickSimpleJsonCategory = JsonCategory("json/examples/candlestick/CandlestickSimpleJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val candlestickSimpleJsonExample: JsonExample =
    // --------------------------------------------------------------- candlestick
        candlestickSimpleJsonCategory.example("candlestick-simple") {
            xAxis {
                type = AxisType.Category
                categories("2017-10-24", "2017-10-25", "2017-10-26", "2017-10-27")
            }
            yAxis { }
            series {
                candlestick {
                    data =
                        rowsOf(
                            listOf<Number>(20.0, 34.0, 10.0, 38.0),
                            listOf<Number>(40.0, 35.0, 30.0, 50.0),
                            listOf<Number>(31.0, 38.0, 33.0, 44.0),
                            listOf<Number>(38.0, 15.0, 5.0, 42.0),
                        )
                }
            }
        }
