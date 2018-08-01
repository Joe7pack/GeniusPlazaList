package com.guzzardo.genuisplazalist

interface EmployeeModel {
    val dataLoaded : Boolean //get() = dataLoaded
    val employeeList: MutableList<Repository.Data>? //get() = employeeList
    fun setEmployeeList()
    fun getEmployee(employeeId: String?): Repository.Data?

}
