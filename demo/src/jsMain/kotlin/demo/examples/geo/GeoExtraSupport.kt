package demo.examples.geo

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.matrix.names
import demo.examples.scatter.point

/** The seven pre-sold seats of the flight seatmap, coloured red and non-interactive. */
internal fun takenRegions(): Array<RegionOption> {
    val names = arrayOf("26E", "26D", "26C", "25D", "23C", "21A", "20F")
    return Array(names.size) { index ->
        unsafeJso<RegionOption> {
            name = names[index]
            silent = true
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#bf0e08") }
        }
    }
}

/** One animated route of the SVG traffic example; `coords` is `[x, y]` along the airport plan. */
internal val TRAFFIC_A =
    arrayOf(
        arrayOf(50.875133928571415, 242.66287667410717),
        arrayOf(62.03696428571425, 264.482421875),
        arrayOf(72.63357421874997, 273.62779017857144),
        arrayOf(92.78291852678569, 285.869140625),
        arrayOf(113.43637834821425, 287.21854073660717),
        arrayOf(141.44788783482142, 288.92947823660717),
        arrayOf(191.71686104910714, 289.5507114955357),
        arrayOf(198.3060072544643, 294.0673828125),
        arrayOf(204.99699497767858, 304.60288783482144),
        arrayOf(210.79177734375003, 316.7373046875),
        arrayOf(212.45179408482142, 329.3656529017857),
        arrayOf(210.8885267857143, 443.3925083705358),
        arrayOf(215.35936941964286, 453.00634765625),
        arrayOf(224.38761997767858, 452.15087890625),
        arrayOf(265.71490792410714, 452.20179966517856),
        arrayOf(493.3408844866072, 453.77525111607144),
        arrayOf(572.8892940848216, 448.77992466517856),
        arrayOf(608.9513755580358, 448.43366350446433),
        arrayOf(619.99099609375, 450.8778599330358),
        arrayOf(624.2479715401787, 456.2194475446429),
        arrayOf(628.1434095982145, 463.9899553571429),
        arrayOf(629.8492550223216, 476.0276227678571),
        arrayOf(631.2750362723216, 535.7322126116071),
        arrayOf(624.6757059151787, 546.6496233258929),
        arrayOf(617.1801702008929, 552.6480887276786),
        arrayOf(603.7269056919645, 554.5066964285714),
        arrayOf(588.0178515625, 557.5517578125),
        arrayOf(529.4386104910716, 556.2991071428571),
        arrayOf(422.1994921875001, 551.38525390625),
        arrayOf(291.66921875, 552.5767996651786),
        arrayOf(219.4279380580357, 547.4949079241071),
        arrayOf(209.53912667410714, 541.5931919642858),
        arrayOf(206.70793247767858, 526.1947544642858),
        arrayOf(206.70793247767858, 507.4049944196429),
        arrayOf(206.12234375000003, 468.7663225446429),
        arrayOf(204.48778738839286, 459.44782366071433),
        arrayOf(197.56256417410714, 452.8943219866071),
        arrayOf(170.31995814732142, 456.27546037946433),
        arrayOf(1.8078906249999704, 460.5935407366071),
    )

internal val TRAFFIC_B =
    arrayOf(
        arrayOf(779.4595368303574, 287.98744419642856),
        arrayOf(689.07009765625, 291.0477818080357),
        arrayOf(301.83300223214286, 290.49783761160717),
        arrayOf(229.31165736607142, 291.73011997767856),
        arrayOf(220.73660156250003, 297.4077845982143),
        arrayOf(214.74832031250003, 308.52378627232144),
        arrayOf(213.82156250000003, 421.35400390625),
        arrayOf(213.19523716517858, 443.0564313616071),
        arrayOf(222.31005301339286, 455.95465959821433),
        arrayOf(271.71846540178575, 454.37611607142856),
        arrayOf(359.64843191964286, 455.9393833705358),
        arrayOf(580.2524358258929, 448.11286272321433),
        arrayOf(627.7156752232145, 460.7463030133929),
        arrayOf(632.3290959821429, 536.6386021205358),
        arrayOf(628.9123130580358, 548.4776785714286),
        arrayOf(612.5667494419645, 556.8235909598214),
        arrayOf(543.7167912946429, 555.4741908482143),
        arrayOf(429.1756361607143, 551.9402901785714),
        arrayOf(293.42089285714286, 551.2172154017858),
        arrayOf(226.20039899553575, 556.0699637276786),
        arrayOf(215.49176339285714, 562.7253069196429),
        arrayOf(213.21051339285714, 591.6024693080358),
        arrayOf(212.00878348214286, 625.6735491071429),
        arrayOf(197.43017020089286, 645.0743582589286),
        arrayOf(187.41405691964286, 647.0857282366071),
        arrayOf(101.79589285714286, 649.0207170758929),
        arrayOf(69.96023437499997, 650.1613420758929),
        arrayOf(56.48150948660714, 656.8268694196429),
        arrayOf(51.11446149553569, 665.2542550223214),
    )

internal val TRAFFIC_C =
    arrayOf(
        arrayOf(2.5920703124999704, 450.66908482142856),
        arrayOf(204.0651450892857, 453.13364955357144),
        arrayOf(378.72844029017864, 453.13874162946433),
        arrayOf(551.1817745535716, 456.1532505580358),
        arrayOf(578.3734598214287, 456.91196986607144),
        arrayOf(601.2317885044645, 458.9895368303571),
        arrayOf(614.1503850446429, 462.1669921875),
        arrayOf(618.99294921875, 479.68882533482144),
        arrayOf(620.0826534598216, 513.5969587053571),
        arrayOf(615.6932840401787, 528.7306082589286),
        arrayOf(608.4829045758929, 533.2625558035714),
        arrayOf(592.7127455357145, 534.9582170758929),
        arrayOf(583.09890625, 527.5492466517858),
        arrayOf(578.6535239955358, 516.4077845982143),
        arrayOf(578.6535239955358, 498.36146763392856),
        arrayOf(577.9966462053571, 477.0613141741071),
        arrayOf(575.3691350446429, 469.1940569196429),
        arrayOf(569.0753292410716, 462.63037109375),
        arrayOf(553.9518638392858, 460.6444614955358),
        arrayOf(298.10051060267864, 465.61432756696433),
        arrayOf(193.49908761160714, 460.1759905133929),
        arrayOf(116.40505859374997, 465.78236607142856),
        arrayOf(3.5137360491071092, 463.47565569196433),
    )

/** Builds one `lines` data item: a coloured, constant-speed effect over the given polyline. */
internal fun trafficRoute(effectColor: String, speed: Double, coords: Array<Array<Double>>): LinesDataItemOption =
    unsafeJso {
        effect = unsafeJso<LinesEffectOption> { color = ZRColor(effectColor); constantSpeed = speed }
        this.coords =
            coords.map { point -> arrayOf(point[0], point[1]) }.toTypedArray()
    }

/** The eight population centres of the example: `[lng, lat, population]`. */
internal fun icelandScatter(): Array<ScatterDataItem?> =
    dataItems(
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-21.9348415), OptionDataValue(64.1334671), OptionDataValue(14523.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-19.028531), OptionDataValue(63.710241), OptionDataValue(45126.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-17.089925), OptionDataValue(65.37887072), OptionDataValue(12345.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-19.15936), OptionDataValue(65.6218101), OptionDataValue(56789.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-19.849175), OptionDataValue(65.7287035), OptionDataValue(67890.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-23.18326), OptionDataValue(65.582939), OptionDataValue(89012.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-14.9515), OptionDataValue(64.475135), OptionDataValue(34567.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-20.88389), OptionDataValue(63.85321), OptionDataValue(45678.0))),
    )

/** One region of the choropleth. */
internal fun region(name: String, value: Double): MapDataItem = MapDataItem(
    unsafeJso<MapDataItemOption> {
    this.name = OptionId(name)
    this.value = OptionDataValue(value)
    },
)

internal fun beefCut(name: String, value: Double): MapDataItem =
    MapDataItem(
        unsafeJso<MapDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
        },
    )

internal fun routeNode(name: String, lng: Double, lat: Double): GraphDataItem =
    GraphDataItem(
        unsafeJso<GraphNodeItemOption> {
            this.name = name
            this.value = OptionDataValueOrArray(arrayOf(OptionDataValue(lng), OptionDataValue(lat)))
        },
    )

internal fun routeEdge(source: String, target: String): GraphEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
    }
