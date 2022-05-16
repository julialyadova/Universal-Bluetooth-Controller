package com.example.ubc.items

class ItemsService {
    fun getItemDefinitions() : List<ItemDefinition> {
        return ItemDefinitions.list()
    }
}