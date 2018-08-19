package ru.rpuxa.strategy.android.visual

import android.app.Dialog
import android.content.Context
import kotlinx.android.synthetic.main.fight_dialog.*
import ru.rpuxa.strategy.R
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit

class FightDialog(
        context: Context,
        textureBank: TextureBank,
        youUnit: FightingUnit,
        enemyUnit: FightingUnit,
        val attackBlock: () -> kotlin.Unit
) : Dialog(context) {

    init {
        setContentView(R.layout.fight_dialog)
        your_icon.setImageBitmap(textureBank[youUnit.icon])
        your_name.text = youUnit.name
        your_hp_bar.progress = youUnit.health
        val yourHp = youUnit.fight(enemyUnit)
        val yourAttack = youUnit.health - yourHp
        you_hp.text = yourHp.hpToString()

        enemy_icon.setImageBitmap(textureBank[youUnit.icon])
        enemy_name.text = enemyUnit.name
        enemy_hp_bar.progress = enemyUnit.health
        val enemyHp = enemyUnit.fight(youUnit)
        val enemyAttack = enemyUnit.health - enemyHp
        enemy_hp.text = enemyHp.hpToString()

        result.text = when {
            enemyAttack == yourAttack -> "Ничья"
            yourHp == 0 -> "Полное поражение"
            enemyHp == 0 -> "Полная победа"
            enemyAttack > yourAttack -> "Проигрыш"
            enemyAttack < yourAttack -> "Победа"
            else -> throw Exception()
        }

        cancel.setOnClickListener {
            dismiss()
        }
        attack.setOnClickListener { attackBlock() }
    }

    private fun Int.hpToString() = if (this <= 0) "(полное уничтожение)" else "($this)"
}