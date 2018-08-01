package com.guzzardo.genuisplazalist

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.Collections
import java.util.HashMap

class Repository(context: Context) : EmployeeModel {

    private val employeeObject: JSONObject? = null
    private val firstName: String? = null
    private val lastName: String? = null
    private val phone: String? = null
    private val mContext = context

    //@delegate:companion
    override val dataLoaded : Boolean //{ return true }
        get() {  return GetDataList.dataLoaded  }

    override val employeeList: MutableList<Data>?  //{ return Repository.employeeList}
        get() = Repository.employeeList

    init {
        loadJSONData(mContext)
    }

    private fun loadJSONData(context : Context) {
        try {
            //JsonTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "https://s3.amazonaws.com/technical-challenge/v3/contacts.json")
            JsonTask(context).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR)
        } catch (e: Exception) {
            println("Error creating JSON Employee list: $e")
        }
    }

    override fun getEmployee(employeeId: String?): Data? {
        val employeeMap2 = employeeMap
        return if (employeeMap2!!.containsKey(employeeId)) {
            employeeMap2.get(employeeId)
        } else {
            null
        }
    }

    override fun setEmployeeList() {
        Collections.sort(employeeList)
        setEmployeeMap()
    }

    // load the Json list from AWS
    class JsonTask(context : Context) : AsyncTask<String?, String?, String?>() {
        private val mContext = context

        override fun doInBackground(vararg params: String?): String? {
            val queue = Volley.newRequestQueue(mContext)

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    Response.Listener { response ->
                        try {
                            setEmployeeList(response)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { Log.d("GetEmployeesFromURL", "That didn't work!") })

            // Add the request to the RequestQueue.
            queue.add(stringRequest)
            return null
        }
    }

    inner class UserList {
        internal var page: String? = null
        internal var per_page: String? = null
        internal var total: String? = null
        internal var total_pages: String? = null
        internal var data: List<Data>? = null
    }

    inner class Data : Comparable<Data> {
        internal var first_name: String? = null
        internal var last_name: String? = null
        internal var id: String? = null
        internal var avatar: String? = null
        //internal var full_name = first_name + " " + last_name

        override fun compareTo(otherEmployee: Data): Int {
            val thisNamePrefix = StringBuilder()
            val otherNamePrefix = StringBuilder()

            /*
            if (this.isFavorite == "true") {
                thisNamePrefix.insert(0, "a")
            } else {

                thisNamePrefix.insert(0, "b")
            }

            if (otherEmployee.isFavorite == "true") {
                otherNamePrefix.insert(0, "a")
            } else {

                otherNamePrefix.insert(0, "b")
            }
            */

            val valueHere = thisNamePrefix.toString() + this.first_name!!
            val valueThere = otherNamePrefix.toString() + otherEmployee.first_name!!
            return valueHere.compareTo(valueThere)
            return 0
        }

    }

    /*
    inner class Address {
        internal var street: String? = null
        internal var state: String? = null
        internal var city: String? = null
        internal var country: String? = null
        internal var zipCode: String? = null
    }

    inner class Phone {
        internal var work: String? = null
        internal var home: String? = null
        internal var mobile: String? = null
    }
    */

    companion object GetDataList {
        //private val url = "https://s3.amazonaws.com/technical-challenge/v3/contacts.json"
        private val url = "https://reqres.in/api/users"

        private var userList: UserList?=null

        private lateinit var employeeList: MutableList<Data>

        private var employeeMap: MutableMap<String?, Data?>? = null

        var dataLoaded = false
        init {
            false
        }

        public fun setEmployeeList(jsonData: String) = try {
            val collectionType = object : TypeToken<UserList>() {
            }.type
            //employeeList = Gson().fromJson<Object<UserList>>(jsonData, collectionType)
            userList = Gson().fromJson(jsonData, collectionType)

            employeeList = mutableListOf<Data>()

            for (data in userList?.data!!) {
                employeeList.add(data)
            }

            Collections.sort(employeeList)
            setEmployeeMap()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        private fun setEmployeeMap() {
            employeeMap = HashMap()

            for (i in employeeList!!)
                employeeMap!![i?.id] = i
            dataLoaded = true
        }
    }
}
