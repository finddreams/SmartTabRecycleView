package com.finddreams.smarttabrecycleview.listener

import androidx.recyclerview.widget.RecyclerView
import com.finddreams.smarttabrecycleview.view.SmartTableScrollView
import com.scwang.smart.refresh.layout.SmartRefreshLayout

interface ITableView {

    val refreshLayout: SmartRefreshLayout
    val recyclerViewHeader : RecyclerView?
        get() = null
    //滑动的header,必须赋值才能联动
    val scrollviewHeader: SmartTableScrollView?
        get() = null
    //滑动的列表,必须赋值item才能左右联动
    val recyclerViewItem: RecyclerView?
        get() = null

    fun initRefreshLayout(enableRefresh: Boolean = true, enableLoadMore: Boolean = false) {
        refreshLayout.setEnableLoadMore(enableLoadMore)
        refreshLayout.setEnableRefresh(enableRefresh)
    }
    fun finishRefresh() {
        refreshLayout.finishRefresh()
    }



    fun finishLoadMore(delay: Int = 0, success: Boolean = true, noMoreData: Boolean = true) {
        refreshLayout.finishLoadMore(delay, success, noMoreData)
    }
    fun finishLoadMore() {
        refreshLayout.finishLoadMore()
    }
    fun setNoMoreData(noMore: Boolean) {
        refreshLayout.setNoMoreData(noMore)
    }
    fun setEnableLoadMore(noMore: Boolean) {
        refreshLayout.setEnableLoadMore(noMore)
    }
    fun setEnableRefresh(enableRefresh: Boolean) {
        refreshLayout.setEnableRefresh(enableRefresh)
    }

}

