package com.msl.talfinalproject.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.msl.talfinalproject.R
import com.msl.talfinalproject.ui.helper.CustomFontHelper

class FontableEditText : AppCompatEditText {
    constructor(context: Context?) : super(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a = context!!.obtainStyledAttributes(
            attrs, R.styleable.FontableEditText
        )
        val index = a!!.getInteger(R.styleable.FontableEditText_font_style, 0)
        a.recycle()

        CustomFontHelper.setCustomFont(this, context, index)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context!!.obtainStyledAttributes(
            attrs, R.styleable.FontableEditText, defStyleAttr, 0
        )
        val index = a!!.getInteger(R.styleable.FontableEditText_font_style, 0)
        a.recycle()

        CustomFontHelper.setCustomFont(this, context, index)
    }
}