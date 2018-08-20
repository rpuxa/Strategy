package ru.rpuxa.strategy.core.implement.game.players

import ru.rpuxa.strategy.core.implement.visual.animations.EndAnimations
import ru.rpuxa.strategy.core.implement.visual.animations.HealthAnimation
import ru.rpuxa.strategy.core.implement.visual.animations.MoveUnitAnimation
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.interfaces.game.Server
import ru.rpuxa.strategy.core.interfaces.visual.FieldVisualizer
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionPaint

/**
 * Игрок, который управляется человеком
 */
class Human(
        override val executor: Server,
        override val field: Field,
        override val color: Int,
        /**
         * Через [visual] человек получает информацию о состоянии поля
         * (экран телефона или компьютера), и взаимодействует с ним
         * (тыкая на экран телефона или с помощью мыши)
         */
        val visual: FieldVisualizer
) : Player {

    override fun onStart() {
        visual.onCreate()
        visual.draw(field)
    }

    override fun onMoveStart() {

    }

    override fun onMoveUnit(from: Location, to: Location, sender: Player) {
        visual.animator.animate(MoveUnitAnimation(from, to, field[to].unit, true, 700))
        if (moveMode.unit.movePoints == 0)
            visual.closeInfoHeader( false)
        else
            visual.openInfoHeader(field[to].unit)

    }

    override fun onRuleViolate(rule: Server.RuleException) {
        throw rule
    }

    override fun onBuild(buildableObject: BuildableObject) {
        visual.draw(field)
    }

    override fun onTownLaid(location: Location) {
        visual.draw(field)
    }

    override fun onAttack(moveFromLocation: Location, attackFromLocation: Location, attacker: Unit, defender: Unit, defenderHit: Int, attackerHit: Int) {
        visual.closeInfoHeader( false)
        visual.animator.animate(MoveUnitAnimation(moveFromLocation, attackFromLocation, attacker, true, 700))
        visual.animator.animate(HealthAnimation(defender, -defenderHit, 3000))
        visual.animator.animate(HealthAnimation(attacker, -attackerHit, 3000))
        visual.animator.animate(EndAnimations())
    }

    val moveMode = MoveMode()

    inner class MoveMode {

        lateinit var unit: Unit
        lateinit var selections: Array<RegionPaint>
        var running = false

        fun on(unit: Unit) {
            this.unit = unit
            running = true
        }

        fun off(invalidate: Boolean) {
            if (!running)
                return
            selections.forEach(visual::deselect)
            if (invalidate)
                visual.invalidate()
            running = false
        }
    }
}