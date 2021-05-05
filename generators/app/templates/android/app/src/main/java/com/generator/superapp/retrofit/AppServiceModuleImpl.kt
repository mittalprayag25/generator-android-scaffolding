package <%= appPackage %>.retrofit


class AppServiceModuleImpl : AppUtility {

    private var superAppServiceApi: SuperAppServiceApi? = null
    private var superAppServiceApiLogin: SuperAppServiceApi? = null

    override fun getInstance(): SuperAppServiceApi {
        if (superAppServiceApiLogin == null) {
            superAppServiceApiLogin = SuperAppServiceApi()
        }
        return superAppServiceApiLogin as SuperAppServiceApi
    }

    override fun getInstance(token: String): SuperAppServiceApi {
        if (superAppServiceApi == null) {
            superAppServiceApi = SuperAppServiceApi(token)
        }
        return superAppServiceApi as SuperAppServiceApi
    }
}
