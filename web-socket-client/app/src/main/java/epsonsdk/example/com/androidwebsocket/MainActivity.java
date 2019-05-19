package epsonsdk.example.com.androidwebsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.java_websocket.WebSocket;

import butterknife.BindView;
import rx.Subscriber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.btn_dis_connect)
    Button btnDisConnect;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.edt_sent_msg)
    EditText edtSentMsg;
    private StompClient stompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect.setOnClickListener(this);
        btnDisConnect.setOnClickListener(this);
        btnSend.setOnClickListener(this);

//         stompClient = Stomp.over(WebSocket.class, "ws://" + Constants.IP_ADDRESS + ":8080/example-endpoint/websocket");
        stompClient = Stomp.over(WebSocket.class, "http://" + Constants.IP_ADDRESS + ":8080/websocket-example");

        stompClient.topic("/topic/hello").subscribe(topicMessage ->
                Log.i("myLog", "topicMessage.getPayload(): " + topicMessage.getPayload()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                stompClient.connect();
                socketLifeCycle();
                break;
            case R.id.btn_dis_connect:
                stompClient.disconnect();
                break;
            case R.id.btn_send:
                String msg = edtSentMsg.getText().toString().trim();
                sendMsg(msg);
                break;
        }
    }

    private void sendMsg(String msg) {
        stompClient.send("/app/hello", msg).subscribe(new Subscriber<Object>() {
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

            }
        });
    }

    private void socketLifeCycle() {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompClient.disconnect();
        if (stompClient != null) {
            stompClient = null;
        }
    }
}
