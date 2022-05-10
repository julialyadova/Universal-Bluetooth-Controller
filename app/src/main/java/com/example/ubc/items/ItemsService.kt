package com.example.ubc.items

import com.example.ubc.data.entities.Item

class ItemsService {
    fun getItemIdentifiers() : List<ItemIdentifier> {
        return listOf(
            ItemIdentifier(Item.Types.BUTTON, "Кнопка", "Кнопка с текстом"),
            ItemIdentifier(Item.Types.SWITCH, "Переключатель", "Обычный переключатель"),
            ItemIdentifier(Item.Types.SIMPLE_DISPLAY, "Дисплей", "Отображает последнее полученное сообщение от соединненного устройства"),
            ItemIdentifier(Item.Types.HISTORY, "История сообщений", "Отображает историю входящих сообщений"),
        )
    }
}