package com.rarible.protocol.aptos.api.client

import java.net.URI

interface AptosApiServiceUriProvider {

    fun getURI(): URI
}
