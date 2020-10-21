package com.magewr.mvvmi.ui.main.viewmodel

import com.magewr.mvvmi.bases.RxViewModel
import com.magewr.mvvmi.bases.RxViewModelInterface
import com.magewr.mvvmi.interactors.Quotes.QuotesInteractorInterface
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject

class MainViewModel() : RxViewModel() {
    class Input(
        var getRandomQuotes: Observer<Unit>
    ): RxViewModelInterface.Input()

    class Output(
        var getRandomQuotesResult: Observable<String>,
        var error: Observable<String>
    ): RxViewModelInterface.Output()

    class Dependency(
        var quotesInteractor: QuotesInteractorInterface
    ): RxViewModelInterface.Dependency()

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