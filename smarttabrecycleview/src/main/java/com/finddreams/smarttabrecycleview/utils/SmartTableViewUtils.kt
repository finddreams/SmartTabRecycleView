package com.finddreams.smarttabrecycleview.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.finddreams.smarttabrecycleview.R
import com.finddreams.smarttabrecycleview.databinding.SmartTableviewRecycleviewHeaderItemBinding
import com.finddreams.smarttabrecycleview.model.TableViewHeaderEntity
import com.finddreams.smarttabrecycleview.model.TableViewSort


/**
 *设置左右滑动的header 支持三个顺序的
 */
fun  RecyclerView.setupScrollHeaderAdapter(
    mScrollHeaderList: ArrayList<TableViewHeaderEntity>,
    onItemContainerClick: (TableViewHeaderEntity) -> Unit = {},
): BindingAdapter =
    linear(orientation = RecyclerView.HORIZONTAL)
        .setup {
            addType<TableViewHeaderEntity>(R.layout.smart_tableview_recycleview_header_item)
            onBind {
                val binding = getBinding<SmartTableviewRecycleviewHeaderItemBinding>()
                val model = getModel<TableViewHeaderEntity>()
                binding.tvHeaderTitle.text = model.title
                if (model.headerTextSize != null) {
                    binding.tvHeaderTitle.setTextSizeDp(model.headerTextSize)
                }

                if (model.width != 0) {
                    binding.tvHeaderLayout.layoutParams.width = model.width.dp
                }

                if (modelPosition == itemCount - 1) {
                    binding.tvHeaderTitle.setPadding(0, 0, 12.dp, 0)
                } else {//不加这一句会导致切换排序以后 文字位置会变
                    binding.tvHeaderTitle.setPadding(0, 0, 0, 0)
                }

                if (model.asc != null) {
                    if (model.isCurrentSortSelected) {
                        binding.tvHeaderTitle.setRightDrawable(model.asc, context)
                    } else {
                        binding.tvHeaderTitle.setRightDrawable(model.asc, context)
                    }
                } else {
                    binding.tvHeaderTitle.setRightDrawableRes(null)
                }
            }
            R.id.tv_header_title.onClick {
                val model = getModel<TableViewHeaderEntity>()
                if (model.asc != null) {
                    var mSort = if (model.isCurrentSortSelected) {
                        switchThreeSortDir(model.asc!!)
                    } else {
                        switchThreeSortDir(model.asc!!)
                    }
                    for ((i, item) in mScrollHeaderList.withIndex()) {
                        item.isCurrentSortSelected = i == modelPosition
                        item.asc =
                            if (i == modelPosition) mSort else TableViewSort.NONE
                    }
                    onItemContainerClick.invoke(model)
                    notifyDataSetChanged()
                }
            }
        }

/**
 * 1 升序
 * -1 降序
 * @param sourceDir
 * @return
 */
fun switchThreeSortDir(sourceDir: TableViewSort): TableViewSort {
    var finalDir =TableViewSort.DESC
    finalDir = when (sourceDir) {
//        TableViewSort.DESC -> TableViewSort.NONE
        TableViewSort.NONE -> TableViewSort.ASC
        TableViewSort.ASC -> TableViewSort.DESC
        TableViewSort.DESC -> TableViewSort.ASC
    }
    return finalDir
}
val Number.dp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

fun TextView?.setTextSizeDp(dp: Int) {
    this?.apply {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat())
    }
}
fun TextView?.setRightDrawableRes(rightDrawable: Drawable?) {
    this?.apply {
        setTVRightDrawable(this@setRightDrawableRes, rightDrawable)
    }
}
fun TextView.setRightDrawable(sortDirValue: TableViewSort?, context: Context) {
    sortDirValue?.apply {
        setTVRightDrawable(this@setRightDrawable, getDirDrawableByMarketDefineSort(sortDirValue, context))
    }
}
fun setTVRightDrawable(textView: TextView, drawable: Drawable?) {
    textView.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        drawable,
        null
    )
}

fun getDirDrawableByMarketDefineSort(sortDirValue: TableViewSort, context: Context): Drawable? {
    return when (sortDirValue) {
        TableViewSort.ASC -> {
            ContextCompat.getDrawable(
                context,
                R.drawable.icon_paixu_up
            )
        }

        TableViewSort.DESC -> {
            ContextCompat.getDrawable(
                context,
                R.drawable.icon_paixu_down
            )
        }

        else -> {
            ContextCompat.getDrawable(
                context,
                R.drawable.icon_paixu_center
            )
        }
    }
}
