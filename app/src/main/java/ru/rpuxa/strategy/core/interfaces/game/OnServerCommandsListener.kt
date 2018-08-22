package ru.rpuxa.strategy.core.interfaces.game

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit

interface OnServerCommandsListener {

    /**
     * Вызывается, когда игрок нарушает правила (по мнению сервера),
     * в [rule] содержится информация о нарушении
     */
    fun onRuleViolate(rule: Server.RuleException)

    /**
     * Вызывается, когда чей то юнит передвигается с локации [from] на [to]
     * sender - игрок, передвинувший юнит
     */
    fun onMoveUnit(from: Location, to: Location, sender: Player, fieldAfterMove: Field)

    /**
     * Вызывается при начале игры
     */
    fun onStart()

    /**
     * Вызывается при начале хода
     */
    fun onMoveStart()

    /**
     * Вызывается при постройке [BuildableObject] в городе
     */
    fun onBuild(buildableObject: BuildableObject, fieldAfterBuild: Field)

    /**
     * Вызывается когда поселенец закладывает город
     */
    fun onTownLaid(location: Location, fieldAfterTownLaid: Field)

    fun onAttack(moveFromLocation: Location,
                 attackFromLocation: Location,
                 attacker: Unit,
                 defender: Unit,
                 defenderHit: Int,
                 attackerHit: Int,
                 killed: Boolean,
                 fieldAfterAttack: Field
    )

    fun onSeizeTown(staticObject: Town, fieldAfterSeize: Field)

    fun onStifleRebellion(town: Town, fieldAfterSeize: Field)

    fun onTownDestroyed(location: Location, fieldAfterTownDestroyed: Field)
}