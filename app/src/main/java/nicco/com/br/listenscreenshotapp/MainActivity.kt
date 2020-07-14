package nicco.com.br.listenscreenshotapp

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class MainActivity : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener {

    private val screenshotDetectionDelegate =
        ScreenshotDetectionDelegate(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkReadExternalStoragePermission()
    }

    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }

    override fun onScreenCaptured(path: String?) {
        // Do something when screen was captured
        Log.d("ok", "ok")
    }

    override fun onScreenCapturedWithDeniedPermission() {
        // Do something when screen was captured but read external storage permission has denied
        Log.d("nok", "nok")
    }

    private fun checkReadExternalStoragePermission() {
        Dexter.withContext(this).withPermissions(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                Log.d("ok", "ok")
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {

            }
        }).check();
    }
}