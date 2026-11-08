package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.tutorialStyling
import demo.json.*

private val docExampleTutorialStylingStep2JsonCategory = JsonCategory("json/examples/doc/DocExampleTutorialStylingStep2Json.kt")

val docExampleTutorialStylingStep2JsonExample: JsonExample =
    docExampleTutorialStylingStep2JsonCategory.example("doc-example/tutorial-styling-step2") {
            tutorialStyling(2)
        }
