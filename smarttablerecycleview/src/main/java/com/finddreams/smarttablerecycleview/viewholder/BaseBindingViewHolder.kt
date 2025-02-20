package com.finddreams.smarttablerecycleview.viewholder

import android.content.Context
import android.view.View
import android.view.View.OnLongClickListener
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright (c) finddreams https://github.com/finddreams
 */
class BaseBindingViewHolder<T : ViewDataBinding?>(val binding: T) : RecyclerView.ViewHolder(
    binding!!.root
) {
    var mOnItemClickListener: OnItemClickListener? = null
    var mOnItemLongClickListener: OnItemLongClickListener? = null

    init {
        binding!!.root.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(getBindingAdapterPosition())
                }
            }
        })
        binding.root.setOnLongClickListener(object : OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener!!.onItemLongClick(getBindingAdapterPosition())
                }
                return true
            }
        })
    }

    val context: Context
        get() = binding!!.root.context

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener?) {
        this.mOnItemLongClickListener = onItemLongClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }
}
