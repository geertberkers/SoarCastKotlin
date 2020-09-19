package geert.berkers.soarcast.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
public class RichtingMetingView extends View {
    private Integer aantal;

    private int centrum = 0;

    private int kustdeg;

    private Paint mMeting;

    private final Integer maxN = Integer.valueOf(288);

    private int maxdeg;

    private int mindeg;

    private Double[] richtingMeting = new Double[this.maxN.intValue()];

    private Integer[] tijd = new Integer[this.maxN.intValue()];

    public RichtingMetingView(Context paramContext) {
        super(paramContext);
        init();
    }

    public RichtingMetingView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public RichtingMetingView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    protected void init() {
        this.mMeting = new Paint(1);
        this.mMeting.setColor(-7829368);
        this.mMeting.setStyle(Paint.Style.STROKE);
        this.mMeting.setTextSize(30.0F);
        this.aantal = Integer.valueOf(0);
    }

    protected void onDraw(Canvas paramCanvas) {
        float f2 = getMeasuredWidth();
        float f3 = getMeasuredHeight();
        float f1 = f2 / 700.0F;
        float f4 = 40.0F * f1;
        float f5 = 15.0F * f1;
        float f6 = f3 - 30.0F * f1;
        float f7 = f2 - 8.0F * f1 - f4;
        float f8 = f6 - f5;
        this.mMeting.setColor(-14671617);
        Paint paint = this.mMeting;
        int i = 1073741824;
        paint.setStrokeWidth(f1 * 2.0F);
        int j;
        for (j = 1;; j++) {
            if (j < this.aantal.intValue()) {
                Integer[] arrayOfInteger = this.tijd;
                int k = j - 1;
                float f9 = arrayOfInteger[k].intValue() * f7 / 24.0F / 3600.0F;
                float f10 = this.tijd[j].intValue() * f7 / 24.0F / 3600.0F;
                if (f9 < f10) {
                    f2 = this.richtingMeting[k].floatValue() - ((360 - this.centrum * 90) % 360);
                    f1 = f2;
                    if (f2 > 180.0F)
                        f1 = f2 - 360.0F;
                    f2 = f1;
                    if (f1 < -180.0F)
                        f2 = f1 + 360.0F;
                    f3 = this.richtingMeting[j].floatValue() - ((360 - 90 * this.centrum) % 360);
                    f1 = f3;
                    if (f3 > 180.0F)
                        f1 = f3 - 360.0F;
                    f3 = f1;
                    if (f1 < -180.0F)
                        f3 = f1 + 360.0F;
                    if (Math.abs(f2 - f3) < 180.0F) {
                        f1 = f8 / 2.0F + f5;
                        float f = f8 / 360.0F;
                        paramCanvas.drawLine(f4 + f9, f1 - f2 * f, f4 + f10, f1 - f * f3, this.mMeting);
                    } else if (f2 > 0.0F) {
                        f1 = f5 + f8 / 2.0F;
                        float f11 = f8 / 360.0F;
                        float f12 = f4 + (f9 + f10) / 2.0F;
                        paramCanvas.drawLine(f4 + f9, f1 - f2 * f11, f12, f5, this.mMeting);
                        paramCanvas.drawLine(f12, f6, f4 + f10, f1 - f11 * f3, this.mMeting);
                    } else {
                        i = 1073741824;
                        f1 = f5 + f8 / 2.0F;
                        float f11 = f8 / 360.0F;
                        float f12 = f4 + (f9 + f10) / 2.0F;
                        paramCanvas.drawLine(f4 + f9, f1 - f2 * f11, f12, f6, this.mMeting);
                        paramCanvas.drawLine(f12, f5, f4 + f10, f1 - f11 * f3, this.mMeting);
                        j++;
                    }
                    i = 1073741824;
                }
            } else {
                break;
            }
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        int i = View.MeasureSpec.getMode(paramInt2);
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        byte b = 100;
        if (i != 1073741824)
            if (i == Integer.MIN_VALUE) {
                paramInt2 = Math.min(100, paramInt2);
            } else {
                paramInt2 = 100;
            }
        int j = View.MeasureSpec.getMode(paramInt1);
        i = View.MeasureSpec.getSize(paramInt1);
        if (j == 1073741824) {
            paramInt1 = i;
        } else {
            paramInt1 = b;
            if (j == Integer.MIN_VALUE)
                paramInt1 = Math.min(100, i);
        }
        setMeasuredDimension(paramInt1, paramInt2);
    }

    public void update(int paramInt1, int paramInt2, int paramInt3, Integer[] paramArrayOfInteger, Double[] paramArrayOfDouble) {
        if (paramInt1 == paramInt2 || paramInt1 < 0 || paramInt1 > 360 || paramInt2 < 0 || paramInt2 > 360) {
            this.kustdeg = -1;
            this.centrum = 0;
        } else {
            this.mindeg = paramInt1;
            this.maxdeg = paramInt2;
            int i = paramInt2;
            if (paramInt1 > paramInt2)
                i = paramInt2 + 360;
            this.kustdeg = (paramInt1 + i) / 2;
            this.kustdeg %= 360;
            this.centrum = (4 - (this.kustdeg + 45) % 360 / 90) % 4;
        }
        this.aantal = Integer.valueOf(paramInt3);
        this.tijd = paramArrayOfInteger;
        this.richtingMeting = paramArrayOfDouble;
        invalidate();
    }
}
