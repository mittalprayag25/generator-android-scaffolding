// Copyright 2012 Stichting Incident Management Nederland
package <%= appPackage %>.commons.errorhandling

/**
 * Published on the Otto bus when an error occurred. The active view can decide
 * how (and if) to present it to the user. We use error code constants as a package-
 * neutral way of distinguishing between events and as a localization key.
 */
class ErrorOccurred(
        /**
         * The error code distinguishes between exception types. We'll end up with a fairly
         * long list of them.
         */
        val errorCode: Int,
        /**
         * (Optional) an object (typically a throwable or a string) with extra detail about the error.
         */
        val detail: Any?,
        /**
         * The site where `this` error occurred can suggest whether the user should or shouldn't be prompted. The
         * decision is ultimately up to the current view.
         */
        val notifyUser: Boolean) {

    constructor(errorCode: Int, notifyUser: Boolean) : this(errorCode, null, notifyUser)

    override fun toString(): String {
        return "ErrorOccurred{" +
                "errorCode=" + errorCode +
                ", notifyUser=" + notifyUser +
                ", detail=" + detail +
                '}'.toString()
    }

    companion object {

        val ERROR_PUSH_REGISTRATION_SERVER = 10003
    }
}
