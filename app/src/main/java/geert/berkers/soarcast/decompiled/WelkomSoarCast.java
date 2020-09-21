package geert.berkers.soarcast.decompiled;

/**
 * Created by Zorgkluis (Geert Berkers)
 */

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
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import geert.berkers.soarcast.R;
import geert.berkers.soarcast.listener.Direction;
import geert.berkers.soarcast.listener.OnSwipeListener;
import geert.berkers.soarcast.model.Locatie;
import geert.berkers.soarcast.model.Richting;
import geert.berkers.soarcast.model.Wind;
import geert.berkers.soarcast.views.RichtingKaderView;
import geert.berkers.soarcast.views.RichtingMetingView;
import geert.berkers.soarcast.views.RichtingModelView;
import geert.berkers.soarcast.views.WaardenView;
import geert.berkers.soarcast.views.WindKaderView;
import geert.berkers.soarcast.views.WindMetingView;
import geert.berkers.soarcast.views.WindModelView;

@SuppressWarnings("deprecation")
public class
WelkomSoarCast extends Activity
{
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
    private final Integer maxN;
    private String[] queryDag;
    public float reedsVerschoven1;
    public float reedsVerschoven2;
    private Integer richt;
    private Integer schaal;
    private Integer tIndicator;
    private String[] tekstEenheid;
    private String[] tekstModel;
    private String[] tekstRicht;
    private Long tijdNul;
    private Integer uurVanaf;
    private Integer weerModel;

    public WelkomSoarCast() {
        this.tekstModel = new String[] { "Harm", "GFS" };
        this.tekstEenheid = new String[] { "m/s", "km/h", "kn", "Bft" };
        this.tekstRicht = new String[] { "N", "deg" };
        this.queryDag = new String[] { "yesterday", "today", "tomorrow", "dayaftertomorrow" };
        this.maxN = 288;
    }

    private Integer[] bepaalKolommen(final String s) {
        final Integer[] array = { -1, -1, -1, -1, -1, -1, -1 };
        final String string = this.getResources().getString(R.string.csv_separator);
        Integer n = 0;
        final Integer value = s.length();
        int n2 = 0;
        while (n < value) {
            Integer value2;
            if ((value2 = s.indexOf(string, n)) < 0) {
                value2 = value;
            }
            final String substring = s.substring(n, value2);
            int n3 = 0;
            Label_0342: {
                switch (substring.hashCode()) {
                    case 664374541: {
                        if (substring.equals("gfs_windsnelheid")) {
                            n3 = 3;
                            break Label_0342;
                        }
                        break;
                    }
                    case 441051965: {
                        if (substring.equals("harm_windrichting")) {
                            n3 = 5;
                            break Label_0342;
                        }
                        break;
                    }
                    case 96835762: {
                        if (substring.equals("etime")) {
                            n3 = 0;
                            break Label_0342;
                        }
                        break;
                    }
                    case -1281577795: {
                        if (substring.equals("gfs_windrichting")) {
                            n3 = 6;
                            break Label_0342;
                        }
                        break;
                    }
                    case -1897228994: {
                        if (substring.equals("harm_windvlaag")) {
                            n3 = 2;
                            break Label_0342;
                        }
                        break;
                    }
                    case -1907962995: {
                        if (substring.equals("harm_windsnelheid")) {
                            n3 = 1;
                            break Label_0342;
                        }
                        break;
                    }
                    case -2064956482: {
                        if (substring.equals("gfs_windvlaag")) {
                            n3 = 4;
                            break Label_0342;
                        }
                        break;
                    }
                }
                n3 = -1;
            }
            switch (n3) {
                default: {
                    if (substring.length() <= 8) {
                        break;
                    }
                    if (substring.substring(0, 8).equals("windstoo")) {
                        array[n2] = 2;
                        break;
                    }
                    if (substring.substring(0, 8).equals("windsnel")) {
                        array[n2] = 1;
                        break;
                    }
                    if (substring.substring(0, 8).equals("windrich")) {
                        array[n2] = 7;
                        break;
                    }
                    break;
                }
                case 6: {
                    array[n2] = 9;
                    break;
                }
                case 5: {
                    array[n2] = 8;
                    break;
                }
                case 4: {
                    array[n2] = 6;
                    break;
                }
                case 3: {
                    array[n2] = 5;
                    break;
                }
                case 2: {
                    array[n2] = 4;
                    break;
                }
                case 1: {
                    array[n2] = 3;
                    break;
                }
                case 0: {
                    array[n2] = 0;
                    break;
                }
            }
            ++n2;
            n = value2 + 1;
        }
        return array;
    }



    private void doeLocatieN() {
        if (this.klaar > 0 && !this.grafiekBezig && this.locID >= 0 && this.locIndex > 0) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            this.locIndex = this.locIndex - 1;
            updateLocatieWindRichting();
            editor.putInt("locatie", this.locIndex);
            editor.apply();
        }
    }

    private void doeLocatieZ() {
        if (this.klaar > 0 && !this.grafiekBezig && this.locID >= 0 && this.locIndex < this.locIndexMax) {
            final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
            this.locIndex = this.locIndex + 1;
            this.updateLocatieWindRichting();
            edit.putInt("locatie", this.locIndex);
            edit.apply();
        }
    }


    private void ikBenKlaar() {
        this.klaar = this.klaar + 1;
        if (this.klaar > 0) {
            ((TextView)findViewById(R.id.txtLoading)).setText("");
            ((WaardenView)findViewById(R.id.waardenView)).update(this.eenheid, this.schaal, this.uurVanaf, this.tIndicator);
        }
    }

    private void updateLocatieWindRichting() {
        this.klaar = -1;
        this.locID = this.mLocatie.get(this.locIndex).id;
        ((TextView)this.findViewById(R.id.txtLoading)).setText(this.getResources().getString(R.string.laden));
        ((TextView)this.findViewById(R.id.locatie)).setText(this.mLocatie.get(this.locIndex).naam);
        if (!this.mWind.isEmpty()) {
            this.mWind.clear();
        }
        if (!this.mRichting.isEmpty()) {
            this.mRichting.clear();
        }

        new LeesWind().execute(this.locID);
        new LeesRichting().execute(this.locID);
    }

    private void updateRichting() {
        final Integer[] array = new Integer[(int)this.maxN];
        final Double[] array2 = new Double[(int)this.maxN];
        final int size = this.mRichting.size();
        final Long value = this.tijdNul + this.uurVanaf * 3600;
        Long n = value + 86400L;
        int n2 = 0;
        int n3 = 0;
        while (this.mRichting.get(n2).unixTimestamp <= n) {
            int n4 = n3;
            if (this.mRichting.get(n2).unixTimestamp >= value) {
                final Double access$4200 = this.mRichting.get(n2).richtingMeting;
                n4 = n3;
                if (access$4200 >= 0.0) {
                    n4 = n3;
                    if (access$4200 < 1000.0 && (n4 = n3) < this.maxN) {
                        array[n3] = (int)(this.mRichting.get(n2).unixTimestamp - value);
                        array2[n3] = access$4200;
                        n4 = n3 + 1;
                    }
                }
            }
            if (n2 < size - 1) {
                ++n2;
                n3 = n4;
            }
            else {
                n = 0L;
                n3 = n4;
            }
        }
        ((RichtingMetingView)this.findViewById(R.id.richtingMetingView)).update(this.mLocatie.get(this.locIndex).mindeg, this.mLocatie.get(this.locIndex).maxdeg, n3, array, array2);
        ((WaardenView)this.findViewById(R.id.waardenView)).zetRichting(n3, array, array2);
    }

    private void updateRichtingModel() {
        final Integer[] array = new Integer[(int)this.maxN];
        final Double[] array2 = new Double[(int)this.maxN];
        final int size = this.mRichting.size();
        final Long value = this.tijdNul + this.uurVanaf * 3600;
        Long n = value + 86400L;
        int index = 0;
        int n2 = 0;
        while (this.mRichting.get(index).unixTimestamp <= n) {
            int n3 = n2;
            if (this.mRichting.get(index).unixTimestamp >= value) {
                Double n4;
                if (this.weerModel == 1) {
                    n4 = this.mRichting.get(index).richtingGFS;
                }
                else {
                    n4 = this.mRichting.get(index).richtingHarmonie;
                }
                n3 = n2;
                if (n4 >= 0.0 && (n3 = n2) < this.maxN) {
                    array[n2] = (int)(this.mRichting.get(index).unixTimestamp - value);
                    array2[n2] = n4;
                    n3 = n2 + 1;
                }
            }
            if (index < size - 1) {
                ++index;
                n2 = n3;
            }
            else {
                n = 0L;
                n2 = n3;
            }
        }
        ((RichtingModelView)this.findViewById(R.id.richtingModelView)).update(this.mLocatie.get(this.locIndex).mindeg, this.mLocatie.get(this.locIndex).maxdeg, n2, array, array2);
    }

    private void updateWindModel() {
        final Integer[] array = new Integer[(int)this.maxN];
        final Double[] array2 = new Double[(int)this.maxN];
        final Double[] array3 = new Double[(int)this.maxN];
        final int size = this.mWind.size();
        final Long value = this.tijdNul + this.uurVanaf * 3600;
        Long n = value + 86400L;
        int index = 0;
        int n2 = 0;
        while (this.mWind.get(index).unixTimestamp <= n) {
            if (this.mWind.get(index).unixTimestamp >= value) {
                Double n3;
                Double n4;
                if (this.weerModel == 1) {
                    n3 = this.mWind.get(index).snelheidGFS;
                    n4 = this.mWind.get(index).vlaagGFS;
                }
                else {
                    n3 = this.mWind.get(index).snelheidHarmonie;
                    n4 = this.mWind.get(index).vlaagHarmonie;
                }
                if (n4 > 0.0 || (n3 > 0.0 && n2 < this.maxN)) {
                    array[n2] = (int)(this.mWind.get(index).unixTimestamp - value);
                    array2[n2] = n3;
                    array3[n2] = n4;
                    ++n2;
                }
            }
            if (index < size - 1) {
                ++index;
            }
            else {
                n = 0L;
            }
        }
        ((WindModelView)this.findViewById(R.id.windModelView)).update(this.schaal, n2, array, array2, array3);
    }

    private void updateWindmeting() {
        final Integer[] array = new Integer[(int)this.maxN];
        final Double[] array2 = new Double[(int)this.maxN];
        final Double[] array3 = new Double[(int)this.maxN];
        final int size = this.mWind.size();
        final Long value = this.tijdNul + this.uurVanaf * 3600;
        Long n = value + 86400L;
        int index = 0;
        int n2 = 0;
        while (this.mWind.get(index).unixTimestamp <= n) {
            int n3 = n2;
            Label_0374: {
                if (this.mWind.get(index).unixTimestamp >= value) {
                    final Double access$3500 = this.mWind.get(index).snelheidMeting;
                    final Double access$3501 = this.mWind.get(index).vlaagMeting;
                    if (access$3501 <= 0.0) {
                        n3 = n2;
                        if (access$3500 <= 0.0 || (n3 = n2) >= this.maxN) {
                            break Label_0374;
                        }
                    }
                    array[n2] = (int)(this.mWind.get(index).unixTimestamp - value);
                    Double n4 = null;
                    Label_0285: {
                        if (access$3500 > 0.0) {
                            n4 = access$3500;
                            if (access$3500 < 400.0) {
                                break Label_0285;
                            }
                        }
                        if (n2 > 0) {
                            n4 = array2[n2 - 1];
                        }
                        else {
                            n4 = access$3501;
                        }
                    }
                    Double n5 = null;
                    Label_0330: {
                        if (access$3501 > 0.0) {
                            n5 = access$3501;
                            if (access$3501 < 400.0) {
                                break Label_0330;
                            }
                        }
                        if (n2 > 0) {
                            n5 = array3[n2 - 1];
                        }
                        else {
                            n5 = n4;
                        }
                    }
                    n3 = n2;
                    if (n4 < 400.0) {
                        n3 = n2;
                        if (n5 < 400.0) {
                            array2[n2] = n4;
                            array3[n2] = n5;
                            n3 = n2 + 1;
                        }
                    }
                }
            }
            if (index < size - 1) {
                ++index;
                n2 = n3;
            }
            else {
                n = 0L;
                n2 = n3;
            }
        }
        ((WindMetingView)this.findViewById(R.id.windMetingView)).update(this.schaal, n2, array, array2, array3);
        ((WaardenView)this.findViewById(R.id.waardenView)).zetWind(this.schaal, n2, array, array2, array3);
        if (!this.grafiekBezig) {
            if (n2 > 0) {
                this.tIndicator = array[n2 - 1];
            }
            if (n2 == 0 || this.tIndicator < 10800 || this.tIndicator > 75600) {
                this.tIndicator = 43200;
            }
        }
    }

    public void locatieOpMaps() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mLocatie.get(this.locIndex).lat.toString());
        sb.append(",");
        sb.append(this.mLocatie.get(this.locIndex).lon.toString());
        final String string = sb.toString();
        final Intent intent = new Intent("android.intent.action.VIEW");
        final Uri.Builder buildUpon = Uri.parse("geo:0,0?").buildUpon();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("(");
        sb2.append(this.mLocatie.get(this.locIndex).naam);
        sb2.append(" - weather station)");
        final Uri build = buildUpon.appendQueryParameter("q", sb2.toString()).build();
        intent.setData(build);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Maps niet bereikt ");
        sb3.append(build);
        Log.d("SoarCast", sb3.toString());
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final Integer value = displayMetrics.widthPixels;
        this.mLocatie = new ArrayList<Locatie>();
        this.mWind = new ArrayList<Wind>();
        this.mRichting = new ArrayList<Richting>();
        this.locIndexMax = 0;
        this.locIndex = 0;
        this.locID = -1;
        this.klaar = -1;
        this.weerModel = 0;
        this.eenheid = 0;
        this.schaal = 1;
        this.richt = 1;
        this.uurVanaf = 24;
        this.tijdNul = 0L;
        this.grafiekBezig = false;
        this.tIndicator = 43200;
        this.getIntent().getExtras().getString("gebruiker");
        final String string = this.getIntent().getExtras().getString("bestand");
        this.eenheid = defaultSharedPreferences.getInt("eenheid", 0);
        this.richt = defaultSharedPreferences.getInt("richting", 0);
        this.locIndex = defaultSharedPreferences.getInt("locatie", 0);

        if (bundle == null) {
            this.setContentView(R.layout.activity_welkomsoarcast);
            ((TextView)this.findViewById(R.id.txtLoading)).setText(this.getResources().getString(R.string.laden));
            new LeesLocaties().execute(string);

            final Button button = this.findViewById(R.id.btnModel);
            final Button button2 = this.findViewById(R.id.btnEenheid);
            final Button button3 = this.findViewById(R.id.btnRichting);
            final ImageButton imageButton = this.findViewById(R.id.btnDelen);
            button.setText(this.tekstModel[this.weerModel]);
            button2.setText(this.tekstEenheid[this.eenheid]);
            button3.setText(this.tekstRicht[this.richt]);
            button.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            button2.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            button3.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            imageButton.getBackground().setColorFilter(-2047872, PorterDuff.Mode.MULTIPLY);
            final ImageButton imageButton2 = this.findViewById(R.id.locatieN);
            final ImageButton imageButton3 = this.findViewById(R.id.locatieZ);
            button.setOnClickListener(view -> {
                if (WelkomSoarCast.this.klaar > 0 && !WelkomSoarCast.this.grafiekBezig) {
//                    WelkomSoarCast.this.weerModel;
//                    ++WelkomSoarCast.this.weerModel;
//                    WelkomSoarCast.this.weerModel %= 2;
                    WelkomSoarCast.this.weerModel = this.weerModel % 2;
                    button.setText(WelkomSoarCast.this.tekstModel[WelkomSoarCast.this.weerModel]);
                    WelkomSoarCast.this.updateWindModel();
                    WelkomSoarCast.this.updateRichtingModel();
                }
            });
            button2.setOnClickListener(view -> {
                if (WelkomSoarCast.this.klaar > 0 && !WelkomSoarCast.this.grafiekBezig) {
//                    WelkomSoarCast.this.eenheid;
//                    ++WelkomSoarCast.this.eenheid;
//                    WelkomSoarCast.this.eenheid %= 4;
                    WelkomSoarCast.this.eenheid = this.eenheid + 1;
                    WelkomSoarCast.this.eenheid = this.eenheid % 4;
                    button2.setText(WelkomSoarCast.this.tekstEenheid[WelkomSoarCast.this.eenheid]);
                    ((WindKaderView)WelkomSoarCast.this.findViewById(R.id.windKaderView)).update(
                            WelkomSoarCast.this.eenheid,
                            WelkomSoarCast.this.schaal,
                            WelkomSoarCast.this.uurVanaf,
                            WelkomSoarCast.this.mWind.get(0).geefZonOpOnder()
                    );
                    ((WaardenView)WelkomSoarCast.this.findViewById(R.id.waardenView)).update(
                            WelkomSoarCast.this.eenheid,
                            WelkomSoarCast.this.schaal,
                            WelkomSoarCast.this.uurVanaf,
                            WelkomSoarCast.this.tIndicator
                    );
                    edit.putInt("eenheid", WelkomSoarCast.this.eenheid);
                    edit.apply();
                }
            });
            button3.setOnClickListener(view -> {
                if (WelkomSoarCast.this.klaar > 0 && !WelkomSoarCast.this.grafiekBezig) {
//                    WelkomSoarCast.this.richt;
//                    ++WelkomSoarCast.this.richt;
//                    WelkomSoarCast.this.richt %= 2;
                    WelkomSoarCast.this.richt = this.richt + 1;
                    WelkomSoarCast.this.richt = this.richt % 2;
                    button3.setText(WelkomSoarCast.this.tekstRicht[WelkomSoarCast.this.richt]);
                    ((RichtingKaderView)WelkomSoarCast.this.findViewById(R.id.richtingKaderView)).update(WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex).mindeg, WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex).maxdeg, WelkomSoarCast.this.richt, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.mRichting.get(0).geefZonOpOnder());
                    edit.putInt("richting", WelkomSoarCast.this.richt);
                    edit.apply();
                }
            });
            imageButton3.setOnClickListener(view -> WelkomSoarCast.this.doeLocatieZ());
            imageButton2.setOnClickListener(view -> WelkomSoarCast.this.doeLocatieN());
            imageButton.setOnClickListener(view -> WelkomSoarCast.this.schermafdruk());
            this.findViewById(R.id.locatie).setOnClickListener(view -> {
                if (WelkomSoarCast.this.locID >= 0) {
                    WelkomSoarCast.this.locatieOpMaps();
                }
            });
            this.gestureDetector1 = new GestureDetector(this, new OnSwipeListener() {
                @Override
                public boolean onSchuif(final float n) {
                    final int i = value / 24;
                    final Integer value = (int)(n - 1.0 * WelkomSoarCast.this.reedsVerschoven1);
                    final Integer value2 = value / i;
                    if (value2 != 0 && !WelkomSoarCast.this.grafiekBezig && WelkomSoarCast.this.klaar > 0) {
                        WelkomSoarCast.this.grafiekBezig = true;
                        WelkomSoarCast.this.reedsVerschoven1 += value;
                        WelkomSoarCast.this.uurVanaf -= value2;
                        if (WelkomSoarCast.this.uurVanaf < 0) {
                            WelkomSoarCast.this.uurVanaf = 0;
                        }
                        if (WelkomSoarCast.this.uurVanaf > 72) {
                            WelkomSoarCast.this.uurVanaf = 72;
                        }
                        ((WindKaderView)WelkomSoarCast.this.findViewById(R.id.windKaderView)).update(WelkomSoarCast.this.eenheid, WelkomSoarCast.this.schaal, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.mWind.get(0).geefZonOpOnder());
                        ((RichtingKaderView)WelkomSoarCast.this.findViewById(R.id.richtingKaderView)).update(WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex).mindeg, WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex).maxdeg, WelkomSoarCast.this.richt, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.mRichting.get(0).geefZonOpOnder());
                        WelkomSoarCast.this.updateWindmeting();
                        WelkomSoarCast.this.updateWindModel();
                        WelkomSoarCast.this.updateRichting();
                        WelkomSoarCast.this.updateRichtingModel();
                        ((WaardenView)WelkomSoarCast.this.findViewById(R.id.waardenView)).update(WelkomSoarCast.this.eenheid, WelkomSoarCast.this.schaal, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.tIndicator);
                        WelkomSoarCast.this.grafiekBezig = false;
                    }
                    return true;
                }

                @Override
                public boolean onSwipe(final Direction direction) {
                    if (direction == Direction.up) {
                        WelkomSoarCast.this.doeLocatieZ();
                    }
                    if (direction == Direction.down) {
                        WelkomSoarCast.this.doeLocatieN();
                    }
                    return true;
                }

                @Override
                public boolean zetSchuifOpNul() {
                    WelkomSoarCast.this.reedsVerschoven1 = 0.0f;
                    return true;
                }
            });
            this.findViewById(R.id.windKaderView).setOnTouchListener((view, motionEvent) -> {
                WelkomSoarCast.this.gestureDetector1.onTouchEvent(motionEvent);
                return true;
            });
            this.findViewById(R.id.richtingKaderView).setOnTouchListener((view, motionEvent) -> {
                WelkomSoarCast.this.gestureDetector1.onTouchEvent(motionEvent);
                return true;
            });
            this.gestureDetector2 = new GestureDetector(this, new OnSwipeListener() {
                @Override
                public boolean onSchuif(float n) {
                    final Integer value = (int)(n - 1.0 * WelkomSoarCast.this.reedsVerschoven2);
                    n = 86400.0f * (value * 1.0f) / (1.0f * value);
                    if (n != 0.0f && !WelkomSoarCast.this.grafiekBezig && WelkomSoarCast.this.klaar > 0) {
                        WelkomSoarCast.this.grafiekBezig = true;
                        WelkomSoarCast.this.reedsVerschoven2 += value;
                        WelkomSoarCast.this.tIndicator += (int)n;
                        if (WelkomSoarCast.this.tIndicator < 10800) {
                            WelkomSoarCast.this.tIndicator = 10800;
                        }
                        if (WelkomSoarCast.this.tIndicator > 75600) {
                            WelkomSoarCast.this.tIndicator = 75600;
                        }
                        ((WaardenView)WelkomSoarCast.this.findViewById(R.id.waardenView)).update(WelkomSoarCast.this.eenheid, WelkomSoarCast.this.schaal, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.tIndicator);
                        WelkomSoarCast.this.grafiekBezig = false;
                    }
                    return true;
                }

                @Override
                public boolean zetSchuifOpNul() {
                    WelkomSoarCast.this.reedsVerschoven2 = 0.0f;
                    return true;
                }
            });
            this.findViewById(R.id.waardenView).setOnTouchListener((view, motionEvent) -> {
                WelkomSoarCast.this.gestureDetector2.onTouchEvent(motionEvent);
                return true;
            });
        }
    }

    public void schermafdruk() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission((Context)this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            final View rootView = this.getWindow().getDecorView().getRootView();
            rootView.setDrawingCacheEnabled(true);
            final Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);
            final File externalStorageDirectory = Environment.getExternalStorageDirectory();
            final StringBuilder sb = new StringBuilder();
            sb.append(externalStorageDirectory.getAbsolutePath());
            sb.append((Object)this.getResources().getText(R.string.schermafdruk));
            final File file = new File(sb.toString());
            try {
                file.createNewFile();
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)fileOutputStream);
                fileOutputStream.close();
            }
            catch (Exception ex) {
                Log.e("SoarCast", "Error screenshot", ex);
            }
            final Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            this.startActivity(Intent.createChooser(intent, "Share via"));
            return;
        }
        Toast.makeText(this.getBaseContext(), this.getResources().getString(R.string.permissie), Toast.LENGTH_SHORT).show();
    }

    private class LeesLocaties extends AsyncTask<String, Void, Integer>
    {
        private final String LOG_TAG;

        private LeesLocaties() {
            this.LOG_TAG = LeesLocaties.class.getSimpleName();
        }

        // TODO: Implement translation of bytecode
        protected Integer doInBackground(final String... params) {

            // URL:
            // http://www.erwinvoogt.com/SoarCastApp/locatie01.txt

            // TODO: Make list of locations

            // Result:
            /*
                naam;id;lat;long;mindeg;maxdeg
                Texel (Q1);29;52.925338;4.150363;270;360
                Wijk aan Zee (KNMI);52;52.462242867998;4.5549006792363;255;315
                Langevelderslag (Schiphol);55;52.3154084;4.7902228;264;318
                Langevelderslag (Katwijk);91;52.215426;4.402033;264;318
                Slufter/Lichteiland Goeree;34;51.925748;3.669877;200;290
                Westenschouwen (OS4);28;51.655883;3.693955;170;230
                Zoutelande;14;51.503721;3.242052;225;260
             */

            final StringBuilder sb = new StringBuilder();
            sb.append(WelkomSoarCast.this.getResources().getString(R.string.url_login));
            sb.append(params[0]);
            sb.append(WelkomSoarCast.this.getResources().getString(R.string.extensie));
            String urlString = sb.toString();

            String data = null;
            try {
                URL url = new URL(Uri.parse(urlString).buildUpon().build().toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                int responseCode = urlConnection.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    urlConnection.getInputStream()
                            )
                    );

                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        response.append(inputLine);

                        if (inputLine.equals("naam;id;lat;long;mindeg;maxdeg")) {
                            continue;
                        } else {
                            try {
                                Locatie location = createLocation(inputLine.split(";"));
                                mLocatie.add(location);
                            } catch (Exception ex) {
                                System.out.println("Could NOT parse location!");
                            }
                        }
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());

                    return mLocatie.size();
                } else {
                    System.out.println("GET request not worked");
                    return -1;
                }



            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     3: dup
            //     4: invokespecial   java/lang/StringBuilder.<init>:()V
            //     7: astore_3
            //     8: aload_3
            //     9: aload_0
            //    10: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    13: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    16: ldc             2131427395 //TODO: url_login (http://www.erwinvoogt.com/SoarCastApp/)
            //    18: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    21: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    24: pop
            //    25: aload_3
            //    26: aload_1
            //    27: iconst_0
            //    28: aaload           // TODO: Load parameter from execute() -> locatie01
            //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    32: pop
            //    33: aload_3
            //    34: aload_0
            //    35: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    38: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    41: ldc             2131427365 //TODO: extensie (.txt)
            //    43: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    46: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    49: pop
            //    50: aload_3
            //    51: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    54: astore_1
            //    55: iconst_0
            //    56: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //    59: astore          15
            //    61: aload_0
            //    62: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    65: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$1700:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //    68: invokevirtual   java/util/ArrayList.isEmpty:()Z
            //    71: ifne            84
            //    74: aload_0
            //    75: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    78: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$1700:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //    81: invokevirtual   java/util/ArrayList.clear:()V
            //    84: aconst_null
            //    85: astore          4
            //    87: aconst_null
            //    88: astore_3
            //    89: new             Ljava/net/URL;
            //    92: dup
            //    93: aload_1           // TODO: Start HTTP Url Connection
            //    94: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //    97: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //   100: checkcast       Ljava/net/HttpURLConnection;
            //   103: astore          13
            //   105: aload           13
            //   107: ldc             "GET"
            //   109: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
            //   112: aload           13
            //   114: invokevirtual   java/net/HttpURLConnection.connect:()V
            //   117: aload           13
            //   119: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
            //   122: astore_1
            //   123: aload_1
            //   124: ifnonnull       142
            //   127: aload           13
            //   129: ifnull          137
            //   132: aload           13
            //   134: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   137: iconst_0
            //   138: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   141: areturn           // TODO: Read bufferedreader
            //   142: new             Ljava/io/BufferedReader;
            //   145: dup
            //   146: new             Ljava/io/InputStreamReader;
            //   149: dup
            //   150: aload_1
            //   151: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
            //   154: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
            //   157: astore          14
            //   159: ldc             ""
            //   161: astore          7
            //   163: iconst_0
            //   164: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   167: astore          8
            //   169: dconst_0
            //   170: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   173: astore_3
            //   174: dconst_0
            //   175: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   178: astore_1
            //   179: iconst_0
            //   180: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   183: astore          5
            //   185: iconst_0
            //   186: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   189: astore          4
            //   191: iconst_m1
            //   192: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   195: astore          6
            //   197: aload_0
            //   198: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   201: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   204: ldc             R.string.csv_separator // TODO: Specify csv seperator
            //   206: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   209: astore          17
            //   211: aload           14
            //   213: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
            //   216: astore          18
            //   218: aload           18
            //   220: ifnull          910
            //   223: aload           15
            //   225: invokevirtual   java/lang/Integer.intValue:()I
            //   228: ifle            1143
            //   231: aload           18
            //   233: aload           17
            //   235: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
            //   238: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   241: astore          16
            //   243: aload           6
            //   245: astore          11
            //   247: aload           16
            //   249: invokevirtual   java/lang/Integer.intValue:()I
            //   252: ifle            280
            //   255: aload           18
            //   257: iconst_0
            //   258: aload           16
            //   260: invokevirtual   java/lang/Integer.intValue:()I
            //   263: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   266: astore          7
            //   268: aload           16
            //   270: invokevirtual   java/lang/Integer.intValue:()I
            //   273: iconst_1
            //   274: iadd
            //   275: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   278: astore          11
            //   280: aload           11
            //   282: invokevirtual   java/lang/Integer.intValue:()I
            //   285: istore_2
            //   286: aload           7
            //   288: astore          10
            //   290: aload           8
            //   292: astore          6
            //   294: aload           16
            //   296: astore          12
            //   298: aload           11
            //   300: astore          9
            //   302: aload           10
            //   304: astore          7
            //   306: iload_2
            //   307: aload           16
            //   309: invokevirtual   java/lang/Integer.intValue:()I
            //   312: if_icmple       418
            //   315: aload           18
            //   317: aload           17
            //   319: aload           11
            //   321: invokevirtual   java/lang/Integer.intValue:()I
            //   324: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   327: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   330: astore          16
            //   332: aload           8
            //   334: astore          6
            //   336: aload           16
            //   338: astore          12
            //   340: aload           11
            //   342: astore          9
            //   344: aload           10
            //   346: astore          7
            //   348: aload           16
            //   350: invokevirtual   java/lang/Integer.intValue:()I
            //   353: aload           11
            //   355: invokevirtual   java/lang/Integer.intValue:()I
            //   358: if_icmplt       418
            //   361: aload           18
            //   363: aload           11
            //   365: invokevirtual   java/lang/Integer.intValue:()I
            //   368: aload           16
            //   370: invokevirtual   java/lang/Integer.intValue:()I
            //   373: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   376: astore          6
            //   378: aload           6
            //   380: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
            //   383: astore          6
            //   385: aload           10
            //   387: astore          7
            //   389: goto            402
            //   392: iconst_0
            //   393: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   396: astore          6
            //   398: ldc             ""
            //   400: astore          7
            //   402: aload           16
            //   404: invokevirtual   java/lang/Integer.intValue:()I
            //   407: iconst_1
            //   408: iadd
            //   409: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   412: astore          9
            //   414: aload           16
            //   416: astore          12
            //   418: aload           12
            //   420: astore          8
            //   422: aload           9
            //   424: invokevirtual   java/lang/Integer.intValue:()I
            //   427: aload           12
            //   429: invokevirtual   java/lang/Integer.intValue:()I
            //   432: if_icmple       1118
            //   435: aload           18
            //   437: aload           17
            //   439: aload           9
            //   441: invokevirtual   java/lang/Integer.intValue:()I
            //   444: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   447: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   450: astore          10
            //   452: aload           10
            //   454: astore          8
            //   456: aload           10
            //   458: invokevirtual   java/lang/Integer.intValue:()I
            //   461: aload           9
            //   463: invokevirtual   java/lang/Integer.intValue:()I
            //   466: if_icmplt       1118
            //   469: aload           18
            //   471: aload           9
            //   473: invokevirtual   java/lang/Integer.intValue:()I
            //   476: aload           10
            //   478: invokevirtual   java/lang/Integer.intValue:()I
            //   481: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   484: astore_3
            //   485: aload_3
            //   486: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //   489: astore_3
            //   490: goto            498
            //   493: dconst_0
            //   494: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   497: astore_3
            //   498: aload           10
            //   500: invokevirtual   java/lang/Integer.intValue:()I
            //   503: iconst_1
            //   504: iadd
            //   505: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   508: astore          9
            //   510: aload           10
            //   512: astore          8
            //   514: goto            517
            //   517: aload           8
            //   519: astore          10
            //   521: aload           9
            //   523: invokevirtual   java/lang/Integer.intValue:()I
            //   526: aload           8
            //   528: invokevirtual   java/lang/Integer.intValue:()I
            //   531: if_icmple       1121
            //   534: aload           18
            //   536: aload           17
            //   538: aload           9
            //   540: invokevirtual   java/lang/Integer.intValue:()I
            //   543: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   546: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   549: astore          8
            //   551: aload           8
            //   553: astore          10
            //   555: aload           8
            //   557: invokevirtual   java/lang/Integer.intValue:()I
            //   560: aload           9
            //   562: invokevirtual   java/lang/Integer.intValue:()I
            //   565: if_icmplt       1121
            //   568: aload           18
            //   570: aload           9
            //   572: invokevirtual   java/lang/Integer.intValue:()I
            //   575: aload           8
            //   577: invokevirtual   java/lang/Integer.intValue:()I
            //   580: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   583: astore_1
            //   584: aload_1
            //   585: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //   588: astore_1
            //   589: goto            597
            //   592: dconst_0
            //   593: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   596: astore_1
            //   597: aload           8
            //   599: invokevirtual   java/lang/Integer.intValue:()I
            //   602: iconst_1
            //   603: iadd
            //   604: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   607: astore          11
            //   609: aload           8
            //   611: astore          10
            //   613: goto            616
            //   616: aload           10
            //   618: astore          12
            //   620: aload           5
            //   622: astore          8
            //   624: aload           11
            //   626: astore          9
            //   628: aload           11
            //   630: invokevirtual   java/lang/Integer.intValue:()I
            //   633: aload           10
            //   635: invokevirtual   java/lang/Integer.intValue:()I
            //   638: if_icmple       732
            //   641: aload           18
            //   643: aload           17
            //   645: aload           11
            //   647: invokevirtual   java/lang/Integer.intValue:()I
            //   650: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   653: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   656: astore          10
            //   658: aload           10
            //   660: astore          12
            //   662: aload           5
            //   664: astore          8
            //   666: aload           11
            //   668: astore          9
            //   670: aload           10
            //   672: invokevirtual   java/lang/Integer.intValue:()I
            //   675: aload           11
            //   677: invokevirtual   java/lang/Integer.intValue:()I
            //   680: if_icmplt       732
            //   683: aload           18
            //   685: aload           11
            //   687: invokevirtual   java/lang/Integer.intValue:()I
            //   690: aload           10
            //   692: invokevirtual   java/lang/Integer.intValue:()I
            //   695: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   698: astore          5
            //   700: aload           5
            //   702: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
            //   705: astore          8
            //   707: goto            716
            //   710: iconst_0
            //   711: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   714: astore          8
            //   716: aload           10
            //   718: invokevirtual   java/lang/Integer.intValue:()I
            //   721: iconst_1
            //   722: iadd
            //   723: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   726: astore          9
            //   728: aload           10
            //   730: astore          12
            //   732: aload           9
            //   734: invokevirtual   java/lang/Integer.intValue:()I
            //   737: aload           12
            //   739: invokevirtual   java/lang/Integer.intValue:()I
            //   742: if_icmple       1128
            //   745: aload           18
            //   747: aload           17
            //   749: aload           9
            //   751: invokevirtual   java/lang/Integer.intValue:()I
            //   754: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   757: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   760: astore          10
            //   762: aload           10
            //   764: astore          5
            //   766: aload           10
            //   768: invokevirtual   java/lang/Integer.intValue:()I
            //   771: aload           9
            //   773: invokevirtual   java/lang/Integer.intValue:()I
            //   776: if_icmpge       789
            //   779: aload           18
            //   781: invokevirtual   java/lang/String.length:()I
            //   784: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   787: astore          5
            //   789: aload           5
            //   791: invokevirtual   java/lang/Integer.intValue:()I
            //   794: aload           9
            //   796: invokevirtual   java/lang/Integer.intValue:()I
            //   799: if_icmplt       1128
            //   802: aload           18
            //   804: aload           9
            //   806: invokevirtual   java/lang/Integer.intValue:()I
            //   809: aload           5
            //   811: invokevirtual   java/lang/Integer.intValue:()I
            //   814: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   817: astore          4
            //   819: aload           4
            //   821: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
            //   824: astore          4
            //   826: goto            835
            //   829: iconst_0
            //   830: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   833: astore          4
            //   835: aload           5
            //   837: invokevirtual   java/lang/Integer.intValue:()I
            //   840: iconst_1
            //   841: iadd
            //   842: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   845: astore          9
            //   847: goto            1128
            //   850: aload           7
            //   852: invokevirtual   java/lang/String.length:()I
            //   855: ifle            891
            //   858: aload_0
            //   859: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   862: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$1700:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   865: new             Lcom/erwinvoogt/soarcast/WelkomSoarCast$Locatie;
            //   868: dup
            //   869: aload_0
            //   870: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   873: aload           7
            //   875: aload           10
            //   877: aload_3
            //   878: aload_1
            //   879: aload           5
            //   881: aload           4
            //   883: aconst_null
            //   884: invokespecial   com/erwinvoogt/soarcast/WelkomSoarCast$Locatie.<init>:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Integer;Ljava/lang/Integer;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //   887: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //   890: pop
            //   891: aload           15
            //   893: invokevirtual   java/lang/Integer.intValue:()I
            //   896: iconst_1
            //   897: iadd
            //   898: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   901: astore          15
            //   903: aload           10
            //   905: astore          8
            //   907: goto            211
            //   910: aload           13
            //   912: ifnull          920
            //   915: aload           13
            //   917: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   920: aload           14
            //   922: ifnull          945
            //   925: aload           14
            //   927: invokevirtual   java/io/BufferedReader.close:()V
            //   930: aload           15
            //   932: areturn
            //   933: astore_1
            //   934: aload_0
            //   935: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.LOG_TAG:Ljava/lang/String;
            //   938: ldc             "Error closing stream"
            //   940: aload_1
            //   941: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   944: pop
            //   945: aload           15
            //   947: areturn
            //   948: astore_3
            //   949: aload           14
            //   951: astore_1
            //   952: goto            965
            //   955: astore_3
            //   956: aload           14
            //   958: astore_1
            //   959: goto            977
            //   962: astore_3
            //   963: aconst_null
            //   964: astore_1
            //   965: aload_3
            //   966: astore          4
            //   968: aload           13
            //   970: astore_3
            //   971: goto            1054
            //   974: astore_3
            //   975: aconst_null
            //   976: astore_1
            //   977: aload_3
            //   978: astore          4
            //   980: aload           13
            //   982: astore_3
            //   983: goto            1000
            //   986: astore_1
            //   987: aconst_null
            //   988: astore          5
            //   990: aload           4
            //   992: astore_3
            //   993: goto            1060
            //   996: astore          4
            //   998: aconst_null
            //   999: astore_1
            //  1000: aload_0
            //  1001: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.LOG_TAG:Ljava/lang/String;
            //  1004: ldc             "Error "
            //  1006: aload           4
            //  1008: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1011: pop
            //  1012: iconst_m1
            //  1013: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1016: astore          4
            //  1018: aload_3
            //  1019: ifnull          1026
            //  1022: aload_3
            //  1023: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1026: aload_1
            //  1027: ifnull          1049
            //  1030: aload_1
            //  1031: invokevirtual   java/io/BufferedReader.close:()V
            //  1034: aload           4
            //  1036: areturn
            //  1037: astore_1
            //  1038: aload_0
            //  1039: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.LOG_TAG:Ljava/lang/String;
            //  1042: ldc             "Error closing stream"
            //  1044: aload_1
            //  1045: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1048: pop
            //  1049: aload           4
            //  1051: areturn
            //  1052: astore          4
            //  1054: aload_1
            //  1055: astore          5
            //  1057: aload           4
            //  1059: astore_1
            //  1060: aload_3
            //  1061: ifnull          1068
            //  1064: aload_3
            //  1065: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1068: aload           5
            //  1070: ifnull          1093
            //  1073: aload           5
            //  1075: invokevirtual   java/io/BufferedReader.close:()V
            //  1078: goto            1093
            //  1081: astore_3
            //  1082: aload_0
            //  1083: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesLocaties.LOG_TAG:Ljava/lang/String;
            //  1086: ldc             "Error closing stream"
            //  1088: aload_3
            //  1089: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1092: pop
            //  1093: aload_1
            //  1094: athrow
            //  1095: astore          6
            //  1097: goto            392
            //  1100: astore_3
            //  1101: goto            493
            //  1104: astore_1
            //  1105: goto            592
            //  1108: astore          5
            //  1110: goto            710
            //  1113: astore          4
            //  1115: goto            829
            //  1118: goto            517
            //  1121: aload           9
            //  1123: astore          11
            //  1125: goto            616
            //  1128: aload           6
            //  1130: astore          10
            //  1132: aload           8
            //  1134: astore          5
            //  1136: aload           9
            //  1138: astore          6
            //  1140: goto            850
            //  1143: aload           8
            //  1145: astore          10
            //  1147: goto            850
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ---------------------------------
            //  89     105    996    1000   Ljava/io/IOException;
            //  89     105    986    996    Any
            //  105    123    974    977    Ljava/io/IOException;
            //  105    123    962    965    Any
            //  142    159    974    977    Ljava/io/IOException;
            //  142    159    962    965    Any
            //  163    211    955    962    Ljava/io/IOException;
            //  163    211    948    955    Any
            //  211    218    955    962    Ljava/io/IOException;
            //  211    218    948    955    Any
            //  223    243    955    962    Ljava/io/IOException;
            //  223    243    948    955    Any
            //  247    280    955    962    Ljava/io/IOException;
            //  247    280    948    955    Any
            //  280    286    955    962    Ljava/io/IOException;
            //  280    286    948    955    Any
            //  306    332    955    962    Ljava/io/IOException;
            //  306    332    948    955    Any
            //  348    378    955    962    Ljava/io/IOException;
            //  348    378    948    955    Any
            //  378    385    1095   402    Ljava/lang/NumberFormatException;
            //  378    385    955    962    Ljava/io/IOException;
            //  378    385    948    955    Any
            //  392    398    955    962    Ljava/io/IOException;
            //  392    398    948    955    Any
            //  402    414    955    962    Ljava/io/IOException;
            //  402    414    948    955    Any
            //  422    452    955    962    Ljava/io/IOException;
            //  422    452    948    955    Any
            //  456    485    955    962    Ljava/io/IOException;
            //  456    485    948    955    Any
            //  485    490    1100   498    Ljava/lang/NumberFormatException;
            //  485    490    955    962    Ljava/io/IOException;
            //  485    490    948    955    Any
            //  493    498    955    962    Ljava/io/IOException;
            //  493    498    948    955    Any
            //  498    510    955    962    Ljava/io/IOException;
            //  498    510    948    955    Any
            //  521    551    955    962    Ljava/io/IOException;
            //  521    551    948    955    Any
            //  555    584    955    962    Ljava/io/IOException;
            //  555    584    948    955    Any
            //  584    589    1104   597    Ljava/lang/NumberFormatException;
            //  584    589    955    962    Ljava/io/IOException;
            //  584    589    948    955    Any
            //  592    597    955    962    Ljava/io/IOException;
            //  592    597    948    955    Any
            //  597    609    955    962    Ljava/io/IOException;
            //  597    609    948    955    Any
            //  628    658    955    962    Ljava/io/IOException;
            //  628    658    948    955    Any
            //  670    700    955    962    Ljava/io/IOException;
            //  670    700    948    955    Any
            //  700    707    1108   716    Ljava/lang/NumberFormatException;
            //  700    707    955    962    Ljava/io/IOException;
            //  700    707    948    955    Any
            //  710    716    955    962    Ljava/io/IOException;
            //  710    716    948    955    Any
            //  716    728    955    962    Ljava/io/IOException;
            //  716    728    948    955    Any
            //  732    762    955    962    Ljava/io/IOException;
            //  732    762    948    955    Any
            //  766    789    955    962    Ljava/io/IOException;
            //  766    789    948    955    Any
            //  789    819    955    962    Ljava/io/IOException;
            //  789    819    948    955    Any
            //  819    826    1113   835    Ljava/lang/NumberFormatException;
            //  819    826    955    962    Ljava/io/IOException;
            //  819    826    948    955    Any
            //  829    835    955    962    Ljava/io/IOException;
            //  829    835    948    955    Any
            //  835    847    955    962    Ljava/io/IOException;
            //  835    847    948    955    Any
            //  850    891    955    962    Ljava/io/IOException;
            //  850    891    948    955    Any
            //  891    903    955    962    Ljava/io/IOException;
            //  891    903    948    955    Any
            //  925    930    933    945    Ljava/io/IOException;
            //  1000   1018   1052   1054   Any
            //  1030   1034   1037   1049   Ljava/io/IOException;
            //  1073   1078   1081   1093   Ljava/io/IOException;
            //
            // The error that occurred was:
            //
            // java.lang.IndexOutOfBoundsException: Index 540 out of bounds for length 540
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            //

            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        private Locatie createLocation(String[] metadata) {
            String naam = metadata[0];
            Integer id = Integer.valueOf(metadata[1]);
            Double lat = Double.valueOf(metadata[2]);
            Double lon = Double.valueOf(metadata[3]);
            Integer mindeg = Integer.valueOf(metadata[4]);
            Integer maxdeg = Integer.valueOf(metadata[5]);

            return new Locatie(naam, id, lat, lon, maxdeg, mindeg);
        }

        protected void onPostExecute(final Integer result) {
            if (result > 0) {
                WelkomSoarCast.this.locIndexMax = WelkomSoarCast.this.mLocatie.size() - 1;
                if (WelkomSoarCast.this.locIndex > WelkomSoarCast.this.locIndexMax) {
                    WelkomSoarCast.this.locIndex = WelkomSoarCast.this.locIndexMax / 2;
                }
                WelkomSoarCast.this.updateLocatieWindRichting();
                return;
            }
            Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private class LeesRichting extends AsyncTask<Integer, Void, Integer>
    {
        private final String LOG_TAG;

        private LeesRichting() {
            this.LOG_TAG = LeesRichting.class.getSimpleName();
        }

        // todo: Test some urls:

        // Plot
        // http://www.soarcast.nl/sc/plotPerLocAndUnit.php?activity=238&day=2020-09-20&day=today&windUnits=ms&forecastToShow=harm&runsToShow=lastOnly&run=2020092000

        // JSON
        // http://www.soarcast.nl/sc/plotPerLocAndUnit.php?json&activity=238&day=2020-09-20&day=today&windUnits=ms&forecastToShow=harm&runsToShow=lastOnly&run=2020092000

        // CSV
        //http://www.soarcast.nl/sc/plotPerLocAndUnit.php?csv&unit=deg&location=61&day=today&windUnits=ms&forecastToShow=harm&runsToShow=lastOnly&activity_id=238&canvas=cnv_61_deg
        // TODO: Implement translation of bytecode
        protected Integer doInBackground(final Integer... p0) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     1: iconst_0
            //     2: aaload
            //     3: astore          18
            //     5: aload_0
            //     6: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //     9: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    12: ldc             R.string.csv_separator
            //    14: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    17: astore_1
            //    18: iconst_0
            //    19: istore_2
            //    20: dconst_0
            //    21: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    24: astore          9
            //    26: dconst_0
            //    27: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    30: astore          8
            //    32: dconst_0
            //    33: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    36: astore          6
            //    38: iload_2
            //    39: iconst_4
            //    40: if_icmpge       1165
            //    43: new             Ljava/lang/StringBuilder;
            //    46: dup
            //    47: invokespecial   java/lang/StringBuilder.<init>:()V
            //    50: astore          7
            //    52: aload           7
            //    54: aload_0
            //    55: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    58: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    61: ldc             2131427396 // TODO: url_soarcast (http://www.soarcast.nl/sc/)
            //    63: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    66: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    69: pop
            //    70: aload           7
            //    72: aload_0
            //    73: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    76: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    79: ldc             2131427381 // TODO: php_soarcast (plotPerLocAndUnit.php?)
            //    81: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    84: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    87: pop
            //    88: aload           7
            //    90: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    93: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
            //    96: invokevirtual   android/net/Uri.buildUpon:()Landroid/net/Uri.Builder;
            //    99: aload_0
            //   100: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   103: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   106: ldc             2131427387 // TODO: q_unit (unit)
            //   108: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   111: aload_0
            //   112: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   115: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   118: ldc             2131427374 // TODO: p_unit_richting
            //   120: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   123: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   126: aload_0
            //   127: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   130: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   133: ldc             2131427385 // TODO: q_location
            //   135: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   138: aload           18
            //   140: invokevirtual   java/lang/Integer.toString:()Ljava/lang/String;
            //   143: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   146: aload_0
            //   147: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   150: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   153: ldc             2131427384 // TODO: q_day
            //   155: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   158: aload_0
            //   159: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   162: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$4500:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)[Ljava/lang/String;
            //   165: iload_2
            //   166: aaload
            //   167: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   170: aload_0
            //   171: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   174: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   177: ldc             2131427386 // TODO: q_runsToShow
            //   179: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   182: aload_0
            //   183: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   186: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   189: ldc             2131427373 // TODO: p_runsToShow
            //   191: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   194: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   197: aload_0
            //   198: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   201: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   204: ldc             2131427382 // TODO: q_activity_id (1)
            //   206: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   209: aload_0
            //   210: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   213: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   216: ldc             2131427371 // TODO: p_activity_id (activity_id)
            //   218: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   221: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   224: aload_0
            //   225: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   228: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   231: ldc             2131427383 // TODO: q_csv
            //   233: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   236: ldc             ""
            //   238: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   241: invokevirtual   android/net/Uri.Builder.build:()Landroid/net/Uri;
            //   244: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
            //   247: astore          7
            //   249: new             Ljava/net/URL;
            //   252: dup
            //   253: aload           7
            //   255: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //   258: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //   261: checkcast       Ljava/net/HttpURLConnection;
            //   264: astore          7
            //   266: aload           7
            //   268: ldc             "GET"
            //   270: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
            //   273: aload           7
            //   275: invokevirtual   java/net/HttpURLConnection.connect:()V
            //   278: aload           7
            //   280: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
            //   283: astore          10
            //   285: aload           10
            //   287: ifnull          979
            //   290: new             Ljava/io/BufferedReader;
            //   293: dup
            //   294: new             Ljava/io/InputStreamReader;
            //   297: dup
            //   298: aload           10
            //   300: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
            //   303: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
            //   306: astore          11
            //   308: aload           11
            //   310: astore          12
            //   312: aload           11
            //   314: astore          13
            //   316: bipush          7
            //   318: anewarray       Ljava/lang/Integer;
            //   321: dup
            //   322: iconst_0
            //   323: iconst_m1
            //   324: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   327: aastore
            //   328: dup
            //   329: iconst_1
            //   330: iconst_m1
            //   331: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   334: aastore
            //   335: dup
            //   336: iconst_2
            //   337: iconst_m1
            //   338: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   341: aastore
            //   342: dup
            //   343: iconst_3
            //   344: iconst_m1
            //   345: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   348: aastore
            //   349: dup
            //   350: iconst_4
            //   351: iconst_m1
            //   352: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   355: aastore
            //   356: dup
            //   357: iconst_5
            //   358: iconst_m1
            //   359: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   362: aastore
            //   363: dup
            //   364: bipush          6
            //   366: iconst_m1
            //   367: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   370: aastore
            //   371: astore          15
            //   373: aload           11
            //   375: astore          12
            //   377: aload           11
            //   379: astore          13
            //   381: iconst_0
            //   382: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   385: astore          14
            //   387: aload_1
            //   388: astore          10
            //   390: aload           11
            //   392: astore_1
            //   393: aload           15
            //   395: astore          11
            //   397: aload_1
            //   398: astore          12
            //   400: aload_1
            //   401: astore          13
            //   403: aload_1
            //   404: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
            //   407: astore          19
            //   409: aload           19
            //   411: ifnull          942
            //   414: aload_1
            //   415: astore          12
            //   417: aload_1
            //   418: astore          13
            //   420: aload           14
            //   422: invokevirtual   java/lang/Integer.intValue:()I
            //   425: istore_3
            //   426: iload_3
            //   427: ifne            473
            //   430: aload_0
            //   431: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   434: aload           19
            //   436: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$4600:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/String;)[Ljava/lang/Integer;
            //   439: astore          11
            //   441: aload           11
            //   443: iconst_0
            //   444: aaload
            //   445: invokevirtual   java/lang/Integer.intValue:()I
            //   448: istore_3
            //   449: iload_3
            //   450: iconst_m1
            //   451: if_icmpne       473
            //   454: goto            942
            //   457: astore          8
            //   459: aload_1
            //   460: astore          6
            //   462: goto            957
            //   465: astore          8
            //   467: aload_1
            //   468: astore          6
            //   470: goto            969
            //   473: aload_1
            //   474: astore          12
            //   476: aload_1
            //   477: astore          13
            //   479: aload           14
            //   481: invokevirtual   java/lang/Integer.intValue:()I
            //   484: ifle            1274
            //   487: aload_1
            //   488: astore          12
            //   490: aload_1
            //   491: astore          13
            //   493: aload           19
            //   495: invokevirtual   java/lang/String.length:()I
            //   498: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   501: astore          17
            //   503: aload_1
            //   504: astore          12
            //   506: aload_1
            //   507: astore          13
            //   509: ldc2_w          -1.0
            //   512: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   515: astore          8
            //   517: aload_1
            //   518: astore          12
            //   520: aload_1
            //   521: astore          13
            //   523: ldc2_w          -1.0
            //   526: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   529: astore          6
            //   531: aload_1
            //   532: astore          12
            //   534: aload_1
            //   535: astore          13
            //   537: lconst_0
            //   538: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //   541: astore          15
            //   543: aload_1
            //   544: astore          12
            //   546: aload_1
            //   547: astore          13
            //   549: iconst_0
            //   550: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   553: astore          16
            //   555: aload_1
            //   556: astore          12
            //   558: aload_1
            //   559: astore          13
            //   561: ldc2_w          -1.0
            //   564: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   567: astore          9
            //   569: iconst_0
            //   570: istore_3
            //   571: aload_1
            //   572: astore          12
            //   574: aload_1
            //   575: astore          13
            //   577: aload           16
            //   579: invokevirtual   java/lang/Integer.intValue:()I
            //   582: istore          4
            //   584: aload_1
            //   585: astore          12
            //   587: aload_1
            //   588: astore          13
            //   590: aload           17
            //   592: invokevirtual   java/lang/Integer.intValue:()I
            //   595: istore          5
            //   597: iload           4
            //   599: iload           5
            //   601: if_icmpge       813
            //   604: iload_3
            //   605: iconst_4
            //   606: if_icmpge       813
            //   609: aload           19
            //   611: aload           10
            //   613: aload           16
            //   615: invokevirtual   java/lang/Integer.intValue:()I
            //   618: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   621: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   624: astore          12
            //   626: aload           12
            //   628: astore          13
            //   630: aload           12
            //   632: invokevirtual   java/lang/Integer.intValue:()I
            //   635: ifge            642
            //   638: aload           17
            //   640: astore          13
            //   642: aload           19
            //   644: aload           16
            //   646: invokevirtual   java/lang/Integer.intValue:()I
            //   649: aload           13
            //   651: invokevirtual   java/lang/Integer.intValue:()I
            //   654: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   657: astore          12
            //   659: aload           11
            //   661: iload_3
            //   662: aaload
            //   663: invokevirtual   java/lang/Integer.intValue:()I
            //   666: istore          4
            //   668: iload           4
            //   670: ifeq            774
            //   673: iload           4
            //   675: tableswitch {
            //               14: 749
            //               15: 728
            //               16: 707
            //          default: 700
            //        }
            //   700: aload           15
            //   702: astore          12
            //   704: goto            790
            //   707: aload           12
            //   709: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //   712: astore          6
            //   714: goto            700
            //   717: ldc2_w          -1.0
            //   720: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   723: astore          6
            //   725: goto            700
            //   728: aload           12
            //   730: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //   733: astore          8
            //   735: goto            700
            //   738: ldc2_w          -1.0
            //   741: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   744: astore          8
            //   746: goto            700
            //   749: aload           12
            //   751: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //   754: astore          9
            //   756: goto            700
            //   759: ldc2_w          -1.0
            //   762: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   765: astore          9
            //   767: aload           15
            //   769: astore          12
            //   771: goto            790
            //   774: aload           12
            //   776: invokestatic    java/lang/Long.valueOf:(Ljava/lang/String;)Ljava/lang/Long;
            //   779: astore          12
            //   781: goto            790
            //   784: lconst_0
            //   785: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //   788: astore          12
            //   790: iload_3
            //   791: iconst_1
            //   792: iadd
            //   793: istore_3
            //   794: aload           13
            //   796: invokevirtual   java/lang/Integer.intValue:()I
            //   799: iconst_1
            //   800: iadd
            //   801: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   804: astore          16
            //   806: aload           12
            //   808: astore          15
            //   810: goto            571
            //   813: aload_1
            //   814: astore          12
            //   816: aload_1
            //   817: astore          13
            //   819: aload           15
            //   821: invokevirtual   java/lang/Long.longValue:()J
            //   824: lconst_0
            //   825: lcmp
            //   826: ifle            1251
            //   829: aload_1
            //   830: astore          12
            //   832: aload_1
            //   833: astore          13
            //   835: aload_0
            //   836: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   839: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$2000:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //   842: astore          16
            //   844: aload_1
            //   845: astore          12
            //   847: aload_1
            //   848: astore          13
            //   850: aload_0
            //   851: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   854: astore          17
            //   856: aload           16
            //   858: new             Lcom/erwinvoogt/soarcast/WelkomSoarCast$Richting;
            //   861: dup
            //   862: aload           17
            //   864: aload           15
            //   866: iload_2
            //   867: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   870: aload           18
            //   872: aload           9
            //   874: aload           8
            //   876: aload           6
            //   878: aconst_null
            //   879: invokespecial   com/erwinvoogt/soarcast/WelkomSoarCast$Richting.<init>:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //   882: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //   885: pop
            //   886: goto            1251
            //   889: aload           14
            //   891: invokevirtual   java/lang/Integer.intValue:()I
            //   894: iconst_1
            //   895: iadd
            //   896: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   899: astore          14
            //   901: goto            397
            //   904: astore          6
            //   906: aload_1
            //   907: astore          8
            //   909: aload           6
            //   911: astore_1
            //   912: aload           8
            //   914: astore          6
            //   916: goto            1126
            //   919: astore          6
            //   921: aload           7
            //   923: astore          8
            //   925: aload_1
            //   926: astore          9
            //   928: aload           6
            //   930: astore          7
            //   932: aload           8
            //   934: astore_1
            //   935: aload           9
            //   937: astore          6
            //   939: goto            1064
            //   942: aload_1
            //   943: astore          11
            //   945: aload           10
            //   947: astore_1
            //   948: goto            982
            //   951: astore          8
            //   953: aload           12
            //   955: astore          6
            //   957: aload           8
            //   959: astore_1
            //   960: goto            1126
            //   963: astore          8
            //   965: aload           13
            //   967: astore          6
            //   969: aload           7
            //   971: astore_1
            //   972: aload           8
            //   974: astore          7
            //   976: goto            1064
            //   979: aconst_null
            //   980: astore          11
            //   982: aload           7
            //   984: ifnull          992
            //   987: aload           7
            //   989: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   992: aload           11
            //   994: ifnull          1019
            //   997: aload           11
            //   999: invokevirtual   java/io/BufferedReader.close:()V
            //  1002: goto            1019
            //  1005: astore          7
            //  1007: aload_0
            //  1008: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.LOG_TAG:Ljava/lang/String;
            //  1011: ldc             "Error closing stream"
            //  1013: aload           7
            //  1015: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1018: pop
            //  1019: iload_2
            //  1020: iconst_1
            //  1021: iadd
            //  1022: istore_2
            //  1023: goto            38
            //  1026: astore_1
            //  1027: aconst_null
            //  1028: astore          6
            //  1030: goto            1126
            //  1033: astore_1
            //  1034: aload           7
            //  1036: astore          6
            //  1038: aload_1
            //  1039: astore          7
            //  1041: aload           6
            //  1043: astore_1
            //  1044: goto            1061
            //  1047: astore_1
            //  1048: aconst_null
            //  1049: astore          6
            //  1051: aconst_null
            //  1052: astore          7
            //  1054: goto            1126
            //  1057: astore          7
            //  1059: aconst_null
            //  1060: astore_1
            //  1061: aconst_null
            //  1062: astore          6
            //  1064: aload_0
            //  1065: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.LOG_TAG:Ljava/lang/String;
            //  1068: ldc             "Error "
            //  1070: aload           7
            //  1072: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1075: pop
            //  1076: iconst_m1
            //  1077: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1080: astore          7
            //  1082: aload_1
            //  1083: ifnull          1090
            //  1086: aload_1
            //  1087: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1090: aload           6
            //  1092: ifnull          1115
            //  1095: aload           6
            //  1097: invokevirtual   java/io/BufferedReader.close:()V
            //  1100: aload           7
            //  1102: areturn
            //  1103: astore_1
            //  1104: aload_0
            //  1105: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.LOG_TAG:Ljava/lang/String;
            //  1108: ldc             "Error closing stream"
            //  1110: aload_1
            //  1111: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1114: pop
            //  1115: aload           7
            //  1117: areturn
            //  1118: astore          8
            //  1120: aload_1
            //  1121: astore          7
            //  1123: aload           8
            //  1125: astore_1
            //  1126: aload           7
            //  1128: ifnull          1136
            //  1131: aload           7
            //  1133: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1136: aload           6
            //  1138: ifnull          1163
            //  1141: aload           6
            //  1143: invokevirtual   java/io/BufferedReader.close:()V
            //  1146: goto            1163
            //  1149: astore          6
            //  1151: aload_0
            //  1152: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.LOG_TAG:Ljava/lang/String;
            //  1155: ldc             "Error closing stream"
            //  1157: aload           6
            //  1159: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1162: pop
            //  1163: aload_1
            //  1164: athrow
            //  1165: aload_0
            //  1166: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1169: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$2000:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //  1172: invokevirtual   java/util/ArrayList.size:()I
            //  1175: iconst_1
            //  1176: if_icmpge       1226
            //  1179: aload_0
            //  1180: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1183: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$2000:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //  1186: new             Lcom/erwinvoogt/soarcast/WelkomSoarCast$Richting;
            //  1189: dup
            //  1190: aload_0
            //  1191: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesRichting.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1194: ldc2_w          1512514800
            //  1197: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //  1200: iconst_0
            //  1201: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1204: aload           18
            //  1206: aload           8
            //  1208: aload           6
            //  1210: aload           9
            //  1212: aconst_null
            //  1213: invokespecial   com/erwinvoogt/soarcast/WelkomSoarCast$Richting.<init>:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //  1216: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //  1219: pop
            //  1220: bipush          -2
            //  1222: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1225: areturn
            //  1226: iconst_1
            //  1227: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1230: areturn
            //  1231: astore          6
            //  1233: goto            717
            //  1236: astore          8
            //  1238: goto            738
            //  1241: astore          9
            //  1243: goto            759
            //  1246: astore          12
            //  1248: goto            784
            //  1251: aload           9
            //  1253: astore          12
            //  1255: aload           8
            //  1257: astore          13
            //  1259: aload           6
            //  1261: astore          9
            //  1263: aload           12
            //  1265: astore          8
            //  1267: aload           13
            //  1269: astore          6
            //  1271: goto            889
            //  1274: goto            889
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ---------------------------------
            //  249    266    1057   1061   Ljava/io/IOException;
            //  249    266    1047   1057   Any
            //  266    285    1033   1047   Ljava/io/IOException;
            //  266    285    1026   1033   Any
            //  290    308    1033   1047   Ljava/io/IOException;
            //  290    308    1026   1033   Any
            //  316    373    963    969    Ljava/io/IOException;
            //  316    373    951    957    Any
            //  381    387    963    969    Ljava/io/IOException;
            //  381    387    951    957    Any
            //  403    409    963    969    Ljava/io/IOException;
            //  403    409    951    957    Any
            //  420    426    963    969    Ljava/io/IOException;
            //  420    426    951    957    Any
            //  430    449    465    473    Ljava/io/IOException;
            //  430    449    457    465    Any
            //  479    487    963    969    Ljava/io/IOException;
            //  479    487    951    957    Any
            //  493    503    963    969    Ljava/io/IOException;
            //  493    503    951    957    Any
            //  509    517    963    969    Ljava/io/IOException;
            //  509    517    951    957    Any
            //  523    531    963    969    Ljava/io/IOException;
            //  523    531    951    957    Any
            //  537    543    963    969    Ljava/io/IOException;
            //  537    543    951    957    Any
            //  549    555    963    969    Ljava/io/IOException;
            //  549    555    951    957    Any
            //  561    569    963    969    Ljava/io/IOException;
            //  561    569    951    957    Any
            //  577    584    963    969    Ljava/io/IOException;
            //  577    584    951    957    Any
            //  590    597    963    969    Ljava/io/IOException;
            //  590    597    951    957    Any
            //  609    626    465    473    Ljava/io/IOException;
            //  609    626    457    465    Any
            //  630    638    465    473    Ljava/io/IOException;
            //  630    638    457    465    Any
            //  642    668    465    473    Ljava/io/IOException;
            //  642    668    457    465    Any
            //  707    714    1231   728    Ljava/lang/NumberFormatException;
            //  707    714    465    473    Ljava/io/IOException;
            //  707    714    457    465    Any
            //  717    725    465    473    Ljava/io/IOException;
            //  717    725    457    465    Any
            //  728    735    1236   749    Ljava/lang/NumberFormatException;
            //  728    735    465    473    Ljava/io/IOException;
            //  728    735    457    465    Any
            //  738    746    465    473    Ljava/io/IOException;
            //  738    746    457    465    Any
            //  749    756    1241   774    Ljava/lang/NumberFormatException;
            //  749    756    465    473    Ljava/io/IOException;
            //  749    756    457    465    Any
            //  759    767    465    473    Ljava/io/IOException;
            //  759    767    457    465    Any
            //  774    781    1246   790    Ljava/lang/NumberFormatException;
            //  774    781    465    473    Ljava/io/IOException;
            //  774    781    457    465    Any
            //  784    790    465    473    Ljava/io/IOException;
            //  784    790    457    465    Any
            //  794    806    465    473    Ljava/io/IOException;
            //  794    806    457    465    Any
            //  819    829    963    969    Ljava/io/IOException;
            //  819    829    951    957    Any
            //  835    844    963    969    Ljava/io/IOException;
            //  835    844    951    957    Any
            //  850    856    963    969    Ljava/io/IOException;
            //  850    856    951    957    Any
            //  856    886    919    942    Ljava/io/IOException;
            //  856    886    904    919    Any
            //  889    901    919    942    Ljava/io/IOException;
            //  889    901    904    919    Any
            //  997    1002   1005   1019   Ljava/io/IOException;
            //  1064   1082   1118   1126   Any
            //  1095   1100   1103   1115   Ljava/io/IOException;
            //  1141   1146   1149   1163   Ljava/io/IOException;
            //
            // The error that occurred was:
            //
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        protected void onPostExecute(final Integer n) {
            if (n < 1) {
                if (n == -2) {
                    Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                WelkomSoarCast.this.tijdNul = WelkomSoarCast.this.mRichting.get(0).unixTimestamp;
            }
            WelkomSoarCast.this.updateRichting();
            WelkomSoarCast.this.updateRichtingModel();
            ((RichtingKaderView)WelkomSoarCast.this.findViewById(R.id.richtingKaderView)).update(WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex).mindeg, WelkomSoarCast.this.mLocatie.get(WelkomSoarCast.this.locIndex).maxdeg, WelkomSoarCast.this.richt, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.mRichting.get(0).geefZonOpOnder());
            WelkomSoarCast.this.ikBenKlaar();
        }
    }

    private class LeesWind extends AsyncTask<Integer, Void, Integer>
    {
        private final String LOG_TAG;

        private LeesWind() {
            this.LOG_TAG = LeesWind.class.getSimpleName();
        }

        // TODO: Implement translation of bytecode
        protected Integer doInBackground(final Integer... p0) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     1: iconst_0
            //     2: aaload
            //     3: astore_1
            //     4: aload_0
            //     5: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //     8: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    11: ldc             R.string.csv_separator
            //    13: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    16: astore          8
            //    18: lconst_0
            //    19: lstore          5
            //    21: lconst_0
            //    22: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //    25: astore          15
            //    27: dconst_0
            //    28: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    31: astore          9
            //    33: dconst_0
            //    34: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    37: astore          10
            //    39: dconst_0
            //    40: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    43: astore          11
            //    45: dconst_0
            //    46: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    49: astore          12
            //    51: dconst_0
            //    52: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    55: astore          13
            //    57: iconst_0
            //    58: istore_2
            //    59: dconst_0
            //    60: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //    63: astore          14
            //    65: iload_2
            //    66: iconst_4
            //    67: if_icmpge       1785
            //    70: new             Ljava/lang/StringBuilder;
            //    73: dup
            //    74: invokespecial   java/lang/StringBuilder.<init>:()V
            //    77: astore          7
            //    79: aload           7
            //    81: aload_0
            //    82: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //    85: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //    88: ldc             2131427396 // TODO: url_soarcast (http://www.soarcast.nl/sc/)
            //    90: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //    93: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    96: pop
            //    97: aload           7
            //    99: aload_0
            //   100: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   103: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   106: ldc             2131427381 // TODO php_soarcast (plotPerLocAndUnit.php?)
            //   108: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   111: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   114: pop
            //   115: aload           7
            //   117: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   120: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
            //   123: invokevirtual   android/net/Uri.buildUpon:()Landroid/net/Uri.Builder;
            //   126: aload_0
            //   127: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   130: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   133: ldc             2131427387 // TODO: q_unit (unit)
            //   135: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   138: aload_0
            //   139: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   142: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   145: ldc             2131427375 // TODO: p_unit_wind (m/s)
            //   147: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   150: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   153: aload_0
            //   154: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   157: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   160: ldc             2131427385 // TODO: q_location (location)
            //   162: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   165: aload_1
            //   166: invokevirtual   java/lang/Integer.toString:()Ljava/lang/String;
            //   169: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   172: aload_0
            //   173: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   176: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   179: ldc             2131427384 // TODO: q_day (day
            //   181: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   184: aload_0
            //   185: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   188: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$4500:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)[Ljava/lang/String;
            //   191: iload_2
            //   192: aaload
            //   193: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   196: aload_0
            //   197: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   200: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   203: ldc             2131427386 // TODO: q_runsToShow (runsToShow)
            //   205: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   208: aload_0
            //   209: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   212: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   215: ldc             2131427373 // TODO: p_runsToShow (lastOnly)
            //   217: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   220: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   223: aload_0
            //   224: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   227: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   230: ldc             2131427382 TODO: q_activity_id (activity_id)
            //   232: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   235: aload_0
            //   236: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   239: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   242: ldc             2131427371 TODO: p_activity_id (1)
            //   244: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   247: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   250: aload_0
            //   251: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   254: invokevirtual   com/erwinvoogt/soarcast/WelkomSoarCast.getResources:()Landroid/content/res/Resources;
            //   257: ldc             2131427383 TODO: q_csv (csv)
            //   259: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
            //   262: ldc             ""
            //   264: invokevirtual   android/net/Uri.Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri.Builder;
            //   267: invokevirtual   android/net/Uri.Builder.build:()Landroid/net/Uri;
            //   270: invokevirtual   android/net/Uri.toString:()Ljava/lang/String;
            //   273: astore          7
            //   275: new             Ljava/net/URL;
            //   278: dup
            //   279: aload           7
            //   281: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //   284: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //   287: checkcast       Ljava/net/HttpURLConnection;
            //   290: astore          7
            //   292: aload           7
            //   294: ldc             "GET"
            //   296: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
            //   299: aload           7
            //   301: invokevirtual   java/net/HttpURLConnection.connect:()V
            //   304: aload           7
            //   306: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
            //   309: astore          16
            //   311: aload           16
            //   313: ifnull          1563
            //   316: new             Ljava/io/BufferedReader;
            //   319: dup
            //   320: new             Ljava/io/InputStreamReader;
            //   323: dup
            //   324: aload           16
            //   326: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
            //   329: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
            //   332: astore          17
            //   334: aload           7
            //   336: astore          19
            //   338: aload           17
            //   340: astore          18
            //   342: aload           7
            //   344: astore          20
            //   346: aload           17
            //   348: astore          21
            //   350: bipush          7
            //   352: anewarray       Ljava/lang/Integer;
            //   355: astore          23
            //   357: aload           7
            //   359: astore          19
            //   361: aload           17
            //   363: astore          18
            //   365: aload           7
            //   367: astore          20
            //   369: aload           17
            //   371: astore          21
            //   373: aload           23
            //   375: iconst_0
            //   376: iconst_m1
            //   377: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   380: aastore
            //   381: aload           7
            //   383: astore          19
            //   385: aload           17
            //   387: astore          18
            //   389: aload           7
            //   391: astore          20
            //   393: aload           17
            //   395: astore          21
            //   397: aload           23
            //   399: iconst_1
            //   400: iconst_m1
            //   401: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   404: aastore
            //   405: aload           7
            //   407: astore          19
            //   409: aload           17
            //   411: astore          18
            //   413: aload           7
            //   415: astore          20
            //   417: aload           17
            //   419: astore          21
            //   421: aload           23
            //   423: iconst_2
            //   424: iconst_m1
            //   425: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   428: aastore
            //   429: aload           7
            //   431: astore          19
            //   433: aload           17
            //   435: astore          18
            //   437: aload           7
            //   439: astore          20
            //   441: aload           17
            //   443: astore          21
            //   445: aload           23
            //   447: iconst_3
            //   448: iconst_m1
            //   449: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   452: aastore
            //   453: aload           7
            //   455: astore          19
            //   457: aload           17
            //   459: astore          18
            //   461: aload           7
            //   463: astore          20
            //   465: aload           17
            //   467: astore          21
            //   469: aload           23
            //   471: iconst_4
            //   472: iconst_m1
            //   473: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   476: aastore
            //   477: aload           7
            //   479: astore          19
            //   481: aload           17
            //   483: astore          18
            //   485: aload           7
            //   487: astore          20
            //   489: aload           17
            //   491: astore          21
            //   493: aload           23
            //   495: iconst_5
            //   496: iconst_m1
            //   497: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   500: aastore
            //   501: aload           7
            //   503: astore          19
            //   505: aload           17
            //   507: astore          18
            //   509: aload           7
            //   511: astore          20
            //   513: aload           17
            //   515: astore          21
            //   517: aload           23
            //   519: bipush          6
            //   521: iconst_m1
            //   522: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   525: aastore
            //   526: aload           7
            //   528: astore          19
            //   530: aload           17
            //   532: astore          18
            //   534: aload           7
            //   536: astore          20
            //   538: aload           17
            //   540: astore          21
            //   542: iconst_0
            //   543: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   546: astore          22
            //   548: aload_1
            //   549: astore          16
            //   551: aload           17
            //   553: astore_1
            //   554: aload           23
            //   556: astore          17
            //   558: aload           7
            //   560: astore          19
            //   562: aload_1
            //   563: astore          18
            //   565: aload           7
            //   567: astore          20
            //   569: aload_1
            //   570: astore          21
            //   572: aload_1
            //   573: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
            //   576: astore          25
            //   578: aload           25
            //   580: ifnull          1523
            //   583: aload           7
            //   585: astore          19
            //   587: aload_1
            //   588: astore          18
            //   590: aload           7
            //   592: astore          20
            //   594: aload_1
            //   595: astore          21
            //   597: aload           22
            //   599: invokevirtual   java/lang/Integer.intValue:()I
            //   602: istore_3
            //   603: iload_3
            //   604: ifne            660
            //   607: aload           7
            //   609: astore          18
            //   611: aload_1
            //   612: astore          19
            //   614: aload_0
            //   615: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //   618: aload           25
            //   620: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$4600:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/String;)[Ljava/lang/Integer;
            //   623: astore          17
            //   625: aload           7
            //   627: astore          18
            //   629: aload_1
            //   630: astore          19
            //   632: aload           17
            //   634: iconst_0
            //   635: aaload
            //   636: invokevirtual   java/lang/Integer.intValue:()I
            //   639: istore_3
            //   640: iload_3
            //   641: iconst_m1
            //   642: if_icmpne       660
            //   645: goto            1523
            //   648: astore          8
            //   650: aload           7
            //   652: astore          20
            //   654: aload_1
            //   655: astore          7
            //   657: goto            1557
            //   660: aload           7
            //   662: astore          19
            //   664: aload_1
            //   665: astore          18
            //   667: aload           7
            //   669: astore          20
            //   671: aload_1
            //   672: astore          21
            //   674: aload           22
            //   676: invokevirtual   java/lang/Integer.intValue:()I
            //   679: istore_3
            //   680: iload_3
            //   681: ifle            1272
            //   684: aload           7
            //   686: astore          18
            //   688: aload_1
            //   689: astore          19
            //   691: iconst_0
            //   692: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   695: astore          23
            //   697: aload           7
            //   699: astore          18
            //   701: aload_1
            //   702: astore          19
            //   704: aload           25
            //   706: invokevirtual   java/lang/String.length:()I
            //   709: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   712: astore          15
            //   714: aload           7
            //   716: astore          18
            //   718: aload_1
            //   719: astore          19
            //   721: dconst_0
            //   722: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   725: astore          21
            //   727: aload           7
            //   729: astore          18
            //   731: aload_1
            //   732: astore          19
            //   734: dconst_0
            //   735: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   738: astore          14
            //   740: aload           7
            //   742: astore          18
            //   744: aload_1
            //   745: astore          19
            //   747: dconst_0
            //   748: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   751: astore          11
            //   753: aload           7
            //   755: astore          18
            //   757: aload_1
            //   758: astore          19
            //   760: dconst_0
            //   761: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   764: astore          13
            //   766: aload           7
            //   768: astore          18
            //   770: aload_1
            //   771: astore          19
            //   773: dconst_0
            //   774: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   777: astore          10
            //   779: aload           7
            //   781: astore          18
            //   783: aload_1
            //   784: astore          19
            //   786: dconst_0
            //   787: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   790: astore          12
            //   792: aload           7
            //   794: astore          18
            //   796: aload_1
            //   797: astore          19
            //   799: lconst_0
            //   800: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //   803: astore          20
            //   805: iconst_0
            //   806: istore_3
            //   807: aload           8
            //   809: astore          9
            //   811: aload           21
            //   813: astore          8
            //   815: aload           7
            //   817: astore          18
            //   819: aload_1
            //   820: astore          19
            //   822: aload           23
            //   824: invokevirtual   java/lang/Integer.intValue:()I
            //   827: aload           15
            //   829: invokevirtual   java/lang/Integer.intValue:()I
            //   832: if_icmpge       1237
            //   835: iload_3
            //   836: bipush          7
            //   838: if_icmpge       1237
            //   841: aload           7
            //   843: astore          18
            //   845: aload_1
            //   846: astore          19
            //   848: aload           25
            //   850: aload           9
            //   852: aload           23
            //   854: invokevirtual   java/lang/Integer.intValue:()I
            //   857: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   860: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   863: astore          24
            //   865: aload           24
            //   867: astore          21
            //   869: aload           7
            //   871: astore          18
            //   873: aload_1
            //   874: astore          19
            //   876: aload           24
            //   878: invokevirtual   java/lang/Integer.intValue:()I
            //   881: ifge            888
            //   884: aload           15
            //   886: astore          21
            //   888: aload           7
            //   890: astore          18
            //   892: aload_1
            //   893: astore          19
            //   895: aload           25
            //   897: aload           23
            //   899: invokevirtual   java/lang/Integer.intValue:()I
            //   902: aload           21
            //   904: invokevirtual   java/lang/Integer.intValue:()I
            //   907: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   910: astore          23
            //   912: aload           7
            //   914: astore          18
            //   916: aload_1
            //   917: astore          19
            //   919: aload           17
            //   921: iload_3
            //   922: aaload
            //   923: invokevirtual   java/lang/Integer.intValue:()I
            //   926: istore          4
            //   928: iload           4
            //   930: tableswitch {
            //                0: 1166
            //                1: 1133
            //                2: 1100
            //                3: 1067
            //                4: 1034
            //                5: 1001
            //                6: 975
            //          default: 972
            //        }
            //   972: goto            1211
            //   975: aload           7
            //   977: astore          18
            //   979: aload_1
            //   980: astore          19
            //   982: aload           23
            //   984: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //   987: astore          8
            //   989: goto            972
            //   992: dconst_0
            //   993: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //   996: astore          8
            //   998: goto            1211
            //  1001: aload           7
            //  1003: astore          18
            //  1005: aload_1
            //  1006: astore          19
            //  1008: aload           23
            //  1010: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //  1013: astore          14
            //  1015: goto            1211
            //  1018: aload           7
            //  1020: astore          18
            //  1022: aload_1
            //  1023: astore          19
            //  1025: dconst_0
            //  1026: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //  1029: astore          14
            //  1031: goto            1015
            //  1034: aload           7
            //  1036: astore          18
            //  1038: aload_1
            //  1039: astore          19
            //  1041: aload           23
            //  1043: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //  1046: astore          11
            //  1048: goto            1211
            //  1051: aload           7
            //  1053: astore          18
            //  1055: aload_1
            //  1056: astore          19
            //  1058: dconst_0
            //  1059: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //  1062: astore          11
            //  1064: goto            1048
            //  1067: aload           7
            //  1069: astore          18
            //  1071: aload_1
            //  1072: astore          19
            //  1074: aload           23
            //  1076: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //  1079: astore          13
            //  1081: goto            1211
            //  1084: aload           7
            //  1086: astore          18
            //  1088: aload_1
            //  1089: astore          19
            //  1091: dconst_0
            //  1092: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //  1095: astore          13
            //  1097: goto            1081
            //  1100: aload           7
            //  1102: astore          18
            //  1104: aload_1
            //  1105: astore          19
            //  1107: aload           23
            //  1109: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //  1112: astore          10
            //  1114: goto            1211
            //  1117: aload           7
            //  1119: astore          18
            //  1121: aload_1
            //  1122: astore          19
            //  1124: dconst_0
            //  1125: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //  1128: astore          10
            //  1130: goto            1114
            //  1133: aload           7
            //  1135: astore          18
            //  1137: aload_1
            //  1138: astore          19
            //  1140: aload           23
            //  1142: invokestatic    java/lang/Double.valueOf:(Ljava/lang/String;)Ljava/lang/Double;
            //  1145: astore          12
            //  1147: goto            1211
            //  1150: aload           7
            //  1152: astore          18
            //  1154: aload_1
            //  1155: astore          19
            //  1157: dconst_0
            //  1158: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
            //  1161: astore          12
            //  1163: goto            1147
            //  1166: aload           7
            //  1168: astore          18
            //  1170: aload_1
            //  1171: astore          19
            //  1173: aload           23
            //  1175: invokestatic    java/lang/Long.valueOf:(Ljava/lang/String;)Ljava/lang/Long;
            //  1178: astore          20
            //  1180: aload           20
            //  1182: astore          18
            //  1184: aload           18
            //  1186: astore          20
            //  1188: goto            1211
            //  1191: aload           7
            //  1193: astore          18
            //  1195: aload_1
            //  1196: astore          19
            //  1198: lconst_0
            //  1199: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //  1202: astore          20
            //  1204: aload           20
            //  1206: astore          18
            //  1208: goto            1184
            //  1211: iload_3
            //  1212: iconst_1
            //  1213: iadd
            //  1214: istore_3
            //  1215: aload           7
            //  1217: astore          18
            //  1219: aload_1
            //  1220: astore          19
            //  1222: aload           21
            //  1224: invokevirtual   java/lang/Integer.intValue:()I
            //  1227: iconst_1
            //  1228: iadd
            //  1229: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1232: astore          23
            //  1234: goto            815
            //  1237: aload           14
            //  1239: astore          18
            //  1241: aload           8
            //  1243: astore          19
            //  1245: aload           20
            //  1247: astore          8
            //  1249: aload           12
            //  1251: astore          15
            //  1253: aload           10
            //  1255: astore          14
            //  1257: aload           11
            //  1259: astore          12
            //  1261: aload           18
            //  1263: astore          11
            //  1265: aload           19
            //  1267: astore          10
            //  1269: goto            1324
            //  1272: aload           8
            //  1274: astore          18
            //  1276: aload           15
            //  1278: astore          8
            //  1280: aload           11
            //  1282: astore          15
            //  1284: aload           10
            //  1286: astore          19
            //  1288: aload           9
            //  1290: astore          20
            //  1292: aload           14
            //  1294: astore          21
            //  1296: aload           18
            //  1298: astore          9
            //  1300: aload           13
            //  1302: astore          10
            //  1304: aload           12
            //  1306: astore          11
            //  1308: aload           15
            //  1310: astore          12
            //  1312: aload           19
            //  1314: astore          13
            //  1316: aload           20
            //  1318: astore          14
            //  1320: aload           21
            //  1322: astore          15
            //  1324: aload           7
            //  1326: astore          19
            //  1328: aload_1
            //  1329: astore          18
            //  1331: aload           7
            //  1333: astore          20
            //  1335: aload_1
            //  1336: astore          21
            //  1338: aload           8
            //  1340: invokevirtual   java/lang/Long.longValue:()J
            //  1343: lconst_0
            //  1344: lcmp
            //  1345: ifle            1891
            //  1348: aload           7
            //  1350: astore          19
            //  1352: aload_1
            //  1353: astore          18
            //  1355: aload           7
            //  1357: astore          20
            //  1359: aload_1
            //  1360: astore          21
            //  1362: aload_0
            //  1363: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1366: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$1100:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //  1369: astore          23
            //  1371: aload           7
            //  1373: astore          19
            //  1375: aload_1
            //  1376: astore          18
            //  1378: aload           7
            //  1380: astore          20
            //  1382: aload_1
            //  1383: astore          21
            //  1385: aload_0
            //  1386: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1389: astore          24
            //  1391: aload           23
            //  1393: new             Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;
            //  1396: dup
            //  1397: aload           24
            //  1399: aload           8
            //  1401: iload_2
            //  1402: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1405: aload           16
            //  1407: aload           15
            //  1409: aload           14
            //  1411: aload           13
            //  1413: aload           12
            //  1415: aload           11
            //  1417: aload           10
            //  1419: aconst_null
            //  1420: invokespecial   com/erwinvoogt/soarcast/WelkomSoarCast$Wind.<init>:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //  1423: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //  1426: pop
            //  1427: goto            1430
            //  1430: aload           22
            //  1432: invokevirtual   java/lang/Integer.intValue:()I
            //  1435: iconst_1
            //  1436: iadd
            //  1437: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1440: astore          22
            //  1442: aload           14
            //  1444: astore          18
            //  1446: aload           11
            //  1448: astore          19
            //  1450: aload           10
            //  1452: astore          20
            //  1454: aload           15
            //  1456: astore          14
            //  1458: aload           8
            //  1460: astore          15
            //  1462: aload           9
            //  1464: astore          8
            //  1466: aload           18
            //  1468: astore          9
            //  1470: aload           13
            //  1472: astore          10
            //  1474: aload           12
            //  1476: astore          11
            //  1478: aload           19
            //  1480: astore          12
            //  1482: aload           20
            //  1484: astore          13
            //  1486: goto            558
            //  1489: astore          8
            //  1491: aload_1
            //  1492: astore          9
            //  1494: aload           8
            //  1496: astore_1
            //  1497: aload           7
            //  1499: astore          19
            //  1501: aload           9
            //  1503: astore          7
            //  1505: goto            1746
            //  1508: astore          8
            //  1510: aload_1
            //  1511: astore          9
            //  1513: aload           7
            //  1515: astore_1
            //  1516: aload           9
            //  1518: astore          7
            //  1520: goto            1659
            //  1523: lconst_0
            //  1524: lstore          5
            //  1526: aload_1
            //  1527: astore          17
            //  1529: aload           7
            //  1531: astore          18
            //  1533: aload           8
            //  1535: astore          7
            //  1537: aload           16
            //  1539: astore_1
            //  1540: goto            1574
            //  1543: astore          7
            //  1545: aload           18
            //  1547: astore_1
            //  1548: goto            1736
            //  1551: astore          8
            //  1553: aload           21
            //  1555: astore          7
            //  1557: aload           20
            //  1559: astore_1
            //  1560: goto            1659
            //  1563: aload           7
            //  1565: astore          18
            //  1567: aconst_null
            //  1568: astore          17
            //  1570: aload           8
            //  1572: astore          7
            //  1574: aload           18
            //  1576: ifnull          1584
            //  1579: aload           18
            //  1581: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1584: aload           17
            //  1586: ifnull          1611
            //  1589: aload           17
            //  1591: invokevirtual   java/io/BufferedReader.close:()V
            //  1594: goto            1611
            //  1597: astore          8
            //  1599: aload_0
            //  1600: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.LOG_TAG:Ljava/lang/String;
            //  1603: ldc             "Error closing stream"
            //  1605: aload           8
            //  1607: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1610: pop
            //  1611: iload_2
            //  1612: iconst_1
            //  1613: iadd
            //  1614: istore_2
            //  1615: aload           7
            //  1617: astore          8
            //  1619: goto            65
            //  1622: astore_1
            //  1623: goto            1638
            //  1626: astore          8
            //  1628: aload           7
            //  1630: astore_1
            //  1631: goto            1656
            //  1634: astore_1
            //  1635: aconst_null
            //  1636: astore          7
            //  1638: aconst_null
            //  1639: astore          8
            //  1641: aload           7
            //  1643: astore          19
            //  1645: aload           8
            //  1647: astore          7
            //  1649: goto            1746
            //  1652: astore          8
            //  1654: aconst_null
            //  1655: astore_1
            //  1656: aconst_null
            //  1657: astore          7
            //  1659: aload_1
            //  1660: astore          18
            //  1662: aload           7
            //  1664: astore          19
            //  1666: aload_0
            //  1667: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.LOG_TAG:Ljava/lang/String;
            //  1670: ldc             "Error "
            //  1672: aload           8
            //  1674: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1677: pop
            //  1678: aload_1
            //  1679: astore          18
            //  1681: aload           7
            //  1683: astore          19
            //  1685: iconst_m1
            //  1686: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1689: astore          8
            //  1691: aload_1
            //  1692: ifnull          1699
            //  1695: aload_1
            //  1696: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1699: aload           7
            //  1701: ifnull          1724
            //  1704: aload           7
            //  1706: invokevirtual   java/io/BufferedReader.close:()V
            //  1709: aload           8
            //  1711: areturn
            //  1712: astore_1
            //  1713: aload_0
            //  1714: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.LOG_TAG:Ljava/lang/String;
            //  1717: ldc             "Error closing stream"
            //  1719: aload_1
            //  1720: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1723: pop
            //  1724: aload           8
            //  1726: areturn
            //  1727: astore          7
            //  1729: aload           19
            //  1731: astore_1
            //  1732: aload           18
            //  1734: astore          19
            //  1736: aload           7
            //  1738: astore          8
            //  1740: aload_1
            //  1741: astore          7
            //  1743: aload           8
            //  1745: astore_1
            //  1746: aload           19
            //  1748: ifnull          1756
            //  1751: aload           19
            //  1753: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //  1756: aload           7
            //  1758: ifnull          1783
            //  1761: aload           7
            //  1763: invokevirtual   java/io/BufferedReader.close:()V
            //  1766: goto            1783
            //  1769: astore          7
            //  1771: aload_0
            //  1772: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.LOG_TAG:Ljava/lang/String;
            //  1775: ldc             "Error closing stream"
            //  1777: aload           7
            //  1779: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //  1782: pop
            //  1783: aload_1
            //  1784: athrow
            //  1785: aload_0
            //  1786: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1789: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$1100:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //  1792: invokevirtual   java/util/ArrayList.size:()I
            //  1795: iconst_1
            //  1796: if_icmpge       1851
            //  1799: aload_0
            //  1800: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1803: invokestatic    com/erwinvoogt/soarcast/WelkomSoarCast.access$1100:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;)Ljava/util/ArrayList;
            //  1806: new             Lcom/erwinvoogt/soarcast/WelkomSoarCast$Wind;
            //  1809: dup
            //  1810: aload_0
            //  1811: getfield        com/erwinvoogt/soarcast/WelkomSoarCast$LeesWind.this$0:Lcom/erwinvoogt/soarcast/WelkomSoarCast;
            //  1814: ldc2_w          1512514800
            //  1817: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //  1820: iconst_0
            //  1821: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1824: aload_1
            //  1825: aload           14
            //  1827: aload           9
            //  1829: aload           10
            //  1831: aload           11
            //  1833: aload           12
            //  1835: aload           13
            //  1837: aconst_null
            //  1838: invokespecial   com/erwinvoogt/soarcast/WelkomSoarCast$Wind.<init>:(Lcom/erwinvoogt/soarcast/WelkomSoarCast;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lcom/erwinvoogt/soarcast/WelkomSoarCast$1;)V
            //  1841: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //  1844: pop
            //  1845: bipush          -2
            //  1847: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1850: areturn
            //  1851: iconst_1
            //  1852: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1855: areturn
            //  1856: astore          8
            //  1858: goto            992
            //  1861: astore          14
            //  1863: goto            1018
            //  1866: astore          11
            //  1868: goto            1051
            //  1871: astore          13
            //  1873: goto            1084
            //  1876: astore          10
            //  1878: goto            1117
            //  1881: astore          12
            //  1883: goto            1150
            //  1886: astore          18
            //  1888: goto            1191
            //  1891: goto            1430
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ---------------------------------
            //  275    292    1652   1656   Ljava/io/IOException;
            //  275    292    1634   1638   Any
            //  292    311    1626   1634   Ljava/io/IOException;
            //  292    311    1622   1626   Any
            //  316    334    1626   1634   Ljava/io/IOException;
            //  316    334    1622   1626   Any
            //  350    357    1551   1557   Ljava/io/IOException;
            //  350    357    1543   1551   Any
            //  373    381    1551   1557   Ljava/io/IOException;
            //  373    381    1543   1551   Any
            //  397    405    1551   1557   Ljava/io/IOException;
            //  397    405    1543   1551   Any
            //  421    429    1551   1557   Ljava/io/IOException;
            //  421    429    1543   1551   Any
            //  445    453    1551   1557   Ljava/io/IOException;
            //  445    453    1543   1551   Any
            //  469    477    1551   1557   Ljava/io/IOException;
            //  469    477    1543   1551   Any
            //  493    501    1551   1557   Ljava/io/IOException;
            //  493    501    1543   1551   Any
            //  517    526    1551   1557   Ljava/io/IOException;
            //  517    526    1543   1551   Any
            //  542    548    1551   1557   Ljava/io/IOException;
            //  542    548    1543   1551   Any
            //  572    578    1551   1557   Ljava/io/IOException;
            //  572    578    1543   1551   Any
            //  597    603    1551   1557   Ljava/io/IOException;
            //  597    603    1543   1551   Any
            //  614    625    648    660    Ljava/io/IOException;
            //  614    625    1727   1736   Any
            //  632    640    648    660    Ljava/io/IOException;
            //  632    640    1727   1736   Any
            //  674    680    1551   1557   Ljava/io/IOException;
            //  674    680    1543   1551   Any
            //  691    697    648    660    Ljava/io/IOException;
            //  691    697    1727   1736   Any
            //  704    714    648    660    Ljava/io/IOException;
            //  704    714    1727   1736   Any
            //  721    727    648    660    Ljava/io/IOException;
            //  721    727    1727   1736   Any
            //  734    740    648    660    Ljava/io/IOException;
            //  734    740    1727   1736   Any
            //  747    753    648    660    Ljava/io/IOException;
            //  747    753    1727   1736   Any
            //  760    766    648    660    Ljava/io/IOException;
            //  760    766    1727   1736   Any
            //  773    779    648    660    Ljava/io/IOException;
            //  773    779    1727   1736   Any
            //  786    792    648    660    Ljava/io/IOException;
            //  786    792    1727   1736   Any
            //  799    805    648    660    Ljava/io/IOException;
            //  799    805    1727   1736   Any
            //  822    835    648    660    Ljava/io/IOException;
            //  822    835    1727   1736   Any
            //  848    865    648    660    Ljava/io/IOException;
            //  848    865    1727   1736   Any
            //  876    884    648    660    Ljava/io/IOException;
            //  876    884    1727   1736   Any
            //  895    912    648    660    Ljava/io/IOException;
            //  895    912    1727   1736   Any
            //  919    928    648    660    Ljava/io/IOException;
            //  919    928    1727   1736   Any
            //  982    989    1856   1001   Ljava/lang/NumberFormatException;
            //  982    989    648    660    Ljava/io/IOException;
            //  982    989    1727   1736   Any
            //  1008   1015   1861   1034   Ljava/lang/NumberFormatException;
            //  1008   1015   648    660    Ljava/io/IOException;
            //  1008   1015   1727   1736   Any
            //  1025   1031   648    660    Ljava/io/IOException;
            //  1025   1031   1727   1736   Any
            //  1041   1048   1866   1067   Ljava/lang/NumberFormatException;
            //  1041   1048   648    660    Ljava/io/IOException;
            //  1041   1048   1727   1736   Any
            //  1058   1064   648    660    Ljava/io/IOException;
            //  1058   1064   1727   1736   Any
            //  1074   1081   1871   1100   Ljava/lang/NumberFormatException;
            //  1074   1081   648    660    Ljava/io/IOException;
            //  1074   1081   1727   1736   Any
            //  1091   1097   648    660    Ljava/io/IOException;
            //  1091   1097   1727   1736   Any
            //  1107   1114   1876   1133   Ljava/lang/NumberFormatException;
            //  1107   1114   648    660    Ljava/io/IOException;
            //  1107   1114   1727   1736   Any
            //  1124   1130   648    660    Ljava/io/IOException;
            //  1124   1130   1727   1736   Any
            //  1140   1147   1881   1166   Ljava/lang/NumberFormatException;
            //  1140   1147   648    660    Ljava/io/IOException;
            //  1140   1147   1727   1736   Any
            //  1157   1163   648    660    Ljava/io/IOException;
            //  1157   1163   1727   1736   Any
            //  1173   1180   1886   1211   Ljava/lang/NumberFormatException;
            //  1173   1180   648    660    Ljava/io/IOException;
            //  1173   1180   1727   1736   Any
            //  1198   1204   648    660    Ljava/io/IOException;
            //  1198   1204   1727   1736   Any
            //  1222   1234   648    660    Ljava/io/IOException;
            //  1222   1234   1727   1736   Any
            //  1338   1348   1551   1557   Ljava/io/IOException;
            //  1338   1348   1543   1551   Any
            //  1362   1371   1551   1557   Ljava/io/IOException;
            //  1362   1371   1543   1551   Any
            //  1385   1391   1551   1557   Ljava/io/IOException;
            //  1385   1391   1543   1551   Any
            //  1391   1427   1508   1523   Ljava/io/IOException;
            //  1391   1427   1489   1508   Any
            //  1430   1442   1508   1523   Ljava/io/IOException;
            //  1430   1442   1489   1508   Any
            //  1589   1594   1597   1611   Ljava/io/IOException;
            //  1666   1678   1727   1736   Any
            //  1685   1691   1727   1736   Any
            //  1704   1709   1712   1724   Ljava/io/IOException;
            //  1761   1766   1769   1783   Ljava/io/IOException;
            //
            // The error that occurred was:
            //
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        protected void onPostExecute(final Integer n) {
            if (n < 1) {
                if (n == -2) {
                    Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(WelkomSoarCast.this.getBaseContext(), WelkomSoarCast.this.getResources().getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                WelkomSoarCast.this.tijdNul = WelkomSoarCast.this.mWind.get(0).unixTimestamp;
            }
            WelkomSoarCast.this.schaal = 0;
            int n2;
            for (int size = WelkomSoarCast.this.mWind.size(), i = 0; i < size; i = n2 + 1) {
                if (WelkomSoarCast.this.mWind.get(i).snelheidMeting <= 15.0 && WelkomSoarCast.this.mWind.get(i).vlaagMeting <= 15.0 && WelkomSoarCast.this.mWind.get(i).snelheidHarmonie <= 15.0 && WelkomSoarCast.this.mWind.get(i).vlaagHarmonie <= 15.0 && WelkomSoarCast.this.mWind.get(i).snelheidGFS <= 15.0) {
                    n2 = i;
                    if (WelkomSoarCast.this.mWind.get(i).vlaagGFS <= 15.0) {
                        continue;
                    }
                }
                WelkomSoarCast.this.schaal = 1;
                n2 = size;
            }
            ((WindKaderView)WelkomSoarCast.this.findViewById(R.id.windKaderView)).update(WelkomSoarCast.this.eenheid, WelkomSoarCast.this.schaal, WelkomSoarCast.this.uurVanaf, WelkomSoarCast.this.mWind.get(0).geefZonOpOnder());
            WelkomSoarCast.this.updateWindmeting();
            WelkomSoarCast.this.updateWindModel();
            WelkomSoarCast.this.ikBenKlaar();
        }
    }


}