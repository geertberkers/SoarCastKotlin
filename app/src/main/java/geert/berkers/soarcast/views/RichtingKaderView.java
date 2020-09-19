package geert.berkers.soarcast;

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

    private Integer[] zonOpOnder = new Integer[] { Integer.valueOf(6), Integer.valueOf(18) };

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
        float f1 = getMeasuredWidth();
        float f3 = getMeasuredHeight();
        float f4 = f1 / 700.0F;
        float f5 = 40.0F * f4;
        float f7 = 15.0F * f4;
        float f2 = 8.0F * f4;
        f1 -= f2;
        float f8 = f3 - 30.0F * f4;
        float f9 = f1 - f5;
        float f6 = f8 - f7;
        int k = 4;
        String[] arrayOfString = new String[4];
        arrayOfString[0] = "S";
        int j = 1;
        arrayOfString[1] = "E";
        arrayOfString[2] = "N";
        arrayOfString[3] = "W";
        if (this.richt == 1) {
            arrayOfString[0] = "180";
            arrayOfString[1] = "90";
            arrayOfString[2] = "0";
            arrayOfString[3] = "270";
        }
        Paint paint = this.mAssen;
        f3 = 28.0F * f4;
        paint.setTextSize(f3);
        this.mAssen.setTextAlign(Paint.Align.LEFT);
        this.mAssen.setTypeface(Typeface.DEFAULT_BOLD);
        this.mAssen.setStyle(Paint.Style.FILL);
        int i = 1;
        while (i < 25) {
            k = i - 1;
            int m = (this.uurVanaf + k) % 24;
            if (m < this.zonOpOnder[0].intValue() || m > this.zonOpOnder[j].intValue()) {
                this.mAssen.setColor(268435711);
                float f14 = f9 / 24.0F;
                float f15 = k;
                float f16 = i;
                paint = this.mAssen;
                j = 1;
                paramCanvas.drawRect(f15 * f14 + f5, f7, f14 * f16 + f5, f8, paint);
            }
            if (m == this.zonOpOnder[0].intValue() || m == this.zonOpOnder[j].intValue()) {
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
        paramCanvas.drawLine(f5, f10, f1, f10, this.mAssen);
        f10 = f7 + 5.0F * f6 / 8.0F;
        paramCanvas.drawLine(f5, f10, f1, f10, this.mAssen);
        f10 = f6 * 3.0F;
        float f11 = f7 + f10 / 8.0F;
        paramCanvas.drawLine(f5, f11, f1, f11, this.mAssen);
        f11 = f7 + 1.0F * f6 / 8.0F;
        paramCanvas.drawLine(f5, f11, f1, f11, this.mAssen);
        this.mAssen.setColor(-7829368);
        f11 = f10 / 4.0F;
        f10 = f7 + f11;
        paramCanvas.drawLine(f5, f10, f1, f10, this.mAssen);
        float f12 = f6 / 4.0F;
        f10 = f7 + f12;
        paramCanvas.drawLine(f5, f10, f1, f10, this.mAssen);
        float f13 = f6 / 2.0F;
        f10 = f7 + f13;
        paramCanvas.drawLine(f5, f10, f1, f10, this.mAssen);
        paramCanvas.drawLine(f5, f7, f1, f7, this.mAssen);
        paramCanvas.drawLine(f5, f8, f1, f8, this.mAssen);
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
            this.mAssen.setTextSize(f3);
        }
        this.mAssen.setTextAlign(Paint.Align.RIGHT);
        paramCanvas.drawLine(f5, f7, f5, f8, this.mAssen);
        String str = arrayOfString[this.centrum];
        f3 = f5 - f2;
        f4 = f7 + f2;
        paramCanvas.drawText(str, f3, f4, this.mAssen);
        paramCanvas.drawText(arrayOfString[(1 + this.centrum) % 4], f3, f4 + f12, this.mAssen);
        paramCanvas.drawText(arrayOfString[(2 + this.centrum) % 4], f3, f4 + f13, this.mAssen);
        paramCanvas.drawText(arrayOfString[(3 + this.centrum) % 4], f3, f4 + f11, this.mAssen);
        paramCanvas.drawText(arrayOfString[(k + this.centrum) % 4], f3, f8 + f2, this.mAssen);
        this.mAssen.setColor(1355853952);
        this.mAssen.setStyle(Paint.Style.FILL);
        f3 = (this.mindeg - (360 - this.centrum * 90) % 360);
        f4 = (this.maxdeg - (360 - 90 * this.centrum) % 360);
        f2 = f3;
        if (f3 > 180.0F)
            f2 = f3 - 360.0F;
        f3 = f4;
        if (f4 > 180.0F)
            f3 = f4 - 360.0F;
        f4 = f6 / 360.0F;
        paramCanvas.drawRect(f5, f10 - f3 * f4, f1, f10 - f4 * f2, this.mAssen);
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

    public void update(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Integer[] paramArrayOfInteger) {
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
        this.richt = paramInt3;
        this.uurVanaf = paramInt4;
        this.zonOpOnder = paramArrayOfInteger;
        invalidate();
    }
}
