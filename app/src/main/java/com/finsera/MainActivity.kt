package com.finsera

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.navigation.findNavController
import com.finsera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.findNavController(R.id.NavHostFragment)
    }

    override fun attachBaseContext(newBase: Context) {
        val configuration = newBase.resources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val displayMetrics = newBase.resources.displayMetrics
            if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                // Current density is different from Default Density. Override it
                configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
            }
        }
        configuration.fontScale = 1.0f
        val newContext = newBase.createConfigurationContext(configuration)
        super.attachBaseContext(newContext)
    }
}