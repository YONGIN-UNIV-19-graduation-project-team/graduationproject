package com.nyj.routinemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment1.*
import kotlinx.coroutines.selects.select

class Fragment1 : Fragment() {

    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"55","55","55",
            true,true,true,true,true,true,true)
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ////xml과 연결
        val view = inflater.inflate(R.layout.fragment1, container, false)
        val db = Room.databaseBuilder(
            requireActivity().applicationContext,AppDatabase::class.java,"database"
        ).allowMainThreadQueries().build()
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ////루틴리스트
        val Adapter = RoutineAdapter(requireContext(), RoutineList)
        listView.adapter = Adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as Routine
                //Toast.makeText(requireContext(),selectItem.name,Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, ModRoutine_Activity::class.java)
                intent.apply {
                    intent.putExtra(
                        "routineid",
                                selectItem.id
                    )


                    startActivity(intent)

                }
                setOnClickListener()
            }
    }

    fun setOnClickListener() {
        cal_button.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Challenge_Activity::class.java)
            startActivity(intent)
        })
        add_button.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddRoutine_Activity::class.java)
            startActivity(intent)
        })
    }



}