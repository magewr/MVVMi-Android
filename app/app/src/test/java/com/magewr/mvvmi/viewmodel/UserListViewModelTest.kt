package com.magewr.mvvmi.viewmodel

import com.magewr.mvvmi.RxTest
import com.magewr.mvvmi.interactors.favoriteusers.FavoriteUsersInteractor
import com.magewr.mvvmi.interactors.searchusers.SearchUsersInteractor
import com.magewr.mvvmi.interactors.searchusers.SearchUsersParam
import com.magewr.mvvmi.ui.main.model.SearchUsersResultModel
import com.magewr.mvvmi.ui.main.model.Users
import com.magewr.mvvmi.ui.main.viewmodel.MainViewModel
import com.magewr.mvvmi.ui.main.viewmodel.UserListViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class UserListViewModelTest: RxTest() {
    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var searchInteractor: SearchUsersInteractor
    @Mock lateinit var favoriteInteractor: FavoriteUsersInteractor

    private lateinit var testViewModel: UserListViewModel
    private lateinit var usersResult: SearchUsersResultModel

    @Before
    fun setUp() {
        val userList = ArrayList<Users>()
        userList.add(
            Users("magewr", 32250713, "MDQ6VXNlcjMyMjUwNzEz", "https://avatars0.githubusercontent.com/u/32250713?v=4", "",
                "https://api.github.com/users/magewr",
                "https://github.com/magewr",
                "https://api.github.com/users/magewr/followers",
                "https://api.github.com/users/magewr/following{/other_user}",
                "https://api.github.com/users/magewr/gists{/gist_id}",
                "https://api.github.com/users/magewr/starred{/owner}{/repo}",
                "https://api.github.com/users/magewr/subscriptions",
                "https://api.github.com/users/magewr/orgs",
                1)
        )
        usersResult = SearchUsersResultModel(1, false, userList)

        val param = SearchUsersParam("magewr", null, null, 1, 10)
        Mockito.`when`(searchInteractor.getSearchUsers(param)).thenReturn(Single.just(usersResult))
        Mockito.`when`(favoriteInteractor.subject).thenReturn(PublishSubject.create())

        testViewModel = UserListViewModel(UserListViewModel.Dependency(searchInteractor, favoriteInteractor))
    }

    @Test
    fun getUserListTest() {
        val testObserver = TestObserver<SearchUsersResultModel>()
        testViewModel.output.getUsersResult.subscribe(testObserver)
        testViewModel.input.queryChanged.onNext("magewr")

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result.items[0].login, `is`("magewr"))
    }
}