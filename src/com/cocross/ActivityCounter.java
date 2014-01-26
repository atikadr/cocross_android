package com.cocross;

import com.cocross.utils.ParseProxyObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.FloatMath;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class ActivityCounter extends FragmentActivity {

	Sensor proxySensor;
	Sensor acceSensor;
	SensorManager sensorManager;

	int counter;

	Chronometer countingChronometer;

	TextView counterTextView;

	private SHAKE_MODE shakeMode;
	private boolean isLight = true;
	private boolean isFirstTime = true;
	
	private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
 
    private long mShakeTimestamp;

	private enum SHAKE_MODE {
		START, PAUSE, NON
	};

	long secondsWaiting = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		proxySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		acceSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sensorManager.registerListener(listener, proxySensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(listener, acceSensor,
				SensorManager.SENSOR_DELAY_GAME);

		counterTextView = (TextView) findViewById(R.id.counterTextView);

		countingChronometer = (Chronometer) findViewById(R.id.countingChronometer);

		mShakeTimestamp = System.currentTimeMillis();

	}

	public void onFinishClicked(View v) {
		String score = (String) ((TextView)findViewById(R.id.counterTextView)).getText();
		ParseProxyObject workout = (ParseProxyObject) getIntent().getSerializableExtra("workout");
		SubmitDialog mDialog = SubmitDialog.newInstance("Dialog Message",
				workout, score);
		mDialog.show(getSupportFragmentManager(), "qr_dialog");
	}

	public void onDiscardClicked(View v){
		finish();
	}
	private SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
				isLight = !isLight;
				if (!isLight) {
					counter++;
					counterTextView.setText(String.valueOf(counter));
				}
			}

			else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				
				if (listener != null) {
		            float x = event.values[0];
		            float y = event.values[1];
		            float z = event.values[2];
		 
		            float gX = x / SensorManager.GRAVITY_EARTH;
		            float gY = y / SensorManager.GRAVITY_EARTH;
		            float gZ = z / SensorManager.GRAVITY_EARTH;
		 
		            // gForce will be close to 1 when there is no movement.
		            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);
		 
		            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
		                final long now = System.currentTimeMillis();
		                // ignore shake events too close to each other (500ms)
		                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
		                    return;
		                }
		 
		                if(isFirstTime) {
		                	shakeMode = SHAKE_MODE.START;
		                	isFirstTime = false;
		                } else {
		                	shakeMode = SHAKE_MODE.PAUSE;
		                }
		 
		                mShakeTimestamp = now;
		                doAccordingToShakeMode(shakeMode);
		            }
				}
				
				
			}
		}

		private void doAccordingToShakeMode(SHAKE_MODE shakeMode) {

			if (shakeMode == SHAKE_MODE.START) {
				int stoppedMilliseconds = 0;

				String chronoText = countingChronometer.getText().toString();
				String array[] = chronoText.split(":");

				try {

					if (array.length == 2) {

						stoppedMilliseconds = Integer.parseInt(array[0]) * 60
								* 1000 + Integer.parseInt(array[1]) * 1000;

					} else if (array.length == 3) {

						stoppedMilliseconds = Integer.parseInt(array[0]) * 60
								* 60 * 1000 + Integer.parseInt(array[1]) * 60
								* 1000 + Integer.parseInt(array[2]) * 1000;
					}

				} catch (Exception e) {
					stoppedMilliseconds = 0;
				}

				countingChronometer.setBase(SystemClock.elapsedRealtime()
						- stoppedMilliseconds);

				secondsWaiting = Long.parseLong(array[1]);
				countingChronometer.start();

			} else if (shakeMode == SHAKE_MODE.PAUSE) {
				countingChronometer.stop();
			}
		}
	};

}
