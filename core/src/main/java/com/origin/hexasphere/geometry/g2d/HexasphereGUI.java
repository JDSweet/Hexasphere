package com.origin.hexasphere.geometry.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HexasphereGUI extends Stage
{
    private Skin guiSkin;
    private String selection;
    private boolean editingEnabled;

    public HexasphereGUI()
    {
        super();
        construct();
    }

    public HexasphereGUI(Viewport viewport)
    {
        super(viewport);
        construct();
    }

    public HexasphereGUI(Viewport viewport, Batch batch)
    {
        super(viewport, batch);
        construct();
    }

    private void construct()
    {
        this.selection = "Grass";
        this.editingEnabled = true;
        this.guiSkin = new Skin(Gdx.files.internal("assets/ui/uiskin.json"));
        TextButton tb = textButton(0f, 0f, "Toggle Editing");
        SelectBox<String> dd = dropDown(0.9f, 0.95f, "Grass", "Ocean", "Desert", "Mountain", "Hill");
        dd.setSelectedIndex(0);
        tb.setDisabled(false);
        dd.setDisabled(false);

        tb.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editingEnabled = !editingEnabled;
            }
        });

        dd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selection = dd.getSelected();
            }
        });
    }

    // (x/y) = The position of the button as a ratio of the screen's width and height respectively.
    private TextButton textButton(float x, float y, String text)
    {
        TextButton btn = new TextButton(text, this.guiSkin);
        btn.setPosition(x * Gdx.graphics.getWidth(), y * Gdx.graphics.getHeight());
        addActor(btn);
        return btn;
    }

    private SelectBox<String> dropDown (float x, float y, String... items)
    {
        SelectBox<String> dropDown = new SelectBox<>(this.guiSkin);
        dropDown.setPosition(x * Gdx.graphics.getWidth(), y * Gdx.graphics.getHeight());
        dropDown.setSize(60f, 20f);
        dropDown.setItems(items);
        addActor(dropDown);
        return dropDown;
    }

    public boolean isEditingEnabled()
    {
        return this.editingEnabled;
    }

    public String getSelection()
    {
        return selection;
    }
}
