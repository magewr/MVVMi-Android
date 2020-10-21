package com.magewr.gitusersearch.ui.main.viewmodel

import com.magewr.gitusersearch.bases.RxViewModel
import com.magewr.gitusersearch.bases.RxViewModelInterface
import com.magewr.gitusersearch.interactors.favoriteusers.FavoriteUsersInteractorInterface
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersInteractorInterface
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersParam
import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import com.magewr.gitusersearch.ui.main.model.Users
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject

class UserListViewModel(): RxViewModel() {
    class Input(
        var favoriteToggled: Observer<Users>,
        var queryChanged: Observer<String>
    ): RxViewModelInterface.Input()

    class Output(
        var getUsersResult: Observable<SearchUsersResultModel>,
        var favoriteChanged: Observable<Unit>,
        var error: Observable<String>
    ): RxViewModelInterface.Output()

    // 디펜던시는 클래스가 아닌 인터페이스를 레퍼런스로 사용, 용도에 맞게 API, Local 아이템을 넣어서 사용하게 함
    class Dependency(
        var searchUsersInteractor: SearchUsersInteractorInterface,
        var favoriteUsersInteractor: FavoriteUsersInteractorInterface
    ): RxViewModelInterface.Dependency()

    override lateinit var input: Input
    override lateinit var output: Output
    override lateinit var dependency: Dependency

    private var favoriteToggledSubject = PublishSubject.create<Users>()
    private var queryChangedSubject = PublishSubject.create<String>()
    private var getUsersResultSubject = PublishSubject.create<SearchUsersResultModel>()
    private var favoriteChangedSubject = PublishSubject.create<Unit>()
    private var errorSubject = PublishSubject.create<String>()

    // 디펜던시는 생성자를 통해 외부에서 주입
    constructor(dependency: Dependency) : this() {
        this.dependency = dependency
        this.input =
            Input(
                favoriteToggled = favoriteToggledSubject,
                queryChanged = queryChangedSubject
            )
        this.output =
            Output(
                getUsersResult = getUsersResultSubject,
                favoriteChanged = favoriteChangedSubject,
                error = errorSubject
            )

        bindInputs()
        bindOutputs()
    }

    private fun bindInputs() {
        // 쿼리 텍스트 변경 시 핸들링
        queryChangedSubject
            .subscribeOn(io())
            .flatMapSingle {
                dependency.searchUsersInteractor
                    .getSearchUsers(
                        SearchUsersParam(
                            it,
                            null,
                            null,
                            1
                        )
                    )
                    .onErrorResumeNext {
                        errorSubject.onNext(it.localizedMessage)
                        Single.never()
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { userList ->
                getUsersResultSubject.onNext(userList)
            }
            .apply { disposeBag.add(this) }

        // 좋아요 버튼 클릭시 핸들링
        favoriteToggledSubject
            .subscribeOn(io())
            .subscribe{dependency.favoriteUsersInteractor.toggleFavoriteUser(it)}
            .apply { disposeBag.add(this) }
    }

    private fun bindOutputs() {
        // 좋아요 상태 변경 시 핸들링 - 유저검색 인터렉터에도 노티하여 각각 설정된 기능을 수행한다.
        dependency.favoriteUsersInteractor
            .subject
            .map{ favoriteChangedSubject.onNext(Unit) }
            .flatMapSingle { dependency.searchUsersInteractor.favoriteUpdated() }
            .subscribe{
                getUsersResultSubject.onNext(it)
            }
            .apply { disposeBag.add(this) }
    }
}