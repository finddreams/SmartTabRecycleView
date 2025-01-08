package com.finddreams.smarttabrecycleview.recycleview;

import android.content.Context;
import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by lx on 17-11-20.
 */

public class BaseBindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private T binding;
    private ViewDataBinding bindingMoveChild;
    private ViewDataBinding bindingFirstColumChild;
     public OnItemClickListener onItemClickListener;
     public OnItemLongClickListener onItemLongClickListener;

    public BaseBindingViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null) {
                    onItemClickListener.onItemClick(getBindingAdapterPosition());
                }
            }
        });
        binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener!=null) {
                    onItemLongClickListener.onItemLongClick(getBindingAdapterPosition());
                }
                return true;
            }
        });
    }
    public void setBindingMoveChild(ViewDataBinding viewDataBinding){
        bindingMoveChild =viewDataBinding;
    }
    public void setBindingFirstColumChild(ViewDataBinding viewDataBinding){
        bindingFirstColumChild =viewDataBinding;
    }
    public T getBinding() {
        return binding;
    }
    public Context getContext() {
        return binding.getRoot().getContext();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public ViewDataBinding getBindingMoveChild() {
        return bindingMoveChild;
    }

    public ViewDataBinding getBindingFirstColumChild() {
        return bindingFirstColumChild;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
