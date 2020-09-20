package geert.berkers.soarcast.decompiled

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import geert.berkers.soarcast.R
import geert.berkers.soarcast.WelkomSoarCast
import java.io.Serializable
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Zorgkluis (Geert Berkers)
 */
// 
// Decompiled by Procyon v0.5.36
// 
class MainActivity3 : Activity() {
    private var bestand: String? = null
    private var gebruiker: String? = null
    private var loginOK: Boolean? = null
    private var sponsor1OK: Boolean? = null
    private var sponsor2OK: Boolean? = null
    private fun startSoarCast() {
        val applicationContext = this.applicationContext
        val edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
        edit.putBoolean("sponsor1", sponsor1OK as Boolean)
        edit.putBoolean("sponsor2", sponsor2OK as Boolean)
        edit.apply()
        val intent = Intent(applicationContext, WelkomSoarCast::class.java).apply {
            putExtra("bestand", bestand)
        }
        this.startActivity(intent)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        gebruiker = this.resources.getString(R.string.login_id)
        loginOK = false
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this as Context)
        sponsor1OK = defaultSharedPreferences.getBoolean("sponsor1", false)
        sponsor2OK = defaultSharedPreferences.getBoolean("sponsor2", false)
        if (bundle == null) {
            this.setContentView(R.layout.activity_main)
            val imageView = findViewById<View>(R.id.sponsorlogo1) as ImageView
            if (sponsor1OK!!) {
                imageView.visibility = View.VISIBLE
                imageView.setOnClickListener(View.OnClickListener {
                    this@MainActivity3.startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(this@MainActivity3.resources.getString(R.string.sponsor1url))
                        )
                    )
                })
            } else {
                imageView.visibility = View.INVISIBLE
            }
            val imageView2 = findViewById<View>(R.id.sponsorlogo2) as ImageView
            if (sponsor2OK!!) {
                imageView2.visibility = View.VISIBLE
                imageView2.setOnClickListener(View.OnClickListener {
                    this@MainActivity3.startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(this@MainActivity3.resources.getString(R.string.sponsor2url))
                        )
                    )
                })
            } else {
                imageView2.visibility = View.INVISIBLE
            }
            val textView = findViewById<View>(R.id.paasei1) as TextView
            val textView2 = findViewById<View>(R.id.paasei2) as TextView
            textView.setOnClickListener(View.OnClickListener {
                loginOK = false
                if (gebruiker != this@MainActivity3.resources.getString(R.string.paasei1)) {
                    gebruiker = this@MainActivity3.resources.getString(R.string.paasei1)
                    textView.setBackgroundColor(552599552)
                    textView2.setBackgroundColor(0)
                } else {
                    gebruiker = this@MainActivity3.resources.getString(R.string.login_id)
                    textView.setBackgroundColor(0)
                }
                CheckGebruiker().execute(gebruiker)
            })
            textView2.setOnClickListener(View.OnClickListener {
                loginOK = false
                if (gebruiker != this@MainActivity3.resources.getString(R.string.paasei2)) {
                    gebruiker = this@MainActivity3.resources.getString(R.string.paasei2)
                    textView2.setBackgroundColor(552599552)
                    textView.setBackgroundColor(0)
                } else {
                    gebruiker = this@MainActivity3.resources.getString(R.string.login_id)
                    textView2.setBackgroundColor(0)
                }
                CheckGebruiker().execute(gebruiker)
            })
            val textView3 = findViewById<View>(R.id.status) as TextView
            textView3.text = this.resources.getString(R.string.connecting)
            textView3.setOnClickListener(View.OnClickListener {
                if (loginOK!!) {
                    startSoarCast()
                    return@OnClickListener
                }
                CheckGebruiker().execute(gebruiker)
            })
            CheckGebruiker().execute(gebruiker)
        }
    }

//    @Suppress("DEPRECATION")
//    @SuppressLint("StaticFieldLeak")
    inner class CheckGebruiker : AsyncTask<String?, Void?, Int>(){

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            val applicationContext = this@MainActivity3.applicationContext
            val textView = findViewById<View>(R.id.status) as TextView
            var text = ""

            result?.let {
                when {
                    it < 0 -> {
                        Toast.makeText(applicationContext, text as CharSequence, Toast.LENGTH_SHORT).show()
                    }
                    it == -1 -> {
                        text = applicationContext.getString(R.string.check_login)
                    }
                    it == -2 -> {
                        text = applicationContext.getString(R.string.tekst_verlopen)
                    }
                    it == -9 -> {
                        text = applicationContext.getString(R.string.check_connection)
                    }
                    it == 1 -> {
                        text = applicationContext.getString(R.string.verder)
                    }
                }

                textView.text = text
                if (result == 1) {
                    loginOK = true
                    startSoarCast()
                }
            }
        }

        override fun doInBackground(vararg arguments: String?): Int {
            val id = arguments[0]
            var httpConnection: HttpURLConnection? = null
            try {
                val ursString = "http://www.erwinvoogt.com/SoarCastApp/SoarCastApp.php?id=$id"
                val url = URL(ursString)
                httpConnection = url.openConnection() as HttpURLConnection
                httpConnection.requestMethod = "GET"
                httpConnection.connect()

                val data = httpConnection.inputStream.bufferedReader().readText()
                println("Data: $data")

                if(data.isEmpty()) {
                    return 9
                }
                if (data == this@MainActivity3.getString(R.string.login_onjuist)) {
                    return -1
                }
                if (data == this@MainActivity3.getString(R.string.login_verlopen)) { //"expired") {
                    return -2
                }

                val index = data.indexOf(";")
                if (index > 0) {
                    this@MainActivity3.bestand = data.substring(0, index)
                } else {
                    this@MainActivity3.bestand = data
                }
                this@MainActivity3.sponsor1OK = data.indexOf("sp1") > 0
                this@MainActivity3.sponsor2OK = data.indexOf("sp2") > 0
                return 1


            } catch (ex: Exception) {
                return -9
            } finally {
                httpConnection?.disconnect()
            }
        }

    }
//    class CheckGebruiker : AsyncTask<String?, Void?, Int>() {
//        private val LOG_TAG: String
//        protected override fun doInBackground(vararg ex: String): Int {
//            if (ex.size == 0) {
//                return -1
//            }
//            val b = false
//            if (ex[0].length < 2) {
//                return -1
//            }
//            val sb = StringBuilder()
//            sb.append(this@MainActivity3.resources.getString(R.string.url_login))
//            sb.append(this@MainActivity3.resources.getString(R.string.php_login))
//            val `in`: Any = sb.toString()
//            val string = this@MainActivity3.resources.getString(R.string.php_login_parameter)
//            val line: Any? = null
//            val s: Serializable? = null
//            //            Label_0615:
//            {
//                try {
//                    ex = (IOException) new URL(Uri.parse((String) in).buildUpon().appendQueryParameter(string, ex[0]).build().toString()).openConnection();
//                    try {
//                        ((HttpURLConnection) ex).setRequestMethod("GET");
//                        ((URLConnection) ex).connect();
//                        in = ((URLConnection) ex).getInputStream();
//                        s = new StringBuffer();
//                        if (in == null) {
//                            if (ex != null) {
//                                ((HttpURLConnection) ex).disconnect();
//                            }
//                            return -9;
//                        }
//                        in = new BufferedReader(new InputStreamReader((InputStream) in));
//                        try {
//                            while (true) {
//                                line = ((BufferedReader) in).readLine();
//                                if (line == null) {
//                                    break;
//                                }
//                                ((StringBuffer) s).append((String) line);
//                            }
//                            if (((StringBuffer) s).length() == 0) {
//                                s = -9;
//                                if (ex != null) {
//                                    ((HttpURLConnection) ex).disconnect();
//                                }
//                                if (in != null) {
//                                    try {
//                                        ((BufferedReader) in).close();
//                                        return (Integer) s;
//                                    } catch (IOException ex) {
//                                        Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex);
//                                    }
//                                }
//                                return (Integer) s;
//                            }
//                            s = ((StringBuffer) s).toString();
//                            if (ex != null) {
//                                ((HttpURLConnection) ex).disconnect();
//                            }
//                            if (in != null) {
//                                try {
//                                    ((BufferedReader) in).close();
//                                } catch (IOException ex) {
//                                    Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex);
//                                }
//                            }
//                            if (((String) s).length() < 1) {
//                                return -9;
//                            }
//                            if (((String) s).equals(MainActivity3.this.getResources().getString(R.string.login_onjuist))) {
//                                return -1;
//                            }
//                            if (((String) s).equals(MainActivity3.this.getResources().getString(R.string.login_verlopen))) {
//                                return -2;
//                            }
//                            final int index = ((String) s).indexOf(MainActivity3.this.getResources().getString(R.string.csv_separator));
//                            if (index > 0) {
//                                MainActivity3.this.bestand = ((String) s).substring(0, index);
//                            } else {
//                                MainActivity3.this.bestand = (String) s;
//                            }
//                            ex = (IOException) MainActivity3.this;
//                            ((MainActivity3) ex).sponsor1OK = (((String) s).indexOf("sp1") > 0);
//                            ex = (IOException) MainActivity3.this;
//                            boolean b2 = b;
//                            if (((String) s).indexOf("sp2") > 0) {
//                                b2 = true;
//                            }
//                            ((MainActivity3) ex).sponsor2OK = b2;
//                            return 1;
//                        } catch (IOException line) {
//                        } finally {
//                            line = ex;
//                        }
//                    } catch (IOException line) {
//                        s = null;
//                    } finally {
//                        break Label_0615;
//                    }
//                    in = line;
//                    line = ex;
//                } catch (IOException in) {
//                    ex = null;
//                    line = s;
//                } finally {
//                    ex = null;
//                    break Label_0615;
//                }
//                try {
//                    Log.e(this.LOG_TAG, "Error ", (Throwable) in);
//                    final Integer value = -9;
//                    if (line != null) {
//                        ((HttpURLConnection) line).disconnect();
//                    }
//                    if (ex != null) {
//                        try {
//                            ((BufferedReader) ex).close();
//                            return value;
//                        } catch (IOException ex) {
//                            Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex);
//                        }
//                    }
//                    return value;
//                } finally {
//                    in = ex;
//                }
//                ex = (IOException) line;
//                line = in;
//            }
//            if (ex != null) {
//                ((HttpURLConnection) ex).disconnect();
//            }
//            if (line != null) {
//                try {
//                    ((BufferedReader) line).close();
//                } catch (IOException ex2) {
//                    Log.e(this.LOG_TAG, "Error closing stream", (Throwable) ex2);
//                }
//            }
//            throw ;
//        }
//
//        override fun onPostExecute(n: Int) {
//            val applicationContext = this@MainActivity3.applicationContext
//            val textView = findViewById<View>(R.id.status) as TextView
//            var text = ""
//            if (n == -1) {
//                text = applicationContext.getString(R.string.check_login)
//            }
//            if (n == -2) {
//                text = applicationContext.getString(R.string.tekst_verlopen)
//            }
//            if (n == -9) {
//                text = applicationContext.getString(R.string.check_connection)
//            }
//            if (n < 0) {
//                Toast.makeText(applicationContext, text as CharSequence, Toast.LENGTH_SHORT).show()
//            }
//            if (n == 1) {
//                text = applicationContext.getString(R.string.verder)
//            }
//            textView.text = text
//            if (n == 1) {
//                loginOK = true
//                startSoarCast()
//            }
//        }
//
//        init {
//            LOG_TAG = CheckGebruiker::class.java.simpleName
//        }
//    }
}