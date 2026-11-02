package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.tutorialStyling
import demo.json.*

private val docExampleTutorialStylingStep3JsonCategory = JsonCategory("json/examples/doc/DocExampleTutorialStylingStep3Json.kt")

val docExampleTutorialStylingStep3JsonExample: JsonExample =
    docExampleTutorialStylingStep3JsonCategory.example("doc-example/tutorial-styling-step3") {
            tutorialStyling(3)
        }
