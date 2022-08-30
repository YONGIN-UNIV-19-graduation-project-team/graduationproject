package com.nyj.routinemaker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment1.*

class Fragment1 : Fragment() {

    //전체 루틴 더미 객체리스트 생성
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","55","55",
            true,true,true,true,true,true,true,false)
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ////xml과 연결->view
        val view = inflater.inflate(R.layout.fragment1, container, false)

        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //전체 루틴을 생성한 객체리스트에 저장
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())

        //db 닫기
        db.close()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //리스너 호출
        setOnClickListener()
        //루틴리스트 ListView에 띄우기
        val Adapter = RoutineAdapter(requireContext(), RoutineList)
        listView.adapter = Adapter

        //ListView의 클릭리스너 구현 -> id값을 전달해서 루틴수정으로 넘어갈 수 있도록 설정
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as Routine

                //intent로 루틴수정(ModRoutine)으로 넘어갈 수 있음
                val intent = Intent(activity, ModRoutine_Activity::class.java)
                intent.apply {
                    intent.putExtra("routineid", selectItem.id)
                    startActivity(intent)
                }
                //리스너 재호출
                setOnClickListener()
            }
    }

    //리스너(루틴추가)
    fun setOnClickListener() {
        addrt_button.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity, Test::class.java)
            startActivity(intent)
        })
    }



}