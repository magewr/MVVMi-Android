package com.magewr.mvvmi.scenes.main.viewmodel

import com.magewr.mvvmi.bases.RxViewModel
import com.magewr.mvvmi.bases.RxViewModelProtocol
import com.magewr.mvvmi.interactors.Quotes.QuotesInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class QuotesViewModel() : RxViewModel() {
    class Input(
        var getRandomQuotes: Subject<Unit>
    ): RxViewModelProtocol.Input()

    class Output(
        var getRandomQuotesResult: Observable<String>,
        var error: Observable<String>
    ): RxViewModelProtocol.Output()

    class Dependency(
        var quotesInteractor: QuotesInteractor
    ): RxViewModelProtocol.Dependency()

    override lateinit var input: Input
    override lateinit var output: Output
    override lateinit var dependency: Dependency

    private var getRandomQuotesSubject = PublishSubject.create<Unit>()
    private var getRandomQuotesResultSubject = PublishSubject.create<String>()
    private var errorSubject = PublishSubject.create<String>()

    constructor(dependency: Dependency) : this() {
        this.dependency = dependency
        this.input = Input(getRandomQuotes = this.getRandomQuotesSubject)
        this.output = Output(getRandomQuotesResult = this.getRandomQuotesResultSubject
            , error = this.errorSubject)

        bindInputs()
        bindOutputs()
    }

    private fun bindInputs() {
        getRandomQuotesSubject
            .subscribeOn(io())
            .flatMapSingle { dependency.quotesInteractor.requestRandomQuotes() }
            .map{ it.en }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ quotes ->
                getRandomQuotesResultSubject.onNext(quotes)
            }, { error ->
                errorSubject.onNext(error.localizedMessage)
            })
            .apply { disposeBag.add(this) }
    }

    private fun bindOutputs() {

    }
}