package com.yongsu.floproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yongsu.floproject.R
import com.yongsu.floproject.databinding.FragmentMusicFileBinding
import com.yongsu.floproject.databinding.FragmentSavedSongBinding

class MusicFileFragment : Fragment() {

    private var _binding: FragmentMusicFileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMusicFileBinding.inflate(inflater, container, false)

        return binding.root
    }

}