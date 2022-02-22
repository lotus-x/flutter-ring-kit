package com.oryn.lotus.flutter_ring_kit.utils

import android.content.res.Resources

class DimensionHelper {
    companion object {
        @JvmStatic
        fun dpToPx(dp: Float): Float {
            return dp * Resources.getSystem().displayMetrics.density
        }
    }
}