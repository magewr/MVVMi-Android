package com.magewr.gitusersearch.ui.main.view.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding4.view.clicks
import com.magewr.gitusersearch.R
import com.magewr.gitusersearch.databinding.ItemUserBinding
import com.magewr.gitusersearch.ui.main.model.Users
import io.reactivex.rxjava3.core.Observer

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var bnd: ItemUserBinding = DataBindingUtil.bind(itemView)!!

    fun render(user: Users, favoriteToggleObserver: Observer<Users>) {
        Glide.with(itemView.context)
            .load(user.avatar_url)
            .transition(withCrossFade())
            .apply(RequestOptions().transform(CircleCrop()))
            .error(R.drawable.all_empty_profile)
            .into(bnd.imgUser)

        bnd.txtUserId.text = user.login

        val favoriteResourceId = if (user.isFavorite()) R.drawable.btn_favorited else R.drawable.btn_favorite
        Glide.with(itemView.context)
            .load(favoriteResourceId)
            .into(bnd.imgFavorite)

        itemView.clicks().subscribe{favoriteToggleObserver.onNext(user)}
    }
}