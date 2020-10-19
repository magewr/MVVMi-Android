package com.magewr.mvvmi.interactor

import com.magewr.mvvmi.RxTest
import com.magewr.mvvmi.clients.RestClient
import com.magewr.mvvmi.clients.apis.APIQuotes
import com.magewr.mvvmi.interactors.Quotes.QuotesInteractor
import com.magewr.mvvmi.ui.main.model.QuotesResultModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class QuotesInteractorTest: RxTest() {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var client: RestClient<APIQuotes>
    @Mock lateinit var api: APIQuotes

    private lateinit var testInteractor: QuotesInteractor

    @Before
    fun setUp() {
        testInteractor = QuotesInteractor(client)
        Mockito.`when`(client.api).thenReturn(api)
    }

    @Test
    fun getRandomQuotesTest() {
        val quotesResult = QuotesResultModel("5aa4511b7832df00040ac9b8", "A beautiful program is like a beautiful theorem: It does the job elegantly.", "Butler Lampson", "5aa4511b7832df00040ac9b8")
        Mockito.`when`(client.api.getRandomQuotes()).thenReturn(Single.just(quotesResult))

        val result = testInteractor.requestRandomQuotes()

        val testObserver = TestObserver<QuotesResultModel>()
        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.id, `is`("5aa4511b7832df00040ac9b8"))
        assertThat(listResult.en, `is`("A beautiful program is like a beautiful theorem: It does the job elegantly."))
        assertThat(listResult.author, `is`("Butler Lampson"))
        assertThat(listResult.quotesResultModelID, `is`("5aa4511b7832df00040ac9b8"))
    }
}