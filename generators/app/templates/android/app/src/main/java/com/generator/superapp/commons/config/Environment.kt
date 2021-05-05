package <%= appPackage %>.commons.config

import <%= appPackage %>.commons.config.Constants.SERVER_URL_DEVELOPMENT
import <%= appPackage %>.commons.config.Constants.SERVER_URL_PRODUCTION
import <%= appPackage %>.commons.config.Constants.SERVER_URL_STAGING

enum class Environment(val environment: String) {
    DEVELOPMENT("Development") {
        override fun getServerUrl(): String {
            return SERVER_URL_DEVELOPMENT
        }
    },
    STAGING("Staging") {
        override fun getServerUrl(): String {
            return SERVER_URL_STAGING
        }

    },
    PRODUCTION("Production") {
        override fun getServerUrl(): String {
            return SERVER_URL_PRODUCTION
        }

    };

    abstract fun getServerUrl(): String
}