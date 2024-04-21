package com.waseem.foodapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.waseem.foodapp.ui.dialog.AppOpenDialogBox
private const val LOG_TAG = "MyApplication"
class MyApplication : Application(), Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

//    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null
    private var mLoadingDialog: AppOpenDialogBox? = null

//    companion object {
//        lateinit var realm: Realm
//    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//        appOpenAdManager = AppOpenAdManager()
//        realm = Realm.open(
//            configuration = RealmConfiguration.create(
//                schema = setOf(
//                    FavoriteModel::class
//                )
//            )
//        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let {
//            appOpenAdManager.showAdIfAvailable(it)
        }
        // Show the ad (if available) when the app moves to foreground.
    }

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
//        if (!appOpenAdManager.isShowingAd) {
//            currentActivity = activity
//        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

//    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
//        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
//        // class.
//        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
//    }

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

//    private inner class AppOpenAdManager {
//
//        private var appOpenAd: AppOpenAd? = null
//        private var isLoadingAd = false
//        var isShowingAd = false
//
//        private var loadTime: Long = 0
//        fun loadAd(context: Context) {
//            if (!AppSharedPreferences.getSubscription(this@MyApplication)) {
//                if (currentActivity is AdActivity) {
//                    return
//                }
//                if (isLoadingAd || isAdAvailable()) {
//                    return
//                }
//
//                isLoadingAd = true
//                val request = AdManagerAdRequest.Builder().build()
//                val unitId = getString(R.string.openapp_adsmob_ad_id)
//                AppOpenAd.load(
//                    context,
//                    unitId,
//                    request,
//                    object : AppOpenAd.AppOpenAdLoadCallback() {
//                        override fun onAdLoaded(ad: AppOpenAd) {
//                            appOpenAd = ad
//                            isLoadingAd = false
//                            loadTime = Date().time
//                            Log.d(LOG_TAG, "onAdLoaded.")
//                        }
//
//                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                            isLoadingAd = false
//                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
//                        }
//                    }
//                )
//            } else {
//                return
//            }
//        }
//
//        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
//            val dateDifference: Long = Date().time - loadTime
//            val numMilliSecondsPerHour: Long = 3600000
//            return dateDifference < numMilliSecondsPerHour * numHours
//        }
//
//        private fun isAdAvailable(): Boolean {
//            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
//        }
//
//        fun showAdIfAvailable(activity: Activity) {
//            showAdIfAvailable(
//                activity,
//                object : OnShowAdCompleteListener {
//                    override fun onShowAdComplete() {
//                        // Empty because the user will go back to the activity that shows the ad.
//                    }
//                }
//            )
//        }
//
//        fun showAdIfAvailable(
//            activity: Activity,
//            onShowAdCompleteListener: OnShowAdCompleteListener
//        ) {
//            if (isShowingAd) {
//                Log.d(LOG_TAG, "The app open ad is already showing.")
//                return
//            }
//
//            if (!isAdAvailable()) {
//                Log.d(LOG_TAG, "The app open ad is not ready yet.")
//                onShowAdCompleteListener.onShowAdComplete()
//                dismissDialog()
//                loadAd(activity)
//                return
//            }
//            if (currentActivity is MainActivity || currentActivity is AdActivity || isInterstitialIsShowing) {
//                return
//            }
//            Log.d(LOG_TAG, "Will show ad. myApp")
//            CoroutineScope(Dispatchers.Main).launch {
//                mLoadingDialog = AppOpenDialogBox(activity, 1)
//                if (!(activity).isFinishing) {
//                    mLoadingDialog?.show()
//                    delay(1000)
//                    Log.d(LOG_TAG, "Will show ad.")
//                    isShowingAd = true
//                    appOpenAd?.show(activity)
//                    appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//                        override fun onAdDismissedFullScreenContent() {
//                            dismissDialog()
//                            appOpenAd = null
//                            isShowingAd = false
//                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.")
//                            onShowAdCompleteListener.onShowAdComplete()
//                            loadAd(activity)
//                        }
//
//                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                            dismissDialog()
//                            appOpenAd = null
//                            isShowingAd = false
//                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.message)
//                            onShowAdCompleteListener.onShowAdComplete()
//                            loadAd(activity)
//                        }
//
//                        override fun onAdShowedFullScreenContent() {
//                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.")
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun dismissDialog() {
        Log.d(LOG_TAG, "dismissDialog.")
        try {
            if (mLoadingDialog?.isShowing == true) {
                mLoadingDialog?.dismiss()
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, "dialog.   ${e.message}")
        }
    }
}