package com.example.tapanjoshi.cameracurrentlocationapplication

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Patterns
import android.widget.EditText
import com.example.tapanjoshi.cameracurrentlocationapplication.R.string.email
import kotlinx.android.synthetic.main.activity_main.*

var CAMERA_REQUEST_CODE = 100


class MainActivity : AppCompatActivity(){

    val mEmail : EditText? = null
    val mPassword : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  mEmail =findViewById(R.id.et_email) as EditText
        val mPassword=findViewById(R.id.et_password) as EditText

        btn_login.setOnClickListener{

            val intent=Intent(this,SurfaveViewActivity::class.java)
            intent.putExtra("key", et_email.text.toString() )
            intent.putExtra("key1", et_password.text.toString() )
            startActivityForResult(intent,CAMERA_REQUEST_CODE)
            validation()
        }
    }

    fun validation(){

        if(mEmail!!.equals("")){
            mEmail.setError("Please Enter Email Id")
        }
//        else if(Patterns.EMAIL_ADDRESS.matcher(mEmail?).matches()){
//            mEmail.setError("Please Enter correct Email Format");
//        }
        else if( mPassword!!.equals("") )
        {
            mPassword.setError("Please Enter Password");
        }
    }


    override fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resCode, data)

        if (resCode == RESULT_OK) {
            if (reqCode == CAMERA_REQUEST_CODE) {
                if (data != null) {
                    val photo = data.getExtras().get("data") as Bitmap
                    val IntentCamera = Intent(this, CameraActivity::class.java)
                    IntentCamera.putExtra("BitmapImage", photo)
                    IntentCamera.putExtra("key", et_email.text.toString() )
                    IntentCamera.putExtra("key1", et_password.text.toString() )
                    startActivity(IntentCamera)
                }

            }
        }
    }
}






