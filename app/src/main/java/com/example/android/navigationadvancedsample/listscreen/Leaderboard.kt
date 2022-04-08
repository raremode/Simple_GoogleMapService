/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationadvancedsample.listscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigationadvancedsample.R
import kotlin.properties.Delegates

/**
 * Shows a static leaderboard with multiple users.
 */
class Leaderboard : Fragment() {

    private val TAG = "MapActivity"
    var choiceType : Int = 0 //0-default, 1-пластик, 2-стекло, 3-батарейки
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    lateinit var buttonChoicePlastic : Button
    lateinit var buttonChoiceGlass : Button
    lateinit var buttonChoiceBatteries : Button
    lateinit var buttonSendRequest : Button
    lateinit var fieldLatitude : EditText
    lateinit var fieldLongitude : EditText

    private lateinit var databaseWork : DataBase_work

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonChoicePlastic = view.findViewById(R.id.choicePlastic) as Button
        buttonChoiceGlass = view.findViewById(R.id.choiceGlass) as Button
        buttonChoiceBatteries = view.findViewById(R.id.choiceBatteries) as Button
        buttonSendRequest = view.findViewById(R.id.sendRequest) as Button
        fieldLatitude = view.findViewById(R.id.getLatitude) as EditText
        fieldLongitude = view.findViewById(R.id.getLongitude) as EditText

        databaseWork = DataBase_work(context)
        choiceClickListener()
    }

    private fun choiceClickListener(){
        buttonChoicePlastic.setOnClickListener {
            choiceType = 1
            Toast.makeText(
                context,
                "Choiced type of garbage - Plastic, type " + choiceType,
                Toast.LENGTH_SHORT
            ).show()
        }

        buttonChoiceGlass.setOnClickListener {
            choiceType = 2
            Toast.makeText(
                context,
                "Choiced type of garbage - Glass, type " + choiceType,
                Toast.LENGTH_SHORT
            ).show()
        }

        buttonChoiceBatteries.setOnClickListener {
            choiceType = 3
            Toast.makeText(
                context,
                "Choiced type of garbage - Batteries, type " + choiceType,
                Toast.LENGTH_SHORT
            ).show()
        }

        buttonSendRequest.setOnClickListener {
            var sendLatitude : Double = fieldLatitude.text.toString().toDouble()
            var sendLongitude : Double = fieldLongitude.text.toString().toDouble()
            if(choiceType !=0 && sendLatitude!=0.0 && sendLongitude!=0.0){
        databaseWork.insert(choiceType, sendLatitude, sendLongitude)
            }
        }
    }
}

