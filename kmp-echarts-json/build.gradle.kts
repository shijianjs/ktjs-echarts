plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

// This module models the ECharts option schema, so its base version tracks the echarts npm
// version. Version = {echarts version}-kt.{revision}; see the versioning section in AGENTS.md.
@Suppress("UNCHECKED_CAST")
val npmVersions = rootProject.extra["npmVersions"] as Map<String, String>
@Suppress("UNCHECKED_CAST")
val wrapperRevisions = rootProject.extra["wrapperRevisions"] as Map<String, Int>
version = "${npmVersions.getValue("echarts")}-kt.${wrapperRevisions.getValue(project.name)}"
description = "Kotlin Multiplatform model for building type-safe ECharts option JSON."

kotlin {
    jvm()
    js {
        useEsModules()
        nodejs()
    }
    wasmJs {
        nodejs()
    }
    linuxX64()
    mingwX64()

    sourceSets {
        commonMain.dependencies {
            // Both are public API: JsonElement appears in escape hatches, Instant/LocalDateTime in
            // time values.
            api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
            api("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
