package com.finsera

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.finsera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findNavController(R.id.NavHostFragment)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateContextConfiguration(newBase))
    }

    private fun updateContextConfiguration(context: Context): Context {
        var newContext = context

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val resources = context.resources
            val displayMetrics = resources.displayMetrics
            val configuration = resources.configuration

            Log.v("TESTS", "attachBaseContext: currentDensityDp: ${configuration.densityDpi}, " +
                    "widthPixels: ${displayMetrics.widthPixels}, deviceDefault: ${DisplayMetrics.DENSITY_DEVICE_STABLE}")

            if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                // Check if the device supports screen resolution changes
                if (Settings.Global.getString(context.contentResolver, "display_size_forced") != null) {
                    Log.v("TESTS", "attachBaseContext: This device supports screen resolution changes")

                    // Density is densityDp / 160
                    val defaultDensity = (DisplayMetrics.DENSITY_DEVICE_STABLE / DisplayMetrics.DENSITY_DEFAULT).toFloat()
                    val defaultScreenWidthDp = displayMetrics.widthPixels / defaultDensity
                    Log.v("TESTS", "attachBaseContext: defaultDensity: $defaultDensity, defaultScreenWidthDp: $defaultScreenWidthDp")
                    configuration.densityDpi = findDensityDpCanFitScreen(defaultScreenWidthDp.toInt())
                    configuration.fontScale = 1.0f;
                } else {
                    // Set the default density if the device does not support screen resolution changes
                    configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
                    configuration.fontScale = 1.0f;
                }
                Log.v("TESTS", "attachBaseContext: result: ${configuration.densityDpi}")
                newContext = context.createConfigurationContext(configuration)
            }
        }
        return newContext
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun findDensityDpCanFitScreen(densityDp: Int): Int {
        val orderedDensityDp = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> ORDERED_DENSITY_DP_P
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 -> ORDERED_DENSITY_DP_N_MR1
            else -> ORDERED_DENSITY_DP_N
        }

        var index = 0
        while (densityDp >= orderedDensityDp[index]) {
            index++
        }
        return orderedDensityDp[index]
    }

    companion object {
        @TargetApi(Build.VERSION_CODES.N)
        private val ORDERED_DENSITY_DP_N = intArrayOf(
            DisplayMetrics.DENSITY_LOW,
            DisplayMetrics.DENSITY_MEDIUM,
            DisplayMetrics.DENSITY_TV,
            DisplayMetrics.DENSITY_HIGH,
            DisplayMetrics.DENSITY_280,
            DisplayMetrics.DENSITY_XHIGH,
            DisplayMetrics.DENSITY_360,
            DisplayMetrics.DENSITY_400,
            DisplayMetrics.DENSITY_420,
            DisplayMetrics.DENSITY_XXHIGH,
            DisplayMetrics.DENSITY_560,
            DisplayMetrics.DENSITY_XXXHIGH
        )

        @TargetApi(Build.VERSION_CODES.N_MR1)
        private val ORDERED_DENSITY_DP_N_MR1 = intArrayOf(
            DisplayMetrics.DENSITY_LOW,
            DisplayMetrics.DENSITY_MEDIUM,
            DisplayMetrics.DENSITY_TV,
            DisplayMetrics.DENSITY_HIGH,
            DisplayMetrics.DENSITY_260,
            DisplayMetrics.DENSITY_280,
            DisplayMetrics.DENSITY_XHIGH,
            DisplayMetrics.DENSITY_340,
            DisplayMetrics.DENSITY_360,
            DisplayMetrics.DENSITY_400,
            DisplayMetrics.DENSITY_420,
            DisplayMetrics.DENSITY_XXHIGH,
            DisplayMetrics.DENSITY_560,
            DisplayMetrics.DENSITY_XXXHIGH
        )

        @TargetApi(Build.VERSION_CODES.P)
        private val ORDERED_DENSITY_DP_P = intArrayOf(
            DisplayMetrics.DENSITY_LOW,
            DisplayMetrics.DENSITY_MEDIUM,
            DisplayMetrics.DENSITY_TV,
            DisplayMetrics.DENSITY_HIGH,
            DisplayMetrics.DENSITY_260,
            DisplayMetrics.DENSITY_280,
            DisplayMetrics.DENSITY_XHIGH,
            DisplayMetrics.DENSITY_340,
            DisplayMetrics.DENSITY_360,
            DisplayMetrics.DENSITY_400,
            DisplayMetrics.DENSITY_420,
            DisplayMetrics.DENSITY_440,
            DisplayMetrics.DENSITY_XXHIGH,
            DisplayMetrics.DENSITY_560,
            DisplayMetrics.DENSITY_XXXHIGH
        )
    }
}
