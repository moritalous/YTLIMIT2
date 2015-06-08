
package forest.rice.field.k.ytlimit2.shortcut;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ShortcutActivity extends Activity {

    public static final String EXTRA_UUID = "UUID";
    public static final String EXTRA_PACKAGE_NAME = "PACKAGE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String packageName = getIntent().getStringExtra(EXTRA_PACKAGE_NAME);
        String uuid = getIntent().getStringExtra(EXTRA_UUID);
        if (packageName == null || uuid == null) {
            finish();
            return;
        }

        PackageManager manager = getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        startActivity(intent);

        finish();
    }

}
