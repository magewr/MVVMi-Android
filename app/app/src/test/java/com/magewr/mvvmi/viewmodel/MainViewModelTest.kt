package com.magewr.mvvmi.viewmodel

import com.magewr.mvvmi.RxTest
import com.magewr.mvvmi.interactors.Quotes.QuotesInteractor
import com.magewr.mvvmi.ui.main.model.QuotesResultModel
import com.magewr.mvvmi.ui.main.viewmodel.MainViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import java.util.concurrent.TimeUnit

class MainViewModelTest: RxTest() {
    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var interactor: QuotesInteractor

    private lateinit var testViewModel: MainViewModel

    @Before
    fun setUp() {
        testViewModel = MainViewModel(MainViewModel.Dependency(interactor))
    }

    @Test
    fun getRandomQuotesTest() {
        val quotesResult = QuotesResultModel("5aa4511b7832df00040ac9b8", "A beautiful program is like a beautiful theorem: It does the job elegantly.", "Butler Lampson", "5aa4511b7832df00040ac9b8")
        Mockito.`when`(interactor.requestRandomQuotes()).thenReturn(Single.just(quotesResult))

        val testObserver = TestObserver<String>()
        testViewModel.output.getRandomQuotesResult.subscribe(testObserver)
        testViewModel.input.getRandomQuotes.onNext(Unit)

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result, `is`("A beautiful program is like a beautiful theorem: It does the job elegantly."))

    }

    @Test
    fun getRandomQuotesErrorTest() {
        val someError = IllegalStateException("Parsing Error")
        Mockito.`when`(interactor.requestRandomQuotes()).thenReturn(Single.error(someError))

        val testObserver = TestObserver<String>()
        testViewModel.output.error.subscribe(testObserver)
        testViewModel.input.getRandomQuotes.onNext(Unit)

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result, `is`("Parsing Error"))

    }
}