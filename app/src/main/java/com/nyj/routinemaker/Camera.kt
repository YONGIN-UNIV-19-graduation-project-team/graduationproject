package com.nyj.routinemaker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.PixelCopy
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.exifinterface.media.ExifInterface
import com.googlecode.tesseract.android.TessBaseAPI
import kotlinx.android.synthetic.main.activity_camera.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import splitties.toast.toast
import java.io.*


class Camera : AppCompatActivity() {

    //ocr
    lateinit var tess: TessBaseAPI //Tesseract API 객체 생성
    var dataPath: String = "" //데이터 경로 변수 선언


    init {
        val isIntialized = OpenCVLoader.initDebug()
    }

    private lateinit var mSurfaceViewHolder: SurfaceHolder
    private lateinit var mImageReader: ImageReader
    private lateinit var mCameraDevice: CameraDevice
    private lateinit var mPreviewBuilder: CaptureRequest.Builder
    private lateinit var mSession: CameraCaptureSession

    private var mHandler: Handler? = null

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private lateinit var mSensorManager: SensorManager

    private val deviceOrientation: DeviceOrientation by lazy { DeviceOrientation() }
    private var mHeight: Int = 0
    private var mWidth: Int = 0

    var mCameraId = CAMERA_BACK

    companion object {
        const val CAMERA_BACK = "0"
        const val CAMERA_FRONT = "1"

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //System.currentTimeMillis()
        //val nowTime = "${System.currentTimeMillis()}"
        //val getTime = intent.getStringExtra("time").toString().toInt()
        //val calculate_time = nowTime.toInt()-getTime

//        if(calculate_time>60000) {
//            AlertDialog.Builder(this)
//                .setMessage("시간초과!!!!!!!")
//                .setPositiveButton("예", object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        val intent = Intent(applicationContext, MainActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                })
//                .create()
//                .show()
//        }


        // 상태바 숨기기
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // 화면 켜짐 유지
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        /////////tesseract/////////

        var lang: String = "kor"

        dataPath = "$filesDir/tesseract/" //언어데이터의 경로 미리 지정
        checkFile(File(dataPath), "kor")//사용할 언어파일의 이름 지정
        tess = TessBaseAPI() //api준비
        //tess.init(dataPath,lang) //해당 사용할 언어데이터로 초기화



        button_check.setOnClickListener() {
            val bitmap  = capturePicture()

            if (bitmap != null) {
                Toast.makeText(applicationContext,"capture success",Toast.LENGTH_SHORT).show()
                processImage(bitmap)

                } else
                    Toast.makeText(applicationContext,"capture fail",Toast.LENGTH_SHORT).show()


//            val getID = intent.getStringExtra("id")!!.toLong()
//            val db = Room.databaseBuilder(
//                this,AppDatabase::class.java,"routine_databases"
//            ).allowMainThreadQueries().build()
//            val Routine = db.routine_DAO().getRoutinebyId(getID)
//            Routine.routineischecked=true
//            db.routine_DAO().update(Routine)
//            db.close()
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
        }
        initSensor()
        initView()
    }





    //surfaceview capture
    @RequiresApi(Build.VERSION_CODES.N)
    fun capturePicture()  : Bitmap {
        val Cbitmap = Bitmap.createBitmap(surfaceView.width, surfaceView.height, Bitmap.Config.ARGB_8888)
        PixelCopy.request(surfaceView, Cbitmap, { i ->
            imageViewSf.setImageBitmap(Cbitmap) //"iv_Result" is the image view
        }, Handler(Looper.getMainLooper()))
        return Cbitmap
    }




    private fun initSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun initView() {
        with(DisplayMetrics()){
            windowManager.defaultDisplay.getMetrics(this)
            mHeight = heightPixels
            mWidth = widthPixels
        }

        mSurfaceViewHolder = surfaceView.holder
        mSurfaceViewHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                initCameraAndPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mCameraDevice.close()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int,
                width: Int, height: Int
            ) {

            }
        })

        btn_convert.setOnClickListener { switchCamera() }
    }

    private fun switchCamera() {
        when(mCameraId){
            CAMERA_BACK -> {
                mCameraId = CAMERA_FRONT
                mCameraDevice.close()
                openCamera()
            }
            else -> {
                mCameraId = CAMERA_BACK
                mCameraDevice.close()
                openCamera()
            }
        }
    }


    fun initCameraAndPreview() {
        val handlerThread = HandlerThread("CAMERA2")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)

        openCamera()
    }

    private fun openCamera() {
        try {
            val mCameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = mCameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            val largestPreviewSize = map!!.getOutputSizes(ImageFormat.JPEG)[0]
            setAspectRatioTextureView(largestPreviewSize.height, largestPreviewSize.width)

            mImageReader = ImageReader.newInstance(
                largestPreviewSize.width,
                largestPreviewSize.height,
                ImageFormat.JPEG,
                7
            )
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) return

            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler)
        } catch (e: CameraAccessException) {
            toast("카메라를 열지 못했습니다.")
        }
    }

    private val deviceStateCallback = object : CameraDevice.StateCallback() {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            try {
                takePreview()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCameraDevice.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            toast("카메라를 열지 못했습니다.")
        }
    }

    @Throws(CameraAccessException::class)
    fun takePreview() {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mPreviewBuilder.addTarget(mSurfaceViewHolder.surface)

        mCameraDevice.createCaptureSession(
            listOf(mSurfaceViewHolder.surface, mImageReader.surface), mSessionPreviewStateCallback, mHandler
        )
    }

    private val mSessionPreviewStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            mSession = session
            try {
                // Key-Value 구조로 설정
                // 오토포커싱이 계속 동작
                mPreviewBuilder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                //필요할 경우 플래시가 자동으로 켜짐
                mPreviewBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                )
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Toast.makeText(this@Camera, "카메라 구성 실패", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()

        mSensorManager.registerListener(
            deviceOrientation.eventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI
        )
        mSensorManager.registerListener(
            deviceOrientation.eventListener, mMagnetometer, SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(deviceOrientation.eventListener)
    }

    private fun setAspectRatioTextureView(ResolutionWidth: Int, ResolutionHeight: Int) {
        if (ResolutionWidth > ResolutionHeight) {
            val newWidth = mWidth
            val newHeight = mWidth * ResolutionWidth / ResolutionHeight
            updateTextureViewSize(newWidth, newHeight)

        } else {
            val newWidth = mWidth
            val newHeight = mWidth * ResolutionHeight / ResolutionWidth
            updateTextureViewSize(newWidth, newHeight)
        }

    }

    private fun updateTextureViewSize(viewWidth: Int, viewHeight: Int) {
        Log.d("ViewSize", "TextureView Width : $viewWidth TextureView Height : $viewHeight")
        surfaceView.layoutParams = FrameLayout.LayoutParams(viewWidth, viewHeight)
    }



    ////////이미지 처리, opencv////////

    private fun imageopencv(bitmap : Bitmap) {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        //convert to grayscale
        val grayImg = Mat()
        Imgproc.cvtColor(mat,grayImg, Imgproc.COLOR_RGB2GRAY)

        //이진화
        val binaryImg = Mat()
        Imgproc.threshold(grayImg, binaryImg, 0.0,255.0 , Imgproc.THRESH_OTSU)

        //윤곽선찾기
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            binaryImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE
        )

        // 가장 면적이 큰 윤곽선 찾기
        var biggestContour: MatOfPoint? = null
        var biggestContourArea: Double = 0.0
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > biggestContourArea) {
                biggestContour = contour
                biggestContourArea = area
            }
        }

        if (biggestContour == null) {
            throw IllegalArgumentException("No Contour")
        }
        // 너무 작아도 안됨
        if (biggestContourArea < 400) {
            throw IllegalArgumentException("too small")
        }


        //근사화
        val candidate2f = MatOfPoint2f(*biggestContour.toArray())
        val approxCandidate = MatOfPoint2f()
        Imgproc.approxPolyDP(
            candidate2f,
            approxCandidate,
            Imgproc.arcLength(candidate2f, true) * 0.02,
            true
        )

        //투시변환
        // 좌상단부터 시계 반대 방향으로 정점을 정렬한다.
        val points = arrayListOf(
            Point(approxCandidate.get(0, 0)[0], approxCandidate.get(0, 0)[1]),
            Point(approxCandidate.get(1, 0)[0], approxCandidate.get(1, 0)[1]),
            Point(approxCandidate.get(2, 0)[0], approxCandidate.get(2, 0)[1]),
            Point(approxCandidate.get(3, 0)[0], approxCandidate.get(3, 0)[1]),
        )
        points.sortBy { it.x } // x좌표 기준으로 먼저 정렬

        if (points[0].y > points[1].y) {
            val temp = points[0]
            points[0] = points[1]
            points[1] = temp
        }

        if (points[2].y < points[3].y) {
            val temp = points[2]
            points[2] = points[3]
            points[3] = temp
        }
        // 원본 영상 내 정점들
        val srcQuad = MatOfPoint2f().apply { fromList(points) }

        val maxSize = calculateMaxWidthHeight(
            tl = points[0],
            bl = points[1],
            br = points[2],
            tr = points[3]
        )
        val dw = maxSize.width
        val dh = dw * maxSize.height/maxSize.width
        val dstQuad = MatOfPoint2f(
            Point(0.0, 0.0),
            Point(0.0, dh),
            Point(dw, dh),
            Point(dw, 0.0)
        )
    // 투시변환 매트릭스 구하기
        val perspectiveTransform = Imgproc.getPerspectiveTransform(srcQuad, dstQuad)

    // 투시변환 된 결과 영상 얻기
        val dst = Mat()
        Imgproc.warpPerspective(mat, dst, perspectiveTransform, Size(dw, dh))


        val result = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888)
        if (result != null) {
            Toast.makeText(applicationContext,"opencv success",Toast.LENGTH_SHORT).show()
            test.setImageBitmap(result)
            processImage(result)
        } else
            Toast.makeText(applicationContext,"opencv fail",Toast.LENGTH_SHORT).show()


    }


    // 사각형 꼭짓점 정보로 사각형 최대 사이즈 구하기
    // 평면상 두 점 사이의 거리는 직각삼각형의 빗변길이 구하기와 동일
    private fun calculateMaxWidthHeight(
        tl:Point,
        tr:Point,
        br:Point,
        bl:Point,
    ): Size {
        // Calculate width
        val widthA = kotlin.math.sqrt((tl.x - tr.x) * (tl.x - tr.x) + (tl.y - tr.y) * (tl.y -tr.y))
        val widthB = kotlin.math.sqrt((bl.x - br.x) * (bl.x - br.x) + (bl.y - br.y) * (bl.y - br.y))
        val maxWidth = kotlin.math.max(widthA, widthB)
        // Calculate height
        val heightA =  kotlin.math.sqrt((tl.x - bl.x) * (tl.x - bl.x) + (tl.y - bl.y) * (tl.y - bl.y))
        val heightB =  kotlin.math.sqrt((tr.x - br.x) * (tr.x - br.x) + (tr.y - br.y) * (tr.y - br.y))
        val maxHeight = kotlin.math.max(heightA, heightB)
        return Size(maxWidth, maxHeight)
    }



    /////////////////////////////ocr기능 구현//////////////////////////////
    fun copyFile(lang : String){
        try{
            //언어 데이터 파일 위치
            var filePath : String = dataPath+lang+".traineddata"

            //AssestManager
            var assetManager : AssetManager = getAssets();

            //byte 스트림을 읽기 쓰기용으로 열기
            var inputStream : InputStream = assetManager.open(lang+".traineddata")
            var outStream : OutputStream = FileOutputStream(filePath)


            //위에 적어둔 파일 경로쪽으로 해당 바이트코드 파일을 복사한다.
            var buffer : ByteArray = ByteArray(1024)

            var read : Int = 0
            read = inputStream.read(buffer)
            while(read!=-1){
                outStream.write(buffer,0,read)
                read = inputStream.read(buffer)
            }
            outStream.flush()
            outStream.close()
            inputStream.close()
        }
        catch (e : FileNotFoundException){
            Log.v("error", e.toString())
        }catch (e : IOException){
            Log.v("error",e.toString())
        }
    }

    fun checkFile(dir : File, lang : String){

        //파일의 존재여부 확인 후 내부로 복사
        if(!dir.exists()&&dir.mkdirs()){
            copyFile(lang)
        }

        if(dir.exists()){
            var datafilePath : String = dataPath+lang+".traineddata"
            var dataFile : File = File(datafilePath)
            if(!dataFile.exists()){
                copyFile(lang)
            }
        }

    }


    fun processImage(bitmap: Bitmap){
        Toast.makeText(applicationContext,"잠시 기다려 주세요",Toast.LENGTH_SHORT).show()
        var ocrResult : String? = null;
        tess.setImage(bitmap)
        ocrResult = tess.utF8Text
        Toast.makeText(applicationContext,"결과 : " + ocrResult,Toast.LENGTH_SHORT).show()

    }




}

