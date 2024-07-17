package com.example.kaiyanproject.logic.model

import android.graphics.drawable.Drawable
import android.widget.TextView
import com.example.kaiyanproject.extension.dp2px


/**
 * 设置TextView图标
 *
 * @param drawable     图标
 * @param iconWidth    图标宽dp：默认自动根据图标大小
 * @param iconHeight   图标高dp：默认自动根据图标大小
 * @param direction    图标方向，0左 1上 2右 3下 默认图标位于左侧0
 */
enum class DrawableDirection {
    LEFT, TOP, RIGHT, BOTTOM
}

fun TextView.setDrawable(
    drawable: Drawable?,
    iconWidth: Float? = null,
    iconHeight: Float? = null,
    direction: DrawableDirection = DrawableDirection.LEFT
) {
    if (iconWidth != null && iconHeight != null) {
        //第一个0是距左边距离，第二个0是距上边距离，iconWidth、iconHeight 分别是长宽
        drawable?.setBounds(0, 0, iconWidth.dp2px, iconHeight.dp2px)
    }
    when (direction) {
        DrawableDirection.LEFT -> setCompoundDrawables(drawable, null, null, null)
        DrawableDirection.TOP -> setCompoundDrawables(null, drawable, null, null)
        DrawableDirection.RIGHT -> setCompoundDrawables(null, null, drawable, null)
        DrawableDirection.BOTTOM -> setCompoundDrawables(null, null, null, drawable)
    }
}

/**
 * 设置TextView图标
 *
 * @param lDrawable     左边图标
 * @param rDrawable     右边图标
 * @param lIconWidth    左边图标宽dp：默认自动根据图标大小
 * @param lIconHeight   左边图标高dp：默认自动根据图标大小
 * @param rIconWidth    右边图标宽dp：默认自动根据图标大小
 * @param rIconHeight   右边图标高dp：默认自动根据图标大小
 */
fun TextView.setDrawables(
    lDrawable: Drawable?,
    rDrawable: Drawable?,
    lIconWidth: Float? = null,
    lIconHeight: Float? = null,
    rIconWidth: Float? = null,
    rIconHeight: Float? = null
) {
    if (lIconWidth != null && lIconHeight != null) {
        lDrawable?.setBounds(0, 0, lIconWidth.dp2px, lIconHeight.dp2px)
    }
    if (rIconWidth != null && rIconHeight != null) {
        rDrawable?.setBounds(0, 0, rIconWidth.dp2px, rIconHeight.dp2px)
    }
    setCompoundDrawables(lDrawable, null, rDrawable, null)
}