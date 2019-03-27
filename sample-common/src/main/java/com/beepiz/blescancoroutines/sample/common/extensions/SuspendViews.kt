package com.beepiz.blescancoroutines.sample.common.extensions

import android.view.View
import androidx.core.view.isVisible
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun View.awaitOneClick(
    disableAfterClick: Boolean = true,
    hideAfterClick: Boolean = false
) = try {
    if (disableAfterClick) isEnabled = true
    if (hideAfterClick) isVisible = true
    suspendCancellableCoroutine<Unit> { continuation ->
        setOnClickListener { continuation.resume(Unit) }
    }
} finally {
    setOnClickListener(null)
    if (disableAfterClick) isEnabled = false
    if (hideAfterClick) isVisible = false
}
