package com.yongsu.floproject.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yongsu.floproject.databinding.ItemSongBinding
import com.yongsu.floproject.datas.Album

class SavedSongRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private val switchStatus = SparseBooleanArray()

    interface SavedSongClickListener{
        fun onRemoveSavedSong(position: Int)
    }

    private lateinit var mItemClickListener: SavedSongClickListener

    fun setSavedSongClickListener(itemClickListener: SavedSongClickListener){
        mItemClickListener = itemClickListener
    }

    fun removeSong(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = albumList.size

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])

        holder.binding.itemSongMoreIv.setOnClickListener { mItemClickListener.onRemoveSavedSong(position) }
    }

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemSongTitleTv.text = album.title
            binding.itemSongSingerTv.text = album.singer
            binding.itemSongImgIv.setImageResource(album.coverImg!!)

            binding.switch1.isChecked = switchStatus[adapterPosition]
            binding.switch1.setOnClickListener {
                if (!binding.switch1.isChecked)
                    switchStatus.put(adapterPosition, false)
                else
                    switchStatus.put(adapterPosition, true)
                notifyItemChanged(adapterPosition)
            }
        }
    }
}