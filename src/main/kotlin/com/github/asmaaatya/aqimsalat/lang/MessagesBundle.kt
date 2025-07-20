package com.github.asmaaatya.aqimsalat.lang

import com.intellij.CommonBundle
import java.util.*

object MessagesBundle {
    private val bundle: ResourceBundle by lazy {
        ResourceBundle.getBundle("messages", Locale.getDefault())
    }

    fun message(key: String, vararg params: Any): String {
        return CommonBundle.message(bundle, key, *params)
    }
}
