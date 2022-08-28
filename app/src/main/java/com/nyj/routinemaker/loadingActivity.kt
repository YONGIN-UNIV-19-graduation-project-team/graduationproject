package com.nyj.routinemaker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission


class loadingActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 2000 // 1 sec


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        setPermission()

    }
    //카메라 권한허용 설정
    fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                ///Toast.makeText(this@MainActivity, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    startActivity(Intent(applicationContext, MainActivity::class.java))

                    // close this activity
                    finish()
                }, SPLASH_TIME_OUT)

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                //Toast.makeText(this@MainActivity, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 혀용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다.")
            .setPermissions(android.Manifest.permission.CAMERA)
            .check()
    }
}










