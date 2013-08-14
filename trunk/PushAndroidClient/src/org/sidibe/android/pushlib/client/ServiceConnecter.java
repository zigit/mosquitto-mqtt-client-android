package org.sidibe.android.pushlib.client;

import org.sidibe.mqtt.android.lib.MqttMessage;
import org.sidibe.mqtt.android.lib.MqttTopic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceConnecter implements ServiceConnection {

	private MqttBroadCastService service;
	private boolean isBound = false;

	public void bind(Context context) {
		Intent intent = new Intent(context, MqttBroadCastService.class);
		context.bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	public void unbind(Context context) {
		context.unbindService(this);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		@SuppressWarnings("unchecked")
		LocalBinder<MqttBroadCastService> localBinder = (LocalBinder<MqttBroadCastService>) binder;
		service = localBinder.getService();
		isBound = true;

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		isBound = false;

	}

	public String getGeneratedClientId() {
		if (isBound) {
			return service.getGeneratedClient();
		}
		return null;
	}

	public void subscribeToTopic(MqttTopic mqttTopic) {
		if (isBound) {
			service.subscribeToTopic(mqttTopic);
		}
	}

	public void publish(MqttMessage mqttMessage, MqttTopic topic) {
		if (isBound) {
			service.publish(mqttMessage, topic);
		}
	}

	public boolean IsConnected() {
		return this.service.isConnected();
	}
}
