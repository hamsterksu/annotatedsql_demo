package hamsterksu.demo.ui;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import hamsterksu.demo.R;
import hamsterksu.demo.store.PostSchema2.PostsView2.CommentTable;
import hamsterksu.demo.store.PostSchema2.PostsView2.PostTable;
import hamsterksu.demo.store.PostStore.PostsView;
import hamsterksu.demo.store.PostsProvider;

/**
 * Created by hamsterksu on 12.07.2014.
 */
public class PostsListFragment extends CursorListFragment {

    private static final String[] PROJECTION = new String[]{PostTable.ID, PostTable.TEXT, "count (" + CommentTable.ID + ") as cc"};
    private static final int COLUMN_TEXT = 1;
    private static final int COLUMN_COUNT = 2;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListener().onShowComments(id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getTitleId() {
        return R.string.posts_title;
    }

    @Override
    protected int getEmptyTextId() {
        return R.string.posts_empty;
    }

    @Override
    protected ResourceCursorAdapter createAdapter() {
        return new PostsAdapter(getActivity());
    }

    @Override
    protected Loader<Cursor> onCreateLoader(int id) {
        //will generate query like
        // select post_table.id, post_table.text, count(comment_table.id) as cc from post_view group by post_table.id
        return new CursorLoader(getActivity(),
                PostsProvider.contentUriGroupBy(PostsView.CONTENT_URI, PostTable.ID),
                PROJECTION,
                null, null, null);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.posts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            getListener().onShowAddPost();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private IPostActionListener getListener() {
        return (IPostActionListener) getActivity();
    }

    private class PostsAdapter extends ResourceCursorAdapter {

        public PostsAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor c) {
            TextView text = (TextView) view;
            //c.getString(COLUMN_COUNT) - no need to get int, we just need String
            text.setText(String.format("%s (%s)", c.getString(COLUMN_TEXT), c.getString(COLUMN_COUNT)));
        }
    }

    public static interface IPostActionListener {
        void onShowAddPost();
        void onShowComments(long postId);
    }
}
