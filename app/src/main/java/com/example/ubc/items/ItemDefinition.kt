package com.example.ubc.items

import android.content.Context
import com.example.ubc.items.smf.*
import com.example.ubc.ui.panel.items.ItemView
import com.example.ubc.ui.panel.items.ubcarduino.*

class ItemDefinition (
    val name: String,
    val description: String? = null,
    val itemBinding: () -> Item,
    val itemViewBinding: ((Item, Context) -> ItemView)
) {
    companion object {
        val definitions : Map<String, ItemDefinition> = mapOf(
            Item.Types.BUTTON to ItemDefinition("Кнопка", "Кнопка с текстом",
                {ItemButton()}, {item, context -> ButtonView(item as ItemButton, context) }),

            Item.Types.SWITCH to ItemDefinition("Переключатель", "Обычный переключатель",
                {ItemSwitch()}, {item, context -> SwitchView(item as ItemSwitch, context) }),

            Item.Types.SIMPLE_DISPLAY to ItemDefinition("Дисплей", "Отображает последнее полученное сообщение от соединненного устройства",
                {ItemDisplay()}, {item, context -> SimpleDisplayView(item as ItemDisplay, context) }),

            Item.Types.HISTORY to ItemDefinition("История сообщений", "Отображает историю входящих сообщений",
                {ItemHistory()}, {item, context -> HistoryView(item as ItemHistory, context)}),

            Item.Types.SLIDER to ItemDefinition("Слайдер", "Позволяет выбирать значние из диапазона. Отправляет значение на устройство, когда ползунок отпускается",
                { ItemSlider() }, { item, context -> SliderView(item as ItemSlider, context)}),

            Item.Types.SCALE to ItemDefinition("Шкала", "Отображает значение на шкале",
                {ItemScale()}, {item, context -> ScaleView(item as ItemScale, context)})
        )
    }
}