package org.sidibe.android.pushlib.client;

import org.sidibe.android.pushlib.client.demo.R;
import org.sidibe.mqtt.android.lib.MqttAndroidClient;
import org.sidibe.mqtt.android.lib.MqttClientState;
import org.sidibe.mqtt.android.lib.MqttMessage;
import org.sidibe.mqtt.android.lib.MqttTopic;
import org.sidibe.mqtt.android.lib.PushListener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;

public class MqttBroadCastService extends Service implements PushListener {

	private static final int NOTIFICATION_ID = 0x1212;
	private MqttAndroidClient mqttClient;
	public static final String ACTION_PUSH_ARRIVED = "action_push_arrived";
	public static final String ACTION_STATE_CHANGED = "action_state_changed";
	public static final String ACTION_PUSH_SEND = "action_push_send";
	public static final String KEY_STATE = "key_state";
	public static final String KEY_DATA = "key_data";
	private static final String HOST = "88.191.130.14";
	private PushSendBrodcast pushSendBroacast;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mqttClient = new MqttAndroidClient(this, HOST);
		mqttClient.addPushListener(this);
		mqttClient.addTopic(new MqttTopic("kalana"));
		mqttClient.start();
		pushSendBroacast = new PushSendBrodcast();
		registerReceiver(pushSendBroacast, new IntentFilter(
		        DemoActivity.DATA_TO_SEND));
		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mqttClient.disconnect();
		unregisterReceiver(pushSendBroacast);
	}

	@Override
	public void onPushMessageReceived(MqttMessage mqttMessage) {
		Intent broadcastIntent = new Intent(ACTION_PUSH_ARRIVED);
		broadcastIntent.putExtra(KEY_DATA, (Parcelable) mqttMessage);
		sendBroadcast(broadcastIntent);
		showNotification(NOTIFICATION_ID + 1,
		        getString(R.string.mosquitto_incomming), mqttMessage.toString());

	}

	private void showNotification(int id, String title, String content) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		        this).setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title).setContentText(content);
		Intent resultIntent = new Intent(this, DemoActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(DemoActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
		        PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id, mBuilder.build());
	}

	@Override
	public void onConnectionStateChanged(MqttClientState connectionState) {
		Intent broadcastIntent = new Intent(ACTION_STATE_CHANGED);
		broadcastIntent.putExtra(KEY_STATE, connectionState);
		sendBroadcast(broadcastIntent);
		showNotification(NOTIFICATION_ID,
		        getString(R.string.mosquitto_client_status),
		        connectionState.name());

	}

	private class PushSendBrodcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			String action = intent.getAction();
			if (ACTION_PUSH_SEND.equalsIgnoreCase(action)) {
				MqttMessage message = intent.getExtras().getParcelable(
				        MqttBroadCastService.KEY_DATA);
				mqttClient.push(message, message.getTopic());

			}

		}

	}

}
