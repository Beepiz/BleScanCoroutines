package com.beepiz.blescancoroutines.sample

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.ReceiveChannel
import splitties.toast.longToast

suspend inline fun Activity.getPermission(
        permission: String,
        requestResultChannel: ReceiveChannel<Int>,
        reqCode: Int,
        @Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
        acknowledgePermissionRationale: suspend () -> Unit
) {
    if (SDK_INT < 23) return
    while (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        if (shouldShowRequestPermissionRationale(permission)) {
            acknowledgePermissionRationale()
        }
        requestPermissions(arrayOf(permission), reqCode)
        val grantResult = requestResultChannel.receive()
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) continue
            longToast("Permission denied. Grant it into the settings to continue.")
            throw CancellationException("Permission denied + don't ask again")
        }
    }
}
