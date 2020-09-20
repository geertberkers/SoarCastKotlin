package geert.berkers.soarcast.decompiled;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zorgkluis (Geert Berkers)
 */

public class WelkomSoarCast extends Activity {
    private Integer eenheid;

    public GestureDetector gestureDetector1;

    public GestureDetector gestureDetector2;

    private Boolean grafiekBezig;

    private Integer klaar;

    private Integer locID;

    private Integer locIndex;

    private Integer locIndexMax;

    private ArrayList<Locatie> mLocatie;

    private ArrayList<Richting> mRichting;

    private ArrayList<Wind> mWind;

    private final Integer maxN = Integer.valueOf(288);

    private String[] queryDag = new String[] { "yesterday", "today", "tomorrow", "dayaftertomorrow" };

    public float reedsVerschoven1;

    public float reedsVerschoven2;

    private Integer richt;

    private Integer schaal;

    private Integer tIndicator;

    private String[] tekstEenheid = new String[] { "m/s", "km/h", "kn", "Bft" };

    private String[] tekstModel = new String[] { "Harm", "GFS" };

    private String[] tekstRicht = new String[] { "N", "deg" };

    private Long tijdNul;

    private Integer uurVanaf;

    private Integer weerModel;

    private Integer[] bepaalKolommen(String paramString) {
        Integer[] arrayOfInteger = new Integer[7];
        arrayOfInteger[0] = Integer.valueOf(-1);
        arrayOfInteger[1] = Integer.valueOf(-1);
        arrayOfInteger[2] = Integer.valueOf(-1);
        arrayOfInteger[3] = Integer.valueOf(-1);
        arrayOfInteger[4] = Integer.valueOf(-1);
        arrayOfInteger[5] = Integer.valueOf(-1);
        arrayOfInteger[6] = Integer.valueOf(-1);
        String str = getResources().getString(2131427328);
        Integer integer1 = Integer.valueOf(0);
        Integer integer2 = Integer.valueOf(paramString.length());
        int i = 0;
        while (true) {
            Integer integer;
            if (integer1.intValue() < integer2.intValue()) {
                byte b;
                Integer integer3 = Integer.valueOf(paramString.indexOf(str, integer1.intValue()));
                integer = integer3;
                if (integer3.intValue() < 0)
                    integer = integer2;
                String str1 = paramString.substring(integer1.intValue(), integer.intValue());
                switch (str1.hashCode()) {
                    default:
                        b = -1;
                        break;
                    case 664374541:
                        if (str1.equals("gfs_windsnelheid")) {
                            b = 3;
                            break;
                        }
                    case 441051965:
                        if (str1.equals("harm_windrichting")) {
                            b = 5;
                            break;
                        }
                    case 96835762:
                        if (str1.equals("etime")) {
                            b = 0;
                            break;
                        }
                    case -1281577795:
                        if (str1.equals("gfs_windrichting")) {
                            b = 6;
                            break;
                        }
                    case -1897228994:
                        if (str1.equals("harm_windvlaag")) {
                            b = 2;
                            break;
                        }
                    case -1907962995:
                        if (str1.equals("harm_windsnelheid")) {
                            b = 1;
                            break;
                        }
                    case -2064956482:
                        if (str1.equals("gfs_windvlaag")) {
                            b = 4;
                            break;
                        }
                }
                switch (b) {
                    default:
                        if (str1.length() > 8) {
                            if (str1.substring(0, 8).equals("windstoo")) {
                                arrayOfInteger[i] = Integer.valueOf(2);
                                break;
                            }
                            if (str1.substring(0, 8).equals("windsnel")) {
                                arrayOfInteger[i] = Integer.valueOf(1);
                            } else if (str1.substring(0, 8).equals("windrich")) {
                                arrayOfInteger[i] = Integer.valueOf(7);
                            }
                            i++;
                            Integer integer4 = Integer.valueOf(integer.intValue() + 1);
                            continue;
                        }
                        break;
                    case 6:
                        arrayOfInteger[i] = Integer.valueOf(9);
                        break;
                    case 5:
                        arrayOfInteger[i] = Integer.valueOf(8);
                        break;
                    case 4:
                        arrayOfInteger[i] = Integer.valueOf(6);
                        break;
                    case 3:
                        arrayOfInteger[i] = Integer.valueOf(5);
                        break;
                    case 2:
                        arrayOfInteger[i] = Integer.valueOf(4);
                        break;
                    case 1:
                        arrayOfInteger[i] = Integer.valueOf(3);
                        break;
                    case 0:
                        arrayOfInteger[i] = Integer.valueOf(0);
                        break;
                }
            } else {
                break;
            }
            i++;
            integer1 = Integer.valueOf(integer.intValue() + 1);
        }
        return arrayOfInteger;
    }

    private void doeLocatieN() {
        if (this.klaar.intValue() > 0 && !this.grafiekBezig.booleanValue() && this.locID.intValue() >= 0 && this.locIndex.intValue() > 0) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            Integer integer = this.locIndex;
            this.locIndex = Integer.valueOf(this.locIndex.intValue() - 1);
            updateLocatieWindRichting();
            editor.putInt("locatie", this.locIndex.intValue());
            editor.apply();
        }
    }

    private void doeLocatieZ() {
        if (this.klaar.intValue() > 0 && !this.grafiekBezig.booleanValue() && this.locID.intValue() >= 0 && this.locIndex.intValue() < this.locIndexMax.intValue()) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            Integer integer = this.locIndex;
            this.locIndex = Integer.valueOf(this.locIndex.intValue() + 1);
            updateLocatieWindRichting();
            editor.putInt("locatie", this.locIndex.intValue());
            editor.apply();
        }
    }

    private void ikBenKlaar() {
        Integer integer = this.klaar;
        this.klaar = Integer.valueOf(this.klaar.intValue() + 1);
        if (this.klaar.intValue() > 0) {
            ((TextView)findViewById(2131165247)).setText("");
            ((waardenView)findViewById(2131165326)).update(this.eenheid.intValue(), this.schaal.intValue(), this.uurVanaf.intValue(), this.tIndicator.intValue());
        }
    }

    private void updateLocatieWindRichting() {
        this.klaar = Integer.valueOf(-1);
        this.locID = (this.mLocatie.get(this.locIndex.intValue())).id;
        ((TextView)findViewById(2131165247)).setText(getResources().getString(2131427366));
        ((TextView)findViewById(2131165252)).setText((this.mLocatie.get(this.locIndex.intValue())).naam);
        if (!this.mWind.isEmpty())
            this.mWind.clear();
        if (!this.mRichting.isEmpty())
            this.mRichting.clear();
        LeesWind leesWind = new LeesWind();
        LeesRichting leesRichting = new LeesRichting();
        leesWind.execute((Object[])new Integer[] { this.locID });
        leesRichting.execute((Object[])new Integer[] { this.locID });
    }

    private void updateRichting() {
        Integer[] arrayOfInteger = new Integer[this.maxN.intValue()];
        Double[] arrayOfDouble = new Double[this.maxN.intValue()];
        int k = this.mRichting.size();
        Long long_2 = Long.valueOf(this.tijdNul.longValue() + (this.uurVanaf.intValue() * 3600));
        Long long_1 = Long.valueOf(long_2.longValue() + 86400L);
        int i = 0;
        int j;
        for (j = 0; (this.mRichting.get(i)).unixTimestamp.longValue() <= long_1.longValue(); j = m) {
            int m = j;
            if ((this.mRichting.get(i)).unixTimestamp.longValue() >= long_2.longValue()) {
                Double double_ = (this.mRichting.get(i)).richtingMeting;
                m = j;
                if (double_.doubleValue() >= 0.0D) {
                    m = j;
                    if (double_.doubleValue() < 1000.0D) {
                        m = j;
                        if (j < this.maxN.intValue()) {
                            arrayOfInteger[j] = Integer.valueOf((int)((this.mRichting.get(i)).unixTimestamp.longValue() - long_2.longValue()));
                            arrayOfDouble[j] = double_;
                            m = j + 1;
                        }
                    }
                }
            }
            if (i < k - 1) {
                i++;
                j = m;
                continue;
            }
            long_1 = Long.valueOf(0L);
        }
        ((richtingMetingView)findViewById(2131165275)).update((this.mLocatie.get(this.locIndex.intValue())).mindeg.intValue(), (this.mLocatie.get(this.locIndex.intValue())).maxdeg.intValue(), j, arrayOfInteger, arrayOfDouble);
        ((waardenView)findViewById(2131165326)).zetRichting(j, arrayOfInteger, arrayOfDouble);
    }

    private void updateRichtingModel() {
        Integer[] arrayOfInteger = new Integer[this.maxN.intValue()];
        Double[] arrayOfDouble = new Double[this.maxN.intValue()];
        int k = this.mRichting.size();
        Long long_2 = Long.valueOf(this.tijdNul.longValue() + (this.uurVanaf.intValue() * 3600));
        Long long_1 = Long.valueOf(long_2.longValue() + 86400L);
        int i = 0;
        int j;
        for (j = 0; (this.mRichting.get(i)).unixTimestamp.longValue() <= long_1.longValue(); j = m) {
            int m = j;
            if ((this.mRichting.get(i)).unixTimestamp.longValue() >= long_2.longValue()) {
                Double double_;
                if (this.weerModel.intValue() == 1) {
                    double_ = (this.mRichting.get(i)).richtingGFS;
                } else {
                    double_ = (this.mRichting.get(i)).richtingHarmonie;
                }
                m = j;
                if (double_.doubleValue() >= 0.0D) {
                    m = j;
                    if (j < this.maxN.intValue()) {
                        arrayOfInteger[j] = Integer.valueOf((int)((this.mRichting.get(i)).unixTimestamp.longValue() - long_2.longValue()));
                        arrayOfDouble[j] = double_;
                        m = j + 1;
                    }
                }
            }
            if (i < k - 1) {
                i++;
                j = m;
                continue;
            }
            long_1 = Long.valueOf(0L);
        }
        ((richtingModelView)findViewById(2131165276)).update((this.mLocatie.get(this.locIndex.intValue())).mindeg.intValue(), (this.mLocatie.get(this.locIndex.intValue())).maxdeg.intValue(), j, arrayOfInteger, arrayOfDouble);
    }

    private void updateWindModel() {
        Integer[] arrayOfInteger = new Integer[this.maxN.intValue()];
        Double[] arrayOfDouble1 = new Double[this.maxN.intValue()];
        Double[] arrayOfDouble2 = new Double[this.maxN.intValue()];
        int k = this.mWind.size();
        Long long_1 = Long.valueOf(this.tijdNul.longValue() + (this.uurVanaf.intValue() * 3600));
        Long long_2 = Long.valueOf(long_1.longValue() + 86400L);
        int i = 0;
        int j = 0;
        while ((this.mWind.get(i)).unixTimestamp.longValue() <= long_2.longValue()) {
            if ((this.mWind.get(i)).unixTimestamp.longValue() >= long_1.longValue()) {
                Double double_1;
                Double double_2;
                if (this.weerModel.intValue() == 1) {
                    double_1 = (this.mWind.get(i)).snelheidGFS;
                    double_2 = (this.mWind.get(i)).vlaagGFS;
                } else {
                    double_1 = (this.mWind.get(i)).snelheidHarmonie;
                    double_2 = (this.mWind.get(i)).vlaagHarmonie;
                }
                if (double_2.doubleValue() > 0.0D || (double_1.doubleValue() > 0.0D && j < this.maxN.intValue())) {
                    arrayOfInteger[j] = Integer.valueOf((int)((this.mWind.get(i)).unixTimestamp.longValue() - long_1.longValue()));
                    arrayOfDouble1[j] = double_1;
                    arrayOfDouble2[j] = double_2;
                    j++;
                }
            }
            if (i < k - 1) {
                i++;
                continue;
            }
            long_2 = Long.valueOf(0L);
        }
        ((windModelView)findViewById(2131165329)).update(this.schaal.intValue(), j, arrayOfInteger, arrayOfDouble1, arrayOfDouble2);
    }

    private void updateWindmeting() {
        // Byte code:
        //   0: aload_0
        //   1: getfield maxN : Ljava/lang/Integer;
        //   4: invokevirtual intValue : ()I
        //   7: anewarray java/lang/Integer
        //   10: astore #9
        //   12: aload_0
        //   13: getfield maxN : Ljava/lang/Integer;
        //   16: invokevirtual intValue : ()I
        //   19: anewarray java/lang/Double
        //   22: astore #10
        //   24: aload_0
        //   25: getfield maxN : Ljava/lang/Integer;
        //   28: invokevirtual intValue : ()I
        //   31: anewarray java/lang/Double
        //   34: astore #11
        //   36: aload_0
        //   37: getfield mWind : Ljava/util/ArrayList;
        //   40: invokevirtual size : ()I
        //   43: istore #4
        //   45: aload_0
        //   46: getfield tijdNul : Ljava/lang/Long;
        //   49: invokevirtual longValue : ()J
        //   52: aload_0
        //   53: getfield uurVanaf : Ljava/lang/Integer;
        //   56: invokevirtual intValue : ()I
        //   59: sipush #3600
        //   62: imul
        //   63: i2l
        //   64: ladd
        //   65: invokestatic valueOf : (J)Ljava/lang/Long;
        //   68: astore #12
        //   70: aload #12
        //   72: invokevirtual longValue : ()J
        //   75: ldc2_w 86400
        //   78: ladd
        //   79: invokestatic valueOf : (J)Ljava/lang/Long;
        //   82: astore #7
        //   84: iconst_0
        //   85: istore_1
        //   86: iconst_0
        //   87: istore_2
        //   88: aload_0
        //   89: getfield mWind : Ljava/util/ArrayList;
        //   92: iload_1
        //   93: invokevirtual get : (I)Ljava/lang/Object;
        //   96: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
        //   99: invokestatic access$3400 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Long;
        //   102: invokevirtual longValue : ()J
        //   105: aload #7
        //   107: invokevirtual longValue : ()J
        //   110: lcmp
        //   111: ifgt -> 402
        //   114: iload_2
        //   115: istore_3
        //   116: aload_0
        //   117: getfield mWind : Ljava/util/ArrayList;
        //   120: iload_1
        //   121: invokevirtual get : (I)Ljava/lang/Object;
        //   124: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
        //   127: invokestatic access$3400 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Long;
        //   130: invokevirtual longValue : ()J
        //   133: aload #12
        //   135: invokevirtual longValue : ()J
        //   138: lcmp
        //   139: iflt -> 374
        //   142: aload_0
        //   143: getfield mWind : Ljava/util/ArrayList;
        //   146: iload_1
        //   147: invokevirtual get : (I)Ljava/lang/Object;
        //   150: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
        //   153: invokestatic access$3500 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
        //   156: astore #6
        //   158: aload_0
        //   159: getfield mWind : Ljava/util/ArrayList;
        //   162: iload_1
        //   163: invokevirtual get : (I)Ljava/lang/Object;
        //   166: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
        //   169: invokestatic access$3600 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
        //   172: astore #8
        //   174: aload #8
        //   176: invokevirtual doubleValue : ()D
        //   179: dconst_0
        //   180: dcmpl
        //   181: ifgt -> 209
        //   184: iload_2
        //   185: istore_3
        //   186: aload #6
        //   188: invokevirtual doubleValue : ()D
        //   191: dconst_0
        //   192: dcmpl
        //   193: ifle -> 374
        //   196: iload_2
        //   197: istore_3
        //   198: iload_2
        //   199: aload_0
        //   200: getfield maxN : Ljava/lang/Integer;
        //   203: invokevirtual intValue : ()I
        //   206: if_icmpge -> 374
        //   209: aload #9
        //   211: iload_2
        //   212: aload_0
        //   213: getfield mWind : Ljava/util/ArrayList;
        //   216: iload_1
        //   217: invokevirtual get : (I)Ljava/lang/Object;
        //   220: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
        //   223: invokestatic access$3400 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Long;
        //   226: invokevirtual longValue : ()J
        //   229: aload #12
        //   231: invokevirtual longValue : ()J
        //   234: lsub
        //   235: l2i
        //   236: invokestatic valueOf : (I)Ljava/lang/Integer;
        //   239: aastore
        //   240: aload #6
        //   242: invokevirtual doubleValue : ()D
        //   245: dconst_0
        //   246: dcmpg
        //   247: ifle -> 266
        //   250: aload #6
        //   252: astore #5
        //   254: aload #6
        //   256: invokevirtual doubleValue : ()D
        //   259: ldc2_w 400.0
        //   262: dcmpl
        //   263: iflt -> 285
        //   266: iload_2
        //   267: ifle -> 281
        //   270: aload #10
        //   272: iload_2
        //   273: iconst_1
        //   274: isub
        //   275: aaload
        //   276: astore #5
        //   278: goto -> 285
        //   281: aload #8
        //   283: astore #5
        //   285: aload #8
        //   287: invokevirtual doubleValue : ()D
        //   290: dconst_0
        //   291: dcmpg
        //   292: ifle -> 311
        //   295: aload #8
        //   297: astore #6
        //   299: aload #8
        //   301: invokevirtual doubleValue : ()D
        //   304: ldc2_w 400.0
        //   307: dcmpl
        //   308: iflt -> 330
        //   311: iload_2
        //   312: ifle -> 326
        //   315: aload #11
        //   317: iload_2
        //   318: iconst_1
        //   319: isub
        //   320: aaload
        //   321: astore #6
        //   323: goto -> 330
        //   326: aload #5
        //   328: astore #6
        //   330: iload_2
        //   331: istore_3
        //   332: aload #5
        //   334: invokevirtual doubleValue : ()D
        //   337: ldc2_w 400.0
        //   340: dcmpg
        //   341: ifge -> 374
        //   344: iload_2
        //   345: istore_3
        //   346: aload #6
        //   348: invokevirtual doubleValue : ()D
        //   351: ldc2_w 400.0
        //   354: dcmpg
        //   355: ifge -> 374
        //   358: aload #10
        //   360: iload_2
        //   361: aload #5
        //   363: aastore
        //   364: aload #11
        //   366: iload_2
        //   367: aload #6
        //   369: aastore
        //   370: iload_2
        //   371: iconst_1
        //   372: iadd
        //   373: istore_3
        //   374: iload_1
        //   375: iload #4
        //   377: iconst_1
        //   378: isub
        //   379: if_icmpge -> 391
        //   382: iload_1
        //   383: iconst_1
        //   384: iadd
        //   385: istore_1
        //   386: iload_3
        //   387: istore_2
        //   388: goto -> 88
        //   391: lconst_0
        //   392: invokestatic valueOf : (J)Ljava/lang/Long;
        //   395: astore #7
        //   397: iload_3
        //   398: istore_2
        //   399: goto -> 88
        //   402: aload_0
        //   403: ldc_w 2131165328
        //   406: invokevirtual findViewById : (I)Landroid/view/View;
        //   409: checkcast com/erwinvoogt/soarcast/windMetingView
        //   412: aload_0
        //   413: getfield schaal : Ljava/lang/Integer;
        //   416: invokevirtual intValue : ()I
        //   419: iload_2
        //   420: aload #9
        //   422: aload #10
        //   424: aload #11
        //   426: invokevirtual update : (II[Ljava/lang/Integer;[Ljava/lang/Double;[Ljava/lang/Double;)V
        //   429: aload_0
        //   430: ldc_w 2131165326
        //   433: invokevirtual findViewById : (I)Landroid/view/View;
        //   436: checkcast com/erwinvoogt/soarcast/waardenView
        //   439: aload_0
        //   440: getfield schaal : Ljava/lang/Integer;
        //   443: invokevirtual intValue : ()I
        //   446: iload_2
        //   447: aload #9
        //   449: aload #10
        //   451: aload #11
        //   453: invokevirtual zetWind : (II[Ljava/lang/Integer;[Ljava/lang/Double;[Ljava/lang/Double;)V
        //   456: aload_0
        //   457: getfield grafiekBezig : Ljava/lang/Boolean;
        //   460: invokevirtual booleanValue : ()Z
        //   463: ifne -> 520
        //   466: iload_2
        //   467: ifle -> 480
        //   470: aload_0
        //   471: aload #9
        //   473: iload_2
        //   474: iconst_1
        //   475: isub
        //   476: aaload
        //   477: putfield tIndicator : Ljava/lang/Integer;
        //   480: iload_2
        //   481: ifeq -> 510
        //   484: aload_0
        //   485: getfield tIndicator : Ljava/lang/Integer;
        //   488: invokevirtual intValue : ()I
        //   491: sipush #10800
        //   494: if_icmplt -> 510
        //   497: aload_0
        //   498: getfield tIndicator : Ljava/lang/Integer;
        //   501: invokevirtual intValue : ()I
        //   504: ldc_w 75600
        //   507: if_icmple -> 520
        //   510: aload_0
        //   511: ldc_w 43200
        //   514: invokestatic valueOf : (I)Ljava/lang/Integer;
        //   517: putfield tIndicator : Ljava/lang/Integer;
        //   520: return
    }

    public void locatieOpMaps() {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append((this.mLocatie.get(this.locIndex.intValue())).lat.toString());
        stringBuilder2.append(",");
        stringBuilder2.append((this.mLocatie.get(this.locIndex.intValue())).lon.toString());
        String str = stringBuilder2.toString();
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri.Builder builder = Uri.parse("geo:0,0?").buildUpon();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str);
        stringBuilder3.append("(");
        stringBuilder3.append((this.mLocatie.get(this.locIndex.intValue())).naam);
        stringBuilder3.append(" - weather station)");
        Uri uri = builder.appendQueryParameter("q", stringBuilder3.toString()).build();
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            return;
        }
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Maps niet bereikt ");
        stringBuilder1.append(uri);
        Log.d("SoarCast", stringBuilder1.toString());
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final Integer schermbreedte = Integer.valueOf(displayMetrics.widthPixels);
        this.mLocatie = new ArrayList<Locatie>();
        this.mWind = new ArrayList<Wind>();
        this.mRichting = new ArrayList<Richting>();
        this.locIndexMax = Integer.valueOf(0);
        this.locIndex = Integer.valueOf(0);
        this.locID = Integer.valueOf(-1);
        this.klaar = Integer.valueOf(-1);
        this.weerModel = Integer.valueOf(0);
        this.eenheid = Integer.valueOf(0);
        this.schaal = Integer.valueOf(1);
        this.richt = Integer.valueOf(1);
        this.uurVanaf = Integer.valueOf(24);
        this.tijdNul = Long.valueOf(0L);
        this.grafiekBezig = Boolean.valueOf(false);
        this.tIndicator = Integer.valueOf(43200);
        getIntent().getExtras().getString("gebruiker");
        String str = getIntent().getExtras().getString("bestand");
        this.eenheid = Integer.valueOf(sharedPreferences.getInt("eenheid", 0));
        this.richt = Integer.valueOf(sharedPreferences.getInt("richting", 0));
        this.locIndex = Integer.valueOf(sharedPreferences.getInt("locatie", 0));
        if (paramBundle == null) {
            setContentView(2131296284);
            ((TextView)findViewById(2131165247)).setText(getResources().getString(2131427366));
            (new LeesLocaties()).execute((Object[])new String[] { str });
            final Button btM = (Button)findViewById(2131165257);
            final Button btE = (Button)findViewById(2131165233);
            final Button btR = (Button)findViewById(2131165273);
            ImageButton imageButton1 = (ImageButton)findViewById(2131165228);
            button1.setText(this.tekstModel[this.weerModel.intValue()]);
            button2.setText(this.tekstEenheid[this.eenheid.intValue()]);
            button3.setText(this.tekstRicht[this.richt.intValue()]);
            button1.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            button2.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            button3.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            imageButton1.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            ImageButton imageButton2 = (ImageButton)findViewById(2131165253);
            ImageButton imageButton3 = (ImageButton)findViewById(2131165254);
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    if (WelkomSoarCast.this.klaar.intValue() > 0 && !WelkomSoarCast.this.grafiekBezig.booleanValue()) {
                        WelkomSoarCast.this.weerModel;
                        WelkomSoarCast.access$302(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.weerModel.intValue() + 1));
                        WelkomSoarCast.access$302(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.weerModel.intValue() % 2));
                        btM.setText(WelkomSoarCast.this.tekstModel[WelkomSoarCast.this.weerModel.intValue()]);
                        WelkomSoarCast.this.updateWindModel();
                        WelkomSoarCast.this.updateRichtingModel();
                    }
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    if (WelkomSoarCast.this.klaar.intValue() > 0 && !WelkomSoarCast.this.grafiekBezig.booleanValue()) {
                        WelkomSoarCast.this.eenheid;
                        WelkomSoarCast.access$702(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.eenheid.intValue() + 1));
                        WelkomSoarCast.access$702(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.eenheid.intValue() % 4));
                        btE.setText(WelkomSoarCast.this.tekstEenheid[WelkomSoarCast.this.eenheid.intValue()]);
                        ((windKaderView)WelkomSoarCast.this.findViewById(2131165327)).update(WelkomSoarCast.this.eenheid.intValue(), WelkomSoarCast.this.schaal.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), ((WelkomSoarCast.Wind)WelkomSoarCast.this.mWind.get(0)).geefZonOpOnder());
                        ((waardenView)WelkomSoarCast.this.findViewById(2131165326)).update(WelkomSoarCast.this.eenheid.intValue(), WelkomSoarCast.this.schaal.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), WelkomSoarCast.this.tIndicator.intValue());
                        editor.putInt("eenheid", WelkomSoarCast.this.eenheid.intValue());
                        editor.apply();
                    }
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    if (WelkomSoarCast.this.klaar.intValue() > 0 && !WelkomSoarCast.this.grafiekBezig.booleanValue()) {
                        WelkomSoarCast.this.richt;
                        WelkomSoarCast.access$1402(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.richt.intValue() + 1));
                        WelkomSoarCast.access$1402(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.richt.intValue() % 2));
                        btR.setText(WelkomSoarCast.this.tekstRicht[WelkomSoarCast.this.richt.intValue()]);
                        ((richtingKaderView)WelkomSoarCast.this.findViewById(2131165274)).update((WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex.intValue())).mindeg.intValue(), (WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex.intValue())).maxdeg.intValue(), WelkomSoarCast.this.richt.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), ((WelkomSoarCast.Richting)WelkomSoarCast.this.mRichting.get(0)).geefZonOpOnder());
                        editor.putInt("richting", WelkomSoarCast.this.richt.intValue());
                        editor.apply();
                    }
                }
            });
            imageButton3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    WelkomSoarCast.this.doeLocatieZ();
                }
            });
            imageButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    WelkomSoarCast.this.doeLocatieN();
                }
            });
            imageButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    WelkomSoarCast.this.schermafdruk();
                }
            });
            ((TextView)findViewById(2131165252)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View param1View) {
                    if (WelkomSoarCast.this.locID.intValue() >= 0)
                        WelkomSoarCast.this.locatieOpMaps();
                }
            });
            this.gestureDetector1 = new GestureDetector((Context)this, (GestureDetector.OnGestureListener)new OnSwipeListener() {
                public boolean onSchuif(float param1Float) {
                    int i = schermbreedte.intValue() / 24;
                    Integer integer1 = Integer.valueOf((int)(param1Float - 1.0D * WelkomSoarCast.this.reedsVerschoven1));
                    Integer integer2 = Integer.valueOf(integer1.intValue() / Integer.valueOf(i).intValue());
                    if (integer2.intValue() != 0 && !WelkomSoarCast.this.grafiekBezig.booleanValue() && WelkomSoarCast.this.klaar.intValue() > 0) {
                        WelkomSoarCast.access$202(WelkomSoarCast.this, Boolean.valueOf(true));
                        WelkomSoarCast.this.reedsVerschoven1 += integer1.intValue();
                        WelkomSoarCast.access$1002(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.uurVanaf.intValue() - integer2.intValue()));
                        if (WelkomSoarCast.this.uurVanaf.intValue() < 0)
                            WelkomSoarCast.access$1002(WelkomSoarCast.this, Integer.valueOf(0));
                        if (WelkomSoarCast.this.uurVanaf.intValue() > 72)
                            WelkomSoarCast.access$1002(WelkomSoarCast.this, Integer.valueOf(72));
                        ((windKaderView)WelkomSoarCast.this.findViewById(2131165327)).update(WelkomSoarCast.this.eenheid.intValue(), WelkomSoarCast.this.schaal.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), ((WelkomSoarCast.Wind)WelkomSoarCast.this.mWind.get(0)).geefZonOpOnder());
                        ((richtingKaderView)WelkomSoarCast.this.findViewById(2131165274)).update((WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex.intValue())).mindeg.intValue(), (WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex.intValue())).maxdeg.intValue(), WelkomSoarCast.this.richt.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), ((WelkomSoarCast.Richting)WelkomSoarCast.this.mRichting.get(0)).geefZonOpOnder());
                        WelkomSoarCast.this.updateWindmeting();
                        WelkomSoarCast.this.updateWindModel();
                        WelkomSoarCast.this.updateRichting();
                        WelkomSoarCast.this.updateRichtingModel();
                        ((waardenView)WelkomSoarCast.this.findViewById(2131165326)).update(WelkomSoarCast.this.eenheid.intValue(), WelkomSoarCast.this.schaal.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), WelkomSoarCast.this.tIndicator.intValue());
                        WelkomSoarCast.access$202(WelkomSoarCast.this, Boolean.valueOf(false));
                    }
                    return true;
                }

                public boolean onSwipe(OnSwipeListener.Direction param1Direction) {
                    if (param1Direction == OnSwipeListener.Direction.up)
                        WelkomSoarCast.this.doeLocatieZ();
                    if (param1Direction == OnSwipeListener.Direction.down)
                        WelkomSoarCast.this.doeLocatieN();
                    return true;
                }

                public boolean zetSchuifOpNul() {
                    WelkomSoarCast.this.reedsVerschoven1 = 0.0F;
                    return true;
                }
            });
            findViewById(2131165327).setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                    WelkomSoarCast.this.gestureDetector1.onTouchEvent(param1MotionEvent);
                    return true;
                }
            });
            findViewById(2131165274).setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                    WelkomSoarCast.this.gestureDetector1.onTouchEvent(param1MotionEvent);
                    return true;
                }
            });
            this.gestureDetector2 = new GestureDetector((Context)this, (GestureDetector.OnGestureListener)new OnSwipeListener() {
                public boolean onSchuif(float param1Float) {
                    Integer integer = Integer.valueOf((int)(param1Float - 1.0D * WelkomSoarCast.this.reedsVerschoven2));
                    param1Float = 86400.0F * integer.intValue() * 1.0F / 1.0F * schermbreedte.intValue();
                    if (param1Float != 0.0F && !WelkomSoarCast.this.grafiekBezig.booleanValue() && WelkomSoarCast.this.klaar.intValue() > 0) {
                        WelkomSoarCast.access$202(WelkomSoarCast.this, Boolean.valueOf(true));
                        WelkomSoarCast.this.reedsVerschoven2 += integer.intValue();
                        WelkomSoarCast.access$1302(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.tIndicator.intValue() + (int)param1Float));
                        if (WelkomSoarCast.this.tIndicator.intValue() < 10800)
                            WelkomSoarCast.access$1302(WelkomSoarCast.this, Integer.valueOf(10800));
                        if (WelkomSoarCast.this.tIndicator.intValue() > 75600)
                            WelkomSoarCast.access$1302(WelkomSoarCast.this, Integer.valueOf(75600));
                        ((waardenView)WelkomSoarCast.this.findViewById(2131165326)).update(WelkomSoarCast.this.eenheid.intValue(), WelkomSoarCast.this.schaal.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), WelkomSoarCast.this.tIndicator.intValue());
                        WelkomSoarCast.access$202(WelkomSoarCast.this, Boolean.valueOf(false));
                    }
                    return true;
                }

                public boolean zetSchuifOpNul() {
                    WelkomSoarCast.this.reedsVerschoven2 = 0.0F;
                    return true;
                }
            });
            findViewById(2131165326).setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                    WelkomSoarCast.this.gestureDetector2.onTouchEvent(param1MotionEvent);
                    return true;
                }
            });
        }
    }

    public void schermafdruk() {
        if (ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission((Context)this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            View view = getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File file = Environment.getExternalStorageDirectory();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append(getResources().getText(2131427388));
            file = new File(stringBuilder.toString());
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception exception) {
                Log.e("SoarCast", "Error screenshot", exception);
            }
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(file));
            startActivity(Intent.createChooser(intent, "Share via"));
            return;
        }
        Toast.makeText(getBaseContext(), getResources().getString(2131427378), 0).show();
    }

    private class LeesLocaties extends AsyncTask<String, Void, Integer> {
        private final String LOG_TAG = LeesLocaties.class.getSimpleName();

        private LeesLocaties() {}

        protected Integer doInBackground(String... param1VarArgs) {
            // Byte code:
            //   0: new java/lang/StringBuilder
            //   3: dup
            //   4: invokespecial <init> : ()V
            //   7: astore_3
            //   8: aload_3
            //   9: aload_0
            //   10: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   13: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   16: ldc 2131427395
            //   18: invokevirtual getString : (I)Ljava/lang/String;
            //   21: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   24: pop
            //   25: aload_3
            //   26: aload_1
            //   27: iconst_0
            //   28: aaload
            //   29: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   32: pop
            //   33: aload_3
            //   34: aload_0
            //   35: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   38: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   41: ldc 2131427365
            //   43: invokevirtual getString : (I)Ljava/lang/String;
            //   46: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   49: pop
            //   50: aload_3
            //   51: invokevirtual toString : ()Ljava/lang/String;
            //   54: astore_1
            //   55: iconst_0
            //   56: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   59: astore #15
            //   61: aload_0
            //   62: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   65: invokestatic access$1700 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   68: invokevirtual isEmpty : ()Z
            //   71: ifne -> 84
            //   74: aload_0
            //   75: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   78: invokestatic access$1700 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   81: invokevirtual clear : ()V
            //   84: aconst_null
            //   85: astore #4
            //   87: aconst_null
            //   88: astore_3
            //   89: new java/net/URL
            //   92: dup
            //   93: aload_1
            //   94: invokespecial <init> : (Ljava/lang/String;)V
            //   97: invokevirtual openConnection : ()Ljava/net/URLConnection;
            //   100: checkcast java/net/HttpURLConnection
            //   103: astore #13
            //   105: aload #13
            //   107: ldc 'GET'
            //   109: invokevirtual setRequestMethod : (Ljava/lang/String;)V
            //   112: aload #13
            //   114: invokevirtual connect : ()V
            //   117: aload #13
            //   119: invokevirtual getInputStream : ()Ljava/io/InputStream;
            //   122: astore_1
            //   123: aload_1
            //   124: ifnonnull -> 142
            //   127: aload #13
            //   129: ifnull -> 137
            //   132: aload #13
            //   134: invokevirtual disconnect : ()V
            //   137: iconst_0
            //   138: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   141: areturn
            //   142: new java/io/BufferedReader
            //   145: dup
            //   146: new java/io/InputStreamReader
            //   149: dup
            //   150: aload_1
            //   151: invokespecial <init> : (Ljava/io/InputStream;)V
            //   154: invokespecial <init> : (Ljava/io/Reader;)V
            //   157: astore #14
            //   159: ldc ''
            //   161: astore #7
            //   163: iconst_0
            //   164: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   167: astore #8
            //   169: dconst_0
            //   170: invokestatic valueOf : (D)Ljava/lang/Double;
            //   173: astore_3
            //   174: dconst_0
            //   175: invokestatic valueOf : (D)Ljava/lang/Double;
            //   178: astore_1
            //   179: iconst_0
            //   180: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   183: astore #5
            //   185: iconst_0
            //   186: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   189: astore #4
            //   191: iconst_m1
            //   192: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   195: astore #6
            //   197: aload_0
            //   198: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   201: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   204: ldc 2131427328
            //   206: invokevirtual getString : (I)Ljava/lang/String;
            //   209: astore #17
            //   211: aload #14
            //   213: invokevirtual readLine : ()Ljava/lang/String;
            //   216: astore #18
            //   218: aload #18
            //   220: ifnull -> 910
            //   223: aload #15
            //   225: invokevirtual intValue : ()I
            //   228: ifle -> 1143
            //   231: aload #18
            //   233: aload #17
            //   235: invokevirtual indexOf : (Ljava/lang/String;)I
            //   238: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   241: astore #16
            //   243: aload #6
            //   245: astore #11
            //   247: aload #16
            //   249: invokevirtual intValue : ()I
            //   252: ifle -> 280
            //   255: aload #18
            //   257: iconst_0
            //   258: aload #16
            //   260: invokevirtual intValue : ()I
            //   263: invokevirtual substring : (II)Ljava/lang/String;
            //   266: astore #7
            //   268: aload #16
            //   270: invokevirtual intValue : ()I
            //   273: iconst_1
            //   274: iadd
            //   275: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   278: astore #11
            //   280: aload #11
            //   282: invokevirtual intValue : ()I
            //   285: istore_2
            //   286: aload #7
            //   288: astore #10
            //   290: aload #8
            //   292: astore #6
            //   294: aload #16
            //   296: astore #12
            //   298: aload #11
            //   300: astore #9
            //   302: aload #10
            //   304: astore #7
            //   306: iload_2
            //   307: aload #16
            //   309: invokevirtual intValue : ()I
            //   312: if_icmple -> 418
            //   315: aload #18
            //   317: aload #17
            //   319: aload #11
            //   321: invokevirtual intValue : ()I
            //   324: invokevirtual indexOf : (Ljava/lang/String;I)I
            //   327: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   330: astore #16
            //   332: aload #8
            //   334: astore #6
            //   336: aload #16
            //   338: astore #12
            //   340: aload #11
            //   342: astore #9
            //   344: aload #10
            //   346: astore #7
            //   348: aload #16
            //   350: invokevirtual intValue : ()I
            //   353: aload #11
            //   355: invokevirtual intValue : ()I
            //   358: if_icmplt -> 418
            //   361: aload #18
            //   363: aload #11
            //   365: invokevirtual intValue : ()I
            //   368: aload #16
            //   370: invokevirtual intValue : ()I
            //   373: invokevirtual substring : (II)Ljava/lang/String;
            //   376: astore #6
            //   378: aload #6
            //   380: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Integer;
            //   383: astore #6
            //   385: aload #10
            //   387: astore #7
            //   389: goto -> 402
            //   392: iconst_0
            //   393: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   396: astore #6
            //   398: ldc ''
            //   400: astore #7
            //   402: aload #16
            //   404: invokevirtual intValue : ()I
            //   407: iconst_1
            //   408: iadd
            //   409: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   412: astore #9
            //   414: aload #16
            //   416: astore #12
            //   418: aload #12
            //   420: astore #8
            //   422: aload #9
            //   424: invokevirtual intValue : ()I
            //   427: aload #12
            //   429: invokevirtual intValue : ()I
            //   432: if_icmple -> 1118
            //   435: aload #18
            //   437: aload #17
            //   439: aload #9
            //   441: invokevirtual intValue : ()I
            //   444: invokevirtual indexOf : (Ljava/lang/String;I)I
            //   447: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   450: astore #10
            //   452: aload #10
            //   454: astore #8
            //   456: aload #10
            //   458: invokevirtual intValue : ()I
            //   461: aload #9
            //   463: invokevirtual intValue : ()I
            //   466: if_icmplt -> 1118
            //   469: aload #18
            //   471: aload #9
            //   473: invokevirtual intValue : ()I
            //   476: aload #10
            //   478: invokevirtual intValue : ()I
            //   481: invokevirtual substring : (II)Ljava/lang/String;
            //   484: astore_3
            //   485: aload_3
            //   486: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   489: astore_3
            //   490: goto -> 498
            //   493: dconst_0
            //   494: invokestatic valueOf : (D)Ljava/lang/Double;
            //   497: astore_3
            //   498: aload #10
            //   500: invokevirtual intValue : ()I
            //   503: iconst_1
            //   504: iadd
            //   505: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   508: astore #9
            //   510: aload #10
            //   512: astore #8
            //   514: goto -> 517
            //   517: aload #8
            //   519: astore #10
            //   521: aload #9
            //   523: invokevirtual intValue : ()I
            //   526: aload #8
            //   528: invokevirtual intValue : ()I
            //   531: if_icmple -> 1121
            //   534: aload #18
            //   536: aload #17
            //   538: aload #9
            //   540: invokevirtual intValue : ()I
            //   543: invokevirtual indexOf : (Ljava/lang/String;I)I
            //   546: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   549: astore #8
            //   551: aload #8
            //   553: astore #10
            //   555: aload #8
            //   557: invokevirtual intValue : ()I
            //   560: aload #9
            //   562: invokevirtual intValue : ()I
            //   565: if_icmplt -> 1121
            //   568: aload #18
            //   570: aload #9
            //   572: invokevirtual intValue : ()I
            //   575: aload #8
            //   577: invokevirtual intValue : ()I
            //   580: invokevirtual substring : (II)Ljava/lang/String;
            //   583: astore_1
            //   584: aload_1
            //   585: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   588: astore_1
            //   589: goto -> 597
            //   592: dconst_0
            //   593: invokestatic valueOf : (D)Ljava/lang/Double;
            //   596: astore_1
            //   597: aload #8
            //   599: invokevirtual intValue : ()I
            //   602: iconst_1
            //   603: iadd
            //   604: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   607: astore #11
            //   609: aload #8
            //   611: astore #10
            //   613: goto -> 616
            //   616: aload #10
            //   618: astore #12
            //   620: aload #5
            //   622: astore #8
            //   624: aload #11
            //   626: astore #9
            //   628: aload #11
            //   630: invokevirtual intValue : ()I
            //   633: aload #10
            //   635: invokevirtual intValue : ()I
            //   638: if_icmple -> 732
            //   641: aload #18
            //   643: aload #17
            //   645: aload #11
            //   647: invokevirtual intValue : ()I
            //   650: invokevirtual indexOf : (Ljava/lang/String;I)I
            //   653: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   656: astore #10
            //   658: aload #10
            //   660: astore #12
            //   662: aload #5
            //   664: astore #8
            //   666: aload #11
            //   668: astore #9
            //   670: aload #10
            //   672: invokevirtual intValue : ()I
            //   675: aload #11
            //   677: invokevirtual intValue : ()I
            //   680: if_icmplt -> 732
            //   683: aload #18
            //   685: aload #11
            //   687: invokevirtual intValue : ()I
            //   690: aload #10
            //   692: invokevirtual intValue : ()I
            //   695: invokevirtual substring : (II)Ljava/lang/String;
            //   698: astore #5
            //   700: aload #5
            //   702: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Integer;
            //   705: astore #8
            //   707: goto -> 716
            //   710: iconst_0
            //   711: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   714: astore #8
            //   716: aload #10
            //   718: invokevirtual intValue : ()I
            //   721: iconst_1
            //   722: iadd
            //   723: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   726: astore #9
            //   728: aload #10
            //   730: astore #12
            //   732: aload #9
            //   734: invokevirtual intValue : ()I
            //   737: aload #12
            //   739: invokevirtual intValue : ()I
            //   742: if_icmple -> 1128
            //   745: aload #18
            //   747: aload #17
            //   749: aload #9
            //   751: invokevirtual intValue : ()I
            //   754: invokevirtual indexOf : (Ljava/lang/String;I)I
            //   757: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   760: astore #10
            //   762: aload #10
            //   764: astore #5
            //   766: aload #10
            //   768: invokevirtual intValue : ()I
            //   771: aload #9
            //   773: invokevirtual intValue : ()I
            //   776: if_icmpge -> 789
            //   779: aload #18
            //   781: invokevirtual length : ()I
            //   784: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   787: astore #5
            //   789: aload #5
            //   791: invokevirtual intValue : ()I
            //   794: aload #9
            //   796: invokevirtual intValue : ()I
            //   799: if_icmplt -> 1128
            //   802: aload #18
            //   804: aload #9
            //   806: invokevirtual intValue : ()I
            //   809: aload #5
            //   811: invokevirtual intValue : ()I
            //   814: invokevirtual substring : (II)Ljava/lang/String;
            //   817: astore #4
            //   819: aload #4
            //   821: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Integer;
            //   824: astore #4
            //   826: goto -> 835
            //   829: iconst_0
            //   830: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   833: astore #4
            //   835: aload #5
            //   837: invokevirtual intValue : ()I
            //   840: iconst_1
            //   841: iadd
            //   842: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   845: astore #9
            //   847: goto -> 1128
            //   850: aload #7
            //   852: invokevirtual length : ()I
            //   855: ifle -> 891
            //   858: aload_0
            //   859: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   862: invokestatic access$1700 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   865: new com/erwinvoogt/soarcast/WelkomSoarCast$Locatie
            //   868: dup
            //   869: aload_0
            //   870: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   873: aload #7
            //   875: aload #10
            //   877: aload_3
            //   878: aload_1
            //   879: aload #5
            //   881: aload #4
            //   883: aconst_null
            //   884: invokespecial <init> : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //   887: invokevirtual add : (Ljava/lang/Object;)Z
            //   890: pop
            //   891: aload #15
            //   893: invokevirtual intValue : ()I
            //   896: iconst_1
            //   897: iadd
            //   898: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   901: astore #15
            //   903: aload #10
            //   905: astore #8
            //   907: goto -> 211
            //   910: aload #13
            //   912: ifnull -> 920
            //   915: aload #13
            //   917: invokevirtual disconnect : ()V
            //   920: aload #14
            //   922: ifnull -> 945
            //   925: aload #14
            //   927: invokevirtual close : ()V
            //   930: aload #15
            //   932: areturn
            //   933: astore_1
            //   934: aload_0
            //   935: getfield LOG_TAG : Ljava/lang/String;
            //   938: ldc 'Error closing stream'
            //   940: aload_1
            //   941: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   944: pop
            //   945: aload #15
            //   947: areturn
            //   948: astore_3
            //   949: aload #14
            //   951: astore_1
            //   952: goto -> 965
            //   955: astore_3
            //   956: aload #14
            //   958: astore_1
            //   959: goto -> 977
            //   962: astore_3
            //   963: aconst_null
            //   964: astore_1
            //   965: aload_3
            //   966: astore #4
            //   968: aload #13
            //   970: astore_3
            //   971: goto -> 1054
            //   974: astore_3
            //   975: aconst_null
            //   976: astore_1
            //   977: aload_3
            //   978: astore #4
            //   980: aload #13
            //   982: astore_3
            //   983: goto -> 1000
            //   986: astore_1
            //   987: aconst_null
            //   988: astore #5
            //   990: aload #4
            //   992: astore_3
            //   993: goto -> 1060
            //   996: astore #4
            //   998: aconst_null
            //   999: astore_1
            //   1000: aload_0
            //   1001: getfield LOG_TAG : Ljava/lang/String;
            //   1004: ldc 'Error '
            //   1006: aload #4
            //   1008: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1011: pop
            //   1012: iconst_m1
            //   1013: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1016: astore #4
            //   1018: aload_3
            //   1019: ifnull -> 1026
            //   1022: aload_3
            //   1023: invokevirtual disconnect : ()V
            //   1026: aload_1
            //   1027: ifnull -> 1049
            //   1030: aload_1
            //   1031: invokevirtual close : ()V
            //   1034: aload #4
            //   1036: areturn
            //   1037: astore_1
            //   1038: aload_0
            //   1039: getfield LOG_TAG : Ljava/lang/String;
            //   1042: ldc 'Error closing stream'
            //   1044: aload_1
            //   1045: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1048: pop
            //   1049: aload #4
            //   1051: areturn
            //   1052: astore #4
            //   1054: aload_1
            //   1055: astore #5
            //   1057: aload #4
            //   1059: astore_1
            //   1060: aload_3
            //   1061: ifnull -> 1068
            //   1064: aload_3
            //   1065: invokevirtual disconnect : ()V
            //   1068: aload #5
            //   1070: ifnull -> 1093
            //   1073: aload #5
            //   1075: invokevirtual close : ()V
            //   1078: goto -> 1093
            //   1081: astore_3
            //   1082: aload_0
            //   1083: getfield LOG_TAG : Ljava/lang/String;
            //   1086: ldc 'Error closing stream'
            //   1088: aload_3
            //   1089: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1092: pop
            //   1093: aload_1
            //   1094: athrow
            //   1095: astore #6
            //   1097: goto -> 392
            //   1100: astore_3
            //   1101: goto -> 493
            //   1104: astore_1
            //   1105: goto -> 592
            //   1108: astore #5
            //   1110: goto -> 710
            //   1113: astore #4
            //   1115: goto -> 829
            //   1118: goto -> 517
            //   1121: aload #9
            //   1123: astore #11
            //   1125: goto -> 616
            //   1128: aload #6
            //   1130: astore #10
            //   1132: aload #8
            //   1134: astore #5
            //   1136: aload #9
            //   1138: astore #6
            //   1140: goto -> 850
            //   1143: aload #8
            //   1145: astore #10
            //   1147: goto -> 850
            // Exception table:
            //   from	to	target	type
            //   89	105	996	java/io/IOException
            //   89	105	986	finally
            //   105	123	974	java/io/IOException
            //   105	123	962	finally
            //   142	159	974	java/io/IOException
            //   142	159	962	finally
            //   163	211	955	java/io/IOException
            //   163	211	948	finally
            //   211	218	955	java/io/IOException
            //   211	218	948	finally
            //   223	243	955	java/io/IOException
            //   223	243	948	finally
            //   247	280	955	java/io/IOException
            //   247	280	948	finally
            //   280	286	955	java/io/IOException
            //   280	286	948	finally
            //   306	332	955	java/io/IOException
            //   306	332	948	finally
            //   348	378	955	java/io/IOException
            //   348	378	948	finally
            //   378	385	1095	java/lang/NumberFormatException
            //   378	385	955	java/io/IOException
            //   378	385	948	finally
            //   392	398	955	java/io/IOException
            //   392	398	948	finally
            //   402	414	955	java/io/IOException
            //   402	414	948	finally
            //   422	452	955	java/io/IOException
            //   422	452	948	finally
            //   456	485	955	java/io/IOException
            //   456	485	948	finally
            //   485	490	1100	java/lang/NumberFormatException
            //   485	490	955	java/io/IOException
            //   485	490	948	finally
            //   493	498	955	java/io/IOException
            //   493	498	948	finally
            //   498	510	955	java/io/IOException
            //   498	510	948	finally
            //   521	551	955	java/io/IOException
            //   521	551	948	finally
            //   555	584	955	java/io/IOException
            //   555	584	948	finally
            //   584	589	1104	java/lang/NumberFormatException
            //   584	589	955	java/io/IOException
            //   584	589	948	finally
            //   592	597	955	java/io/IOException
            //   592	597	948	finally
            //   597	609	955	java/io/IOException
            //   597	609	948	finally
            //   628	658	955	java/io/IOException
            //   628	658	948	finally
            //   670	700	955	java/io/IOException
            //   670	700	948	finally
            //   700	707	1108	java/lang/NumberFormatException
            //   700	707	955	java/io/IOException
            //   700	707	948	finally
            //   710	716	955	java/io/IOException
            //   710	716	948	finally
            //   716	728	955	java/io/IOException
            //   716	728	948	finally
            //   732	762	955	java/io/IOException
            //   732	762	948	finally
            //   766	789	955	java/io/IOException
            //   766	789	948	finally
            //   789	819	955	java/io/IOException
            //   789	819	948	finally
            //   819	826	1113	java/lang/NumberFormatException
            //   819	826	955	java/io/IOException
            //   819	826	948	finally
            //   829	835	955	java/io/IOException
            //   829	835	948	finally
            //   835	847	955	java/io/IOException
            //   835	847	948	finally
            //   850	891	955	java/io/IOException
            //   850	891	948	finally
            //   891	903	955	java/io/IOException
            //   891	903	948	finally
            //   925	930	933	java/io/IOException
            //   1000	1018	1052	finally
            //   1030	1034	1037	java/io/IOException
            //   1073	1078	1081	java/io/IOException
        }

        protected void onPostExecute(Integer param1Integer) {
            if (param1Integer.intValue() > 0) {
                WelkomSoarCast.access$2802(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.mLocatie.size() - 1));
                if (WelkomSoarCast.this.locIndex.intValue() > WelkomSoarCast.this.locIndexMax.intValue())
                    WelkomSoarCast.access$1602(WelkomSoarCast.this, Integer.valueOf(WelkomSoarCast.this.locIndexMax.intValue() / 2));
                WelkomSoarCast.this.updateLocatieWindRichting();
                return;
            }
            Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(2131427361), 0).show();
        }
    }

    private class LeesRichting extends AsyncTask<Integer, Void, Integer> {
        private final String LOG_TAG = LeesRichting.class.getSimpleName();

        private LeesRichting() {}

        protected Integer doInBackground(Integer... param1VarArgs) {
            Integer integer = param1VarArgs[0];
            String str = WelkomSoarCast.this.getResources().getString(2131427328);
            byte b = 0;
            Double double_3 = Double.valueOf(0.0D);
            Double double_2 = Double.valueOf(0.0D);
            Double double_1 = Double.valueOf(0.0D);
            while (true) {
                if (b < 4) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(WelkomSoarCast.this.getResources().getString(2131427396));
                    stringBuilder.append(WelkomSoarCast.this.getResources().getString(2131427381));
                    String str1 = Uri.parse(stringBuilder.toString()).buildUpon().appendQueryParameter(WelkomSoarCast.this.getResources().getString(2131427387), WelkomSoarCast.this.getResources().getString(2131427374)).appendQueryParameter(WelkomSoarCast.this.getResources().getString(2131427385), integer.toString()).appendQueryParameter(WelkomSoarCast.this.getResources().getString(2131427384), WelkomSoarCast.this.queryDag[b]).appendQueryParameter(WelkomSoarCast.this.getResources().getString(2131427386), WelkomSoarCast.this.getResources().getString(2131427373)).appendQueryParameter(WelkomSoarCast.this.getResources().getString(2131427382), WelkomSoarCast.this.getResources().getString(2131427371)).appendQueryParameter(WelkomSoarCast.this.getResources().getString(2131427383), "").build().toString();
                    try {

                    } catch (IOException iOException) {

                    } finally {
                        str = null;
                        double_1 = null;
                        str1 = null;
                    }
                } else {
                    if (WelkomSoarCast.this.mRichting.size() < 1) {
                        WelkomSoarCast.this.mRichting.add(new WelkomSoarCast.Richting(Long.valueOf(1512514800L), Integer.valueOf(0), integer, double_2, double_1, double_3));
                        return Integer.valueOf(-2);
                    }
                    return Integer.valueOf(1);
                }
                double_1 = null;
                continue;
            }
        }

        protected void onPostExecute(Integer param1Integer) {
            if (param1Integer.intValue() < 1) {
                if (param1Integer.intValue() == -2) {
                    Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(2131427370), 0).show();
                } else {
                    Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(2131427361), 0).show();
                }
            } else {
                WelkomSoarCast.access$4802(WelkomSoarCast.this, (WelkomSoarCast.this.mRichting.get(0)).unixTimestamp);
            }
            WelkomSoarCast.this.updateRichting();
            WelkomSoarCast.this.updateRichtingModel();
            ((richtingKaderView)WelkomSoarCast.this.findViewById(2131165274)).update((WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex.intValue())).mindeg.intValue(), (WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex.intValue())).maxdeg.intValue(), WelkomSoarCast.this.richt.intValue(), WelkomSoarCast.this.uurVanaf.intValue(), ((WelkomSoarCast.Richting)WelkomSoarCast.this.mRichting.get(0)).geefZonOpOnder());
            WelkomSoarCast.this.ikBenKlaar();
        }
    }

    private class LeesWind extends AsyncTask<Integer, Void, Integer> {
        private final String LOG_TAG = LeesWind.class.getSimpleName();

        private LeesWind() {}

        protected Integer doInBackground(Integer... param1VarArgs) {
            // Byte code:
            //   0: aload_1
            //   1: iconst_0
            //   2: aaload
            //   3: astore_1
            //   4: aload_0
            //   5: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   8: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   11: ldc 2131427328
            //   13: invokevirtual getString : (I)Ljava/lang/String;
            //   16: astore #8
            //   18: lconst_0
            //   19: lstore #5
            //   21: lconst_0
            //   22: invokestatic valueOf : (J)Ljava/lang/Long;
            //   25: astore #15
            //   27: dconst_0
            //   28: invokestatic valueOf : (D)Ljava/lang/Double;
            //   31: astore #9
            //   33: dconst_0
            //   34: invokestatic valueOf : (D)Ljava/lang/Double;
            //   37: astore #10
            //   39: dconst_0
            //   40: invokestatic valueOf : (D)Ljava/lang/Double;
            //   43: astore #11
            //   45: dconst_0
            //   46: invokestatic valueOf : (D)Ljava/lang/Double;
            //   49: astore #12
            //   51: dconst_0
            //   52: invokestatic valueOf : (D)Ljava/lang/Double;
            //   55: astore #13
            //   57: iconst_0
            //   58: istore_2
            //   59: dconst_0
            //   60: invokestatic valueOf : (D)Ljava/lang/Double;
            //   63: astore #14
            //   65: iload_2
            //   66: iconst_4
            //   67: if_icmpge -> 1785
            //   70: new java/lang/StringBuilder
            //   73: dup
            //   74: invokespecial <init> : ()V
            //   77: astore #7
            //   79: aload #7
            //   81: aload_0
            //   82: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   85: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   88: ldc 2131427396
            //   90: invokevirtual getString : (I)Ljava/lang/String;
            //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   96: pop
            //   97: aload #7
            //   99: aload_0
            //   100: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   103: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   106: ldc 2131427381
            //   108: invokevirtual getString : (I)Ljava/lang/String;
            //   111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   114: pop
            //   115: aload #7
            //   117: invokevirtual toString : ()Ljava/lang/String;
            //   120: invokestatic parse : (Ljava/lang/String;)Landroid/net/Uri;
            //   123: invokevirtual buildUpon : ()Landroid/net/Uri$Builder;
            //   126: aload_0
            //   127: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   130: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   133: ldc 2131427387
            //   135: invokevirtual getString : (I)Ljava/lang/String;
            //   138: aload_0
            //   139: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   142: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   145: ldc 2131427375
            //   147: invokevirtual getString : (I)Ljava/lang/String;
            //   150: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
            //   153: aload_0
            //   154: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   157: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   160: ldc 2131427385
            //   162: invokevirtual getString : (I)Ljava/lang/String;
            //   165: aload_1
            //   166: invokevirtual toString : ()Ljava/lang/String;
            //   169: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
            //   172: aload_0
            //   173: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   176: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   179: ldc 2131427384
            //   181: invokevirtual getString : (I)Ljava/lang/String;
            //   184: aload_0
            //   185: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   188: invokestatic access$4500 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)[Ljava/lang/String;
            //   191: iload_2
            //   192: aaload
            //   193: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
            //   196: aload_0
            //   197: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   200: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   203: ldc 2131427386
            //   205: invokevirtual getString : (I)Ljava/lang/String;
            //   208: aload_0
            //   209: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   212: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   215: ldc 2131427373
            //   217: invokevirtual getString : (I)Ljava/lang/String;
            //   220: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
            //   223: aload_0
            //   224: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   227: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   230: ldc 2131427382
            //   232: invokevirtual getString : (I)Ljava/lang/String;
            //   235: aload_0
            //   236: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   239: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   242: ldc 2131427371
            //   244: invokevirtual getString : (I)Ljava/lang/String;
            //   247: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
            //   250: aload_0
            //   251: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   254: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   257: ldc 2131427383
            //   259: invokevirtual getString : (I)Ljava/lang/String;
            //   262: ldc ''
            //   264: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
            //   267: invokevirtual build : ()Landroid/net/Uri;
            //   270: invokevirtual toString : ()Ljava/lang/String;
            //   273: astore #7
            //   275: new java/net/URL
            //   278: dup
            //   279: aload #7
            //   281: invokespecial <init> : (Ljava/lang/String;)V
            //   284: invokevirtual openConnection : ()Ljava/net/URLConnection;
            //   287: checkcast java/net/HttpURLConnection
            //   290: astore #7
            //   292: aload #7
            //   294: ldc 'GET'
            //   296: invokevirtual setRequestMethod : (Ljava/lang/String;)V
            //   299: aload #7
            //   301: invokevirtual connect : ()V
            //   304: aload #7
            //   306: invokevirtual getInputStream : ()Ljava/io/InputStream;
            //   309: astore #16
            //   311: aload #16
            //   313: ifnull -> 1563
            //   316: new java/io/BufferedReader
            //   319: dup
            //   320: new java/io/InputStreamReader
            //   323: dup
            //   324: aload #16
            //   326: invokespecial <init> : (Ljava/io/InputStream;)V
            //   329: invokespecial <init> : (Ljava/io/Reader;)V
            //   332: astore #17
            //   334: aload #7
            //   336: astore #19
            //   338: aload #17
            //   340: astore #18
            //   342: aload #7
            //   344: astore #20
            //   346: aload #17
            //   348: astore #21
            //   350: bipush #7
            //   352: anewarray java/lang/Integer
            //   355: astore #23
            //   357: aload #7
            //   359: astore #19
            //   361: aload #17
            //   363: astore #18
            //   365: aload #7
            //   367: astore #20
            //   369: aload #17
            //   371: astore #21
            //   373: aload #23
            //   375: iconst_0
            //   376: iconst_m1
            //   377: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   380: aastore
            //   381: aload #7
            //   383: astore #19
            //   385: aload #17
            //   387: astore #18
            //   389: aload #7
            //   391: astore #20
            //   393: aload #17
            //   395: astore #21
            //   397: aload #23
            //   399: iconst_1
            //   400: iconst_m1
            //   401: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   404: aastore
            //   405: aload #7
            //   407: astore #19
            //   409: aload #17
            //   411: astore #18
            //   413: aload #7
            //   415: astore #20
            //   417: aload #17
            //   419: astore #21
            //   421: aload #23
            //   423: iconst_2
            //   424: iconst_m1
            //   425: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   428: aastore
            //   429: aload #7
            //   431: astore #19
            //   433: aload #17
            //   435: astore #18
            //   437: aload #7
            //   439: astore #20
            //   441: aload #17
            //   443: astore #21
            //   445: aload #23
            //   447: iconst_3
            //   448: iconst_m1
            //   449: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   452: aastore
            //   453: aload #7
            //   455: astore #19
            //   457: aload #17
            //   459: astore #18
            //   461: aload #7
            //   463: astore #20
            //   465: aload #17
            //   467: astore #21
            //   469: aload #23
            //   471: iconst_4
            //   472: iconst_m1
            //   473: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   476: aastore
            //   477: aload #7
            //   479: astore #19
            //   481: aload #17
            //   483: astore #18
            //   485: aload #7
            //   487: astore #20
            //   489: aload #17
            //   491: astore #21
            //   493: aload #23
            //   495: iconst_5
            //   496: iconst_m1
            //   497: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   500: aastore
            //   501: aload #7
            //   503: astore #19
            //   505: aload #17
            //   507: astore #18
            //   509: aload #7
            //   511: astore #20
            //   513: aload #17
            //   515: astore #21
            //   517: aload #23
            //   519: bipush #6
            //   521: iconst_m1
            //   522: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   525: aastore
            //   526: aload #7
            //   528: astore #19
            //   530: aload #17
            //   532: astore #18
            //   534: aload #7
            //   536: astore #20
            //   538: aload #17
            //   540: astore #21
            //   542: iconst_0
            //   543: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   546: astore #22
            //   548: aload_1
            //   549: astore #16
            //   551: aload #17
            //   553: astore_1
            //   554: aload #23
            //   556: astore #17
            //   558: aload #7
            //   560: astore #19
            //   562: aload_1
            //   563: astore #18
            //   565: aload #7
            //   567: astore #20
            //   569: aload_1
            //   570: astore #21
            //   572: aload_1
            //   573: invokevirtual readLine : ()Ljava/lang/String;
            //   576: astore #25
            //   578: aload #25
            //   580: ifnull -> 1523
            //   583: aload #7
            //   585: astore #19
            //   587: aload_1
            //   588: astore #18
            //   590: aload #7
            //   592: astore #20
            //   594: aload_1
            //   595: astore #21
            //   597: aload #22
            //   599: invokevirtual intValue : ()I
            //   602: istore_3
            //   603: iload_3
            //   604: ifne -> 660
            //   607: aload #7
            //   609: astore #18
            //   611: aload_1
            //   612: astore #19
            //   614: aload_0
            //   615: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   618: aload #25
            //   620: invokestatic access$4600 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/String;)[Ljava/lang/Integer;
            //   623: astore #17
            //   625: aload #7
            //   627: astore #18
            //   629: aload_1
            //   630: astore #19
            //   632: aload #17
            //   634: iconst_0
            //   635: aaload
            //   636: invokevirtual intValue : ()I
            //   639: istore_3
            //   640: iload_3
            //   641: iconst_m1
            //   642: if_icmpne -> 660
            //   645: goto -> 1523
            //   648: astore #8
            //   650: aload #7
            //   652: astore #20
            //   654: aload_1
            //   655: astore #7
            //   657: goto -> 1557
            //   660: aload #7
            //   662: astore #19
            //   664: aload_1
            //   665: astore #18
            //   667: aload #7
            //   669: astore #20
            //   671: aload_1
            //   672: astore #21
            //   674: aload #22
            //   676: invokevirtual intValue : ()I
            //   679: istore_3
            //   680: iload_3
            //   681: ifle -> 1272
            //   684: aload #7
            //   686: astore #18
            //   688: aload_1
            //   689: astore #19
            //   691: iconst_0
            //   692: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   695: astore #23
            //   697: aload #7
            //   699: astore #18
            //   701: aload_1
            //   702: astore #19
            //   704: aload #25
            //   706: invokevirtual length : ()I
            //   709: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   712: astore #15
            //   714: aload #7
            //   716: astore #18
            //   718: aload_1
            //   719: astore #19
            //   721: dconst_0
            //   722: invokestatic valueOf : (D)Ljava/lang/Double;
            //   725: astore #21
            //   727: aload #7
            //   729: astore #18
            //   731: aload_1
            //   732: astore #19
            //   734: dconst_0
            //   735: invokestatic valueOf : (D)Ljava/lang/Double;
            //   738: astore #14
            //   740: aload #7
            //   742: astore #18
            //   744: aload_1
            //   745: astore #19
            //   747: dconst_0
            //   748: invokestatic valueOf : (D)Ljava/lang/Double;
            //   751: astore #11
            //   753: aload #7
            //   755: astore #18
            //   757: aload_1
            //   758: astore #19
            //   760: dconst_0
            //   761: invokestatic valueOf : (D)Ljava/lang/Double;
            //   764: astore #13
            //   766: aload #7
            //   768: astore #18
            //   770: aload_1
            //   771: astore #19
            //   773: dconst_0
            //   774: invokestatic valueOf : (D)Ljava/lang/Double;
            //   777: astore #10
            //   779: aload #7
            //   781: astore #18
            //   783: aload_1
            //   784: astore #19
            //   786: dconst_0
            //   787: invokestatic valueOf : (D)Ljava/lang/Double;
            //   790: astore #12
            //   792: aload #7
            //   794: astore #18
            //   796: aload_1
            //   797: astore #19
            //   799: lconst_0
            //   800: invokestatic valueOf : (J)Ljava/lang/Long;
            //   803: astore #20
            //   805: iconst_0
            //   806: istore_3
            //   807: aload #8
            //   809: astore #9
            //   811: aload #21
            //   813: astore #8
            //   815: aload #7
            //   817: astore #18
            //   819: aload_1
            //   820: astore #19
            //   822: aload #23
            //   824: invokevirtual intValue : ()I
            //   827: aload #15
            //   829: invokevirtual intValue : ()I
            //   832: if_icmpge -> 1237
            //   835: iload_3
            //   836: bipush #7
            //   838: if_icmpge -> 1237
            //   841: aload #7
            //   843: astore #18
            //   845: aload_1
            //   846: astore #19
            //   848: aload #25
            //   850: aload #9
            //   852: aload #23
            //   854: invokevirtual intValue : ()I
            //   857: invokevirtual indexOf : (Ljava/lang/String;I)I
            //   860: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   863: astore #24
            //   865: aload #24
            //   867: astore #21
            //   869: aload #7
            //   871: astore #18
            //   873: aload_1
            //   874: astore #19
            //   876: aload #24
            //   878: invokevirtual intValue : ()I
            //   881: ifge -> 888
            //   884: aload #15
            //   886: astore #21
            //   888: aload #7
            //   890: astore #18
            //   892: aload_1
            //   893: astore #19
            //   895: aload #25
            //   897: aload #23
            //   899: invokevirtual intValue : ()I
            //   902: aload #21
            //   904: invokevirtual intValue : ()I
            //   907: invokevirtual substring : (II)Ljava/lang/String;
            //   910: astore #23
            //   912: aload #7
            //   914: astore #18
            //   916: aload_1
            //   917: astore #19
            //   919: aload #17
            //   921: iload_3
            //   922: aaload
            //   923: invokevirtual intValue : ()I
            //   926: istore #4
            //   928: iload #4
            //   930: tableswitch default -> 972, 0 -> 1166, 1 -> 1133, 2 -> 1100, 3 -> 1067, 4 -> 1034, 5 -> 1001, 6 -> 975
            //   972: goto -> 1211
            //   975: aload #7
            //   977: astore #18
            //   979: aload_1
            //   980: astore #19
            //   982: aload #23
            //   984: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   987: astore #8
            //   989: goto -> 972
            //   992: dconst_0
            //   993: invokestatic valueOf : (D)Ljava/lang/Double;
            //   996: astore #8
            //   998: goto -> 1211
            //   1001: aload #7
            //   1003: astore #18
            //   1005: aload_1
            //   1006: astore #19
            //   1008: aload #23
            //   1010: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   1013: astore #14
            //   1015: goto -> 1211
            //   1018: aload #7
            //   1020: astore #18
            //   1022: aload_1
            //   1023: astore #19
            //   1025: dconst_0
            //   1026: invokestatic valueOf : (D)Ljava/lang/Double;
            //   1029: astore #14
            //   1031: goto -> 1015
            //   1034: aload #7
            //   1036: astore #18
            //   1038: aload_1
            //   1039: astore #19
            //   1041: aload #23
            //   1043: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   1046: astore #11
            //   1048: goto -> 1211
            //   1051: aload #7
            //   1053: astore #18
            //   1055: aload_1
            //   1056: astore #19
            //   1058: dconst_0
            //   1059: invokestatic valueOf : (D)Ljava/lang/Double;
            //   1062: astore #11
            //   1064: goto -> 1048
            //   1067: aload #7
            //   1069: astore #18
            //   1071: aload_1
            //   1072: astore #19
            //   1074: aload #23
            //   1076: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   1079: astore #13
            //   1081: goto -> 1211
            //   1084: aload #7
            //   1086: astore #18
            //   1088: aload_1
            //   1089: astore #19
            //   1091: dconst_0
            //   1092: invokestatic valueOf : (D)Ljava/lang/Double;
            //   1095: astore #13
            //   1097: goto -> 1081
            //   1100: aload #7
            //   1102: astore #18
            //   1104: aload_1
            //   1105: astore #19
            //   1107: aload #23
            //   1109: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   1112: astore #10
            //   1114: goto -> 1211
            //   1117: aload #7
            //   1119: astore #18
            //   1121: aload_1
            //   1122: astore #19
            //   1124: dconst_0
            //   1125: invokestatic valueOf : (D)Ljava/lang/Double;
            //   1128: astore #10
            //   1130: goto -> 1114
            //   1133: aload #7
            //   1135: astore #18
            //   1137: aload_1
            //   1138: astore #19
            //   1140: aload #23
            //   1142: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
            //   1145: astore #12
            //   1147: goto -> 1211
            //   1150: aload #7
            //   1152: astore #18
            //   1154: aload_1
            //   1155: astore #19
            //   1157: dconst_0
            //   1158: invokestatic valueOf : (D)Ljava/lang/Double;
            //   1161: astore #12
            //   1163: goto -> 1147
            //   1166: aload #7
            //   1168: astore #18
            //   1170: aload_1
            //   1171: astore #19
            //   1173: aload #23
            //   1175: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Long;
            //   1178: astore #20
            //   1180: aload #20
            //   1182: astore #18
            //   1184: aload #18
            //   1186: astore #20
            //   1188: goto -> 1211
            //   1191: aload #7
            //   1193: astore #18
            //   1195: aload_1
            //   1196: astore #19
            //   1198: lconst_0
            //   1199: invokestatic valueOf : (J)Ljava/lang/Long;
            //   1202: astore #20
            //   1204: aload #20
            //   1206: astore #18
            //   1208: goto -> 1184
            //   1211: iload_3
            //   1212: iconst_1
            //   1213: iadd
            //   1214: istore_3
            //   1215: aload #7
            //   1217: astore #18
            //   1219: aload_1
            //   1220: astore #19
            //   1222: aload #21
            //   1224: invokevirtual intValue : ()I
            //   1227: iconst_1
            //   1228: iadd
            //   1229: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1232: astore #23
            //   1234: goto -> 815
            //   1237: aload #14
            //   1239: astore #18
            //   1241: aload #8
            //   1243: astore #19
            //   1245: aload #20
            //   1247: astore #8
            //   1249: aload #12
            //   1251: astore #15
            //   1253: aload #10
            //   1255: astore #14
            //   1257: aload #11
            //   1259: astore #12
            //   1261: aload #18
            //   1263: astore #11
            //   1265: aload #19
            //   1267: astore #10
            //   1269: goto -> 1324
            //   1272: aload #8
            //   1274: astore #18
            //   1276: aload #15
            //   1278: astore #8
            //   1280: aload #11
            //   1282: astore #15
            //   1284: aload #10
            //   1286: astore #19
            //   1288: aload #9
            //   1290: astore #20
            //   1292: aload #14
            //   1294: astore #21
            //   1296: aload #18
            //   1298: astore #9
            //   1300: aload #13
            //   1302: astore #10
            //   1304: aload #12
            //   1306: astore #11
            //   1308: aload #15
            //   1310: astore #12
            //   1312: aload #19
            //   1314: astore #13
            //   1316: aload #20
            //   1318: astore #14
            //   1320: aload #21
            //   1322: astore #15
            //   1324: aload #7
            //   1326: astore #19
            //   1328: aload_1
            //   1329: astore #18
            //   1331: aload #7
            //   1333: astore #20
            //   1335: aload_1
            //   1336: astore #21
            //   1338: aload #8
            //   1340: invokevirtual longValue : ()J
            //   1343: lconst_0
            //   1344: lcmp
            //   1345: ifle -> 1891
            //   1348: aload #7
            //   1350: astore #19
            //   1352: aload_1
            //   1353: astore #18
            //   1355: aload #7
            //   1357: astore #20
            //   1359: aload_1
            //   1360: astore #21
            //   1362: aload_0
            //   1363: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   1366: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   1369: astore #23
            //   1371: aload #7
            //   1373: astore #19
            //   1375: aload_1
            //   1376: astore #18
            //   1378: aload #7
            //   1380: astore #20
            //   1382: aload_1
            //   1383: astore #21
            //   1385: aload_0
            //   1386: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   1389: astore #24
            //   1391: aload #23
            //   1393: new com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   1396: dup
            //   1397: aload #24
            //   1399: aload #8
            //   1401: iload_2
            //   1402: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1405: aload #16
            //   1407: aload #15
            //   1409: aload #14
            //   1411: aload #13
            //   1413: aload #12
            //   1415: aload #11
            //   1417: aload #10
            //   1419: aconst_null
            //   1420: invokespecial <init> : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //   1423: invokevirtual add : (Ljava/lang/Object;)Z
            //   1426: pop
            //   1427: goto -> 1430
            //   1430: aload #22
            //   1432: invokevirtual intValue : ()I
            //   1435: iconst_1
            //   1436: iadd
            //   1437: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1440: astore #22
            //   1442: aload #14
            //   1444: astore #18
            //   1446: aload #11
            //   1448: astore #19
            //   1450: aload #10
            //   1452: astore #20
            //   1454: aload #15
            //   1456: astore #14
            //   1458: aload #8
            //   1460: astore #15
            //   1462: aload #9
            //   1464: astore #8
            //   1466: aload #18
            //   1468: astore #9
            //   1470: aload #13
            //   1472: astore #10
            //   1474: aload #12
            //   1476: astore #11
            //   1478: aload #19
            //   1480: astore #12
            //   1482: aload #20
            //   1484: astore #13
            //   1486: goto -> 558
            //   1489: astore #8
            //   1491: aload_1
            //   1492: astore #9
            //   1494: aload #8
            //   1496: astore_1
            //   1497: aload #7
            //   1499: astore #19
            //   1501: aload #9
            //   1503: astore #7
            //   1505: goto -> 1746
            //   1508: astore #8
            //   1510: aload_1
            //   1511: astore #9
            //   1513: aload #7
            //   1515: astore_1
            //   1516: aload #9
            //   1518: astore #7
            //   1520: goto -> 1659
            //   1523: lconst_0
            //   1524: lstore #5
            //   1526: aload_1
            //   1527: astore #17
            //   1529: aload #7
            //   1531: astore #18
            //   1533: aload #8
            //   1535: astore #7
            //   1537: aload #16
            //   1539: astore_1
            //   1540: goto -> 1574
            //   1543: astore #7
            //   1545: aload #18
            //   1547: astore_1
            //   1548: goto -> 1736
            //   1551: astore #8
            //   1553: aload #21
            //   1555: astore #7
            //   1557: aload #20
            //   1559: astore_1
            //   1560: goto -> 1659
            //   1563: aload #7
            //   1565: astore #18
            //   1567: aconst_null
            //   1568: astore #17
            //   1570: aload #8
            //   1572: astore #7
            //   1574: aload #18
            //   1576: ifnull -> 1584
            //   1579: aload #18
            //   1581: invokevirtual disconnect : ()V
            //   1584: aload #17
            //   1586: ifnull -> 1611
            //   1589: aload #17
            //   1591: invokevirtual close : ()V
            //   1594: goto -> 1611
            //   1597: astore #8
            //   1599: aload_0
            //   1600: getfield LOG_TAG : Ljava/lang/String;
            //   1603: ldc 'Error closing stream'
            //   1605: aload #8
            //   1607: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1610: pop
            //   1611: iload_2
            //   1612: iconst_1
            //   1613: iadd
            //   1614: istore_2
            //   1615: aload #7
            //   1617: astore #8
            //   1619: goto -> 65
            //   1622: astore_1
            //   1623: goto -> 1638
            //   1626: astore #8
            //   1628: aload #7
            //   1630: astore_1
            //   1631: goto -> 1656
            //   1634: astore_1
            //   1635: aconst_null
            //   1636: astore #7
            //   1638: aconst_null
            //   1639: astore #8
            //   1641: aload #7
            //   1643: astore #19
            //   1645: aload #8
            //   1647: astore #7
            //   1649: goto -> 1746
            //   1652: astore #8
            //   1654: aconst_null
            //   1655: astore_1
            //   1656: aconst_null
            //   1657: astore #7
            //   1659: aload_1
            //   1660: astore #18
            //   1662: aload #7
            //   1664: astore #19
            //   1666: aload_0
            //   1667: getfield LOG_TAG : Ljava/lang/String;
            //   1670: ldc 'Error '
            //   1672: aload #8
            //   1674: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1677: pop
            //   1678: aload_1
            //   1679: astore #18
            //   1681: aload #7
            //   1683: astore #19
            //   1685: iconst_m1
            //   1686: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1689: astore #8
            //   1691: aload_1
            //   1692: ifnull -> 1699
            //   1695: aload_1
            //   1696: invokevirtual disconnect : ()V
            //   1699: aload #7
            //   1701: ifnull -> 1724
            //   1704: aload #7
            //   1706: invokevirtual close : ()V
            //   1709: aload #8
            //   1711: areturn
            //   1712: astore_1
            //   1713: aload_0
            //   1714: getfield LOG_TAG : Ljava/lang/String;
            //   1717: ldc 'Error closing stream'
            //   1719: aload_1
            //   1720: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1723: pop
            //   1724: aload #8
            //   1726: areturn
            //   1727: astore #7
            //   1729: aload #19
            //   1731: astore_1
            //   1732: aload #18
            //   1734: astore #19
            //   1736: aload #7
            //   1738: astore #8
            //   1740: aload_1
            //   1741: astore #7
            //   1743: aload #8
            //   1745: astore_1
            //   1746: aload #19
            //   1748: ifnull -> 1756
            //   1751: aload #19
            //   1753: invokevirtual disconnect : ()V
            //   1756: aload #7
            //   1758: ifnull -> 1783
            //   1761: aload #7
            //   1763: invokevirtual close : ()V
            //   1766: goto -> 1783
            //   1769: astore #7
            //   1771: aload_0
            //   1772: getfield LOG_TAG : Ljava/lang/String;
            //   1775: ldc 'Error closing stream'
            //   1777: aload #7
            //   1779: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   1782: pop
            //   1783: aload_1
            //   1784: athrow
            //   1785: aload_0
            //   1786: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   1789: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   1792: invokevirtual size : ()I
            //   1795: iconst_1
            //   1796: if_icmpge -> 1851
            //   1799: aload_0
            //   1800: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   1803: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   1806: new com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   1809: dup
            //   1810: aload_0
            //   1811: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   1814: ldc2_w 1512514800
            //   1817: invokestatic valueOf : (J)Ljava/lang/Long;
            //   1820: iconst_0
            //   1821: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1824: aload_1
            //   1825: aload #14
            //   1827: aload #9
            //   1829: aload #10
            //   1831: aload #11
            //   1833: aload #12
            //   1835: aload #13
            //   1837: aconst_null
            //   1838: invokespecial <init> : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //   1841: invokevirtual add : (Ljava/lang/Object;)Z
            //   1844: pop
            //   1845: bipush #-2
            //   1847: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1850: areturn
            //   1851: iconst_1
            //   1852: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   1855: areturn
            //   1856: astore #8
            //   1858: goto -> 992
            //   1861: astore #14
            //   1863: goto -> 1018
            //   1866: astore #11
            //   1868: goto -> 1051
            //   1871: astore #13
            //   1873: goto -> 1084
            //   1876: astore #10
            //   1878: goto -> 1117
            //   1881: astore #12
            //   1883: goto -> 1150
            //   1886: astore #18
            //   1888: goto -> 1191
            //   1891: goto -> 1430
            // Exception table:
            //   from	to	target	type
            //   275	292	1652	java/io/IOException
            //   275	292	1634	finally
            //   292	311	1626	java/io/IOException
            //   292	311	1622	finally
            //   316	334	1626	java/io/IOException
            //   316	334	1622	finally
            //   350	357	1551	java/io/IOException
            //   350	357	1543	finally
            //   373	381	1551	java/io/IOException
            //   373	381	1543	finally
            //   397	405	1551	java/io/IOException
            //   397	405	1543	finally
            //   421	429	1551	java/io/IOException
            //   421	429	1543	finally
            //   445	453	1551	java/io/IOException
            //   445	453	1543	finally
            //   469	477	1551	java/io/IOException
            //   469	477	1543	finally
            //   493	501	1551	java/io/IOException
            //   493	501	1543	finally
            //   517	526	1551	java/io/IOException
            //   517	526	1543	finally
            //   542	548	1551	java/io/IOException
            //   542	548	1543	finally
            //   572	578	1551	java/io/IOException
            //   572	578	1543	finally
            //   597	603	1551	java/io/IOException
            //   597	603	1543	finally
            //   614	625	648	java/io/IOException
            //   614	625	1727	finally
            //   632	640	648	java/io/IOException
            //   632	640	1727	finally
            //   674	680	1551	java/io/IOException
            //   674	680	1543	finally
            //   691	697	648	java/io/IOException
            //   691	697	1727	finally
            //   704	714	648	java/io/IOException
            //   704	714	1727	finally
            //   721	727	648	java/io/IOException
            //   721	727	1727	finally
            //   734	740	648	java/io/IOException
            //   734	740	1727	finally
            //   747	753	648	java/io/IOException
            //   747	753	1727	finally
            //   760	766	648	java/io/IOException
            //   760	766	1727	finally
            //   773	779	648	java/io/IOException
            //   773	779	1727	finally
            //   786	792	648	java/io/IOException
            //   786	792	1727	finally
            //   799	805	648	java/io/IOException
            //   799	805	1727	finally
            //   822	835	648	java/io/IOException
            //   822	835	1727	finally
            //   848	865	648	java/io/IOException
            //   848	865	1727	finally
            //   876	884	648	java/io/IOException
            //   876	884	1727	finally
            //   895	912	648	java/io/IOException
            //   895	912	1727	finally
            //   919	928	648	java/io/IOException
            //   919	928	1727	finally
            //   982	989	1856	java/lang/NumberFormatException
            //   982	989	648	java/io/IOException
            //   982	989	1727	finally
            //   1008	1015	1861	java/lang/NumberFormatException
            //   1008	1015	648	java/io/IOException
            //   1008	1015	1727	finally
            //   1025	1031	648	java/io/IOException
            //   1025	1031	1727	finally
            //   1041	1048	1866	java/lang/NumberFormatException
            //   1041	1048	648	java/io/IOException
            //   1041	1048	1727	finally
            //   1058	1064	648	java/io/IOException
            //   1058	1064	1727	finally
            //   1074	1081	1871	java/lang/NumberFormatException
            //   1074	1081	648	java/io/IOException
            //   1074	1081	1727	finally
            //   1091	1097	648	java/io/IOException
            //   1091	1097	1727	finally
            //   1107	1114	1876	java/lang/NumberFormatException
            //   1107	1114	648	java/io/IOException
            //   1107	1114	1727	finally
            //   1124	1130	648	java/io/IOException
            //   1124	1130	1727	finally
            //   1140	1147	1881	java/lang/NumberFormatException
            //   1140	1147	648	java/io/IOException
            //   1140	1147	1727	finally
            //   1157	1163	648	java/io/IOException
            //   1157	1163	1727	finally
            //   1173	1180	1886	java/lang/NumberFormatException
            //   1173	1180	648	java/io/IOException
            //   1173	1180	1727	finally
            //   1198	1204	648	java/io/IOException
            //   1198	1204	1727	finally
            //   1222	1234	648	java/io/IOException
            //   1222	1234	1727	finally
            //   1338	1348	1551	java/io/IOException
            //   1338	1348	1543	finally
            //   1362	1371	1551	java/io/IOException
            //   1362	1371	1543	finally
            //   1385	1391	1551	java/io/IOException
            //   1385	1391	1543	finally
            //   1391	1427	1508	java/io/IOException
            //   1391	1427	1489	finally
            //   1430	1442	1508	java/io/IOException
            //   1430	1442	1489	finally
            //   1589	1594	1597	java/io/IOException
            //   1666	1678	1727	finally
            //   1685	1691	1727	finally
            //   1704	1709	1712	java/io/IOException
            //   1761	1766	1769	java/io/IOException
        }

        protected void onPostExecute(Integer param1Integer) {
            // Byte code:
            //   0: aload_1
            //   1: invokevirtual intValue : ()I
            //   4: iconst_1
            //   5: if_icmpge -> 75
            //   8: aload_1
            //   9: invokevirtual intValue : ()I
            //   12: bipush #-2
            //   14: if_icmpne -> 46
            //   17: aload_0
            //   18: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   21: invokevirtual getBaseContext : ()Landroid/content/Context;
            //   24: aload_0
            //   25: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   28: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   31: ldc 2131427370
            //   33: invokevirtual getString : (I)Ljava/lang/String;
            //   36: iconst_0
            //   37: invokestatic makeText : (Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
            //   40: invokevirtual show : ()V
            //   43: goto -> 100
            //   46: aload_0
            //   47: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   50: invokevirtual getBaseContext : ()Landroid/content/Context;
            //   53: aload_0
            //   54: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   57: invokevirtual getResources : ()Landroid/content/res/Resources;
            //   60: ldc 2131427361
            //   62: invokevirtual getString : (I)Ljava/lang/String;
            //   65: iconst_0
            //   66: invokestatic makeText : (Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
            //   69: invokevirtual show : ()V
            //   72: goto -> 100
            //   75: aload_0
            //   76: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   79: aload_0
            //   80: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   83: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   86: iconst_0
            //   87: invokevirtual get : (I)Ljava/lang/Object;
            //   90: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   93: invokestatic access$3400 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Long;
            //   96: invokestatic access$4802 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;)Ljava/lang/Long;
            //   99: pop
            //   100: aload_0
            //   101: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   104: iconst_0
            //   105: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   108: invokestatic access$902 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Integer;)Ljava/lang/Integer;
            //   111: pop
            //   112: aload_0
            //   113: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   116: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   119: invokevirtual size : ()I
            //   122: istore #4
            //   124: iconst_0
            //   125: istore_2
            //   126: iload_2
            //   127: iload #4
            //   129: if_icmpge -> 318
            //   132: aload_0
            //   133: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   136: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   139: iload_2
            //   140: invokevirtual get : (I)Ljava/lang/Object;
            //   143: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   146: invokestatic access$3500 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
            //   149: invokevirtual doubleValue : ()D
            //   152: ldc2_w 15.0
            //   155: dcmpl
            //   156: ifgt -> 296
            //   159: aload_0
            //   160: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   163: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   166: iload_2
            //   167: invokevirtual get : (I)Ljava/lang/Object;
            //   170: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   173: invokestatic access$3600 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
            //   176: invokevirtual doubleValue : ()D
            //   179: ldc2_w 15.0
            //   182: dcmpl
            //   183: ifgt -> 296
            //   186: aload_0
            //   187: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   190: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   193: iload_2
            //   194: invokevirtual get : (I)Ljava/lang/Object;
            //   197: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   200: invokestatic access$3900 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
            //   203: invokevirtual doubleValue : ()D
            //   206: ldc2_w 15.0
            //   209: dcmpl
            //   210: ifgt -> 296
            //   213: aload_0
            //   214: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   217: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   220: iload_2
            //   221: invokevirtual get : (I)Ljava/lang/Object;
            //   224: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   227: invokestatic access$4000 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
            //   230: invokevirtual doubleValue : ()D
            //   233: ldc2_w 15.0
            //   236: dcmpl
            //   237: ifgt -> 296
            //   240: aload_0
            //   241: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   244: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   247: iload_2
            //   248: invokevirtual get : (I)Ljava/lang/Object;
            //   251: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   254: invokestatic access$3700 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
            //   257: invokevirtual doubleValue : ()D
            //   260: ldc2_w 15.0
            //   263: dcmpl
            //   264: ifgt -> 296
            //   267: iload_2
            //   268: istore_3
            //   269: aload_0
            //   270: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   273: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   276: iload_2
            //   277: invokevirtual get : (I)Ljava/lang/Object;
            //   280: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   283: invokestatic access$3800 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)Ljava/lang/Double;
            //   286: invokevirtual doubleValue : ()D
            //   289: ldc2_w 15.0
            //   292: dcmpl
            //   293: ifle -> 311
            //   296: aload_0
            //   297: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   300: iconst_1
            //   301: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   304: invokestatic access$902 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Integer;)Ljava/lang/Integer;
            //   307: pop
            //   308: iload #4
            //   310: istore_3
            //   311: iload_3
            //   312: iconst_1
            //   313: iadd
            //   314: istore_2
            //   315: goto -> 126
            //   318: aload_0
            //   319: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   322: ldc_w 2131165327
            //   325: invokevirtual findViewById : (I)Landroid/view/View;
            //   328: checkcast com/erwinvoogt/soarcast/windKaderView
            //   331: aload_0
            //   332: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   335: invokestatic access$700 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/lang/Integer;
            //   338: invokevirtual intValue : ()I
            //   341: aload_0
            //   342: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   345: invokestatic access$900 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/lang/Integer;
            //   348: invokevirtual intValue : ()I
            //   351: aload_0
            //   352: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   355: invokestatic access$1000 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/lang/Integer;
            //   358: invokevirtual intValue : ()I
            //   361: aload_0
            //   362: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   365: invokestatic access$1100 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   368: iconst_0
            //   369: invokevirtual get : (I)Ljava/lang/Object;
            //   372: checkcast com/erwinvoogt/soarcast/WelkomSoarCast$Wind
            //   375: invokestatic access$1200 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;)[Ljava/lang/Integer;
            //   378: invokevirtual update : (III[Ljava/lang/Integer;)V
            //   381: aload_0
            //   382: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   385: invokestatic access$2500 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)V
            //   388: aload_0
            //   389: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   392: invokestatic access$500 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)V
            //   395: aload_0
            //   396: getfield this$0 : Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   399: invokestatic access$4900 : (Lcom/erwinvoogt/soarcast/WelkomSoarCast;)V
            //   402: return
        }
    }

    private class Locatie {
        private Integer id;

        private Double lat;

        private Double lon;

        private Integer maxdeg;

        private Integer mindeg;

        private String naam;

        private Locatie(String param1String, Integer param1Integer1, Double param1Double1, Double param1Double2, Integer param1Integer2, Integer param1Integer3) {
            this.naam = param1String;
            this.id = param1Integer1;
            this.lat = param1Double1;
            this.lon = param1Double2;
            this.mindeg = param1Integer2;
            this.maxdeg = param1Integer3;
        }
    }

    private class Richting {
        private Integer dag;

        private Integer locatieID;

        private Double richtingGFS;

        private Double richtingHarmonie;

        private Double richtingMeting;

        private Long unixTimestamp;

        private Richting(Long param1Long, Integer param1Integer1, Integer param1Integer2, Double param1Double1, Double param1Double2, Double param1Double3) {
            this.unixTimestamp = param1Long;
            this.dag = param1Integer1;
            this.locatieID = param1Integer2;
            this.richtingMeting = param1Double1;
            this.richtingHarmonie = param1Double2;
            this.richtingGFS = param1Double3;
        }

        private Integer[] geefZonOpOnder() {
            Date date = new Date(this.unixTimestamp.longValue() * 1000L);
            String str = (new SimpleDateFormat("MM")).format(date);
            try {
                integer1 = Integer.valueOf(Integer.parseInt(str));
            } catch (NumberFormatException numberFormatException) {
                integer1 = Integer.valueOf(0);
            }
            switch (integer1.intValue()) {
                default:
                    integer1 = Integer.valueOf(8);
                    integer2 = Integer.valueOf(16);
                    return new Integer[] { integer1, integer2 };
                case 12:
                    integer1 = Integer.valueOf(8);
                    integer2 = Integer.valueOf(16);
                    return new Integer[] { integer1, integer2 };
                case 11:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(17);
                    return new Integer[] { integer1, integer2 };
                case 10:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(19);
                    return new Integer[] { integer1, integer2 };
                case 9:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(20);
                    return new Integer[] { integer1, integer2 };
                case 8:
                    integer1 = Integer.valueOf(6);
                    integer2 = Integer.valueOf(21);
                    return new Integer[] { integer1, integer2 };
                case 7:
                    integer1 = Integer.valueOf(5);
                    integer2 = Integer.valueOf(22);
                    return new Integer[] { integer1, integer2 };
                case 6:
                    integer1 = Integer.valueOf(5);
                    integer2 = Integer.valueOf(22);
                    return new Integer[] { integer1, integer2 };
                case 5:
                    integer1 = Integer.valueOf(6);
                    integer2 = Integer.valueOf(21);
                    return new Integer[] { integer1, integer2 };
                case 4:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(20);
                    return new Integer[] { integer1, integer2 };
                case 3:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(18);
                    return new Integer[] { integer1, integer2 };
                case 2:
                    integer1 = Integer.valueOf(8);
                    integer2 = Integer.valueOf(17);
                    return new Integer[] { integer1, integer2 };
                case 1:
                    break;
            }
            Integer integer1 = Integer.valueOf(8);
            Integer integer2 = Integer.valueOf(16);
            return new Integer[] { integer1, integer2 };
        }
    }

    private class Wind {
        private Integer dag;

        private Integer locatieID;

        private Double snelheidGFS;

        private Double snelheidHarmonie;

        private Double snelheidMeting;

        private Long unixTimestamp;

        private Double vlaagGFS;

        private Double vlaagHarmonie;

        private Double vlaagMeting;

        private Wind(Long param1Long, Integer param1Integer1, Integer param1Integer2, Double param1Double1, Double param1Double2, Double param1Double3, Double param1Double4, Double param1Double5, Double param1Double6) {
            this.unixTimestamp = param1Long;
            this.dag = param1Integer1;
            this.locatieID = param1Integer2;
            this.snelheidMeting = param1Double1;
            this.vlaagMeting = param1Double2;
            this.snelheidHarmonie = param1Double3;
            this.vlaagHarmonie = param1Double4;
            this.snelheidGFS = param1Double5;
            this.vlaagGFS = param1Double6;
        }

        private Integer[] geefZonOpOnder() {
            Date date = new Date(this.unixTimestamp.longValue() * 1000L);
            String str = (new SimpleDateFormat("MM")).format(date);
            try {
                integer1 = Integer.valueOf(Integer.parseInt(str));
            } catch (NumberFormatException numberFormatException) {
                integer1 = Integer.valueOf(0);
            }
            switch (integer1.intValue()) {
                default:
                    integer1 = Integer.valueOf(8);
                    integer2 = Integer.valueOf(16);
                    return new Integer[] { integer1, integer2 };
                case 12:
                    integer1 = Integer.valueOf(8);
                    integer2 = Integer.valueOf(16);
                    return new Integer[] { integer1, integer2 };
                case 11:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(17);
                    return new Integer[] { integer1, integer2 };
                case 10:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(19);
                    return new Integer[] { integer1, integer2 };
                case 9:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(20);
                    return new Integer[] { integer1, integer2 };
                case 8:
                    integer1 = Integer.valueOf(6);
                    integer2 = Integer.valueOf(21);
                    return new Integer[] { integer1, integer2 };
                case 7:
                    integer1 = Integer.valueOf(5);
                    integer2 = Integer.valueOf(22);
                    return new Integer[] { integer1, integer2 };
                case 6:
                    integer1 = Integer.valueOf(5);
                    integer2 = Integer.valueOf(22);
                    return new Integer[] { integer1, integer2 };
                case 5:
                    integer1 = Integer.valueOf(6);
                    integer2 = Integer.valueOf(21);
                    return new Integer[] { integer1, integer2 };
                case 4:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(20);
                    return new Integer[] { integer1, integer2 };
                case 3:
                    integer1 = Integer.valueOf(7);
                    integer2 = Integer.valueOf(18);
                    return new Integer[] { integer1, integer2 };
                case 2:
                    integer1 = Integer.valueOf(8);
                    integer2 = Integer.valueOf(17);
                    return new Integer[] { integer1, integer2 };
                case 1:
                    break;
            }
            Integer integer1 = Integer.valueOf(8);
            Integer integer2 = Integer.valueOf(16);
            return new Integer[] { integer1, integer2 };
        }
    }
}
