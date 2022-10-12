package com.oryn.lotus.flutter_ring_kit

import android.app.Activity
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oryn.lotus.flutter_ring_kit.helpers.IntentHelper
import com.oryn.lotus.flutter_ring_kit.models.ReminderData
import com.oryn.lotus.flutter_ring_kit.receivers.ActionBroadcastReceiver
import com.oryn.lotus.flutter_ring_kit.utils.Definitions
import com.oryn.lotus.flutter_ring_kit.widgets.RippleRelativeLayout

class ReminderActivity : Activity() {
    private lateinit var broadcastReceiver: BroadcastReceiver

    private lateinit var txtTitle: TextView
    private lateinit var txtReminderName: TextView
    private lateinit var txtDescription: TextView
    private lateinit var rlStart: RelativeLayout
    private lateinit var rlEnd: RelativeLayout
    private lateinit var rlRippleMain: RippleRelativeLayout

    private lateinit var reminderData: ReminderData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.reminder_screen)
        initializeWakeLock()
        storeIntentData(intent)
        initView()
        registerBroadcastListener()
    }

    override fun onDestroy() {
        // unregister broadcast receiver
        with(LocalBroadcastManager.getInstance(this)) {
            unregisterReceiver(broadcastReceiver)
        }
        // stop wake lock
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onDestroy()
    }

    private fun initializeWakeLock() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true)
            setShowWhenLocked(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        // dismiss keyguard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        }
    }

    private fun makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        val windowInsetsController = ViewCompat.getWindowInsetsController(
            window.decorView
        ) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun storeIntentData(intent: Intent) {
        // check extras exist
        if (intent.extras == null) return
        reminderData = ReminderData.fromBundle(intent.extras!!)
    }

    private fun initView() {
        txtTitle = findViewById(R.id.txt_title)
        txtReminderName = findViewById(R.id.txt_reminder_name)
        txtDescription = findViewById(R.id.txt_description)
        rlStart = findViewById(R.id.rl_start)
        rlEnd = findViewById(R.id.rl_ignore)
        rlRippleMain = findViewById(R.id.rl_ripple_main)
        // set values
        txtTitle.text = reminderData.fullScreenTitle
        txtReminderName.text = reminderData.reminderName
        txtDescription.text = reminderData.fullScreenDescription
        // start rippling
        rlRippleMain.startRippleAnimation()
        // set animations
//        animateStartCall()
    }

//    private fun animateStartCall() {
//        val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.btn_shaker)
//        rlStart.animation = shakeAnimation
//    }

    private fun registerBroadcastListener() {
        // create broadcast receiver
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // check intent usable
                if (intent == null || context == null || TextUtils.isEmpty(intent.action)) return
                // close activity upon any action
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                } else {
                    finish()
                }
            }
        }
        // create intent filter
        val intentFilter = IntentFilter()
        intentFilter.addAction(Definitions.ACTION_REMINDER_ACCEPT)
        intentFilter.addAction(Definitions.ACTION_REMINDER_REJECT)
        intentFilter.addAction(Definitions.ACTION_REMINDER_TIMED_OUT)
        // register broadcast receiver
        with(LocalBroadcastManager.getInstance(this)) {
            registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    fun onStartReminder(@Suppress("UNUSED_PARAMETER") view: View?) {
        val launchIntent = IntentHelper.getLaunchIntent(applicationContext) ?: return
        // add data
        launchIntent.putExtra(Definitions.EXTRA_LAUNCHED_ON_REMINDER, true)
        launchIntent.putExtra(Definitions.EXTRA_LAUNCHED_ON_REMINDER_DATA, reminderData.toBundle())
        launchIntent.putExtra(
            Definitions.EXTRA_LAUNCHED_ACTION, Definitions.EXTRA_ACTION_REMINDER_ACCEPT
        )
        startActivity(launchIntent)
    }

    fun onIgnoreReminder(@Suppress("UNUSED_PARAMETER") view: View?) {
        val intent = Intent(this, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_REMINDER_REJECT
            putExtras(reminderData.toBundle())
        }
        applicationContext.sendBroadcast(intent)
    }
}