package demo

import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.SiteTexts
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts

/**
 * The layout's strings, one object per language.
 *
 * The group and page titles are derived from [catalog] rather than listed again: a page's names live in
 * its entry, and the drawer, the breadcrumbs and the index all read them from there. Adding an example
 * therefore never needs a second translation table.
 */
private val englishSite: SiteTexts = SiteTexts(
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
    groupTitles = catalogGroups.associate { it.id to it.titleEn },
    pageTitles = catalog.associate { it.id to it.titleEn },
)

private val chineseSite: SiteTexts = SiteTexts(
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
    groupTitles = catalogGroups.associate { it.id to it.titleZh },
    pageTitles = catalog.associate { it.id to it.titleZh },
)

/** The layout's strings, one entry per registered language. */
val siteTexts: Translations<SiteTexts> = texts(
    Langs.EN to englishSite,
    Langs.ZH_HANS to chineseSite,
)
