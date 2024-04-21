package com.waseem.foodapp.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.waseem.foodapp.databinding.FragmentHome2Binding
import com.waseem.foodapp.models.Results
import com.waseem.foodapp.utils.Constants
import com.waseem.foodapp.utils.getBatteryPercentage
import com.waseem.foodapp.utils.setOnSingleClickListener
import com.waseem.foodapp.utils.showShortToast
import com.waseem.foodapp.utils.visible
import com.waseem.foodapp.viewModel.AnimViewModel
import com.waseem.foodapp.viewModel.ApiState
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private var currentPage = 1
    private var nextPage = 1
    private var lastPage = 2
    var isApiHit: Boolean = false
    private lateinit var animationAdapter: AnimationAdapter
    private var mAnimationList: ArrayList<Results> = ArrayList()
    private lateinit var binding: FragmentHome2Binding

    //    private val component = Component()
    private val mainViewModel by activityViewModels<AnimViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getNewComment(currentPage.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHome2Binding.inflate(layoutInflater, container, false)
        initRecyclerview()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Constants.sendEvent("HomeFragment")

        getAnimationData()
        clickListeners()
        val value = context?.getBatteryPercentage() ?: 0
//        binding.waveLoadingView.progressValue = value
//        binding.waveLoadingView.centerTitle = "$value%"
        binding.batteryPercentTx.text = "$value%"


    }

    private fun clickListeners() {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        activity?.registerReceiver(broadcastReceiver, intentFilter)

        binding.unlockBtn.setOnSingleClickListener {
            Constants.sendEvent("Dashbard_card_pre")
        }
        binding.seeMoreTx.setOnSingleClickListener {
            Constants.sendEvent("Dashbard_seeMore")
            activity?.let { activity ->

            }
        }
    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.let {
                if (it.action == Intent.ACTION_BATTERY_CHANGED) {
                    val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL
                    // Now you can use the isCharging variable to determine the charging state
                    if (isCharging) {
//                        binding.batteryCharging.visible()
                    } else {
//                        binding.batteryCharging.hide()

                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        animationAdapter = AnimationAdapter(callback = {
//            val action = HomeFragmentDirections.actionHomeFragmentToPreviewFragment(it, null,"Download_item_click")
//            findNavController().safeNavigate(action)
        })
        val manager = GridLayoutManager(context, 1)
        binding.recycleView.apply {
            layoutManager = manager
            adapter = animationAdapter
        }
//        binding.unlockBtn.visibility = View.GONE
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()
                val temp = firstVisibleItemPosition + visibleItemCount
                if (temp != 0 && temp > 4) { // Adjust threshold as needed
                    binding.unlockBtn.visibility = View.GONE
                } else {
                    binding.unlockBtn.visibility = View.VISIBLE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < lastPage && !isApiHit)
                        currentPage++
                    if (!isApiHit && currentPage < 3) {
                        isApiHit = true
                        mainViewModel.getNewComment(currentPage.toString())
                    }
                }
            }
        })
    }

    private fun getAnimationData() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.animationState.collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        println("HomeFragment55   123  Success  ${state.data}")
                        isApiHit = false
                        state.data?.let {
                            mAnimationList.addAll(it.results)
                            updateList(it.results)
                        }
                        hideLoading(binding.centerAnimation)
                    }

                    is ApiState.Failure -> {
                        println("HomeFragment55   135  Failure  ${state.msg}")
                        isApiHit = false
                        context?.showShortToast(state.msg.message ?: "Try Again")
                        hideLoading(binding.centerAnimation)
                    }

                    is ApiState.Loading -> {
                        binding.centerAnimation.visible()
                        println("HomeFragment55   142  Loading")
                    }
                    is ApiState.Empty -> {
//                        hideLoading(binding.centerAnimation)
//                        if (mAnimationList.isNotEmpty()){
//                            updateList(mAnimationList)
//                        }else {
////                            component.mainViewModel.getBus(currentPage.toString())
//                        }
                        println("HomeFragment55   174  Empty")
                    }
                }
            }
        }
    }

    private fun updateList(list: ArrayList<Results>) {
        val oldList = animationAdapter.currentList
        val newList = oldList.toMutableList() // Create a mutable copy
        newList.addAll(list) // Add new items
        val uniqueList = newList.distinct() // Remove duplicates while maintaining order
        animationAdapter.submitList(uniqueList.toMutableList())
    }


    private fun hideLoading(loaderAnimation: CircularProgressIndicator) {
        if (loaderAnimation.isVisible) {
            loaderAnimation.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding.unlockBtn.visibility = View.GONE
        println("HomeFragment55   174  onDestroyView")
    }
}