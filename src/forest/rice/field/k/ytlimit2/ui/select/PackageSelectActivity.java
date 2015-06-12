
package forest.rice.field.k.ytlimit2.ui.select;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import forest.rice.field.k.ytlimit2.R;
import forest.rice.field.k.ytlimit2.ui.select.PackageSelectFragment.OnFragmentInteractionListener;

public class PackageSelectActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_select);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, PackageSelectFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onFragmentInteraction(PackageInfo packageInfo) {

        Intent data = new Intent();
        data.putExtra("PackageName", packageInfo.packageName);
        setResult(RESULT_OK, data);
        finish();
    }

}
