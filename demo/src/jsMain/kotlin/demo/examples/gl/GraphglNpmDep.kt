package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.unsafeCast
import demo.examples.Category

private val graphglNpmDepCategory = Category(Groups.GL, "examples/gl/GraphglNpmDep.kt")

val graphglNpmDepEntry: CatalogEntry =
    graphglNpmDepCategory.interactive(
            "graphgl-npm-dep",
            "NPM 依赖关系图",
            "NPM Dependencies with graphGL",
            "一万个 npm 包、两万七千条依赖边：节点大小按依赖者数量的平方根，依赖者超过 5000 的包直接显示名字。",
            "Ten thousand npm packages and twenty-seven thousand dependency edges: a node's size is " +
                "the square root of its dependent count, and packages with more than 5000 dependents " +
                "show their name.",
        ) { chart ->
            loadJson("data/gl/npmdep.json").then { json ->
                chart.setOption(npmDependencyOption(json.unsafeCast<NpmDependencyJson>()))
            }
        }
