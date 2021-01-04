package ru.test.testjni.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;

import androidx.annotation.NonNull;

import java.io.IOException;

import ru.test.testjni.CanSocket;
import ru.test.testjni.HexUtils;
import ru.test.testjni.callback.ResultReceiverCallback;
import ru.test.testjni.canbus.CanUtils;
import ru.test.testjni.canbus.response.CanBusBean;

// TODO: intent service
public class CanService extends Service {

    private static final String ACTION_LISTEN_CAN_INTERFACE = "ru.test.testjni.action.LISTEN_CAN_INTERFACE";
    private static final String EXTRA_RESULT_RECEIVER = "ru.test.testjni.extra.RESULT_RECEIVER";

    private ResultReceiver resultReceiver;

    public CanService() {
    }

    public static void listenCanInterface(Context context, ResultReceiverCallback<CanBusBean> callback) {
        CanDataResultReceiver<CanBusBean> resultReceiver = new CanDataResultReceiver<>(new Handler(context.getMainLooper()));
        resultReceiver.setReceiverCallback(callback);

        Intent intent = new Intent(context, CanService.class);
        intent.setAction(ACTION_LISTEN_CAN_INTERFACE);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    private Runnable listeningCanBusRunnable = new Runnable() {
        @Override
        public void run() {
            startCanBusListening();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

        final String action = intent.getAction();
        if (ACTION_LISTEN_CAN_INTERFACE.equals(action)) {
            try {
                CanUtils.initCan0();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(listeningCanBusRunnable).start();
        }

        return Service.START_STICKY;
    }

    private void startCanBusListening() {
        while (true) {
            try {
                CanSocket.CanFrame frame = CanUtils.revCan0Data();
                parseDataFrame(frame);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseDataFrame(CanSocket.CanFrame frame) {
        CanBusBean canBusBean = new CanBusBean();
        canBusBean.setCanId(HexUtils.intToByteArray(frame.getCanId().getCanId_EFF()));
        canBusBean.setData(frame.getData());
        resultReceiver.send(CanDataResultReceiver.RESULT_CODE_OK, getBundle(canBusBean));
    }


    @NonNull
    private Bundle getBundle(CanBusBean canBusBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CanDataResultReceiver.PARAM_RESULT, canBusBean);
        return bundle;
    }


}
