package com.waseem.foodapp.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hmbl.battery.charginganimation.ui.dashboard.download.DownloadAdapter
import com.waseem.foodapp.R
import com.waseem.foodapp.databinding.FragmentDownloadBinding
import com.waseem.foodapp.utils.Constants
import com.waseem.foodapp.utils.Constants.fetchVideosFromCacheFolder
import com.waseem.foodapp.utils.hide
import com.waseem.foodapp.utils.visible
import kotlinx.coroutines.launch


class DownloadFragment : Fragment() {

    private lateinit var mDownloadAdapter: DownloadAdapter
    private lateinit var binding: FragmentDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Constants.sendEvent("DownloadFragment")
        initRecyclerview()
        viewLifecycleOwner.lifecycleScope.launch {
            activity?.let {activity ->
                val list = fetchVideosFromCacheFolder(activity,Constants.bgImages)
                if (list.isEmpty()){
                 binding.emptyLT.visible()
                }else {
                    binding.emptyLT.hide()
                    mDownloadAdapter.submitList(list)
                }
            }
        }
        binding.apply {
            toolbar.title.text = context?.resources?.getString(R.string.download)
            toolbar.backBtn.hide()
        }
    }
    private fun initRecyclerview() {
        mDownloadAdapter = DownloadAdapter(callback = {
//            val action = DownloadFragmentDirections.actionDownloadFragmentToPreviewFragment(null,it.absolutePath,"Dash_item_click")
//            findNavController().navigate(action)
        })
        val manager = GridLayoutManager(context, 2)

        binding.recycleView.apply {
            layoutManager = manager
            adapter = mDownloadAdapter
        }
    }
}