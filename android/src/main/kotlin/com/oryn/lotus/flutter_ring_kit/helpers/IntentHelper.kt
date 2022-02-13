package com.oryn.lotus.flutter_ring_kit.helpers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class IntentHelper {
    companion object {
        fun getLaunchIntent(context: Context): Intent? {
            val packageName = context.packageName
            val packageManager: PackageManager = context.packageManager
            return packageManager.getLaunchIntentForPackage(packageName)
        }
    }
}
