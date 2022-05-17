package com.example.ubc.items

import com.example.ubc.data.entities.ItemEntity

class ItemDefinition (
    val itemType: String,
    val name: String,
    val description: String? = null
) {
    companion object {
        val definitions : List<ItemDefinition> = listOf(
            ItemDefinition(ItemEntity.Types.BUTTON, "Кнопка", "Кнопка с текстом"),
            ItemDefinition(ItemEntity.Types.SWITCH, "Переключатель", "Обычный переключатель"),
            ItemDefinition(ItemEntity.Types.SIMPLE_DISPLAY, "Дисплей", "Отображает последнее полученное сообщение от соединненного устройства"),
            ItemDefinition(ItemEntity.Types.HISTORY, "История сообщений", "Отображает историю входящих сообщений")
        )
    }
}