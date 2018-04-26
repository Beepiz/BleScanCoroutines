package com.beepiz.bluetooth.scancoroutines.experimental

import android.annotation.TargetApi
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.os.Build.VERSION_CODES.O_MR1

internal const val SCANNER_ID_SCANNING_TOO_FREQUENTLY = -2

@TargetApi(O_MR1)
internal fun BluetoothLeScanner.getScannerId(callback: ScanCallback): Int {
    val scanClientsField = BluetoothLeScanner::class.java.getDeclaredField("mLeScanClients").apply {
        isAccessible = true
    }
    @Suppress("UNCHECKED_CAST")
    val leScanClients = scanClientsField?.get(this)!! as Map<ScanCallback, *>
    val bleScanCallbackWrapper = leScanClients[callback] ?: return SCANNER_ID_SCANNING_TOO_FREQUENTLY
    val scannerIdField = bleScanCallbackWrapper.javaClass.getDeclaredField("mScannerId").apply {
        isAccessible = true
    }
    return scannerIdField.getInt(bleScanCallbackWrapper)
}
