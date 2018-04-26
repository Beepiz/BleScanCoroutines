package com.beepiz.bluetooth.scancoroutines.experimental

import android.Manifest
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.O_MR1
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import splitties.exceptions.illegal
import splitties.systemservices.bluetoothManager
import kotlin.coroutines.experimental.CoroutineContext

/**
 * @throws IllegalStateException if there's no [BluetoothLeScanner] available (e.g. because
 * Bluetooth is turned off).
 */
@RequiresApi(LOLLIPOP)
class BleScanner(private val context: CoroutineContext = CommonPool) {
    private val scanner: BluetoothLeScanner = bluetoothManager.adapter?.bluetoothLeScanner
            ?: illegal("No BluetoothLeScanner available. Is Bluetooth turned on?")

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun scan(filters: List<ScanFilter>?, settings: ScanSettings): ReceiveChannel<ScanResult> {
        requireSafeSettings(settings)
        return produce(context) {
            val scanResultsChannel = Channel<ScanResult>(capacity = UNLIMITED)
            val scanCallback = ChannelScanCallback(scanResultsChannel)
            scanner.startScan(filters, settings, scanCallback)
            try {
                if (SDK_INT >= O_MR1 && scanCallback.isScanningTooFrequently()) {
                    scanResultsChannel.close(ScanFailedException(SCAN_FAILED_SCANNING_TOO_FREQUENTLY))
                }
                for (scanResult in scanResultsChannel) {
                    send(scanResult)
                }
            } finally {
                scanner.stopScan(scanCallback)
            }
        }
    }

    private fun requireSafeSettings(settings: ScanSettings) {
        val callbackType = settings.callbackType
        require(callbackType == ScanSettings.CALLBACK_TYPE_ALL_MATCHES) {
            "Only CALLBACK_TYPE_ALL_MATCHES is supported, because CALLBACK_TYPE_FIRST_MATCH and" +
                    "CALLBACK_TYPE_MATCH_LOST are unreliable on many devices, and their behavior" +
                    "is not documented. If you need such a feature, use CALLBACK_TYPE_ALL_MATCHES" +
                    "(default) and develop your own logic where you control timeouts."
        }
    }

    @RequiresApi(O_MR1)
    private fun ScanCallback.isScanningTooFrequently(): Boolean {
        return scanner.getScannerId(this) == SCANNER_ID_SCANNING_TOO_FREQUENTLY
    }
}
