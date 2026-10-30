package io.github.shijianjs.kmpechartsjson

import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Layout direction of a legend, timeline, data zoom or tree. */
@Serializable
public enum class Orient {
    /** Horizontal layout. */
    @SerialName("horizontal")
    Horizontal,

    /** Vertical layout. */
    @SerialName("vertical")
    Vertical,
}

/**
 * Discriminator of a series.
 *
 * A value class instead of an enum so third-party series (for example the `echarts-gl` 3D types)
 * can be emitted as well. The constants cover every series type bundled with ECharts.
 */
@Serializable(with = SeriesTypeSerializer::class)
@JvmInline
public value class SeriesType(public val value: String) {
    public companion object {
        /** Line chart. */
        public val Line: SeriesType = SeriesType("line")

        /** Bar chart. */
        public val Bar: SeriesType = SeriesType("bar")

        /** Pie chart. */
        public val Pie: SeriesType = SeriesType("pie")

        /** Scatter chart. */
        public val Scatter: SeriesType = SeriesType("scatter")

        /** Scatter chart with ripple animation. */
        public val EffectScatter: SeriesType = SeriesType("effectScatter")

        /** Radar chart. */
        public val Radar: SeriesType = SeriesType("radar")

        /** Map chart. */
        public val Map: SeriesType = SeriesType("map")

        /** Treemap. */
        public val Treemap: SeriesType = SeriesType("treemap")

        /** Relation graph. */
        public val Graph: SeriesType = SeriesType("graph")

        /** Flight/route lines. */
        public val Lines: SeriesType = SeriesType("lines")

        /** Tree. */
        public val Tree: SeriesType = SeriesType("tree")

        /** Sunburst. */
        public val Sunburst: SeriesType = SeriesType("sunburst")

        /** Box plot. */
        public val Boxplot: SeriesType = SeriesType("boxplot")

        /** Candlestick. */
        public val Candlestick: SeriesType = SeriesType("candlestick")

        /** Heatmap. */
        public val Heatmap: SeriesType = SeriesType("heatmap")

        /** Custom series rendered by a callback. */
        public val Custom: SeriesType = SeriesType("custom")

        /** Sankey diagram. */
        public val Sankey: SeriesType = SeriesType("sankey")

        /** Funnel chart. */
        public val Funnel: SeriesType = SeriesType("funnel")

        /** Gauge. */
        public val Gauge: SeriesType = SeriesType("gauge")

        /** Pictorial bar chart. */
        public val PictorialBar: SeriesType = SeriesType("pictorialBar")

        /** Theme river. */
        public val ThemeRiver: SeriesType = SeriesType("themeRiver")

        /** Parallel coordinates. */
        public val Parallel: SeriesType = SeriesType("parallel")

        /** Chord diagram. */
        public val Chord: SeriesType = SeriesType("chord")
    }
}

/** Serializer for [SeriesType]; not needed directly in normal use. */
public object SeriesTypeSerializer : KSerializer<SeriesType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.shijianjs.kmpechartsjson.SeriesType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SeriesType) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): SeriesType =
        SeriesType(decoder.decodeString())
}
