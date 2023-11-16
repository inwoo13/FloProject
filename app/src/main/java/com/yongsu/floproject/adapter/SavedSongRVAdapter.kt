package com.yongsu.floproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yongsu.floproject.databinding.ItemSongBinding
import com.yongsu.floproject.roomdb.entity.Song

class SavedSongRVAdapter(): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    //private val switchStatus = SparseBooleanArray()

    interface SavedSongClickListener{
        fun onRemoveSavedSong(songId: Int)
    }

    private lateinit var mItemClickListener: SavedSongClickListener

    fun setMyItemClickListener(itemClickListener: SavedSongClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])

        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSavedSong(songs[position].id)
            removeSongs(position)

        }
    }

    @SuppressLint("NotifyDataSetChanged")   // 모든 노래들을 넣는 함수
    fun addSongs(songs: ArrayList<Song>){
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSongs(position: Int){
        songs.removeAt(position)

        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
            binding.itemSongImgIv.setImageResource(song.coverImg!!)

//            binding.switch1.isChecked = switchStatus[adapterPosition]
//            binding.switch1.setOnClickListener {
//                if (!binding.switch1.isChecked)
//                    switchStatus.put(adapterPosition, false)
//                else
//                    switchStatus.put(adapterPosition, true)
//                notifyItemChanged(adapterPosition)
//            }
        }
    }
}