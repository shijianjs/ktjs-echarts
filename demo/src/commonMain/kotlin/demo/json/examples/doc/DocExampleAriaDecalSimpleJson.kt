package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.decalAxes
import demo.json.*

private val docExampleAriaDecalSimpleJsonCategory = JsonCategory("json/examples/doc/DocExampleAriaDecalSimpleJson.kt")

val docExampleAriaDecalSimpleJsonExample: JsonExample =
    docExampleAriaDecalSimpleJsonCategory.example("doc-example/aria-decal-simple") {
            decalAxes()
            series {
                bar { values(120, 200, 150, 80, 70, 110, 130) }
                bar { values(20, 40, 90, 40, 30, 70, 120) }
                bar { values(140, 230, 120, 50, 30, 150, 120) }
            }
            aria {
                enabled = true
                decal = AriaDecalOption().apply { show = true }
            }
        }
