package org.sidibe.android.pushlib.client;

import org.sidibe.android.pushlib.client.demo.R;
import org.sidibe.mqtt.android.lib.MqttMessage;
import org.sidibe.mqtt.android.lib.MqttTopic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PushActivity extends Activity {

	private ServiceConnecter serviceConnecter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_push);
		serviceConnecter = new ServiceConnecter();
		serviceConnecter.bind(this);

	}

	public void onTopicSubmitted(View v) {
		if (!serviceConnecter.IsConnected()) {
			Toast.makeText(this, "Your are not connected", Toast.LENGTH_SHORT)
			        .show();
			return;
		}
		EditText editText = (EditText) findViewById(R.id.edit_field_topic);
		String topicName = editText.getText().toString();
		if (topicName == null || topicName.length() < 1) {
			Toast.makeText(this, "Topic Name empty", Toast.LENGTH_SHORT).show();
		} else {
			MqttTopic topic = new MqttTopic(topicName);
			serviceConnecter.subscribeToTopic(topic);
		}
	}

	public void onPushMessage(View v) {
		if (!serviceConnecter.IsConnected()) {
			Toast.makeText(this, "Your are not connected", Toast.LENGTH_SHORT)
			        .show();
			return;
		}

		EditText editText = (EditText) findViewById(R.id.message_field_content);
		String message = editText.getText().toString();
		EditText topicField = (EditText) findViewById(R.id.message_field_topic);
		String topicName = topicField.getText().toString();
		if (message == null || message.length() < 1 || topicName == null
		        || topicName.length() < 1) {
			Toast.makeText(this, "Topic or message cannot be empty",
			        Toast.LENGTH_SHORT).show();
		} else {

			CheckBox checkBox = (CheckBox) findViewById(R.id.retain_checkbox);
			Spinner spinner = (Spinner) findViewById(R.id.qos_spinner);
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setContent(message);
			mqttMessage.setRetains(checkBox.isChecked());
			mqttMessage.setQos(spinner.getSelectedItemPosition());
			MqttTopic mqttTopic = new MqttTopic(topicName);
			serviceConnecter.publish(mqttMessage, mqttTopic);
		}
	}

}
