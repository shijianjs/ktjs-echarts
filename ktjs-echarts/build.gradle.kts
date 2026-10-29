import org.jetbrains.kotlin.gradle.ExperimentalJsTestDsl

plugins {
    kotlin("multiplatform")
}

// Wrapper version = {upstream npm version}-kt.{revision}. See the versioning section in AGENTS.md.
@Suppress("UNCHECKED_CAST")
val npmVersions = rootProject.extra["npmVersions"] as Map<String, String>
@Suppress("UNCHECKED_CAST")
val wrapperRevisions = rootProject.extra["wrapperRevisions"] as Map<String, Int>
val upstreamVersion = npmVersions.getValue("echarts")
val wrapperRevision = wrapperRevisions.getValue(project.name)
version = "$upstreamVersion-kt.$wrapperRevision"
description = "Kotlin/JS wrapper for the echarts package."

kotlin {
    js {
        useEsModules()

        // Browser test runtime. Swap to `nodejs()` (and jsNodeTest) for a Node-only package.
        browser {
            @OptIn(ExperimentalJsTestDsl::class)
            test {
                chromium()
            }
        }
    }

    sourceSets {
        jsMain.dependencies {
            implementation(npm("echarts", upstreamVersion))

            // The public API exposes `web.html.HTMLElement` for the chart container. kotlin-browser
            // types must not be mixed with Kotlin/JS's own org.w3c.dom.* / kotlinx.browser.*.
            api("org.jetbrains.kotlin-wrappers:kotlin-browser:2026.10.4")
        }
        jsTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
