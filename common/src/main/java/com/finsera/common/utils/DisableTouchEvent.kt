package com.finsera.common.utils

import android.app.Activity
import android.content.Context
import android.view.WindowManager

object DisableTouchEvent {
     fun setInteractionDisabled(activity: Activity, disabled : Boolean) {
        if (disabled) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
}