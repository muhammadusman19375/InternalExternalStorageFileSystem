package com.example.filesystem

import android.content.pm.PackageManager
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*
import java.util.jar.Manifest

class ExternalActivity : AppCompatActivity() {
    val TAG = "TAG"
    lateinit var editText: EditText
    var textView1: TextView? = null
    var textView2: TextView? = null
    var tv_cache: TextView? = null

    var FILE_NAME: String = "demoFile.txt"
    var REQUEST_PERMISSION_WRITE: Int = 1001
    var permissionGranted: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external)


//        var file: File? = getExternalFilesDir(null)     //used for private files
//        Log.d("TAG", "onCreate: " + file!!.absolutePath)  //used for private files

        var file: File = getExternalStorageDirectory() //used for public files
        var file1: File? = getExternalFilesDir(null)

        var path: String = cacheDir.absolutePath    // for internal cache dir
        Log.d(TAG, "onCreate: Internal cache : "+path)

        var path1: String = externalCacheDir!!.absolutePath    // for external cache dir
        Log.d(TAG, "onCreate: External cache : "+path1)



        Log.d(TAG, "onCreate: "+file.absolutePath)

        var editText = findViewById<EditText>(R.id.editText)
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)
        tv_cache  = findViewById(R.id.tv_cache)
        var btn_c_public = findViewById<Button>(R.id.btn_c_public)
        var btn_r_public = findViewById<Button>(R.id.btn_r_public)
        var btn_c_private = findViewById<Button>(R.id.btn_c_private)
        var btn_r_private = findViewById<Button>(R.id.btn_r_private)
        var btn_create = findViewById<Button>(R.id.btn_create)
        var btn_read = findViewById<Button>(R.id.btn_read)

        btn_create.setOnClickListener {
            var file: File = File(externalCacheDir,FILE_NAME)
            writeToFile(file)
        }

        btn_read.setOnClickListener {
            Thread(Runnable {
                var fr: FileReader? = null
                var myExternalFile: File = File(externalCacheDir, FILE_NAME)
                var stringBuilder: StringBuilder = StringBuilder()
                try {
                    fr = FileReader(myExternalFile)
                    var br: BufferedReader = BufferedReader(fr)
                    var line: String = br.readLine()
                    for (word in line) {
                        stringBuilder.append(word)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                runOnUiThread(Runnable {
                    tv_cache!!.setText(stringBuilder)
                })
            }).start()
        }

        btn_c_public.setOnClickListener {
            if (checkPermission()) {
                var file: File = File(getExternalStorageDirectory(), FILE_NAME)
                writeToFile(file)
                Toast.makeText(this,"Data is written to your file...",Toast.LENGTH_SHORT).show()
            }
            else{
                checkPermission()
            }
        }

        btn_r_public.setOnClickListener {
            if (checkPermission()){
                Thread(Runnable {
                    var fr: FileReader? = null
                    var myExternalFile: File = File(getExternalStorageDirectory(), FILE_NAME)
                    var stringBuilder: StringBuilder = StringBuilder()
                    try {
                        fr = FileReader(myExternalFile)
                        var br: BufferedReader = BufferedReader(fr)
                        var line: String = br.readLine()
                        for (word in line) {
                            stringBuilder.append(word)
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    runOnUiThread(Runnable {
                        textView1!!.setText(stringBuilder)
                    })
                }).start()
            }

        }

        btn_c_private.setOnClickListener {
            var file: File = File(getExternalFilesDir(null), FILE_NAME)
            writeToFile(file)
        }

        btn_r_private.setOnClickListener {
            Thread(Runnable {
                var fr: FileReader? = null
                var myExternalFile: File = File(getExternalFilesDir(null), FILE_NAME)
                var stringBuilder: StringBuilder = StringBuilder()
                try {
                    fr = FileReader(myExternalFile)
                    var br: BufferedReader = BufferedReader(fr)
                    var line: String = br.readLine()
                    for (word in line) {
                        stringBuilder.append(word)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                runOnUiThread(Runnable {
                    textView2!!.setText(stringBuilder)
                })
            }).start()
        }

    }

    fun writeToFile(file: File) {
        editText = findViewById(R.id.editText)
        var data: String = editText.text.toString()

        Log.d(TAG, "writeToFile1: "+data)

        var inputStream: FileOutputStream? = null

        try {
            inputStream = FileOutputStream(file)
            inputStream.write(data.toByteArray())
            Log.d(TAG, "writeToFile2: " + file.name + " " + file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            try {
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isExternalStorageWritable(): Boolean { //It check for external storage is avaliable for read and write
        var state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(state)
    }

    fun isExternalStorageReadable(): Boolean { //It check for external storage is avaliable for read
        var state: String = Environment.getExternalStorageState()
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(
            state
        ))
    }

    fun checkPermission(): Boolean {
        if (!isExternalStorageWritable() || !isExternalStorageReadable()) {
            Toast.makeText(
                this,
                "This app only works for devices with usable external storage",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            var permissionCheck: Int = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_WRITE
                )
                return false
            } else {
                return true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_WRITE ->
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true
                    Toast.makeText(this,"External storage permission granted",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}