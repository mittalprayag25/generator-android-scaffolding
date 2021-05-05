package <%= appPackage %>.base

import androidx.annotation.Keep

@Keep
data class BaseModel(val code:Int,val msg:String)