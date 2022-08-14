package unicam.pi.mqespolversion2.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import unicam.pi.mqespolversion2.R;
import unicam.pi.mqespolversion2.data.LocalBroker;

public class mqttService extends Service {

    final String CHANNELID = "Foreground Service ID";
    private int  NOTIFICATION_ID=1;
    private Context context;
    private Boolean isDestroyed =false;
    private IBinder mbinder= new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mbinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG","Service onStartCommad");
        Toast.makeText(context,"Service MQTT started",Toast.LENGTH_SHORT).show();
        doTask();
        return super.onStartCommand(intent, flags, startId);
    }
    void doTask(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                            Log.e("Service", "Service is running...");
                            LocalBroker localBroker=new LocalBroker("roro",1883,"roro");
                           Log.e("TAG","Local Broker Create");
                            MQTTServerListener mqttServerListener = new MQTTServerListener();
                             Log.e("TAG","Listener Create");
                            try {
                                mqttMosquette.startMoquette(localBroker,mqttServerListener);
                                Thread.sleep(2000);
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                    }
                }
        ).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","Server Stopped");
        isDestroyed=true;
        Toast.makeText(context,"Stopping Service MQTT",Toast.LENGTH_SHORT).show();
        mqttMosquette.stopMoquette();
    }

    @Override
    public void onCreate() {
        Log.e("TAG","Service Create");
        super.onCreate();
        context= this;
        startForeground(NOTIFICATION_ID, mostrarNotificacion(""));
    }

    private Notification mostrarNotificacion(String contenido){

        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                "Notificacion Broker",
                    NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager notificationManager=getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText(contenido)
                .setContentTitle("Server MQTT running...")
                .setOngoing(true)
                .setTicker("MQTT")
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_mq_logo_foreground);
        return notification.build();
    }

    public class LocalBinder extends Binder{
        public mqttService getService(){
            return mqttService.this;
        }
    }

}
