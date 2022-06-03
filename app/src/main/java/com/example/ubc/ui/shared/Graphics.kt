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

        fun setSize(view: View, width: Int, height: Int) {
            val params = view.layoutParams
            params.width = width
            params.height = height
            view.layoutParams = params
        }

        private fun setVerticalOffset(view: View, y : Int) {
            val param = view.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(param.leftMargin, y, param.rightMargin, -y)
            view.layoutParams = param
        }

        @SuppressLint("ClickableViewAccessibility")
        fun addPressAnimationOnTouch(touchTarget: View, depth : Int = 6, animatedView: View = touchTarget) {
            val params = animatedView.layoutParams as ViewGroup.MarginLayoutParams
            val top = params.topMargin
            val bottom = params.bottomMargin
            val left = params.leftMargin
            val right = params.rightMargin

            touchTarget.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        params.setMargins(left, top + depth, right, bottom - depth)
                        animatedView.layoutParams = params
                        animatedView.alpha = 0.8f
                        touchTarget.callOnClick()
                    }
                    MotionEvent.ACTION_UP -> {
                        params.setMargins(left, top, right, bottom)
                        animatedView.layoutParams = params
                        animatedView.alpha = 1f
                    }
                }
                true
            }
        }
    }
}