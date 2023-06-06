package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.opsc.workmate.R

class GoalsFragment : Fragment() {

    lateinit var btnSetGoals: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        //Set onClick for Set Goals button
        btnSetGoals = view.findViewById(R.id.btnSetGoals)
        btnSetGoals.setOnClickListener {
            //navigate
            findNavController().navigate(R.id.action_goalsFragment_to_setGoalsFragment)
        }

        return view

    }
}