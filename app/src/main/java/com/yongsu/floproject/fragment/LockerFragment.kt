package com.yongsu.floproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yongsu.floproject.MainActivity
import com.yongsu.floproject.adapter.LockerVPAdapter
import com.yongsu.floproject.auth.LoginActivity
import com.yongsu.floproject.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        initViewPager()



        return binding.root
    }

    private fun initViewPager(){
        with(binding){
            val lockerAdapter = LockerVPAdapter(this@LockerFragment)
            lockerContentVp.adapter = lockerAdapter
            TabLayoutMediator(lockerContentTb, lockerContentVp){
                tab, position ->
                tab.text = information[position]
            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun getJwt():Int{
        // activity?를 붙이는 이유는 프래그먼트에서 사용할 때의 방법이기 떄문
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }


    private fun initViews(){
        val jwt: Int = getJwt()
        Log.d("jwtjwt", "$jwt")
        if(jwt == 0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }else{
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                // 로그아웃 진행
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        // jwt라는 키에 해당하는 값을 없앰
        editor.remove("jwt")
        editor.apply()
    }

}