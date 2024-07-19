package com.example.kaiyanproject.ui.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.example.kaiyanproject.R

class TypefaceRadioButton : AppCompatRadioButton {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("CustomViewStyleable")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typeArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = TypefaceTextView.getTypeface(typefaceType)
            typeArray.recycle()
        }
    }
}