package com.mayada1994.starpath.screens

import com.mayada1994.starpath.StarPath

class MenuScreen(game: StarPath) : BaseScreen(game) {

    override fun render(delta: Float) {
        super.render(delta)

        game.setScreen<GameScreen>()
    }

}