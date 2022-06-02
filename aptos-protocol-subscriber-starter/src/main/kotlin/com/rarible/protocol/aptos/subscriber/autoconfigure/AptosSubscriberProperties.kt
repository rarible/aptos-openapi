package com.rarible.protocol.aptos.subscriber.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

internal const val PROTOCOL_APTOS_SUBSCRIBER = "protocol.aptos.subscriber"

@ConfigurationProperties(PROTOCOL_APTOS_SUBSCRIBER)
@ConstructorBinding
data class AptosSubscriberProperties (
    val brokerReplicaSet: String
)
