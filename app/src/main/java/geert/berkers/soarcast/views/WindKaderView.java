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

public class WindKaderView extends View {
    private int eenheid;

    private Paint mAssen;

    private int schaal;

    private int uurVanaf;

    private Integer[] zonOpOnder = new Integer[] { Integer.valueOf(6), Integer.valueOf(18) };

    public WindKaderView(Context paramContext) {
        super(paramContext);
        init();
    }

    public WindKaderView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public WindKaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    protected void init() {
        this.mAssen = new Paint(1);
        this.mAssen.setColor(-7829368);
        this.mAssen.setStyle(Paint.Style.STROKE);
        this.eenheid = 0;
        this.schaal = 0;
        this.uurVanaf = 24;
    }

    protected void onDraw(Canvas paramCanvas) {
        float f5;
        float f6;
        float f7;
        int k;
        float f3 = getMeasuredWidth();
        float f1 = getMeasuredHeight();
        float f2 = f3 / 700.0F;
        this.mAssen.setTextSize(28.0F * f2);
        float f8 = 40.0F * f2;
        float f10 = 35.0F * f2;
        float f4 = 8.0F * f2;
        f3 -= f4;
        float f9 = f1 - f2 * 15.0F;
        float f11 = f3 - f8;
        f1 = f9 - f10;
        int i = this.schaal;
        int j = 1;
        if (i == 1) {
            f2 = 25.0F;
            f5 = 90.0F;
            f6 = 48.6F;
            k = 5;
            f7 = 10.0F;
        } else {
            f2 = 15.0F;
            f5 = 54.0F;
            f6 = 29.16F;
            f7 = 7.0F;
            k = 2;
        }
        this.mAssen.setTextAlign(Paint.Align.LEFT);
        this.mAssen.setTypeface(Typeface.DEFAULT_BOLD);
        this.mAssen.setStyle(Paint.Style.FILL);
        i = 1;
        while (true) {
            byte b = 25;
            int n = 24;
            if (i < 25) {
                n = i - 1;
                int i1 = (this.uurVanaf + n) % 24;
                if (i1 < this.zonOpOnder[0].intValue() || i1 > this.zonOpOnder[j].intValue()) {
                    this.mAssen.setColor(268435711);
                    float f13 = f11 / 24.0F;
                    float f14 = n;
                    float f15 = i;
                    Paint paint = this.mAssen;
                    j = 1;
                    paramCanvas.drawRect(f14 * f13 + f8, f10, f8 + f13 * f15, f9, paint);
                }
                if (i1 == this.zonOpOnder[0].intValue() || i1 == this.zonOpOnder[j].intValue()) {
                    this.mAssen.setColor(100663551);
                    float f13 = f11 / 24.0F;
                    paramCanvas.drawRect(n * f13 + f8, f10, f8 + f13 * i, f9, this.mAssen);
                }
                this.mAssen.setColor(-3355444);
                float f12 = f8 + f11 / 24.0F * i;
                paramCanvas.drawLine(f12, f10, f12, f9, this.mAssen);
                i++;
                continue;
            }
            if (this.eenheid == j) {
                int i1 = 0;
                i = b;
                j = n;
                while (true) {
                    float f12 = i1;
                    if (f12 < f5) {
                        f12 = f9 - f12 * f1 / f5;
                        paramCanvas.drawLine(f8, f12, f3, f12, this.mAssen);
                        i1 += k;
                        continue;
                    }
                    break;
                }
            } else {
                n = 24;
                b = 25;
                if (this.eenheid == 2) {
                    int i1 = 0;
                    while (true) {
                        float f12 = i1;
                        i = b;
                        j = n;
                        if (f12 < f6) {
                            f12 = f9 - f12 * f1 / f6;
                            paramCanvas.drawLine(f8, f12, f3, f12, this.mAssen);
                            i1 += k;
                            continue;
                        }
                        break;
                    }
                } else if (this.eenheid == 3) {
                    i = b;
                    j = n;
                } else {
                    k = 0;
                    while (true) {
                        float f12 = k;
                        i = b;
                        j = n;
                        if (f12 < f2) {
                            f12 = f9 - f12 * f1 / f2;
                            paramCanvas.drawLine(f8, f12, f3, f12, this.mAssen);
                            k++;
                            continue;
                        }
                        break;
                    }
                }
            }
            this.mAssen.setColor(-7829368);
            int m = 3;
            k = i;
            i = m;
            while (i < k) {
                k = i - this.uurVanaf % 3;
                m = this.uurVanaf % j / 3;
                float f12 = f11 / 24.0F * k;
                float f13 = f8 + f12;
                paramCanvas.drawLine(f13, f10, f13, f9, this.mAssen);
                if (k < j) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append((m * 3 + i) % 24);
                    paramCanvas.drawText(stringBuilder.toString(), f8 - f4 + f12, f4 * 3.0F, this.mAssen);
                }
                i += 3;
                k = 25;
            }
            this.mAssen.setColor(-3355444);
            this.mAssen.setTextAlign(Paint.Align.CENTER);
            float f = f1 / 10.0F + f10;
            f11 = ((36 - this.uurVanaf % j) % j) * f11 / 24.0F + f8;
            if (this.uurVanaf < 10)
                paramCanvas.drawText(getResources().getString(R.string.yesterday), f11, f, this.mAssen);
            if (this.uurVanaf > 38 && this.uurVanaf < 59)
                paramCanvas.drawText(getResources().getString(R.string.tomorrow), f11, f, this.mAssen);
            if (this.uurVanaf > 60)
                paramCanvas.drawText(getResources().getString(R.string.dayaftertomorrow), f11, f, this.mAssen);
            this.mAssen.setColor(-7829368);
            this.mAssen.setTextAlign(Paint.Align.RIGHT);
            paramCanvas.drawLine(f8, f10, f8, f9, this.mAssen);
            if (this.eenheid == 1) {
                i = 0;
                while (true) {
                    f2 = i;
                    f6 = f5 / 10.0F;
                    if (f2 < f6 + 1.0F) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(i * 10);
                        String str = stringBuilder.toString();
                        f2 = f9 - f2 * f1 / f6;
                        paramCanvas.drawText(str, f8 - f4, f2 + f4, this.mAssen);
                        paramCanvas.drawLine(f8, f2, f3, f2, this.mAssen);
                        i++;
                        continue;
                    }
                    break;
                }
            } else if (this.eenheid == 2) {
                i = 0;
                while (true) {
                    f2 = i;
                    f5 = f6 / 10.0F;
                    if (f2 < f5) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(i * 10);
                        String str = stringBuilder.toString();
                        f2 = f9 - f2 * f1 / f5;
                        paramCanvas.drawText(str, f8 - f4, f2 + f4, this.mAssen);
                        paramCanvas.drawLine(f8, f2, f3, f2, this.mAssen);
                        i++;
                        continue;
                    }
                    break;
                }
            } else if (this.eenheid == 3) {
                for (i = 0; i < f7; i++) {
                    f5 = (float)Math.pow(1.0D * i, 1.5D) * 0.836F;
                    if (this.schaal != 1 || i != 1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(i);
                        paramCanvas.drawText(stringBuilder.toString(), f8 - f4, f9 - f5 * f1 / f2 + f4, this.mAssen);
                    }
                    f5 = f9 - f5 * f1 / f2;
                    paramCanvas.drawLine(f8, f5, f3, f5, this.mAssen);
                }
            } else {
                i = 0;
                while (true) {
                    f5 = i;
                    f6 = f2 / 5.0F;
                    if (f5 < f6 + 1.0F) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(i * 5);
                        String str = stringBuilder.toString();
                        f5 = f9 - f5 * f1 / f6;
                        paramCanvas.drawText(str, f8 - f4, f5 + f4, this.mAssen);
                        paramCanvas.drawLine(f8, f5, f3, f5, this.mAssen);
                        i++;
                        continue;
                    }
                    break;
                }
            }
            return;
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

    public void update(int paramInt1, int paramInt2, int paramInt3, Integer[] paramArrayOfInteger) {
        this.eenheid = paramInt1;
        this.schaal = paramInt2;
        this.uurVanaf = paramInt3;
        this.zonOpOnder = paramArrayOfInteger;
        invalidate();
    }
}
