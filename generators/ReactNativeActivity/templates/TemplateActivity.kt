package <%= appPackage %>.<%= packageName %>;

import android.os.Bundle
import android.app.Activity
import android.view.KeyEvent
import com.facebook.react.BuildConfig
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
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

class <%= name %> : BaseActivity(), DefaultHardwareBackBtnHandler {

    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    private lateinit var viewModel: <%= name %>ViewModel
    
    @Inject
    lateinit var viewModelFactory: SuperAppModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        mReactRootView = ReactRootView(this)
        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackage(MainReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()
        mReactRootView!!.startReactApplication(mReactInstanceManager, "HiAppRnStarter", null)
        setContentView(mReactRootView)

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

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onHostPause(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onHostResume(this, this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onHostDestroy(this)
        }
        if (mReactRootView != null) {
            mReactRootView!!.unmountReactApplication()
        }
    }

    override fun onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager!!.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

}