package com.mayada1994.starpath;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class StarPath extends Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}