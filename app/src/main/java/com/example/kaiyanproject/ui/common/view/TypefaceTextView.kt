package com.example.kaiyanproject.ui.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.kaiyanproject.R
import com.example.kaiyanproject.util.TypeFaceUtil

class TypefaceTextView :
    AppCompatTextView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (isInEditMode) return
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typeArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = getTypeface(typefaceType)
            typeArray.recycle()
        }
    }

    companion object {
        fun getTypeface(typefaceType: Int?) = when (typefaceType) {
            TypeFaceUtil.FZLL_TYPEFACE -> TypeFaceUtil.fzlLTypeface
            TypeFaceUtil.FZDB1_TYPEFACE -> TypeFaceUtil.fzdb1Typeface
            TypeFaceUtil.FUTURA_TYPEFACE -> TypeFaceUtil.dinTypeface
            TypeFaceUtil.DIN_TYPEFACE -> TypeFaceUtil.futuraTypeface
            TypeFaceUtil.LOBSTER_TYPEFACE -> TypeFaceUtil.lobsterTypeface
            else -> Typeface.DEFAULT
        }
    }
}