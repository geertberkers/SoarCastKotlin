//package geert.berkers.soarcast;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * Created by Zorgkluis (Geert Berkers)
// */
//public class MainActivity extends Activity {
//    private String bestand;
//
//    private String gebruiker;
//
//    private Boolean loginOK;
//
//    private Boolean sponsor1OK;
//
//    private Boolean sponsor2OK;
//
//    private void startSoarCast() {
//        Context context = getApplicationContext();
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
//        editor.putBoolean("sponsor1", this.sponsor1OK);
//        editor.putBoolean("sponsor2", this.sponsor2OK);
//        editor.apply();
//        startActivity((new Intent(context, WelkomSoarCast.class)).putExtra("bestand", this.bestand));
//    }
//
//    protected void onCreate(Bundle paramBundle) {
//        super.onCreate(paramBundle);
//        this.gebruiker = getResources().getString(R.string.login_id);
//        this.loginOK = Boolean.valueOf(false);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
//        this.sponsor1OK = Boolean.valueOf(sharedPreferences.getBoolean("sponsor1", false));
//        this.sponsor2OK = Boolean.valueOf(sharedPreferences.getBoolean("sponsor2", false));
//        if (paramBundle == null) {
//            setContentView(R.layout.activity);
//            ImageView imageView = (ImageView)findViewById(2131165301);
//            if (this.sponsor1OK.booleanValue()) {
//                imageView.setVisibility(0);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View param1View) {
//                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(MainActivity.this.getResources().getString(2131427390)));
//                        MainActivity.this.startActivity(intent);
//                    }
//                });
//            } else {
//                imageView.setVisibility(4);
//            }
//            imageView = (ImageView)findViewById(2131165302);
//            if (this.sponsor2OK.booleanValue()) {
//                imageView.setVisibility(0);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View param1View) {
//                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(MainActivity.this.getResources().getString(2131427391)));
//                        MainActivity.this.startActivity(intent);
//                    }
//                });
//            } else {
//                imageView.setVisibility(4);
//            }
//            final TextView ei1 = (TextView)findViewById(2131165265);
//            final TextView ei2 = (TextView)findViewById(2131165266);
//            textView1.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View param1View) {
//                    MainActivity.access$002(MainActivity.this, Boolean.valueOf(false));
//                    if (!MainActivity.this.gebruiker.equals(MainActivity.this.getResources().getString(2131427376))) {
//                        MainActivity.access$102(MainActivity.this, MainActivity.this.getResources().getString(2131427376));
//                        ei1.setBackgroundColor(552599552);
//                        ei2.setBackgroundColor(0);
//                    } else {
//                        MainActivity.access$102(MainActivity.this, MainActivity.this.getResources().getString(2131427367));
//                        ei1.setBackgroundColor(0);
//                    }
//                    (new MainActivity.CheckGebruiker()).execute((Object[])new String[] { MainActivity.access$100(this.this$0) });
//                }
//            });
//            textView2.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View param1View) {
//                    MainActivity.access$002(MainActivity.this, Boolean.valueOf(false));
//                    if (!MainActivity.this.gebruiker.equals(MainActivity.this.getResources().getString(2131427377))) {
//                        MainActivity.access$102(MainActivity.this, MainActivity.this.getResources().getString(2131427377));
//                        ei2.setBackgroundColor(552599552);
//                        ei1.setBackgroundColor(0);
//                    } else {
//                        MainActivity.access$102(MainActivity.this, MainActivity.this.getResources().getString(2131427367));
//                        ei2.setBackgroundColor(0);
//                    }
//                    (new MainActivity.CheckGebruiker()).execute((Object[])new String[] { MainActivity.access$100(this.this$0) });
//                }
//            });
//            textView1 = (TextView)findViewById(2131165309);
//            textView1.setText(getResources().getString(2131427363));
//            textView1.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View param1View) {
//                    if (MainActivity.this.loginOK.booleanValue()) {
//                        MainActivity.this.startSoarCast();
//                        return;
//                    }
//                    (new MainActivity.CheckGebruiker()).execute((Object[])new String[] { MainActivity.access$100(this.this$0) });
//                }
//            });
//            (new CheckGebruiker()).execute((Object[])new String[] { this.gebruiker });
//        }
//    }
//
//    public class CheckGebruiker extends AsyncTask<String, Void, Integer> {
//        private final String LOG_TAG = CheckGebruiker.class.getSimpleName();
//
//        protected Integer doInBackground(String... param1VarArgs) {
//            // Byte code:
//            //   0: aload_1
//            //   1: arraylength
//            //   2: ifne -> 10
//            //   5: iconst_m1
//            //   6: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   9: areturn
//            //   10: iconst_0
//            //   11: istore #4
//            //   13: aload_1
//            //   14: iconst_0
//            //   15: aaload
//            //   16: invokevirtual length : ()I
//            //   19: iconst_2
//            //   20: if_icmpge -> 28
//            //   23: iconst_m1
//            //   24: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   27: areturn
//            //   28: new java/lang/StringBuilder
//            //   31: dup
//            //   32: invokespecial <init> : ()V
//            //   35: astore #5
//            //   37: aload #5
//            //   39: aload_0
//            //   40: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   43: invokevirtual getResources : ()Landroid/content/res/Resources;
//            //   46: ldc 2131427395
//            //   48: invokevirtual getString : (I)Ljava/lang/String;
//            //   51: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
//            //   54: pop
//            //   55: aload #5
//            //   57: aload_0
//            //   58: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   61: invokevirtual getResources : ()Landroid/content/res/Resources;
//            //   64: ldc 2131427379
//            //   66: invokevirtual getString : (I)Ljava/lang/String;
//            //   69: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
//            //   72: pop
//            //   73: aload #5
//            //   75: invokevirtual toString : ()Ljava/lang/String;
//            //   78: astore #7
//            //   80: aload_0
//            //   81: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   84: invokevirtual getResources : ()Landroid/content/res/Resources;
//            //   87: ldc 2131427380
//            //   89: invokevirtual getString : (I)Ljava/lang/String;
//            //   92: astore #8
//            //   94: aconst_null
//            //   95: astore #6
//            //   97: aconst_null
//            //   98: astore #5
//            //   100: new java/net/URL
//            //   103: dup
//            //   104: aload #7
//            //   106: invokestatic parse : (Ljava/lang/String;)Landroid/net/Uri;
//            //   109: invokevirtual buildUpon : ()Landroid/net/Uri$Builder;
//            //   112: aload #8
//            //   114: aload_1
//            //   115: iconst_0
//            //   116: aaload
//            //   117: invokevirtual appendQueryParameter : (Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
//            //   120: invokevirtual build : ()Landroid/net/Uri;
//            //   123: invokevirtual toString : ()Ljava/lang/String;
//            //   126: invokespecial <init> : (Ljava/lang/String;)V
//            //   129: invokevirtual openConnection : ()Ljava/net/URLConnection;
//            //   132: checkcast java/net/HttpURLConnection
//            //   135: astore_1
//            //   136: aload_1
//            //   137: ldc 'GET'
//            //   139: invokevirtual setRequestMethod : (Ljava/lang/String;)V
//            //   142: aload_1
//            //   143: invokevirtual connect : ()V
//            //   146: aload_1
//            //   147: invokevirtual getInputStream : ()Ljava/io/InputStream;
//            //   150: astore #7
//            //   152: new java/lang/StringBuffer
//            //   155: dup
//            //   156: invokespecial <init> : ()V
//            //   159: astore #5
//            //   161: aload #7
//            //   163: ifnonnull -> 180
//            //   166: aload_1
//            //   167: ifnull -> 174
//            //   170: aload_1
//            //   171: invokevirtual disconnect : ()V
//            //   174: bipush #-9
//            //   176: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   179: areturn
//            //   180: new java/io/BufferedReader
//            //   183: dup
//            //   184: new java/io/InputStreamReader
//            //   187: dup
//            //   188: aload #7
//            //   190: invokespecial <init> : (Ljava/io/InputStream;)V
//            //   193: invokespecial <init> : (Ljava/io/Reader;)V
//            //   196: astore #7
//            //   198: aload #7
//            //   200: invokevirtual readLine : ()Ljava/lang/String;
//            //   203: astore #6
//            //   205: aload #6
//            //   207: ifnull -> 221
//            //   210: aload #5
//            //   212: aload #6
//            //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
//            //   217: pop
//            //   218: goto -> 198
//            //   221: aload #5
//            //   223: invokevirtual length : ()I
//            //   226: ifne -> 272
//            //   229: bipush #-9
//            //   231: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   234: astore #5
//            //   236: aload_1
//            //   237: ifnull -> 244
//            //   240: aload_1
//            //   241: invokevirtual disconnect : ()V
//            //   244: aload #7
//            //   246: ifnull -> 269
//            //   249: aload #7
//            //   251: invokevirtual close : ()V
//            //   254: aload #5
//            //   256: areturn
//            //   257: astore_1
//            //   258: aload_0
//            //   259: getfield LOG_TAG : Ljava/lang/String;
//            //   262: ldc 'Error closing stream'
//            //   264: aload_1
//            //   265: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
//            //   268: pop
//            //   269: aload #5
//            //   271: areturn
//            //   272: aload #5
//            //   274: invokevirtual toString : ()Ljava/lang/String;
//            //   277: astore #5
//            //   279: aload_1
//            //   280: ifnull -> 287
//            //   283: aload_1
//            //   284: invokevirtual disconnect : ()V
//            //   287: aload #7
//            //   289: ifnull -> 312
//            //   292: aload #7
//            //   294: invokevirtual close : ()V
//            //   297: goto -> 312
//            //   300: astore_1
//            //   301: aload_0
//            //   302: getfield LOG_TAG : Ljava/lang/String;
//            //   305: ldc 'Error closing stream'
//            //   307: aload_1
//            //   308: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
//            //   311: pop
//            //   312: aload #5
//            //   314: invokevirtual length : ()I
//            //   317: iconst_1
//            //   318: if_icmpge -> 327
//            //   321: bipush #-9
//            //   323: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   326: areturn
//            //   327: aload #5
//            //   329: aload_0
//            //   330: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   333: invokevirtual getResources : ()Landroid/content/res/Resources;
//            //   336: ldc 2131427368
//            //   338: invokevirtual getString : (I)Ljava/lang/String;
//            //   341: invokevirtual equals : (Ljava/lang/Object;)Z
//            //   344: ifeq -> 352
//            //   347: iconst_m1
//            //   348: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   351: areturn
//            //   352: aload #5
//            //   354: aload_0
//            //   355: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   358: invokevirtual getResources : ()Landroid/content/res/Resources;
//            //   361: ldc 2131427369
//            //   363: invokevirtual getString : (I)Ljava/lang/String;
//            //   366: invokevirtual equals : (Ljava/lang/Object;)Z
//            //   369: ifeq -> 378
//            //   372: bipush #-2
//            //   374: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   377: areturn
//            //   378: aload #5
//            //   380: aload_0
//            //   381: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   384: invokevirtual getResources : ()Landroid/content/res/Resources;
//            //   387: ldc 2131427328
//            //   389: invokevirtual getString : (I)Ljava/lang/String;
//            //   392: invokevirtual indexOf : (Ljava/lang/String;)I
//            //   395: istore_2
//            //   396: iload_2
//            //   397: ifle -> 418
//            //   400: aload_0
//            //   401: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   404: aload #5
//            //   406: iconst_0
//            //   407: iload_2
//            //   408: invokevirtual substring : (II)Ljava/lang/String;
//            //   411: invokestatic access$302 : (Lcom/erwinvoogt/soarcast/MainActivity;Ljava/lang/String;)Ljava/lang/String;
//            //   414: pop
//            //   415: goto -> 428
//            //   418: aload_0
//            //   419: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   422: aload #5
//            //   424: invokestatic access$302 : (Lcom/erwinvoogt/soarcast/MainActivity;Ljava/lang/String;)Ljava/lang/String;
//            //   427: pop
//            //   428: aload_0
//            //   429: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   432: astore_1
//            //   433: aload #5
//            //   435: ldc 'sp1'
//            //   437: invokevirtual indexOf : (Ljava/lang/String;)I
//            //   440: ifle -> 448
//            //   443: iconst_1
//            //   444: istore_3
//            //   445: goto -> 450
//            //   448: iconst_0
//            //   449: istore_3
//            //   450: aload_1
//            //   451: iload_3
//            //   452: invokestatic valueOf : (Z)Ljava/lang/Boolean;
//            //   455: invokestatic access$402 : (Lcom/erwinvoogt/soarcast/MainActivity;Ljava/lang/Boolean;)Ljava/lang/Boolean;
//            //   458: pop
//            //   459: aload_0
//            //   460: getfield this$0 : Lcom/erwinvoogt/soarcast/MainActivity;
//            //   463: astore_1
//            //   464: iload #4
//            //   466: istore_3
//            //   467: aload #5
//            //   469: ldc 'sp2'
//            //   471: invokevirtual indexOf : (Ljava/lang/String;)I
//            //   474: ifle -> 479
//            //   477: iconst_1
//            //   478: istore_3
//            //   479: aload_1
//            //   480: iload_3
//            //   481: invokestatic valueOf : (Z)Ljava/lang/Boolean;
//            //   484: invokestatic access$502 : (Lcom/erwinvoogt/soarcast/MainActivity;Ljava/lang/Boolean;)Ljava/lang/Boolean;
//            //   487: pop
//            //   488: iconst_1
//            //   489: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   492: areturn
//            //   493: astore #5
//            //   495: aload_1
//            //   496: astore #6
//            //   498: goto -> 608
//            //   501: astore #6
//            //   503: aload #7
//            //   505: astore #5
//            //   507: goto -> 520
//            //   510: astore #5
//            //   512: goto -> 615
//            //   515: astore #6
//            //   517: aconst_null
//            //   518: astore #5
//            //   520: aload #6
//            //   522: astore #7
//            //   524: aload_1
//            //   525: astore #6
//            //   527: aload #5
//            //   529: astore_1
//            //   530: goto -> 548
//            //   533: astore #5
//            //   535: aconst_null
//            //   536: astore_1
//            //   537: goto -> 615
//            //   540: astore #7
//            //   542: aconst_null
//            //   543: astore_1
//            //   544: aload #5
//            //   546: astore #6
//            //   548: aload_0
//            //   549: getfield LOG_TAG : Ljava/lang/String;
//            //   552: ldc 'Error '
//            //   554: aload #7
//            //   556: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
//            //   559: pop
//            //   560: bipush #-9
//            //   562: invokestatic valueOf : (I)Ljava/lang/Integer;
//            //   565: astore #5
//            //   567: aload #6
//            //   569: ifnull -> 577
//            //   572: aload #6
//            //   574: invokevirtual disconnect : ()V
//            //   577: aload_1
//            //   578: ifnull -> 600
//            //   581: aload_1
//            //   582: invokevirtual close : ()V
//            //   585: aload #5
//            //   587: areturn
//            //   588: astore_1
//            //   589: aload_0
//            //   590: getfield LOG_TAG : Ljava/lang/String;
//            //   593: ldc 'Error closing stream'
//            //   595: aload_1
//            //   596: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
//            //   599: pop
//            //   600: aload #5
//            //   602: areturn
//            //   603: astore #5
//            //   605: aload_1
//            //   606: astore #7
//            //   608: aload #6
//            //   610: astore_1
//            //   611: aload #7
//            //   613: astore #6
//            //   615: aload_1
//            //   616: ifnull -> 623
//            //   619: aload_1
//            //   620: invokevirtual disconnect : ()V
//            //   623: aload #6
//            //   625: ifnull -> 648
//            //   628: aload #6
//            //   630: invokevirtual close : ()V
//            //   633: goto -> 648
//            //   636: astore_1
//            //   637: aload_0
//            //   638: getfield LOG_TAG : Ljava/lang/String;
//            //   641: ldc 'Error closing stream'
//            //   643: aload_1
//            //   644: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
//            //   647: pop
//            //   648: aload #5
//            //   650: athrow
//            // Exception table:
//            //   from	to	target	type
//            //   100	136	540	java/io/IOException
//            //   100	136	533	finally
//            //   136	161	515	java/io/IOException
//            //   136	161	510	finally
//            //   180	198	515	java/io/IOException
//            //   180	198	510	finally
//            //   198	205	501	java/io/IOException
//            //   198	205	493	finally
//            //   210	218	501	java/io/IOException
//            //   210	218	493	finally
//            //   221	236	501	java/io/IOException
//            //   221	236	493	finally
//            //   249	254	257	java/io/IOException
//            //   272	279	501	java/io/IOException
//            //   272	279	493	finally
//            //   292	297	300	java/io/IOException
//            //   548	567	603	finally
//            //   581	585	588	java/io/IOException
//            //   628	633	636	java/io/IOException
//        }
//
//        protected void onPostExecute(Integer param1Integer) {
//            Context context = MainActivity.this.getApplicationContext();
//            TextView textView = (TextView)MainActivity.this.findViewById(2131165309);
//            String str = "";
//            if (param1Integer.intValue() == -1)
//                str = context.getString(2131427362);
//            if (param1Integer.intValue() == -2)
//                str = context.getString(2131427393);
//            if (param1Integer.intValue() == -9)
//                str = context.getString(2131427361);
//            if (param1Integer.intValue() < 0)
//                Toast.makeText(context, str, 0).show();
//            if (param1Integer.intValue() == 1)
//                str = context.getString(2131427397);
//            textView.setText(str);
//            if (param1Integer.intValue() == 1) {
//                MainActivity.access$002(MainActivity.this, Boolean.valueOf(true));
//                MainActivity.this.startSoarCast();
//            }
//        }
//    }
//}
