package com.proj.neareststation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_get_position.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class GetPositionActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var TAG = "Debug";

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_position)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val getCoordinatesListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.buttonGetCoordinates ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                        editTextTextLatitude.setText(location!!.latitude.toString(), TextView.BufferType.EDITABLE);
                        editTextTextLongitude.setText(location!!.longitude.toString(), TextView.BufferType.EDITABLE);
                        location.reset();
                        Log.i(TAG, "Coords acquired")
                    }
            }
        }

        val checkLocationListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.buttonCheck ->{
                    val lat = editTextTextLatitude.text.toString();
                    val lon = editTextTextLongitude.text.toString();
                    Log.i(TAG, "lon :" + lon)
                    Log.i(TAG, "lat :" + lat)
                    if(lat.toDoubleOrNull() != null && lon.toDoubleOrNull() != null)
                    {
                        val data = CheckLocation()
                        try {
                            Log.i(TAG, "Ready to execute API call")
                            data.execute(lat, lon)
                        } catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }

            }
        }

        val getStationsListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.buttonGetStations ->
                {
                    val lat = editTextTextLatitude.text.toString();
                    val lon = editTextTextLongitude.text.toString();
                    Log.i(TAG, "Start DisplayStationActivity")
                    val intent= Intent(this@GetPositionActivity, DisplayStationActivity::class.java)
                    intent.putExtra("lat", lat)
                    intent.putExtra("lon", lon)
                    startActivity(intent)
                }
            }
        }

        buttonGetCoordinates.setOnClickListener(getCoordinatesListener)
        buttonCheck.setOnClickListener(checkLocationListener)
        buttonGetStations.setOnClickListener(getStationsListener)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    Log.i(TAG, "Update location")
                }
            }
        }

        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 5000
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.smallestDisplacement = 100f
        val ok = fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        Log.i(TAG, "update lances: " + ok.isSuccessful)

    }

    override fun onStart() {
        super.onStart()
    }

    @SuppressLint("MissingPermission")
    fun reset (view: View) {
        when (view.id) {
            R.id.buttonReset -> {
                editTextTextLatitude.setText("", TextView.BufferType.EDITABLE);
                editTextTextLatitude.isEnabled = true;
                editTextTextLongitude.setText("", TextView.BufferType.EDITABLE);
                editTextTextLongitude.isEnabled = true;
                buttonGetCoordinates.isEnabled = true;
                buttonGetStations.isVisible = false;
                fusedLocationClient.flushLocations()
            }
        }
    }

    @Suppress("DEPRECATION")
    inner class CheckLocation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg coords: String?): String {

            var result = ""
            var url: URL
            val httpURLConnection: HttpURLConnection

            try {

                val latitude = coords[0]
                val longitude = coords[1]

                url = URL("https://api.opencagedata.com/geocode/v1/json?q=$latitude%2C%20$longitude&key=83128c9bcbc84e6ca53969500bd1b73a&language=fr&pretty=1")
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

                val jSONObject = JSONObject(resultAPICall)
                println(jSONObject)
                val results = jSONObject.getJSONArray("results")
                val components = results.getJSONObject(0).getJSONObject("components")
                val countryCode = components.getString("country_code")
                println(countryCode)

                if(countryCode== "ch"){
                    buttonGetStations.isVisible = true;
                    editTextTextLatitude.isEnabled = false;
                    editTextTextLongitude.isEnabled = false;
                    buttonGetCoordinates.isEnabled = false;
                }
                else
                {
                    buttonGetStations.isVisible = false;
                    val alert = AlertDialog.Builder(this@GetPositionActivity)
                    alert.setMessage("Selectionnez un emplacement en Suisse!")
                        .show();
                }

            } catch (e: Exception) {

                e.printStackTrace()

            }
        }
    }
}