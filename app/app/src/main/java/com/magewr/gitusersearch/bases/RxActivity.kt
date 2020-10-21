package com.magewr.gitusersearch.bases

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable


abstract class RxActivity<ViewModel: RxViewModel> : AppCompatActivity(),
    Deinitializable {
    lateinit var viewModel: ViewModel
    var disposeBag = CompositeDisposable()

    abstract fun bindInputs()
    abstract fun bindOutputs()

    override fun deinitialize() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
        viewModel.deinitialize()
    }

    // Stop에서 구독중인 Disposable 해제
    override fun onStop() {
        super.onStop()
        disposeBag.clear()
    }

    // Start에서 바인딩
    override fun onStart() {
        super.onStart()
        bindInputs()
        bindOutputs()
    }

    override fun onDestroy() {
        deinitialize()
        super.onDestroy()
    }
}