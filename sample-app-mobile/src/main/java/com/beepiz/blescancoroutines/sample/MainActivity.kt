package com.beepiz.blescancoroutines.sample

import android.Manifest
import android.bluetooth.le.ScanSettings
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.beepiz.blescancoroutines.sample.common.extensions.awaitOneClick
import com.beepiz.blescancoroutines.sample.common.extensions.createJob
import com.beepiz.bluetooth.scancoroutines.experimental.scanChannel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import splitties.alertdialog.appcompat.alert
import splitties.alertdialog.appcompat.message
import splitties.alertdialog.appcompat.okButton
import splitties.alertdialog.appcompat.onDismiss
import splitties.systemservices.bluetoothManager
import splitties.toast.longToast
import splitties.toast.toast
import splitties.views.dsl.core.setContentView
import splitties.views.onClick
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val job = lifecycle.createJob()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = MainUi(this)
        if (SDK_INT >= 21) CoroutineScope(job).launch {
            getPermission(Manifest.permission.ACCESS_FINE_LOCATION, permissionRequestsChannel, 0) {
                val acknowledgment = CompletableDeferred<Unit>()
                alert {
                    message = "BLE scan needs location permission"
                    okButton { acknowledgment.complete(Unit) }
                    onDismiss { acknowledgment.complete(Unit) }
                }.show()
                acknowledgment.await()
            }
            consumeScanStartStops(ui.btn)
        } else {
            toast("Unsupported device. Android 5.0 or newer required for BLE scan")
            finish()
            return
        }
        setContentView(ui)
    }

    private val permissionRequestsChannel = Channel<Int>(capacity = Channel.CONFLATED)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> permissionRequestsChannel.offer(grantResults.single())
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @RequiresApi(21)
    @ExperimentalCoroutinesApi
    private suspend fun consumeScanStartStops(btn: Button): Nothing {
        val scanner = bluetoothManager.adapter?.bluetoothLeScanner
            ?: error("No BluetoothLeScanner available. Is Bluetooth turned on?")
        val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
        while (true) try {
            btn.text = "Start scan"
            btn.awaitOneClick()
            bluetoothManager.adapter.let {
                if (!it.isOffloadedScanBatchingSupported && !it.isOffloadedFilteringSupported) {
                    longToast("Device may have broken Bluetooth Low Energy support")
                }
            }
            val scanChannel = scanner.scanChannel(null, scanSettings)
            btn.text = "Stop scan"
            btn.onClick { scanChannel.cancel() }
            for (result in scanChannel) {
                Timber.i("$result")
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
