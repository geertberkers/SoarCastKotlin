package geert.berkers.soarcast

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import geert.berkers.soarcast.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class WelkomSoarCast {


}
class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding

    lateinit var bestand: String
    lateinit var gebruiker: String

    var loginOk by Delegates.notNull<Boolean>()
    var sponsor10K by Delegates.notNull<Boolean>()
    var sponsor20K by Delegates.notNull<Boolean>()

    companion object {
        const val SPONSOR_1 = "sponsor1"
        const val SPONSOR_2 = "sponsor2"
    }

    private val sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    private fun startSoarCast() {
        sharedPrefs.edit {
            putBoolean(SPONSOR_1, sponsor10K)
            putBoolean(SPONSOR_2, sponsor20K)
        }

        val soarIntent = Intent(this, WelkomSoarCast::class.java).apply {
            putExtra("bestand", bestand)
        }

        startActivity(soarIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.loginOk = false
        this.gebruiker = getString(R.string.login_id)

        this.sponsor10K = sharedPrefs.getBoolean(SPONSOR_1, false)
        this.sponsor20K = sharedPrefs.getBoolean(SPONSOR_2, false)

        setContentView(R.layout.activity_main)

        if (this.sponsor10K) {

        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}