package <%= appPackage %>.<%= packageName %>;

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import <%= appPackage %>.base.BaseViewModel
import <%= appPackage %>.retrofit.AppUtility
import javax.inject.Inject

class <%= name %>ViewModel
@Inject
constructor(

) : BaseViewModel() {

    @Inject
    lateinit var retro: AppUtility

    private val TAG = <%= name %>ViewModel::class.java.simpleName

    val someSampleData = MutableLiveData<String>()

    /*
    * Write your Logic and function here
    */

    @SuppressLint("CheckResult")
    fun click(){}

    fun getSampleDataValue() : MutableLiveData<String> {
        return someSampleData
    }

}