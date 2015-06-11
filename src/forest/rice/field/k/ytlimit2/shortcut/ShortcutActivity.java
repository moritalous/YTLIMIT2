
package forest.rice.field.k.ytlimit2.shortcut;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class ShortcutActivity extends Activity {

    public static final String EXTRA_UUID = "UUID";
    public static final String EXTRA_PACKAGE_NAME = "PACKAGE_NAME";
    public static final String EXTRA_COUNT = "COUNT";
    public static final String EXTRA_MESSAGE = "MESSAGE";

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String packageName = getIntent().getStringExtra(EXTRA_PACKAGE_NAME);
        String uuid = getIntent().getStringExtra(EXTRA_UUID);
        int count = getIntent().getIntExtra(EXTRA_COUNT, 10);
        String message = getIntent().getStringExtra(EXTRA_MESSAGE);

        if (packageName == null || uuid == null) {
            finish();
            moveTaskToBack(true);
            return;
        }

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Set<String> pref = sharedPref.getStringSet(uuid, new HashSet<String>());

        long nowTimeInMillis = Calendar.getInstance().getTimeInMillis();
        long OneHourBeforeTimeInMillis = nowTimeInMillis - 60 * 60 * 1000;
        Set<String> newHistory = new HashSet<String>();

        for (String history : pref) {
            try {
                long oldTimeInMillis = Long.parseLong(history);
                if (OneHourBeforeTimeInMillis < oldTimeInMillis) {
                    newHistory.add(Long.toString(oldTimeInMillis));
                }
            } catch (NumberFormatException e) {
            }
        }
        sharedPref.edit().putStringSet(uuid, newHistory).commit();

        if (count <= newHistory.size()) {

            // ホームアプリを起動
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);

            // 回数オーバー
            String tmpMessage;
            if (message == null || message.length() == 0) {
                tmpMessage = "起動回数が上限を超えました。";
            } else {
                tmpMessage = message;
            }

            Toast.makeText(getApplicationContext(), tmpMessage,
                    Toast.LENGTH_SHORT).show();
        } else {
            newHistory.add(Long.toString(nowTimeInMillis));
            sharedPref.edit().putStringSet(uuid, newHistory).commit();

            PackageManager manager = getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            startActivity(intent);

            Toast.makeText(getApplicationContext(),
                    String.format("%d / %d", newHistory.size(), count),
                    Toast.LENGTH_SHORT).show();
        }

        finish();
        moveTaskToBack(true);
    }
}
