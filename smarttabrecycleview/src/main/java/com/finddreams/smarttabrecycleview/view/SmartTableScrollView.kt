package com.finddreams.smarttabrecycleview.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.OverScroller
import com.finddreams.smarttabrecycleview.listener.OnScrollChangedListener
import java.lang.reflect.Field

/**
 *左右滑动列表
 * Copyright (c) finddreams https://github.com/finddreams
 */
class SmartTableScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs) {
    private var currentScrollL: Int = 0
    private  var scrollChangedListener: OnScrollChangedListener? = null
    var isIntercept: Boolean = true
    lateinit var scrollerField: Field
    var maxScrollX = 0
    var changeListeners= arrayListOf<OnScrollChangedListener>()

    init {
        tag = VIEW_TAG
        overScrollMode = OVER_SCROLL_NEVER
        try {
            scrollerField = javaClass.superclass.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
    fun abortScroll() {
        try {
            val scroller = scrollerField[this] as OverScroller
            if (scroller != null && !scroller.isFinished) {
                scroller.abortAnimation()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    /**
     * 获取当前HorizontalScrollView最大能滑动的距离
     */
    private fun getChildWidth(): Int {
        try {
            if (maxScrollX==0) {
                maxScrollX = getChildAt(0).measuredWidth - measuredWidth
            }
        } catch (e: Exception) {
            Log.e("HSV", "测量最大滑动距离发生异常:$e")
        }
        return maxScrollX
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            scrollChangedListener?.abort()
        }
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                val scrollX = scrollX
                val childWidth = getChildWidth()
//                Log.i("HSV", "scrollX= $scrollX;  getChildWidth==  $childWidth  ")
                //最右边
                if(isIntercept) {
                    if (scrollX >= childWidth) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else if (scrollX == 0) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }
    fun addOnScrollChange(onScrollChangedListener: OnScrollChangedListener){
        scrollChangedListener=onScrollChangedListener
    }

    override fun fling(velocityX: Int) {
        scrollChangedListener?.onFling(velocityX)
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        //当前的CHSCrollView被触摸时，滑动其它
//        Log.i("HSV", "l=$l t=$t  oldl= $oldl;  oldt==  $oldt")
        super.onScrollChanged(l, t, oldl, oldt)
        currentScrollL=l
        scrollChangedListener?.onScrollChanged(l,t,oldl,oldt)
        if (isIntercept) {
            if (l >= getChildWidth()) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else if (l == 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }


    }

    companion object {
        val VIEW_TAG = "TableHorizonScrollView"
    }

}