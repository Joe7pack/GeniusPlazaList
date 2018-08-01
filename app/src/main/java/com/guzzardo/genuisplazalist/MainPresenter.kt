package com.guzzardo.genuisplazalist

import android.content.Context

class MainPresenter(context: Context) : Presenter<MvpView> {

    override val context: Context get() { return context}

    override var employeeList: MutableList<Repository.Data>?  = null //{ return employeeList}
        get()  { return model.employeeList }

    override val model : Repository // get() {return Repository(context) }

    private var mvpView: MvpView? = null

    var dataLoaded: Boolean = false
        get()  { return model.dataLoaded }

    init {
        model = Repository(context)
    }

    override fun attachView(view: MvpView) {
        this.mvpView = view
    }

    override fun detachView() {
        this.mvpView = null
    }

    override fun setEmployeeList() {
        model.setEmployeeList()
    }
}

