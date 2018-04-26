package com.beepiz.bluetooth.scancoroutines.experimental

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.support.annotation.RequiresApi
import kotlinx.coroutines.experimental.channels.SendChannel

@RequiresApi(LOLLIPOP)
internal class ChannelScanCallback(private val channel: SendChannel<ScanResult>) : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        channel.offer(result)
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        results.forEach { channel.offer(it) }
    }

    override fun onScanFailed(errorCode: Int) {
        channel.close(ScanFailedException(errorCode))
    }
}
