package com.mayada1994.starpath

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(StarPath(), AndroidApplicationConfiguration().apply {
            hideStatusBar = true
            useImmersiveMode = true
        })
    }
}