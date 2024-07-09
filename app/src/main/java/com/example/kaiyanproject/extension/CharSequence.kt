package com.example.kaiyanproject.extension

import android.widget.Toast
import com.example.kaiyanproject.KaiYanApplication

/**
 * 弹出Toast提示。
 *
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(KaiYanApplication.context, this, duration).show()
}
