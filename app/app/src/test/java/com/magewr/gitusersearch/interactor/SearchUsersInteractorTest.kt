package com.magewr.gitusersearch.interactor

import com.magewr.gitusersearch.DummyDataFactory
import com.magewr.gitusersearch.RxTest
import com.magewr.gitusersearch.clients.RestClient
import com.magewr.gitusersearch.clients.apis.APISearch
import com.magewr.gitusersearch.interactors.searchusers.LocalUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersParam
import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import com.magewr.gitusersearch.ui.main.model.Users
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

class SearchUsersInteractorTest: RxTest() {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var client: RestClient<APISearch>
    @Mock lateinit var api: APISearch

    private lateinit var testInteractor: SearchUsersInteractor
    private lateinit var localTestInteractor: LocalUsersInteractor

    @Before
    fun setUp() {
        testInteractor =
            SearchUsersInteractor(
                client
            )
        localTestInteractor =
            LocalUsersInteractor()
        Mockito.`when`(client.api).thenReturn(api)
    }

    /**
     * API용 인터렉터에서는 요청이 들어오면 서버로부터 데이터를 받아서 보여줘야 한다.
     * API 결과는 목으로 제공되며 이 결과를 그대로 보여줘야 한다.
     */
    @Test
    fun apiGetUsersTest() {
        val param =
            SearchUsersParam(
                "magewr",
                null,
                null,
                1,
                10
            )
        Mockito.`when`(client.api.getSearchUsers(param.q, param.sort, param.order, param.page, param.per_page)).thenReturn(Single.just(
            DummyDataFactory.getResultModel()))

        val result = testInteractor.getSearchUsers(param)

        val testObserver = TestObserver<SearchUsersResultModel>()
        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.items[0].login, `is`("magewr"))
    }

    /**
     * 로컬용 인터렉터에서는 요청이들어오면 실제 로컬 데이터에서 결과를 보여줘야 한다.
     * 로컬에 좋아요 정보가 없으므로 결과값이 비어있어야 한다.
     */
    @Test
    fun localGetUsersTest() {
        val param =
            SearchUsersParam(
                "magewr",
                null,
                null,
                1,
                10
            )

        val result = localTestInteractor.getSearchUsers(param)

        val testObserver = TestObserver<SearchUsersResultModel>()
        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.items.size, `is`(0))
    }

    /**
     * API용 인터렉터에서는 좋아요 변경사항이 목록에는 변화가 없어야 한다.
     * 호출되어도 결과가 무시되어야 한다.
     */
    @Test
    fun apiFavoriteChangeTest() {
        val result = testInteractor.favoriteUpdated()

        val testObserver = TestObserver<SearchUsersResultModel>()
        result.subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertNoValues()
    }

    /**
     * 로컬용 인터렉터에서는 좋아요 변경사항이 목록에는 변화가 있어야 한다.
     * 호출되면 실제 좋아요 정보에 맞추서 갱신되어야 한다.
     * 현재 좋아요 데이터가 없으므로 값은 비어있어야 한다.
     */
    @Test
    fun localFavoriteChangeTest() {
        val result = localTestInteractor.favoriteUpdated()

        val testObserver = TestObserver<SearchUsersResultModel>()
        result.subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.items.size, `is`(0))
    }
}