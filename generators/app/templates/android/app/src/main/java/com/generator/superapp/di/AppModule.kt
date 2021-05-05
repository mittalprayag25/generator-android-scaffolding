package <%= appPackage %>.di

import android.content.Context
import <%= appPackage %>.commons.preference.PreferenceHelper
import dagger.Module
import dagger.Provides
import <%= appPackage %>.retrofit.AppServiceModuleImpl
import <%= appPackage %>.retrofit.AppUtility
import javax.inject.Singleton


@Module(includes = arrayOf(ViewModelModule::class))
internal class AppModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(ctx: Context): PreferenceHelper {
        return PreferenceHelper(ctx)
    }


    @Provides
    @Singleton
    internal fun provideSuperAppServiceApi(): AppUtility {
        return AppServiceModuleImpl()
    }

}