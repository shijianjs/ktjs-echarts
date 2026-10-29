plugins {
    kotlin("multiplatform")
}

kotlin {
    js {
        useEsModules()

        // The demo is a standalone page, so the JS target needs an executable webpack bundle.
        binaries.executable()
        browser()
    }

    // The JVM target carries only the Playwright end-to-end suite in `src/jvmTest`; the demo itself
    // stays a JS page.
    jvm()

    sourceSets {
        jsMain.dependencies {
            implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:2026.10.4")
            // Wrapper modules used by the demo.
            implementation(project(":ktjs-echarts"))
            implementation(project(":ktjs-echarts-gl"))
        }

        jvmTest.dependencies {
            implementation("com.microsoft.playwright:playwright:1.61.0")
            implementation("cn.hutool:hutool-all:5.8.48")
            implementation("org.junit.jupiter:junit-jupiter:6.1.3")
            implementation("org.junit-pioneer:junit-pioneer:2.3.0")
            implementation(kotlin("test-junit5"))
            runtimeOnly("org.junit.platform:junit-platform-launcher:6.1.3")
        }
    }
}

// ---------------------------------------------------------------------------------------------
// End-to-end suite
//
// The suite lives in `src/jvmTest`, so the demo stays one KMP module instead of carrying a second
// Gradle project. It uses Playwright's official JUnit 5 integration plus Hutool's SimpleServer for
// the static bundle, and reuses the Chromium that `kotlinInstallPlaywrightBrowsers` installs for
// the Kotlin/JS browser tests (hence the pinned Playwright version in the dependencies).
// `jvmTest` itself is disabled to keep `build`/`check` browser-free; this dedicated task runs the
// same test compilation on demand.
// ---------------------------------------------------------------------------------------------
val jvmE2eCompilation = kotlin.targets.getByName("jvm").compilations.getByName("test")

tasks.register<Test>("e2eTest") {
    group = "verification"
    description = "Runs the Kotlin/JVM Playwright suite against the bundled demo page."
    useJUnitPlatform()
    dependsOn("jsBrowserDistribution", ":ktjs-echarts:kotlinInstallPlaywrightBrowsers")

    testClassesDirs = jvmE2eCompilation.output.classesDirs
    classpath = files(jvmE2eCompilation.output.allOutputs, jvmE2eCompilation.runtimeDependencyFiles)
    systemProperty("demo.dist", layout.buildDirectory.dir("dist/js/productionExecutable").get().asFile.absolutePath)
    systemProperty("e2e.output", layout.buildDirectory.dir("e2e-results").get().asFile.absolutePath)
    // The suite walks the gallery, switches language and theme and mounts charts, so a test needs
    // more than the default budget.
    systemProperty("junit.jupiter.execution.timeout.default", "90 s")

    outputs.upToDateWhen { false }
}

tasks.named<Test>("jvmTest") { enabled = false }
