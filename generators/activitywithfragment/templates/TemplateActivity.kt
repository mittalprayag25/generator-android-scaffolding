package <%= appPackage %>.<%= packageName %>;

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import <%= appPackage %>.R
import <%= appPackage %>.base.BaseActivity
import <%= appPackage %>.di.SuperAppModelFactory
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class <%= name %> : BaseActivity(), HasSupportFragmentInjector {

    private lateinit var viewModel: <%= name %>ViewModel
    
    @Inject
    lateinit var viewModelFactory: SuperAppModelFactory

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.<%= layoutName %>)

        initView()
        initToolbar()
        initAndObserveViewModel()
    }


    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initToolbar() {
        TODO("Not yet implemented")
    }

    override fun initAndObserveViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(<%= name %>ViewModel::class.java)

        /**
         * Example below for reference
         */
        viewModel.getSampleDataValue().observe(this, Observer {
            println("initAndObserveViewModel$it")
        })
    }

    override
    fun supportFragmentInjector() = dispatchingAndroidInjector
}