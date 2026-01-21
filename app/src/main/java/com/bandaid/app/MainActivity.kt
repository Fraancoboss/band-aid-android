package com.bandaid.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textTitle.text = "Band-AId funcionando \uD83D\uDC8A"
    }
}
