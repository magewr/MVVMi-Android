package com.magewr.mvvmi.viewmodel

import com.magewr.mvvmi.RxTest
import com.magewr.mvvmi.interactors.searchusers.SearchUsersInteractor
import com.magewr.mvvmi.ui.main.model.SearchUsersResultModel
import com.magewr.mvvmi.ui.main.viewmodel.MainViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class MainViewModelTest: RxTest() {
    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var interactor: SearchUsersInteractor

    private lateinit var testViewModel: MainViewModel

    @Before
    fun setUp() {
        testViewModel = MainViewModel(MainViewModel.Dependency(interactor))
    }

    @Test
    fun getRandomQuotesTest() {
        val quotesResult = SearchUsersResultModel("5aa4511b7832df00040ac9b8", "A beautiful program is like a beautiful theorem: It does the job elegantly.", "Butler Lampson", "5aa4511b7832df00040ac9b8")
        Mockito.`when`(interactor.getSearchUsers()).thenReturn(Single.just(quotesResult))

        val testObserver = TestObserver<String>()
        testViewModel.output.getRandomQuotesResult.subscribe(testObserver)
        testViewModel.input.getRandomQuotes.onNext(Unit)

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result, `is`("A beautiful program is like a beautiful theorem: It does the job elegantly."))

    }
}