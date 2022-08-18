package com.nyj.routinemaker

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment1.*
import kotlinx.android.synthetic.main.fragment2.*
import kotlinx.android.synthetic.main.fragment2.listView
import java.time.LocalDate

class Fragment2 : Fragment() {

    var PlanList = arrayListOf<Plan>(
        Plan(
            0L, "더미", "2022", "11",
            "12", "0", "0", ""
        )
    )
    var date_ = ""
    var clicked_year = "2022"
    var clicked_month = "8"
    var clicked_dayofMonth = "20"
    var count = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //xml과 연결
        val view = inflater.inflate(R.layout.fragment2, container, false)
        clicked_year= LocalDate.now().year.toString()
        clicked_month= LocalDate.now().monthValue.toString()
        clicked_dayofMonth= LocalDate.now().dayOfMonth.toString()
        date_ = clicked_year+clicked_month+clicked_dayofMonth
        println(date_)
        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext, AppDatabase::class.java, "routine_database"
        ).allowMainThreadQueries().build()
        PlanList = db.plan_DAO().getPlanbyDate(date_).toTypedArray().toCollection(ArrayList<Plan>())
        count = db.plan_DAO().countPlan(date_)
        db.close()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listcount.text = count.toString()+"개"

        val Adapter = PlanAdapter(requireContext(), PlanList)
        listView.adapter = Adapter

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as Plan

                val intent = Intent(activity, ModPlan_Activity::class.java)
                intent.apply {
                    intent.putExtra(
                        "planid",
                        selectItem.id
                    )


                    startActivity(intent)

                }

            }



        calendarView.setOnDateChangeListener { view, year, month, dayofMonth ->
            clicked_year = year.toString()
            clicked_month = (month + 1).toString()
            clicked_dayofMonth = dayofMonth.toString()
            val db = Room.databaseBuilder(
                requireActivity().applicationContext, AppDatabase::class.java, "routine_database"
            ).allowMainThreadQueries().build()

            //
            date_ = year.toString() + (month + 1).toString() + dayofMonth.toString()
            PlanList =
                db.plan_DAO().getPlanbyDate(date_).toTypedArray().toCollection(ArrayList<Plan>())
            println(PlanList)
            count = db.plan_DAO().countPlan(date_)
            listcount.text = count.toString()+"개"

            val Adapter = PlanAdapter(requireContext(), PlanList)
            listView.adapter = Adapter

            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectItem = parent.getItemAtPosition(position) as Plan

                    val intent = Intent(activity, ModPlan_Activity::class.java)
                    intent.apply {
                        intent.putExtra(
                            "planid",
                            selectItem.id
                        )


                        startActivity(intent)

                    }

                }
            db.close()


        }

        button.setOnClickListener {
            val intent = Intent(activity, AddPlan_Activity::class.java)
            intent.apply {
                intent.putExtra(
                    "year",
                    clicked_year.toString()
                )//toString으로 형변환 해야 null값이 아닌채로 데이터 전송.0730
                intent.putExtra("month", clicked_month.toString())
                intent.putExtra("day", clicked_dayofMonth.toString())//dev브랜치테스트
                startActivity(intent)
            }


        }


    }
}
