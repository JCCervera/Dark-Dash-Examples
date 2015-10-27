package com.juancervera.androidgame;

/**
 * Created by Juan Cervera on 1/31/2015.
 */

import android.graphics.Paint;
import android.view.KeyEvent;

import java.util.List;

import com.juancervera.framework.Audio;
import com.juancervera.framework.Game;
import com.juancervera.framework.Graphics;
import com.juancervera.framework.Screen;
import com.juancervera.framework.Input.TouchEvent;
import com.juancervera.framework.Music;

public class MainMenuScreen extends Screen {
    Paint paint;
    public MainMenuScreen(Game game){
        super(game);
        paint = new Paint();
        if(game.getVolume() == 0) {
            paint.setAlpha(100);
        }
        else{
            paint.setAlpha(255);
        }
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for(int i=0; i<touchEvents.size(); i++) {
                TouchEvent event = touchEvents.get(i);
                if (event.type == TouchEvent.TOUCH_UP) {

                    if (inBounds(event, 750, 1230, 800, 1280)) {
                        //START GAME
                        if(game.getVolume() == 0){
                            game.setVolume((float)0.3);
                        }
                        else {
                            game.setVolume(0);
                        }

                    }
                    else{
                        Assets.mainMenu.setLooping(false);
                        Assets.mainMenu.seekBegin();
                        Assets.mainMenu.stop();
                        game.setScreen(new GameScreen(game));
                    }
                }
            }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Checking for the "menu" key
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            return true;
        } else {
            return false;
        }
    }
    //Touch in bounds
    private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height) {
        if(event.x > x && event.x < x + width -1 && event.y > y
                && event.y < y+height - 1)
            return true;
        else
            return false;
    }

    //Paint the assests and play the music
    @Override
    public void paint(float deltaTime){
        Graphics g = game.getGraphics();
        if(Assets.menu != null){g.drawImage(Assets.menu, 0, 0);}
        else{Assets.menu = g.newImage("MMBGG.jpg", Graphics.ImageFormat.RGB565);}
        if(Assets.startB != null){g.drawImage(Assets.startB, 150, 970);}
        else{Assets.startB = g.newImage("StartButton.png", Graphics.ImageFormat.RGB565);}
        if(game.getVolume() == 0) {
            paint.setAlpha(100);
        }
        else{
            paint.setAlpha(255);
        }
        g.drawImage(Assets.volumeButton, 750, 1230, paint);

        //Play the music
        Assets.mainMenu.play();
        Assets.mainMenu.setLooping(true);
        Assets.mainMenu.setVolume(game.getVolume());

    }

    @Override
    public void pause(){
        Assets.mainMenu.stop();//If the game is existed via the Home button
    }
    @Override
    public void resume(){
        Assets.mainMenu.play();//When they return
    }
    @Override
    public void dispose(){

    }
    @Override
    public void backButton(){
        System.exit(0); //Close the game
    }

}
