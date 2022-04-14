package to.imcurious.realityml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    private val model: RealityModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //configureViews()
    }

    private fun configureViews() {

        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController*/
    }

}