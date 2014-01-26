package com.cocross;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
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
	
	private long lastUpdate;

	private enum SHAKE_MODE {
		START, PAUSE
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
		
		lastUpdate = System.currentTimeMillis();

	}

	public void onFinishClicked(View v) {
		SubmitDialog mDialog = SubmitDialog.newInstance("Dialog Message",
				"AMRAP");
		mDialog.show(getSupportFragmentManager(), "qr_dialog");
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

			else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				float[] values = event.values;
			    // Movement
			    float x = values[0];
			    float y = values[1];
			    float z = values[2];

			    float accelationSquareRoot = (x * x + y * y + z * z)
			        / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
			    long actualTime = System.currentTimeMillis();
			    if (accelationSquareRoot >= 2) //
			    {
			      if (actualTime - lastUpdate < 200) {
			        return;
			      }
			      lastUpdate = actualTime;
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
			}
		}
	};

}
