//package com.hmbl.battery.charginganimation.ui.dashboard.home
//
//import android.app.Activity
//import android.app.ActivityManager
//import android.app.Dialog
//import android.content.Context
//import android.content.Context.POWER_SERVICE
//import android.content.Intent
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.net.Uri
//import android.os.Bundle
//import android.os.PowerManager
//import android.provider.Settings
//import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.ContextCompat
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.media3.common.MediaItem
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import androidx.recyclerview.widget.GridLayoutManager
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.hmbl.battery.charginganimation.R
//import com.hmbl.battery.charginganimation.ads.MaxInterstitial
//import com.hmbl.battery.charginganimation.ads.NativeMaxAd
//import com.hmbl.battery.charginganimation.ads.NativeType
//import com.hmbl.battery.charginganimation.ads.RewardedMaxAd
//import com.hmbl.battery.charginganimation.databinding.BottomSheetColorsDialogBinding
//import com.hmbl.battery.charginganimation.databinding.BottomSheetPermissionLayoutBinding
//import com.hmbl.battery.charginganimation.databinding.DialogUnlockTemplateLayoutBinding
//import com.hmbl.battery.charginganimation.databinding.FragmentPreviewBinding
//import com.hmbl.battery.charginganimation.models.SavedListModel
//import com.hmbl.battery.charginganimation.models.SavedModel
//import com.hmbl.battery.charginganimation.service.Services
//import com.hmbl.battery.charginganimation.ui.preview.TextColorAdapter
//import com.hmbl.battery.charginganimation.utils.Constants
//import com.hmbl.battery.charginganimation.utils.Constants.COLOR_LIST
//import com.hmbl.battery.charginganimation.utils.Constants.downloadItemDialog
//import com.hmbl.battery.charginganimation.utils.Constants.getFileNameWithoutExtension
//import com.hmbl.battery.charginganimation.utils.SPRepository
//import com.hmbl.battery.charginganimation.utils.SPRepository.getModel
//import com.hmbl.battery.charginganimation.utils.SPRepository.getSavedList
//import com.hmbl.battery.charginganimation.utils.SPRepository.saveModel
//import com.hmbl.battery.charginganimation.utils.SPRepository.updateSavedList
//import com.hmbl.battery.charginganimation.utils.getBatteryPercentage
//import com.hmbl.battery.charginganimation.utils.hide
//import com.hmbl.battery.charginganimation.utils.hideViewWithAnim
//import com.hmbl.battery.charginganimation.utils.inVisible
//import com.hmbl.battery.charginganimation.utils.safeNavigate
//import com.hmbl.battery.charginganimation.utils.showLongToast
//import com.hmbl.battery.charginganimation.utils.showShortToast
//import com.hmbl.battery.charginganimation.utils.showViewWithAnim
//import com.hmbl.battery.charginganimation.utils.visible
//import org.koin.android.ext.android.inject
//
//
//class PreviewFragment : Fragment() {
//    private lateinit var binding: FragmentPreviewBinding
//    private val player: ExoPlayer by inject()
//    private var isViewVisible = true
//    private val args: PreviewFragmentArgs by navArgs()
//    private var mColorBottomSheet: BottomSheetDialog? = null
//    private var mColorSheetBinding: BottomSheetColorsDialogBinding? = null
//    private lateinit var mTextColorAdapter: TextColorAdapter
//    private var mTextColor = "#FFFFFFFF"
//    private var mAdShowedCount = 0
//    private var isPremiumItem = 0
//    private val mMaxAds = 2
//    private var mPermissionBottomSheet: BottomSheetDialog? = null
//    private var mPermissionSheetBinding: BottomSheetPermissionLayoutBinding? = null
//    private var mAnimationPath = ""
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentPreviewBinding.inflate(layoutInflater, container, false)
//        activity?.let {
//            hideBottomNavigationBar(it)
//        }
//        return binding.root
//    }
//
//
//    override fun onStop() {
//        super.onStop()
//        showNavigationBar(requireActivity())
//    }
//
//    private fun hideBottomNavigationBar(activity: Activity) {
//        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//    }
//
//    private fun showNavigationBar(activity: Activity) {
//        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//    }
//
//    private fun setFullscreen(activity: Activity) {
//        var flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN
//        flags =
//            flags or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//        activity.window.decorView.systemUiVisibility = flags
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Constants.sendEvent("PreviewFragment")
//        binding.timeClock.format12Hour = "hh:mm"
//        binding.dayText.text = Constants.getDayAndDate()
//        Log.d("response", "onViewCreated:   ${args.selectedModel}")
//        activity?.let { mActivity ->
//            args.selectedModel.let { model ->
//                isPremiumItem = model?.isPremium ?: 0
//                model?.path1?.let {
//                    val cacheFilePAth = Constants.getFilePathIfExistsInCache(mActivity, it)
//                    if (cacheFilePAth != null) {
//                        initPlayer(cacheFilePAth)
//                    } else {
//                        binding.root.inVisible()
//                        downloadItemDialog(mActivity, it,args.eventName?:"", adLoadCallback = { path ->
//                            binding.root.visible()
//                            if (path.isNotEmpty()) {
//                                initPlayer(path)
//                                context?.updateSavedList(SavedListModel(getFileNameWithoutExtension(it), isPremiumItem))
//                            } else {
//                                mActivity.showShortToast(mActivity.resources.getString(R.string.tryAgain))
//                            }
//                        })
//                    }
//                }
//            }
//            args.downloadedPath?.let { path ->
//                Log.d("response", "onViewCreated: downloadedPath  ${path}")
//                val existModel =
//                    mActivity.getSavedList().find { it.path == getFileNameWithoutExtension(path) }
//                existModel?.let {
//                    isPremiumItem = it.isPremium
//                }
//                Log.d("response", "onViewCreated: downloadedPath existModel  ${existModel}")
//                initPlayer(path)
//                mActivity.getModel()?.let {
//                    if (getFileNameWithoutExtension(mAnimationPath) == it.pathName) {
//                        mTextColor = it.textColor
//                        setTextColors(mTextColor)
//                    }
//                }
//            }
//        }
//        setUpBottomSheets()
//        activity?.let {
//            NativeMaxAd.createNativeAdWithShimmer(
//                it,
//                binding.nativeAdView,
//                binding.nativeShimmerView,
//                NativeType.WITH_MEDIA
//            ) {}
//        }
//    }
//
//    private fun setUpBottomSheets() {
//        context?.let { context ->
//            mColorBottomSheet = BottomSheetDialog(context)
//            mColorSheetBinding =
//                BottomSheetColorsDialogBinding.inflate(LayoutInflater.from(context))
//            mColorSheetBinding?.let { mColorBottomSheet?.setContentView(it.root) }
//
//            mPermissionBottomSheet = BottomSheetDialog(context)
//            mPermissionSheetBinding =
//                BottomSheetPermissionLayoutBinding.inflate(LayoutInflater.from(context))
//            mPermissionSheetBinding?.let { mPermissionBottomSheet?.setContentView(it.root) }
//        }
//        binding.apply {
//            "${context?.getBatteryPercentage() ?: 0}%".also { batteryPercent.text = it }
//            colorBtn.setOnClickListener {
//                Constants.sendEvent("previewBackPress")
//                showColorBottomSheet()
//            }
//            fullViewBtn.setOnClickListener {
//                Constants.sendEvent("preview_fullView")
//                if (topColor.isVisible) {
//                    topColor.hide()
//                    bottomColor.hide()
//                } else {
//                    topColor.visible()
//                    bottomColor.visible()
//                }
//            }
//            toolbar.backBtn.setOnClickListener {
//                Constants.sendEvent("previewBackPress")
//                findNavController().popBackStack()
//            }
//            setButton.setOnClickListener {
//                Constants.sendEvent("applyButton")
//                activity?.let { activity ->
//                    if (isPremiumItem == 1) {
//                        Log.d("response", "onViewCreated:  mAdShowedCount ${mAdShowedCount}")
//                        if (mAdShowedCount < 2) {
//                            unlockTemplate(activity, showAd = { shouldShowAd ->
//                                if (shouldShowAd) {
//                                    RewardedMaxAd.showMaxRewarded(activity,  event = "preview_seeMore_Rewarded", adLoadCallback = {
//                                        mAdShowedCount++
//                                        if (mAdShowedCount > 2) {
//                                            showPermissionBottomSheet(activity)
//                                        }
//                                    })
//                                } else {
//                                    findNavController().navigate(R.id.inAppFragment)
//                                }
//                            })
//                        } else {
//                            showPermissionBottomSheet(activity)
//                        }
//                    } else {
//                        MaxInterstitial.showMaxInterstitial(activity, event = "preview_apply_INT",  adLoadCallback = {
//                            showPermissionBottomSheet(activity)
//                        })
//                    }
//                }
//            }
//            tapLayout.setOnClickListener {
//                Constants.sendEvent("preview_tabClick")
////                showViews()
//                if (isViewVisible) {
//                    hideView()
//                } else {
//                    showViews()
//                }
//            }
//            previewBtn.setOnClickListener {
//                Constants.sendEvent("previewButton")
//                if (isViewVisible) {
//                    hideView()
//                } else {
//                    showViews()
//                }
//            }
//        }
//    }
//
//    private fun showViews() {
//        isViewVisible = true
//        context?.showViewWithAnim(binding.setButton, R.anim.slide_down_from_top)
//        context?.showViewWithAnim(binding.toolbar.root, R.anim.slide_down_from_top)
//        context?.showViewWithAnim(binding.optionLayout, R.anim.slide_right_to_left)
//        context?.showViewWithAnim(binding.adLayout, R.anim.slide_up_from_bottom)
////        binding.tapLayout.hide()
//    }
//
//    private fun hideView() {
//        isViewVisible = false
//        context?.hideViewWithAnim(binding.toolbar.root, R.anim.slide_up)
//        context?.hideViewWithAnim(binding.optionLayout, R.anim.slide_left_to_right)
//        context?.hideViewWithAnim(binding.setButton, R.anim.slide_up)
//        context?.hideViewWithAnim(binding.adLayout, R.anim.slide_down)
////        binding.tapLayout.visible()
//    }
//
//    private fun showColorBottomSheet() {
//        if (mColorBottomSheet?.isShowing == true) {
//            return
//        }
//        var color = "#FFFFFFFF"
//        mColorBottomSheet?.show()
//        mColorSheetBinding?.let { sheetBinding ->
//            activity?.let { activity ->
//                NativeMaxAd.createNativeAdWithShimmer(
//                    activity,
//                    sheetBinding.nativeAdView,
//                    sheetBinding.nativeShimmerView,
//                    NativeType.WITHOUT_MEDIA
//                ) {}
//            }
//            val pos = if (COLOR_LIST.contains(mTextColor)) {
//                COLOR_LIST.indexOf(mTextColor)
//            } else {
//                -1
//            }
//            mTextColorAdapter = TextColorAdapter(pos, COLOR_LIST, callback = {
//                color = it
//            })
//            sheetBinding.colorRecyclerView.apply {
//                adapter = mTextColorAdapter
//                layoutManager = GridLayoutManager(activity, 5)
//            }
//            sheetBinding.setButton.setOnClickListener {
//                Constants.sendEvent("colorSelectBtn")
//                mColorBottomSheet?.dismiss()
//                mTextColor = color
//                setTextColors(mTextColor)
//            }
//        }
//    }
//
//    private fun setTextColors(color: String) {
//        binding.timeClock.setTextColor(Color.parseColor(color))
//        binding.dayText.setTextColor(Color.parseColor(color))
//        binding.batteryPercent.setTextColor(Color.parseColor(color))
//    }
//
//    private fun showPermissionBottomSheet(activity: Activity) {
//        if (mPermissionBottomSheet?.isShowing == true) {
//            return
//        }
//        val isOptimize = checkBatteryOptimize(activity)
//        val isOverlayPerm = Settings.canDrawOverlays(activity)
//        if (isOptimize && isOverlayPerm) {
//            applyAnimation()
//        } else {
//            mPermissionBottomSheet?.show()
//            mPermissionSheetBinding?.let { sheetBinding ->
//                if (isOverlayPerm) {
//                    sheetBinding.overLaySwitch.isChecked = true
//                    sheetBinding.overLaySwitch.isEnabled = false
//                }
//                if (isOptimize) {
//                    sheetBinding.batterySwitch.isChecked = true
//                    sheetBinding.batterySwitch.isEnabled = false
//                } else {
//                    sheetBinding.batterySwitch.isChecked = false
//                    sheetBinding.batterySwitch.isEnabled = true
//                }
//                sheetBinding.batterySwitch.setOnClickListener {
//                    batteryOptimizationOk()
//                }
//                sheetBinding.overLaySwitch.setOnClickListener {
//                    overLayPermissionOk(activity = activity)
//                }
//            }
//        }
//    }
//
//    private fun initPlayer(path: String) {
//        mAnimationPath = path
//        player.apply {
//            val mediaItem = MediaItem.fromUri(path)
//            setMediaItem(mediaItem)
//            prepare()
//            addListener(playerListener)
//        }
//    }
//
//    private fun releasePlayer() {
//        player.apply {
//            playWhenReady = false
//            release()
//        }
////        player = null
//    }
//
//    private fun pause() {
//        player.playWhenReady = false
//    }
//
//    private fun play() {
//        player.playWhenReady = true
//    }
//
//    private fun restartPlayer() {
//        player.seekTo(0)
//        player.playWhenReady = true
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (mPermissionBottomSheet?.isShowing == true) {
//            if (Settings.canDrawOverlays(activity)) {
//                mPermissionSheetBinding?.overLaySwitch?.isChecked = true
//                mPermissionSheetBinding?.overLaySwitch?.isEnabled = false
//            }
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        pause()
//    }
//
//    private val playerListener = object : Player.Listener {
//        override fun onPlaybackStateChanged(playbackState: Int) {
//            super.onPlaybackStateChanged(playbackState)
//            when (playbackState) {
//                Player.STATE_ENDED -> restartPlayer()
//                Player.STATE_READY -> {
//                    binding.playerView.player = player
//                    play()
//                }
//
//                Player.STATE_BUFFERING -> {
//                }
//
//                Player.STATE_IDLE -> {
//                }
//            }
//        }
//    }
//
//    private fun unlockTemplate(activity: Activity, showAd: ((Boolean) -> Unit)) {
//        val dialog = Dialog(activity)
//        val binding = DialogUnlockTemplateLayoutBinding.inflate(LayoutInflater.from(activity))
//        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setCancelable(true)
//        dialog.setContentView(binding.root)
//        val width = (activity.resources?.displayMetrics?.widthPixels?.times(0.9))?.toInt()
//        if (width != null) {
//            dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
//        }
//        dialog.show()
//
//        binding.btnUnlock.setOnClickListener {
//            if (dialog.isShowing) {
//                dialog.dismiss()
//            }
//            showAd(false)
//        }
//        binding.progressBar.progress = mAdShowedCount
//        "${activity.resources.getString(R.string.watch_2_videos)}($mAdShowedCount/$mMaxAds)".also {
//            binding.desc.text = it
//        }
//        "$mAdShowedCount ${activity.resources.getString(R.string.of)} $mMaxAds".also {
//            binding.watchedAdValue.text = it
//        }
//        binding.btnWatchVideo.setOnClickListener {
//            if (dialog.isShowing) {
//                dialog.dismiss()
//
//                showAd(true)
//            }
//        }
//    }
//
//    private fun overLayPermissionOk(activity: Activity) {
//        runCatching {
//            launchForOverLayPermission.launch(
//                Intent(
//                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
//                        "package:${activity.packageName}"
//                    )
//                )
//            )
//        }
//    }
//
//    private var launchForOverLayPermission =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                context?.showLongToast("Permission granted")
//                activity?.let { activity ->
//                    val isOptimize = checkBatteryOptimize(activity)
//                    if (isOptimize) {
//                        applyAnimation()
//                    }
//                }
//            } else {
//                mPermissionSheetBinding?.overLaySwitch?.isChecked = false
//            }
//        }
//    private fun batteryOptimizationOk() {
//        runCatching {
//            val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//            intent.data = Uri.parse("package:${context?.packageName}")
//            launchForBatteryOptimization.launch(intent)
//        }
//    }
//
//    private fun checkBatteryOptimize(activity: Activity): Boolean {
//        val pm = activity.getSystemService(POWER_SERVICE) as PowerManager?
//        return pm?.isIgnoringBatteryOptimizations(activity.packageName) ?: false
//    }
//
//    private var launchForBatteryOptimization =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            activity?.let { activity ->
//                val isOptimize = checkBatteryOptimize(activity)
//                if (isOptimize) {
//                    activity.showLongToast("Permission granted")
//                    val isOverlayPerm = Settings.canDrawOverlays(activity)
//                    if (isOverlayPerm) {
//                        applyAnimation()
//                    }
//                } else {
//                    mPermissionSheetBinding?.batterySwitch?.isChecked = isOptimize
//                }
//            }
//
//        }
//
//    private fun applyAnimation() {
//        activity?.let {
//            SPRepository.setAnimationUrl(it, mAnimationPath)
//            it.saveModel(
//                SavedModel(
//                    pathName = getFileNameWithoutExtension(mAnimationPath),
//                    textColor = mTextColor
//                )
//            )
//            prepareForService(it)
//            showAppliedScreen()
//            Constants.sendEvent("applyAnim")
//        }
//    }
//
//    private fun showAppliedScreen() {
//        binding.optionLayout.hide()
//        binding.setButton.hide()
//        val action = PreviewFragmentDirections.actionPreviewFragmentToAppliedPreviewFragment()
//        findNavController().safeNavigate(action)
//    }
//
//    private fun prepareForService(activity: Activity) {
//        val serviceIntent = Intent(activity, Services::class.java)
//        if (!isServiceRunning(activity, Services::class.java)) {
//            ContextCompat.startForegroundService(activity, serviceIntent)
//        }
//    }
//
//
//    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
//        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
//        val serviceName = serviceClass.name
//
//        for (serviceInfo in runningServices) {
//            if (serviceName == serviceInfo.service.className) {
//                return true
//            }
//        }
//        return false
//    }
//
//}