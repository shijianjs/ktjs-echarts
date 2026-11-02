package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val map3dAlcoholConsumptionCategory = Category(Groups.GL, "examples/gl/Map3dAlcoholConsumption.kt")

val map3dAlcoholConsumptionEntry: CatalogEntry =
    map3dAlcoholConsumptionCategory.interactive(
            "map3d-alcohol-consumption",
            "Map3D - 酒精消费量",
            "Map3D - Alcohol Consumption",
            "世界地图按人均饮酒量着色并挤出成立体，视线从正上方略偏的角度俯瞰。",
            "The world map colored by alcohol consumption per person and extruded, seen from a " +
                "slightly tilted top-down view.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/gl/alcohol.json").then { alcohol ->
                    chart.setOption(alcoholMapOption(alcohol))
                }
            }
        }
