package com.beepiz.bluetooth.scancoroutines.experimental

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.support.annotation.RequiresApi
import kotlinx.coroutines.channels.SendChannel

@RequiresApi(21)
internal class ChannelScanCallback(private val channel: SendChannel<ScanResult>) : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        runCatching { channel.offer(result) }
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        runCatching { results.forEach { channel.offer(it) } }
    }

    override fun onScanFailed(errorCode: Int) {
        channel.close(ScanFailedException(errorCode))
    }
}
