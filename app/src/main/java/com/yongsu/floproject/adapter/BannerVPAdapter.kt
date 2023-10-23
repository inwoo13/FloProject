package com.yongsu.floproject.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    // 이 클래스에 연결된 ViewPager에 몇개의 데이터를 전달할 것인지 써주는 함수
//    override fun getItemCount(): Int {
//        return fragmentlist.size
//    }
    override fun getItemCount(): Int = fragmentlist.size

    //count가 4면 0,1,2,3 실행됨
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    // 프래그먼트 리스트에 아무것도 없어서 추가할 수 있도록 하는 함수
    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)  // fragmentlist에 인자로 받은 fragment를 넣어줌
        // 리스트 안에 새로운 값이 들어왔을때 Viewpager에 알려줘야됨
        notifyItemInserted(fragmentlist.size-1)
    }
}