package <%= appPackage %>.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {
    abstract fun initView(): Unit
    abstract fun initToolbar(): Unit
    abstract fun initAndObserveViewModel(): Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
