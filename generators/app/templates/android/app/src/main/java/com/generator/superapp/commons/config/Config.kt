package <%= appPackage %>.commons.config

object Config {
    //TODO change the status to false for production
    var LOGGING_STATUS = false
    var SERVER_ENVIRONMENT = Environment.PRODUCTION
    var SERVER_READ_TIMEOUT : Long = 10
    var SERVER_CONNECT_TIMEOUT : Long = 10
    var SERVER_WRITE_TIMEOUT : Long = 10
}