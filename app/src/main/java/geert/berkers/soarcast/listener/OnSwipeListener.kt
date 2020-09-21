package geert.berkers.soarcast.listener

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent

/**
 * Created by Zorgkluis (Geert Berkers)
 */
open class OnSwipeListener : SimpleOnGestureListener() {

    open fun onSchuif(n: Float): Boolean = false

    open fun zetSchuifOpNul(): Boolean = false

    open fun onSwipe(direction: Direction?): Boolean = false

    override fun onDown(motionEvent: MotionEvent): Boolean = zetSchuifOpNul()

    fun getDirection(n: Float, n2: Float, n3: Float, n4: Float): Direction =
        Direction[getAngle(n, n2, n3, n4)]

    fun getAngle(n: Float, n2: Float, n3: Float, n4: Float): Double =
        ((Math.atan2(
            n2 - n4.toDouble(),
            n3 - n.toDouble()
        ) + 3.141592653589793) * 180.0 / 3.141592653589793 + 180.0) % 360.0

    override fun onScroll(
        motionEvent: MotionEvent,
        motionEvent2: MotionEvent,
        x: Float,
        n: Float
    ): Boolean =
        onSchuif(motionEvent2.x - motionEvent.x)

    override fun onFling(
        motionEvent: MotionEvent,
        motionEvent2: MotionEvent,
        x: Float,
        n: Float
    ): Boolean =
        onSwipe(getDirection(motionEvent.x, motionEvent.y, motionEvent2.x, motionEvent2.y))

}