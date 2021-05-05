package <%= appPackage %>.commons.logging

import android.util.Log
import <%= appPackage %>.commons.config.Config

object Logs {
    /* Send a DEBUG log message. */
    fun d(tag: String, message: String) {
        if (Config.LOGGING_STATUS)
            Log.d(tag, message)
    }

    /* Send a ERROR log message. */
    fun e(tag: String, message: String) {
        if (Config.LOGGING_STATUS)
            Log.e(tag, message)
    }

}