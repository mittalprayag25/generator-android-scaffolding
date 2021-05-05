package <%= appPackage %>.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import <%= appPackage %>.ui.main.MainViewModel
//import <%= appPackage %>.ui.main.dashboard.viewmodel.DashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class ViewModelModule {

/*
    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindActivation(viewModel: MainViewModel): ViewModel
*/

    // TODO: Add More here

    @Binds
    abstract fun bindViewModelFactory(factory: SuperAppModelFactory): ViewModelProvider.Factory
}