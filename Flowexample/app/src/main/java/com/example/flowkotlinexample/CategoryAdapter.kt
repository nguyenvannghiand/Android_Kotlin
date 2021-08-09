package com.example.flowkotlinexample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.flowkotlinexample.databinding.ListItemPhotoBinding
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.*

class CategoryAdapter() :
	RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

	private val listCategory: MutableList<Photo> = mutableListOf()

	fun setCategoryList(list: List<Photo>) {
		if (listCategory.size > 0) {
			return
		}
		val previousItemSize = listCategory.size
		listCategory.clear()
		listCategory.addAll(list)
		notifyItemRangeChanged(previousItemSize, list.size)

	}


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding =
			DataBindingUtil.inflate<ListItemPhotoBinding>(
				inflater,
				R.layout.list_item_photo,
				parent,
				false
			)
		return CategoryViewHolder(binding)
	}

	override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
		holder.onBind(listCategory[position], position)
	}

	override fun getItemCount(): Int {
		return listCategory.size
	}


	fun getItem(position: Int): Photo {
		var pos = position
		while (pos >= itemCount && pos > 0) {
			--pos
		}
		return listCategory[pos]
	}


	class CategoryViewHolder(var binding: ListItemPhotoBinding) :
		RecyclerView.ViewHolder(binding.root) {


		fun onBind(cate: Photo, position: Int) {
			binding.photographer.text = cate.name + " --- " + cate.id
			Glide.with(binding.root.context)
				.load("https://wallstorage.net/ringstorage/categories/fr2019tkv2sec/${cate.url}")
				.transition(DrawableTransitionOptions.withCrossFade())
				.into(binding.plantPhoto)
			binding.executePendingBindings()
		}
	}


}
