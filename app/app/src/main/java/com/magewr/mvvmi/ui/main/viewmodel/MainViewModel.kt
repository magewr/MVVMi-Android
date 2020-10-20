package com.magewr.mvvmi.ui.main.viewmodel

import com.magewr.mvvmi.bases.RxViewModel
import com.magewr.mvvmi.bases.RxViewModelProtocol
import com.magewr.mvvmi.interactors.searchusers.SearchUsersInteractorInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject

class MainViewModel() : RxViewModel() {
    class Input(
    ): RxViewModelProtocol.Input()

    class Output(
    ): RxViewModelProtocol.Output()

    class Dependency(
    ): RxViewModelProtocol.Dependency()

    override lateinit var input: Input
    override lateinit var output: Output
    override lateinit var dependency: Dependency

    constructor(dependency: Dependency) : this() {
        this.dependency = dependency
        this.input = Input()
        this.output = Output()

        bindInputs()
        bindOutputs()
    }

    private fun bindInputs() {

    }

    private fun bindOutputs() {

    }
}