package com.xioneko.android.nekoanime.ui.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import kotlin.random.Random

@Composable
fun currentScreenSizeDp(): Pair<Int, Int> {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        configuration.screenWidthDp to configuration.screenHeightDp
    }
}

@Composable
fun getAspectRadio(): Float {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        configuration.screenHeightDp.toFloat() / configuration.screenWidthDp.toFloat()
    }
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configuration.screenHeightDp > 600
        } else {
            configuration.screenWidthDp > 600
        }
    }
}

fun Context.setScreenOrientation(orientation: Int) {
    val activity = this as? Activity ?: return
    activity.requestedOrientation = orientation
}

fun Context.isOrientationLocked() =
    Settings.System.getInt(
        contentResolver,
        Settings.System.ACCELEROMETER_ROTATION,
        1
    ) == 0

@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }
}


fun vibrate(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(100, 16))
}

fun <T> List<T>.getRandomElements(n: Int): List<T> {
    if (size <= n) return this
    val result = toMutableList()
    for (i in 0 until n) {
        val randomIndex = i + Random.nextInt(result.size - i)
        result[i] = result[randomIndex].also { result[randomIndex] = result[i] }
    }
    return result.take(n)
}