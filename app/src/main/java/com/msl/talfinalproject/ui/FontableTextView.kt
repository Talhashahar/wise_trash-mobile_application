package com.msl.talfinalproject.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.msl.talfinalproject.R
import com.msl.talfinalproject.ui.helper.CustomFontHelper

class FontableTextView : AppCompatTextView {

    constructor(context: Context?) : super(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a : TypedArray? =  context?.obtainStyledAttributes(attrs, R.styleable.FontableEditText)

        val index = a?.getInteger(R.styleable.FontableTextView_font_style, 0)
        a?.recycle()

        CustomFontHelper.setCustomFont(this, context, index)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }
}