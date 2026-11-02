package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.hiddenTextAxes
import demo.json.examples.shared.textBlock
import demo.json.examples.shared.titleBlockRich
import demo.json.*

private val docExampleTitleBlockJsonCategory = JsonCategory("json/examples/doc/DocExampleTitleBlockJson.kt")

val docExampleTitleBlockJsonExample: JsonExample =
    docExampleTitleBlockJsonCategory.example("doc-example/title-block") {
            series {
                scatter {
                    data =
                        listOf(
                            textBlock(
                                0,
                                "{tc|Center Title}{titleBg|}\n  Content text xxxxxxxx  xxxxxxxx  \n{hr|}\n" +
                                    "  xxxxx  xxxxxxxx  xxxxxxxxx  ",
                                true,
                            ),
                            textBlock(
                                1,
                                "{titleBg|Left Title}\n  Content text xxxxxxxx  xxxxxxxx  \n{hr|}\n" +
                                    "  xxxxx  xxxxxxxx  xxxxxxxxx  ",
                                false,
                            ),
                            textBlock(
                                2,
                                "{titleBg|Right Title}\n  Content text xxxxxxxx  xxxxxxxx  \n{hr|}\n" +
                                    "  xxxxx  xxxxxxxx  xxxxxxxxx  ",
                                true,
                            ),
                        )
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    label {
                        show = true
                        backgroundColor = "#ddd".asColor()
                        borderColor = "#555".asColor()
                        borderWidth = 1.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        color = "#000".asColor()
                        fontSize = 14.0
                        rich = titleBlockRich()
                    }
                }
            }
            hiddenTextAxes(minY = 0, maxY = 2, inverseY = true)
        }
