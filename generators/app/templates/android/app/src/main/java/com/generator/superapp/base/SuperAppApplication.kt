package <%= appPackage %>.base

import android.app.Activity
import android.app.Application
import android.app.Service
import <%= appPackage %>.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject


class SuperAppApplication : Application(), HasActivityInjector, HasServiceInjector {
    var tag = SuperAppApplication::class.java.simpleName
    // for injecting activity
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    // for injecting service
    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    // this is required to setup Dagger2 for Activity
    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    // this is required to setup Dagger2 for Service
    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }



}