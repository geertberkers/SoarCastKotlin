package geert.berkers.soarcast.decompiled;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class OnSwipeListener extends GestureDetector.SimpleOnGestureListener
{
    public double getAngle(final float n, final float n2, final float n3, final float n4) {
        return ((Math.atan2(n2 - n4, n3 - n) + 3.141592653589793) * 180.0 / 3.141592653589793 + 180.0) % 360.0;
    }

    public Direction getDirection(final float n, final float n2, final float n3, final float n4) {
        return Direction.get(this.getAngle(n, n2, n3, n4));
    }

    public boolean onDown(final MotionEvent motionEvent) {
        return this.zetSchuifOpNul();
    }

    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        return this.onSwipe(this.getDirection(motionEvent.getX(), motionEvent.getY(), motionEvent2.getX(), motionEvent2.getY()));
    }

    public boolean onSchuif(final float n) {
        return false;
    }

    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, float x, final float n) {
        x = motionEvent.getX();
        return this.onSchuif(motionEvent2.getX() - x);
    }

    public boolean onSwipe(final Direction direction) {
        return false;
    }

    public boolean zetSchuifOpNul() {
        return false;
    }

    public enum Direction
    {
        down,
        left,
        right,
        up;

        public static Direction get(final double n) {
            if (inRange(n, 60.0f, 120.0f)) {
                return Direction.up;
            }
            if (inRange(n, 0.0f, 60.0f) || inRange(n, 300.0f, 360.0f)) {
                return Direction.right;
            }
            if (inRange(n, 240.0f, 300.0f)) {
                return Direction.down;
            }
            return Direction.left;
        }

        public static boolean inRange(final double n, final float n2, final float n3) {
            return n >= n2 && n < n3;
        }
    }
}