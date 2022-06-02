package com.rarible.protocol.aptos.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.aptos.api.client.AptosApiClientFactory
import com.rarible.protocol.aptos.api.client.AptosApiServiceUriProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@SpringBootTest
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(AptosApiClientAutoconfigurationIt.Configuration::class)
class AptosApiClientAutoconfigurationIt {

    @Autowired
    private lateinit var uriProvider: AptosApiServiceUriProvider

    @Autowired
    private lateinit var clientFactory: AptosApiClientFactory

    @Autowired
    @Qualifier(APTOS_WEB_CLIENT_CUSTOMIZER)
    private lateinit var webClientCustomizer: WebClientCustomizer

    @Test
    internal fun `test initialization`() {
        assertThat(uriProvider).isNotNull
        assertThat(clientFactory).isNotNull

        every { webClientCustomizer.customize(any()) } returns Unit

        clientFactory.createDefaultApiClient()

        verify { webClientCustomizer.customize(any()) }
    }

    @TestConfiguration
    inner class Configuration {

        @Bean
        @Qualifier(APTOS_WEB_CLIENT_CUSTOMIZER)
        fun webClientCustomizer(): WebClientCustomizer = mockk()

        @Bean
        fun applicationEnvironmentInfo(): ApplicationEnvironmentInfo =
            ApplicationEnvironmentInfo("test", "test.local")
    }
}
