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

class MainActivity : RxActivity<QuotesViewModel>() {
    private lateinit var bnd: ActivityMainBinding
    override lateinit var viewModel: QuotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // DI or Factory 통해서 주입받아야 함
        viewModel = QuotesViewModel(QuotesViewModel.Dependency(QuotesInteractor(RestClient(APIQuotes::class.java))))

        bindInputs()
        bindOutputs()

        viewModel.input.getRandomQuotes.onNext(Unit)
    }

    private fun bindInputs() {
        bnd.btnNext.clicks()
            .subscribe{ viewModel.input.getRandomQuotes.onNext(Unit) }
            .apply { disposeBag.add(this) }

    }

    private fun bindOutputs() {
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