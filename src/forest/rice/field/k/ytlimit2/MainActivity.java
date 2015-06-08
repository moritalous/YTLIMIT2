
package forest.rice.field.k.ytlimit2;

import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import forest.rice.field.k.ytlimit2.shortcut.ShortcutActivity;
import forest.rice.field.k.ytlimit2.ui.select.PackageSelectActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Button selectPackageButton;
        private Button createShortcutButton;

        private PackageInfo packageInfo;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            selectPackageButton = (Button) rootView.findViewById(R.id.select_package_button);
            selectPackageButton.setText("Select Application");
            selectPackageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PackageSelectActivity.class);
                    startActivityForResult(intent, 9999);
                }
            });

            createShortcutButton = (Button) rootView.findViewById(R.id.create_shortcut_button);
            createShortcutButton.setVisibility(View.INVISIBLE);
            createShortcutButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (packageInfo != null) {
                        createShortcutOnHomeScreen(createShortcutIntent());
                    }

                }
            });

            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // TODO Auto-generated method stub
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode != RESULT_OK) {
                return;
            }

            try {
                String packageName = data.getStringExtra("PackageName");

                PackageManager manager = getActivity().getPackageManager();
                packageInfo = manager.getPackageInfo(packageName,
                        PackageManager.GET_ACTIVITIES);

                selectPackageButton.setText(packageInfo.applicationInfo.loadLabel(manager));
                Drawable icon = manager.getApplicationIcon(packageInfo.packageName);
                selectPackageButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

                createShortcutButton.setVisibility(View.VISIBLE);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        private Intent createShortcutIntent() {
            Intent intent = new Intent(getActivity(), ShortcutActivity.class);

            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ShortcutActivity.EXTRA_PACKAGE_NAME,
                    packageInfo.applicationInfo.packageName);
            intent.putExtra(ShortcutActivity.EXTRA_UUID, UUID.randomUUID().toString());

            return intent;
        }

        protected void createShortcutOnHomeScreen(Intent intent) {
            PackageManager manager = getActivity().getPackageManager();
            Intent createShortcut = new Intent();
            createShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            createShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                    packageInfo.applicationInfo.loadLabel(manager));
            try {
                createShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                        ((BitmapDrawable) manager
                                .getApplicationIcon(packageInfo.packageName))
                                .getBitmap());
            } catch (Exception e) {
                System.out.println("Error");
            }
            createShortcut
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            getActivity().sendBroadcast(createShortcut);

        }
    }

    public static class PlaceholderFragment2 extends PlaceholderFragment {

        @Override
        protected void createShortcutOnHomeScreen(Intent intent) {
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        }
    }

}
