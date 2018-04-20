@file:Suppress("NOTHING_TO_INLINE")

package com.beepiz.blescancoroutines.sample.common.extensions

import android.annotation.SuppressLint
import android.arch.core.executor.ArchTaskExecutor
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import splitties.uithread.checkUiThread
import kotlin.coroutines.experimental.CoroutineContext

inline fun launchInUi(
        parent: Job? = null,
        noinline block: suspend CoroutineScope.() -> Unit
): Job {
    checkUiThread()
    return launch(UI, CoroutineStart.UNDISPATCHED, parent, block)
}

object IO : CoroutineDispatcher() {
    @SuppressLint("RestrictedApi")
    private val dispatcher = ArchTaskExecutor.getIOThreadExecutor().asCoroutineDispatcher()
    override fun dispatch(context: CoroutineContext, block: Runnable) = dispatcher.dispatch(context, block)
}
