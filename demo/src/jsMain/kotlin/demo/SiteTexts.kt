package demo

import demo.Catalog
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.SiteTexts
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts

/**
 * The layout's strings, one object per language.
 *
 * The group and page titles are derived from [Catalog.ENTRIES] rather than listed again: a page's name
 * lives in its entry, and the drawer, the breadcrumbs and the index all read it from there. Adding an
 * example therefore never needs a second translation table.
 *
 * Both language objects are `by lazy`, and the table that holds them is too: this file sits in `demo`,
 * where nothing is ready before the catalog is. Building the strings here would read all 384 entries at
 * import time; building them on first use reads them when the shell asks for a language.
 */
private val englishSite: SiteTexts by lazy {
    SiteTexts(
        siteTitle = "ktjs-echarts",
        langLabel = "English",
        languageLabel = "Language",
        themeLabel = "Theme",
        docsLabel = "Docs",
        siteDocsLabel = "Documentation",
        apiLabel = "API reference",
        sourceLabel = "GitHub",
        lightLabel = "Light",
        darkLabel = "Dark",
        systemLabel = "Follow browser",
        groupTitles = Catalog.GROUPS.associate { it.id to it.titleEn },
        pageTitles = Catalog.ENTRIES.associate { it.id to it.titleEn },
    )
}

private val chineseSite: SiteTexts by lazy {
    SiteTexts(
        siteTitle = "ktjs-echarts",
        langLabel = "中文",
        languageLabel = "语言",
        themeLabel = "外观",
        docsLabel = "文档",
        siteDocsLabel = "使用文档",
        apiLabel = "API 文档",
        sourceLabel = "GitHub 仓库",
        lightLabel = "亮色",
        darkLabel = "暗色",
        systemLabel = "跟随浏览器",
        groupTitles = Catalog.GROUPS.associate { it.id to it.titleZh },
        pageTitles = Catalog.ENTRIES.associate { it.id to it.titleZh },
    )
}

/** The layout's strings, one entry per registered language. */
val siteTexts: Translations<SiteTexts> by lazy {
    texts(
        Langs.EN to englishSite,
        Langs.ZH_HANS to chineseSite,
    )
}
