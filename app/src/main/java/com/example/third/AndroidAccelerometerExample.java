package com.example.third;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;


public class AndroidAccelerometerExample extends Activity implements SensorEventListener {
    

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    double max=50;

    private float vibrateThreshold = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    HalfGauge halfGaugeX,halfGaugeY,halfGaugeZ;
    ArcGauge arcGaugeX,arcGaugeY,arcGaugeZ;

        Range rangeHalfGaugeX,rangeHalfGaugeY,rangeHalfGaugeZ ;
        Range rangeArcGaugeX,rangeArcGaugeY,rangeArcGaugeZ;
    public Vibrator v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rangeHalfGaugeX = new Range();
        rangeHalfGaugeY = new Range();
        rangeHalfGaugeZ = new Range();

        rangeArcGaugeX = new Range();
        rangeArcGaugeY = new Range();
        rangeArcGaugeZ = new Range();
        initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {

        }


        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);

        halfGaugeX = findViewById(R.id.halfGaugeX);
        halfGaugeY = findViewById(R.id.halfGaugeY);
        halfGaugeZ = findViewById(R.id.halfGaugeZ);

        arcGaugeX = findViewById(R.id.arcGaugeXAxis);
        arcGaugeY = findViewById(R.id.arcGaugeYAxis);
        arcGaugeZ = findViewById(R.id.arcGaugeZAxis);


        rangeHalfGaugeX.setFrom(0.0);
        rangeHalfGaugeY.setFrom(0.0);
        rangeHalfGaugeZ.setFrom(0.0);

        rangeArcGaugeX.setFrom(0.0);
        rangeArcGaugeY.setFrom(0.0);
        rangeArcGaugeZ.setFrom(0.0);



    }


    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        displayCleanValues();

        displayCurrentValues();

        displayMaxValues();


        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);


        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if ((deltaZ > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            v.vibrate(50);
        }
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");

        rangeArcGaugeX.setFrom(0.0);
        rangeArcGaugeY.setFrom(0.0);
        rangeArcGaugeZ.setFrom(0.0);

        rangeHalfGaugeX.setFrom(0.0);
        rangeHalfGaugeY.setFrom(0.0);
        rangeHalfGaugeZ.setFrom(0.0);

        rangeArcGaugeX.setTo(max);
        rangeArcGaugeY.setTo(max);
        rangeArcGaugeZ.setTo(max);

        rangeHalfGaugeX.setTo(max);
        rangeHalfGaugeY.setTo(max);
        rangeHalfGaugeZ.setTo(max);

        halfGaugeX.addRange(rangeHalfGaugeX);
        halfGaugeY.addRange(rangeHalfGaugeY);
        halfGaugeZ.addRange(rangeHalfGaugeZ);

        arcGaugeX.addRange(rangeArcGaugeX);
        arcGaugeY.addRange(rangeHalfGaugeY);
        arcGaugeZ.addRange(rangeArcGaugeZ);


    }


    public void displayCurrentValues() {


        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));

        halfGaugeX.setValue(deltaX);
        halfGaugeY.setValue(deltaY);
        halfGaugeZ.setValue(deltaZ);

        arcGaugeX.setValue(deltaX);
        arcGaugeY.setValue(deltaY);
        arcGaugeZ.setValue(deltaZ);






    }


    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            halfGaugeX.setMaxValue(deltaXMax);
            arcGaugeX.setMaxValue(deltaXMax);
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            halfGaugeY.setMaxValue(deltaYMax);
            arcGaugeY.setMaxValue(deltaYMax);
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            halfGaugeZ.setMaxValue(deltaZMax);
            arcGaugeZ.setMaxValue(deltaZMax);
            maxZ.setText(Float.toString(deltaZMax));
        }
    }
}
