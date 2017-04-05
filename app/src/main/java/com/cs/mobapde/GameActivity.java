package com.cs.mobapde;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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

public class GameActivity extends AppCompatActivity implements SensorEventListener{

    GameScreen gameScreen;
    GameLogic gameLogic;

    LayoutInflater inflater;
    GameOverActivity gameOverActivity;

    DisplayMetrics dm;

    boolean isRunning;
    float speedModifier = 1;

    boolean gameOver = false;

    float time = 0;
    float timePrevious = 0;

    float spawnThresh = 1;
    float spawnRate = 1;
    float shotCooldown = (float)0.25;

    float powerupTimer = 10;

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
    Sensor accelerometer;
    Sensor magnetometer;
    SensorManager sensorManager;

    float[] accelOutput;
    float[] magnetOutput;

    float[] orientation= new float[3];
    float[] startOrientation;

    float startNegation = 1;

    /*Controls*/
    Double vectorX;
    Double vectorY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        initializePopUps();

        hideActionBar();//This hides actionbar

        initializeSensors();//For Accelerometer

        shootRed = false;
        shootGreen = false;
        shootBlue = false;

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        button1 = new CanvasButton(getResources(), "red",(int)(dm.widthPixels*.33));
        button2 = new CanvasButton(getResources(), "green",(int)(dm.widthPixels*.33));
        button3 = new CanvasButton(getResources(), "blue",(int)(dm.widthPixels*.33));
        gameLogic = new GameLogic();
        gameScreen = new GameScreen(this);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("TimerTask executing counter is: " + time);
                time += 0.001;
                shotCooldown -= 0.001;
                timerSlow -= 0.001;
                timerHaste -= 0.001;
                timerVoid -= 0.001;
                powerupTimer -= 0.001;
            }
        };
        Timer timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask, 1, 1);

        setContentView(gameScreen);

        startOrientation=null;
    }

    @Override
    protected void onPause() {
        super.onPause();

        pauseSensors();

        isRunning = false;
        gameScreen.pause();
        gameLogic.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerSensors();

        isRunning = true;
        gameScreen.resume();
        gameLogic.resume();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if(data.getStringExtra("result").equals("retry")) {
                    player.setHp(1);
                    this.gameLogic.init();
                }
                else if(data.getStringExtra("result").equals("home")) {
                    finish();
                }
                else{
                    finish();
                }
            }
        }
    }

//    public void initializePopUps(){
//
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//
//
//        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View gameOverLayout = (ViewGroup)inflater.inflate(R.layout.activity_game_over,null);
//        gameOverActivity = new GameOverActivity(this,gameOverLayout,(RelativeLayout)findViewById(R.id.activity_game),width,height);
//
//
//    }

    public class GameScreen extends SurfaceView implements Runnable, View.OnTouchListener{

        Thread thread;
        SurfaceHolder surfaceHolder;
        Bitmap bg;

        public GameScreen(Context context) {
            super(context);


            bg = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(getResources(), R.drawable.long_bg4), dm.widthPixels, dm.heightPixels, true);
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

            canvas.drawBitmap(bg, 0, 0, null);

            drawButtons(canvas);
            drawRotatedPlayer(canvas);

            for(int i = 0; i < gameObjects.size(); i++) {
                canvas.drawBitmap(gameObjects.get(i).getSprite(), gameObjects.get(i).getxCoord() - gameObjects.get(i).getWidth()/2, gameObjects.get(i).getyCoord() - gameObjects.get(i).getHeight()/2, null);
            }
        }

        public void drawRotatedPlayer(Canvas canvas) {
            canvas.save(Canvas.MATRIX_SAVE_FLAG); //Saving the canvas and later restoring it so only this image will be rotated.
            canvas.translate(player.getxCoord(), player.getyCoord());
            canvas.rotate(player.getRotation());
            canvas.translate(-player.getxCoord(), -player.getyCoord());
            canvas.drawBitmap(player.getSprite(), player.getxCoord()-player.getWidth()/2, player.getyCoord()-player.getHeight()/2, null);
            canvas.restore();
        }

        public void drawButtons(Canvas canvas) {
            button1.setyCoord(canvas.getHeight()-150);
            button2.setyCoord(canvas.getHeight()-150);
            button3.setyCoord(canvas.getHeight()-150);

            button1.setxCoord(0);
            button2.setxCoord((int)(canvas.getWidth()/2)-(button2.getWidth()/2));
            button3.setxCoord(canvas.getWidth()-button3.getWidth());

            canvas.drawBitmap(button1.getSprite(), button1.getxCoord(), button1.getyCoord(), null);
            canvas.drawBitmap(button2.getSprite(), button2.getxCoord(), button2.getyCoord(), null);
            canvas.drawBitmap(button3.getSprite(), button3.getxCoord(), button3.getyCoord(), null);
        }

        public boolean isPressingButton(CanvasButton button, MotionEvent event) {
            return (event.getX() > button.getxCoord() && event.getX() < button.getxCoord()+button.getWidth() && event.getY() > button.getyCoord() && event.getY() < button.getyCoord()+button.getHeight());
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

                if(timerSlow <= 0) {
                    speedModifier = 1;
                }
                if(timerHaste <= 0) {
                    player.setSpeed(3000);
                }

                spawnTarget();
                spawnPowerup();

                if(shotCooldown < 0) {
                    if(timerVoid > 0) {
                        Shot temp = new Shot(getResources(), "chroma", player);
                        gameObjects.add(temp);
                        shots.add(temp);

                        shotCooldown = (float) 0.25;
                    }
                    else {
                        if (shootRed) {
                            shootRed = false;
                            Shot temp = new Shot(getResources(), "red", player);
                            gameObjects.add(temp);
                            shots.add(temp);

                            shotCooldown = (float) 0.25;
                        }

                        if (shootGreen) {
                            shootGreen = false;
                            Shot temp = new Shot(getResources(), "green", player);
                            gameObjects.add(temp);
                            shots.add(temp);

                            shotCooldown = (float) 0.25;
                        }

                        if (shootBlue) {
                            shootBlue = false;
                            Shot temp = new Shot(getResources(), "blue", player);
                            gameObjects.add(temp);
                            shots.add(temp);

                            shotCooldown = (float) 0.25;
                        }
                    }
                }

                for(int i = 0; i < powerups.size(); i++) {
                    if(pointCircleCollision(player, powerups.get(i))) {
                        powerups.get(i).decrementHp();
                    }

                    if(powerups.get(i).isDead()) {
                        Log.v("POWERUP", "GOT: " + powerups.get(i).getName());
                        if(powerups.get(i).getName().equals("shield")) {
                            player.setHp(2);
                        }
                        else if(powerups.get(i).getName().equals("slow")) {
                            timerSlow = 5;
                            speedModifier = (float)0.5;
                        }
                        else if(powerups.get(i).getName().equals("haste")) {
                            timerHaste = 5;
                            player.setSpeed(6000);
                        }
                        else if(powerups.get(i).getName().equals("void")) {
                            timerVoid = 5;
                        }

                        gameObjects.remove(powerups.get(i));
                        powerups.remove(powerups.get(i));
                        i--;
                    }
                }

                for(int i = 0; i < targets.size(); i++) {
                    trackPlayer(targets.get(i));

                    if(pointCircleCollision(player, targets.get(i))) {
                        Log.v("COLLISION", "Player and Target");
                        targets.get(i).decrementHp();
                        player.decrementHp();
                    }
                }

                player.move(speedModifier, timeDelta, 0, gameScreen.getWidth(), 0, gameScreen.getHeight()-200);
                for(int i = 0; i < gameObjects.size(); i++) {
                    gameObjects.get(i).move(speedModifier, timeDelta);
                }

                shotAndTargetCollision();

                for(int i = 0; i < shots.size(); i++) {
                    if(checkOutOfBounds(shots.get(i))) {
                        shots.get(i).decrementHp();
                    }

                    if(shots.get(i).isDead()) {
                        gameObjects.remove(shots.get(i));
                        shots.remove(i);
                        i--;
                    }
                }

                for(int i = 0; i < targets.size(); i++) {
                    if(checkOutOfBounds(targets.get(i))) {
                        targets.get(i).decrementHp();
                    }

                    if(targets.get(i).isDead()) {
                        score += 10 * spawnRate;
                        gameObjects.remove(targets.get(i));
                        targets.remove(i);
                        i--;
                    }
                }

                if(player.isDead()){
                    //TODO ADD Game over screen
//                    Handler handler = new Handler(Looper.getMainLooper());
//
//                    handler.postDelayed(new Runnable() {
//                       @Override
//                        public void run() {
//
//                            GameActivity.this.gameOverActivity.show((SurfaceView)GameActivity.this.findViewById(R.id.mainCanvas));
//                        }
//                    }, 1000 );
//                    pause();

                    gameOver=true;

                }
                if(gameOver){
                    isRunning=false;
                    Intent i = new Intent(GameActivity.this,GameOverActivity.class);
                    i.putExtra("score", score+"");
                    startActivityForResult(i,1);
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
            startOrientation = null;
            score = 0;
            gameObjects = new ArrayList<>();
            targets = new ArrayList<>();
            shots = new ArrayList<>();
            powerups = new ArrayList<>();

            player = new Player(getResources());

            speedModifier = 1;

            gameOver = false;

            time = 0;
            timePrevious = 0;

            spawnThresh = 1;
            spawnRate = 1;
            shotCooldown = (float)0.25;

            timerSlow = 0;
            timerHaste = 0;
            timerVoid = 0;

            score = 0;
        }

        private void trackPlayer(Target target) {
            float distX = player.getxCoord() - target.getxCoord();
            float distY = player.getyCoord() - target.getyCoord();
            float dist = (float)Math.sqrt((distX*distX) + (distY*distY));

            target.setxVector(distX / dist);
            target.setyVector(distY / dist);
        }

        private boolean circleCollision(GameObject obj1, GameObject obj2) {
            double x = (obj1.getxCoord() + obj1.getWidth()/2) - (obj2.getxCoord() + obj2.getWidth()/2);
            double y = (obj1.getyCoord() + obj1.getHeight()/2) - (obj2.getyCoord() + obj2.getHeight()/2);
            double dist = Math.sqrt(x*x + y*y);

            return (dist < (obj1.getRadius() + obj2.getRadius()));
        }

        private boolean pointCircleCollision(GameObject obj1, GameObject obj2) {
            double x = (obj1.getxCoord() + obj1.getWidth()/2) - (obj2.getxCoord() + obj2.getWidth()/2);
            double y = (obj1.getyCoord() + obj1.getHeight()/2) - (obj2.getyCoord() + obj2.getHeight()/2);
            double dist = Math.sqrt(x*x + y*y);

            return (dist < obj2.getRadius());
        }

        private boolean checkOutOfBounds(GameObject obj) {
            return (obj.getyCoord() > gameScreen.getHeight()-200 || obj.getxCoord() < 0 || obj.getxCoord() > gameScreen.getWidth() || obj.getyCoord() < 0);
        }

        private void movePlayer(MotionEvent event) {
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

        private void movePlayer(Float xVector, Float yVector) {
            player.setxVector(xVector);
            player.setyVector(yVector);

            player.setRotation((float)Math.toDegrees(Math.atan2(yVector,xVector)));
            //System.out.println(player.getRotation());

        }

        private void spawnTarget() {
            if(targets.size() < spawnThresh) {
                if(spawnRate > 6) {
                    spawnThresh++;
                    spawnRate = 1;
                }
                else {
                    spawnRate++;
                }

                for (int i = 0; i < spawnRate; i++) {
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
                            temp.setyCoord(gameScreen.getHeight()-250);
                            temp.setxCoord((float)Math.floor(Math.random()*gameScreen.getWidth()));
                        }
                    }

                    gameObjects.add(temp);
                    targets.add(temp);
                }
            }
        }

        private void spawnPowerup() {
            if(powerupTimer <= 0) {
                Log.v("POWERUPS", "SPAWN");
                powerupTimer = 10;
                Powerup temp = new Powerup(getResources(), gameScreen.getWidth(), gameScreen.getHeight());
                gameObjects.add(temp);
                powerups.add(temp);
            }
        }

        private void shotAndTargetCollision() {
            for(int i = 0; i < shots.size(); i++) {
                for(int j = 0; j < targets.size(); j++) {
                    if(circleCollision(shots.get(i), targets.get(j))) {
                        shots.get(i).decrementHp();

                        if(shots.get(i).getColor().equals(targets.get(j).getColor())  || shots.get(i).getColor().equals("chroma")) {
                            targets.get(j).decrementHp();
                        }
                        else {
                            targets.get(j).empower();
                        }
                    }
                }
            }
        }
    }

    public void hideActionBar(){// yes
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    @Override
    public void onBackPressed() {
    }

    /** FUNCTIONS FOR ACCELELLOROMETER*/

    public void initializeSensors(){
        //Sensor Manager
        this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //actual Sensor
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        registerSensors();

    }

    public void registerSensors(){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pauseSensors(){
        this.sensorManager.unregisterListener(this);
    }


    public boolean is180=false;
    public int mark180 = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        /*Times -1 for Quadrants*/


        if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
            accelOutput = event.values;

            //System.out.println("A");
        }
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ){
            magnetOutput = event.values;
            //System.out.println("M");
        }



        if(accelOutput!=null&&magnetOutput!=null){
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,accelOutput,magnetOutput);



            if(success){
                SensorManager.getOrientation(R,this.orientation);
                if(startOrientation==null){
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation,0,startOrientation,0, orientation.length);

//                    reCalibrate();
                }
            }
        }

        if(orientation!=null&&startOrientation!=null){

            float pitch = orientation[1]-startOrientation[1]; //pi to -pi
            float roll = (orientation[2]-startOrientation[2]); // pi/2 o -pi/2


            System.out.println("Pitch = " +pitch);
            System.out.println("Roll = " +roll);


            this.vectorX = getMinMax(roll / Math.PI, -1.0, 1.0);
            this.vectorY = getMinMax(-pitch / Math.PI, -1.0, 1.0);


            gameLogic.movePlayer(Float.parseFloat(this.vectorX.toString()),Float.parseFloat(this.vectorY.toString()));
        }
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

//    public void reCalibrate(){
////        Log.e("RECALIBRATE",startOrientation[0]+" "+ startOrientation[1]+ " "+startOrientation[2]);
//
//        if(startOrientation[0]>=0){
//            startNegation = 1;
//        }
//        else
//            startNegation=-1;
//    }


}
