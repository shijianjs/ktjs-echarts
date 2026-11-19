package demo.examples.kmpjson

import demo.CatalogEntry
import demo.ExampleBody
import demo.Groups
import demo.json.ModelExample
import demo.json.kmpJsonExamples
import demo.REPO_COMMON_SOURCE_ROOT

/**
 * Presentation of the pure-KMP `kmp-echarts-json` pages.
 *
 * The copy, the model builder and the serialized JSON all live in `commonMain/kotlin/demo/json/ModelExamples.kt`;
 * this file only turns each [ModelExample] into a catalog entry. The page itself is [demo.ExamplePage],
 * which shows the Kotlin builder, the JSON and the chart that consumes it — the model's whole point is
 * that the same option is written once and consumed as JSON.
 */
val kmpJsonEntries: List<CatalogEntry> = kmpJsonExamples.map(::modelEntry)

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
        sourceRoot = demo.REPO_COMMON_SOURCE_ROOT,
        officialUrl = null,
    )

/** Path of the model builders, relative to `demo/src/commonMain/kotlin/demo`. */
private const val MODEL_SOURCE: String = "json/ModelExamples.kt"
