pluginManagement {
    repositories {
        // The docs site is built with the ktjs-docusaurus Gradle plugin. Released versions come from
        // Central; `mavenLocal` is what serves a SNAPSHOT one, which is how the site is developed
        // against an unreleased wrapper. An empty local repository falls through to the next
        // repository, so the order costs nothing in CI.
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }

    // The plugin's version is a catalog entry (`libs.plugins.ktjs.docusaurus` in
    // `gradle/libs.versions.toml`); `docs` applies it with `alias(...)`, so no resolution strategy
    // is needed here.
}

dependencyResolutionManagement {
    repositories {
        // kotlin-wrappers used to be pinned to Central via exclusiveContent because mirrors
        // sometimes served its metadata without the matching klibs. That pinning is gone:
        // in the CNB build environment it forced every kotlin-wrappers request through
        // Maven Central, which rate-limits CNB's shared egress IP with HTTP 429.
        //
        // A mirror only misses a klib the first time it is asked for it — the proxy fetches
        // it upstream and caches it, so a retry succeeds. Letting the mirror serve these
        // modules and relying on retry is cheaper than a hard 429 every cold build.
        //
        // `mavenLocal` first, for the same reason as in pluginManagement: the docs site builds
        // against the ktjs-docusaurus SNAPSHOT klib of a working tree. Empty, it costs nothing.
        //
        // The Kotlin coordinates below must not come from `mavenLocal`: a locally published stdlib
        // is POM-only and carries no JS variant, and once Gradle finds the metadata there it does not
        // fall back — `:docs:compileKotlinJs` then reports "Missing stdlib class" on every file.
        exclusiveContent {
            forRepository { mavenCentral() }
            filter {
                includeGroup("org.jetbrains.kotlin")
                includeGroup("org.jetbrains.kotlin-wrappers")
            }
        }
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "ktjs-echarts"

// The demo is the GitHub Pages site, not a published module.
include(":demo")

// The documentation site: Kotlin configuration plus Markdown, assembled by the ktjs-docusaurus
// Gradle plugin into a static site that `pagesSite` publishes at the Pages root.
include(":docs")

// One Gradle project per published module.
include(":ktjs-echarts")
include(":ktjs-echarts-gl")
include(":kmp-echarts-json")
