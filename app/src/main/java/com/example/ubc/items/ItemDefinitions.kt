package com.example.ubc.items

import com.example.ubc.data.entities.Item

class ItemDefinitions {
    companion object {
        fun list() : List<ItemDefinition> {
            return listOf(
                    ItemDefinition(
                            Item.Types.BUTTON,
                            "Кнопка",
                            "Кнопка с текстом",
                            listOf(
                                    ItemParamDefinition("command", ParamType.TEXT ,"btn"),
                            )),
                    ItemDefinition(
                            Item.Types.SWITCH,
                            "Переключатель",
                            "Обычный переключатель",
                            listOf(
                                    ItemParamDefinition("command on", ParamType.TEXT ,"switch on"),
                                    ItemParamDefinition("command off", ParamType.TEXT ,"switch off"),
                                    ItemParamDefinition("example param", ParamType.FLAG ,"true")
                            )),
                    ItemDefinition(
                            Item.Types.SIMPLE_DISPLAY,
                            "Дисплей",
                            "Отображает последнее полученное сообщение от соединненного устройства",
                            listOf(
                                    ItemParamDefinition("id", ParamType.TEXT ,"btn"),
                                    ItemParamDefinition("format", ParamType.CATEGORY ,"ASCII,DECIMAL,FLOAT"),
                            )),
                    ItemDefinition(
                            Item.Types.HISTORY,
                            "История сообщений",
                            "Отображает историю входящих сообщений",
                            listOf(
                                    ItemParamDefinition("max records", ParamType.DECIMAL ,"10"),
                            )),
            )
        }
    }
}