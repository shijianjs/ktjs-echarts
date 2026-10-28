# Third-party datasets

Every file here is copied verbatim from the Apache ECharts examples site
(<https://github.com/apache/echarts-examples>), which is licensed under the Apache License 2.0 — see
[LICENSE](LICENSE) in this directory for the full text. The GeoJSON / SVG maps come from
`public/data/asset/geo/`, the small weather icons from `public/data/asset/img/weather/`, the rest from
`public/data/asset/data/`.

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
| `world.json` | `geo/` | `lines-airline`, `custom-wind`, `scatter-world-population` |
| `winds.json` | `data-gl/asset/data/` | `custom-wind` |
| `iceland.geo.json` | `geo/` | `geo-choropleth-scatter` |
| `ch.geo.json` | `geo/` | `geo-graph` |
| `Map_of_Iceland.svg` | `geo/` | `geo-svg-scatter-simple` |
| `MacOdrum-LV5-floorplan-web.svg` | `geo/` | `geo-svg-lines` |
| `flight-seats.svg` | `geo/` | `geo-seatmap-flight` |
| `Veins_Medical_Diagram_clip_art.svg` | `geo/` | `geo-organ` |
| `ksia-ext-plan-min.svg` | `geo/` | `geo-svg-traffic` |
| `Beef_cuts_France.svg` | `geo/` | `geo-beef-cuts` |
| `obama_budget_proposal_2012.json` | `data/` | `treemap-obama` |
| `obama_budget_proposal_2012.list.json` | `data/` | `doc-example/bar-dataZoom-filterMode` |
| `stock-DJI.json` | `data/` | `doc-example/candlestick-axisPointer` |
| `wind-barb-hobart.json` | `data/` | (not ported yet, `custom-wind`) |
| `aqi-beijing.json` | `data/` | `line-aqi` |
| `energy.json` | `data/` | `sankey-energy`, `sankey-nodeAlign-left`, `sankey-nodeAlign-right` |
| `product.json` | `data/` | `sankey-levels` |
| `confidence-band.json` | `data/` | `confidence-band` |
| `masterPainterColorChoice.json` | `data/` | `scatter-painter-choice` |
| `rainfall-evaporation.json` | derived | `grid-multiple` |
| `rainfall-flow.json` | derived | `area-rainfall` |
| `img/weather/sunny_128.png` | `img/weather/` | `bar-rich-text`, `pie-rich-text` (rich-text label icons) |
| `img/weather/cloudy_128.png` | `img/weather/` | see `sunny_128.png` |
| `img/weather/showers_128.png` | `img/weather/` | see `sunny_128.png` |

The two `rainfall-*.json` files are **derived**, not copied: upstream keeps these 3079-point series
inline in `examples/ts/grid-multiple.ts` and `examples/ts/area-rainfall.ts`, so a one-off script split
the arrays into JSON (`time` plus the two series) to keep the Kotlin port a diff of code rather than
numbers.

Files added here must keep the upstream name so the example source stays traceable to
`public/data/asset/{data,geo}/<name>`.
