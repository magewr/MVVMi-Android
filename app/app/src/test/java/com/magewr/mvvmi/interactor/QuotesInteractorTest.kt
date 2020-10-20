package com.magewr.mvvmi.interactor

import com.magewr.mvvmi.RxTest
import com.magewr.mvvmi.clients.RestClient
import com.magewr.mvvmi.clients.apis.APISearch
import com.magewr.mvvmi.interactors.searchusers.SearchUsersInteractor
import com.magewr.mvvmi.interactors.searchusers.SearchUsersParam
import com.magewr.mvvmi.ui.main.model.SearchUsersResultModel
import com.magewr.mvvmi.ui.main.model.Users
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.any
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class QuotesInteractorTest: RxTest() {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var client: RestClient<APISearch>
    @Mock lateinit var api: APISearch

    private lateinit var testInteractor: SearchUsersInteractor

    @Before
    fun setUp() {
        testInteractor = SearchUsersInteractor(client)
        Mockito.`when`(client.api).thenReturn(api)
    }

    @Test
    fun getUsersTest() {
        val userList = ArrayList<Users>()
        userList.add(Users("magewr", 32250713, "MDQ6VXNlcjMyMjUwNzEz", "https://avatars0.githubusercontent.com/u/32250713?v=4", "",
            "https://api.github.com/users/magewr",
            "https://github.com/magewr",
            "https://api.github.com/users/magewr/followers",
            "https://api.github.com/users/magewr/following{/other_user}",
            "https://api.github.com/users/magewr/gists{/gist_id}",
            "https://api.github.com/users/magewr/starred{/owner}{/repo}",
            "https://api.github.com/users/magewr/subscriptions",
            "https://api.github.com/users/magewr/orgs",
            1))
        val usersResult = SearchUsersResultModel(1, false, userList)
        val param = SearchUsersParam("magewr", null, null, 1, 10)
        Mockito.`when`(client.api.getSearchUsers(param.q, param.sort, param.order, param.page, param.per_page)).thenReturn(Single.just(usersResult))

        val result = testInteractor.getSearchUsers(param)

        val testObserver = TestObserver<SearchUsersResultModel>()
        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.items[0].login, `is`("magewr"))
    }
}