package com.example.kaiyanproject.util

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.example.kaiyanproject.KaiYanApplication
import com.example.kaiyanproject.extension.logW
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

object GlobalUtil {
    private var TAG = "GlobalUtil"

    /**
     * 获取当前应用程序的包名。
     *
     * @return 当前应用程序的包名。
     */
    val appPackage: String
        get() = KaiYanApplication.context.packageName

    /**
     * 获取当前应用程序的名称。
     * @return 当前应用程序的名称。
     */
    val appName: String
        get() = KaiYanApplication.context.resources.getString(KaiYanApplication.context.applicationInfo.labelRes)

    /**
     * 获取当前应用程序的版本名。
     * @return 当前应用程序的版本名。
     */
    val appVersionName: String
        get() = KaiYanApplication.context.packageManager.getPackageInfo(
            appPackage,
            0
        ).versionName

    /**
     * 获取当前应用程序的版本号。
     * @return 当前应用程序的版本号。
     */
    val appVersionCode: Long
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            KaiYanApplication.context.packageManager.getPackageInfo(
                appPackage,
                0
            ).longVersionCode
        } else {
            KaiYanApplication.context.packageManager.getPackageInfo(
                appPackage,
                0
            ).versionCode.toLong()
        }

    /**
     * 获取开眼应用程序的版本名。
     * @return 开眼当前应用程序的版本名。
     */
    val eyepetizerVersionName: String
        get() = "6.3.1"

    /**
     * 获取开眼应用程序的版本号。
     * @return 开眼当前应用程序的版本号。
     */
    val eyepetizerVersionCode: Long
        get() = 6030012

    /**
     * 获取设备的的型号，如果无法获取到，则返回Unknown。
     * @return 设备型号。
     */
    val deviceModel: String
        get() {
            var deviceModel = Build.MODEL
            if (TextUtils.isEmpty(deviceModel)) {
                deviceModel = "unknown"
            }
            return deviceModel
        }

    /**
     * 获取设备的品牌，如果无法获取到，则返回Unknown。
     * @return 设备品牌，全部转换为小写格式。
     */
    val deviceBrand: String
        get() {
            var deviceBrand = Build.BRAND
            if (TextUtils.isEmpty(deviceBrand)) {
                deviceBrand = "unknown"
            }
            return deviceBrand.toLowerCase(Locale.getDefault())
        }

    private var deviceSerial: String? = null

    /**
     * 获取设备的序列号。如果无法获取到设备的序列号，则会生成一个随机的UUID来作为设备的序列号，UUID生成之后会存入缓存，
     * 下次获取设备序列号的时候会优先从缓存中读取。
     * @return 设备的序列号。
     */
    @SuppressLint("HardwareIds")
    fun getDeviceSerial(): String {
        if (deviceSerial == null) {
            var deviceId: String? = null
            val appChannel = getApplicationMetaData("APP_CHANNEL")
            if ("google" != appChannel || "samsung" != appChannel) {
                try {
                    deviceId = Settings.Secure.getString(
                        KaiYanApplication.context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } catch (e: Exception) {
                    logW(TAG, "get android_id with error", e)
                }
                if (!TextUtils.isEmpty(deviceId) && deviceId!!.length < 255) {
                    deviceSerial = deviceId
                    return deviceSerial.toString()
                }
            }
            var uuid = DataStoreUtils.readStringData("uuid", "")
            if (!TextUtils.isEmpty(uuid)) {
                deviceSerial = uuid
                return deviceSerial.toString()
            }
            uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.getDefault())
            CoroutineScope(Dispatchers.IO).launch { DataStoreUtils.saveStringData("uuid", uuid) }
            deviceSerial = uuid
            return deviceSerial.toString()
        } else {
            return deviceSerial.toString()
        }
    }

    /**
     * 获取AndroidManifest.xml文件中，<application>标签下的meta-data值。
     *
     * @param key
     *  <application>标签下的meta-data健
     */
    fun getApplicationMetaData(key: String): String? {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = KaiYanApplication.context.packageManager.getApplicationInfo(
                appPackage,
                PackageManager.GET_META_DATA
            )
        } catch (e: PackageManager.NameNotFoundException) {
            logW(TAG, e.message, e)
        }
        if (applicationInfo == null) return ""
        return applicationInfo.metaData.getString(key)
    }

    /**
     * 判断某个应用是否安装。
     * @param packageName
     * 要检查是否安装的应用包名
     * @return 安装返回true，否则返回false。
     */
    fun isInstalled(packageName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            KaiYanApplication.context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo != null
    }

    /**
     * 获取当前应用程序的图标。
     */
    fun getAppIcon(): Drawable {
        val packageManager = KaiYanApplication.context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(appPackage, 0)
        return packageManager.getApplicationIcon(applicationInfo)
    }

    /**
     * 判断手机是否安装了QQ。
     */
    fun isQQInstalled() = isInstalled("com.tencent.mobileqq")

    /**
     * 判断手机是否安装了微信。
     */
    fun isWechatInstalled() = isInstalled("com.tencent.mm")

    /**
     * 判断手机是否安装了微博。
     * */
    fun isWeiboInstalled() = isInstalled("com.sina.weibo")


    /**
     * 获取指定资源名的资源id。
     *
     * @param name
     * 资源名
     * @param type
     * 资源类型
     * @return 指定资源名的资源id。
     */
    fun getResourceId(name: String, type: String): Int =
        KaiYanApplication.context.resources.getIdentifier(name, type, appPackage)

}