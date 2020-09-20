@file:Suppress("DEPRECATION")

package geert.berkers.soarcast

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
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Zorgkluis (Geert Berkers)
 */
//
// Decompiled by Procyon v0.5.36
//
class MainActivity : Activity() {

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
                imageView.setOnClickListener {
                    this@MainActivity.startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(this@MainActivity.resources.getString(R.string.sponsor1url))
                        )
                    )
                }
            } else {
                imageView.visibility = View.INVISIBLE
            }
            val imageView2 = findViewById<View>(R.id.sponsorlogo2) as ImageView
            if (sponsor2OK!!) {
                imageView2.visibility = View.VISIBLE
                imageView2.setOnClickListener {
                    this@MainActivity.startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(this@MainActivity.resources.getString(R.string.sponsor2url))
                        )
                    )
                }
            } else {
                imageView2.visibility = View.INVISIBLE
            }
            
            val textView = findViewById<View>(R.id.paasei1) as TextView
            val textView2 = findViewById<View>(R.id.paasei2) as TextView
            textView.setOnClickListener {
                loginOK = false
                if (gebruiker != this@MainActivity.resources.getString(R.string.paasei1)) {
                    gebruiker = this@MainActivity.resources.getString(R.string.paasei1)
                    textView.setBackgroundColor(552599552)
                    textView2.setBackgroundColor(0)
                } else {
                    gebruiker = this@MainActivity.resources.getString(R.string.login_id)
                    textView.setBackgroundColor(0)
                }
                CheckGebruiker().execute(gebruiker)
            }
            
            textView2.setOnClickListener {
                loginOK = false
                if (gebruiker != this@MainActivity.resources.getString(R.string.paasei2)) {
                    gebruiker = this@MainActivity.resources.getString(R.string.paasei2)
                    textView2.setBackgroundColor(552599552)
                    textView.setBackgroundColor(0)
                } else {
                    gebruiker = this@MainActivity.resources.getString(R.string.login_id)
                    textView2.setBackgroundColor(0)
                }
                CheckGebruiker().execute(gebruiker)
            }
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

    @SuppressLint("StaticFieldLeak")
    inner class CheckGebruiker : AsyncTask<String?, Void?, Int>(){

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            val applicationContext = this@MainActivity.applicationContext
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
                if (data == this@MainActivity.getString(R.string.login_onjuist)) {
                    return -1
                }
                if (data == this@MainActivity.getString(R.string.login_verlopen)) { //"expired") {
                    return -2
                }

                val index = data.indexOf(";")
                if (index > 0) {
                    this@MainActivity.bestand = data.substring(0, index)
                } else {
                    this@MainActivity.bestand = data
                }

                this@MainActivity.sponsor1OK = data.indexOf("sp1") > 0
                this@MainActivity.sponsor2OK = data.indexOf("sp2") > 0

                return 1
            } catch (ex: Exception) {
                return -9
            } finally {
                httpConnection?.disconnect()
            }
        }
    }
}