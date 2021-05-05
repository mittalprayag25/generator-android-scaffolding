package <%= appPackage %>.platforms.web

import android.content.Context
import android.webkit.JavascriptInterface
import <%= appPackage %>.commons.util.Util

class HiAppJSBridge (context: Context){

    var context = context

    @JavascriptInterface
    fun getNetworkInfo(): String {
        return  Util.getNetworkTypeInfo(context)
    }
}