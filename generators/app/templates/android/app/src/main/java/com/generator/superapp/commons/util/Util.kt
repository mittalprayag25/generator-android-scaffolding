package <%= appPackage %>.commons.util

import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.text.TextUtils
import android.widget.Toast
import androidx.multidex.BuildConfig
import <%= appPackage %>.R
import <%= appPackage %>.commons.config.Constants
import <%= appPackage %>.commons.logging.Logs
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object Util {
    private val TAG: String = Util::class.java.simpleName

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun getAppVersion(context: Context): String = context.getString(R.string.version) + " " + BuildConfig.VERSION_NAME

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    /**
     * return false if in settings "Not optimized" and true if "Optimizing battery use"
     */
    fun checkBatteryOptimized(context: Context): Boolean {
        val pwrm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = context.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return !pwrm.isIgnoringBatteryOptimizations(name)
        }
        return false
    }

    fun isNotificationChannelCreated(context: Context, channelId: String): Boolean? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!TextUtils.isEmpty(channelId)) {
                var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = notificationManager.getNotificationChannel(channelId)
                Logs.d(TAG, "check if channel id exist return true else false")
                return channel != null
            }
            Logs.d(TAG, "Channel ID error")
            return false
        } else {
            Logs.d(TAG, "Below OREO")
            return true
        }
    }

    fun getNetworkTypeInfo(context: Context): String {
        var result = "false"
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return "false"
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return "false"
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "3g"
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "4g"
                else -> "unknown"
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> "wifi"
                        ConnectivityManager.TYPE_MOBILE -> "3g"
                        ConnectivityManager.TYPE_ETHERNET -> "2g"
                        else -> "unknown"
                    }

                }
            }
        }

        return result
    }
}