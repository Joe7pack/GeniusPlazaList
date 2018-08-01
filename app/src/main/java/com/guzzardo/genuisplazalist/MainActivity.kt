package com.guzzardo.genuisplazalist

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity(), MvpView, View.OnClickListener {

    private var presenter: MainPresenter? = null
    private var mListView: ListView? = null
    private var mEmployeeSelected: Int = 0
    private var myApplication: MyApplication? = null
    private var mContext: Context? = null
    private lateinit var  myIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListView = findViewById<View>(R.id.list) as ListView
        mListView!!.isClickable = true

        myApplication = this.application as MyApplication
        myApplication!!.setListView(mListView)
        myApplication!!.context = this
        mContext = this

/*
        val toolbar = findViewById<View>(R.id.tool_bar) as Toolbar
        setSupportActionBar(toolbar)
        myApplication = this.application as MyApplication
        val contextCompat = myApplication!!.context!!
        toolbar.setNavigationIcon(ContextCompat.getDrawable(contextCompat, R.drawable.plus_arrow))
*/
        val toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        //setSupportActionBar(findViewById(R.id.my_toolbar))


        mListView!!.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            val o = mListView!!.getItemAtPosition(position)
            mEmployeeSelected = position
            myApplication!!.employeeId = Integer.toString(position)
            showEmployeeDetail()
        }

        presenter = MainPresenter(this)
        employeeSmallIcons = HashMap()
        inflater = this.layoutInflater

        (this.application as MyApplication).presenter = presenter
        DownloadFilesTask(this).execute("one", "two", "three")
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.add_contact_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id=item.itemId


        if (id == R.id.action_add_contact) {
            addNewEmployee()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewEmployee() {
        myIntent = Intent(this, AddEmployeeActivity::class.java)
        //i.putExtra(EmployeeDetailActivity.ID, Integer.toString(mEmployeeSelected))
        myIntent.putExtra("keep", true)
        startActivity(myIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //finishActivity(requestCode)
    }


    private fun showEmployeeDetail() {
        val i = Intent(this, EmployeeDetailActivity::class.java)
        i.putExtra(EmployeeDetailActivity.ID, Integer.toString(mEmployeeSelected))
        startActivity(i)
    }

    internal fun loadImageFromWebOperations(url: String?): Drawable? {
        try {
            val `is` = URL(url).content as InputStream
            val bitmapDrawable = Drawable.createFromStream(`is`, "src name")
            val bitmap = bitmapDrawable.current
            return bitmapDrawable
        } catch (e: Exception) {
            println("error in loadImageFromWebOperations: $e")
            return mContext!!.resources.getDrawable(R.drawable.user_icon_small)
        }
    }

    private inner class DownloadFilesTask(activity: MainActivity) : AsyncTask<String?, Int?, Void?>() {
        private val progressBar = findViewById(R.id.progressBar1) as ProgressBar
        private val progressText = findViewById(R.id.progressText1) as TextView
        private val progressLayout = findViewById(R.id.progressLayout1) as ViewGroup

        //private val dialog: ProgressBar
        private var employeeList: List<Repository.Data> ? = null
            get()  =  presenter!!.employeeList

        override fun onPreExecute() {
            progressBar.setProgress(10, true)
        }

        override fun doInBackground(vararg params: String?): Void? {
            for (i in 0..29) {
                try {
                    if (presenter!!.dataLoaded) {
                        employeeList = presenter!!.employeeList
                        if (listViewLoaded) {
                            return null
                        }
                        loadListView(employeeList!!)
                    }
                    progressBar.incrementProgressBy(20);
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    Thread.interrupted()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            // do UI work here
            mListView!!.adapter = myApplication!!.getmAdapter()
            progressLayout.visibility = View.GONE
        }

        //load the small icon bitmaps...
        private fun loadListView(employeeList: List<Repository.Data>) {
            val listNames = arrayOfNulls<String>(employeeList.size)
            val listCompany = arrayOfNulls<String>(employeeList.size)
            val listImages = arrayOfNulls<Int>(employeeList.size)
            val listFavorites = arrayOfNulls<String>(employeeList.size)
            val customList = ArrayList<View>()
            val bitmapMap = HashMap<String?, Bitmap?>()
            val imageId = resources.getIdentifier("user_icon_small", "drawable", packageName)

            if (employeeSmallIcons!!.size == 0) {
                for (x in employeeList.indices) {
                    val employee = employeeList[x]
                    listNames[x] = employee.first_name + " " + employee.last_name
                    //listCompany[x] = employee.companyName

                    listImages[x] = imageId
                    //listFavorites[x] = employee.isFavorite
                    myApplication!!.setEmployeeIdByPosition(Integer.toString(x), employee.id)

                    val smallIconUrl = employee.avatar //.smallImageURL
                    val contactSmallIcon = loadImageFromWebOperations(smallIconUrl)
                    employeeSmallIcons!![employee.id] = contactSmallIcon
                    val bitmap = (contactSmallIcon as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val bitmapdata = stream.toByteArray()
                    val bitmap2 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size)
                    bitmapMap[employee.id] = bitmap2
                }
            }

            for (x in employeeList.indices) {
                val rowView = inflater!!.inflate(R.layout.list_single, null, false)
                customList.add(rowView)
                val employeeId = myApplication!!.getEmployeeIdByPosition(Integer.toString(x))
                val contactSmallIcon = employeeSmallIcons!![employeeId]
                val imageView = rowView.findViewById<View>(R.id.img) as ImageView
                val bm = (contactSmallIcon as BitmapDrawable).bitmap
                imageView.setImageBitmap(bm)
            }

            myApplication!!.setCustomList(customList)
            myApplication!!.setSmallIconBitmapMap(bitmapMap)
            val adapter = CustomList(myApplication!!, this@MainActivity, listNames, listCompany, listImages, listFavorites)
            myApplication!!.setListViewAdapter(adapter)
            listViewLoaded = true
        }
    }

    fun loadListView2(employeeList: List<Repository.Data>?) {
        val listNames = arrayOfNulls<String>(employeeList!!.size)
        val listCompany = arrayOfNulls<String>(employeeList.size)
        val listImages = arrayOfNulls<Int>(employeeList.size)
        val listFavorites = arrayOfNulls<String>(employeeList.size)
        val customList = ArrayList<View>()

        val imageId = resources.getIdentifier("user_icon_small", "drawable", packageName)

        for (x in employeeList!!.indices) {
            val employee = employeeList[x]

            listNames[x] = employee.first_name + " " + employee.last_name
            //listCompany[x] = employee.companyName

            listImages[x] = imageId
            //listFavorites[x] = employee.isFavorite
            myApplication!!.setEmployeeIdByPosition(Integer.toString(x), employee.id)

            val rowView = inflater!!.inflate(R.layout.list_single, null, false)
            customList.add(rowView)
        }
        myApplication!!.setCustomList(customList)
        val adapter = CustomList(myApplication!!, this@MainActivity, listNames, listCompany, listImages, listFavorites)
        myApplication!!.setListViewAdapter(adapter)
        mListView!!.adapter = adapter

        myIntent = Intent(this, AddEmployeeActivity::class.java)
        myIntent.putExtra("keep", false)
        startActivity(myIntent)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.name)
            showEmployeeDetail()
    }

    override fun onResume() {
        super.onResume()
        //mListView.setAdapter(myApplication.getmAdapter());
    }

    companion object {
        private var listViewLoaded: Boolean = false
        private var employeeSmallIcons: MutableMap<String?, Drawable?>? = null
        private var inflater: LayoutInflater? = null
    }

}
