package com.rarible.protocol.aptos.api.client

import java.net.URI

class FixedAptosApiServiceUriProvider(
    private val fixedURI: URI
) : AptosApiServiceUriProvider {
    override fun getURI(): URI = fixedURI
}
