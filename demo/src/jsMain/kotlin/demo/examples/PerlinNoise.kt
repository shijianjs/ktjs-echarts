package demo.examples

import kotlin.math.floor

/**
 * The perlin noise helper of the official `heatmap-large` and `graphic-wave-animation` examples.
 *
 * Upstream pastes a copy of https://github.com/josephg/noisejs into each example file; the port keeps
 * a single shared copy, so the heatmap and the graphic family animate the same field. Upstream seeds
 * it with `Math.random()`, which would change the canvas on every visit and make the end-to-end pixel
 * assertions meaningless, so [seed] takes a fixed value instead.
 */
internal class PerlinNoise {
    private val permutation = IntArray(512)
    private val gradX = DoubleArray(512)
    private val gradY = DoubleArray(512)

    init {
        seed(0.0)
    }

    /**
     * Rebuild the doubled permutation table from [value].
     *
     * Upstream scales a fraction out to the 16-bit range and spreads a small integer over both bytes,
     * which is what makes `seed(Math.random())` and `seed(3000)` reach different tables.
     */
    fun seed(value: Double) {
        var scaled = value
        if (scaled > 0 && scaled < 1) {
            scaled *= 65536.0
        }
        var current = floor(scaled).toInt()
        if (current < 256) {
            current = current or (current shl 8)
        }
        for (index in 0 until 256) {
            val source =
                if (index and 1 == 1) {
                    PERMUTATION[index] xor (current and 255)
                } else {
                    PERMUTATION[index] xor ((current shr 8) and 255)
                }
            permutation[index] = source
            permutation[index + 256] = source
            gradX[index] = GRAD_X[source % 12]
            gradY[index] = GRAD_Y[source % 12]
            gradX[index + 256] = GRAD_X[source % 12]
            gradY[index + 256] = GRAD_Y[source % 12]
        }
    }

    /** Noise value of `[x, y]`, in `[-1, 1]`. */
    fun perlin2(x: Double, y: Double): Double {
        val cellX = floor(x).toInt()
        val cellY = floor(y).toInt()
        val offsetX = x - cellX
        val offsetY = y - cellY
        // Wrap the integer cells at 255, as upstream does with `X & 255`.
        val wrappedX = cellX and 255
        val wrappedY = cellY and 255
        val n00 = dot(wrappedX + permutation[wrappedY], offsetX, offsetY)
        val n01 = dot(wrappedX + permutation[wrappedY + 1], offsetX, offsetY - 1)
        val n10 = dot(wrappedX + 1 + permutation[wrappedY], offsetX - 1, offsetY)
        val n11 = dot(wrappedX + 1 + permutation[wrappedY + 1], offsetX - 1, offsetY - 1)
        val curve = fade(offsetX)
        return lerp(lerp(n00, n10, curve), lerp(n01, n11, curve), fade(offsetY))
    }

    private fun dot(index: Int, x: Double, y: Double): Double = gradX[index] * x + gradY[index] * y

    private fun fade(t: Double): Double = t * t * t * (t * (t * 6 - 15) + 10)

    private fun lerp(a: Double, b: Double, t: Double): Double = (1 - t) * a + t * b

    private companion object {
        /** The permutation table of noisejs; the twelve gradients repeat every `256` entries. */
        val PERMUTATION =
            intArrayOf(
                151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53,
                194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99,
                37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75,
                0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32,
                57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136,
                171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
                77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230,
                220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54,
                65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187,
                208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86,
                164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
                124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212,
                207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
                223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70,
                221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253,
                19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104,
                218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12,
                191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
                49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176,
                115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93,
                222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66,
                215, 61, 156, 180,
            )

        /** The twelve gradient directions of noisejs, split into components for the lookup above. */
        val GRAD_X = doubleArrayOf(1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0)
        val GRAD_Y = doubleArrayOf(1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0, -1.0)
    }
}
