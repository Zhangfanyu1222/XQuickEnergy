package pansong291.xposed.quickenergy.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.data.RuntimeInfo;
import pansong291.xposed.quickenergy.entity.FriendWatch;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.LanguageUtil;
import pansong291.xposed.quickenergy.util.PermissionUtil;
import pansong291.xposed.quickenergy.util.Statistics;

public class MainActivity extends Activity {
    public static String version = "";
    TextView tvStatistics;

    private static boolean isExpModuleActive(Context context) {
        boolean isExp = false;
        if (context == null)
            throw new IllegalArgumentException("context must not be null!!");

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.parse("content://me.weishu.exposed.CP/");
            Bundle result = null;
            try {
                result = contentResolver.call(uri, "active", null, null);
            } catch (RuntimeException e) {
                // TaiChi is killed, try invoke
                try {
                    Intent intent = new Intent("me.weishu.exp.ACTION_ACTIVE");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Throwable e1) {
                    return false;
                }
            }
            if (result == null)
                result = contentResolver.call(uri, "active", null, null);

            if (result == null)
                return false;
            isExp = result.getBoolean("active", false);
        } catch (Throwable ignored) {
        }
        return isExp;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.setLocale(this);
        setContentView(R.layout.activity_main);
        RuntimeInfo.process = "app";

        tvStatistics = findViewById(R.id.tv_statistics);
        PermissionUtil.requestPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStatistics.setText(Statistics.getText());
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test) {
            if (isApkInDebug(this)) {
                Toast toast = Toast.makeText(this, "测试", Toast.LENGTH_SHORT);
                toast.setGravity(toast.getGravity(), toast.getXOffset(), Config.toastOffsetY());
                toast.show();
                sendBroadcast(new Intent("com.eg.android.AlipayGphone.xqe.test"));
            }
            return;
        }

        String data = "file://";
        switch (v.getId()) {
            case R.id.btn_forest_log:
                data += FileUtils.getForestLogFile().getAbsolutePath();
                break;

            case R.id.btn_farm_log:
                data += FileUtils.getFarmLogFile().getAbsolutePath();
                break;

            case R.id.btn_other_log:
                data += FileUtils.getOtherLogFile().getAbsolutePath();
                break;

            case R.id.btn_github:
                data = "https://github.com/constanline/XQuickEnergy";
                break;

            case R.id.btn_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return;
            case R.id.btn_friend_watch:
                ListDialog.show(this, getString(R.string.friend_watch), FriendWatch.getList(), new ArrayList<>(), null, ListDialog.ListType.SHOW);
                return;
        }
        Intent it = new Intent(this, HtmlViewerActivity.class);
        it.setData(Uri.parse(data));
        startActivity(it);
    }
}
