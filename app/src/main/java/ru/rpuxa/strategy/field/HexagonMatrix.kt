package ru.rpuxa.strategy.field


val offers = arrayOf(
        1 to 0,
        2 to 0,
        1 to -1,
        -1 to -1,
        -2 to 0,
        -1 to 0
)

/**
 * Гексагональная матрица
 */
open class HexagonMatrix<T>(val field: Array<Array<T>>) {

    /**
     * Получить содержимое по координатам
     */
    operator fun get(x: Int, y: Int): T = field[x][y]

    operator fun set(x: Int, y: Int, value: T)  {
        field[x][y] = value
    }

    /**
     * Получить всех соседей по клетке
     */
    fun getNeighbours(x: Int, y: Int, array: Array<T?>): Array<T?> {
        if (array.size != offers.size)
            throw IllegalStateException("array must have size ${offers.size}")

        for (i in offers.indices) {

            val (shiftX, shiftY) = offers[i]
                    array[i] = get(x + shiftX, y + shiftY)
        }

        return array
    }


}