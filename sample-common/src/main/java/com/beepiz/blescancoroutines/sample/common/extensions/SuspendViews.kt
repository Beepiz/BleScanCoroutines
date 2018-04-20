package com.beepiz.blescancoroutines.sample.common.extensions

import android.view.View
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun View.awaitOneClick() = suspendCoroutine<Unit> { continuation ->
    setOnClickListener {
        setOnClickListener(null)
        continuation.resume(Unit)
    }
}

suspend fun View.clicks(capacity: Int = Channel.UNLIMITED): ReceiveChannel<Unit> {
    val clicksChannel = Channel<Unit>(capacity)
    setOnClickListener { clicksChannel.offer(Unit) }
    return clicksChannel
}
