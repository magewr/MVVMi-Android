package com.magewr.mvvmi.clients

import io.reactivex.rxjava3.core.Single
import java.util.*

internal interface ClientProtocol {
    fun request(url: String, parameters: Map<String, Any>) : Single<Objects>
}
