package ru.rpuxa.strategy.android.visual

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fight_dialog.*
import kotlinx.android.synthetic.main.main.*
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
        val view = layoutInflater.inflate(R.layout.fight_dialog, null, false)
        setContentView(view, ViewGroup.LayoutParams(((context as Activity).main_view.width * .9f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT))
        your_icon.setImageBitmap(textureBank[youUnit.icon])
        your_name.text = youUnit.name
        val yourHp = youUnit.fight(enemyUnit)
        val yourAttack = youUnit.health - yourHp
        your_hp_bar.progress = yourHp
        you_hp.text = yourHp.hpToString()

        enemy_icon.setImageBitmap(textureBank[youUnit.icon])
        enemy_name.text = enemyUnit.name
        val enemyHp = enemyUnit.fight(youUnit)
        val enemyAttack = enemyUnit.health - enemyHp
        enemy_hp_bar.progress = enemyHp
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
        attack.setOnClickListener {
            dismiss()
            attackBlock()
        }
    }

    private fun Int.hpToString() = if (this <= 0) "(полное уничтожение)" else "($this)"
}