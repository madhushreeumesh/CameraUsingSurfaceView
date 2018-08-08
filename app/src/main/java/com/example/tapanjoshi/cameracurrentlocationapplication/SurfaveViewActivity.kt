package com.example.tapanjoshi.cameracurrentlocationapplication

import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class SurfaveViewActivity : AppCompatActivity(), SurfaceHolder.Callback {
    internal var testView: TextView? = null
    internal var camera: Camera? = null
    internal var surfaceView: SurfaceView? = null
    internal var surfaceHolder: SurfaceHolder? = null
    internal var rawCallback: android.hardware.Camera.PictureCallback? = null
    internal var shutterCallback: android.hardware.Camera.ShutterCallback? = null
    internal var jpegCallback: android.hardware.Camera.PictureCallback? = null
    lateinit var mainIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surfave_view)
        mainIntent=intent
        var path: String = String.format("/sdcard/%d.jpg", System.currentTimeMillis())
        surfaceView = findViewById(R.id.surfaceView) as SurfaceView
        surfaceHolder = surfaceView!!.getHolder()
        surfaceHolder!!.addCallback(this)
        surfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        jpegCallback = object : Camera.PictureCallback {
            override fun onPictureTaken(data: ByteArray, camera: Camera) {
                var outStream: FileOutputStream? = null
                try {
                    outStream = FileOutputStream(path)
                    outStream.write(data)
                    outStream.close()

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                }
                refreshCamera()
                Toast.makeText(applicationContext, "Picture Saved", Toast.LENGTH_SHORT).show()
//for sending image
                var intent = Intent(applicationContext, CameraActivity::class.java)
                intent.putExtra("BitmapImagePath", path)
                intent.putExtra("key", mainIntent.getStringExtra("key"))
                intent.putExtra("key1", mainIntent.getStringExtra("key1"))
                startActivity(intent)

            }
        }
    }


    @Throws(IOException::class)
    fun captureImage(v: View) {

        camera!!.takePicture(null, null, jpegCallback)
    }

    fun refreshCamera() {
        if (surfaceHolder!!.getSurface() == null) {
            return
        }

        try {
            camera!!.stopPreview()
        } catch (e: Exception) {
        }
        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.startPreview()
        } catch (e: Exception) {
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        refreshCamera()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera!!.stopPreview()
        camera!!.release()
        camera = null

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            camera = Camera.open()
        } catch (e: RuntimeException) {

            System.err.println(e)
            return
        }
        val param: Camera.Parameters
        param = camera!!.getParameters()
        param.setPreviewSize(352, 288)
        camera!!.setParameters(param)
        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.setDisplayOrientation(90)
            camera!!.startPreview()
        } catch (e: Exception) {
            System.err.println(e)
            return
        }
    }

}