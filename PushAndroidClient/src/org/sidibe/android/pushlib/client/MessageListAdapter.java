package org.sidibe.android.pushlib.client;

import java.util.ArrayList;
import java.util.List;

import org.sidibe.android.pushlib.client.demo.R;
import org.sidibe.mqtt.android.lib.MqttMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter {

	List<MqttMessage> messages = new ArrayList<MqttMessage>();

	public MessageListAdapter(List<MqttMessage> messages) {
		this.messages = messages;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MqttMessage message = messages.get(position);
		MessageItemView itemView = null;
		if (convertView != null && convertView instanceof MessageItemView) {
			itemView = (MessageItemView) convertView;
			itemView.reuse(message);
			return itemView;
		}
		return new MessageItemView(parent.getContext(), message);
	}

	class MessageItemView extends LinearLayout {
		TextView value;

		public MessageItemView(Context context, MqttMessage message) {
			super(context);
			LayoutInflater.from(context).inflate(R.layout.message_item, this,
			        true);
			value = (TextView) findViewById(R.id.message);
			reuse(message);
		}

		private void reuse(MqttMessage message) {
			value.setText(message.toString());

		}

	}

	public void add(MqttMessage mqttMessage) {
		if (!messages.contains(mqttMessage)) {
			messages.add(mqttMessage);
			notifyDataSetChanged();
		}

	}

}
