package ru.rpuxa.strategy.field

/**
 * Data класс для хранения возможных ходов юнита
 */
data class UnitMove(
        /**
         * Клетка, куда он может добраться
         */
        val cell: Cell,

        /**
         * Сколько ОП ему нужно потратить для перемещения
         * на эту клетку
         */
        val steps: Int
)