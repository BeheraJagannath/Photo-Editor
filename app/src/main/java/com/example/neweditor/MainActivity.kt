package com.example.neweditor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        runtimePermission()
    }

    @SuppressLint("NewApi")
    private fun runtimePermission() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
//            startApp()

        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                    startApp()
//
//                }
//            } else {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
//                    )
//                } else {
//                    val builder = AlertDialog.Builder(this)
//                    builder.setMessage("""For Know weather forecast you must need to access the Location for performing necessary task .
//                            Please permit the permission through Settings screen.
//                Select Permissions -> Enable permission
//                """.trimIndent())
//                    builder.setCancelable(false)
//                    builder.setPositiveButton("Permit Manually") {
//                            dialog, which ->
//                        dialog.dismiss()
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        val uri = Uri.fromParts("package", packageName, null)
//                        intent.data = uri
//                        startActivity(intent)
//                    }
//                    builder.setNegativeButton("Cancel", null)
//                    builder.show()
//                }
//            }
//        }
//    }

}