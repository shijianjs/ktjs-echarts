package demo.examples.gl

import demo.CatalogEntry

/**
 * 3D charts examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object GlEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            Bar3dPunchCard.ENTRY, TransparentBar3d.ENTRY, Bar3dDataset.ENTRY, Line3dOrthographic.ENTRY,
            SimpleSurface.ENTRY, SphereParametricSurface.ENTRY, SurfaceWave.ENTRY,
            ImageSurfaceSushuang.ENTRY, Bar3dMyth.ENTRY, GraphglGpuLayout.ENTRY,
            GraphglLargeInternet.ENTRY, GraphglNpmDep.ENTRY, Bar3dGlobalPopulation.ENTRY,
            GlobalPopulationBar3dOnGlobe.ENTRY, Geo3d.ENTRY, Geo3dWithDifferentHeight.ENTRY,
            GlobeEchartsGlHelloWorld.ENTRY, GlobeAtmosphere.ENTRY, GlobeDisplacement.ENTRY,
            GlobeMoon.ENTRY, GlobeLayers.ENTRY, Scatter3dGlobePopulation.ENTRY,
            GlobeWithEchartsSurface.ENTRY, Map3dAlcoholConsumption.ENTRY, Map3dBuildings.ENTRY,
            Map3dWoodCity.ENTRY, Map3dWoodMap.ENTRY, ImageToBar3d.ENTRY, Bar3dVoxelizeImage.ENTRY,
            Lines3dAirlineOnGlobe.ENTRY, Lines3dFlightsOnGeo3d.ENTRY, Lines3dFlights.ENTRY,
            Lines3dFlightsGl.ENTRY, Scatter3d.ENTRY, Scatter3dScatter.ENTRY, Scatter3DDataset.ENTRY,
            LinesGLNy.ENTRY, ScatterGLGps.ENTRY, MetalSurface.ENTRY, SurfaceBreather.ENTRY,
            SurfaceMolluscShell.ENTRY, SurfaceGoldenRose.ENTRY, SurfaceThemeRoses.ENTRY,
            ParametricSurfaceRose.ENTRY, SurfaceLeather.ENTRY, ScatterglWeibo.ENTRY,
        )
    }
}
