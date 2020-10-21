package com.magewr.gitusersearch.ui.main.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.widget.textChangeEvents
import com.magewr.gitusersearch.R
import com.magewr.gitusersearch.bases.RxActivity
import com.magewr.gitusersearch.clients.RestClient
import com.magewr.gitusersearch.clients.apis.APISearch
import com.magewr.gitusersearch.databinding.ActivityMainBinding
import com.magewr.gitusersearch.interactors.favoriteusers.FavoriteUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.LocalUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersInteractor
import com.magewr.gitusersearch.ui.main.view.adapter.MainPagerAdapter
import com.magewr.gitusersearch.ui.main.viewmodel.MainViewModel
import com.magewr.gitusersearch.ui.main.viewmodel.UserListViewModel
import java.util.concurrent.TimeUnit

class MainActivity : RxActivity<MainViewModel>() {
    private lateinit var bnd: ActivityMainBinding
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = MainViewModel(
            MainViewModel.Dependency()
        )
        createTab()
    }

    private fun createTab() {

        // 좋아요용 인터렉터는 하나의 객체를 공유
        val favoriteUsersInteractor =
            FavoriteUsersInteractor()

        // API용, Local용 인터렉터를 달리 만들어서 사용, 의존성 제거하여 ViewModel 입장에서는 어떤 인터렉터인지 알 필요 없도록 설계
        val apiViewModel =
            UserListViewModel(
                UserListViewModel.Dependency(
                    SearchUsersInteractor(
                        RestClient(
                            APISearch::class.java
                        )
                    ),
                    favoriteUsersInteractor
                )
            )
        val localViewModel =
            UserListViewModel(
                UserListViewModel.Dependency(
                    LocalUsersInteractor(),
                    favoriteUsersInteractor
                )
            )

        val fragmentList = ArrayList<UserListFragment>()
        // API호출용 프래그먼트는 API Interactor를 주입받은 뷰모델을 사용
        fragmentList.add(
            UserListFragment.newInstance(
                "API",
                apiViewModel
            )
        )
        // Local 호출용 프래그먼트는 Local Interactor를 주입받은 뷰모델을 사용
        fragmentList.add(
            UserListFragment.newInstance(
                "Favorite",
                localViewModel
            )
        )

        pagerAdapter =
            MainPagerAdapter(this)
        pagerAdapter.fragmentList = fragmentList
        bnd.pager.adapter = pagerAdapter
        tabLayoutMediator = TabLayoutMediator(bnd.tabItem, bnd.pager, true, false) { tab, position -> tab.text = pagerAdapter.fragmentList[position].title }
        tabLayoutMediator.attach()
    }

    override fun bindInputs() {
        bnd.editQuery.textChangeEvents().debounce(200, TimeUnit.MILLISECONDS)
            .map{it.text.toString()}
            .subscribe{string ->
                pagerAdapter.fragmentList
                    .map{it.viewModel.input.queryChanged}
                    .forEach { it.onNext(string) }
            }
            .apply { disposeBag.add(this) }
    }

    override fun bindOutputs() {

    }
}