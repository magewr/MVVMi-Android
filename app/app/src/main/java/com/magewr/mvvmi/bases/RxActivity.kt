package com.magewr.mvvmi.bases

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable


class RxActivity<ViewModel: RxViewModel> : AppCompatActivity(), Deinitializable {
    lateinit var viewModel: ViewModel
    var disposeBag = CompositeDisposable()
    override fun deinitialize() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
    }

    override fun onDestroy() {
        deinitialize()
        super.onDestroy()
    }
}