package com.beepiz.blescancoroutines.sample

import android.bluetooth.le.ScanSettings
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.beepiz.blescancoroutines.sample.common.extensions.awaitOneClick
import com.beepiz.blescancoroutines.sample.common.extensions.createJob
import com.beepiz.blescancoroutines.sample.common.extensions.launchInUi
import com.beepiz.bluetooth.scancoroutines.experimental.BleScanner
import splitties.toast.toast
import splitties.viewdsl.core.setContentView
import splitties.views.onClick
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val job = lifecycle.createJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = MainUi(this)
        if (SDK_INT >= LOLLIPOP) launchInUi(parent = job) {
            consumeScanStartStops(ui.btn)
        } else {
            toast("Unsupported device. Android 5.0 or newer required for BLE scan")
            finish()
            return
        }
        setContentView(ui)

    }

    @RequiresApi(LOLLIPOP)
    private suspend fun consumeScanStartStops(btn: Button): Nothing {
        val scanner = BleScanner()
        val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
        while (true) try {
            btn.text = "Start scan"
            btn.awaitOneClick()
            val scanChannel = scanner.scan(null, scanSettings)
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
