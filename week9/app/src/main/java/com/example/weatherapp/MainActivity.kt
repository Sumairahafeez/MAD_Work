package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var tvTemperature: TextView

    // IMPORTANT: Replace with your actual API key from openweathermap.org
    private val API_KEY = "6655adb1a1b57dff323167a36e2dc475"
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCity = findViewById(R.id.etCity)
        btnGetWeather = findViewById(R.id.btnGetWeather)
        tvTemperature = findViewById(R.id.tvTemperature)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)

        btnGetWeather.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            } else if (API_KEY == "YOUR_API_KEY") {
                Toast.makeText(this, "Please set your API Key in MainActivity.kt", Toast.LENGTH_LONG).show()
                tvTemperature.text = "API Key Missing"
            } else {
                getWeather(service, city)
            }
        }
    }

    private fun getWeather(service: WeatherService, city: String) {
        tvTemperature.text = "Loading..."
        service.getWeather(city, API_KEY).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val temp = weatherResponse?.main?.temp
                    tvTemperature.text = "${temp}°C"
                } else {
                    tvTemperature.text = "Error: ${response.code()}"
                    val errorMsg = if (response.code() == 401) "Invalid API Key" else "City not found"
                    Toast.makeText(this@MainActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvTemperature.text = "Network Error"
                Toast.makeText(this@MainActivity, "Check internet connection", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
