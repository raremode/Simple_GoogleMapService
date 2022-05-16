package com.raremode.gorodskoy.extensions

import android.view.View

internal fun View.beGone() {
    visibility = View.GONE
}

internal fun View.beInvisible() {
    visibility = View.INVISIBLE
}

internal fun View.beVisible() {
    visibility = View.VISIBLE
}

internal fun View.beVisibleIf(beVisibleIf: Boolean) = if (beVisibleIf) beVisible() else beGone()

internal fun View.beInvisibleIf(beInvisibleIf: Boolean) = if (beInvisibleIf) beInvisible() else beVisible()

internal fun View.beGoneIf(beGoneIf: Boolean) = if (beGoneIf) beGone() else beVisible()