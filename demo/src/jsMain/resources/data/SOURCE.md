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
| `ec-option-doc-statistics-201604.json` | `data/` | `treemap-drill-down` |
| `echarts-package-size.json` | `data/` | `treemap-sunburst-transition` |
| `life-expectancy.json` | `data/` | `graph-life-expectancy`, `scatter-life-expectancy-timeline` |
| `life-expectancy-table.json` | `data/` | `dataset-encode1`, `data-transform-filter`, `line-race`, `bar3d-dataset`, `scatter3d-scatter`, `scatter3D-dataset`, `bar-race-country` |
| `emoji-flags.json` | npm `emoji-flags@1.3.0` | `bar-race-country` (the emoji of a country name) |
| `les-miserables.json` | `data/` | `graph`, `graph-circular-layout`, `graph-force`, `graph-label-overlap` |
| `npmdepgraph.min10.json` | `data/` | `graph-npm` |
| `webkit-dep.json` | `data/` | `graph-webkit-dep` |
| `flights.json` | `data/` | `lines-airline` |
| `gl/flights.json` | `data-gl/asset/data/` | `lines3d-airline-on-globe`, `lines3d-flights-on-geo3d` (17k routes, smaller than the 2D table) |
| `nba-court.json` | `data/` | `custom-hexbin` |
| `kawhi-leonard-16-17-regular.json` | `data/` | `custom-hexbin` |
| `stack-trace.json` | `data/` | `flame-graph` |
| `world.json` | `geo/` | `lines-airline`, `custom-wind`, `scatter-world-population` |
| `winds.json` | `data-gl/asset/data/` | `custom-wind` |
| `iceland.geo.json` | `geo/` | `geo-choropleth-scatter`, `map-iceland-pie` |
| `ch.geo.json` | `geo/` | `geo-graph`, `matrix-mini-bar-geo` |
| `HK.json` | `geo/` | `map-HK` |
| `USA.json` | `geo/` | `map-usa`, `map-bar-morph` |
| `Sicily_prehellenic_topographic_map.svg` | `geo/` | `geo-svg-map` |
| `Map_of_Iceland.svg` | `geo/` | `geo-svg-scatter-simple` |
| `MacOdrum-LV5-floorplan-web.svg` | `geo/` | `geo-svg-lines` |
| `flight-seats.svg` | `geo/` | `geo-seatmap-flight` |
| `Veins_Medical_Diagram_clip_art.svg` | `geo/` | `geo-organ` |
| `ksia-ext-plan-min.svg` | `geo/` | `geo-svg-traffic` |
| `Beef_cuts_France.svg` | `geo/` | `geo-beef-cuts` |
| `obama_budget_proposal_2012.json` | `data/` | `treemap-obama` |
| `obama_budget_proposal_2012.list.json` | `data/` | `doc-example/bar-dataZoom-filterMode` |
| `stock-DJI.json` | `data/` | `doc-example/candlestick-axisPointer` |
| `wind-barb-hobart.json` | `data/` | `wind-barb` |
| `aqi-beijing.json` | `data/` | `line-aqi` |
| `energy.json` | `data/` | `sankey-energy`, `sankey-nodeAlign-left`, `sankey-nodeAlign-right` |
| `product.json` | `data/` | `sankey-levels` |
| `confidence-band.json` | `data/` | `confidence-band` |
| `masterPainterColorChoice.json` | `data/` | `scatter-painter-choice` |
| `nutrients.json` | `data/` | `scatter3d`, `scatter-nutrients`, `scatter-nutrients-matrix`, `parallel-nutrients` |
| `rainfall-evaporation.json` | derived | `grid-multiple` |
| `rainfall-flow.json` | derived | `area-rainfall` |
| `img/custom-gauge-panel.png` | `img/` | `custom-gauge` (dial picture clipped into the scale arc and the pointer) |
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
| `img/gl/portrait.jpg` | derived (was a base64 data URL) | `image-to-bar3d` (one bar per pixel, inverted luminance) |
| `img/gl/bitcoin.png` | `data-gl/asset/` | `bar3d-voxelize-image` (one bar per pixel, HDR colors) |
| `img/gl/woods.jpg` | `data-gl/asset/` | `geo3d-with-different-height` (detail texture) |
| `img/gl/starfield.jpg` | `data-gl/asset/` | the globe pages (environment) |
| `img/gl/lake.hdr` | `data-gl/asset/` | `scatter3d-globe-population` (ambient cubemap) |
| `img/gl/wood/diffuse.jpg` | `data-gl/asset/wood/` | `map3d-wood-map` (detail texture of the map material) |
| `img/gl/wood/normal.jpg` | `data-gl/asset/wood/` | `map3d-wood-map` (normal map) |
| `img/gl/wood/roughness.png` | `data-gl/asset/wood/` | `map3d-wood-map` (roughness map) |
| `img/gl/redbricks/diffuse.jpg` | `data-gl/asset/redbricks/` | `map3d-wood-map` (detail texture of the ground plane) |
| `img/gl/redbricks/normal.jpg` | `data-gl/asset/redbricks/` | `map3d-wood-map` (normal map of the ground plane) |
| `img/gl/redbricks/roughness.jpg` | `data-gl/asset/redbricks/` | `map3d-wood-map` (roughness map of the ground plane) |
| `img/gl/earth.jpg` | `data-gl/asset/` | `globe-atmosphere`, `globe-layers` (base texture) |
| `img/gl/moon-base.jpg` | `data-gl/asset/` | `globe-moon` (base texture) |
| `img/gl/moon-bump.jpg` | `data-gl/asset/` | `globe-moon` (height texture) |
| `img/gl/night.jpg` | `data-gl/asset/` | `globe-layers` (emission layer) |
| `img/gl/clouds.png` | `data-gl/asset/` | `globe-layers` (overlay layer) |
| `img/gl/world.topo.bathy.200401.jpg` | `data-gl/asset/` | `globe-echarts-gl-hello-world` (base and height texture) |
| `img/gl/bathymetry_bw_composite_4k.jpg` | `data-gl/asset/` | `globe-displacement`, `globe-layers` (displacement / height texture) |
| `world-population.json` | `data-gl/asset/data/` | `geo3d-with-different-height` (region heights), `globe-with-echarts-surface` (map values) |
| `population.json` | `data-gl/asset/data/` | `scatter3d-globe-population`, `bar3d-global-population`, `global-population-bar3d-on-globe` (`[lng, lat, population]` cells) |
| `gl/internet.graph.json` | `data-gl/asset/data/` | `graphgl-large-internet` (23k nodes, `[source, target]` edge rows) |
| `gl/npmdep.json` | `data-gl/asset/data/` | `graphgl-npm-dep` (10k packages, flat endpoint list) |
| `gl/alcohol.json` | `data-gl/asset/data/` | `map3d-alcohol-consumption`, `map3d-wood-map` (`[country, litres]` rows) |
| `gl/buildings.json` | `data-gl/asset/data/` | `map3d-buildings`, `map3d-wood-city` (one extruded region per building) |
| `house-price-area2.json` | `data/` | `scatter-stream-visual` (`[area, price]` pairs) |
| `fake-nebula.bin` | `data/asset/data/` | `scatter-nebula` (8 MB of 32-bit floats, a flat `[x0, y0, x1, y1, ...]` table) |
| `ny/links_ny_{0..7}.bin` | `data/asset/data/links-ny/` | `lines-ny`, `linesGL-ny` (street polylines as `[offsetX, offsetY, count, x0, y0, ...]` rows of 32-bit floats; 8 of the 32 upstream chunks) |
| `gps/gps_{0..19}.bin` | `data/asset/data/gps/` | `scatterGL-gps` (`[lat, lng]` pairs as 32-bit integers scaled by 1e7; 20 of the 230 upstream chunks) |
| `calendar-lunar.json` | derived | `calendar-lunar` (the 365-row `dateList` of `examples/ts/calendar-lunar.ts`, each row `[date, lunarDay]` or `[date, lunarDay, solarTerm]`) |

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

The two chunk tables are **subsets**. Upstream streams 32 `links_ny_*.bin` files (23 MB, close to a
million polylines) and 230 `gps_*.bin` files (89 MB, 10 million points), which is more than a demo
site should carry, so the repository ships 8 and 20 chunks of 7.7 MB each: 922,916 street segments
and 1,000,000 GPS points. Neither split is a spatial tiling — the street chunks share one bounding
box and the GPS chunks are one random sample of the world — so the pages show the same geography as
upstream, only sparser, and `scatterGL-gps` titles itself after the points it actually draws.

`emoji-flags.json` is the one file that is **not** from the Apache examples site: `bar-race-country`
loads the flags from the `emoji-flags@1.3.0` npm package (MIT,
<https://github.com/matiassingers/emoji-flags>) through its CDN, so the `data.json` of that package is
vendored here instead. The port reads only `name` and `emoji`; all 251 records are kept, because the
country column of the life-expectancy table is matched by name and every one of its 19 countries has
a flag.

Files added here must keep the upstream name so the example source stays traceable to
`public/data/asset/{data,geo}/<name>`.
