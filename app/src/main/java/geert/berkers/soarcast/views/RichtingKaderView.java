package geert.berkers.soarcast.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
public class RichtingKaderView extends View {
    private int centrum = 0;

    private int kustdeg;

    private Paint mAssen;

    private int maxdeg;

    private int mindeg;

    private int richt;

    private int uurVanaf;

    private Integer[] zonOpOnder = new Integer[] {6, 18};

    public RichtingKaderView(Context paramContext) {
        super(paramContext);
        init();
    }

    public RichtingKaderView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public RichtingKaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    protected void init() {
        this.mAssen = new Paint(1);
        this.mAssen.setColor(-7829368);
        this.mAssen.setStyle(Paint.Style.STROKE);
        this.richt = 0;
    }

    protected void onDraw(Canvas paramCanvas) {
        float measuredWidth = getMeasuredWidth();
        float measuredHeight = getMeasuredHeight();
        float f4 = measuredWidth / 700.0F;
        float f5 = 40.0F * f4;
        float f7 = 15.0F * f4;
        float f2 = 8.0F * f4;
        measuredWidth -= f2;
        float f8 = measuredHeight - 30.0F * f4;
        float f9 = measuredWidth - f5;
        float f6 = f8 - f7;
        int k = 4;
        String[] verticalAxisDescription = new String[4];
        verticalAxisDescription[0] = "S";
        int j = 1;
        verticalAxisDescription[1] = "E";
        verticalAxisDescription[2] = "N";
        verticalAxisDescription[3] = "W";
        if (this.richt == 1) {
            verticalAxisDescription[0] = "180";
            verticalAxisDescription[1] = "90";
            verticalAxisDescription[2] = "0";
            verticalAxisDescription[3] = "270";
        }
        Paint paint = this.mAssen;
        measuredHeight = 28.0F * f4;
        paint.setTextSize(measuredHeight);
        this.mAssen.setTextAlign(Paint.Align.LEFT);
        this.mAssen.setTypeface(Typeface.DEFAULT_BOLD);
        this.mAssen.setStyle(Paint.Style.FILL);
        int i = 1;
        while (i < 25) {
            k = i - 1;
            int m = (this.uurVanaf + k) % 24;
            if (m < this.zonOpOnder[0] || m > this.zonOpOnder[j]) {
                this.mAssen.setColor(268435711);
                float f14 = f9 / 24.0F;
                float f15 = k;
                float f16 = i;
                paint = this.mAssen;
                j = 1;
                paramCanvas.drawRect(f15 * f14 + f5, f7, f14 * f16 + f5, f8, paint);
            }
            if (m == this.zonOpOnder[0] || m == this.zonOpOnder[j]) {
                this.mAssen.setColor(100663551);
                float f14 = f9 / 24.0F;
                paramCanvas.drawRect(k * f14 + f5, f7, f5 + f14 * i, f8, this.mAssen);
            }
            this.mAssen.setColor(-3355444);
            float f = f5 + f9 / 24.0F * i;
            paramCanvas.drawLine(f, f7, f, f8, this.mAssen);
            i++;
            k = 4;
        }
        float f10 = f7 + 7.0F * f6 / 8.0F;
        paramCanvas.drawLine(f5, f10, measuredWidth, f10, this.mAssen);
        f10 = f7 + 5.0F * f6 / 8.0F;
        paramCanvas.drawLine(f5, f10, measuredWidth, f10, this.mAssen);
        f10 = f6 * 3.0F;
        float f11 = f7 + f10 / 8.0F;
        paramCanvas.drawLine(f5, f11, measuredWidth, f11, this.mAssen);
        f11 = f7 + 1.0F * f6 / 8.0F;
        paramCanvas.drawLine(f5, f11, measuredWidth, f11, this.mAssen);
        this.mAssen.setColor(-7829368);
        f11 = f10 / 4.0F;
        f10 = f7 + f11;
        paramCanvas.drawLine(f5, f10, measuredWidth, f10, this.mAssen);
        float f12 = f6 / 4.0F;
        f10 = f7 + f12;
        paramCanvas.drawLine(f5, f10, measuredWidth, f10, this.mAssen);
        float f13 = f6 / 2.0F;
        f10 = f7 + f13;
        paramCanvas.drawLine(f5, f10, measuredWidth, f10, this.mAssen);
        paramCanvas.drawLine(f5, f7, measuredWidth, f7, this.mAssen);
        paramCanvas.drawLine(f5, f8, measuredWidth, f8, this.mAssen);
        for (i = 3; i < 25; i += 3) {
            j = i - this.uurVanaf % 3;
            int m = this.uurVanaf % 24 / 3;
            float f14 = f9 / 24.0F * j;
            float f15 = f5 + f14;
            paramCanvas.drawLine(f15, f7, f15, f8, this.mAssen);
            if (j < 24) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append((m * 3 + i) % 24);
                paramCanvas.drawText(stringBuilder.toString(), f5 - f2 + f14, f2 * 3.0F + f8, this.mAssen);
            }
        }
        if (this.richt == 1) {
            this.mAssen.setTextSize(20.0F * f4);
        } else {
            this.mAssen.setTextSize(measuredHeight);
        }
        this.mAssen.setTextAlign(Paint.Align.RIGHT);
        paramCanvas.drawLine(f5, f7, f5, f8, this.mAssen);
        String str = verticalAxisDescription[this.centrum];
        measuredHeight = f5 - f2;
        f4 = f7 + f2;
        paramCanvas.drawText(str, measuredHeight, f4, this.mAssen);
        paramCanvas.drawText(verticalAxisDescription[(1 + this.centrum) % 4], measuredHeight, f4 + f12, this.mAssen);
        paramCanvas.drawText(verticalAxisDescription[(2 + this.centrum) % 4], measuredHeight, f4 + f13, this.mAssen);
        paramCanvas.drawText(verticalAxisDescription[(3 + this.centrum) % 4], measuredHeight, f4 + f11, this.mAssen);
        paramCanvas.drawText(verticalAxisDescription[(k + this.centrum) % 4], measuredHeight, f8 + f2, this.mAssen);
        this.mAssen.setColor(1355853952);
        this.mAssen.setStyle(Paint.Style.FILL);
        measuredHeight = (this.mindeg - (360 - this.centrum * 90) % 360);
        f4 = (this.maxdeg - (360 - 90 * this.centrum) % 360);
        f2 = measuredHeight;
        if (measuredHeight > 180.0F)
            f2 = measuredHeight - 360.0F;
        measuredHeight = f4;
        if (f4 > 180.0F)
            measuredHeight = f4 - 360.0F;
        f4 = f6 / 360.0F;
        paramCanvas.drawRect(f5, f10 - measuredHeight * f4, measuredWidth, f10 - f4 * f2, this.mAssen);
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

    public void update(int minDeg, int maxDeg, int richting, int uurVanaf, Integer[] zonOpOnder) {
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
        this.richt = richting;
        this.uurVanaf = uurVanaf;
        this.zonOpOnder = zonOpOnder;
        invalidate();
    }
}
