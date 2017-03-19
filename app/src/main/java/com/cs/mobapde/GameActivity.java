package com.cs.mobapde;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.cs.mobapde.canvas.CanvasButton;
import com.cs.mobapde.canvas.GameObject;
import com.cs.mobapde.canvas.Player;
import com.cs.mobapde.canvas.Powerup;
import com.cs.mobapde.canvas.Shot;
import com.cs.mobapde.canvas.Target;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;

/**
 * Created by Vincent on 03/13/2017.
 */

public class GameActivity extends AppCompatActivity implements SensorEventListener{

    GameScreen gameScreen;
    GameLogic gameLogic;

    boolean isRunning;
    int speedModifier = 1;

    boolean gameOver = false;

    float time = 0;
    float timePrevious = 0;

    float spawnThresh = 5;
    float spawnThreshInc = 5;
    float spawnRate = 1;
    float shotCooldown = (float)0.25;

    float timerSlow = 0;
    float timerHaste = 0;
    float timerVoid = 0;

    float score = 0;

    boolean shootRed;
    boolean shootGreen;
    boolean shootBlue;

    Player player;
    ArrayList<GameObject> gameObjects;
    ArrayList<Target> targets;
    ArrayList<Shot> shots;
    ArrayList<Powerup> powerups;

    CanvasButton button1;
    CanvasButton button2;
    CanvasButton button3;

    /* Accelorometer Stuff*/
    Sensor sensor;
    SensorManager sensorManager;

    /*Controls*/
    Double vectorX;
    Double vectorY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hideActionBar();//This hides actionbar

        initializeSensors();//For Accelerometer

        shootRed = false;
        shootGreen = false;
        shootBlue = false;

        button1 = new CanvasButton(getResources(), "red");
        button2 = new CanvasButton(getResources(), "green");
        button3 = new CanvasButton(getResources(), "blue");

        gameLogic = new GameLogic();
        gameScreen = new GameScreen(this);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("TimerTask executing counter is: " + time);
                time += 0.001;
                shotCooldown -= 0.001;
            }
        };
        Timer timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask, 1, 1);

        setContentView(gameScreen);
    }

    @Override
    protected void onPause() {
        super.onPause();

        isRunning = false;
        gameScreen.pause();
        gameLogic.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isRunning = true;
        gameScreen.resume();
        gameLogic.resume();
    }


    public class GameScreen extends SurfaceView implements Runnable, View.OnTouchListener{

        Thread thread;
        SurfaceHolder surfaceHolder;

        public GameScreen(Context context) {
            super(context);

            this.setOnTouchListener(this);
            surfaceHolder = getHolder();
        }

        @Override
        public void run() {
            while(isRunning) {
                if(!surfaceHolder.getSurface().isValid()) {
                    continue;
                }

                Canvas canvas = surfaceHolder.lockCanvas();

                render(canvas);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            try {
                thread.join();
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void resume() {
            thread = new Thread(this);
            thread.start();
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            if(isPressingButton(button1, event)) {
                //SPAWN RED BULLET
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    shootRed = true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    shootRed = false;
                }
            }
            else if (isPressingButton(button2, event)) {
                //SPAWN GREEN BULLET
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    shootGreen = true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    shootGreen = false;
                }
            }
            else if (isPressingButton(button3, event)) {
                //SPAWN BLUE BULLET
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    shootBlue = true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    shootBlue = false;
                }
            }
            else {
                gameLogic.movePlayer(event);
            }

            return true;
        }


        public void render(Canvas canvas) {
            canvas.drawARGB(255, 255, 255, 255);

            canvas.drawBitmap(button1.getSprite(), button1.getxCoord(), button1.getyCoord(), null);
            canvas.drawBitmap(button2.getSprite(), button2.getxCoord(), button2.getyCoord(), null);
            canvas.drawBitmap(button3.getSprite(), button3.getxCoord(), button3.getyCoord(), null);

            for(int i = 0; i < gameObjects.size(); i++) {
                canvas.drawBitmap(gameObjects.get(i).getSprite(), gameObjects.get(i).getxCoord() - gameObjects.get(i).getWidth()/2, gameObjects.get(i).getyCoord() - gameObjects.get(i).getHeight()/2, null);
            }
        }

        public boolean isPressingButton(CanvasButton button, MotionEvent event) {
            if(event.getX() > button.getxCoord() && event.getX() < button.getxCoord()+button.getWidth() && event.getY() > button.getyCoord() && event.getY() < button.getyCoord()+button.getHeight()) {
                return true;
            }

            return false;
        }
    }

    public class GameLogic implements Runnable {

        Thread thread;
        float timeDelta;

        public GameLogic () {
            init();
        }

        @Override
        public void run() {
            while(isRunning) {
                timeDelta = time - timePrevious;
                timePrevious = time;

                if(timerSlow == 0) {
                    speedModifier = 1;
                }
                if(timerHaste == 0) {
                    player.setSpeed(30);
                }

                spawnTarget();

                if(shotCooldown < 0) {
                    if (shootRed == true) {
                        Shot temp = new Shot(getResources(), "red", player);
                        gameObjects.add(temp);
                        shots.add(temp);

                        shotCooldown = (float)0.25;
                    }

                    if (shootGreen == true) {
                        Shot temp = new Shot(getResources(), "green", player);
                        gameObjects.add(temp);
                        shots.add(temp);

                        shotCooldown = (float)0.25;
                    }

                    if (shootBlue == true) {
                        Shot temp = new Shot(getResources(), "blue", player);
                        gameObjects.add(temp);
                        shots.add(temp);

                        shotCooldown = (float)0.25;
                    }
                }

                for(int i = 0; i < targets.size(); i++) {
                    trackPlayer(targets.get(i));

                    if(circleCollision(player, targets.get(i))) {
                        Log.v("COLLISION", "Player and Target");
                        targets.get(i).decrementHp();
                        player.decrementHp();
                    }
                }

                for(int i = 0; i < gameObjects.size(); i++) {
                    move(gameObjects.get(i));
                }


                shotAndTargetKillConditions();

                for(int i = 0; i < shots.size(); i++) {
                    if(shots.get(i).isDead()) {
                        gameObjects.remove(shots.get(i));
                        shots.remove(i);
                        i--;
                    }
                }

                for(int i = 0; i < targets.size(); i++) {
                    if(targets.get(i).isDead()) {
                        score = 10 * spawnRate;
                        gameObjects.remove(targets.get(i));
                        targets.remove(i);
                        i--;
                    }
                }
            }
        }

        public void pause() {
            try {
                thread.join();
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void resume() {
            thread = new Thread(this);
            thread.start();
        }

        public void init() {
            gameObjects = new ArrayList<>();
            targets = new ArrayList<>();
            shots = new ArrayList<>();

            player = new Player(getResources());
            gameObjects.add(player);
        }

        public void trackPlayer(Target target) {
            float distX = player.getxCoord() - target.getxCoord();
            float distY = player.getyCoord() - target.getyCoord();
            float dist = (float)Math.sqrt((distX*distX) + (distY*distY));

            target.setxVector(distX / dist);
            target.setyVector(distY / dist);
        }

        public boolean circleCollision(GameObject obj1, GameObject obj2) {
            double x = (obj1.getxCoord() + obj1.getWidth()/2) - (obj2.getxCoord() + obj2.getWidth()/2);
            double y = (obj1.getyCoord() + obj1.getHeight()/2) - (obj2.getyCoord() + obj2.getHeight()/2);
            double dist = Math.sqrt(x*x + y*y);

            if(dist > (obj1.getRadius() + obj2.getRadius())) {
                return false;
            }
            else {
                return true;
            }
        }

        public boolean checkOutOfBounds(GameObject obj) {
            if(obj.getxCoord() > gameScreen.getHeight() || obj.getxCoord() < 0 || obj.getyCoord() > gameScreen.getWidth() || obj.getyCoord() < 0) {
                return true;
            }

            return false;
        }

        public void movePlayer(MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    player.setxCoord(event.getX());
                    player.setyCoord(event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    player.setxCoord(event.getX());
                    player.setyCoord(event.getY());
                    break;
            }
        }

        public void movePlayer(Float xVector, Float yVector) {

            player.setxVector(xVector*player.getSpeed());
            player.setyVector(yVector*player.getSpeed());

            player.setRotation( Math.toDegrees(Math.atan2(yVector,xVector)));
            //System.out.println(player.getRotation());

        }

        public void move(GameObject gameObject) {
            gameObject.move(speedModifier, timeDelta);
        }

        public void spawnTarget() {
            if(time > spawnThresh) {
                if(spawnThreshInc < 2) {
                    spawnRate++;
                    spawnThreshInc = 5;
                }
                else {
                    spawnThreshInc--;
                }

                for(int i = 0; i < spawnRate; i++) {
                    Target temp = new Target(getResources());

                    if(Math.floor(Math.random()*10) % 2 == 0) {
                        if(Math.floor(Math.random()*10) % 2 == 0) {
                            temp.setxCoord(0);
                            temp.setyCoord((float)Math.floor(Math.random()*gameScreen.getHeight()));
                        }
                        else {
                            temp.setxCoord(gameScreen.getWidth()-50);
                            temp.setyCoord((float)Math.floor(Math.random()*gameScreen.getHeight()));
                        }
                    }
                    else {
                        if(Math.floor(Math.random()*10) % 2 == 0) {
                            temp.setyCoord(0);
                            temp.setxCoord((float)Math.floor(Math.random()*gameScreen.getWidth()));
                        }
                        else {
                            temp.setyCoord(gameScreen.getHeight()-50);
                            temp.setxCoord((float)Math.floor(Math.random()*gameScreen.getWidth()));
                        }
                    }

                    targets.add(temp);
                    gameObjects.add(temp);
                }

                spawnThresh += spawnThreshInc;
            }
        }

        public void shotAndTargetKillConditions() {
            for(int i = 0; i < shots.size(); i++) {
                for(int j = 0; j < targets.size(); j++) {
                    if(circleCollision(shots.get(i), targets.get(j))) {
                        shots.get(i).decrementHp();

                        if(shots.get(i).getColor() == targets.get(j).getColor() || shots.get(i).getColor() == "black") {
                            targets.get(j).decrementHp();
                        }
                        else {
                            targets.get(j).empower();
                        }
                    }

                    checkOutOfBounds(targets.get(j));
                }

                checkOutOfBounds(shots.get(i));
            }
        }
    }

    public void hideActionBar(){// yes
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }


    /** FUNCTIONS FOR ACCELELLOROMETER*/

    public void initializeSensors(){

        //Sensor Manager
        this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //actual Sensor
        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        /*Times -1 for Quadrants*/

        this.vectorX = getMinMax(event.values[0] * -0.1, -1.0, 1.0);
        this.vectorY = getMinMax(event.values[1] * 0.1, -1.0, 1.0);

        gameLogic.movePlayer(Float.parseFloat(this.vectorX.toString()),Float.parseFloat(this.vectorY.toString()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Double getMinMax(Double val, Double min, Double max){
        if(val > max)
            val = max;
        if(val<min)
            val = min;

        return val;
    }

}
