package <%= appPackage %>.commons.permissions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import <%= appPackage %>.R
import <%= appPackage %>.commons.util.Util
import <%= appPackage %>.commons.logging.Logs
import <%= appPackage %>.commons.config.Constants
import java.util.*


class PermissionUtils(internal var context: Context) {
    private val TAG = PermissionUtils::class.java.simpleName
    internal var permissionResultCallback: PermissionCallback = context as PermissionCallback
    internal var permissionList = ArrayList<String>()
    internal var listPermissionsNeeded = ArrayList<String>()
    internal var dialogContent = ""
    internal var reqCode: Int = 0


    fun checkPermission(permissions: ArrayList<String>, dialogContent: String, requestCode: Int) {
        this.permissionList = permissions
        this.dialogContent = dialogContent
        this.reqCode = requestCode

        if (checkAndRequestPermissions(permissions, requestCode)) {
            permissionResultCallback.permissionGranted(requestCode)
        }
    }

    private fun checkAndRequestPermissions(permissions: ArrayList<String>, requestCode: Int): Boolean {

        if (permissions.size > 0) {
            listPermissionsNeeded = ArrayList()

            for (i in permissions.indices) {
                val hasPermission = ContextCompat.checkSelfPermission(context, permissions[i])

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions[i])
                }

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(context as Activity, listPermissionsNeeded.toTypedArray(), requestCode)
                return false
            }
        }

        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.DYNAMIC_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val perms = HashMap<String, Int>()

                for (i in permissions.indices) {
                    perms[permissions[i]] = grantResults[i]
                }

                val pendingPermissions = ArrayList<String>()

                for (i in listPermissionsNeeded.indices) {
                    if (perms[listPermissionsNeeded[i]] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, listPermissionsNeeded[i]))
                            pendingPermissions.add(listPermissionsNeeded[i])
                        else {
                            permissionResultCallback.neverAskAgain(reqCode)
                            Util.showToast(context, "Go to settings and enable permissions")
                            return
                        }
                    }

                }

                if (pendingPermissions.size > 0) {
                    showMessageOKCancel(dialogContent,
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkPermission(permissionList, dialogContent, reqCode)
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        Logs.d(TAG, "permisson not fully given")
                                        if (permissionList.size == pendingPermissions.size) {
                                            permissionResultCallback.permissionDenied(reqCode)
                                        } else {
                                            permissionResultCallback.partialPermissionGranted(reqCode, pendingPermissions)
                                        }

                                    }
                                }
                            })

                } else {
                    permissionResultCallback.permissionGranted(reqCode)

                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), okListener)
                .setNegativeButton(context.getString(R.string.cancel), okListener)
                .create()
                .show()
    }

}