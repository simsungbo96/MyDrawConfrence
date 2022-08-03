package com.sbsj.mydrawconfrence

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class AppPermission constructor(var activity: Activity) {


    private var permissionDeniedList = ArrayList<String>()


    fun check(permission: MutableList<String>) {

        for (p in permission) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    p
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionDeniedList.add(p)
            }

        }
        permissionGranted()
    }

    private fun permissionGranted(){
        if(permissionDeniedList.size > 0){
            val permission = permissionDeniedList.toArray(arrayOfNulls<String>(permissionDeniedList.size))
            ActivityCompat.requestPermissions(activity,permission,100)
        }else{ // 권한부여완료

        }
    }
}
