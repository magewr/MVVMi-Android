package com.magewr.gitusersearch.interactor

import com.magewr.gitusersearch.DummyDataFactory
import com.magewr.gitusersearch.RxTest
import com.magewr.gitusersearch.interactors.favoriteusers.FavoriteUsersInteractor
import com.magewr.gitusersearch.interactors.favoriteusers.LocalFavoriteUsers
import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import com.magewr.gitusersearch.ui.main.model.Users
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class FavoriteUsersInteractorTest : RxTest() {
    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock
    lateinit var localFavoriteUsers: LocalFavoriteUsers

    private lateinit var testInteractor: FavoriteUsersInteractor

    @Before
    fun setUp() {
        testInteractor =
            FavoriteUsersInteractor()
        val map = HashMap<Long, Users>()
        map[32250713] = DummyDataFactory.getUsers()
        Mockito.`when`(localFavoriteUsers.users).thenReturn(map)
    }

    /**
     * 좋아요 정보가 변경되면 옵저버들에게 노티파이 해주어야 한다.
     */
    @Test
    fun toggleFavoriteUserTest() {
        val testObserver = TestObserver<Unit>()
        testInteractor.subject.subscribe(testObserver)

        testInteractor.toggleFavoriteUser(DummyDataFactory.getUsers())

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }
}