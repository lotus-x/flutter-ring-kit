package com.oryn.lotus.flutter_ring_kit.helpers

import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

class StringHelper {
    companion object {
        fun getColorizedText(string: String, colorHex: String): Spannable {
            val spannable: Spannable = SpannableString(string)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                spannable.setSpan(
                    ForegroundColorSpan(Color.parseColor(colorHex)),
                    0,
                    spannable.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            return spannable
        }
    }
}
