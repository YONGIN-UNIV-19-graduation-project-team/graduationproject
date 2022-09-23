package com.nyj.routinemaker


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.concurrent.timer

class Test : AppCompatActivity() {
    var resultText="아무 텍스트나 입력"
    var routineSuccess=false
    var getID:Long = 999
    var second : Int = 60


    val handler: Handler = object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            // 원래 하려던 동작 (UI변경 작업 등)
            if(second>0){
                timer.text =second.toString()
            }else timer.text = "시간 초과! 곧 카메라가 종료됩니다."

        }
    }


    @SuppressLint("UnsafeOptInUsageError", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_test)

        val pref = this.getSharedPreferences("rec",0)

        //타이머는 메인쓰레드가 아니라서 view에 접근할 수 없다. 그러므로 handler객체를 사용해야 함
        timer(period = 1000, initialDelay = 1000){
            second--
            val msg = handler.obtainMessage()
            handler.sendMessage(msg)

            if(second<1){

                routineSuccess=true
                //application이동
            }

        }

        /////db연동....
        getID = intent.getStringExtra("id")!!.toLong()
        val db = Room.databaseBuilder(
                this,AppDatabase::class.java,"routine_databases"
            ).allowMainThreadQueries().build()
        val Routine = db.routine_DAO().getRoutinebyId(getID)
        routineName.text = "["+Routine.name.toString()+"] 글자를 인식하세요!"


        button.setOnClickListener(){
            var editor_exit = pref.edit()
            editor_exit.clear()
            editor_exit.putLong("routineId",getID)
            editor_exit.apply()
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)

        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val textRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        val executor = ContextCompat.getMainExecutor(this)
        if(!routineSuccess) {
            cameraProviderFuture.addListener({
                val provider = cameraProviderFuture.get()

                val previewUseCase = Preview.Builder().build().apply {
                    setSurfaceProvider(TESTpreView.surfaceProvider)
                }

                val analysisUseCase = ImageAnalysis.Builder().build().apply {
                    setAnalyzer(executor) { image: ImageProxy ->
                        textRecognizer.process(
                            InputImage.fromMediaImage(
                                image.image!!,
                                image.imageInfo.rotationDegrees
                            )

                        ).addOnSuccessListener { visionText ->
                            // 인식이 끝났을 때에 할 일
                            for (block in visionText.textBlocks) {
                                var BoundingBox = block.boundingBox
                                var canvas = Canvas()
                                rectOverlay.drawOverlay(BoundingBox,canvas)
                                resultText = block.text
                                //공백제거
                                var replace_resultText = resultText.replace(" ","")
                                //텍스트뷰에 띄우기
                                TESTTextView.text = replace_resultText

                                if (Routine.name == resultText.toString()) {
                                    routineSuccess = true
                                    Routine.routineischecked = true
                                    db.routine_DAO().update(Routine)
                                    db.close()
                                }
                            }


                            //인식완료?
                        }.addOnCompleteListener {
                            image.close()
                            var editor = pref.edit()
                            editor.clear()
                            editor.putLong("routineId",getID)
                            editor.apply()

                            if (Routine.name == resultText.toString()) {
                                Toast.makeText(applicationContext,"루틴 성공!",Toast.LENGTH_SHORT ).show()
                                var editor2 = pref.edit()
                                editor2.clear()
                                editor2.putLong("routineId",999)
                                editor2.apply()
                                //반복 provider 종료
                                provider.unbindAll()
                                val intent = Intent(applicationContext,MainActivity::class.java)
                                startActivity(intent)

                            }
                            if(routineSuccess){
                                provider.unbindAll()
                                val intent = Intent(applicationContext,MainActivity::class.java)
                                startActivity(intent)
                            }
                            //종료 후 intent로 application 이동

                        }

                    }

                }
//                if(second==0){
//                    //반복 provider 종료
//                    if(routineSuccess)provider.unbindAll()
//                    //종료 후 intent로 application 이동
//                    Toast.makeText(applicationContext,"제한시간 경과로 실패!",Toast.LENGTH_SHORT ).show()
//                    val intent = Intent(applicationContext,MainActivity::class.java)
//                    startActivity(intent)
//                }

                //카메라 provider의 무한 루프
                provider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    analysisUseCase,


                )



            }, executor)
        }



    }


//    fun timerSetting(){
//        timer(period = 1000, initialDelay = 1000){
//            second--
//            timer.text = second.toString()
//        }
//    }
}
