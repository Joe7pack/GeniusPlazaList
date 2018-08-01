package com.guzzardo.genuisplazalist

import android.content.Context

interface Presenter<V> {
    val context: Context //get() = context
    var employeeList: MutableList<Repository.Data>?
    val model: Repository
    fun attachView(view: V)
    fun detachView()
    fun setEmployeeList()
}