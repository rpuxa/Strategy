package ru.rpuxa.strategy.core.interfaces.visual.region

import ru.rpuxa.strategy.core.interfaces.field.Cell


/**
 * Список регионов
 */
interface RegionList : List<Region> {

    /**
     * Коллекция клеток, который содержат все регионы вместе
     */
    val cells: Collection<Cell>
}