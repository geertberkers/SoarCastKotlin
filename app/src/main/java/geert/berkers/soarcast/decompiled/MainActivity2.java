package geert.berkers.soarcast.decompiled;

/**
 * Created by Zorgkluis (Geert Berkers)
 */
// 
// Decompiled by Procyon v0.5.36
// 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import geert.berkers.soarcast.R;
import geert.berkers.soarcast.WelkomSoarCast;

public class MainActivity2 extends Activity {
    private String bestand;
    private String gebruiker;
    private Boolean loginOK;
    private Boolean sponsor1OK;
    private Boolean sponsor2OK;

    private void startSoarCast() {
        final Context applicationContext = this.getApplicationContext();
        final Editor edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        edit.putBoolean("sponsor1", (boolean) this.sponsor1OK);
        edit.putBoolean("sponsor2", (boolean) this.sponsor2OK);
        edit.apply();
        this.startActivity(new Intent(applicationContext, WelkomSoarCast.class).putExtra("bestand", this.bestand));
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.gebruiker = this.getResources().getString(R.string.login_id);
        this.loginOK = false;
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context) this);
        this.sponsor1OK = defaultSharedPreferences.getBoolean("sponsor1", false);
        this.sponsor2OK = defaultSharedPreferences.getBoolean("sponsor2", false);
        if (bundle == null) {
            this.setContentView(R.layout.activity_main);
            final ImageView imageView = (ImageView) this.findViewById(R.id.sponsorlogo1);
            if (this.sponsor1OK) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener((OnClickListener) new OnClickListener() {
                    public void onClick(final View view) {
                        MainActivity2.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(MainActivity2.this.getResources().getString(R.string.sponsor1url))));
                    }
                });
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
            final ImageView imageView2 = (ImageView) this.findViewById(R.id.sponsorlogo2);
            if (this.sponsor2OK) {
                imageView2.setVisibility(View.VISIBLE);
                imageView2.setOnClickListener((OnClickListener) new OnClickListener() {
                    public void onClick(final View view) {
                        MainActivity2.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(MainActivity2.this.getResources().getString(R.string.sponsor2url))));
                    }
                });
            } else {
                imageView2.setVisibility(View.INVISIBLE);
            }
            final TextView textView = (TextView) this.findViewById(R.id.paasei1);
            final TextView textView2 = (TextView) this.findViewById(R.id.paasei2);
            textView.setOnClickListener((OnClickListener) new OnClickListener() {
                public void onClick(final View view) {
                    MainActivity2.this.loginOK = false;
                    if (!MainActivity2.this.gebruiker.equals(MainActivity2.this.getResources().getString(R.string.paasei1))) {
                        MainActivity2.this.gebruiker = MainActivity2.this.getResources().getString(R.string.paasei1);
                        textView.setBackgroundColor(552599552);
                        textView2.setBackgroundColor(0);
                    } else {
                        MainActivity2.this.gebruiker = MainActivity2.this.getResources().getString(R.string.login_id);
                        textView.setBackgroundColor(0);
                    }
                    new CheckGebruiker().execute(MainActivity2.this.gebruiker);
                }
            });
            textView2.setOnClickListener((OnClickListener) new OnClickListener() {
                public void onClick(final View view) {
                    MainActivity2.this.loginOK = false;
                    if (!MainActivity2.this.gebruiker.equals(MainActivity2.this.getResources().getString(R.string.paasei2))) {
                        MainActivity2.this.gebruiker = MainActivity2.this.getResources().getString(R.string.paasei2);
                        textView2.setBackgroundColor(552599552);
                        textView.setBackgroundColor(0);
                    } else {
                        MainActivity2.this.gebruiker = MainActivity2.this.getResources().getString(R.string.login_id);
                        textView2.setBackgroundColor(0);
                    }
                    new CheckGebruiker().execute(MainActivity2.this.gebruiker);
                }
            });
            final TextView textView3 = (TextView) this.findViewById(R.id.status);
            textView3.setText((CharSequence) this.getResources().getString(R.string.connecting));
            textView3.setOnClickListener((OnClickListener) new OnClickListener() {
                public void onClick(final View view) {
                    if (MainActivity2.this.loginOK) {
                        MainActivity2.this.startSoarCast();
                        return;
                    }
                    new CheckGebruiker().execute(MainActivity2.this.gebruiker);
                }
            });
            new CheckGebruiker().execute(MainActivity2.this.gebruiker);
        }
    }

    public class CheckGebruiker extends AsyncTask<String, Void, Integer> {
        private final String LOG_TAG;

        public CheckGebruiker() {
            this.LOG_TAG = CheckGebruiker.class.getSimpleName();
        }

        protected Integer doInBackground(String... ex) {
            if (ex.length == 0) {
                return -1;
            }
            final boolean b = false;
            if (ex[0].length() < 2) {
                return -1;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(MainActivity2.this.getResources().getString(R.string.url_login));
            sb.append(MainActivity2.this.getResources().getString(R.string.php_login));
            Object in = sb.toString();
            final String string = MainActivity2.this.getResources().getString(R.string.php_login_parameter);
            Object line = null;
            Serializable s = null;
            Label_0615:
            {
                try {
                    ex = (IOException) new URL(Uri.parse((String) in).buildUpon().appendQueryParameter(string, ex[0]).build().toString()).openConnection();
                    try {
                        ((HttpURLConnection) ex).setRequestMethod("GET");
                        ((URLConnection) ex).connect();
                        in = ((URLConnection) ex).getInputStream();
                        s = new StringBuffer();
                        if (in == null) {
                            if (ex != null) {
                                ((HttpURLConnection) ex).disconnect();
                            }
                            return -9;
                        }
                        in = new BufferedReader(new InputStreamReader((InputStream) in));
                        try {
                            while (true) {
                                line = ((BufferedReader) in).readLine();
                                if (line == null) {
                                    break;
                                }
                                ((StringBuffer) s).append((String) line);
                            }
                            if (((StringBuffer) s).length() == 0) {
                                s = -9;
                                if (ex != null) {
                                    ((HttpURLConnection) ex).disconnect();
                                }
                                if (in != null) {
                                    try {
                                        ((BufferedReader) in).close();
                                        return (Integer) s;
                                    } catch (IOException ex) {
                                        Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex);
                                    }
                                }
                                return (Integer) s;
                            }
                            s = ((StringBuffer) s).toString();
                            if (ex != null) {
                                ((HttpURLConnection) ex).disconnect();
                            }
                            if (in != null) {
                                try {
                                    ((BufferedReader) in).close();
                                } catch (IOException ex) {
                                    Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex);
                                }
                            }
                            if (((String) s).length() < 1) {
                                return -9;
                            }
                            if (((String) s).equals(MainActivity2.this.getResources().getString(R.string.login_onjuist))) {
                                return -1;
                            }
                            if (((String) s).equals(MainActivity2.this.getResources().getString(R.string.login_verlopen))) {
                                return -2;
                            }
                            final int index = ((String) s).indexOf(MainActivity2.this.getResources().getString(R.string.csv_separator));
                            if (index > 0) {
                                MainActivity2.this.bestand = ((String) s).substring(0, index);
                            } else {
                                MainActivity2.this.bestand = (String) s;
                            }
                            ex = (IOException) MainActivity2.this;
                            ((MainActivity2) ex).sponsor1OK = (((String) s).indexOf("sp1") > 0);
                            ex = (IOException) MainActivity2.this;
                            boolean b2 = b;
                            if (((String) s).indexOf("sp2") > 0) {
                                b2 = true;
                            }
                            ((MainActivity2) ex).sponsor2OK = b2;
                            return 1;
                        } catch (IOException line) {
                        } finally {
                            line = ex;
                        }
                    } catch (IOException line) {
                        s = null;
                    } finally {
                        break Label_0615;
                    }
                    in = line;
                    line = ex;
                } catch (IOException in) {
                    ex = null;
                    line = s;
                } finally {
                    ex = null;
                    break Label_0615;
                }
                try {
                    Log.e(this.LOG_TAG, "Error ", (Throwable) in);
                    final Integer value = -9;
                    if (line != null) {
                        ((HttpURLConnection) line).disconnect();
                    }
                    if (ex != null) {
                        try {
                            ((BufferedReader) ex).close();
                            return value;
                        } catch (IOException ex) {
                            Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex);
                        }
                    }
                    return value;
                } finally {
                    in = ex;
                }
                ex = (IOException) line;
                line = in;
            }
            if (ex != null) {
                ((HttpURLConnection) ex).disconnect();
            }
            if (line != null) {
                try {
                    ((BufferedReader) line).close();
                } catch (IOException ex2) {
                    Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex2);
                }
            }
            throw ;
        }

        protected void onPostExecute(final Integer n) {
            final Context applicationContext = MainActivity2.this.getApplicationContext();
            final TextView textView = (TextView) MainActivity2.this.findViewById(R.id.status);
            String text = "";
            if (n == -1) {
                text = applicationContext.getString(R.string.check_login);
            }
            if (n == -2) {
                text = applicationContext.getString(R.string.tekst_verlopen);
            }
            if (n == -9) {
                text = applicationContext.getString(R.string.check_connection);
            }
            if (n < 0) {
                Toast.makeText(applicationContext, (CharSequence) text, Toast.LENGTH_SHORT).show();
            }
            if (n == 1) {
                text = applicationContext.getString(R.string.verder);
            }
            textView.setText((CharSequence) text);
            if (n == 1) {
                MainActivity2.this.loginOK = true;
                MainActivity2.this.startSoarCast();
            }
        }
    }
}