import org.jetbrains.kotlin.gradle.ExperimentalJsTestDsl

plugins {
    kotlin("multiplatform")
}

// Wrapper version = {upstream npm version}-kt.{revision}. See the versioning section in AGENTS.md.
@Suppress("UNCHECKED_CAST")
val npmVersions = rootProject.extra["npmVersions"] as Map<String, String>
@Suppress("UNCHECKED_CAST")
val wrapperRevisions = rootProject.extra["wrapperRevisions"] as Map<String, Int>
val upstreamVersion = npmVersions.getValue("echarts-gl")
val wrapperRevision = wrapperRevisions.getValue(project.name)
version = "$upstreamVersion-kt.$wrapperRevision"
description = "Kotlin/JS wrapper for the echarts-gl package."

kotlin {
    js {
        useEsModules()

        // echarts-gl renders through WebGL, so the tests have to run in a real browser with a
        // WebGL context. Playwright's Chromium provides WebGL2 through SwiftShader.
        browser {
            @OptIn(ExperimentalJsTestDsl::class)
            test {
                chromium()
            }
        }
    }

    sourceSets {
        jsMain.dependencies {
            implementation(npm("echarts-gl", upstreamVersion))

            // The GL chart and component types extend EChartsOption / SeriesOption and reuse the
            // option types of the echarts package; consumers need both on the classpath.
            api(project(":ktjs-echarts"))
        }
        jsTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
