package geert.berkers.soarcast.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorgkluis (Geert Berkers)
 */

public class RichtingModelView extends View {
    private Integer aantal;

    private int centrum = 0;

    private int kustdeg;

    private Paint mModel;

    private final Integer maxN = 288;

    private int maxdeg;

    private int mindeg;

    private Double[] richtingModel = new Double[this.maxN];

    private Integer[] tijd = new Integer[this.maxN];

    public RichtingModelView(Context paramContext) {
        super(paramContext);
        init();
    }

    public RichtingModelView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public RichtingModelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    protected void init() {
        this.mModel = new Paint(1);
        this.mModel.setColor(-7829368);
        this.mModel.setStyle(Paint.Style.STROKE);
        this.mModel.setTextSize(30.0F);
        this.aantal = 0;
    }

    protected void onDraw(Canvas paramCanvas) {
        float measuredWidth = getMeasuredWidth();
        float measuredHeight = getMeasuredHeight();
        float f2 = measuredWidth / 700.0F;
        float f4 = 40.0F * f2;
        float f5 = 15.0F * f2;
        float f6 = measuredHeight - 30.0F * f2;
        float f7 = measuredWidth - 8.0F * f2 - f4;
        float f8 = f6 - f5;
        this.mModel.setColor(805306623);
        this.mModel.setStrokeWidth(6.0F);
        int i;
        for (i = 1; i < this.aantal; i++) {
            Integer[] arrayOfInteger = this.tijd;
            int j = i - 1;
            float f9 = arrayOfInteger[j] * f7 / 24.0F / 3600.0F;
            float f10 = this.tijd[i] * f7 / 24.0F / 3600.0F;
            if (f9 < f10) {
                f2 = this.richtingModel[j].floatValue() - ((360 - this.centrum * 90) % 360);
                measuredWidth = f2;
                if (f2 > 180.0F)
                    measuredWidth = f2 - 360.0F;
                f2 = measuredWidth;
                if (measuredWidth < -180.0F)
                    f2 = measuredWidth + 360.0F;
                measuredHeight = this.richtingModel[i].floatValue() - ((360 - 90 * this.centrum) % 360);
                measuredWidth = measuredHeight;
                if (measuredHeight > 180.0F)
                    measuredWidth = measuredHeight - 360.0F;
                measuredHeight = measuredWidth;
                if (measuredWidth < -180.0F)
                    measuredHeight = measuredWidth + 360.0F;
                if (Math.abs(f2 - measuredHeight) < 180.0F) {
                    measuredWidth = f8 / 2.0F + f5;
                    float f = f8 / 360.0F;
                    paramCanvas.drawLine(f4 + f9, measuredWidth - f2 * f, f4 + f10, measuredWidth - f * measuredHeight, this.mModel);
                } else if (f2 > 0.0F) {
                    measuredWidth = f5 + f8 / 2.0F;
                    float f11 = f8 / 360.0F;
                    float f12 = f4 + (f9 + f10) / 2.0F;
                    paramCanvas.drawLine(f4 + f9, measuredWidth - f2 * f11, f12, f5, this.mModel);
                    paramCanvas.drawLine(f12, f6, f4 + f10, measuredWidth - f11 * measuredHeight, this.mModel);
                } else {
                    measuredWidth = f5 + f8 / 2.0F;
                    float f11 = f8 / 360.0F;
                    float f12 = f4 + (f9 + f10) / 2.0F;
                    paramCanvas.drawLine(f4 + f9, measuredWidth - f2 * f11, f12, f6, this.mModel);
                    paramCanvas.drawLine(f12, f5, f4 + f10, measuredWidth - f11 * measuredHeight, this.mModel);
                }
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = View.MeasureSpec.getMode(heightMeasureSpec);
        heightMeasureSpec = View.MeasureSpec.getSize(heightMeasureSpec);
        byte b = 100;
        if (i != 1073741824)
            if (i == Integer.MIN_VALUE) {
                heightMeasureSpec = Math.min(100, heightMeasureSpec);
            } else {
                heightMeasureSpec = 100;
            }
        int j = View.MeasureSpec.getMode(widthMeasureSpec);
        i = View.MeasureSpec.getSize(widthMeasureSpec);
        if (j == 1073741824) {
            widthMeasureSpec = i;
        } else {
            widthMeasureSpec = b;
            if (j == Integer.MIN_VALUE)
                widthMeasureSpec = Math.min(100, i);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void update(int minDeg, int maxDeg, int aantal, Integer[] tijd, Double[] richtingModel) {
        if (minDeg == maxDeg || minDeg < 0 || minDeg > 360 || maxDeg < 0 || maxDeg > 360) {
            this.kustdeg = -1;
            this.centrum = 0;
        } else {
            this.mindeg = minDeg;
            this.maxdeg = maxDeg;
            int i = maxDeg;
            if (minDeg > maxDeg)
                i = maxDeg + 360;
            this.kustdeg = (minDeg + i) / 2;
            this.kustdeg %= 360;
            this.centrum = (4 - (this.kustdeg + 45) % 360 / 90) % 4;
        }
        this.aantal = aantal;
        this.tijd = tijd;
        this.richtingModel = richtingModel;
        invalidate();
    }
}
