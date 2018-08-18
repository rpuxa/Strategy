package ru.rpuxa.strategy.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main.*
import ru.rpuxa.strategy.players.GameBuilder
import ru.rpuxa.strategy.R
import ru.rpuxa.strategy.visual.view.ObjInfoController

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val game = GameBuilder(main_field).alone()
        game.start()
        next_move.setOnClickListener {
            val human = game.controllingHuman!!
            ObjInfoController.deactivate(human, true)
            game.endMove(human)
        }
        ObjInfoController.setGameActivity(this)
    }
}
