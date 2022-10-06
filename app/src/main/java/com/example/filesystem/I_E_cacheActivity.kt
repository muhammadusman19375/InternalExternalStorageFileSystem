package com.example.filesystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class I_E_cacheActivity : AppCompatActivity() {
    var et_type_here: EditText? = null
    var btn_create: Button? = null
    var btn_read: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ie_cache)

        et_type_here = findViewById(R.id.et_type_here)
        btn_create = findViewById(R.id.btn_create)
        btn_read = findViewById(R.id.btn_read)
    }
}