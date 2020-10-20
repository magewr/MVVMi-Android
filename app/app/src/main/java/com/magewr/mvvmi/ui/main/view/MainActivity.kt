package com.magewr.mvvmi.ui.main.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.widget.textChangeEvents
import com.jakewharton.rxbinding4.widget.textChanges
import com.magewr.mvvmi.R
import com.magewr.mvvmi.bases.RxActivity
import com.magewr.mvvmi.clients.RestClient
import com.magewr.mvvmi.clients.apis.APISearch
import com.magewr.mvvmi.databinding.ActivityMainBinding
import com.magewr.mvvmi.interactors.favoriteusers.FavoriteUsersInteractor
import com.magewr.mvvmi.interactors.searchusers.LocalUsersInteractor
import com.magewr.mvvmi.interactors.searchusers.SearchUsersInteractor
import com.magewr.mvvmi.ui.main.view.adapter.MainPagerAdapter
import com.magewr.mvvmi.ui.main.viewmodel.MainViewModel
import com.magewr.mvvmi.ui.main.viewmodel.UserListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : RxActivity<MainViewModel>() {
    private lateinit var bnd: ActivityMainBinding
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        viewModel = MainViewModel(MainViewModel.Dependency(SearchUsersInteractor(RestClient(APISearch::class.java))))
        viewModel = MainViewModel(MainViewModel.Dependency())




//        viewModel.input.getRandomQuotes.onNext(Unit)
        createTab()
    }

    private fun createTab() {

        val favoriteUsersInteractor = FavoriteUsersInteractor()
        val apiViewModel = UserListViewModel(UserListViewModel.Dependency(SearchUsersInteractor(RestClient(APISearch::class.java)), favoriteUsersInteractor))
        val localViewModel = UserListViewModel(UserListViewModel.Dependency(LocalUsersInteractor(), favoriteUsersInteractor ))

        val fragmentList = ArrayList<UserListFragment>()
        fragmentList.add(UserListFragment.newInstance("API", apiViewModel))
        fragmentList.add(UserListFragment.newInstance("Favorite", localViewModel))
        pagerAdapter = MainPagerAdapter(this)
        pagerAdapter.fragmentList = fragmentList
        bnd.pager.adapter = pagerAdapter
        tabLayoutMediator = TabLayoutMediator(bnd.tabItem, bnd.pager, true, false) { tab, position -> tab.text = pagerAdapter.fragmentList[position].title }
        tabLayoutMediator.attach()
    }

    override fun bindInputs() {
        bnd.editQuery.textChangeEvents().debounce(200, TimeUnit.MILLISECONDS)
            .map{it.text.toString()}
            .filter{it.isNotEmpty()}
            .subscribe{string -> pagerAdapter.fragmentList.map{it.viewModel.input.queryChanged}.forEach {
                it.onNext(string)
            }}
    }

    override fun bindOutputs() {

    }
}