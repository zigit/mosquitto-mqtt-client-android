package org.sidibe.android.pushlib.client;

import java.util.ArrayList;

import org.sidibe.android.pushlib.client.demo.R;
import org.sidibe.mqtt.android.lib.MqttClientState;
import org.sidibe.mqtt.android.lib.MqttMessage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DemoActivity extends Activity {

	private MqttDataReceiver dataReceiver;
	private final MessageListAdapter messageAdapter = new MessageListAdapter(
	        new ArrayList<MqttMessage>());
	private TextView connectionStateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		connectionStateView = (TextView) findViewById(R.id.text_status);
		setupIncommingMessagePart();
		boolean result = retrieveIfLaunchFromNotification(getIntent());
		if (!result) {
			addBroadCastReceiver();
		}
		Intent intent = new Intent(this, MqttBroadCastService.class);
		startService(intent);
	}

	private boolean retrieveIfLaunchFromNotification(Intent intent) {
		if (intent == null) {
			return false;
		}
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			return false;
		}
		MqttClientState clientState = (MqttClientState) bundle
		        .getSerializable(MqttBroadCastService.KEY_STATE);

		if (clientState != null) {
			setupConnectionStatePart(clientState);
			return true;
		}

		MqttMessage mqttMessage = (MqttMessage) bundle
		        .getParcelable(MqttBroadCastService.KEY_DATA);
		if (mqttMessage != null) {
			messageAdapter.add(mqttMessage);
			return true;
		}
		return false;

	}

	private void addBroadCastReceiver() {
		IntentFilter intentCFilter = new IntentFilter(
		        MqttBroadCastService.ACTION_PUSH_ARRIVED);
		intentCFilter.addAction(MqttBroadCastService.ACTION_STATE_CHANGED);
		intentCFilter.addAction(MqttBroadCastService.ACTION_CLIEND_ID);
		dataReceiver = new MqttDataReceiver();
		registerReceiver(dataReceiver, intentCFilter);

	}

	public void gotoPage(View v) {
		Intent i = new Intent(this, PushActivity.class);
		startActivity(i);
	}

	private void setupConnectionStatePart(MqttClientState clientState) {
		connectionStateView.setText(clientState.name());

	}

	private void showGeneratedClientId(String clientID) {
		TextView clientId = (TextView) findViewById(R.id.tv_client_id);
		clientId.setText(clientID);
	}

	private void setupIncommingMessagePart() {
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(messageAdapter);
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
				return;

			}
			if (MqttBroadCastService.ACTION_STATE_CHANGED
			        .equalsIgnoreCase(action)) {
				MqttClientState connectionState = (MqttClientState) intent
				        .getSerializableExtra(MqttBroadCastService.KEY_STATE);
				setupConnectionStatePart(connectionState);
				return;

			}
			if (MqttBroadCastService.ACTION_CLIEND_ID.equalsIgnoreCase(action)) {
				String clientID = intent
				        .getStringExtra(MqttBroadCastService.KEY_CLIENT_ID);
				showGeneratedClientId(clientID);
				return;

			}
		}

	}
}
