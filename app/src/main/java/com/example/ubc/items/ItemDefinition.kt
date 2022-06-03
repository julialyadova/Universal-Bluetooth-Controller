package com.example.ubc.items

import android.content.Context
import com.example.ubc.items.smf.*
import com.example.ubc.ui.items.ItemView
import com.example.ubc.ui.items.ubcarduino.*

class ItemDefinition (
    val name: String,
    val description: String? = null,
    val itemBinding: () -> Item,
    val itemViewBinding: ((Item, Context) -> ItemView)
) {
    companion object {
        val definitions : Map<String, ItemDefinition> = mapOf(
            Item.Types.BUTTON to ItemDefinition("Кнопка с текстом", "Кнопка с текстом",
                {ItemButton()}, {item, context -> ButtonView(item as ItemButton, context) }),

            Item.Types.ICON_BUTTON to ItemDefinition("Кнопка с иконкой", "Кнопка с иконкой",
                {ItemIconButton()}, { item, context -> IconButtonView(item as ItemIconButton, context) }),

            Item.Types.SWITCH to ItemDefinition("Переключатель", "Обычный переключатель",
                {ItemSwitch()}, {item, context -> SwitchView(item as ItemSwitch, context) }),

            Item.Types.SIMPLE_DISPLAY to ItemDefinition("Дисплей", "Отображает последнее полученное сообщение от соединненного устройства",
                {ItemDisplay()}, {item, context -> SimpleDisplayView(item as ItemDisplay, context) }),

            Item.Types.HISTORY to ItemDefinition("История сообщений", "Отображает историю входящих сообщений",
                {ItemHistory()}, {item, context -> HistoryView(item as ItemHistory, context)}),

            Item.Types.SLIDER to ItemDefinition("Слайдер", "Позволяет выбирать значние из диапазона. Отправляет значение на устройство, когда ползунок отпускается",
                { ItemSlider() }, { item, context -> SliderView(item as ItemSlider, context)}),

            Item.Types.SCALE to ItemDefinition("Шкала", "Отображает значение на шкале",
                {ItemScale()}, {item, context -> ScaleView(item as ItemScale, context)}),

            Item.Types.ROUND_SCALE to ItemDefinition("Круговая шкала", "Отображает значение на круговой на шкале",
                {ItemRoundScale()}, { item, context -> RoundScaleView(item as ItemRoundScale, context)}),

            Item.Types.JOYSTICK to ItemDefinition("Джойстик", "передает на устройство X и Y",
                {ItemJoystick()}, { item, context -> JoystickView(item as ItemJoystick, context)}),

            Item.Types.INDICATOR to ItemDefinition("Индикатор", "загорается и потухает по команде",
                {ItemIndicator()}, { item, context -> IndicatorView(item as ItemIndicator, context)}),

            Item.Types.TEXT_INPUT to ItemDefinition("Поле ввода текста", "отправиляет текст ASCII",
                {ItemTextInput()}, { item, context -> TextInputView(item as ItemTextInput, context)})
        )
    }
}