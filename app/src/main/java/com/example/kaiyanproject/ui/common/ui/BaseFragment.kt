package com.example.kaiyanproject.ui.common.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kaiyanproject.R
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.extension.logD
import com.example.kaiyanproject.ui.common.callback.RequestLifecycle
import com.example.kaiyanproject.util.ShareUtil
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseFragment : Fragment(), RequestLifecycle {
    /*
    * 是否已经加载过数据
    * */
    private var mHasLoadedData = false

    private var loadErrorView: View? = null
    protected var rootView: View? = null

    protected var loading: ProgressBar? = null

    lateinit var activity: Activity

    protected val TAG: String = this.javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = this.requireActivity()
        logD(TAG, "BaseFragment-->onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(TAG, "BaseFragment-->onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logD(TAG, "BaseFragment-->onCreateView()")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD(TAG, "BaseFragment-->onViewCreated()")
    }

    override fun onStart() {
        super.onStart()
        logD(TAG, "BaseFragment-->onStart()")
    }

    override fun onResume() {
        super.onResume()
        logD(TAG, "BaseFragment-->onResume()")
        MobclickAgent.onPageStart(javaClass.name)
        if (!mHasLoadedData) {
            loadDataOnce()
            mHasLoadedData = true
        }
    }

    override fun onPause() {
        super.onPause()
        logD(TAG, "BaseFragment-->onPause()")
        MobclickAgent.onPageEnd(javaClass.name)
    }

    override fun onStop() {
        super.onStop()
        logD(TAG, "BaseFragment-->onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logD(TAG, "BaseFragment-->onDestroyView()")
        EventBus.getDefault().unregister(this)
        if (rootView?.parent != null) (rootView?.parent as ViewGroup).removeView(rootView)
    }

    override fun onDestroy() {
        super.onDestroy()
        logD(TAG, "BaseFragment-->onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        logD(TAG, "BaseFragment-->onDetach()")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        logD(TAG, "BaseFragment-->onMessageEvent()")
    }

    @CallSuper
    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideLoadErrorView()
    }

    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
    }

    fun onCreateView(view: View): View {
        logD(TAG, "BaseFragment-->onCreateView()")
        rootView = view
        loading = view.findViewById(R.id.loading)
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        return view
    }

    /**
     * 页面首次可见时调用一次该方法，在这里可以请求网络数据等。
     */
    open fun loadDataOnce() {

    }

    protected fun showLoadErrorView(tip: String, block: View.() -> Unit) {
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.loadErrorView)
            if (viewStub != null) {
                loadErrorView = viewStub.inflate()
                val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
                loadErrorText?.text = tip
                val loadErrorkRootView = loadErrorText?.findViewById<View>(R.id.loadErrorkRootView)
                loadErrorkRootView?.setOnClickListener { it.block() }
            }
        }
    }

    /**
     * 将load error view进行隐藏。
     */
    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    /**
     * 调用系统原生分享
     *
     * @param shareContent 分享内容
     * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
     */
    protected fun share(shareContent: String, shareType: Int) {
        ShareUtil.share(this.activity, shareContent, shareType)
    }

    protected fun showDialogShare(shareContent: String) {
        (this.activity as AppCompatActivity).let {
            com.example.kaiyanproject.extension.showDialogShare(
                it,
                shareContent
            )
        }
    }
}