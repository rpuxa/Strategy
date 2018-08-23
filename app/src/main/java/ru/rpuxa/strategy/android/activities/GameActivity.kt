package ru.rpuxa.strategy.android.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main.*
import ru.rpuxa.strategy.R
import ru.rpuxa.strategy.core.implement.builders.FieldBuilder
import ru.rpuxa.strategy.core.implement.builders.ServerBuilder

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val game = ServerBuilder(main_field, FieldBuilder.rectangle(30, 13)).alone()
        game.start()
    }
}
