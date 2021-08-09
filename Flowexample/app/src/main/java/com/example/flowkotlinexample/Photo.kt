package com.example.flowkotlinexample

import com.google.gson.annotations.SerializedName

data class Photo(
	@field:SerializedName("id") val id: Int,
	@field:SerializedName("name") val name: String,
	@field:SerializedName("url") val url: String
)
