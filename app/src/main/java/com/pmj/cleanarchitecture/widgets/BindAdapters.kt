package com.pmj.cleanarchitecture.widgets

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.pmj.cleanarchitecture.R

@BindingAdapter("imageUrl", requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url)
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_error)
        .into(imageView)
}