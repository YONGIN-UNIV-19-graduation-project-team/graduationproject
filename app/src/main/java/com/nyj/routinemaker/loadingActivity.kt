package com.nyj.routinemaker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
//import com.gun0912.tedpermission.PermissionListener
//import com.gun0912.tedpermission.TedPermission


class loadingActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 2000 // 1 sec


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        Handler().postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)

    }

}










