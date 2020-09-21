package com.hyecheon.itemclient.controller

import com.hyecheon.itemclient.domain.*
import org.springframework.http.*
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

	@GetMapping("/client/retrieve/singleItem")
	fun getOneItemsUsingRetrieve(): Mono<Item> {
		return webClient.get().uri("/v1/items/{id}", "ABC")
				.retrieve()
				.bodyToMono(Item::class.java)
				.log("Items in Client Project")
	}

	@GetMapping("/client/exchange/singleItem")
	fun getOneItemsUsingExchange(): Mono<Item> {
		return webClient.get().uri("/v1/items/{id}", "ABC")
				.exchange()
				.flatMap { clientResponse ->
					clientResponse.bodyToMono(Item::class.java)
				}
				.log("Items in Client Project exchange : ")
	}

	@PostMapping("/client/createItem")
	fun createItem(@RequestBody item: Item) = run {
		val monoItem = Mono.just(item)
		webClient.post().uri("/v1/items")
				.contentType(MediaType.APPLICATION_JSON)
				.body(monoItem)
				.retrieve()
				.bodyToMono(Item::class.java)
				.log("Created item is : ")
	}

}