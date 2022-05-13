package com.quwi.testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quwi.testapp.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}