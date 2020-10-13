package com.magewr.mvvmi.bases

import io.reactivex.rxjava3.disposables.CompositeDisposable

interface RxViewModelProtocol<Input, Output, Dependency> {
    var input : Input
    var output: Output
    var dependency: Dependency
}

open class RxViewModel: Deinitializable {
    var disposeBag = CompositeDisposable()

    override fun deinitialize() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
    }
}