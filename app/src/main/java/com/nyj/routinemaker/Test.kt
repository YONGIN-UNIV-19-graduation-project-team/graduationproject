package com.nyj.routinemaker


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.android.synthetic.main.activity_test.*


class Test : AppCompatActivity() {
    var resultText="아무 텍스트나 입력"
    var routineSuccess=false
    @SuppressLint("UnsafeOptInUsageError", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        /////db연동....
        val getID = intent.getStringExtra("id")!!.toLong()
        val db = Room.databaseBuilder(
                this,AppDatabase::class.java,"routine_databases"
            ).allowMainThreadQueries().build()
        val Routine = db.routine_DAO().getRoutinebyId(getID)



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
                                resultText = block.text
                                TESTTextView.text = resultText

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
                            if (Routine.name == resultText.toString()) {
                                Toast.makeText(applicationContext,"루틴 성공!",Toast.LENGTH_SHORT ).show()

                                //반복 provider 종료
                                if(routineSuccess)provider.unbindAll()
                                val intent = Intent(applicationContext,MainActivity::class.java)
                                startActivity(intent)

                            }
                        }

                    }

                }


                provider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    analysisUseCase,


                )


            }, executor)
        }



    }

}
