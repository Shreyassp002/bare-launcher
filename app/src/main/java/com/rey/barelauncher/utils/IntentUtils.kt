package com.rey.barelauncher.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast

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
