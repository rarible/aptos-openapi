package com.rarible.protocol.dto.aptos

class AptosNftOwnershipEventTopicProvider {

    companion object {
        const val VERSION = "v1"

        fun getTopic(environment: String): String {
            return "protocol.$environment.aptos.indexer.nft.ownership"
        }
    }
}
