package com.hyecheon.itemclient.controller

import com.hyecheon.itemclient.domain.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.*

@RestController
class ItemClientController {
	val webClient: WebClient = WebClient.create("http://localhost:8080")

	@GetMapping("/client/retrieve")
	fun getAllItemsUsingRetrieve(): Flux<Item> {
		return webClient.get().uri("/v1/items")
				.retrieve()
				.bodyToFlux(Item::class.java)
				.log("Items in Client Project")
	}

	@GetMapping("/client/exchange")
	fun getAllItemsUsingExchange(): Flux<Item> {
		return webClient.get().uri("/v1/items")
				.exchange()
				.flatMapMany { clientResponse ->
					clientResponse.bodyToFlux(Item::class.java)
				}
				.log("Items in Client Project exchange : ")
	}


}