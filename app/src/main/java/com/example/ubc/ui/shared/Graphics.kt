package com.example.ubc.ui.shared

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt

class Graphics {
    companion object {
        fun setBackgroundColor(view: View, @ColorInt color: Int) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                @Suppress("DEPRECATION")
                view.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            } else {
                view.background.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            }
        }

        fun setVerticalOffset(view: View, y : Int) {
            val param = view.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,y,0,-y)
            view.layoutParams = param
        }

        @SuppressLint("ClickableViewAccessibility")
        fun addPressAnimationOnTouch(touchTarget: View, animatedView: View = touchTarget) {
            touchTarget.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        setVerticalOffset(animatedView, 6)
                        animatedView.alpha = 0.8f
                        touchTarget.callOnClick()
                    }
                    MotionEvent.ACTION_UP -> {
                        setVerticalOffset(animatedView, 0)
                        animatedView.alpha = 1f
                    }
                }
                true
            }
        }
    }
}