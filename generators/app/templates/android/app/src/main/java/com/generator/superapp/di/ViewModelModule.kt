package <%= appPackage %>.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class ViewModelModule {

    // Auto Generate code by Yeoman

    // TODO: Add More here

    @Binds
    abstract fun bindViewModelFactory(factory: SuperAppModelFactory): ViewModelProvider.Factory
}