package net.infobank.lab.testparseapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.infobank.lab.testparseapp.R;
import net.infobank.lab.testparseapp.adpter.PhoneListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe about this class here...
 *
 * @author ohjongin
 * @since 1.0
 * 15. 7. 1
 */
public class PhoneListFragment extends ListFragment {
    public static ListFragment newInstance(CharSequence label) {
        PhoneListFragment f = new PhoneListFragment();
        Bundle b = new Bundle();
        b.putCharSequence(LABEL, label);
        f.setArguments(b);
        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListAdapter = (PhoneListAdapter) getListAdapter();
        if (mListAdapter == null) {
            mListAdapter = new PhoneListAdapter(getActivity(), R.layout.fragment_list_row, new ArrayList<ParseObject>());
            setListAdapter(mListAdapter);
        }

        onRefresh();

        setHasOptionsMenu(true);
        registerForContextMenu(this.getListView());
    }

    @Override
    protected void onRefresh() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PhoneList");
        query.orderByDescending("updatedAt");
        query.addDescendingOrder("createdAt");
        query.setLimit(100);
        query.findInBackground(new FindCallback<ParseObject>() {
            @SuppressLint("CommitPrefEdits")
            public void done(final List<ParseObject> objList, ParseException e) {
                if (e == null) {
                    if (DEBUG_LOG) Log.d("", "Retrieved " + objList.size() + " files");
                    mListAdapter.clear();

                    for (ParseObject po : objList) {
                        mListAdapter.add(po);
                    }

                    if (mListAdapter.getCount() < 1) {
                        getView().findViewById(R.id.pbar_loading).setVisibility(View.GONE);
                        getView().findViewById(R.id.tv_no_file).setVisibility(View.VISIBLE);
                        Log.e("", "No files in the server!! category:" + ((getArguments() != null) ? getArguments().getString(SECTION) : "null"));
                    }
                    mListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDataSetChanged() {

    }

    protected Context getContext() {
        return getActivity();
    }
}
