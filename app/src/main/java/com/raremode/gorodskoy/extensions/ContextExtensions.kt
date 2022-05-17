package com.raremode.gorodskoy.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Context.dpsToIntPixels(float: Float): Int {
    return (this.dpsToFloatPixels(float) + 0.5f).toInt()
}

fun Context.dpsToFloatPixels(float: Float): Float {
    return float * getDensity(this)
}

private fun getDensity(context: Context): Float {
    return context.resources.displayMetrics.density
}

fun Context.bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
    vectorDrawable?.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable?.intrinsicWidth ?: 0,
        vectorDrawable?.intrinsicHeight ?: 0,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable?.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.setupKeyboardListener(callback: (isShown: Boolean) -> Unit) {
    val parentView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    var alreadyOpen = false
    val defaultKeyboardHeightDP = 100
    val estimatedKeyboardDP =
        defaultKeyboardHeightDP + 48
    val rect = Rect()
    parentView.viewTreeObserver.addOnGlobalLayoutListener {
        val estimatedKeyboardHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            estimatedKeyboardDP.toFloat(),
            parentView.resources.displayMetrics
        )
            .toInt()
        parentView.getWindowVisibleDisplayFrame(rect)
        val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
        val isShown = heightDiff >= estimatedKeyboardHeight
        if (isShown == alreadyOpen) {
            return@addOnGlobalLayoutListener
        }
        alreadyOpen = isShown
        callback(isShown)
    }
}

fun Fragment.hideKeyboard() {
    view?.let { context?.hideKeyboard(it) }
}