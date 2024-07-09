package com.example.kaiyanproject.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.util.TypeFaceUtil
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent

class NoStatusFooter : SimpleComponent, RefreshFooter {
    private var mTitleText: TextView
    private var mTextNothing = ""
    private var mFooterHeight = 0
    private var mBackgroundColor = 0
    private var mNoMoreData = false
    private var mRefreshKernel: RefreshKernel? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        View.inflate(context, R.layout.layout_srl_classics_footer, this)
        mTitleText = findViewById(R.id.srl_classics_title)
        val typeArray =
            context.obtainStyledAttributes(attrs, R.styleable.NoStatusFooter, 0, 0)
        typeArray.let {
            if (it.hasValue(R.styleable.NoStatusFooter_srlPrimaryColor)) {
                setPrimaryColor(it.getColor(R.styleable.NoStatusFooter_srlPrimaryColor, 0))
            }
            if (it.hasValue(R.styleable.NoStatusFooter_srlAccentColor)) {
                setAccentColor(it.getColor(R.styleable.NoStatusFooter_srlAccentColor, 0))
            }
            if (it.hasValue(R.styleable.NoStatusFooter_srlTextSizeTitle)) {
                mTitleText.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it.getDimensionPixelSize(
                        R.styleable.NoStatusFooter_srlTextSizeTitle,
                        16f.dp2px
                    ).toFloat()
                )
            }
            mTextNothing = when {
                it.hasValue(R.styleable.NoStatusFooter_srlTextNothing) -> {
                    it.getString(R.styleable.NoStatusFooter_srlTextNothing)
                        ?: R.string.srl_footer_nothing.resString
                }

                REFRESH_FOOTER_NOTHING != null -> {
                    REFRESH_FOOTER_NOTHING!!
                }

                else -> {
                    R.string.srl_footer_nothing.resString
                }
            }
            mTitleText.typeface = TypefaceTextView.getTypeface(
                it.getInt(
                    R.styleable.NoStatusFooter_srlTextTitleTypeface,
                    TypeFaceUtil.LOBSTER_TYPEFACE
                )
            )
            typeArray.recycle()
        }
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        mRefreshKernel = kernel
        mRefreshKernel?.requestDrawBackgroundFor(this, mBackgroundColor)
        if (mFooterHeight == 0) mFooterHeight = height
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return super.onFinish(refreshLayout, success)
        return 0
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            refreshFooterHeight()
            if (noMoreData) {
                mTitleText.text = mTextNothing
            }
        }
        return true
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        super.onStateChanged(refreshLayout, oldState, newState)
        if (!mNoMoreData) {
            when (newState) {
                RefreshState.None -> {

                }

                RefreshState.PullUpToLoad -> {
                    refreshFooterHeight()
                }

                RefreshState.Loading, RefreshState.LoadReleased -> {

                }

                RefreshState.ReleaseToLoad -> {

                }

                RefreshState.Refreshing -> {

                }

                else -> {

                }
            }
        } else {
            refreshFooterHeight()
        }
    }

    private fun refreshFooterHeight() {
        if (mNoMoreData) {
            mRefreshKernel?.refreshLayout?.setFooterHeightPx(mFooterHeight)
        } else {
            mRefreshKernel?.refreshLayout?.setFooterHeight(0f)
        }
        mRefreshKernel?.requestRemeasureHeightFor(this)
    }

    fun setTextTitleSize(size: Float) = apply {
        mTitleText.textSize = size
        mRefreshKernel?.requestRemeasureHeightFor(this)
    }

    fun setPrimaryColor(@ColorInt primaryColor: Int) = apply {
        mBackgroundColor = primaryColor
        mRefreshKernel?.requestDrawBackgroundFor(this, primaryColor)
    }

    fun setAccentColor(@ColorInt accentColor: Int) = apply {
        mTitleText.setTextColor(accentColor)
    }

    fun setAccentColorId(@ColorRes colorId: Int) = apply {
        val thisView: View = this
        setAccentColor(ContextCompat.getColor(thisView.context, colorId))
    }

    companion object {
        var REFRESH_FOOTER_NOTHING: String? = null
    }
}