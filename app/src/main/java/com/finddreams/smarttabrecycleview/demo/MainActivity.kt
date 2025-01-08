package com.finddreams.smarttabrecycleview.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.finddreams.smarttabrecycleview.model.TabViewItemsEntity
import com.finddreams.smarttabrecycleview.model.TableViewChildItemEntity
import com.finddreams.smarttabrecycleview.model.TableViewDataSet
import com.finddreams.smarttabrecycleview.model.TableViewHeaderEntity
import com.finddreams.smarttabrecycleview.model.TableViewSort
import com.finddreams.smarttabrecycleview.recycleview.SmartTableRecycleView

class MainActivity : AppCompatActivity() {
    val stockHeaders = listOf(
        "现价",
        "涨跌幅",
        "涨跌额",
        "最高",
        "最低",
        "成交量",
        "成交额",
        "换手率",
        "市盈率", 
        "市值",   
        "流通股", 
        "总股本"  
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val cutout = insets.displayCutout
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            if (cutout != null) {
                v.setPadding(
                    cutout.safeInsetLeft,
                    systemBars.top,
                    cutout.safeInsetRight,
                    cutout.safeInsetBottom
                )
            }
            insets
        }

        initSmartRefreshView()
    }

    private fun initSmartRefreshView() {
        val smartTableRecycleView =
            findViewById<SmartTableRecycleView>(R.id.smart_table_recycle_view)
        val cellWidthList = arrayListOf<TableViewHeaderEntity>()
        stockHeaders.forEachIndexed { index, header ->
            cellWidthList.add(
                TableViewHeaderEntity(
                    title = header,
                    width = 75, // 可以根据需要调整宽度
                    asc = if (header == "涨跌幅") TableViewSort.DESC else TableViewSort.NONE // 默认"涨跌幅"排序
                ),
            )
        }
        smartTableRecycleView.setTabDataSet(getDemoTableViewDataSet(cellWidthList), onItemContainerClick = {
            Toast.makeText(this, "点击到了item ${it.firstColumName}", Toast.LENGTH_SHORT).show()
        },onHeaderItemClick={
            Toast.makeText(this, "点击到了header 需要进行排序${it.title} ${it.asc}", Toast.LENGTH_SHORT).show()
        })

    }
}
fun getDemoTableViewDataSet(
    headerList:ArrayList<TableViewHeaderEntity>,
    itemSize: Int=40
): TableViewDataSet {
    val tabViewListEntity = arrayListOf<TabViewItemsEntity>()

    repeat(itemSize) { rowIndex ->
        val tableViewItemEntities = arrayListOf<TableViewChildItemEntity>()

        headerList.forEachIndexed { index,header ->
            val cellContent = when (header.title) {
                "现价" -> "100.5" // 模拟现价
                "涨跌幅" -> if (rowIndex % 2 == 0) "+1.2%" else "-1.2%" // 模拟涨跌幅，偶数行涨，奇数行跌
                "涨跌额" -> if (rowIndex % 2 == 0) "1.20" else "1.20" // 模拟涨跌额
                "最高" -> "101.0" // 模拟最高价
                "最低" -> "99.5" // 模拟最低价
                "成交量" -> if (rowIndex % 2 == 0) "1.2M" else "1.0M" // 模拟成交量（假设负值表示减少的量）
                "成交额" -> "1.5B" // 模拟成交额
                "换手率" -> if (rowIndex % 2 == 0) "3.2%" else "3.2%" // 模拟换手率
                else -> "--" // 其他未匹配的列
            }

            // 设置颜色逻辑
            val color = when (index) {
                0 -> R.color.color_red // 涨
                1 -> R.color.color_green // 跌
                else -> null // 默认色
            }

            // 构建单元格
            val tabViewItem = TableViewChildItemEntity(
                value = cellContent,
                color = color
            )

            tableViewItemEntities.add(tabViewItem)
        }

        val tabViewList = TabViewItemsEntity(
            firstColumName = "腾讯控股 $rowIndex",
            firstColumValue = "00700.HK",
            childItems = tableViewItemEntities
        )
        tabViewListEntity.add(tabViewList)
    }

    val tableViewDataSet = getTableViewDataSet(
        items = tabViewListEntity,
        headers = headerList,
    )
    return tableViewDataSet
}
private fun getTableViewDataSet(
    items: ArrayList<TabViewItemsEntity>,
    headers: ArrayList<TableViewHeaderEntity>
): TableViewDataSet {
    val tableViewDataSet = TableViewDataSet(
        itemHeight = 55,
        items = items,
        headers = headers,
    )
    return tableViewDataSet
}