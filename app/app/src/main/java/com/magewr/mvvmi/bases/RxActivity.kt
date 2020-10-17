package com.magewr.mvvmi.bases

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable


abstract class RxActivity<ViewModel: RxViewModel> : AppCompatActivity(), Deinitializable {
    abstract var viewModel: ViewModel
    var disposeBag = CompositeDisposable()
    override fun deinitialize() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
        viewModel.deinitialize()
    }

    override fun onDestroy() {
        deinitialize()
        super.onDestroy()
    }
}