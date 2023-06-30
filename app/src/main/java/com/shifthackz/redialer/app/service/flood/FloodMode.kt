package com.shifthackz.redialer.app.service.flood

sealed class FloodMode {
    object Loop : FloodMode()
    data class Count(val count: Int) : FloodMode()

    override fun toString(): String = when (this) {
        is Count -> "Fixed count = ${this.count}"
        Loop -> "Endless"
    }
}
