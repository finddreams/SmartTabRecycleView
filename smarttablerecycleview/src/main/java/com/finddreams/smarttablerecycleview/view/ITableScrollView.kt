package com.finddreams.smarttablerecycleview.view

import android.view.View
import android.widget.OverScroller
import com.finddreams.smarttablerecycleview.listener.ITableView
import com.finddreams.smarttablerecycleview.model.TableHeaderEntity
import com.finddreams.smarttablerecycleview.utils.setupScrollHeaderAdapter
import kotlin.math.max

/**
 * 滑动的base类
 * Copyright (c) finddreams https://github.com/finddreams
 */
interface ITableScrollView : ITableView {
    /**
     * 设置标题，可只显示标题，没有列表内容
     */
    fun  setHeaderTitles(
        scrollHeaderList: ArrayList<TableHeaderEntity>,
        onHeaderItemClick: (TableHeaderEntity) -> Unit = {}
    ) {
        recyclerViewHeader?.setupScrollHeaderAdapter(scrollHeaderList, onHeaderItemClick)?.models=scrollHeaderList
    }


    fun onFlingChild(velocityX: Int) {
        onFlingChild(scrollviewHeader, velocityX)
        recyclerViewItem?.apply {
            val childCount: Int = childCount
            for (i in 0 until childCount) {
                val view: View = getChildAt(i)
                val scrollView: SmartTableScrollView? =
                    view.findViewWithTag(SmartTableScrollView.VIEW_TAG)
                onFlingChild(scrollView, velocityX)
            }
        }

    }

    fun abortScroll() {
        recycleViewScrollTo(true)
        scrollviewHeader?.abortScroll()
    }

    fun recycleViewScrollTo(isAbort: Boolean, l: Int = 0) {
        recyclerViewItem?.apply {
            val childCount: Int = childCount
            for (i in 0 until childCount) {
                val view: View = getChildAt(i)
                val scrollView: SmartTableScrollView? =
                    view.findViewWithTag(SmartTableScrollView.VIEW_TAG)
                if (isAbort) {
                    scrollView?.abortScroll()
                } else {
                    scrollView?.scrollTo(l, 0)
                }
            }
        }

    }

    fun onScrollX(l: Int, i: Int) {
        scrollviewHeader?.scrollTo(l, 0)
        recycleViewScrollTo(false, l)
    }

    fun onFlingChild(
        scrollView: SmartTableScrollView?,
        velocityX: Int,
    ) {
        if (scrollView != null) {
            val width: Int =
                scrollView.width - scrollView.getPaddingRight() - scrollView.getPaddingLeft()
            val right: Int = scrollView.getChildAt(0).width
            var scroller: OverScroller? = null
            try {
                scroller = scrollView.scrollerField?.get(scrollView) as OverScroller?
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            if (scroller != null) {
                scroller.fling(
                    scrollView.scrollX, scrollView.scrollY, velocityX,
                    0, 0, max(0, right - width), 0, 0,
                    width / 2, 0
                )
                scrollView.postInvalidateOnAnimation()
            }
        }
    }
}