package com.magewr.gitusersearch.ui.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magewr.gitusersearch.R
import com.magewr.gitusersearch.ui.main.model.Users
import com.magewr.gitusersearch.ui.main.view.viewholder.UserViewHolder
import io.reactivex.rxjava3.core.Observer

class UserListAdapter(var favoriteCallBack: Observer<Users>) : RecyclerView.Adapter<UserViewHolder>() {
    var userList: List<Users>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (userList == null)
            return
        holder.render(userList!![position], favoriteCallBack)
    }
}