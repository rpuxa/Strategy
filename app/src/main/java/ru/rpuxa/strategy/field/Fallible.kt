package ru.rpuxa.strategy.field

interface Fallible {
    /**
     * Метод для обозначения нереализованных полей
     */
    fun fail(): Nothing =
            throw UnsupportedOperationException("NONE constant in class: \"${javaClass.name}\" doesn't implement methods")
}