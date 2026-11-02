package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.tutorialStyling
import demo.json.*

private val docExampleTutorialStylingStep5JsonCategory = JsonCategory("json/examples/doc/DocExampleTutorialStylingStep5Json.kt")

val docExampleTutorialStylingStep5JsonExample: JsonExample =
    docExampleTutorialStylingStep5JsonCategory.example("doc-example/tutorial-styling-step5") {
            tutorialStyling(5)
        }
