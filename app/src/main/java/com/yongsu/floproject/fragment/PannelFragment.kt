package com.yongsu.floproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yongsu.floproject.R
import com.yongsu.floproject.databinding.FragmentBannerBinding
import com.yongsu.floproject.databinding.FragmentPannelBinding


class PannelFragment(val imgRes : Int) : Fragment() {

    private var _binding: FragmentPannelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPannelBinding.inflate(inflater, container, false)

        // 이미지 설정
        binding.homePannelBackgroundIv.setImageResource(imgRes)

        return binding.root
    }
}