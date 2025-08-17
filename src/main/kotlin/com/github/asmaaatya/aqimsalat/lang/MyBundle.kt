package com.github.asmaaatya.aqimsalat.lang


import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

import org.jetbrains.annotations.NonNls

@NonNls
private const val BUNDLE = "messages.MyBundle"
object MyBundle : DynamicBundle(BUNDLE) {

    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)

    @Suppress("unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getLazyMessage(key, *params)
}