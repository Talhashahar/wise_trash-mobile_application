package com.msl.talfinalproject.ui.helper

import android.content.Context
import android.widget.TextView
import com.msl.talfinalproject.ui.fonts.FontText

class CustomFontHelper {

    companion object {
        /**
         * Sets a font on a view that extends TextView based on the custom com.my.package:font attribute
         * If the custom font attribute isn't found in the attributes nothing happens
         * @param textview
         * @param context
         * @param fontIndex
         */
        fun setCustomFont(textview: TextView, context: Context?, fontIndex: Int?) {

            try {
                if (FontText.isValidEnum(fontIndex)) {
                    val font = FontText.nameFromId(fontIndex)
                    setCustomFont(textview, font, context)
                }
            } catch (e: Exception) {
                val msg = e.message
            }

        }


        /**
         * Sets a font on a textview
         * @param textview
         * @param font
         * @param context
         */
        fun setCustomFont(textview: TextView, font: String?, context: Context?) {
            if (font == null) {
                return
            }
            val typeface = FontCache.get(font, context)
            if (typeface != null) {
                textview.typeface = typeface
            }
        }
    }

}