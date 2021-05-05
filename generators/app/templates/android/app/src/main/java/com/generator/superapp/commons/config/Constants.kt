package <%= appPackage %>.commons.config

object Constants {
    const val SERVER_URL_STAGING = "SomeURL"
    const val SERVER_URL_DEVELOPMENT = ""
    const val SERVER_URL_PRODUCTION = "Prod_Url"

    const val ANDROID_JS_BRIDGE = "HIAPPBRIDGE"
    const val HI_APP_LANDING_PAGE = "file:///android_asset/public/index.html"

    object Endpoint {
        /**
         * Endpoint path for the registration command.
         */
        const val REGISTER_DEVICE = "/api/frontend/devices"
    }

    // For dynamic permissions
    const val DYNAMIC_PERMISSION_CODE = 1357

}