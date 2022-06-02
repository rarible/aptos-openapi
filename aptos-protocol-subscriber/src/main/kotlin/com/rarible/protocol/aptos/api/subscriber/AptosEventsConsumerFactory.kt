package com.rarible.protocol.aptos.api.subscriber

import com.rarible.core.kafka.RaribleKafkaConsumer
import com.rarible.core.kafka.json.JsonDeserializer
import com.rarible.protocol.dto.aptos.*
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import java.util.*

class AptosEventsConsumerFactory(
    private val brokerReplicaSet: String,
    private val environment: String,
    host: String
) {

    private val clientIdPrefix = "$environment.aptos.$host.${UUID.randomUUID()}"

    private val offsetStrategy = OffsetResetStrategy.EARLIEST

    fun createItemEventsConsumer(consumerGroup: String): RaribleKafkaConsumer<AptosTokenEventDto> {
        return RaribleKafkaConsumer(
            clientId = "$clientIdPrefix.aptos-item-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = AptosTokenEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = AptosNftItemEventTopicProvider.getTopic(environment),
            offsetResetStrategy = offsetStrategy,
            bootstrapServers = brokerReplicaSet
        )
    }

    fun createOwnershipEventsConsumer(consumerGroup: String): RaribleKafkaConsumer<AptosOwnershipEventDto> {
        return RaribleKafkaConsumer(
            clientId = "$clientIdPrefix.aptos-ownership-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = AptosOwnershipEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = AptosNftOwnershipEventTopicProvider.getTopic(environment),
            offsetResetStrategy = offsetStrategy,
            bootstrapServers = brokerReplicaSet
        )
    }

    fun createActivityEventsConsumer(consumerGroup: String): RaribleKafkaConsumer<ActivityDto> {
        return RaribleKafkaConsumer(
            clientId = "$clientIdPrefix.aptos-activity-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = ActivityDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = AptosActivityEventTopicProvider.getTopic(environment),
            offsetResetStrategy = offsetStrategy,
            bootstrapServers = brokerReplicaSet
        )
    }
}
