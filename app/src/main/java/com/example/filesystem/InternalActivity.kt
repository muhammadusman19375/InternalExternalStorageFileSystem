package com.example.filesystem

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.*

class MainActivity : AppCompatActivity() {
    lateinit var et_text: EditText
    lateinit var output_screen: TextView
    lateinit var btnRead: Button
    lateinit var btnDelete: Button
    lateinit var btnCreate: Button
    lateinit var btnFileList: Button
    lateinit var btnCreateDir: Button
    lateinit var btnReadDir: Button
    lateinit var image_show: ImageView
    var FILE_NAME:String = "mytextfile"
    var IMAGE_NAME:String = "minarepakistan"
    var DELETE_FILE_NAME: String = "mytextfile"
    var DIRECTORY_NAME: String = "myDirectory"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_text = findViewById(R.id.et_text)
        output_screen = findViewById(R.id.tv_output)
        btnCreate = findViewById(R.id.btn_create)
        btnDelete = findViewById(R.id.btn_delete)
        btnRead = findViewById(R.id.btn_read)
        btnFileList = findViewById(R.id.btn_file_list)
        btnCreateDir = findViewById(R.id.btn_create_dir)
        btnReadDir = findViewById(R.id.btn_read_dir)
        image_show = findViewById(R.id.img)


        var path: String = filesDir.absolutePath
        output_screen.setText(path)

        btnRead.setOnClickListener {

            var bitmap: Bitmap? = null
            var inputStream: InputStream? = null

            inputStream = openFileInput(IMAGE_NAME+".jpg")
            try {
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            image_show.setImageBitmap(bitmap)


//
//            var fileInputStream: FileInputStream? = null
//            fileInputStream = openFileInput(FILE_NAME)
//            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
//            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
//            val stringBuilder: StringBuilder = StringBuilder()
//            var text: String? = null
//            while ({ text = bufferedReader.readLine(); text }() != null) {
//                stringBuilder.append(text)
//            }
//            output_screen.setText(stringBuilder.toString()).toString()

        }

        btnCreate.setOnClickListener {
//............................we can also read it as in drawable..........................................
//            var drawable: BitmapDrawable = getDrawable(R.drawable.minarepakistan) as BitmapDrawable
//........................................................................................................
//
//            var data:Bitmap? = getImage()
//            var outputStream:FileOutputStream? = null
//            try {
//                outputStream = openFileOutput(IMAGE_NAME+".jpg", MODE_PRIVATE)
//                data!!.compress(Bitmap.CompressFormat.JPEG,50,outputStream)
//                output_screen.setText("Image written")
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            }finally {
//                if (outputStream != null){
//                    outputStream.close()
//                }
//            }








            var data:String = et_text.text.toString()
            var outputStream:FileOutputStream? = null
            try {
                outputStream = openFileOutput(FILE_NAME, MODE_PRIVATE)
                outputStream.write(data.toByteArray())
                outputStream.flush()
                output_screen.setText("File written")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }finally {
                if (outputStream != null){
                    outputStream.close()
                }
            }



        }

        btnFileList.setOnClickListener {
            var file_list: Array<out String>? = fileList()
            for (fileName:String in file_list!!){
                output_screen.append(fileName+"  ")
            }
        }

        btnDelete.setOnClickListener {
            var deleteFile: Boolean = deleteFile(DELETE_FILE_NAME)
            Toast.makeText(this,"Deleted "+deleteFile,Toast.LENGTH_SHORT).show()
        }

        btnCreateDir.setOnClickListener {
            var path: File = getDir(DIRECTORY_NAME, MODE_PRIVATE)
            var file: File = File(path,"abc.txt")
            var data: String = "My name is Muhammad Usman"
             var outputStream: OutputStream
            try {
                outputStream = FileOutputStream(data)
                outputStream.write(data.toByteArray())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        btnReadDir.setOnClickListener {
            var path: File = getDir(DIRECTORY_NAME, MODE_PRIVATE)
            var file: File = File(path,"abc.txt")


            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput("abc.txt")
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            output_screen.setText(stringBuilder.toString()).toString()




//            if(file.exists()){
//                Toast.makeText(this,"File exists",Toast.LENGTH_SHORT).show()
//            }
//            else{
//                Toast.makeText(this,"File not exists",Toast.LENGTH_SHORT).show()
//            }
        }

    }

    private fun getImage(): Bitmap? {
        var image: Bitmap? = null
        try {
            var inputStream:InputStream = assets.open("minarepakistan.jpg")
            image = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return image
    }
}