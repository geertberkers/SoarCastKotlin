package geert.berkers.soarcast.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorgkluis (Geert Berkers)
 */

public class WindModelView extends View {
    private Integer aantal;

    private Paint mModel;

    private final Integer maxN = 288;

    private int schaal;

    private Integer[] tijd = new Integer[this.maxN];

    private Double[] vlaagModel = new Double[this.maxN];

    private Double[] windModel = new Double[this.maxN];

    public WindModelView(Context paramContext) {
        super(paramContext);
        init();
    }

    public WindModelView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public WindModelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
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
        float f4 = getMeasuredWidth();
        float f3 = getMeasuredHeight();
        float f5 = f4 / 700.0F;
        float f2 = 40.0F * f5;
        float f1 = 15.0F;
        f3 -= f5 * 15.0F;
        f4 = f4 - 8.0F * f5 - f2;
        f5 = f3 - 35.0F * f5;
        int j = this.schaal;
        int i = 1;
        if (j == 1)
            f1 = 25.0F;
        Path path = new Path();
        this.mModel.setColor(1073742079);
        this.mModel.setStyle(Paint.Style.FILL);
        while (i < this.aantal) {
            Integer[] arrayOfInteger = this.tijd;
            j = i - 1;
            float f6 = arrayOfInteger[j] * f4 / 24.0F / 3600.0F + f2;
            float f7 = this.tijd[i] * f4 / 24.0F / 3600.0F + f2;
            if (f6 < f7) {
                float f8 = f3 - this.windModel[j].floatValue() / f1 * f5;
                float f9 = this.windModel[i].floatValue() / f1;
                float f10 = this.vlaagModel[j].floatValue() / f1;
                float f11 = this.vlaagModel[i].floatValue() / f1;
                path.reset();
                path.moveTo(f6, f8);
                path.lineTo(f6, f3 - f10 * f5);
                path.lineTo(f7, f3 - f11 * f5);
                path.lineTo(f7, f3 - f9 * f5);
                path.lineTo(f6, f8);
                paramCanvas.drawPath(path, this.mModel);
            }
            i++;
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

    public void update(int schaal, int aantal, Integer[] tijd, Double[] windModel, Double[] vlaagModel) {
        this.schaal = schaal;
        this.aantal = aantal;
        this.tijd = tijd;
        this.windModel = windModel;
        this.vlaagModel = vlaagModel;
        invalidate();
    }
}
