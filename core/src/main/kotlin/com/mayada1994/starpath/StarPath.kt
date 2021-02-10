package com.mayada1994.starpath

import com.badlogic.gdx.Game

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class StarPath : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}