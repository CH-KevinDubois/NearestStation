package com.proj.neareststation

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_display_station.*
import kotlinx.android.synthetic.main.activity_get_position.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

private var TAG = "Debug App";
private val stations = ArrayList<Station>()
private val stationsAdapter : StationsAdapter = StationsAdapter(stations)

class DisplayStationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_station)

        val lat = intent.getStringExtra("lat")
        val lon = intent.getStringExtra("lon")


        stations.clear()

        //Appel à l'API
        val data = getStationsWithAPI()
        try {
            Log.i(TAG, "getStationsWithAPI : Ready to execute API call")
            data.execute(lat, lon)
        } catch (e: Exception){
            e.printStackTrace()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewStationItems)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = stationsAdapter

        Log.i(TAG, "stations : " + stationsAdapter.itemCount)

    }

    fun onBackClick(view: View) {
        finish();
    }

    @Suppress("DEPRECATION")
    inner class getStationsWithAPI : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg coords: String?): String {

            var result = ""
            var url: URL
            val httpURLConnection: HttpURLConnection

            try {

                val latitude = coords[0]
                val longitude = coords[1]

                url = URL("https://timetable.search.ch/api/completion.en.json?latlon=$latitude,$longitude&accuracy=10")
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character
                    data = inputStreamReader.read()

                }

                return result
            } catch (e: Exception) {
                e.printStackTrace()
                return result

            }

        }

        override fun onPostExecute(resultAPICall: String?) {
            super.onPostExecute(resultAPICall)

            try {

                val JSONarray = JSONArray(resultAPICall)

                for (i in 0 until JSONarray.length()) {
                    val item = JSONarray.getJSONObject(i)

                    println(item.getInt("dist"))
                    stations.add(
                        Station(
                            i+1,
                            item.getString("label"),
                            item.getInt("dist"),
                            item.getString("iconclass")))
                    println(item.getString("iconclass"))
                }



            } catch (e: Exception) {

                e.printStackTrace()

            }
            finally {
                stationsAdapter.notifyDataSetChanged()
            }
        }
    }
}