package <%= appPackage %>.ui.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import <%= appPackage %>.R
import <%= appPackage %>.base.BaseActivity
//import <%= appPackage %>.ui.main.MainActivity

class SplashActivity: BaseActivity() {
    lateinit var handler: Handler
    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initToolbar() {
        TODO("Not yet implemented")
    }

    override fun initAndObserveViewModel() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        handler = Handler()

        handler?.postDelayed(Runnable {
            //val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish();
        }, 3000)
    }
}