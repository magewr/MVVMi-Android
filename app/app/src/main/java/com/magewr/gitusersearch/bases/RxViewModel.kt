package com.magewr.gitusersearch.bases

import io.reactivex.rxjava3.disposables.CompositeDisposable

interface RxViewModelProtocol {
    abstract class Input
    abstract class Output
    abstract class Dependency

    val input : Input
    val output: Output
    val dependency: Dependency
}

abstract class RxViewModel: RxViewModelProtocol,
    Deinitializable {
    var disposeBag = CompositeDisposable()

    override fun deinitialize() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
    }
}