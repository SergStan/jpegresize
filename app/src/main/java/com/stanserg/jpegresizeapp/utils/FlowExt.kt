package com.stanserg.jpegresizeapp.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow

fun <T> StateFlow<T>.collectWhenStarted(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        this@collectWhenStarted.collect { action(it) }
    }
}