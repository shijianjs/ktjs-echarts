package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.tutorialStyling
import demo.json.*

private val docExampleTutorialStylingStep1JsonCategory = JsonCategory("json/examples/doc/DocExampleTutorialStylingStep1Json.kt")

val docExampleTutorialStylingStep1JsonExample: JsonExample =
    docExampleTutorialStylingStep1JsonCategory.example("doc-example/tutorial-styling-step1") {
            tutorialStyling(1)
        }
