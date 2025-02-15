package com.finddreams.smarttablerecycleview.model

import androidx.annotation.StringRes
import com.finddreams.smarttablerecycleview.R

enum class TableSort {
    ASC,//升序
    DESC,//降序
    NONE
}
data class TableDataSet(
    @StringRes
    val firstColumHeaderNameResId: Int= R.string.mingcheng,//第一列的标题资源id 在compose 中用 stringResource 才支持简繁体
    val firstColumWith: Int=112,//单位dp
    val itemHeight: Int=55,//单位dp
    val headerTextSize: Int =13,//单位sp
    val isShowLoadMore:Boolean=false,
    val items: List<TableItemsEntity>? =null,
    val headers:ArrayList<TableHeaderEntity>,
)
