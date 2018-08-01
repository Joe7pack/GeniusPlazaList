package com.guzzardo.genuisplazalist

import android.graphics.Bitmap
import android.widget.ArrayAdapter
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class CustomList(private val mMyApplication: MyApplication, context: Activity,
                 private val names: Array<String?>, private val company: Array<String?>, private val imageId: Array<Int?>,
                 private val favorite: Array<String?>) : ArrayAdapter<String>(context, R.layout.list_single, names) {
    private val presenter: MainPresenter?
    private var mOtherContactStartingPosition = -1

    init {
        presenter = mMyApplication.presenter
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = mMyApplication.getCustomList(position)
        val txtContactType = rowView.findViewById<View>(R.id.contact_type) as TextView
        txtContactType.text = "FAVORITE CONTACTS"
        val imageViewFavorite = rowView.findViewById<View>(R.id.favorite_contact) as ImageView
        if (favorite[position] == "true") {
            imageViewFavorite.setImageResource(R.drawable.favorite_icon_true)
        } else {
            imageViewFavorite.visibility = View.INVISIBLE
            val parms = imageViewFavorite.layoutParams as TableRow.LayoutParams
            parms.width = 10
            txtContactType.text = "OTHER CONTACTS"
            if (mOtherContactStartingPosition < 0 ) {
                mOtherContactStartingPosition = position
            }
        }

        if ((position == 0) or (position == mOtherContactStartingPosition)) {
            txtContactType.visibility = View.VISIBLE
        } else {
            txtContactType.visibility = View.INVISIBLE
            val parms = txtContactType.layoutParams as TableLayout.LayoutParams
            parms.height = 0
            txtContactType.layoutParams = parms
        }

        val txtName = rowView.findViewById<View>(R.id.contact_name) as TextView
        val txtCompany = rowView.findViewById<View>(R.id.contact_company) as TextView
        val imageView = rowView.findViewById<View>(R.id.img) as ImageView
        txtName.text = names[position]
        txtCompany.text = company[position]
        val employeeId = mMyApplication.getEmployeeIdByPosition(Integer.toString(position))
        val bitmap = mMyApplication.getSmallIconBitmapMap(employeeId)
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 40, 40, false))
        return rowView
    }
}
