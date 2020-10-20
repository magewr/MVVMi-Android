package com.magewr.mvvmi.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.magewr.mvvmi.R
import com.magewr.mvvmi.databinding.FragmentUserListBinding
import com.magewr.mvvmi.ui.main.view.adapter.UserListAdapter
import com.magewr.mvvmi.ui.main.viewmodel.UserListViewModel

class UserListFragment : Fragment() {
    companion object {
        fun newInstance(title: String, viewModel: UserListViewModel) : UserListFragment {
            val instance = UserListFragment()
            instance.title = title
            instance.viewModel = viewModel
            return instance
        }
    }

    lateinit var bnd: FragmentUserListBinding
    lateinit var title: String
    lateinit var viewModel: UserListViewModel
    lateinit var adapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecyclerView()

        viewModel.output.getUsersResult
            .subscribe {
                adapter.userList = it.items
                adapter.notifyDataSetChanged()
            }


    }

    private fun createRecyclerView() {
        adapter = UserListAdapter()
        bnd.listUser.layoutManager = LinearLayoutManager(context)
        bnd.listUser.adapter = adapter
    }

}