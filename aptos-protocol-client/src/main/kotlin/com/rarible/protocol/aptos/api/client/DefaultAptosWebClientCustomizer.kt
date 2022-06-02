package com.rarible.protocol.aptos.api.client

import io.netty.channel.ChannelOption
import io.netty.channel.epoll.EpollChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.util.unit.DataSize
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit

class DefaultAptosWebClientCustomizer: WebClientCustomizer {
    override fun customize(webClientBuilder: WebClient.Builder) {
        webClientBuilder.codecs{configurer ->
            configurer.defaultCodecs().maxInMemorySize(DEFAULT_MAX_BODY_SIZE)
        }
        val provider = ConnectionProvider.builder("protocol-default-connection-provider")
            .maxConnections(200)
            .pendingAcquireMaxCount(-1)
            .maxIdleTime(DEFAULT_TIMEOUT)
            .maxLifeTime(DEFAULT_TIMEOUT)
            .lifo()
            .build()

        val client = HttpClient.create(provider)
            .tcpConfiguration {
                it.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                    .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                    .option(EpollChannelOption.TCP_KEEPCNT, 8)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS.toInt())
                    .doOnConnected {connection ->
                        connection.addHandlerFirst(ReadTimeoutHandler(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS))
                        connection.addHandlerLast(WriteTimeoutHandler(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS))
                    }
            }
            .responseTimeout(DEFAULT_TIMEOUT)
            .followRedirect(true)

        val connector = ReactorClientHttpConnector(client)

        webClientBuilder.clientConnector(connector)
    }

    companion object {
        private val DEFAULT_MAX_BODY_SIZE = DataSize.ofMegabytes(10).toBytes().toInt()
        private val DEFAULT_TIMEOUT: Duration = Duration.ofSeconds(30)
        private val DEFAULT_TIMEOUT_MILLIS: Long = DEFAULT_TIMEOUT.toMillis()
    }
}
