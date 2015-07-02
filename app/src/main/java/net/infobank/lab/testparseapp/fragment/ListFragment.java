package net.infobank.lab.testparseapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.infobank.lab.testparseapp.GlobalConstant;
import net.infobank.lab.testparseapp.adpter.ParseListAdapter;

/**
 * Created by chunghj on 15. 7. 2..
 */
public abstract class ListFragment  extends android.support.v4.app.ListFragment implements GlobalConstant {
    protected static final boolean DEBUG_LOG = true;
    protected ParseListAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    abstract protected void onRefresh();

    abstract public void onDataSetChanged();
}
