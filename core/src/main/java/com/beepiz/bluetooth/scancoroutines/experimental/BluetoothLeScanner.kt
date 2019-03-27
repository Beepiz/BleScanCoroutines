package com.beepiz.bluetooth.scancoroutines.experimental

import android.Manifest
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import splitties.systemservices.bluetoothManager

@RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
@ExperimentalCoroutinesApi
@RequiresApi(21)
fun BluetoothLeScanner.scanChannel(
    filters: List<ScanFilter>?,
    settings: ScanSettings
): ReceiveChannel<ScanResult> {
    requireSafeSettings(settings)
    val scanResultsChannel = Channel<ScanResult>(capacity = Channel.UNLIMITED)
    val scanCallback = ChannelScanCallback(scanResultsChannel)
    scanResultsChannel.invokeOnClose { stopScan(scanCallback) }
    //TODO: Restart scan in loop on faulty devices that are especially pesky with Wi-Fi enabled?
    startScan(filters, settings, scanCallback)
    return scanResultsChannel
}

@RequiresApi(21)
private fun requireSafeSettings(settings: ScanSettings) {
    val callbackType = settings.callbackType
    require(callbackType == ScanSettings.CALLBACK_TYPE_ALL_MATCHES) {
        "Only CALLBACK_TYPE_ALL_MATCHES is supported, because CALLBACK_TYPE_FIRST_MATCH and" +
                "CALLBACK_TYPE_MATCH_LOST are unreliable on many devices, and their behavior" +
                "is not documented. If you need such a feature, use CALLBACK_TYPE_ALL_MATCHES" +
                "(default) and develop your own logic where you control timeouts."
    }
}
