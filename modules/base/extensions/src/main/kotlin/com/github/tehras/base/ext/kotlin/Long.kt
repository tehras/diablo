package com.github.tehras.base.ext.kotlin

import java.text.NumberFormat

fun Long?.format(): String {
    if (this == null) return "0"

    return NumberFormat
        .getNumberInstance()
        .format(this)
}