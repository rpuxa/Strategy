package ru.rpuxa.strategy.core.implement.visual.region

import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.visual.region.Region
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionList

class ArrayRegionList(override val cells: Collection<Cell>) : ArrayList<Region>(), RegionList