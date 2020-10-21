package com.magewr.gitusersearch.commons

enum class ClientError (val rawValue: Int) {
    success(0),
    networkTooManyRequest(429),
    networkInvalidUrl(40000),
    networkInvalidResponseData(40001),
    networkInvalidParameter(40002),
    networkInvalidStatus(40003),
    networkInvalidParse(40004),
    networkInvalidRequest(40005),
    systemDeallocated(44444),
    unknownCode(55555);

    companion object {
        operator fun invoke(rawValue: Int) = ClientError.values().firstOrNull { it.rawValue == rawValue }
    }


    internal fun messageKey(value: Int) : String {
        when (value) {
            success.rawValue -> return "success"
            networkTooManyRequest.rawValue -> return "networkTooManyRequest"
            networkInvalidUrl.rawValue -> return "networkInvalidUrl"
            networkInvalidResponseData.rawValue -> return "networkInvalidResponseData"
            networkInvalidParameter.rawValue -> return "networkInvalidParameter"
            networkInvalidStatus.rawValue -> return "networkInvalidStatus"
            networkInvalidParse.rawValue -> return "networkInvalidParse"
            networkInvalidRequest.rawValue -> return "networkInvalidRequest"
            systemDeallocated.rawValue -> return "systemDeallocated"
            else -> return "unknownCode"
        }
    }
}
