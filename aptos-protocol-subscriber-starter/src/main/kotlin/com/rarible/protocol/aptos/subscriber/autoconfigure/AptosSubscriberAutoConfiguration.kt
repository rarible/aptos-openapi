package com.rarible.protocol.aptos.subscriber.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.aptos.api.subscriber.AptosEventsConsumerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@EnableConfigurationProperties(AptosSubscriberProperties::class)
class AptosSubscriberAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo,
    private val properties: AptosSubscriberProperties
) {

    @Bean
    @ConditionalOnMissingBean(AptosEventsConsumerFactory::class)
    fun aptosEventsConsumerFactory(): AptosEventsConsumerFactory {
        return AptosEventsConsumerFactory(
            brokerReplicaSet = properties.brokerReplicaSet,
            environment = applicationEnvironmentInfo.name,
            host = applicationEnvironmentInfo.host
        )
    }
}
