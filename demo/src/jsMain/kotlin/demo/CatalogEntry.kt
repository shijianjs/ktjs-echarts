package demo

class CatalogEntry(
    val id: String,
    val group: Group,
    val titleZh: String,
    val titleEn: String,
    val summaryZh: String,
    val summaryEn: String,
    val body: ExampleBody,
    val sourcePath: String,
    val officialUrl: String? = officialExampleUrl(id),
    /** GitHub blob base of [sourcePath]; the KMP model lives in the `commonMain` tree. */
    val sourceRoot: String = Catalog.REPO_SOURCE_ROOT,
)
