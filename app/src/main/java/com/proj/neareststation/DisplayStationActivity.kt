package com.proj.neareststation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_display_station.*

private var TAG = "Debug App";

class DisplayStationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_station)

        val lat = intent.getStringExtra("lat")
        val lon = intent.getStringExtra("lon")
        val stations = ArrayList<Station>()

        //Faire appel à l'API

        //textView.setText(lat.toString(), TextView.BufferType.EDITABLE);


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewStationItems)
        val stationsAdapter = StationsAdapter(stations)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = stationsAdapter

        stations.add(Station(1, "Zytgloggelaube 2, Bern", 19, "sl-icon-type-adr"))
        stations.add(Station(2, "Bern, Zytglogge", 51, "sl-icon-type-tram"))
        Log.i(TAG, "stations : " + stationsAdapter.itemCount)
        stationsAdapter.notifyDataSetChanged()

    }

    fun onBackClick(view: View) {
        finish();
    }
}