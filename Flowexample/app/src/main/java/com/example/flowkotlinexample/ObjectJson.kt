package com.example.flowkotlinexample

data class ObjectJson(
	var message: String = "",
	val status: String = "",
	val data: List<Photo>? = null
)
