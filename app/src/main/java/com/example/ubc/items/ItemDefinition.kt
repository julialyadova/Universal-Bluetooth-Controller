package com.example.ubc.items

class ItemDefinition (
    val itemType: String,
    val name: String,
    val description: String? = null,
    val params: List<ItemParamDefinition>
)