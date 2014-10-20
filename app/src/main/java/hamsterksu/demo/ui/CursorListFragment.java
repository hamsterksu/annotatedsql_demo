package hamsterksu.demo.ui;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ResourceCursorAdapter;

/**
 * Created by hamsterksu on 12.07.2014.
 */
public abstract class CursorListFragment extends ListFragment {

    protected static final int LOADER_ID = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(getEmptyTextId()));
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, createLoader());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(getTitleId());
    }

    private void updateAdapter(Cursor c) {
        ResourceCursorAdapter adapter = getListAdapter();
        if (adapter == null) {
            adapter = createAdapter();
            setListAdapter(adapter);
        }
        adapter.swapCursor(c);
    }

    @Override
    public ResourceCursorAdapter getListAdapter() {
        return (ResourceCursorAdapter) super.getListAdapter();
    }

    protected abstract int getTitleId();

    protected abstract int getEmptyTextId();

    protected abstract ResourceCursorAdapter createAdapter();

    protected abstract Loader<Cursor> onCreateLoader(int id);

    protected LoaderCallbacks<Cursor> createLoader(){
        return new LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return CursorListFragment.this.onCreateLoader(id);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                updateAdapter(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }

}
