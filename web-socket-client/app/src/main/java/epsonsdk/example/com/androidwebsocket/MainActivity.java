package epsonsdk.example.com.androidwebsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.java_websocket.WebSocket;

import rx.Subscriber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StompClient stompClient = Stomp.over(WebSocket.class, "ws://" + Constants.IP_ADDRESS + ":8080/example-endpoint/websocket");
        StompClient stompClient = Stomp.over(WebSocket.class, "http://" + Constants.IP_ADDRESS + ":8080/websocket-example/websocket");
        stompClient.connect();
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.i("myLog", "Stomp connection opened");
                    break;

                case ERROR:
                    Log.i("myLog", "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.i("myLog", "Stomp connection closed");
                    break;
            }
        });

        stompClient.send("/app/hello", "My first STOMP message!").subscribe(new Subscriber<Object /*This can be anything*/>() {
            @Override
            public void onCompleted() {
                Log.i("myLog", "Sent data!");
            }

            @Override
            public void onError(Throwable error) {
                Log.i("myLog", "Encountered error while sending data!", error);
            }

            @Override
            public void onNext(Object o) {

            } // useless
        });


        stompClient.topic("/topic/hello").subscribe(topicMessage ->
                Log.i("myLog", "topicMessage.getPayload(): " + topicMessage.getPayload()));
    }
}
