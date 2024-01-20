package in.games.gdmatkalive.Config;

import android.content.Intent;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import in.games.gdmatkalive.Activity.SplashActivity;
import in.games.gdmatkalive.Model.NotificationModel;
import in.games.gdmatkalive.Util.NotificationUtils;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

  //  private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "global";
    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ACTION_DESTINATION = "action_destination";
   public static  String rec_Token="";
    @Override
    public void onNewToken(String s) {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        rec_Token=refreshedToken.toString();
//        // now subscribe to `global` topic to receive app wide notifications
//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//
        sendRegistrationToServer();

    }

    private void sendRegistrationToServer() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleData(Map<String, String> data) {
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        NotificationModel notificationVO = new NotificationModel();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setIconUrl(iconUrl);

        notificationVO.setAction(action);
        notificationVO.setActionDestination(actionDestination);

       // Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
//        notificationUtils.playNotificationSound();


    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationModel notificationVO = new NotificationModel();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);


        Intent resultIntent = new Intent(getApplicationContext(),SplashActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
//        notificationUtils.playNotificationSound();
    }



}
