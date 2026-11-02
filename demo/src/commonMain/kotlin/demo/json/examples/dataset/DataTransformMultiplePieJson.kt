package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.*

private val dataTransformMultiplePieJsonCategory = JsonCategory("json/examples/dataset/DataTransformMultiplePieJson.kt")

val dataTransformMultiplePieJsonExample: JsonExample =
    dataTransformMultiplePieJsonCategory.example("data-transform-multiple-pie") {
            dataset {
                rows(
                    row(
                        textValue("Product"),
                        textValue("Sales"),
                        textValue("Price"),
                        textValue("Year"),
                    ),
                    namedRow("Cake", 123, 32, 2011),
                    namedRow("Cereal", 231, 14, 2011),
                    namedRow("Tofu", 235, 5, 2011),
                    namedRow("Dumpling", 341, 25, 2011),
                    namedRow("Biscuit", 122, 29, 2011),
                    namedRow("Cake", 143, 30, 2012),
                    namedRow("Cereal", 201, 19, 2012),
                    namedRow("Tofu", 255, 7, 2012),
                    namedRow("Dumpling", 241, 27, 2012),
                    namedRow("Biscuit", 102, 34, 2012),
                    namedRow("Cake", 153, 28, 2013),
                    namedRow("Cereal", 181, 21, 2013),
                    namedRow("Tofu", 395, 4, 2013),
                    namedRow("Dumpling", 281, 31, 2013),
                    namedRow("Biscuit", 92, 39, 2013),
                    namedRow("Cake", 223, 29, 2014),
                    namedRow("Cereal", 211, 17, 2014),
                    namedRow("Tofu", 345, 3, 2014),
                    namedRow("Dumpling", 211, 35, 2014),
                    namedRow("Biscuit", 72, 24, 2014),
                )
            }
            dataset { transform = transforms(yearFilter(2011)) }
            dataset { transform = transforms(yearFilter(2012)) }
            dataset { transform = transforms(yearFilter(2013)) }
            series {
                stackedYearPie("50%", "25%", 1)
                stackedYearPie("50%", "50%", 2)
                stackedYearPie("50%", "75%", 3)
            }
            // Only for the responsive layout: the pies sit in a row on a wide canvas.
            media {
                query = buildJsonObject { put("minAspectRatio", JsonPrimitive(1)) }
                option = pieCenters("25%", "50%", "75%")
            }
            media { option = pieCenters("50%", "25%", "50%") }
        }
