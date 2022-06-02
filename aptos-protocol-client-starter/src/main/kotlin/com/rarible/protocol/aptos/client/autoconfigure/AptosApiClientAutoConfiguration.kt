package com.rarible.protocol.aptos.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.aptos.api.client.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import java.net.URI

const val APTOS_WEB_CLIENT_CUSTOMIZER = "APTOS_WEB_CLIENT_CUSTOMIZER"

class AptosApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(APTOS_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(AptosApiServiceUriProvider::class)
    fun aptosApiServiceUriProvider(): AptosApiServiceUriProvider {
        return FixedAptosApiServiceUriProvider(URI.create("http://localhost:8080")) //todo devnet/mainnet URI's ?
    }

    @Bean
    @ConditionalOnMissingBean(AptosApiClientFactory::class)
    fun aptosApiClientFactory(aptosApiServiceUriProvider: AptosApiServiceUriProvider): AptosApiClientFactory {
        val customizer =
            CompositeWebClientCustomizer(listOf(DefaultAptosWebClientCustomizer(), webClientCustomizer))
        return AptosApiClientFactory(aptosApiServiceUriProvider, customizer)
    }
}
