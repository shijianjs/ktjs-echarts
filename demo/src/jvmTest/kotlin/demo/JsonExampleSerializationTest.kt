package demo

import demo.json.jsonExamples
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * Serializes every `kmp-echarts-json` example on the JVM.
 *
 * The browser sweep in `DemoPageTest` only sees a page that failed to mount; an exception thrown
 * while the model serializes aborts the page build before any canvas exists. This test isolates that
 * step, so a broken option reports the offending field instead of "no canvas".
 */
class JsonExampleSerializationTest {
    @Test
    fun `every JSON example serializes and parses back`() {
        val failures =
            jsonExamples.values.mapNotNull { example ->
                try {
                    val json = example.json
                    val parsed = Json.parseToJsonElement(json)
                    assertTrue(parsed is JsonObject, "the option should be a JSON object")
                    null
                } catch (error: Throwable) {
                    "${example.id} (${example.sourcePath}): ${error.message}"
                }
            }
        assertTrue(failures.isEmpty(), "examples that failed to serialize:\n${failures.joinToString("\n")}")
    }
}
