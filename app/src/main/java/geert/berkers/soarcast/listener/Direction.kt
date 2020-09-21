package geert.berkers.soarcast.listener

/**
 * Created by Zorgkluis (Geert Berkers)
 */
enum class Direction {
    down, left, right, up;

    companion object {
        public operator fun get(n: Double): Direction {
            if (inRange(n, 60.0f, 120.0f)) {
                return up
            }
            if (inRange(n, 0.0f, 60.0f) || inRange(n, 300.0f, 360.0f)) {
                return right
            }
            return if (inRange(n, 240.0f, 300.0f)) {
                down
            } else left
        }

        public fun inRange(n: Double, n2: Float, n3: Float): Boolean {
            return n >= n2 && n < n3
        }
    }
}