package com.finddreams.smarttablerecycleview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finddreams.smarttablerecycleview.R
import com.finddreams.smarttablerecycleview.databinding.SmartTableviewRecycleviewItemChildContentBinding
import com.finddreams.smarttablerecycleview.model.TableChildItemEntity
import com.finddreams.smarttablerecycleview.model.TableHeaderEntity
import com.finddreams.smarttablerecycleview.viewholder.BaseBindingViewHolder
import com.finddreams.smarttablerecycleview.utils.dp


/**
 *可左右移动的adapter
 * Copyright (c) finddreams https://github.com/finddreams
 */
class BaseTableViewMoveItemContentAdapter(private var itemHeightDp:Float) :
    RecyclerView.Adapter<BaseBindingViewHolder<SmartTableviewRecycleviewItemChildContentBinding>>() {
    private var list= listOf<TableChildItemEntity>()
    private var headers= listOf<TableHeaderEntity>()

    fun updateData(list: List<TableChildItemEntity>, headers: List<TableHeaderEntity>) {
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
        binding.tvContent.text = item.value
        binding.tvContent.textSize = item.textSize
        item.color?.let { binding.tvContent.setTextColor(context.getColor(it)) }
        binding.executePendingBindings()
    }

}