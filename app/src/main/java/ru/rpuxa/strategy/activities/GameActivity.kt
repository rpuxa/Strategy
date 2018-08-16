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
        GameBuilder(main_field).alone.start()
        ObjInfoController.setGameActivity(this)
    }
}
