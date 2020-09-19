package geert.berkers.soarcast.views;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
@SuppressLint("DefaultLocale")
public class WaardenView extends View {
    private Integer aantalR;

    private Integer aantalW;

    private int eenheid;

    private Paint mWaarden;

    private final Integer maxN = 288;

    private Double[] richtingMeting = new Double[this.maxN];

    private int schaal;

    private Integer tIndicator;

    private Integer[] tijdR = new Integer[this.maxN];

    private Integer[] tijdW = new Integer[this.maxN];

    private int uurVanaf;

    private Double[] vlaagMeting = new Double[this.maxN];

    private Double[] windMeting = new Double[this.maxN];

    public WaardenView(Context paramContext) {
        super(paramContext);
        init();
    }

    public WaardenView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public WaardenView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    protected void init() {
        this.mWaarden = new Paint(1);
        this.mWaarden.setColor(-7829368);
        this.mWaarden.setStyle(Paint.Style.STROKE);
        this.aantalW = 0;
        this.aantalR = 0;
        this.eenheid = 0;
        this.schaal = 0;
        this.uurVanaf = 24;
        this.tIndicator = 43200;
        for (int i = 0; i < this.maxN; i++) {
            this.tijdW[i] = 0;
            this.tijdR[i] = 0;
            this.windMeting[i] = 0.0D;
            this.vlaagMeting[i] = 0.0D;
            this.richtingMeting[i] = 0.0D;
        }
    }

    protected void onDraw(Canvas paramCanvas) {
        double d1;
        double d2;
        double d3;
        float f3 = getMeasuredWidth();
        float f7 = getMeasuredHeight();
        float f6 = f3 / 700.0F;
        this.mWaarden.setTextSize(28.0F * f6);
        float f1 = 40.0F * f6;
        float f2 = 8.0F * f6;
        float f4 = f3 - f2 - f1;
        float f8 = f7 - 0.0F;
        f3 = f1 + 1.0F * this.tIndicator / 86400.0F * f4;
        float f5 = f4 * 0.17F;
        f1 = 0.3F * f8;
        f4 = f1 / 2.0F;
        int i = this.schaal;
        if (this.aantalW > 0) {
            i = 1;
            d3 = 0.0D;
            double d = 0.0D;
            while (true) {
                d1 = d3;
                d2 = d;
                if (i < this.aantalW) {
                    int k = this.tIndicator;
                    Integer[] arrayOfInteger = this.tijdW;
                    int m = i - 1;
                    int j = i;
                    d2 = d3;
                    d1 = d;
                    if (k >= arrayOfInteger[m]) {
                        j = i;
                        d2 = d3;
                        d1 = d;
                        if (this.tIndicator <= this.tijdW[i]) {
                            if (Math.abs(this.tijdW[m] - this.tIndicator) <= Math.abs(this.tijdW[i] - this.tIndicator)) {
                                d1 = this.windMeting[m];
                                d2 = this.vlaagMeting[m];
                            } else {
                                d1 = this.windMeting[i];
                                d2 = this.vlaagMeting[i];
                            }
                            d = d1;
                            j = this.aantalW;
                            d1 = d2;
                            d2 = d;
                        }
                    }
                    i = j + 1;
                    d3 = d2;
                    d = d1;
                    continue;
                }
                break;
            }
        } else {
            d1 = 0.0D;
            d2 = 0.0D;
        }
        if (this.aantalR > 0) {
            i = 1;
            double d = 0.0D;
            while (true) {
                d3 = d;
                if (i < this.aantalR) {
                    int k = this.tIndicator;
                    Integer[] arrayOfInteger = this.tijdR;
                    int m = i - 1;
                    int j = i;
                    d3 = d;
                    if (k >= arrayOfInteger[m]) {
                        j = i;
                        d3 = d;
                        if (this.tIndicator <= this.tijdR[i]) {
                            if (Math.abs(this.tijdR[m] - this.tIndicator) <= Math.abs(this.tijdR[i] - this.tIndicator)) {
                                d = this.richtingMeting[m];
                            } else {
                                d = this.richtingMeting[i];
                            }
                            j = this.aantalR;
                            d3 = d;
                        }
                    }
                    i = j + 1;
                    d = d3;
                    continue;
                }
                break;
            }
        } else {
            d3 = 0.0D;
        }
        switch (this.eenheid) {
            case 3:
                d1 = Math.pow(d1 / 0.836D, 0.6666666666666666D);
                d2 = Math.pow(d2 / 0.836D, 0.6666666666666666D);
                break;
            case 2:
                d1 *= 1.944D;
                d2 *= 1.944D;
                break;
            case 1:
                d1 *= 3.6D;
                d2 *= 3.6D;
                break;
        }
        this.mWaarden.setTypeface(Typeface.DEFAULT);
        this.mWaarden.setStyle(Paint.Style.FILL);
        this.mWaarden.setStrokeWidth(2.0F * f6);
        this.mWaarden.setColor(-3355444);
        Path path = new Path();
        path.reset();
        path.moveTo(f3, 0.0F);
        float f9 = f3 - f4;
        float f11 = f8 / 2.0F;
        f6 = 0.0F + f11;
        f8 = f6 - f4;
        path.lineTo(f9, f8);
        float f10 = f3 + f4;
        path.lineTo(f10, f8);
        path.lineTo(f3, 0.0F);
        paramCanvas.drawPath(path, this.mWaarden);
        path.reset();
        path.moveTo(f3, f7);
        f11 = f7 - f11 + f4;
        path.lineTo(f9, f11);
        path.lineTo(f10, f11);
        path.lineTo(f3, f7);
        paramCanvas.drawPath(path, this.mWaarden);
        path.reset();
        f7 = f3 - f5;
        f10 = f4 / 1.5F;
        f11 = f7 - f10;
        path.moveTo(f11, f6);
        path.lineTo(f7, f8);
        f9 = f6 + f4;
        path.lineTo(f7, f9);
        path.lineTo(f11, f6);
        paramCanvas.drawPath(path, this.mWaarden);
        path.reset();
        f5 = f3 + f5;
        f10 += f5;
        path.moveTo(f10, f6);
        path.lineTo(f5, f8);
        path.lineTo(f5, f9);
        path.lineTo(f10, f6);
        paramCanvas.drawPath(path, this.mWaarden);
        paramCanvas.drawRect(f7, f6 - f1, f5, f6 + f1, this.mWaarden);
        this.mWaarden.setColor(-14671617);
        this.mWaarden.setTextAlign(Paint.Align.LEFT);
        String str = String.format("%.1f", d1);
        f1 = f4 / 2.0F;
        f4 = f6 + f2;
        paramCanvas.drawText(str, f7 + f1 + f2, f4, this.mWaarden);
        this.mWaarden.setColor(-5205936);
        this.mWaarden.setTextAlign(Paint.Align.CENTER);
        paramCanvas.drawText(String.format("%.1f", d2), f3, f4, this.mWaarden);
        this.mWaarden.setColor(-14671617);
        this.mWaarden.setTextAlign(Paint.Align.RIGHT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%.0f", d3));
        stringBuilder.append("");
        paramCanvas.drawText(stringBuilder.toString(), f5 - f2, f4, this.mWaarden);
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

    public void update(int eenheid, int schaal, int uurVanaf, int tIndicator) {
        this.eenheid = eenheid;
        this.schaal = schaal;
        this.uurVanaf = uurVanaf;
        this.tIndicator = tIndicator;
        invalidate();
    }

    public void zetRichting(int aantalR, Integer[] tijdR, Double[] richtingMeting) {
        this.aantalR = aantalR;
        this.tijdR = tijdR;
        this.richtingMeting = richtingMeting;
    }

    public void zetWind(int schaal, int aantalW, Integer[] tijdW, Double[] windMeting, Double[] vlaagmeting) {
        this.schaal = schaal;
        this.aantalW = aantalW;
        this.tijdW = tijdW;
        this.windMeting = windMeting;
        this.vlaagMeting = vlaagmeting;
    }
}
