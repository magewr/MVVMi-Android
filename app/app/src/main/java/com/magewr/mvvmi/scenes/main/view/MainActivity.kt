package com.magewr.mvvmi.scenes.main.view

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding4.view.clicks
import com.magewr.mvvmi.R
import com.magewr.mvvmi.bases.RxActivity
import com.magewr.mvvmi.clients.RestClient
import com.magewr.mvvmi.clients.apis.APIQuotes
import com.magewr.mvvmi.databinding.ActivityMainBinding
import com.magewr.mvvmi.interactors.Quotes.QuotesInteractor
import com.magewr.mvvmi.scenes.main.viewmodel.QuotesViewModel
import java.util.concurrent.TimeUnit

class MainActivity : RxActivity<QuotesViewModel>() {
    private lateinit var bnd: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = QuotesViewModel(QuotesViewModel.Dependency(QuotesInteractor(RestClient(APIQuotes::class.java))))

        viewModel.input.getRandomQuotes.onNext(Unit)
    }

    override fun bindInputs() {
        bnd.btnNext.clicks()
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .subscribe{ viewModel.input.getRandomQuotes.onNext(Unit) }
            .apply { disposeBag.add(this) }
    }

    override fun bindOutputs() {
        viewModel.output.getRandomQuotesResult
            .subscribe{ quotes ->
                bnd.txtQuotes.text = quotes
            }
            .apply { disposeBag.add(this) }

        viewModel.output.error
            .subscribe{ errorMessage ->
                Log.d(this::getLocalClassName.toString(), errorMessage)
            }
            .apply { disposeBag.add(this) }
    }
}