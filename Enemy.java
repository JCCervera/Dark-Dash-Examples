package com.juancervera.androidgame;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Owner on 5/4/2015.
 */


public class Enemy {

    public static ArrayList<Enemy> EnemyList = new ArrayList<>();//The list of enemies

    public
            float x;//Where are they
            float y;
            int radius = Assets.Enemy.getWidth()/2;//The radius
            double velocityX;//Where are they going
            double velocityY;
            double dVelocityX;//This is where they are going, without speed
            double dVelocityY;
            float pX[] = new float[4];//random points to follow for each quad
            float pY[] = new float[4];
            Random rX = new Random();//the random num gen
            Random rY = new Random();
            boolean inLight, Spotted, Idle;//bools for the different states
            long currentTime = System.currentTimeMillis();//timer for setting the points to follow
            long timePassed = currentTime;

            Matrix matrix;
            Bitmap scaledBitmap, angry, idle;

    public Enemy(){
        x = -300;//intial point off screen
        y = -300;
        inLight = false;//Set them to idle mode
        Spotted = false;
        Idle = true;
        matrix = new Matrix();
        angry = Bitmap.createScaledBitmap(Assets.angryEnemy.getBitMap(), Assets.Enemy.getWidth()
                , Assets.Enemy.getHeight(), true);//change sprite
        idle =  Bitmap.createScaledBitmap(Assets.Enemy.getBitMap(), Assets.Enemy.getWidth()
                , Assets.Enemy.getHeight(), true);//change sprite
    }

    public boolean collisionCheck(int radius){//if they hit the player
        return Math.sqrt(Math.pow(Math.abs(x - 400),2)+Math.pow(Math.abs(y - 640),2))<100+radius;
    }
    //Move them somewhere based on their mood
    public void updatePositionAI(int quadrant, float screenX, float screenY, int areaWidth,
                                 int areaHeight, float normDeltaX, float normDeltaY){
        if(Spotted){
            //subtract their width and height to the center so their centers come after you.
            velocityX = (normalize((x+radius)-400-radius, (y+radius)-640-radius, 1))*35;
            velocityY = (normalize((x+radius)-400-radius, (y+radius)-640-radius, 2))*35;
            x-= velocityX+normDeltaX;
            y-= velocityY+normDeltaY;

        }
        //Where is the enemy going when they have nowhere to go
        else if (Idle){
            //Each quadrant will change their point to follow based on which quadrant it is
            //Every 2000 milliseconds
            switch(quadrant){
                case 1: {
                    if (timePassed > currentTime + 2000) {
                        pX[quadrant - 1] = rX.nextInt((areaWidth / 2 - 400)) + screenX + 400;
                        pY[quadrant - 1] = rY.nextInt((areaHeight / 2 - 640)) + screenY + 640;
                        currentTime = timePassed;
                    }
                }

                case 2:{
                    if(timePassed>currentTime+2000) {
                        pX[quadrant-1] = rX.nextInt((areaWidth/2))+screenX+areaWidth/2;
                        pY[quadrant-1] = rY.nextInt((areaHeight / 2 - 640)) + screenY + 640;
                        currentTime = timePassed;
                    }
                }

                case 3:{
                    if(timePassed>currentTime+2000) {
                        pX[quadrant-1] = rX.nextInt((areaWidth / 2 - 400)) + screenX + 400;
                        pY[quadrant-1] = rY.nextInt((areaHeight))+screenY+areaHeight/2;
                        currentTime = timePassed;
                    }
                }
                case 4:{
                    if(timePassed>currentTime+2000) {
                        pX[quadrant-1] = rX.nextInt((areaWidth/2))+screenX+areaWidth/2;
                        pY[quadrant-1] = rY.nextInt((areaHeight/2))+screenY+areaHeight/2;
                        currentTime = timePassed;
                    }
                }


            }//end switch

            timePassed = System.currentTimeMillis();//set the time
            //which way should it go
            dVelocityX = normalize(x - pX[quadrant - 1], y - pY[quadrant - 1], 1) / 10;
            dVelocityY = normalize(x - pX[quadrant - 1], y - pY[quadrant - 1], 2) / 10;

            //if it's greater than the upper bound velocity
            velocityX = velocityX > 2 ? 2 : velocityX + (dVelocityX);
            velocityY = velocityY > 2 ? 2 : velocityY + (dVelocityY);

            //if it's lower than the lower bound velocity
            velocityX = velocityX < -2 ? -2 : velocityX + (dVelocityX);
            velocityY = velocityY < -2 ? -2 : velocityY + (dVelocityY);
            //Position update
            x -= velocityX + normDeltaX;
            y -= velocityY + normDeltaY;
        }//end if

        //Bitmap to rotate
        if(Spotted){
            scaledBitmap = angry;
        }
        else {
            scaledBitmap = idle;
        }

        //Rotate a matrix that is mapping out the sprite, so it can be placed back on as the sprite
        //Allows for the sprite to face where its going
        matrix.reset();
        matrix.postTranslate(-scaledBitmap.getWidth()/2, -scaledBitmap.getHeight()/2);
        matrix.postRotate((float)Math.toDegrees(Math.atan2(-velocityY,-velocityX)));
        matrix.postTranslate(x, y);

    }//end AI movement
    public float normalize(float deltaX, float deltaY, int xory){//Normalize function
        if(xory == 1){
            return (float)(deltaX / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
        }
        else {
            return (float)(deltaY / Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
        }
    }
    public Bitmap getScaledBitmap(){
        return scaledBitmap;
    }
    public Matrix getMatrix(){
        return matrix;
    }
}//end class

