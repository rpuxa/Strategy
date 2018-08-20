package ru.rpuxa.strategy.core.interfaces.visual.region

import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.visual.BoardEffect


/**
 * Класс для отрисовки регионов
 */
interface RegionPaint {

    /**
     * Список регионов
     */
    val list: RegionList

    /**
     * Цвет заполнения региона
     */
    var colorFill: Int

    /**
     * Цвет границы региона
     */
    var colorBorder: Int

    /**
     *  Эффекты границы региона
     */
    var lineEffects: Array<BoardEffect>?


    var fillEffects: Array<BoardEffect>?

    /**
     * Ширина границы
     */
    var strokeWidth: Float

    /**
     * Флаг включения сетки
     */
    var wide: Boolean

    /**
     * Содержитсяя ли клетка в этих регионах
     */
    operator fun contains(cell: Cell) = cell in list.cells

    /**
     * Заполнить регион цветом [colorFill]
     */
    fun fill(colorFill: Int): RegionPaint {
        this.colorFill = colorFill
        return this
    }

    /**
     * Установить цвет границы региона
     */
    fun board(colorBorder: Int): RegionPaint {
        this.colorBorder = colorBorder
        return this
    }

    /**
     * Установить ширину региона
     */
    fun boardWidth(w: Float): RegionPaint {
        this.strokeWidth = w
        return this
    }

    /**
     * Установить эффекты для границы региона
     */
    fun boardEffects(effects: Array<BoardEffect>): RegionPaint {
        lineEffects = effects
        return this
    }

    fun fillEffects(effects: Array<BoardEffect>): RegionPaint {
        fillEffects = effects
        return this
    }

    /**
     * Установить|снять сетку
     */
    fun wide(on: Boolean = true): RegionPaint {
        wide = on
        return this
    }
}
