package geert.berkers.soarcast;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
public class WindMetingView extends View {
    private Integer aantal;

    private Paint mMeting;

    private final Integer maxN = Integer.valueOf(288);

    private int schaal;

    private Integer[] tijd = new Integer[this.maxN.intValue()];

    private Double[] vlaagMeting = new Double[this.maxN.intValue()];

    private Double[] windMeting = new Double[this.maxN.intValue()];

    public WindMetingView(Context paramContext) {
        super(paramContext);
        init();
    }

    public WindMetingView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public WindMetingView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
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
        float f4 = getMeasuredWidth();
        float f3 = getMeasuredHeight();
        float f6 = f4 / 700.0F;
        float f2 = 40.0F * f6;
        float f1 = 15.0F;
        f3 -= 15.0F * f6;
        f4 = f4 - 8.0F * f6 - f2;
        float f5 = f3 - 35.0F * f6;
        int i = this.schaal;
        int j = 1;
        if (i == 1)
            f1 = 25.0F;
        this.mMeting.setStrokeWidth(2.0F * f6);
        this.mMeting.setColor(-14671617);
        for (i = 1; i < this.aantal.intValue(); i++) {
            Integer[] arrayOfInteger = this.tijd;
            int k = i - 1;
            f6 = arrayOfInteger[k].intValue() * f4 / 24.0F / 3600.0F;
            float f = this.tijd[i].intValue() * f4 / 24.0F / 3600.0F;
            if (f6 < f)
                paramCanvas.drawLine(f2 + f6, f3 - this.windMeting[k].floatValue() / f1 * f5, f2 + f, f3 - this.windMeting[i].floatValue() / f1 * f5, this.mMeting);
        }
        this.mMeting.setColor(-5205936);
        for (i = j; i < this.aantal.intValue(); i++) {
            Integer[] arrayOfInteger = this.tijd;
            j = i - 1;
            f6 = arrayOfInteger[j].intValue() * f4 / 24.0F / 3600.0F;
            float f = this.tijd[i].intValue() * f4 / 24.0F / 3600.0F;
            if (f6 < f)
                paramCanvas.drawLine(f2 + f6, f3 - this.vlaagMeting[j].floatValue() / f1 * f5, f2 + f, f3 - this.vlaagMeting[i].floatValue() / f1 * f5, this.mMeting);
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

    public void update(int paramInt1, int paramInt2, Integer[] paramArrayOfInteger, Double[] paramArrayOfDouble1, Double[] paramArrayOfDouble2) {
        this.schaal = paramInt1;
        this.aantal = Integer.valueOf(paramInt2);
        this.tijd = paramArrayOfInteger;
        this.windMeting = paramArrayOfDouble1;
        this.vlaagMeting = paramArrayOfDouble2;
        invalidate();
    }
}
