package ru.rpuxa.strategy.core.implement.game.players

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.implement.visual.animations.*
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

    override fun onMoveUnit(from: Location, to: Location, sender: Player, fieldAfterMove: Field) {
        val unit = field[to].unit
        visual.animator.animate(MoveUnitAnimation(from, to, unit, true, 700, fieldAfterMove))
        if (moveMode.unit.movePoints == 0)
            visual.closeInfoHeader(false)
        else
            visual.openInfoHeader(unit)
    }

    override fun onMoveStart() {
        updateVisual(field)
    }

    override fun onRuleViolate(rule: Server.RuleException) {
        throw rule
    }

    override fun onBuild(buildableObject: BuildableObject, fieldAfterBuild: Field) {
        updateVisual(fieldAfterBuild)
    }

    override fun onTownLaid(location: Location, fieldAfterTownLaid: Field) {
        updateVisual(fieldAfterTownLaid)
    }

    override fun onStifleRebellion(town: Town, fieldAfterSeize: Field) {
        updateVisual(fieldAfterSeize)
    }

    private fun updateVisual(field: Field) = visual.animator.animate(UpdateAnimation(field))

    override fun onAttack(moveFromLocation: Location,
                          attackFromLocation: Location,
                          attacker: Unit,
                          defender: Unit,
                          defenderHit: Int,
                          attackerHit: Int,
                          killed: Boolean,
                          fieldAfterAttack: Field) {
        visual.closeInfoHeader(false)
        visual.animator.animate(MoveUnitAnimation(moveFromLocation, attackFromLocation, attacker, false, 700, fieldAfterAttack))
        visual.animator.animate(HealthAnimation(defender, -defenderHit, 3000))
        visual.animator.animate(HealthAnimation(attackFromLocation, -attackerHit, 3000))
        if (killed) {
            visual.animator.animate(RemoveUnitAnimation(defender))
            visual.animator.animate(WaitAnimation(700))
            visual.animator.animate(MoveUnitAnimation(attackFromLocation, defender, attacker, true, 700, fieldAfterAttack))
        } else
            visual.animator.animate(UpdateAnimation(fieldAfterAttack))
    }

    override fun onSeizeTown(staticObject: Town, fieldAfterSeize: Field) {
        updateVisual(fieldAfterSeize)
    }

    override fun onTownDestroyed(location: Location, fieldAfterTownDestroyed: Field) {
        updateVisual(fieldAfterTownDestroyed)
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
                visual.update()
            running = false
        }
    }
}