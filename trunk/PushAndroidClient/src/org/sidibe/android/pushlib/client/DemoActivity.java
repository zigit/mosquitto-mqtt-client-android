package org.sidibe.android.pushlib.client;

import org.sidibe.android.pushlib.client.demo.R;
import org.sidibe.mqtt.android.lib.MqttClientState;
import org.sidibe.mqtt.android.lib.MqttMessage;
import org.sidibe.mqtt.android.lib.MqttQoS;
import org.sidibe.mqtt.android.lib.MqttTopic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DemoActivity extends Activity {

	public static final String DATA_TO_SEND = "sendata";
	private MqttDataReceiver dataReceiver;
	private ArrayAdapter<MqttMessage> messageAdapter;
	private TextView connectionStateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		addBroadCastReceiver();
		Intent intent = new Intent(this, MqttBroadCastService.class);
		startService(intent);
		setupIncommingMessagePart();

	}

	private boolean  retrieveIfLaunchFromNotification(Intent intent) {
		boolean isLaunchedFromNotification=false;
		if(intent==null) {
	        return isLaunchedFromNotification;
        }
		
		MqttClientState clientState
		return null;
		
	}

	private void addBroadCastReceiver() {
		IntentFilter intentCFilter = new IntentFilter(
		        MqttBroadCastService.ACTION_PUSH_ARRIVED);
		intentCFilter.addAction(MqttBroadCastService.ACTION_STATE_CHANGED);
		dataReceiver = new MqttDataReceiver();
		registerReceiver(dataReceiver, intentCFilter);
		connectionStateView = (TextView) findViewById(R.id.text_status);

	}

	private void setupConnectionStatePart(MqttClientState clientState) {
		String message = getString(R.string.client_connection_status_);
		connectionStateView.setText(message + "  " + clientState.name());

	}

	private void setupIncommingMessagePart() {
		ListView listView = (ListView) findViewById(R.id.listView1);
		messageAdapter = new ArrayAdapter<MqttMessage>(this, android.R.id.text1);
		listView.setAdapter(messageAdapter);
	}

	private void sendMessage(String topicName, String messageText) {
		MqttTopic topic = new MqttTopic(topicName);
		MqttMessage message = new MqttMessage(topic, messageText);
		message.setQos(MqttQoS.DELIVERY_LEAST_WITH_CONFIRMATION);

		Intent broadcastIntent = new Intent(DATA_TO_SEND);
		broadcastIntent.putExtra(MqttBroadCastService.KEY_DATA,
		        (Parcelable) message);
		sendBroadcast(broadcastIntent);
	}

	public void OnPushButtonClicked(View v) {
		EditText topicField = (EditText) findViewById(R.id.edit_topicName_field);
		String topic = topicField.getText().toString();
		EditText messageField = (EditText) findViewById(R.id.edit_message_filed);
		String message = messageField.getText().toString();

		if (message != null && topic != null && message.trim().length() > 0
		        && topic.trim().length() > 0) {
			sendMessage(topic, message);
		} else {
			Toast.makeText(this, "We avoid to send empty thing to broker",
			        Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(dataReceiver);
	}

	private class MqttDataReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			String action = intent.getAction();
			if (MqttBroadCastService.ACTION_PUSH_ARRIVED
			        .equalsIgnoreCase(action)) {
				MqttMessage message = intent.getExtras().getParcelable(
				        MqttBroadCastService.KEY_DATA);
				messageAdapter.add(message);
				messageAdapter.notifyDataSetChanged();

			}
			if (MqttBroadCastService.ACTION_STATE_CHANGED
			        .equalsIgnoreCase(action)) {
				MqttClientState connectionState = (MqttClientState) intent
				        .getSerializableExtra(MqttBroadCastService.KEY_STATE);
				setupConnectionStatePart(connectionState);

			}
		}

	}
}
