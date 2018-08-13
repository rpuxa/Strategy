package ru.rpuxa.strategy.field

interface Fallible {
    fun fail(): Nothing =
            throw UnsupportedOperationException("NONE constant in class: \"${javaClass.name}\" doesn't implement methods")
}