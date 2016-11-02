package com.example.android.assignment_2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class AccelerometerService extends Service implements SensorEventListener {

    private SensorManager accelSensorManager;
    private Sensor sensorAccelerometer;

//    DatabaseHelper db = new DatabaseHelper(this);
    final Context context = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.d("In service","* * * * * * * * * * * * * * * * * * *");
        accelSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = accelSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Registers a SensorEventListener for the given sensor at the given sampling frequency.
        //registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs)
        accelSensorManager.registerListener(this, sensorAccelerometer,  SensorManager.SENSOR_DELAY_NORMAL );
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //get the sensor values
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //get the current timestamp
            Long tsLong = System.currentTimeMillis();
            //int timestamp = tsLong.intValue();

            //add in the database
            try{
                DatabaseHelper db = new DatabaseHelper(this);
                db.addAccValue(tsLong,x,y,z);
                //Toast toast = Toast.makeText(getApplicationContext(),"Successfully inserted", Toast.LENGTH_SHORT);
                //toast.show();

                Log.d("Called by service ","* * * * * * * * * * * * * * * * * * * \n **************************************\n" +
                        "********************************************");
            }
            catch(Exception ex){
                Toast.makeText(context, "Error in inserting data to db", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        accelSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
