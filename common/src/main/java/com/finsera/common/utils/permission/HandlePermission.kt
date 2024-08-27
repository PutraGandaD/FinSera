package com.finsera.common.utils.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings


object HandlePermission {
    fun Context.openAppPermissionSettings() {
        val intent = Intent().apply {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", packageName, null)
                }
                else -> {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    addCategory(Intent.CATEGORY_DEFAULT)
                    data = Uri.parse("package:" + packageName)
                }
            }
        }
        startActivity(intent)
    }

    fun Context.openAppStoragePermissionR() {
        try {
            val intent = Intent().apply {
                action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + packageName)
            }
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent().apply {
                setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            }
            startActivity(intent)
        }
    }
}