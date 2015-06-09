
package forest.rice.field.k.ytlimit2;

import android.content.Intent;
import android.widget.Toast;
import forest.rice.field.k.ytlimit2.SettingFragment.OnButtonClickListener;

public class Main2Activity extends MainActivity implements OnButtonClickListener {
    @Override
    public void createShortcutOnHomeScreen(Intent intent) {
        setResult(RESULT_OK, intent);

        Toast.makeText(getApplicationContext(), "作成しました", Toast.LENGTH_LONG).show();

        finish();
    }
}
