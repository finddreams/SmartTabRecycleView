package com.finddreams.smarttabrecycleview.listener

interface OnScrollChangedListener {
    fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)
    fun onFling(velocityX:Int){}
    fun abort(){}
}