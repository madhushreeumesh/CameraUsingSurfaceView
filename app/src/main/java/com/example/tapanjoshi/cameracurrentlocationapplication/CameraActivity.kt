package com.example.tapanjoshi.cameracurrentlocationapplication

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import de.hdodenhof.circleimageview.CircleImageView




class CameraActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        var path: String = intent.getStringExtra("BitmapImagePath")
        Log.d("myTag", intent.getStringExtra("BitmapImagePath"))
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }

        /* System Date/Time */

        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd/MM/yyyy h:mm:ss")
        val dateString = sdf.format(date)
        tv_date_txt.setText(dateString)

        /* To Fetch Image from SurfaceViewActivity */
      //  val pic = findViewById(R.id.iv) as de.hdodenhof.circleimageview.CircleImageView
        iv.setImageURI(Uri.parse(path))

        /* To Fetch Email And Paswsword from MainActivity */

        val name = intent.getStringExtra("key")
        tv_email_txt.setText(name)
        val id = intent.getStringExtra("key1")
        tv_password_txt.setText(id)
    }


    /* To Get Current Location Latitude/Longitude */

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            tv_lat_txt.text = location.longitude.toString()
            tv_long_txt.text = location.latitude.toString()
            var Url = "http://maps.google.com/maps/api/staticmap?center=" + location.latitude + "," + location.longitude + "&zoom=15&size=550x550&sensor=false&markers=color:red|label:|" + location.latitude + "," + location.longitude
            var imageView = findViewById<View>(R.id.map) as ImageView
            Glide.with(applicationContext).load(Url).into(imageView)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onBackPressed() {
        super.onBackPressed()
        shareScreen()
    }

    /* To Take ScreenShot Of Activity */

    fun takeScreenShot(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val b1 = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top


        val width = activity.windowManager.defaultDisplay.width
        val height = activity.windowManager.defaultDisplay.height


        val b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return b
    }

    /* To Save The ScreenShot */

    private fun shareScreen() {
        try {
            val cacheDir = File(
                    Environment.getExternalStorageDirectory(),
                    "devdeeds")

            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)

            val path = File(
                    Environment.getExternalStorageDirectory(),
                    "devdeeds").toString() + "/Image-$n.jpg"

            savePic(takeScreenShot(this), path)

            Toast.makeText(applicationContext, "Screenshot Saved", Toast.LENGTH_SHORT).show()


        } catch (ignored: NullPointerException) {
            ignored.printStackTrace()
        }
    }

    fun savePic(b: Bitmap, strFileName: String) {
        val fos: FileOutputStream
        try {

            fos = FileOutputStream(strFileName)
            b.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}



