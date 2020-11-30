package com.proj.neareststation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private var TAG = "Debug App";


class StationsAdapter (private val stations: ArrayList<Station>) :
        RecyclerView.Adapter<StationsAdapter.MyViewHolder>(){

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var address: TextView = view.findViewById(R.id.textAddress)
        var distance: TextView = view.findViewById(R.id.textDistance)
        var id: TextView = view.findViewById(R.id.textID)
        var picture: ImageView = view.findViewById(R.id.imageViewIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val station = stations[position]
        Log.i(TAG, "onBindViewHolder : " + position)
        holder.id.text = station.id.toString()
        holder.address.text = station.address
        holder.distance.text = station.distance.toString()

        //holder.picture.setImageBitmap(station.bitmap)
    }
    override fun getItemCount(): Int {
        return stations.size
    }


}