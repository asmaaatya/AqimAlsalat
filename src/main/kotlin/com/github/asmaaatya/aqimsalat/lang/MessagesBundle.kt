package com.github.asmaaatya.aqimsalat.lang

import com.intellij.AbstractBundle
import com.intellij.CommonBundle
import org.jetbrains.annotations.PropertyKey
import java.util.*



object MessagesBundle : AbstractBundle("messages.AqimAlsalatBundle") {
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = "messages.AqimAlsalatBundle") key: String, vararg params: Any): String {
        return getMessage(key, *params)
    }
}

