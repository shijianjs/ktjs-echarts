package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValues
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val docExampleAriaPieJsonCategory = JsonCategory("json/examples/doc/DocExampleAriaPieJson.kt")

val docExampleAriaPieJsonExample: JsonExample =
    docExampleAriaPieJsonCategory.example("doc-example/aria-pie") {
            aria { enabled = true }
            title {
                text = "某站点用户访问来源"
                left = str("center")
            }
            series {
                pie {
                    name = "访问来源"
                    data =
                        namedValues(
                            "直接访问" to 335,
                            "邮件营销" to 310,
                            "联盟广告" to 234,
                            "视频广告" to 135,
                            "搜索引擎" to 1548,
                        )
                }
            }
        }
