package com.yongsu.floproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yongsu.floproject.databinding.FragmentHomeBinding
import com.yongsu.floproject.databinding.FragmentLookBinding

class LookFragment : Fragment() {

    private var _binding: FragmentLookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLookBinding.inflate(inflater, container, false)

        return binding.root
    }
}