package com.rey.barelauncher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.widget.Toast


/**
 * Utility functions for app-related operations
 */
object AppUtils {

    /**
     * Checks if an app is a system app
     *
     * @param packageName The package name of the app to check
     * @param context Android context
     * @return true if the app is a system app, false otherwise
     */
    fun isSystemApp(packageName: String, context: Context): Boolean {
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Launches an app by its package name
     *
     * @param context Android context
     * @param packageName The package name of the app to launch
     */
    fun launchApp(context: Context, packageName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to launch app", Toast.LENGTH_SHORT).show()
        }
    }
}