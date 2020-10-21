package com.magewr.gitusersearch.ui.main.viewmodel

import com.magewr.gitusersearch.bases.RxViewModel
import com.magewr.gitusersearch.bases.RxViewModelInterface

class MainViewModel() : RxViewModel() {
    class Input(
    ): RxViewModelInterface.Input()

    class Output(
    ): RxViewModelInterface.Output()

    class Dependency(
    ): RxViewModelInterface.Dependency()

    override lateinit var input: Input
    override lateinit var output: Output
    override lateinit var dependency: Dependency

    constructor(dependency: Dependency) : this() {
        this.dependency = dependency
        this.input =
            Input()
        this.output =
            Output()

        bindInputs()
        bindOutputs()
    }

    private fun bindInputs() {

    }

    private fun bindOutputs() {

    }
}