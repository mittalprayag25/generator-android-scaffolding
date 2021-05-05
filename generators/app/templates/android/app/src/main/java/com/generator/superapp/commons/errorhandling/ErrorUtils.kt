package <%= appPackage %>.commons.errorhandling

import <%= appPackage %>.R

object ErrorUtils {
    fun getErrorMessageId(errorCode: Int): Int {
        return when (errorCode) {
            ErrorOccurred.ERROR_PUSH_REGISTRATION_SERVER -> R.string.error_push_reg_server_failed
            else -> R.string.error_general
        }
    }
}
