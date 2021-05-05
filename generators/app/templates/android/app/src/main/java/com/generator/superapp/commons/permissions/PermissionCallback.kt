package <%= appPackage %>.commons.permissions

internal interface PermissionCallback{
    fun permissionGranted(requestCode: Int)
    fun partialPermissionGranted(requestCode: Int, grantedPermissions: ArrayList<String>)
    fun permissionDenied(requestCode: Int)
    fun neverAskAgain(requestCode: Int)
}