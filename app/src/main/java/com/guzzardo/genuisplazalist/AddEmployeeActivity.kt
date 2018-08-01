package com.guzzardo.genuisplazalist

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class AddEmployeeActivity : AppCompatActivity() {
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText

    // Create an anonymous implementation of OnClickListener
    private val mBackIconPressed=View.OnClickListener { onBackPressed() }

    // Create an anonymous implementation of OnClickListener
    private val mAddContactListener=View.OnClickListener {
        firstNameText=firstName.text.toString()
        lastNameText=lastName.text.toString()
        println("got to here!")
        if (firstNameText.trim { it <= ' ' }.isEmpty() || lastNameText.trim { it <= ' ' }.isEmpty()) {
            val result="Please enter a first and a last name"
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show()
            return@OnClickListener
        }
        addNewContact()
    }

    // Create an anonymous implementation of OnClickListener
    private val mCancelAddContactListener=View.OnClickListener { onBackPressed() }

    override fun onCreate(savedInstanceState: Bundle?) {
        mResources=resources
        myApplication=this.application as MyApplication
        mContext=applicationContext
        super.onCreate(savedInstanceState)

        val intent=this.intent
        val keep=intent.extras!!.getBoolean("keep")
        if (!keep) {
            this.finish()
        }

        setContentView(R.layout.add_employee_detail)
        constraintLayout=findViewById<View>(R.id.add_employee_layout) as ConstraintLayout
        presenter=(this.application as MyApplication).presenter

        val toolbar=findViewById<View>(R.id.add_employee_tool_bar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        toolbar.setNavigationOnClickListener(mBackIconPressed)

        // Capture Add button from layout
        val addButton=findViewById<View>(R.id.add_employee_button) as Button
        // Register the onClick listener with the implementation above
        addButton.setOnClickListener(mAddContactListener)

        // Capture Cancel button from layout
        val cancelButton=findViewById<View>(R.id.cancel_add_employee_button) as Button
        // Register the onClick listener with the implementation above
        cancelButton.setOnClickListener(mCancelAddContactListener)

        firstName=this.findViewById(R.id.edit_first_name)
        lastName=this.findViewById(R.id.edit_last_name)
    }

    private fun addNewContact() {
        SendPostRequest().execute()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val keep=intent.extras!!.getBoolean("keep")
        if (!keep) this.finish()
    }

    private class SendPostRequest : AsyncTask<String, String, String>() {

        override fun onPreExecute() {}

        override fun doInBackground(vararg arg0: String): String {
            try {
                val url=URL("https://reqres.in/api/users") // here is your URL path
                val postDataParams=JSONObject()
                postDataParams.put("first_name", firstNameText)
                postDataParams.put("last_name", lastNameText)
                postDataParams.put("avatar", "https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg")
                Log.e("params", postDataParams.toString())

                val conn=url.openConnection() as HttpURLConnection
                conn.readTimeout=15000
                conn.connectTimeout=15000
                conn.requestMethod="POST"
                conn.doInput=true
                conn.doOutput=true

                val os=conn.outputStream
                val writer=BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(getPostDataString(postDataParams))

                writer.flush()
                writer.close()
                os.close()

                val responseCode=conn.responseCode

                if (responseCode==HttpsURLConnection.HTTP_CREATED) {
                    val `in`=BufferedReader(InputStreamReader(conn.inputStream))

                    val sb=StringBuffer("")
                    //var line=""

                    while (true) {
                        val line = `in`.readLine()
                        if (line!= null)
                            sb.append(line)
                        else
                            break
                    }

                    `in`.close()
                    return sb.toString()
                } else {
                    val sb=StringBuffer("false : $responseCode")
                    return String(sb)
                }
            } catch (e: Exception) {
                val sb=StringBuffer("Exception: $e.message")
                return String(sb)
            }

        }

        override fun onPostExecute(result: String) {
            //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            presenter!!.model.setEmployeeList()
            val mainActivity=myApplication!!.context as MainActivity?
            mainActivity!!.loadListView2(presenter!!.model.employeeList)
            done()
        }
    }

    companion object {

        private var mResources: Resources?=null
        internal var mContext: Context?=null
        lateinit internal var firstNameText: String
        lateinit internal var lastNameText: String
        private var myApplication: MyApplication?=null
        private var presenter: MainPresenter?=null
        internal var constraintLayout: ConstraintLayout?=null

        fun done() {
            println("add done")
        }

        @Throws(Exception::class)
        fun getPostDataString(params: JSONObject): String {
            val result=StringBuilder()
            var first=true
            val itr=params.keys()

            while (itr.hasNext()) {
                val key=itr.next()
                val value=params.get(key)
                if (first) first=false
                else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }
            return result.toString()
        }
    }
}
