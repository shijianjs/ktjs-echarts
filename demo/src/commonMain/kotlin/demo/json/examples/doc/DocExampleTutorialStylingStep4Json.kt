package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.tutorialStyling
import demo.json.*

private val docExampleTutorialStylingStep4JsonCategory = JsonCategory("json/examples/doc/DocExampleTutorialStylingStep4Json.kt")

val docExampleTutorialStylingStep4JsonExample: JsonExample =
    docExampleTutorialStylingStep4JsonCategory.example("doc-example/tutorial-styling-step4") {
            tutorialStyling(4)
        }
