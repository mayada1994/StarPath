package com.mayada1994.starpath

import ktx.app.KtxGame
import ktx.app.KtxScreen

class StarPath : KtxGame<KtxScreen>() {

    override fun create() {
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}