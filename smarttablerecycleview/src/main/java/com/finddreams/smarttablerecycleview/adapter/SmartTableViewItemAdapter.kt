package com.finddreams.smarttablerecycleview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finddreams.smarttablerecycleview.R
import com.finddreams.smarttablerecycleview.databinding.SmartTableviewRecycleviewItemBinding
import com.finddreams.smarttablerecycleview.listener.OnScrollChangedListener
import com.finddreams.smarttablerecycleview.model.TableItemsEntity
import com.finddreams.smarttablerecycleview.model.TableHeaderEntity
import com.finddreams.smarttablerecycleview.viewholder.BaseBindingViewHolder
import com.finddreams.smarttablerecycleview.SmartTableRecycleView
import com.finddreams.smarttablerecycleview.utils.dp

/**
 * mFistColumnWidthDp为固定的第一列的宽度
 * Copyright (c) finddreams https://github.com/finddreams
 *
 */
class SmartTableViewItemAdapter(
    private var baseRecycleViewHSView: SmartTableRecycleView,
    var mItemHeightDp: Float,
    private var mFistColumnWidthDp: Float,
) :
    RecyclerView.Adapter<BaseBindingViewHolder<SmartTableviewRecycleviewItemBinding>>() {
    private var mOnItemContainerClick: ((TableItemsEntity) -> Unit?)? = null
    private lateinit var childMoveViewItemAdapter: BaseTableViewMoveItemContentAdapter
    private var list = listOf<TableItemsEntity>()
    private var headers = listOf<TableHeaderEntity>()

    fun updateData(list: List<TableItemsEntity>, headers: ArrayList<TableHeaderEntity>) {
        this.list = list
        this.headers = headers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        itemType: Int,
    ): BaseBindingViewHolder<SmartTableviewRecycleviewItemBinding> {
        val binding: SmartTableviewRecycleviewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.smart_tableview_recycleview_item,
            viewGroup, false
        )
        val recycledViewPool = RecyclerView.RecycledViewPool()
        recycledViewPool.setMaxRecycledViews(1, 10)
        binding.recyclerViewChildMove.setItemViewCacheSize(10)
        binding.recyclerViewChildMove.setRecycledViewPool(recycledViewPool)
        if (mItemHeightDp != 0f) {
            binding.moveLayout.layoutParams.height = mItemHeightDp.toInt()
            binding.itemClickLl.layoutParams.height = mItemHeightDp.toInt()
            binding.llFirstContiner.layoutParams.height = mItemHeightDp.toInt()
        }
        if (mFistColumnWidthDp != 0f) {
            binding.llFirstContiner.layoutParams.width = mFistColumnWidthDp.toInt()
        }
        var linearLayoutManager =
            LinearLayoutManager(viewGroup.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewChildMove.layoutManager = linearLayoutManager
        childMoveViewItemAdapter = BaseTableViewMoveItemContentAdapter(mItemHeightDp)
        binding.recyclerViewChildMove.adapter = childMoveViewItemAdapter
        binding.recyclerViewChildMove.setHasFixedSize(true)
        binding.recyclerViewChildMove.setItemViewCacheSize(10)
        binding.recyclerViewChildMove.isNestedScrollingEnabled = false
        binding.horizontalScrollview.addOnScrollChange(object : OnScrollChangedListener {
            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                baseRecycleViewHSView.onScrollX(l, 0)
            }

            override fun onFling(velocityX: Int) {
                baseRecycleViewHSView.onFlingChild(velocityX)
            }

            override fun abort() {
                baseRecycleViewHSView.abortScroll()
            }

        })
        var holder = BaseBindingViewHolder(binding)
        binding.moveLayout.setOnClickListener {
            holder.mOnItemClickListener?.onItemClick(holder.bindingAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(
        viewHolder: BaseBindingViewHolder<SmartTableviewRecycleviewItemBinding>,
        i: Int,
    ) {
        val binding = viewHolder.binding
        val item = list[i]
        item.childItems.apply {
            (binding.recyclerViewChildMove.adapter as BaseTableViewMoveItemContentAdapter).updateData(
                this,
                headers
            )
        }
        binding.itemClickLl.isPressed = false
        binding.tvStockName.apply {
            text = item.firstColumName
            setPadding(12.dp, 0, 0, 0)
        }

        binding.tvStockCode.text = item.firstColumValue
        viewHolder.setItemClickListener(object : BaseBindingViewHolder.OnItemClickListener {
            override fun onItemClick(position: Int) {
                binding.itemClickLl.isPressed = true
                binding.itemClickLl.postDelayed({
                    binding.itemClickLl.isPressed = false
                }, 150)
                mOnItemContainerClick?.invoke(item)
            }
        })

        binding.executePendingBindings()
    }

    fun setOnItemClickListener(onItemContainerClick: (TableItemsEntity) -> Unit) {
        mOnItemContainerClick = onItemContainerClick
    }


}