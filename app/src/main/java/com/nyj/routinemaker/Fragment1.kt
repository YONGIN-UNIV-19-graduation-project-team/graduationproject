package com.nyj.routinemaker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment1.*

class Fragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //xml과 연결
        val view = inflater.inflate(R.layout.fragment1, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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