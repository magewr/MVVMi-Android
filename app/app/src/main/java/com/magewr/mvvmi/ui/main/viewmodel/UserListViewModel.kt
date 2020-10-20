package com.magewr.mvvmi.ui.main.viewmodel

import com.magewr.mvvmi.bases.RxViewModel
import com.magewr.mvvmi.bases.RxViewModelProtocol
import com.magewr.mvvmi.interactors.favoriteusers.FavoriteUsersInteractorInterface
import com.magewr.mvvmi.interactors.searchusers.SearchUsersInteractorInterface
import com.magewr.mvvmi.interactors.searchusers.SearchUsersParam
import com.magewr.mvvmi.ui.main.model.SearchUsersResultModel
import com.magewr.mvvmi.ui.main.model.Users
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject

class UserListViewModel(): RxViewModel() {
    class Input(
        var favoriteToggled: Observer<Users>,
        var queryChanged: Observer<String>
    ): RxViewModelProtocol.Input()

    class Output(
        var getUsersResult: Observable<SearchUsersResultModel>,
        var error: Observable<String>
    ): RxViewModelProtocol.Output()

    class Dependency(
        var searchUsersInteractor: SearchUsersInteractorInterface,
        var favoriteUsersInteractor: FavoriteUsersInteractorInterface
    ): RxViewModelProtocol.Dependency()

    override lateinit var input: Input
    override lateinit var output: Output
    override lateinit var dependency: Dependency

    private var favoriteToggledSubject = PublishSubject.create<Users>()
    private var queryChangedSubject = PublishSubject.create<String>()
    private var getUsersResultSubject = PublishSubject.create<SearchUsersResultModel>()
    private var errorSubject = PublishSubject.create<String>()

    constructor(dependency: Dependency) : this() {
        this.dependency = dependency
        this.input = Input(favoriteToggled = favoriteToggledSubject, queryChanged = queryChangedSubject)
        this.output = Output(getUsersResult = getUsersResultSubject, error = errorSubject)

        bindInputs()
        bindOutputs()
    }

    private fun bindInputs() {
        /*
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
         */
        queryChangedSubject
            .subscribeOn(io())
            .flatMapSingle { dependency.searchUsersInteractor.getSearchUsers(SearchUsersParam(it, null, null, 1)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ userList ->
                getUsersResultSubject.onNext(userList)
            }, { error ->
                errorSubject.onNext(error.localizedMessage)
            })
            .apply { disposeBag.add(this) }

    }

    private fun bindOutputs() {

    }
}