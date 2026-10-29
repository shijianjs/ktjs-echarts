# Third-party datasets

Every file here is copied verbatim from the Apache ECharts examples site
(<https://github.com/apache/echarts-examples>), which is licensed under the Apache License 2.0 — see
[LICENSE](LICENSE) in this directory for the full text. The GeoJSON / SVG maps come from
`public/data/asset/geo/`, the small weather icons from `public/data/asset/img/weather/`, the GL
textures from `public/data-gl/asset/`, the rest from `public/data/asset/data/`.

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
| `gl/flights.json` | `data-gl/asset/data/` | `lines3d-airline-on-globe`, `lines3d-flights-on-geo3d` (17k routes, smaller than the 2D table) |
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
| `nutrients.json` | `data/` | `scatter3d` |
| `rainfall-evaporation.json` | derived | `grid-multiple` |
| `rainfall-flow.json` | derived | `area-rainfall` |
| `img/weather/sunny_128.png` | `img/weather/` | `bar-rich-text`, `pie-rich-text` (rich-text label icons) |
| `img/weather/cloudy_128.png` | `img/weather/` | see `sunny_128.png` |
| `img/weather/showers_128.png` | `img/weather/` | see `sunny_128.png` |
| `img/gl/canyon.hdr` | `data-gl/asset/` | `metal-surface`, `surface-breather`, `surface-mollusc-shell`, `parametric-surface-rose` (ambient cubemap) |
| `img/gl/pisa.hdr` | `data-gl/asset/` | `surface-golden-rose`, `surface-theme-roses`, `surface-leather` (ambient cubemap) |
| `img/gl/leather/leather_height.jpg` | `data-gl/asset/leather/` | `surface-leather` (height map sampled into the surface) |
| `img/gl/leather/leather_roughness.jpg` | `data-gl/asset/leather/` | `surface-leather` (roughness map) |
| `img/gl/leather/leather_normal.jpg` | `data-gl/asset/leather/` | `surface-leather` (normal map) |
| `img/gl/sushuang.jpg` | derived | `image-surface-sushuang` (per-pixel surface) |
| `img/gl/sample.jpg` | `data-gl/asset/` | `bar3d-myth` (one bar per pixel, colored from the image) |
| `img/gl/woods.jpg` | `data-gl/asset/` | `geo3d-with-different-height` (detail texture) |
| `img/gl/starfield.jpg` | `data-gl/asset/` | the globe pages (environment) |
| `img/gl/lake.hdr` | `data-gl/asset/` | `scatter3d-globe-population` (ambient cubemap) |
| `img/gl/earth.jpg` | `data-gl/asset/` | `globe-atmosphere`, `globe-layers` (base texture) |
| `img/gl/moon-base.jpg` | `data-gl/asset/` | `globe-moon` (base texture) |
| `img/gl/moon-bump.jpg` | `data-gl/asset/` | `globe-moon` (height texture) |
| `img/gl/night.jpg` | `data-gl/asset/` | `globe-layers` (emission layer) |
| `img/gl/clouds.png` | `data-gl/asset/` | `globe-layers` (overlay layer) |
| `img/gl/world.topo.bathy.200401.jpg` | `data-gl/asset/` | `globe-echarts-gl-hello-world` (base and height texture) |
| `img/gl/bathymetry_bw_composite_4k.jpg` | `data-gl/asset/` | `globe-displacement`, `globe-layers` (displacement / height texture) |
| `world-population.json` | `data-gl/asset/data/` | `geo3d-with-different-height` (region heights), `globe-with-echarts-surface` (map values) |
| `population.json` | `data-gl/asset/data/` | `scatter3d-globe-population`, `bar3d-global-population`, `global-population-bar3d-on-globe` (`[lng, lat, population]` cells) |

The two `rainfall-*.json` files are **derived**, not copied: upstream keeps these 3079-point series
inline in `examples/ts/grid-multiple.ts` and `examples/ts/area-rainfall.ts`, so a one-off script split
the arrays into JSON (`time` plus the two series) to keep the Kotlin port a diff of code rather than
numbers.

`globe-with-echarts-surface` keeps its 177 country values inline upstream; the port reads
`world-population.json` instead. The two lists agree except for one name: upstream writes
`United States of America`, the file writes `United States`, which is the spelling of
`geometry/world.json` — the name this site registers as the `world` map. That map also abbreviates
other region names, so `GlGeo.kt` translates the names of the data back to the names of the map.

`img/gl/sushuang.jpg` is **derived** as well: upstream keeps the picture inline in
`examples/ts/gl/image-surface-sushuang.js` as a base64 data URL, so it was decoded into a file and is
served from `/data/img/gl/sushuang.jpg`.

Files added here must keep the upstream name so the example source stays traceable to
`public/data/asset/{data,geo}/<name>`.
