package com.example.obisidanreminder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat


class SettingsActivity : AppCompatActivity() {
  // var directoryFileObserver: DirectoryFileObserver? = null
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

      //directoryFileObserver = DirectoryFileObserver("/storage/emulated/0/test/test");
      //directoryFileObserver!!.startWatching();
      val serviceIntent = Intent(this, ObserverService::class.java)
      serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
      ContextCompat.startForegroundService(this, serviceIntent)
     verifyStoragePermissions(this)

    val intent = Intent()
    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
    val uri: Uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    startActivity(intent)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

  fun verifyStoragePermissions(activity: Activity) {
    // Check if we have write permission
    val permission: Int =
      ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val permissions = arrayOf<String>(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.MANAGE_DOCUMENTS,
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
}
