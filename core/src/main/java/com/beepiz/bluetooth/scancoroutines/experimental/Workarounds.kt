package com.beepiz.bluetooth.scancoroutines.experimental

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback

internal const val SCANNER_ID_SCANNING_TOO_FREQUENTLY = -2

internal fun BluetoothLeScanner.getScannerId(callback: ScanCallback): Int {
    val scanClientsField = BluetoothLeScanner::class.java.getDeclaredField("mLeScanClients").apply {
        isAccessible = true
    }
    @Suppress("UNCHECKED_CAST")
    val leScanClients = scanClientsField?.get(this)!! as Map<ScanCallback, *>
    val bleScanCallbackWrapper = leScanClients[callback]!!
    val scannerIdField = bleScanCallbackWrapper.javaClass.getDeclaredField("mScannerId").apply {
        isAccessible = true
    }
    return scannerIdField.getInt(bleScanCallbackWrapper)
}
