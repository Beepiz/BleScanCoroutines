package com.beepiz.blescancoroutines.sample.common.extensions

import android.annotation.SuppressLint
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Lifecycle.State.DESTROYED
import android.arch.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

fun Lifecycle.createJob(activeWhile: Lifecycle.State = Lifecycle.State.INITIALIZED): Job {
    require(activeWhile != Lifecycle.State.DESTROYED) {
        "DESTROYED is a terminal state that is forbidden for createJob(…), to avoid leaks."
    }
    return SupervisorJob().also { job ->
        when (currentState) {
            Lifecycle.State.DESTROYED -> job.cancel()
            else -> GlobalScope.launch(Dispatchers.Main) {
                // Ensures state is in sync.
                addObserver(@SuppressLint("RestrictedApi") object : GenericLifecycleObserver {
                    override fun onStateChanged(
                        source: LifecycleOwner?,
                        event: Lifecycle.Event
                    ) {
                        if (currentState < activeWhile) {
                            removeObserver(this)
                            job.cancel()
                        }
                    }
                })
            }
        }
    }
}
