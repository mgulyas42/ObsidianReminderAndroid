package com.example.obsidianreminderandroid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File


class MainActivity : AppCompatActivity() {
  @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
          applicationContext.stopService(Intent(this, ObserverService::class.java))
          // There are no request codes
          val data: Intent? = result.data
          val serviceIntent = Intent(this, ObserverService::class.java)
          serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
          if (data != null && data.data != null) {
            getPermissionToFileSystem();

            val uri: Uri = data.data!!
            val file = uri.path.let { File(it) } //create path from uri
            val split: List<String> = file.path.split(":")  //split the path.

            serviceIntent.putExtra("dataUri", Environment.getExternalStorageDirectory().toString() + "/" + split[1])
          };
          ContextCompat.startForegroundService(this, serviceIntent)
          verifyStoragePermissions(this)
          println(data);
        }
      }
      val button: Button = findViewById<View>(R.id.button_first) as Button
      button.setOnClickListener(View.OnClickListener {
        val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
        fileIntent.type = "*/*"
        //startActivityForResult(fileIntent, PICKFILE_REQUEST_CODE)
        resultLauncher.launch(fileIntent);

      })
    }

  fun verifyStoragePermissions(activity: Activity) {
    // Check if we have write permission
    val permission: Int =
      ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
    val permissions = arrayOf<String>(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.MANAGE_DOCUMENTS,
      Manifest.permission.MANAGE_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(
        activity,
        permissions,
        1
      )
    }
  }

  @RequiresApi(Build.VERSION_CODES.R)
  private fun getPermissionToFileSystem() {
    // Maybe we can remove this permission if we can read file content without it
    if(!Environment.isExternalStorageManager()) {
      val intent = Intent()
      intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
      val uri: Uri = Uri.fromParts("package", this.packageName, null)
      intent.data = uri
      startActivity(intent);
    }
  }

}
