package com.rarible.protocol.aptos.api.client

import com.rarible.protocol.aptos.api.ApiClient
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer

class AptosApiClientFactory(
    private val uriProvider: AptosApiServiceUriProvider,
    private val webClientCustomizer: WebClientCustomizer
) {

    fun createDefaultApiClient(): DefaultApi {
        return DefaultApi(createApiClient())
    }

    fun createActivityApiClient(): ActivityControllerApi {
        return ActivityControllerApi(createApiClient())
    }

    private fun createApiClient(): ApiClient {
        return ApiClient(webClientCustomizer)
            .setBasePath(uriProvider.getURI().toASCIIString())
    }
}
