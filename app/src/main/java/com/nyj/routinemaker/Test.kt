package com.nyj.routinemaker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import org.opencv.dnn.TextDetectionModel_DB
import java.io.IOException


class Test : AppCompatActivity() {
    var surfaceView: SurfaceView? = null
    var textView: TextView? = null
    var cameraSource: CameraSource? = null
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Test.Companion.MY_PERMISSIONS_REQUEST_CMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    try {
                        cameraSource!!.start(surfaceView!!.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        surfaceView = findViewById<View>(R.id.TESTsurfaceView) as SurfaceView
        textView = findViewById<View>(R.id.TESTTextView) as TextView
        //Recognizer 를 생성하고, Detector.Processor 를 구현한 Proce
        val textRecognizer = TextRecognizer.Builder(applicationContext)
            .build()
        //
        if (!textRecognizer.isOperational) {
            Log.d("main", "nonoo")
        } else {
            //카메라 이미지를 얻기 위한 CameraSoutce 객체 형성  이미지를 연속적으로 스트리밍한
            cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(500, 1500)
                .setRequestedFps(2.0f) //초당 2 프레임
                .setAutoFocusEnabled(true)
                .build()
            surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        //permissionCamera();
//                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//
//                            ActivityCompat.requestPermissions(MainActivity.this,new String []{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_CMERA);
//                            return;
//                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.CAMERA
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                //사용자가 허가를 할때까지 기다리게 하는 그런 설명이 필요할때
                                ActivityCompat.requestPermissions(
                                    this@Test,
                                    arrayOf(Manifest.permission.CAMERA),
                                    MY_PERMISSIONS_REQUEST_CMERA
                                )
                                return
                            }
                        }
                        cameraSource!!.start(surfaceView!!.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    cameraSource!!.stop()
                }
            })
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {}
                override fun receiveDetections(detections: Detections<TextBlock>) {
                    val items = detections.detectedItems
                    val lang = arrayOf("ko-Kore")

                    if (items.size() != 0) {
                        textView!!.post {
                            val stringBuilder = StringBuilder()
                            for (i in 0 until items.size()) {
                                val item = items.valueAt(i)
                                stringBuilder.append(item.value)
                                stringBuilder.append('\n')
                            }
                            textView!!.text = stringBuilder.toString()
                        }
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource!!.release()
    }
    companion object {
        private const val MY_PERMISSIONS_REQUEST_CMERA = 1001
    }
}
