package com.csjbot.welcomebot_zkhl;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.csjbot.rosclient.RosClientAgent;
import com.csjbot.rosclient.constant.ClientConstant;
import com.csjbot.rosclient.listener.ClientEvent;
import com.csjbot.rosclient.listener.EventListener;
import com.csjbot.rosclient.utils.PacketBuilder;
import com.csjbot.welcomebot_zkhl.entity.NaviGetPoseRspBean;
import com.csjbot.welcomebot_zkhl.servers.ConnectWithNetty;
import com.csjbot.welcomebot_zkhl.servers.nettyHandler.ClientListener;
import com.csjbot.welcomebot_zkhl.servers.nettyHandler.ConnectHandler;
import com.csjbot.welcomebot_zkhl.utils.Constants;
import com.csjbot.welcomebot_zkhl.utils.SharePreferenceTools;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.pgyersdk.update.PgyUpdateManager;

import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.csjbot.welcomebot_zkhl.CSJToast.showToast;
import static com.csjbot.welcomebot_zkhl.R.id.btn_up;


public class MainActivity extends Activity implements ConnectWithNetty.ClientStateListener, ClientListener, View.OnTouchListener, View.OnLongClickListener, EventListener {

    @BindView(R.id.eet_editText)
    AppCompatEditText eetEditText;
    @BindView(R.id.btnLogin)
    ActionProcessButton btnLogin;
    @BindView(R.id.context_editText)
    AppCompatEditText contextEditText;
    @BindView(R.id.btn_welcome)
    Button btnWelcome;
    @BindView(R.id.btn_speak)
    Button btnSpeak;

    @BindView(R.id.btn_say1)
    Button btnSay1;
    @BindView(R.id.btn_say2)
    Button btnSay2;
    @BindView(R.id.btn_say3)
    Button btnSay3;
    @BindView(R.id.btn_say4)
    Button btnSay4;

    @BindView(R.id.btn_say5)
    Button btnSay5;
    @BindView(R.id.btn_say6)
    Button btnSay6;
    @BindView(R.id.btn_say7)
    Button btnSay7;
    @BindView(R.id.btn_say8)
    Button btnSay8;

    @BindView(R.id.btn_shake_head)
    Button btn_shake_head;
    @BindView(R.id.btn_left)
    Button btnLeft;
    @BindView(R.id.btn_bend)
    Button btn7;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_setPoint)
    Button btn0;
    @BindView(R.id.btn_3)
    Button btn3;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.btn_9)
    Button btn9;
    @BindView(R.id.ll_keyboard)
    LinearLayout llKeyboard;
    @BindView(R.id.ll_auto)
    LinearLayout llAuto;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.btnGotoHardCtrl)
    Button btnGotoHardCtrl;
    @BindView(R.id.btnGotoPic)
    Button btnGotoPic;
    @BindView(R.id.btnGotoPrint)
    Button btnGotoPrint;
    @BindView(R.id.flModule)
    FrameLayout flModule;
    @BindView(R.id.llModule)
    LinearLayout llModule;
    @BindView(R.id.btnPrint)
    Button btnPrint;
    @BindView(R.id.btnCut)
    Button btnCut;
    @BindView(R.id.printTv)
    TextView tvPrint;
    @BindView(R.id.btnPicture)
    Button btnPicture;
    @BindView(R.id.ivShowPicture)
    ImageView ivShowPicture;
    @BindView(R.id.btnTestAudio)
    Button btnTestAudio;
    @BindView(R.id.tvCollect)
    TextView tvCollect;
    @BindView(R.id.btnGotoTestAudio)
    Button btnGotoTestAudio;
    @BindView(R.id.flTop)
    FrameLayout flTop;
    @BindView(R.id.flBottom)
    FrameLayout flBottom;
    @BindView(R.id.flMiddle1)
    FrameLayout flMiddle1;
    @BindView(R.id.flMiddle2)
    FrameLayout flMiddle2;


    private long lastBackPressTime;
    private boolean upStop;
    private String moveString = "";
    private boolean isPutHand, isPutLeftHand;

    private SharedPreferences sharedPreferences = null;
    private ConnectWithNetty client = ConnectWithNetty.getInstence();
    private MainActivityHandler mHandler = new MainActivityHandler(this);
    private NaviGetPoseRspBean.PosBean pose0, pose1, pose2, pose3, pose4;

    private String[] preSetPoints = new String[]{"预设点零", "预设点一", "预设点二", "预设点三", "预设点四"};
    private SharePreferenceTools sharePreferenceTools;
    private int selectPose = -1;

    private int showModuleIndex = 0;

    private RosClientAgent rosClientAgent = null;


    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            upStop = true; // 终止长按动作
        }
        return false;
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(final View v) {
        upStop = false;
        moveString = "";

        switch (v.getId()) {
            case btn_up:
                moveString = Constants.MOVE_ACTION_UP;
                break;
            case R.id.btn_down:
                moveString = Constants.MOVE_ACTION_DOWM;
                break;
            case R.id.btn_left:
                moveString = Constants.MOVE_ACTION_LEFT;
                break;
            case R.id.btn_right:
                moveString = Constants.MOVE_ACTION_RIGHT;
                break;
            default:
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!upStop) {
//                    Logger.d(v.toString());
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    sendMessageToClient(moveString);
                }
            }
        }).start();
        return false;
    }


    private static class MainActivityHandler extends WeakReferenceHandler<MainActivity> {

        public MainActivityHandler(MainActivity reference) {
            super(reference);
        }

        @Override
        protected void handleMessage(MainActivity reference, Message msg) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        rosClientAgent = RosClientAgent.createRosClientAgent(this);

        sharedPreferences = this.getSharedPreferences("Main", Context.MODE_PRIVATE);

        setTitle("迎宾机器人-中科汇联定制");

        ButterKnife.bind(this);

        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        ConnectHandler.setListener(this);

        btnSay1.setText(sharedPreferences.getString("say1", "1"));
        btnSay2.setText(sharedPreferences.getString("say2", "2"));
        btnSay3.setText(sharedPreferences.getString("say3", "3"));
        btnSay4.setText(sharedPreferences.getString("say4", "4"));


        btnSay5.setText(sharedPreferences.getString("say5", "5"));
        btnSay6.setText(sharedPreferences.getString("say6", "6"));
        btnSay7.setText(sharedPreferences.getString("say7", "7"));
        btnSay8.setText(sharedPreferences.getString("say8", "8"));

        eetEditText.setText(sharedPreferences.getString("last_ip", "192.168.3.3"));
        eetEditText.setSelection(eetEditText.getText().toString().length());
        // 设置触摸监听
        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);

        // 设置长按监听
        btnUp.setOnLongClickListener(this);
        btnDown.setOnLongClickListener(this);
        btnRight.setOnLongClickListener(this);
        btnLeft.setOnLongClickListener(this);

        btnSay1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(1, v);
                return false;
            }
        });

        btnSay2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(2, v);
                return false;
            }
        });

        btnSay3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(3, v);
                return false;
            }
        });

        btnSay4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(4, v);
                return false;
            }
        });


        btnSay5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(5, v);
                return false;
            }
        });

        btnSay6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(6, v);
                return false;
            }
        });

        btnSay7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(7, v);
                return false;
            }
        });

        btnSay8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveFavoriteWords(8, v);
                return false;
            }
        });


        btn9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.WAIST, Constants.WaistAction.DOWN));
                CSJToast.showToast(MainActivity.this, "弯腰");
                return true;
            }
        });


        sharePreferenceTools = new SharePreferenceTools(this);
        initPoses();
//        PgyCrashManager.register(this);
        PgyUpdateManager.register(this);
    }

    private void initPoses() {
        pose0 = JSON.parseObject(sharePreferenceTools.getString("pose0", ""), NaviGetPoseRspBean.PosBean.class);
        pose1 = JSON.parseObject(sharePreferenceTools.getString("pose1", ""), NaviGetPoseRspBean.PosBean.class);
        pose2 = JSON.parseObject(sharePreferenceTools.getString("pose2", ""), NaviGetPoseRspBean.PosBean.class);
        pose3 = JSON.parseObject(sharePreferenceTools.getString("pose3", ""), NaviGetPoseRspBean.PosBean.class);
        pose4 = JSON.parseObject(sharePreferenceTools.getString("pose4", ""), NaviGetPoseRspBean.PosBean.class);
    }

    private void initPoseStrings() {
        if (pose0 != null) {
            preSetPoints[0] = pose0.toString();
        }

        if (pose1 != null) {
            preSetPoints[1] = pose1.toString();
        }

        if (pose2 != null) {
            preSetPoints[2] = pose2.toString();
        }

        if (pose3 != null) {
            preSetPoints[3] = pose3.toString();
        }

        if (pose4 != null) {
            preSetPoints[4] = pose4.toString();
        }
    }

    private boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }

    boolean isDiantou;

    @OnClick({R.id.btn_setPoint,
            R.id.btn_gotoPoint,
            R.id.btn_9,
            R.id.btn_bend,
            R.id.btn_3,
            R.id.btn_shake_head,
            R.id.btnLogin,
            R.id.btn_welcome,
            R.id.btn_speak,
            R.id.btn_say1,
            R.id.btn_say2,
            R.id.btn_say3,
            R.id.btn_say4,
            R.id.btn_say5,
            R.id.btn_say6,
            R.id.btn_say7,
            R.id.btn_say8,
            R.id.btn_left,
            R.id.btn_go,
            R.id.btn_up,
            R.id.btn_stop, R.id.btn_down, R.id.btn_right, R.id.btnGotoPrint, R.id.btnGotoHardCtrl, R.id.btnGotoPic, R.id.btnPrint
            , R.id.btnCut, R.id.btnPicture, R.id.btnTestAudio, R.id.btnGotoTestAudio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String ip = eetEditText.getText().toString();
                if (!rosClientAgent.isConnected()) {
                    if (checkIP(ip)) {
                        btnLogin.setEnabled(false);
//                        client.connect(ip, this);
                        rosClientAgent.connect(ip, 60002);
                        btnLogin.setProgress(1);
                        eetEditText.setEnabled(false);
                    } else {
                        new AlertDialog.Builder(MainActivity.this).setMessage("IP地址不合法，请重新输入!").show();
                        btnLogin.requestFocus();
                    }
                } else {
//                    rosClientAgent.destroy();
//                    client.closeConnect();
//                    btnLogin.setText("登录");
//                    eetEditText.setEnabled(true);
//                    setContentEnable(false);
                    CSJToast.showToast(this, "已经连接");
                }
                break;
            case R.id.btn_shake_head:
                sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.LEFT_THEN_RIGHT));
                showToast(this, "摇头");
                break;
            case R.id.btn_3:
                if (isDiantou) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.UP));
                } else {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.DOWN));
                }
                isDiantou ^= true;
                showToast(this, "点头");
                break;
            case R.id.btn_bend:

                if (isPutLeftHand) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.LEFT_ARM, Constants.BodyAction.RIGHT));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.LEFT_FOREARM, Constants.BodyAction.RIGHT));
                        }
                    }, 200);
                    CSJToast.showToast(MainActivity.this, "缩回去");
                    ((Button) view).setText("伸左手");
                    isPutLeftHand = false;
                } else {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.LEFT_ARM, Constants.BodyAction.LEFT));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.LEFT_FOREARM, Constants.BodyAction.LEFT));
                        }
                    }, 200);
                    isPutLeftHand = true;
                    ((Button) view).setText("缩左手");
                    CSJToast.showToast(this, "伸左手");
                }
                break;
//               sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.WAIST, Constants.BodyAction.DOWN));
//                showToast(this, "弯腰");
//                break;
            case R.id.btn_9:
                sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.WAIST, Constants.WaistAction.UP));
                showToast(this, "直立");
                break;
            case R.id.btn_welcome:
                sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.DOWN));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, "尊敬的领导，您好"));
                        showToast(MainActivity.this, "领导好");
                    }
                }, 500);
//               sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.WAIST, Constants.BodyAction.DOWN));
//                CSJToast.showToast(this, "弯腰");

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                       sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.WAIST, Constants.BodyAction.UP));
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.UP));

//                        CSJToast.showToast(MainActivity.this, "直立");

                    }
                }, 3000);
                break;
            case R.id.btn_speak: {
                String say = contextEditText.getText().toString();
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;

            case R.id.btn_say1: {
                String say = sharedPreferences.getString("say1", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say2: {
                String say = sharedPreferences.getString("say2", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say3: {
                String say = sharedPreferences.getString("say3", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say4: {
                String say = sharedPreferences.getString("say4", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say5: {
                String say = sharedPreferences.getString("say5", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say6: {
                String say = sharedPreferences.getString("say6", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say7: {
                String say = sharedPreferences.getString("say7", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_say8: {
                String say = sharedPreferences.getString("say8", "");
                if (!say.isEmpty()) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.SPEAK_MODE, say));
                } else {
                    showToast(this, "说点啥吧");
                }
            }
            break;
            case R.id.btn_go:
                if (isPutHand) {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.RIGHT_ARM, Constants.BodyAction.RIGHT));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.RIGHT_FOREARM, Constants.BodyAction.RIGHT));
                        }
                    }, 200);
                    CSJToast.showToast(MainActivity.this, "缩回去");
                    btnGo.setText("伸右手");
                    isPutHand = false;
                } else {
                    sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.RIGHT_ARM, Constants.BodyAction.LEFT));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.RIGHT_FOREARM, Constants.BodyAction.LEFT));
                        }
                    }, 200);
                    isPutHand = true;
                    btnGo.setText("缩右手");
                    CSJToast.showToast(this, "伸右手");
                }
                break;
            case R.id.btn_stop:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        sendMessageToClient(Constants.MOVE_ACTION_STOP);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.LEFT_ARM, Constants.BodyAction.STOP));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.LEFT_FOREARM, Constants.BodyAction.STOP));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.RIGHT_ARM, Constants.BodyAction.STOP));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.RIGHT_FOREARM, Constants.BodyAction.STOP));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.HEAD_UP_AND_DOWN_STOP));
                        sendMessageToClient(String.format(Locale.getDefault(), Constants.BODY_MOVE_MODE, Constants.BodyPart.HEAD, Constants.BodyAction.STOP));

                    }
                }).start();
                break;
            case btn_up:
                sendMessageToClient(Constants.MOVE_ACTION_UP);
                break;
            case R.id.btn_down:
                sendMessageToClient(Constants.MOVE_ACTION_DOWM);
                break;
            case R.id.btn_left:
                sendMessageToClient(Constants.MOVE_ACTION_LEFT);
                break;
            case R.id.btn_right:
                sendMessageToClient(Constants.MOVE_ACTION_RIGHT);
                break;
            case R.id.btn_setPoint:
                initPoseStrings();
                new AlertDialog.Builder(this)
                        .setTitle("设置预设点")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(preSetPoints,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendMessageToClient(Constants.NAVI_GET_POS_REQ);
                                        selectPose = which;
                                        CSJToast.showToast(MainActivity.this, "设置预设点 " + String.valueOf(which));
                                        dialog.dismiss();
                                    }
                                }
                        ).show();
                break;
            case R.id.btn_gotoPoint:
                initPoseStrings();
                new AlertDialog.Builder(this)
                        .setTitle("到达预设点")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(preSetPoints,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                if (pose0 == null) {
                                                    CSJToast.showToast(MainActivity.this, "预设点 " + String.valueOf(which) + "没有被设置");
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                sendMessageToClient(String.format(Locale.getDefault(), Constants.NAVI_ROBOT_MOVE_TO_REQ,
                                                        pose0.getX(), pose0.getY(), pose0.getZ(), pose0.getRotation()));
                                                break;
                                            case 1:
                                                if (pose1 == null) {
                                                    CSJToast.showToast(MainActivity.this, "预设点 " + String.valueOf(which) + "没有被设置");
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                sendMessageToClient(String.format(Locale.getDefault(), Constants.NAVI_ROBOT_MOVE_TO_REQ,
                                                        pose1.getX(), pose1.getY(), pose1.getZ(), pose1.getRotation()));
                                                break;
                                            case 2:
                                                if (pose2 == null) {
                                                    CSJToast.showToast(MainActivity.this, "预设点 " + String.valueOf(which) + "没有被设置");
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                sendMessageToClient(String.format(Locale.getDefault(), Constants.NAVI_ROBOT_MOVE_TO_REQ,
                                                        pose2.getX(), pose2.getY(), pose2.getZ(), pose2.getRotation()));
                                                break;
                                            case 3:
                                                if (pose3 == null) {
                                                    CSJToast.showToast(MainActivity.this, "预设点 " + String.valueOf(which) + "没有被设置");
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                sendMessageToClient(String.format(Locale.getDefault(), Constants.NAVI_ROBOT_MOVE_TO_REQ,
                                                        pose3.getX(), pose3.getY(), pose3.getZ(), pose3.getRotation()));
                                                break;
                                            case 4:
                                                if (pose4 == null) {
                                                    CSJToast.showToast(MainActivity.this, "预设点 " + String.valueOf(which) + "没有被设置");
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                sendMessageToClient(String.format(Locale.getDefault(), Constants.NAVI_ROBOT_MOVE_TO_REQ,
                                                        pose4.getX(), pose4.getY(), pose4.getZ(), pose4.getRotation()));
                                                break;
                                        }
                                        CSJToast.showToast(MainActivity.this, "到达预设点 " + String.valueOf(which));
                                        dialog.dismiss();
                                    }
                                }
                        ).show();
                break;
            case R.id.btnGotoHardCtrl:
                showModuleIndex = 0;
                switchShowModule();
                break;
            case R.id.btnGotoPic:
                showModuleIndex = 1;
                switchShowModule();
                break;
            case R.id.btnGotoPrint:
                showModuleIndex = 2;
                switchShowModule();
                break;
            case R.id.btnGotoTestAudio:
                showModuleIndex = 3;
                switchShowModule();
                break;
            case R.id.btnPrint:
                String strPrint = tvPrint.getText().toString();
                if (strPrint.isEmpty()) {
                    Toast.makeText(this, "请输入要打印的内容!", Toast.LENGTH_SHORT);
                    tvPrint.requestFocus();
                    break;
                }
                sendMessageToClient(String.format(Constants.PRINT_TEXT_CMD, tvPrint.getText()));
                break;
            case R.id.btnCut:
                sendMessageToClient(Constants.CUT_CMD);
                break;
            case R.id.btnPicture:
                sendMessageToClient(Constants.PHOTO_REQ);
                Toast.makeText(this, "正在生成图像，请等待...", Toast.LENGTH_SHORT);
                ivShowPicture.setImageResource(R.drawable.camera);
                break;
            case R.id.btnTestAudio:
                sendMessageToClient(Constants.OPEN_ONCE_AUDIO_START_REQ);
                Toast.makeText(this, "请对着机器人说话!", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }

    }


    @Override
    public void onEvent(ClientEvent event) {
        switch (event.eventType) {
            case ClientConstant.EVENT_CONNECT_SUCCESS:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        eetEditText.setEnabled(false);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("last_ip", eetEditText.getText().toString());
                        editor.apply();

                        //打开打印机
                        sendMessageToClient(Constants.PRINT_HARD_OPEN);
                        //初始化人脸识别模块
                        sendMessageToClient(Constants.FACE_REG_START_REQ);
                        setContentEnable(true);

                        eetEditText.setEnabled(false);
                        btnLogin.setEnabled(true);
                        btnLogin.setProgress(100);
                        btnLogin.setText("断开");
                    }
                });
                break;
            case ClientConstant.EVENT_CONNECT_FAILD:
            case ClientConstant.EVENT_CONNECT_TIME_OUT:

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        eetEditText.setEnabled(true);
                        btnLogin.setEnabled(true);
                        btnLogin.setProgress(0);
                        showToast(MainActivity.this, "登录失败");
                        setContentEnable(false);

                        eetEditText.setEnabled(true);
                        btnLogin.setProgress(0);
                        btnLogin.setText("登录");
                    }
                });
                break;
        }
    }


    public void sendMessageToClient(String message) {
//        client.sendMsg(message);

        rosClientAgent.sendMessage(PacketBuilder.createAudioPacket(message.getBytes()));
    }

    @Override
    public void connectSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eetEditText.setEnabled(false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("last_ip", eetEditText.getText().toString());
                editor.apply();

                //打开打印机
                sendMessageToClient(Constants.PRINT_HARD_OPEN);
                //初始化人脸识别模块
                sendMessageToClient(Constants.FACE_REG_START_REQ);
                setContentEnable(true);
            }
        });
    }

    @Override
    public void connectFaild() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eetEditText.setEnabled(true);
                btnLogin.setEnabled(true);
                btnLogin.setProgress(0);
                showToast(MainActivity.this, "登录失败");
                setContentEnable(false);
            }
        });
    }

    @Override
    public void recMessage(String msg) {
//        {"msg_id":"NAVI_ROBOT_MOVE_TO_REQ","pos":{"x":10,"y":235,"z":25,"rotation":157}}
        Logger.d("recMessage: " + msg);
        JsonElement obj = new JsonParser().parse(msg);
        String msgType = obj.getAsJsonObject().get("msg_id").getAsString();
        Logger.d("msgId: " + msgType);
        switch (msgType) {
            case "NAVI_GET_POS_RSP":
                NaviGetPoseRspBean bean = JSON.parseObject(msg, NaviGetPoseRspBean.class);
                Logger.d("bean " + bean.getPos().toString());
                switch (selectPose) {
                    case 0:
                        pose0 = bean.getPos();
                        sharePreferenceTools.putString("pose0", JSON.toJSONString(pose0));
                        break;
                    case 1:
                        pose1 = bean.getPos();
                        sharePreferenceTools.putString("pose1", JSON.toJSONString(pose1));
                        break;
                    case 2:
                        pose2 = bean.getPos();
                        sharePreferenceTools.putString("pose2", JSON.toJSONString(pose2));
                        break;
                    case 3:
                        pose3 = bean.getPos();
                        sharePreferenceTools.putString("pose3", JSON.toJSONString(pose3));
                        break;
                    case 4:
                        pose4 = bean.getPos();
                        sharePreferenceTools.putString("pose1", JSON.toJSONString(pose4));
                        break;
                    default:
                        break;
                }
                selectPose = -1;
                break;
            case "FACE_SNAPSHOT_RESULT_RSP":
                JsonObject cameraObj = obj.getAsJsonObject();
                String[] recPath = cameraObj.get("file_path").getAsString().split("\\\\");
                final String filePath = "http://" + eetEditText.getText().toString() + "/" + recPath[recPath.length - 1];
                Logger.d("take picture path : " + filePath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(filePath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivShowPicture);
                        Toast.makeText(MainActivity.this, "拍照成功!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "SPEECH_ISR_LAST_RESULT_NTF":
                JsonObject lastSpeechObj = obj.getAsJsonObject().get("result").getAsJsonObject();
                final String strDetectText = lastSpeechObj.get("text").getAsString();
                Logger.d("detect audio text is " + strDetectText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCollect.setText(strDetectText);
                        Toast.makeText(MainActivity.this, "您说了：" + strDetectText, Toast.LENGTH_SHORT);
                    }
                });
                sendMessageToClient(Constants.OPEN_ONCE_AUDIO_STOP_REQ);
                break;
            default:
                break;
        }
    }

    @Override
    public void clientConnected() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                eetEditText.setEnabled(false);
                btnLogin.setEnabled(true);
                btnLogin.setProgress(100);
                btnLogin.setText("断开");
            }
        }, 500);
    }

    @Override
    public void clientDisConnected() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eetEditText.setEnabled(true);
                btnLogin.setProgress(0);
                btnLogin.setText("登录");
            }
        });
    }

    @Override
    protected void onDestroy() {
        client.exitClient();
        super.onDestroy();
        PgyUpdateManager.unregister();
    }


    private void switchShowModule() {
        for (int i = 0; i < flModule.getChildCount(); i++) {
            if (i == showModuleIndex) {
                flModule.getChildAt(i).setVisibility(View.VISIBLE);
                llModule.getChildAt(i).setBackgroundColor(Color.parseColor("#5E3D6BE0"));
            } else {
                flModule.getChildAt(i).setVisibility(View.INVISIBLE);
                llModule.getChildAt(i).setBackgroundColor(Color.parseColor("#5e639078"));
            }
        }

    }


    @Override
    public void onBackPressed() {
        if (lastBackPressTime == 0) {
            Toast.makeText(this, "再按一次返回键退出程序！", Toast.LENGTH_SHORT).show();
            lastBackPressTime = System.currentTimeMillis();
            return;
        } else {
            if (System.currentTimeMillis() - lastBackPressTime >= 1500) {
                Toast.makeText(this, "再按一次返回键退出程序！", Toast.LENGTH_SHORT).show();
                lastBackPressTime = System.currentTimeMillis();
                return;
            } else {
                finish();
            }
        }

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switchShowModule();
    }

    private void saveFavoriteWords(int index, View v) {
        String say = contextEditText.getText().toString();
        if (say.isEmpty()) {
            Toast.makeText(this, "请输入需要存储的文字!", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = "say" + index;
        editor.putString(key, say);
        editor.apply();
        key = "按钮" + index;
        showToast(MainActivity.this, say + "存入" + key);
        ((Button) v).setText(say);
    }

    private void setContentEnable(boolean enable) {
        FrameLayout[] flParents = {flTop, flMiddle1, flMiddle2, flBottom};
        for (FrameLayout fl : flParents) {
            if (enable) {
                fl.getChildAt(1).setVisibility(View.GONE);
            } else {
                fl.getChildAt(1).setVisibility(View.VISIBLE);
            }
        }
    }
}
