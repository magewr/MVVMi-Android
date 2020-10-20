package com.magewr.mvvmi.ui.main.view.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.magewr.mvvmi.R
import com.magewr.mvvmi.databinding.ItemUserBinding
import com.magewr.mvvmi.ui.main.model.Users

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var bnd: ItemUserBinding = DataBindingUtil.bind(itemView)!!

    fun render(user: Users, isFavorite: Boolean) {
        Glide.with(itemView.context)
            .load(user.avatar_url)
            .transition(withCrossFade())
            .apply(RequestOptions().transform(CircleCrop()))
            .error(R.drawable.all_empty_profile)
            .into(bnd.imgUser)

        bnd.txtUserId.text = user.login

        Glide.with(itemView.context)
            .load(R.drawable.btn_favorite)
            .into(bnd.imgFavorite)
    }
}