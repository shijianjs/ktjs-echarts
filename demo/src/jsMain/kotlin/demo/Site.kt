package demo

/**
 * The site's own identity: the version label the navbar shows and the three URLs the layout links to.
 *
 * These are pure configuration — they read nothing else and nothing else reads them at file init — but
 * they belong together: the layout hands all four to the shell at mount time, so one named home keeps
 * the site's identity out of the menu's own file.
 */
internal object Site {
    /** The Kotlin toolchain this bundle is compiled with; shown next to the title. */
    const val VERSION: String = "Kotlin/JS 2.4.21"

    /** The documentation site. */
    const val DOCS_URL: String = "https://shijianjs.github.io/ktjs-echarts/"

    /** Dokka reference assembled by `pagesSite` next to the demo. */
    const val API_URL: String = "https://shijianjs.github.io/ktjs-echarts/api/"

    /** Source repository, linked from the navbar's link menu. */
    const val SOURCE_URL: String = "https://github.com/shijianjs/ktjs-echarts"
}
