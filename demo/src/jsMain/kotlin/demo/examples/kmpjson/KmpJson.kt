package demo.examples.kmpjson

import demo.Catalog
import demo.CatalogEntry
import demo.ExampleBody
import demo.Groups
import demo.json.ModelExample
import demo.json.kmpJsonExamples

/**
 * Presentation of the pure-KMP `kmp-echarts-json` pages.
 *
 * The copy, the model builder and the serialized JSON all live in `commonMain/kotlin/demo/json/ModelExamples.kt`;
 * this class only turns each [ModelExample] into a catalog entry. The page itself is [demo.ExamplePage],
 * which shows the Kotlin builder, the JSON and the chart that consumes it — the model's whole point is
 * that the same option is written once and consumed as JSON.
 *
 * The list is built on first read rather than at class initialisation: it depends on `commonMain` data,
 * and nothing should have to know in advance when that is ready.
 */
internal class KmpJson {
    companion object {
        val ENTRIES: List<CatalogEntry> by lazy { kmpJsonExamples.map(::modelEntry) }

        /** Path of the model builders, relative to `demo/src/commonMain/kotlin/demo`. */
        private const val MODEL_SOURCE: String = "json/ModelExamples.kt"

        private fun modelEntry(example: ModelExample): CatalogEntry =
            CatalogEntry(
                id = example.id,
                group = Groups.JSON,
                titleZh = example.titleZh,
                titleEn = example.titleEn,
                summaryZh = example.summaryZh,
                summaryEn = example.summaryEn,
                body =
                    ExampleBody.Model(
                        json = example.json,
                        kotlinSource = example.kotlinSource,
                    ),
                sourcePath = MODEL_SOURCE,
                sourceRoot = Catalog.REPO_COMMON_SOURCE_ROOT,
                officialUrl = null,
            )
    }
}
