package com.nyj.routinemaker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission


class loadingActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 1000 // 1 sec


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_loading)

        setPermission()

    }


    //카메라 권한 허용
    private fun setPermission() {
        val permission = object : PermissionListener{
            override fun onPermissionGranted() {
                Handler().postDelayed({
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    // close this activity
                    finish()
                }, SPLASH_TIME_OUT)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@loadingActivity, "권한이 거부 되었습니다.",Toast.LENGTH_SHORT).show()

            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setPermissions(android.Manifest.permission.CAMERA)
            .check()
    }

}










