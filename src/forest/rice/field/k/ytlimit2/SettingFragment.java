
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import forest.rice.field.k.ytlimit2.shortcut.ShortcutActivity;
import forest.rice.field.k.ytlimit2.ui.select.PackageSelectActivity;

class SettingFragment extends Fragment {

    public interface OnButtonClickListener {
        public void createShortcutOnHomeScreen(Intent intent);
    }

    private OnButtonClickListener onButtonClickListener = null;

    private Button selectPackageButton;
    private Button createShortcutButton;
    private ViewGroup settingDetailLayout;
    private NumberPicker numberPicker;
    private EditText messageText;

    private PackageInfo packageInfo;

    public SettingFragment() {
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
                    if (onButtonClickListener != null) {
                        onButtonClickListener
                                .createShortcutOnHomeScreen(getCreateShortcutIntent(getShortcutIntent()));

                    }
                }
            }
        });

        settingDetailLayout = (ViewGroup) rootView.findViewById(R.id.setting_detail);
        settingDetailLayout.setVisibility(View.INVISIBLE);

        numberPicker = (NumberPicker) rootView.findViewById(R.id.count);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(10);

        messageText = (EditText) rootView.findViewById(R.id.message);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnButtonClickListener) {
            onButtonClickListener = (OnButtonClickListener) activity;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
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
            settingDetailLayout.setVisibility(View.VISIBLE);
        } catch (NameNotFoundException e) {
        }
    }

    private Intent getShortcutIntent() {
        Intent intent = new Intent(getActivity(), ShortcutActivity.class);

        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ShortcutActivity.EXTRA_PACKAGE_NAME,
                packageInfo.applicationInfo.packageName);
        intent.putExtra(ShortcutActivity.EXTRA_UUID, UUID.randomUUID().toString());
        intent.putExtra(ShortcutActivity.EXTRA_COUNT, numberPicker.getValue());
        intent.putExtra(ShortcutActivity.EXTRA_MESSAGE, messageText.getText().toString());

        return intent;
    }

    private Intent getCreateShortcutIntent(Intent intent) {
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
        return createShortcut;
    }
}
