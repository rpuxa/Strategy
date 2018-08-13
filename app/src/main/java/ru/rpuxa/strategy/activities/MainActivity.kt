package ru.rpuxa.strategy.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.rpuxa.strategy.GameBuilder
import ru.rpuxa.strategy.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GameBuilder(fieldView).alone.start()
    }
}
