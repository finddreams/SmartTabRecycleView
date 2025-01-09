package com.finddreams.smarttabrecycleview.recycleview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finddreams.smarttabrecycleview.R
import com.finddreams.smarttabrecycleview.adapter.SmartTableViewItemAdapter
import com.finddreams.smarttabrecycleview.databinding.SmartTableviewRecycleviewBinding
import com.finddreams.smarttabrecycleview.listener.ITableScrollView
import com.finddreams.smarttabrecycleview.listener.OnScrollChangedListener
import com.finddreams.smarttabrecycleview.model.TabViewItemsEntity
import com.finddreams.smarttabrecycleview.model.TableViewDataSet
import com.finddreams.smarttabrecycleview.model.TableViewHeaderEntity
import com.finddreams.smarttabrecycleview.view.SmartTableScrollView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 *标准的左右滑动列表
 *Copyright (c) finddreams https://github.com/finddreams
 */
class SmartTableRecycleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs), ITableScrollView {
    private  var mOnScrolled: RecyclerView.OnScrollListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mItemHeightDp: Float = 0f
    lateinit var mBinding: SmartTableviewRecycleviewBinding
    private lateinit var smartTableViewItemAdapter: SmartTableViewItemAdapter
    private var isCanRefresh: Boolean = true
    var isCanLoadMore: Boolean = false
    private var isNeedFirstColumnIcon: Boolean = false
    private var mFistColumnWidth: Float = 0f
    private var mFistColumnHeaderName: String? = ""
    init {
        if (isInEditMode) {
            LayoutInflater.from(context).inflate(R.layout.smart_tableview_recycleview, this, true)
        } else {
            val layoutInflater = LayoutInflater.from(context)
            mBinding = SmartTableviewRecycleviewBinding.inflate(layoutInflater, this, true)
//            val style = context.obtainStyledAttributes(attrs,
//                R.styleable.base_tabview_style, 0, 0)
//            isCanRefresh = style.getBoolean(R.styleable.base_tabview_style_is_can_refresh, true)
//            isCanLoadMore = style.getBoolean(R.styleable.base_tabview_style_is_can_loadMore, false)
//            //固定的第一列是否需要添加图片
//            isNeedFirstColumnIcon = style.getBoolean(R.styleable.base_tabview_style_is_need_first_column_icon, false)
//            mItemHeightDp = style.getDimension(R.styleable.base_tabview_style_item_height,0f)
//            mFistColumnWidth = style.getDimension(R.styleable.base_tabview_style_fist_column_width,0f)
//            mFistColumnHeaderName = style.getString(R.styleable.base_tabview_style_fist_column_header_name)

            initView()
            initListener()
        }
    }

    private fun initView() {
        initRefreshLayout(isCanRefresh,isCanLoadMore)
        initFirstColumnTitle()
        initItemAdapter()
    }

    private fun initFirstColumnTitle() {
        if (!TextUtils.isEmpty(mFistColumnHeaderName)) {
            mBinding.firstHeaderName.text = mFistColumnHeaderName
        }
        if (mFistColumnWidth != 0f) {
            mBinding.rlTitleName.layoutParams.width = mFistColumnWidth.toInt()
        }
    }
    private fun initItemAdapter() {
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerViewItem.layoutManager = linearLayoutManager
        mBinding.recyclerViewItem.setItemViewCacheSize(20)
        mBinding.recyclerViewItem.setHasFixedSize(true)
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        val pool = RecyclerView.RecycledViewPool()
        pool.setMaxRecycledViews(1, 20)
        mBinding.recyclerViewItem.setRecycledViewPool(pool)
        smartTableViewItemAdapter = SmartTableViewItemAdapter(
            this,
            mItemHeightDp,
            mFistColumnWidth,
            isNeedFirstColumnIcon
        )
        mBinding.recyclerViewItem.adapter =
            smartTableViewItemAdapter
    }

    private fun initListener() {
        mBinding.scrollviewHeader.addOnScrollChange(object : OnScrollChangedListener {
            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                onScrollX(l,t)
            }

            override fun onFling(velocityX: Int) {
               onFlingChild(velocityX)
            }

            override fun abort() {
                abortScroll()
            }

        })
        mBinding.recyclerViewItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recycleViewScrollTo(false,mBinding.scrollviewHeader.scrollX)
                mOnScrolled?.onScrolled(recyclerView,dx,dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mOnScrolled?.onScrollStateChanged(recyclerView,newState)
            }
        })
    }

    fun setRecycleViewScrollListener(
        onScrolled: RecyclerView.OnScrollListener,
    ) {
        mOnScrolled=onScrolled
    }

    fun setTabDataSet(dataSet: TableViewDataSet, onItemContainerClick: (TabViewItemsEntity) -> Unit={},onHeaderItemClick: (TableViewHeaderEntity) -> Unit = {}){
        dataSet.items?.let {
            smartTableViewItemAdapter.updateData(it,dataSet.headers)
            smartTableViewItemAdapter.setOnItemClickListener(onItemContainerClick)
        }
        post {
            recycleViewScrollTo(false, scrollviewHeader.scrollX)
        }
        setHeaderTitles(dataSet.headers) {
            onHeaderItemClick.invoke(it)
        }
    }

    fun setOnItemClickListener(
        onItemContainerClick: (TabViewItemsEntity) -> Unit,
    ) {
        smartTableViewItemAdapter.setOnItemClickListener(onItemContainerClick)
    }


    fun setFistColumnHeaderName(headerName: String){
        mBinding.firstHeaderName.text=headerName
    }
    override val refreshLayout: SmartRefreshLayout
        get() = mBinding.refreshLayout

    override val recyclerViewHeader: RecyclerView
        get() = mBinding.recyclerViewHeader
    override val recyclerViewItem: RecyclerView
        get() = mBinding.recyclerViewItem
    override val scrollviewHeader: SmartTableScrollView
        get() = mBinding.scrollviewHeader

    val llHeader: LinearLayout
        get() = mBinding.llHeader
}