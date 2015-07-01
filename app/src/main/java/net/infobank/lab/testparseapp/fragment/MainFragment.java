package net.infobank.lab.testparseapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.infobank.lab.testparseapp.R;

/**
 * Created by chunghj on 15. 6. 30..
 */
public class MainFragment extends Fragment {

    private static final String KEY_CONTENT = "TestFragment:Content";

    private String tabTitle;
    private int position;

    public static MainFragment create(String tabTitle, int position) {
        MainFragment fragmentA = new MainFragment();
        Bundle b = new Bundle();
        b.putString("tabTitle", tabTitle);
        b.putInt("position", position);
        fragmentA.setArguments(b);
        return fragmentA;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tabTitle = this.getArguments().getString("tabTitle");
        this.position = this.getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


}