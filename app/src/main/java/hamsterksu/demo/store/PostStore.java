package hamsterksu.demo.store;

import com.annotatedsql.annotation.provider.Provider;
import com.annotatedsql.annotation.provider.URI;
import com.annotatedsql.annotation.sql.Autoincrement;
import com.annotatedsql.annotation.sql.Column;
import com.annotatedsql.annotation.sql.Column.Type;
import com.annotatedsql.annotation.sql.Columns;
import com.annotatedsql.annotation.sql.From;
import com.annotatedsql.annotation.sql.Join;
import com.annotatedsql.annotation.sql.NotNull;
import com.annotatedsql.annotation.sql.PrimaryKey;
import com.annotatedsql.annotation.sql.Schema;
import com.annotatedsql.annotation.sql.SimpleView;
import com.annotatedsql.annotation.sql.Table;

/**
 * Created by hamsterksu on 12.07.2014.
 */
@Provider(name = "PostsProvider", schemaClass = "PostSchema", authority = "hamsterksu.demo.AUTHORITY")
@Schema(className = "PostSchema", dbName = "posts.db", dbVersion = 1)
public interface PostStore {

    /**
     * we can use inheritance
     */
    public static interface BaseTable{

        @PrimaryKey
        @Autoincrement
        @Column(type = Type.INTEGER)
        String ID = "_id";

        @Column(type = Type.TEXT)
        String TEXT = "description";
    }

    @Table(PostTable.TABLE_NAME)
    public static interface PostTable extends BaseTable{

        String TABLE_NAME = "post";

        //notify our view about changes in the table
        @URI(altNotify = PostsView.CONTENT_URI)
        String CONTENT_URI = "post";
    }

    @Table(CommentTable.TABLE_NAME)
    public static interface CommentTable extends BaseTable{

        String TABLE_NAME = "comment";

        @NotNull
        @Column(type = Type.INTEGER)
        String POST_ID = "post_id";

        @URI(onlyQuery = true, column = POST_ID)
        String CONTENT_URI_BY_POST = "comment_by_post";

        //notify relative uris about changes in the tables
        @URI(altNotify = {PostsView.CONTENT_URI, CommentTable.CONTENT_URI_BY_POST})
        String CONTENT_URI = "comment";
    }

    /**
     * we want to calculate comments in post. we will use group by PostTable.ID
     */
    @SimpleView(PostsView.VIEW_NAME)
    public static interface PostsView{

        //VIEW_NAME - required field!!!
        String VIEW_NAME = "post_view";

        //no need to insert/delete/edit into view, only query :)
        @URI(onlyQuery = true)
        String CONTENT_URI = "post_view";

        @From(PostTable.TABLE_NAME)
        String TABLE_POST = "post_table";

        //we just need one filed from table
        @Columns(CommentTable.ID)
        @Join(type = Join.Type.LEFT, joinTable = CommentTable.TABLE_NAME, joinColumn = CommentTable.POST_ID, onTableAlias = TABLE_POST, onColumn = PostTable.ID)
        String TABLE_COMMENT = "comment_table";
    }
}
