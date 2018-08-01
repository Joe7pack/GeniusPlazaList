package com.guzzardo.genuisplazalist

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ListView

import java.util.HashMap

class MyApplication : Application() {
    var presenter: MainPresenter? = null
    private val employeeIdsByPosition = HashMap<String, String?>()
    var employeeId: String? = null
    private var mListView: ListView? = null
    private var mAdapter: CustomList? = null
    var context: Context? = null
    private var mCustomList: List<View>? = null
    private var mSmallIconBitmapMap: Map<String?, Bitmap?>? = null

    private val mSmallIconBitmapList: List<Bitmap>? = null

    fun getSmallIconBitmapMap(employeeId: String?): Bitmap? {
        return mSmallIconBitmapMap!![employeeId]
    }

    fun setSmallIconBitmapMap(mSmallIconBitmapMap: Map<String?, Bitmap?>) {
        this.mSmallIconBitmapMap = mSmallIconBitmapMap
    }

    fun getEmployeeIdByPosition(position: String?): String? {
        return employeeIdsByPosition[position]
    }

    fun setEmployeeIdByPosition(position: String, employeeId: String?) {
        employeeIdsByPosition[position] = employeeId
    }

    fun setListView(listView: ListView?) {
        mListView = listView
    }

    fun setListViewAdapter(adapter: CustomList) {
        mAdapter = adapter
    }

    fun getmAdapter(): CustomList? {
        return mAdapter
    }

    fun setCustomList(customList: List<View>) {
        mCustomList = customList
    }

    fun getCustomList(position: Int): View {
        return mCustomList!![position]
    }
}
