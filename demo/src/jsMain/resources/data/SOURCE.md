# Third-party datasets

The JSON files in this directory are copied verbatim from the Apache ECharts examples site
(<https://github.com/apache/echarts-examples>, `public/data/asset/data/`), which is licensed under
the Apache License 2.0 — see [LICENSE](LICENSE) in this directory.

They are shipped as static files instead of being transcribed into Kotlin sources: several of them
are hundreds of kilobytes, and keeping them as data makes the port of each example a diff of code
rather than a diff of numbers.

| File | Used by |
| --- | --- |
| `disk.tree.json` | `treemap-disk`, `treemap-borderColor` |
| `life-expectancy.json` | `graph-life-expectancy` |
| `obama_budget_proposal_2012.json` | `treemap-obama` |
| `nba-court.json` | `custom-hexbin` |
| `kawhi-leonard-16-17-regular.json` | `custom-hexbin` |
| `wind-barb-hobart.json` | `custom-wind` |
| `aqi-beijing.json` | `line-aqi` |

Files added here must keep the upstream name so the example source stays traceable to
`public/data/asset/data/<name>`.
