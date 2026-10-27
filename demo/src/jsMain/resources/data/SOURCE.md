# Third-party datasets

Every file here is copied verbatim from the Apache ECharts examples site
(<https://github.com/apache/echarts-examples>), which is licensed under the Apache License 2.0 — see
[LICENSE](LICENSE) in this directory for the full text. The GeoJSON / SVG maps come from
`public/data/asset/geo/`, the rest from `public/data/asset/data/`.

They are shipped as static files instead of being transcribed into Kotlin sources: several of them
are hundreds of kilobytes, and keeping them as data makes the port of each example a diff of code
rather than a diff of numbers. They are served from `/data/<name>` and loaded with
`demo.ui.loadJson` / `demo.ui.loadText`.

| File | Upstream folder | Used by |
| --- | --- | --- |
| `disk.tree.json` | `data/` | `treemap-disk` |
| `life-expectancy.json` | `data/` | `graph-life-expectancy` |
| `life-expectancy-table.json` | `data/` | `dataset-encode1` |
| `flights.json` | `data/` | `lines-airline` |
| `nba-court.json` | `data/` | `custom-hexbin` |
| `kawhi-leonard-16-17-regular.json` | `data/` | `custom-hexbin` |
| `world.json` | `geo/` | `lines-airline`, `custom-wind` |
| `winds.json` | `data-gl/asset/data/` | `custom-wind` |
| `iceland.geo.json` | `geo/` | `geo-choropleth-scatter` |
| `ch.geo.json` | `geo/` | `geo-graph` |
| `Map_of_Iceland.svg` | `geo/` | `geo-svg-scatter-simple` |
| `MacOdrum-LV5-floorplan-web.svg` | `geo/` | `geo-svg-lines` |
| `flight-seats.svg` | `geo/` | `geo-seatmap-flight` |
| `Beef_cuts_France.svg` | `geo/` | `geo-beef-cuts` |
| `obama_budget_proposal_2012.json` | `data/` | `treemap-obama` |
| `wind-barb-hobart.json` | `data/` | (not ported yet, `custom-wind`) |
| `aqi-beijing.json` | `data/` | (not ported yet, `line-aqi`) |

Files added here must keep the upstream name so the example source stays traceable to
`public/data/asset/{data,geo}/<name>`.
