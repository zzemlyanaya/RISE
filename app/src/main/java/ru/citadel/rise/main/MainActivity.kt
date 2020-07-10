package ru.citadel.rise.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.avangard.rise.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
       // val fragment = supportFragmentManager.findFragmentById(R.id.containerMain)
    }
}