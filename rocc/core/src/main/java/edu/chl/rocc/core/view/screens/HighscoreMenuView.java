package edu.chl.rocc.core.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chl.rocc.core.m2phyInterfaces.IRoCCModel;

/**
 * Created by Jacob on 2015-05-11.
 */
public class HighscoreMenuView extends AbstractMenuView {

    //title
    private final Label titleLabel;
    private TextButton backButton;

    public HighscoreMenuView(IRoCCModel model){
        super(model);

        titleLabel = new Label("Highscore", textStyle);
        titleLabel.setFontScale(2);

        //Initialize buttons
        createButtons();

        //adds to table
        table.add(titleLabel);
        table.row();

        float buttonWidth = 200;
        table.add(backButton).width(buttonWidth);

        stage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
    }

    //Creates all of the buttons and adds listeners
    private void createButtons(){
        backButton = new TextButton("Back", textButtonStyle);

        //Padding to button
        backButton.pad(20);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x, float y){
                notifyObserver("menu");
            }
        });
    }
}
