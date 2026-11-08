package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.tutorialStyling
import demo.json.*

private val docExampleTutorialStylingStep0JsonCategory = JsonCategory("json/examples/doc/DocExampleTutorialStylingStep0Json.kt")

val docExampleTutorialStylingStep0JsonExample: JsonExample =
    docExampleTutorialStylingStep0JsonCategory.example("doc-example/tutorial-styling-step0") {
            tutorialStyling(0)
        }
