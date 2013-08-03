package org.sidibe.android.pushlib.client;

import org.sidibe.mqtt.android.lib.MqttAndroidClient;
import org.sidibe.mqtt.android.lib.MqttClientState;
import org.sidibe.mqtt.android.lib.MqttMessage;
import org.sidibe.mqtt.android.lib.MqttTopic;
import org.sidibe.mqtt.android.lib.PushListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Sample use of Mosquitto Client Service.
 * 
 * @author jbromo
 * 
 */
public class MosquittoDemoService extends Service implements PushListener {

	private MqttAndroidClient mqttAndroidClient;
	private static final String HOST = "your host";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mqttAndroidClient = new MqttAndroidClient(this, HOST);
		mqttAndroidClient.addPushListener(this);
		mqttAndroidClient.addTopic(new MqttTopic("kalana"));
		mqttAndroidClient.start();
		return START_STICKY;
	}

	@Override
	public void onPushMessageReceived(MqttMessage mqttMessage) {

		// Do something with mqttMessage. You can broadcast to your application,
		// show as notification

	}

	@Override
	public void onConnectionStateChanged(MqttClientState connectionState) {

		// Do something with with connection state change.You can notify client
		// that connection was lost

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Not necessary to disconnect to the broker
		mqttAndroidClient.disconnect();
	}

}
