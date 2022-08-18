package com.nyj.routinemaker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addplan.*
import kotlinx.android.synthetic.main.activity_modroutine.*
import kotlinx.android.synthetic.main.list_item_plan.*
import java.text.SimpleDateFormat
import java.util.*

class ModPlan_Activity : AppCompatActivity() {
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    var plan_Year=""
    var plan_Month=""
    var plan_Day=""
    var plan_Hour = ""
    var plan_Min=""
    var plan_Name=""


    var timeisselected=true
    var nameisnotnull=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modplan)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"routine_database"
        ).allowMainThreadQueries().build()

        var intent = getIntent()
        var planid = intent.getLongExtra("planid",0)

        //id Toast로 띄우는 주석.
        //Toast.makeText(this, planid.toString(), Toast.LENGTH_SHORT).show()
        var get_plan = db.plan_DAO().getPlanbyId(planid)//여기에서 해당id의 plan 가져옴

        textview_get_date.text = get_plan.year+"년 "+get_plan.month+"월 "+get_plan.day+"일"

        val gethour = get_plan.hour.toInt()
        val getmin = get_plan.min.toInt()
        if(get_plan.hour.toInt()<10&&get_plan.min.toInt()<10) {
            textview_get_time.text = "0$gethour : 0$getmin"
        }
        else {
            if (get_plan.min.toInt()<10) {
                textview_get_time.text = "$gethour : 0$getmin"
            }
            else
                if(get_plan.hour.toInt()<10) {
                    textview_get_time.text = "0$gethour : $getmin"
                }
                else textview_get_time.text = "$gethour : $getmin"
        }



        plan_Name = get_plan.name
        planName.setText(plan_Name)
        plan_Year = get_plan.year
        plan_Month=get_plan.month
        plan_Day = get_plan.day
        plan_Hour = get_plan.hour
        plan_Min = get_plan.min

        //날짜 선택 란//
        btn_pick_date.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR,i)
                    selectDate.set(Calendar.MONTH,i2)
                    selectDate.set(Calendar.DAY_OF_MONTH,i3)
                    val date = formatDate.format(selectDate.time)
                    Toast.makeText(this, "Date : "+date, Toast.LENGTH_SHORT ).show()
                    textview_get_date.text = date

                    plan_Year=selectDate.get(Calendar.YEAR).toString()
                    plan_Month=(selectDate.get(Calendar.MONTH).toInt()+1).toString()//1달 낮게 값 저장되는 현상..
                    plan_Day=selectDate.get(Calendar.DAY_OF_MONTH).toString()

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()


        })


        //시간 선택 란 //정상
        btn_pick_time.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            getDate.set(Calendar.HOUR,gethour)
            getDate.set(Calendar.MINUTE,getmin)
            val datePicker = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimePickerDialog.OnTimeSetListener { timePicker, i, i2->
                val selectDate = Calendar.getInstance()

                selectDate.set(Calendar.HOUR,i)
                selectDate.set(Calendar.MINUTE,i2)
                Toast.makeText(this, "Time : $i : $i2", Toast.LENGTH_SHORT ).show()
                timeisselected=true

                if(i<10&&i2<10) {
                    textview_get_time.text = "0$i : 0$i2"
                }
                else {
                    if (i2<10) {
                        textview_get_time.text = "$i : 0$i2"
                    }
                    else
                        if(i<10) {
                            textview_get_time.text = "0$i : $i2"
                        }
                        else textview_get_time.text = "$i : $i2"
                }

                plan_Hour=selectDate.get(Calendar.HOUR).toString()
                plan_Min=selectDate.get(Calendar.MINUTE).toString()

            },getDate.get(Calendar.HOUR),getDate.get(Calendar.MINUTE),true)
            datePicker.show()
        })


        //수정버튼
        mod_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            nameisnotnull=false
            plan_Name = planName.text.toString()
            if(plan_Name!="")nameisnotnull=true
            val add_date = plan_Year+plan_Month+plan_Day
            val plan = Plan(planid,plan_Name,plan_Year,plan_Month,plan_Day,plan_Hour,plan_Min,add_date)
            if(timeisselected) {
                if(nameisnotnull) {
                    db.plan_DAO().update(plan)
                    db.close()
                    intent.putExtra("access_plan",true)
                    startActivity(intent)
                }else Toast.makeText(this, "이름을 공백으로 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }else Toast.makeText(this, "시간이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }

        //삭제버튼
        del_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val plan = Plan(planid,plan_Name,plan_Year,plan_Month,plan_Day,plan_Hour,plan_Min,"")
            db.plan_DAO().delete(plan)
            db.close()
            intent.putExtra("access_plan",true)
            startActivity(intent)
        }




    }
}