package com.waseem.foodapp.utils

import android.R
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
fun Context.getBatteryPercentage(): Int {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        registerReceiver(null, ifilter)
    }
    val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level != -1 && scale != -1) {
        (level.toFloat() / scale.toFloat() * 100).toInt()
    } else {
        0
    }
}
fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}
fun Activity.isDefaultDialer(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = getSystemService(RoleManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && roleManager?.isRoleAvailable(
                RoleManager.ROLE_DIALER) == true) {
            return roleManager.isRoleHeld(RoleManager.ROLE_DIALER)
        }
    }
    return false
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



fun String.removeWhitespaces() = replace(" ", "")
fun String.removeSpecialCharacter() = replace("[^A-Za-z0-9 ]".toRegex(), "")



fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
fun Context.showViewWithAnim(view: View, anim: Int) {
    CoroutineScope(Dispatchers.Main).launch {
        view.isEnabled = false
        view.visible()
        val slideDown = AnimationUtils.loadAnimation(this@showViewWithAnim, anim)
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                view.isEnabled = true
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
        view.startAnimation(slideDown)
    }
}

fun Context.hideViewWithAnim(view: View, anim: Int) {
    view.isEnabled = false
    CoroutineScope(Dispatchers.Main).launch {
        val slideDown = AnimationUtils.loadAnimation(this@hideViewWithAnim, anim)
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                view.isEnabled = true
                view.inVisible()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        view.startAnimation(slideDown)
    }
}

//fun  Context?.loadImageWithGlide(
//    path: String?,
//    imageView: ImageView,
//) {
//    kotlin.runCatching {
//        this?.let {
//            Glide.with(it).load(Uri.parse(path)).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .apply(
//                    RequestOptions().dontTransform().format(DecodeFormat.PREFER_RGB_565)
//                        .override(Target.SIZE_ORIGINAL)
//                ).listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
//                        imageView.setImageResource(com.hmbl.color.call.wallpaper.cool.R.drawable.baseline_image)
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        model: Any,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        return false
//                    }
//                }).into(imageView)
//        }
//    }
//}


fun NavController.safeNavigate(directions: NavDirections) {
    try {
        currentDestination?.getAction(directions.actionId) ?: return
        navigate(directions.actionId, directions.arguments, null)
    } catch (e : Exception) {
        Log.e("safeNavigate","Navigation error", e)
    }
}

fun NavController.safeNavigate(directions: NavDirections, navOptions: NavOptions?) {
    try {
        currentDestination?.getAction(directions.actionId) ?: return
        navigate(directions.actionId, directions.arguments, navOptions)
    } catch (e : Exception) {
        Log.e("safeNavigate","Navigation error", e)
    }
}

fun NavController.safeNavigate(directions: NavDirections, navigatorExtras: Navigator.Extras) {
    try {
        currentDestination?.getAction(directions.actionId) ?: return
        navigate(directions.actionId, directions.arguments, null, navigatorExtras)
    } catch (e : Exception) {
        Log.e("safeNavigate","Navigation error", e)
    }
}

fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


//Open application settings
fun Context.showSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
fun Context.permissionsDialoge() {
    MaterialAlertDialogBuilder(this).setTitle("Permission Required")
        .setMessage("Write External Storage permissions are required for this purpose")
        .setCancelable(false).setNegativeButton("Deny") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(
                this,
                "Write External Storage permissions are required for this purpose",
                Toast.LENGTH_SHORT
            ).show()
        }.setPositiveButton("Grant") { _, _ ->
            showSettings()
        }.show()
}
fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.inVisible(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.visible(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}
// Function to hide NavigationBar
@RequiresApi(Build.VERSION_CODES.R)
fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(
        window,
        window.decorView.findViewById(R.id.content)
    ).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

}
