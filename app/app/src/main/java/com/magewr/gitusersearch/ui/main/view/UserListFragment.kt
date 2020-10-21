package com.magewr.gitusersearch.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.magewr.gitusersearch.R
import com.magewr.gitusersearch.databinding.FragmentUserListBinding
import com.magewr.gitusersearch.ui.main.view.adapter.UserListAdapter
import com.magewr.gitusersearch.ui.main.viewmodel.UserListViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class UserListFragment : Fragment() {
    companion object {
        fun newInstance(title: String, viewModel: UserListViewModel) : UserListFragment {
            val instance =
                UserListFragment()
            instance.title = title
            instance.viewModel = viewModel
            return instance
        }
    }

    lateinit var bnd: FragmentUserListBinding
    lateinit var title: String
    lateinit var viewModel: UserListViewModel
    lateinit var adapter: UserListAdapter
    private var disposeBag = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecyclerView()

        bindInputs()
        bindOutputs()

    }

    override fun onDestroy() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
        viewModel.deinitialize()
        super.onDestroy()
    }

    private fun bindInputs() {

    }

    private fun bindOutputs() {
        // 리스트 데이터 갱신
        viewModel.output.getUsersResult
            .subscribe {
                adapter.userList = it.items
                adapter.notifyDataSetChanged()
            }
            .apply { disposeBag.add(this) }

        // 좋아요 정보 갱신
        viewModel.output.favoriteChanged
            .subscribe{
                adapter.notifyDataSetChanged()
            }
            .apply { disposeBag.add(this) }

        // 에러 메시지 핸들링
        viewModel.output.error
            .subscribe{
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
            .apply { disposeBag.add(this) }
    }

    private fun createRecyclerView() {
        adapter = UserListAdapter(
            viewModel.input.favoriteToggled
        )
        bnd.listUser.layoutManager = LinearLayoutManager(context)
        bnd.listUser.adapter = adapter
    }

}