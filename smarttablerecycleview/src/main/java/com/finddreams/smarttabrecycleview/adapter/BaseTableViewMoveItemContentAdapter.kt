package com.finddreams.smarttabrecycleview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finddreams.smarttabrecycleview.R
import com.finddreams.smarttabrecycleview.databinding.SmartTableviewRecycleviewItemChildContentBinding
import com.finddreams.smarttabrecycleview.model.TableViewChildItemEntity
import com.finddreams.smarttabrecycleview.model.TableViewHeaderEntity
import com.finddreams.smarttabrecycleview.recycleview.BaseBindingViewHolder
import com.finddreams.smarttabrecycleview.utils.dp


/**
 *可左右移动的adapter
 * Copyright (c) finddreams https://github.com/finddreams
 */
class BaseTableViewMoveItemContentAdapter(private var itemHeightDp:Float) :
    RecyclerView.Adapter<BaseBindingViewHolder<SmartTableviewRecycleviewItemChildContentBinding>>() {
    private var list= listOf<TableViewChildItemEntity>()
    private var headers= listOf<TableViewHeaderEntity>()

    fun updateData(list: List<TableViewChildItemEntity>, headers: List<TableViewHeaderEntity>) {
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
        itemType: Int
    ): BaseBindingViewHolder<SmartTableviewRecycleviewItemChildContentBinding> {
        val binding: SmartTableviewRecycleviewItemChildContentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.smart_tableview_recycleview_item_child_content,
            viewGroup, false
        )
        if (itemHeightDp!=0f) {
            binding.tvContent.layoutParams.height = itemHeightDp.toInt()
        }
        return BaseBindingViewHolder<SmartTableviewRecycleviewItemChildContentBinding>(binding)
    }

    override fun onBindViewHolder(
        viewHolder: BaseBindingViewHolder<SmartTableviewRecycleviewItemChildContentBinding>,
        i: Int) {
        val context = viewHolder.context
        val binding = viewHolder.binding
        val item = list[i]
        val header = headers[i]
        if (header.width!=0){
            binding.tvContent.layoutParams.width=header.width.dp
        }
        if(i==itemCount-1){
            binding.tvContent.setPadding(0,0,12.dp,0)
        }
//        LogUtils.d("child onBind $i ${item.content}")
        binding.tvContent.text = item.value
        binding.tvContent.textSize = item.textSize
        item.color?.let { binding.tvContent.setTextColor(context.getColor(it)) }
        binding.executePendingBindings()
    }

}