package com.finddreams.smarttabrecycleview.model

import android.graphics.Color


data class TabViewItemsEntity(
    val firstColumName:String,
    val firstColumTextSize: Int =15,
    val stockCode:String?=null,
    val firstColumValue:String?=null,
    val bgColor: Color? = null,//背景颜色
    val sortValue:Float?=null,//排序的值
    val childItems:List<TableViewChildItemEntity>,
) {
}
data class TableViewChildItemEntity(
    val value: String,
    val color: Int? = null,
    val textSize: Float = 15f,
) {

}
data class TableViewHeaderEntity(
    val title: String,
    val width: Int = 60,
    var asc: TableViewSort? = null,//为null 则不能排序
    var sortType:Int = 0,
    val headerTextSize: Int? =null,//单个头部文字大小
    var isCurrentSortSelected: Boolean =false,
) {

}