package com.finddreams.smarttabrecycleview.model

import androidx.annotation.StringRes
import com.finddreams.smarttabrecycleview.R

enum class TableViewSort {
    ASC,//升序
    DESC,//降序
    NONE
}
data class TableViewDataSet(
    @StringRes
    val firstColumHeaderNameResId: Int= R.string.mingcheng,//第一列的标题资源id 在compose 中用 stringResource 才支持简繁体
    val firstColumWith: Int=112,
    val itemHeight: Int=55,
    val headerTextSize: Int =13,//单位sp
    val isShowLoadMore:Boolean=false,
    val items: List<TabViewItemsEntity>? =null,
    val headers:ArrayList<TableViewHeaderEntity>,
) {
}
