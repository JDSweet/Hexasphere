package com.origin.hexasphere;

import com.badlogic.gdx.Game;
import com.origin.hexasphere.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HexaGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
