package com.guzzardo.genuisplazalist

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat

class EmployeeDetailActivity : AppCompatActivity() {

    private var presenter: MainPresenter? = null
    private var contactImage: ImageView? = null

    private var myApplication: MyApplication? = null

    private val mCallback = MyCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        mResources = resources

        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_detail)
        val toolbar = findViewById<View>(R.id.detail_tool_bar) as Toolbar
        setSupportActionBar(toolbar)

        myApplication = this.application as MyApplication

        val contextCompat = myApplication!!.context!!
        toolbar.setNavigationIcon(ContextCompat.getDrawable( contextCompat, R.drawable.back_arrow))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val name = this.findViewById<TextView>(R.id.name)
        contactImage = this.findViewById(R.id.contact_image)
        val company = this.findViewById<TextView>(R.id.company)

        val phone = this.findViewById<TextView>(R.id.phone)
        val phoneType = this.findViewById<TextView>(R.id.phoneType)
        val phoneLiteral = findViewById<TextView>(R.id.phoneLiteral)

        val phone2 = this.findViewById<TextView>(R.id.phone2)
        val phoneType2 = this.findViewById<TextView>(R.id.phoneType2)
        val phoneLiteral2 = findViewById<TextView>(R.id.phoneLiteral2)

        val phone3 = this.findViewById<TextView>(R.id.phone3)
        val phoneType3 = this.findViewById<TextView>(R.id.phoneType3)
        val phoneLiteral3 = findViewById<TextView>(R.id.phoneLiteral3)

        val address1 = findViewById<TextView>(R.id.addressLine1)
        val address2 = findViewById<TextView>(R.id.addressLine2)
        val addressLiteral = findViewById<TextView>(R.id.addressLiteral)

        val birthday = findViewById<TextView>(R.id.birthday)
        val birthdayLiteral = findViewById<TextView>(R.id.birthdayLiteral)

        val email = findViewById<TextView>(R.id.email)
        val emailLiteral = findViewById<TextView>(R.id.emailLiteral)

        val positionSelected = intent.getStringExtra(ID)

        presenter = myApplication!!.presenter
        val employeeId = myApplication!!.getEmployeeIdByPosition(positionSelected)
        val employeeData = presenter!!.model.getEmployee(employeeId)
        contactImageURL = employeeData!!.avatar // .largeImageURL

        //these parms are in pixels
        val imageHeight = contactImage!!.height

        var parms = contactImage!!.layoutParams as ConstraintLayout.LayoutParams

        parms = name.layoutParams as ConstraintLayout.LayoutParams
        val nameTopMargin = parms.topMargin
        name.text = employeeData.first_name + " " + employeeData.last_name

        parms = company.layoutParams as ConstraintLayout.LayoutParams
        val companyTopMargin = parms.topMargin
        //company.text = employeeData.companyName

        DownloadContactImageTask(this).execute("one", "two", "three")

        /*
        parms = phone.layoutParams as ConstraintLayout.LayoutParams
        var phoneTopMargin = parms.topMargin
        val verticalIncrement = 70
        val verticalIncrement2 = 30
        val verticalIncrement3 = 50
        parms = phoneType.layoutParams as ConstraintLayout.LayoutParams
        var phoneTypeTopMargin = parms.topMargin
        parms = phoneLiteral.layoutParams as ConstraintLayout.LayoutParams
        var phoneLiteralTopMargin = parms.topMargin

        if (employeeData.phone!!.home != null && employeeData.phone!!.home!!.isNotEmpty()) {
            phone.text = employeeData.phone!!.home
            phoneType.text = "Home"
            phone.visibility = View.VISIBLE
            phoneType.visibility = View.VISIBLE
            phoneLiteral.text = "PHONE"
            phoneLiteral.visibility = View.VISIBLE
            phoneTopMargin += verticalIncrement
            phoneTypeTopMargin += verticalIncrement
            phoneLiteralTopMargin += verticalIncrement
        }

        if (employeeData.phone!!.work != null && employeeData.phone!!.work!!.isNotEmpty()) {
            phone2.text = employeeData.phone!!.work
            phoneType2.text = "Work"
            phone2.visibility = View.VISIBLE
            phoneType2.visibility = View.VISIBLE
            phoneLiteral2.text = "PHONE"
            phoneLiteral2.visibility = View.VISIBLE

            parms = phone2.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneTopMargin
            parms = phoneType2.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneTypeTopMargin
            parms = phoneLiteral2.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneLiteralTopMargin

            phoneTopMargin += verticalIncrement
            phoneTypeTopMargin += verticalIncrement
            phoneLiteralTopMargin += verticalIncrement
        }

        if (employeeData.phone!!.mobile != null && employeeData.phone!!.mobile!!.isNotEmpty()) {
            phone3.text = employeeData.phone!!.mobile
            phoneType3.text = "Mobile"
            phone3.visibility = View.VISIBLE
            phoneType3.visibility = View.VISIBLE
            phoneLiteral3.text = "PHONE"
            phoneLiteral3.visibility = View.VISIBLE

            parms = phone3.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneTopMargin
            parms = phoneType3.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneTypeTopMargin
            parms = phoneLiteral3.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneLiteralTopMargin

            phoneTopMargin += verticalIncrement
        }

        if (employeeData.address!!.street != null && employeeData.address!!.street!!.isNotEmpty()) {
            address1.text = employeeData.address!!.street
            addressLiteral.text = "ADDRESS"

            val addressLine2 = employeeData.address!!.city!! + ", " + employeeData.address!!.state!! + " " + employeeData.address!!.zipCode!! + ", " + employeeData.address!!.country!!
            address2.text = addressLine2
            parms = addressLiteral.layoutParams as ConstraintLayout.LayoutParams
            parms.topMargin = phoneTopMargin
            parms = address1.layoutParams as ConstraintLayout.LayoutParams
            phoneTopMargin += verticalIncrement2
            parms.topMargin = phoneTopMargin
            parms = address2.layoutParams as ConstraintLayout.LayoutParams
            phoneTopMargin += verticalIncrement2
            parms.topMargin = phoneTopMargin
        }

        if (employeeData.birthdate != null && employeeData.birthdate!!.length > 0) {
            try {
                val fmt = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate = fmt.parse(employeeData.birthdate)
                val fmtOut = SimpleDateFormat("MMMM d, yyyy")
                val formattedBirthday = fmtOut.format(formattedDate)
                birthday.text = formattedBirthday
            } catch (e: Exception) {
                birthday.text = employeeData.birthdate
            }

            birthdayLiteral.text = "BIRTHDATE"

            parms = birthdayLiteral.layoutParams as ConstraintLayout.LayoutParams
            phoneTopMargin += verticalIncrement3
            parms.topMargin = phoneTopMargin
            parms = birthday.layoutParams as ConstraintLayout.LayoutParams
            phoneTopMargin += verticalIncrement2
            parms.topMargin = phoneTopMargin
        }

        if (employeeData.emailAddress != null && employeeData.emailAddress!!.isNotEmpty()) {
            email.text = employeeData.emailAddress
            emailLiteral.text = "EMAIL"

            parms = emailLiteral.layoutParams as ConstraintLayout.LayoutParams
            phoneTopMargin += verticalIncrement3
            parms.topMargin = phoneTopMargin
            parms = email.layoutParams as ConstraintLayout.LayoutParams
            phoneTopMargin += verticalIncrement2
            parms.topMargin = phoneTopMargin
        }
        */
    }

    override fun onResume() {
        super.onResume()
        try {
            contactImage!!.setImageDrawable(contactImageDrawable)
        } catch (e: Exception) {
            Log.e(TAG, "OnResume", e)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)

        val employeeSelected = myApplication!!.employeeId
        val employeeId = myApplication!!.getEmployeeIdByPosition(employeeSelected)
        /*
        val isFavorite = presenter!!.model.getEmployee(employeeId)!!.isFavorite
        if (isFavorite == "true") {
            menu.getItem(0).icon = resources.getDrawable(R.drawable.favorite_icon_true)
            mIsFavorite = true
        } else {
            menu.getItem(0).icon = resources.getDrawable(R.drawable.favorite_icon_false_outline)
            mIsFavorite = false
        }
        */
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_favorite) {
            val employeeSelected = myApplication!!.employeeId
            val employeeId = myApplication!!.getEmployeeIdByPosition(employeeSelected)
            /*
            if (mIsFavorite) {
                presenter!!.model.getEmployee(employeeId)!!.isFavorite = "false"
                item.icon = resources.getDrawable(R.drawable.favorite_icon_false_outline)
            } else {
                presenter!!.model.getEmployee(employeeId)!!.isFavorite = "true"
                item.icon = resources.getDrawable(R.drawable.favorite_icon_true)
            }
            mIsFavorite = !mIsFavorite
            */
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        presenter!!.model.setEmployeeList()
        val mainActivity = myApplication!!.context as MainActivity
        //FIXME
        //mainActivity.loadListView2(presenter!!.model.employeeList)
        super.onBackPressed()
    }

    private inner class DownloadContactImageTask(activity: EmployeeDetailActivity) : AsyncTask<String, Void, Void>() {

        private val mCallback = MyCallback()

        fun handleMessage(message: Message) {
            println("handle message reached")
        }

        init {
            Log.i(TAG, "DownloadContactImage")
        }

        override fun onPreExecute() {
            //dialog.setMessage("Loading data, please wait.");
            //dialog.show();
            val handlerThread = HandlerThread("BackgroundThread")
            handlerThread.start()
            contactImageDrawable = null
            mForegroundHandler = Handler(handlerThread.looper, mCallback)
            mBackgroundHandler = Handler(mCallback)
            mBackgroundHandler!!.obtainMessage(START_DOWNLOAD_MSG, this).sendToTarget()
            Log.i(TAG, "onPreExecute called")
        }

        override fun doInBackground(vararg params: String): Void? {
            for (i in 0..9) {
                try {
                    while (contactImageDrawable == null) {
                        contactImageDrawable = LoadImageFromWebOperations(contactImageURL)
                        Log.i(TAG, "doInBackground called")
                        if (contactImageDrawable != null) {
                            mBackgroundHandler!!.obtainMessage(DOWNLOAD_COMPLETED, this).sendToTarget()
                            return null
                        }
                    }
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    Thread.interrupted()
                }

            }
            return null
        }
    }

    internal inner class MyCallback : Handler.Callback {
        init {
            println("constructor called!")
        }

        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                START_DOWNLOAD_MSG -> {
                }
                UPDATE_PROGRESS_MSG -> {
                }
                TOGGLE_START_BTN_MSG -> {
                }
                DOWNLOAD_COMPLETED -> contactImage!!.setImageDrawable(contactImageDrawable)
            }
            return true
        }
    }

    companion object {
        private val packageName = "com.guzzardo.genuisplazalist"

        private var mIsFavorite: Boolean = false
        private var contactImageDrawable: Drawable? = null
        private var contactImageURL: String? = null
        private var mResources: Resources? = null

        private val START_DOWNLOAD_MSG = 1001
        private val UPDATE_PROGRESS_MSG = 2002
        private val TOGGLE_START_BTN_MSG = 3003
        private val DOWNLOAD_COMPLETED = 3004
        private val ALL_DONE = 3005

        private var mBackgroundHandler: Handler? = null
        private var mForegroundHandler: Handler? = null

        val ID = "$packageName.EmployeeDetailActivity.ID"
        private val TAG = "EmployeeDetailActivity"

        fun LoadImageFromWebOperations(url: String?): Drawable {
            try {
                val `is` = URL(url).content as InputStream
                val bitmapDrawable = Drawable.createFromStream(`is`, "src name")
                val bitmap = bitmapDrawable.current
                return bitmapDrawable
            } catch (e: Exception) {
                Log.e(TAG, "LoadImageFromWeb", e)
                return mResources!!.getDrawable(R.drawable.user_icon_large)
            }
        }
    }
}
