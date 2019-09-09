package com.msl.talfinalproject.ui.helper

import android.content.Context
import android.graphics.Typeface
import java.util.*

class FontCache {

    companion object {
        val mFontCache = HashMap<String, Typeface>()

        fun get(name: String, context: Context?) : Typeface? {
            var typeFace: Typeface? = mFontCache[name]
            if (typeFace == null) {
                try {
                    typeFace = Typeface.createFromAsset(context?.assets, name)
                } catch (e: Exception) {
                    return null
                }

                mFontCache[name] = typeFace
            }
            return typeFace
        }
    }
}