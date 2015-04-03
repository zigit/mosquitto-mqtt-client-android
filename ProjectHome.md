Mosquitto MQTT Push solution in Android


Overview:
Mosquitto is an open source (BSD Licensed) message broker that implements the MQ Telemetry Transport protocol version 3.1. MQTT provides a lightweight method of carrying out messaging using a publish/subscribe model. This makes it suitable for "machine to machine" messaging such as with low power sensors or mobile devices such as phones, embedded computers or microcontrollers like the Arduino. A good example of this is all of the work that Andy Stanford-Clark (one of the originators of MQTT) has done in home monitoring and automation with his twittering house and twittering ferry.
For more information see http://mosquitto.org/
A simple usage of Mosquitto is to send push from browser to Mobile device described in the following example.





Mosquito implementation Android


The MQTT protocol is 10 years old now, and has been used in many ways.  It has found applications in healthcare, on Android phones and has recently been given a shot in the arm by the announcement that Facebook are using in their Application Messenger.
Installing Mosquitto
Installing Mosquitto is very simple under Ubuntu. By default Mosquito is not in Linux repository.  The simple and easy way is to add mosquitto package in your sources.list   before install. The following commands install Mosquito under Ubuntu.

1.	Add mosquito to your sources.list and update it:

> sudo apt-add-repository ppa:mosquitto-dev/mosquitto-ppa
> sudo apt-get update

2.	Install mosquitto:

> sudo apt-get install mosquitto

At this point installing Mosquitto is finished and you can use it in your application.
But there are also a couple of command line clients for testing your installation and play with your free amazing push server

3.	Installing Mosquitto client

> sudo apt-get install python-mosquitto
> sudo apt-get install mosquitto-clients

By default Mosquito listens the port 1883. You can specify your custom port by mosquitto  –p when starting.


Starting-Restart-Stop
Once Mosquitto installed, mosquitto should start automatically, anyway like many server, you can  start  it manually by the following command :

mosquitto start

On starting, you can specify some option for example, the listen port  by –p following by the port number of your choice.

Testing Installation
To check your installation, in your terminal you can type the command mosquitto
For correct installation you will see the following message on your terminal


Playing with Mosquito
Topics
A topic is a character string that describes the nature of the data that is published in a publish/subscribe system. Topics are key to the successful delivery of messages in a publish/subscribe system. Instead of including a specific destination address in each message, a publisher assigns a topic to the message. The message broker matches the topic with a list of clients (subscribers) who have subscribed to that topic, and delivers the message to each of those clients.
Note that a publisher can control which subscribers can receive a publication by choosing carefully the topic that is specified in the message.
A system administrator using the workbench can define topics. However, the topic of a message does not have to be defined before a publisher can use it; a topic can also be defined when it is specified in a publication for the first time.
More than one topic can be specified for a publication.
A topic string can include any character from the Unicode character set, including the space character. However, there are three characters that have special meanings. These characters ("/", "#", and "+") are described in Mosquitto specification. Although a null character does not cause an error, do not use null characters in your topic strings.
Topic trees
Although you can use any name for a topic, choose a name that fits into a hierarchical tree structure. Thoughtful design of topic names and topic trees can help you with the following operations:
•	Subscribing to multiple topics.
•	Establishing security policies.
•	Automatically reacting to messages on a specific topic; for example, by sending an alert to a manager's pager.
Each topic that you define is an element, or node, in the topic tree. The topic tree can either start empty or contain topics that have been defined by a system administrator using the workbench. You can define a new topic either by using the workbench or by specifying the topic for the first time in a publication.
Although you can construct a topic tree as a flat, linear structure, it is better to build a topic tree in a hierarchical structure with one or more root topics.
The following figure shows an example of a topic tree with one root topic:



Each character string in the figure represents a node in the topic tree. Aggregating nodes from one or more levels in the topic tree creates a complete topic name.
Topic hierarchies levels are separated by the "/" character. The format of a fully specified topic name is: "root/level2/level3".
The valid topics in the topic tree are:
•	“LANGUAGE"
•	"LANGUAGE/JAVA"
•	"LANGUAGE/PYTHON"
•	"LANGUAGE/JAVA/JAVA SE"
•	“LANGUAGE/JAVA/JAVA EE"
•	“LANGUAGE/JAVA/JAVA ME"
•	“LANGUAGE/JAVA/JAVA SE/ANDROID"
•	"LANGUAGE/PYTHON/DJANGO"
When you design topic names and topic trees, remember that the message broker does not interpret, or attempt to derive meaning from, the topic name itself. It uses the topic name only to send related messages to clients who have subscribed to that topic
To test topic subscription opens 2 terminals. In the first run this command:
To subscribe to a topic “kalana” under wassoulou (Kalana is a village in Wassoulou Area) in line command

> mosquitto\_sub –t wassoulou/kalana







We are now subscribe to a topic kalana under topic wassoulou. So we will receive all message published at the topic kalana under wassoulou. Note also client can subscribe to lot topic as it want. No limitation in topic. But the length of topic name cannot exceed some number character humanly reasonable.
Publish/Subscribe
The MQTT protocol is based on the principle of publishing messages and subscribing to topics, or "pub/sub". Multiple clients connect to a broker and subscribe to topics that they are interested in. Clients also connect to the broker and publish messages to topics. Many clients may subscribe to the same topics and do with the information as they please. The broker and MQTT act as a simple, common interface for everything to connect to.
Notice also to publish a message to a topic, no need to subscribe before. Push message under it is enough.
Publish a message to a topic kalana/wassoulou command line (in debug mode) is :

> mosquitto\_pub –d  -t wassoulou/kalana -m “This is my first PUSH. Thanks."

Notice also this message end with “Thanks” Mosquitto like the good manner.
The Client Identifier
The first UTF-encoded string. The Client Identifier (Client ID) is between 1 and 23 characters long, and uniquely identifies the client to the server. It must be unique across all clients connecting to a single server, and is the key in handling Message IDs messages with QoS levels 1 and 2. If the Client ID contains more than 23 characters, the server responds to: Identifier Rejected.

In the client that you find later, if you don’t set ID Client, the API should generate a random id based in your application installation and not device UUID.

For more information how it is generate: follow the link:

Push message to a Client.
As we know, Mosquitto publish message to a topic and not a client. So all subscribed client bounded to at this topic should receive the message pushed.
If you want to send push to a particular client, and be sure that this only client will receive it and not other client, you have to subscribe all client to her idClient as topic.
Given four clients : client1 (idclient1), client2 (idclient2), client3 (idclient3), client4 (idclient4).
And if each client is bounded to her idclient as topic , and if client4 have to send message to client2, it have to send push to topic idclient2. ClientID are unique.
Client can connect to more topics and her id can be its private topic.
Quality of Service
MQTT defines three levels of Quality of Service (QoS). The QoS defines how hard the broker/client will try to ensure that a message is received. Messages may be sent at any QoS level, and clients may attempt to subscribe to topics at any QoS level. This means that the client chooses the maximum QoS it will receive. For example, if a message is published at QoS 2 and a client is subscribed with QoS 0, the message will be delivered to that client with QoS 0. If a second client is also subscribed to the same topic, but with QoS 2, then it will receive the same message but with QoS 2. For a second example, if a client is subscribed with QoS 2 and a message is published on QoS 0, the client will receive it on QoS 0.
Higher levels of QoS are more reliable, but involve higher latency and have higher bandwidth requirements.
•	0: The broker/client will deliver the message once, with no confirmation.
•	1: The broker/client will deliver the message at least once, with confirmation required.
•	2: The broker/client will deliver the message exactly once by using a four step handshake
In our client API, these values are declared constant as  enum type called MqttQos:



Retained Messages
All messages may be set to be retained. This means that the broker will keep the message even after sending it to all current subscribers. If a new subscription is made that matches the topic of the retained message, then the message will be sent to the client. This is useful as a "last known good" mechanism. If a topic is only updated infrequently, then without a retained message, a newly subscribed client may have to wait a long time to receive an update. With a retained message, the client will receive an instant update.
Clean session / Durable connections
On connection, a client sets the "clean session" flag, which is sometimes also known as the "clean start" flag. If clean session is set to false, then the connection is treated as durable. This means that when the client disconnects, any subscriptions it has will remain and any subsequent QoS 1 or 2 messages will be stored until it connects again in the future. If clean session is true, then all subscriptions will be removed for the client when it disconnects

Mosquitto Client Android API:

The API available for Mosquitto Client Android is this class. All you need to push information or received information to or from broker.




Use of Mosquito Client API
As you know it’s better to use Android Service for background operations which takes a lot time. Use Mosquitto in service is really recommended but you can use it in your Activity.
In the following snipped of Code, there’s a sample use of Mosquitto API Client in the service. In the callback you


public class MosquittoDemoService extends Service implements PushListener {

> private MqttAndroidClient mqttAndroidClient;
> private static final String HOST = "your host";

> @Override
> public int onStartCommand(Intent intent, int flags, int startId) {
// you can also put this connection code in onbind method on the service.

> mqttAndroidClient = new MqttAndroidClient(this, HOST);
> mqttAndroidClient.addPushListener(this);
> mqttAndroidClient.addTopic(new MqttTopic("kalana"));
> mqttAndroidClient.start();
> return START\_STICKY;
> }
We can also put this connection code  in onbind method and disconnect on unbind method

> @Override
> public void onPushMessageReceived(MqttMessage mqttMessage) {
// Do something with mqttMessage.
// You can broadcast to your application
> > // show  it as notification


> }

> @Override
> public void onConnectionStateChanged(MqttClientState state) {

> // Do something with with connection state change.
//You can notify or show it to user..


> }

> //Other service method.You can notify or show it to user..

> @Override
> public void onDestroy() {
> > super.onDestroy();
> > // Not necessary to disconnect to the broker
> > mqttAndroidClient.disconnect();

> }
}






Push a Message to a Broker
•	Subscription to a topic:
To subscribe to a topic, you have just to add topic at the client. Create your topic and call the method addTopic() to your client.
•	Publish a Message
To publish a message under a topic to broker or to specify client, there are many way to push message.






Download and Demo:
Demo:
1.	Create your Android Project no matter the API Version.
2.	Download the Mosquitto client Android jar
3.	Add Mosquitto Client Android jar in your build path.
4.	You need network and network state permission in your manifest.
In our project, we have three components:
1. Service that used in Mosquito client. It connects to the broker and receives the Push information. Once received, it broadcast this information to the other part application and show message as notification.
2. The Broadcast class is used to catch with correct intent and action and send to Activity.
2. Your activity that is used to catch information broadcasted by the service and display to user.
Download page:
Actually this there is no place in the repository to publish the code….(Remember  to Fermat demo). So now I publish the jar that can be easily integrate to Android Project.
In the following link and at the tab download page, you should find client jar and demo how to integrate it.














