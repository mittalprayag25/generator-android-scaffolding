package <%= appPackage %>.retrofit

interface AppUtility {
    fun getInstance(): SuperAppServiceApi
    fun getInstance(token: String): SuperAppServiceApi
}
