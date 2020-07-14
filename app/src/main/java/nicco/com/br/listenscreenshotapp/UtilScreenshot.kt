package nicco.com.br.listenscreenshotapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat

class UtilScreenshot(
    activityWeakReference: Activity ,  listener: ScreenshotDetectionListener
) {
    private var activityWeakReference: Activity = activityWeakReference
    private var listener: ScreenshotDetectionListener? = listener

    fun startScreenshotDetection() {
        activityWeakReference.contentResolver?.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
            contentObserver
            )
    }

    fun stopScreenshotDetection() {
        activityWeakReference.contentResolver?.unregisterContentObserver(contentObserver)
    }

    private val contentObserver: ContentObserver = object : ContentObserver(android.os.Handler()) {
        override fun deliverSelfNotifications(): Boolean {
            return super.deliverSelfNotifications()
        }

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
        }

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            if (isReadExternalStoragePermissionGranted()) {
                val path =
                    getFilePathFromContentResolver(activityWeakReference, uri)
                if (isScreenshotPath(path)) {
                    onScreenCaptured(path)
                }
            } else {
                onScreenCapturedWithDeniedPermission()
            }
        }
    }

    private fun onScreenCaptured(path: String?) {
        if (listener != null) {
            listener!!.onScreenCaptured(path)
        }
    }

    private fun onScreenCapturedWithDeniedPermission() {
        if (listener != null) {
            listener!!.onScreenCapturedWithDeniedPermission()
        }
    }

    private fun isScreenshotPath(path: String?): Boolean {
        return path != null && path.toLowerCase().contains("screenshots")
    }

    private fun getFilePathFromContentResolver(context: Context, uri: Uri?): String? {
        try {
            val cursor: Cursor = uri?.let {
                context.contentResolver.query(
                    it, arrayOf<String?>(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                    ), null, null, null
                )
            }!!
            if (cursor != null && cursor.moveToFirst()) {
                val path: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                cursor.close()
                return path
            }
        } catch (ignored: IllegalStateException) {
        }
        return null
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return activityWeakReference.let {
            ContextCompat.checkSelfPermission(
                it,
                READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    interface ScreenshotDetectionListener {
        fun onScreenCaptured(path: String?)
        fun onScreenCapturedWithDeniedPermission()
    }
}