package com.waseem.foodapp.utils


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import com.waseem.foodapp.databinding.DiscardDialogLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

object Constants {


    const val REQUEST_CODE_SET_DEFAULT_DIALER = 200
    const val REQUEST_CODE_PERMISSIONS = 101
    const val isSubscription = "isSubscription"
    const val RINGTONE_FILE_PATH = "RINGTONE_FILE_PATH"
    const val CHECK_INTENT_FROM = "CHECK_INTENT_FROM"

    var mCallActionPosition = 0
    var VIEW_PAGER_INDEX = 0



    const val translationYText = 150f
    const val translationY = 150f

    var requestPermissionCallback: ((Int?) -> Unit)? = null
    var grantedPermissionCallback: ((Int?) -> Unit)? = null

    fun interstitialDialog(activity: Activity, adLoadCallback: ((Boolean) -> Unit)) {
        runCatching {
            val dialog = Dialog(activity)
            val binding = DiscardDialogLayoutBinding.inflate(LayoutInflater.from(activity))
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(binding.root)
            val width = (activity.resources?.displayMetrics?.widthPixels?.times(0.9))?.toInt()
            if (width != null) {
                dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            try {
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
            binding.buttonYes.setOnClickListener {
                dialog.dismiss()
                adLoadCallback(true)
            }
        }
    }


    suspend fun fetchImagesFromCacheFolder(context: Context, folderName: String): ArrayList<File?> {
        try {
            return withContext(Dispatchers.IO) {
                val cacheDir = context.cacheDir
                val folderDir = File(cacheDir, folderName)
                println("imageFiles  start  ")
                val imageFiles = ArrayList<File?>()
                if (folderDir.exists() && folderDir.isDirectory) {
                    val files = folderDir.listFiles()
                    files?.let {
                        for (file in files) {
                            if (file.isFile && isImageFile(file)) {
                                imageFiles.add(file)
                            }
                        }
                    }
                }
                println("imageFiles  $imageFiles   ")
                imageFiles
            }
        } catch (exception: Exception) {
            return ArrayList()
        }
    }
    suspend fun fetchVideosFromCacheFolder(context: Context, folderName: String): ArrayList<File> {
        try {
            return withContext(Dispatchers.IO) {
                val cacheDir = context.cacheDir
                val folderDir = File(cacheDir, folderName)
                val videoList = ArrayList<File>()
                if (folderDir.exists() && folderDir.isDirectory) {
                    val files = folderDir.listFiles()
                    files?.let {
                        for (file in files) {
                            if (file.exists() && file.isFile && !file.absolutePath.contains(".temp")) {
                                videoList.add(file)
                            }
                        }
                    }
                }
                println("videoList  $videoList   ")
                videoList
            }
        } catch (exception: Exception) {
            return ArrayList()
        }
    }
    suspend fun fetchRingTonesFromCacheFolder(
        context: Context, folderName: String
    ): ArrayList<File> {
        try {
            return withContext(Dispatchers.IO) {
                val cacheDir = context.cacheDir
                val folderDir = File(cacheDir, folderName)
                val imageFiles = ArrayList<File>()
                if (folderDir.exists() && folderDir.isDirectory) {
                    val files = folderDir.listFiles()
                    files?.let {
                        for (file in files) {
                            if (file.isFile) {
                                imageFiles.add(file)
                            }
                        }
                    }
                }
                println("imageFiles  $imageFiles   ")
                imageFiles
            }
        } catch (exception: Exception) {
            return ArrayList()
        }
    }

    private fun isImageFile(file: File): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, options)

        return options.outWidth != -1 && options.outHeight != -1
    }


    fun sendEvent(event: String) {
//        val analytics: FirebaseAnalytics = Firebase.analytics
//        val params = Bundle()
//        params.putString(FirebaseAnalytics.Param.SCREEN_NAME, "")
//        analytics.logEvent(event, params)
    }

    fun isOnline(context: Context?): Boolean {
        try {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } catch (ignore: NullPointerException) {
            return false
        } catch (ignore: java.lang.NullPointerException) {
            return false
        }
    }


    fun startAnimationUp(view: View) {
        val animationUp = TranslateAnimation(0f, 0f, 30f, -100f)
        animationUp.duration = 1200
        animationUp.interpolator = BounceInterpolator()
        animationUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                startAnimationDown(view)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        view.startAnimation(animationUp)
    }

    fun startAnimationDown(view: View) {
        val animationDown = TranslateAnimation(0f, 0f, -100f, 30f)
        animationDown.duration = 1200
        animationDown.interpolator = BounceInterpolator()
        animationDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Animation start logic if needed
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Start the down animation when the up animation ends
                startAnimationUp(view)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeat logic if needed
            }
        })
        view.startAnimation(animationDown)
    }


    const val bgImages = "bgImages"
//    fun downloadFileSaveToCache(
//        context: Context?,
//        path: String,
//        downloadPath: ((String) -> Unit)
//    ) {
//        freeMemory()
//        val folderDir = File(context?.cacheDir, bgImages)
//        if (!folderDir.exists()) {
//            folderDir.mkdir()
//        }
//        val nameWithEx = getFileNameWithoutExtension(path)
//        PRDownloader.download(path, folderDir.absolutePath, nameWithEx).build()
//            .setOnProgressListener { progress ->
//                val progressPercent = progress.currentBytes * 100 / progress.totalBytes
//            }.start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    val downloadedFilePath = folderDir.absolutePath + "/" + nameWithEx
//                    downloadPath(downloadedFilePath)
//                }
//
//                override fun onError(error: com.downloader.Error?) {
//                    downloadPath("")
//                }
//            })
//    }


    fun getFilePathIfExistsInCache(context: Context?, path: String): String? {
        val folderDir = File(context?.cacheDir, bgImages)
        if (!folderDir.exists()) {
            folderDir.mkdir()
        }
        val nameWithEx = getFileNameWithoutExtension(path)
        val file = File(folderDir, nameWithEx)

        return if (file.exists()) {
            file.absolutePath
        } else {
            null
        }
    }

    private fun getFileNameWithoutExtension(url: String): String {
        val lastIndexOfSlash = url.lastIndexOf("/")
        val fileNameWithExtension = if (lastIndexOfSlash != -1) {
            url.substring(lastIndexOfSlash + 1)
        } else {
            url
        }
        val indexOfQuestionMark = fileNameWithExtension.indexOf("?")
        return if (indexOfQuestionMark != -1) {
            fileNameWithExtension.substring(0, indexOfQuestionMark)
        } else {
            fileNameWithExtension
        }
    }

    fun freeMemory() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                System.runFinalization()
                Runtime.getRuntime().gc()
                System.gc()
            } catch (_: Exception) {
            } catch (_: OutOfMemoryError) {
            }
        }
    }
}
