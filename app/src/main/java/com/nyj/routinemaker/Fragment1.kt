package com.nyj.routinemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment1.*

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ////루틴리스트
        val Adapter = RoutineAdapter(requireContext(), RoutineList)
        listView.adapter = Adapter
        setOnClickListener()
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