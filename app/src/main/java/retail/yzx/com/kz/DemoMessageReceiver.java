package retail.yzx.com.kz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Utils.DateUtils;
import Utils.SharedUtil;
import shujudb.Sqlite_Entity;

/**
 * Created by admin on 2017/5/12.
 */
public class DemoMessageReceiver extends PushMessageReceiver {

    private String mRegId;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;
    public Sqlite_Entity sqlite_entity;


    //透传消息到达客户端时调用
    //作用：可通过参数message从而获得透传消息，具体请看官方SDK文档
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Log.v(DemoApplication.TAG,
                "fffffff" + message.toString());
//        String log = context.getString(R.string.recv_passthrough_message, message.getContent());
//        MainActivity.logList.add(0, getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        Log.d("print", "透传过来的消息是" + message.getContent());

        Message msg = Message.obtain();
//        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);
    }


    //通知消息到达客户端时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：通过参数message从而获得通知消息，具体请看官方SDK文档

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        Log.v(DemoApplication.TAG,
                "eeeeeee" + message.toString());
        String megs = message.getContent();

        sqlite_entity=new Sqlite_Entity(context);

//        if (message.getDescription().endsWith(".apk..")){
            sqlite_entity.insertMsg(DateUtils.getCurDate(),message.getDescription().replace("新站内信:",""),"1");
//        }

        Log.d("print", "获取的数据" + message.getDescription());
        try {
            JSONObject jsonObject = new JSONObject(megs);
            Log.d("print", "推送的数据" + megs);
            String type = jsonObject.getString("type");
            if (type.equals("h5_order")) {
                Intent intent = new Intent();
                intent.setAction("com.yzx.dialog");
                //发送广播
                context.sendBroadcast(intent);
            } else {
            String order_id = jsonObject.getString("order_id");
            String time = jsonObject.getString("time");
            String old_id = jsonObject.getString("old_id");
            Intent intent = new Intent("com.yzx.pay");
            intent.putExtra("order_id", order_id);
            Log.d("print", "单号是" + order_id);
            if (SharedUtil.getString("order_id").equals(old_id)) {
                intent.putExtra("time", time);
                context.sendBroadcast(intent);
            }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        String log = context.getString(R.string.arrive_notification_message, message.getContent());
//        MainActivity.logList.add(0, getSimpleDate() + " " + log);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
        Message msg = Message.obtain();
//        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);
    }

    //用户手动点击通知栏消息时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：1. 通过参数message从而获得通知消息，具体请看官方SDK文档
    //2. 设置用户点击消息后打开应用 or 网页 or 其他页面
    //onNotificationClickedWithNoAction


    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

        Intent intent1 = new Intent(context.getApplicationContext(), MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent1);
//        String log = context.getString(R.string.click_notification_message, message.getContent());
//        MainActivity.logList.add(0, getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        Message msg = Message.obtain();
        if (message.isNotified()) {
//            msg.obj = log;
        }
        DemoApplication.getHandler().sendMessage(msg);
    }


    //用来接收客户端向服务器发送命令后的响应结果。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.v(DemoApplication.TAG,
                "bbbbb " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
//                log = context.getString(R.string.register_success);

            } else {
//                log = context.getString(R.string.register_fail);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
//                log = context.getString(R.string.set_alias_success, mAlias);
            } else {
//                log = context.getString(R.string.set_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
//                log = context.getString(R.string.unset_alias_success, mAlias);
            } else {
//                log = context.getString(R.string.unset_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
//                log = context.getString(R.string.set_account_success, mAccount);
            } else {
//                log = context.getString(R.string.set_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
//                log = context.getString(R.string.unset_account_success, mAccount);
            } else {
//                log = context.getString(R.string.unset_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
//                log = context.getString(R.string.subscribe_topic_success, mTopic);
            } else {
//                log = context.getString(R.string.subscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
//                log = context.getString(R.string.unsubscribe_topic_success, mTopic);
            } else {
//                log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
//                log = context.getString(R.string.set_accept_time_success, mStartTime, mEndTime);
            } else {
//                log = context.getString(R.string.set_accept_time_fail, message.getReason());
            }
        } else {
//            log = message.getReason();
        }
//        MainActivity.logList.add(0, getSimpleDate() + "    " + log);

        Message msg = Message.obtain();
//        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);
    }


    //用于接收客户端向服务器发送注册命令后的响应结果。
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.v(DemoApplication.TAG,
                "cccccc" + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                //打印日志：注册成功
//                log = context.getString(R.string.register_success);
            } else {
                //打印日志：注册失败
//                log = context.getString(R.string.register_fail);
            }
        } else {
//            log = message.getReason();
        }

        Message msg = Message.obtain();
//        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);
    }

    @SuppressLint("SimpleDateFormat")
    private static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }
}
