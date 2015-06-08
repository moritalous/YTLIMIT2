
package forest.rice.field.k.ytlimit2.ui.select;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import forest.rice.field.k.ytlimit2.R;

public class PackageSelectFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    public static PackageSelectFragment newInstance() {
        PackageSelectFragment fragment = new PackageSelectFragment();
        return fragment;
    }

    public PackageSelectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        PackageSelectAsynkTask asynkTask = new PackageSelectAsynkTask();
        asynkTask.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        ViewHolder holder = (ViewHolder) v.getTag();

        if (null != mListener) {
            mListener.onFragmentInteraction(holder.packageInfo);
        }
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating
     * with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(PackageInfo packageInfo);
    }

    private class PackageSelectAsynkTask extends AsyncTask<String, String, List<PackageInfo>> {

        @Override
        protected List<PackageInfo> doInBackground(String... params) {
            PackageManager manager = getActivity().getPackageManager();
            List<PackageInfo> packages = manager
                    .getInstalledPackages(PackageManager.GET_ACTIVITIES);
            return packages;
        }

        @Override
        protected void onPostExecute(List<PackageInfo> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            setListAdapter(new PackageSelectAdopter(getActivity(), 0, result));
        }
    }

    class ViewHolder {
        public ImageView image;
        public TextView name;
        public PackageInfo packageInfo;
    }

    private class PackageSelectAdopter extends ArrayAdapter<PackageInfo> {

        private LayoutInflater layoutInflater_;

        public PackageSelectAdopter(Context context, int resource, List<PackageInfo> objects) {
            super(context, resource, objects);

            layoutInflater_ = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PackageInfo packageInfo = getItem(position);
            PackageManager manager = getActivity().getPackageManager();

            final ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater_.inflate(
                        R.layout.fragment_package_select_row, null);
                holder = new ViewHolder();

                holder.image = (ImageView) convertView
                        .findViewById(R.id.fragment_package_select_image);
                holder.name = (TextView) convertView
                        .findViewById(R.id.fragment_package_select_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Bitmap icon = null;
            try {
                icon = ((BitmapDrawable) manager
                        .getApplicationIcon(packageInfo.packageName)).getBitmap();
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            holder.image.setImageBitmap(icon);
            holder.name.setText(packageInfo.applicationInfo.loadLabel(manager));
            holder.packageInfo = packageInfo;

            return convertView;

        }

    }
}
