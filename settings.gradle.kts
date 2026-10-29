pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
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
        mavenCentral()
    }
}

rootProject.name = "ktjs-echarts"

// The demo is the GitHub Pages site, not a published module.
include(":demo")

// One Gradle project per published wrapper module.
include(":ktjs-echarts")
include(":ktjs-echarts-gl")
