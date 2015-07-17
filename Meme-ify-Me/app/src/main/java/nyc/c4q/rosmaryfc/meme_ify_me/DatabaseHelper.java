package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by c4q-ac35 on 7/17/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String MEMEDB = "memedb.db";
    private static final int VERSION = 1;

    private static DatabaseHelper mHelper;

    public DatabaseHelper(Context context){
        super(context,MEMEDB,null,VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,Meme.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Meme.class,true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow(int picture, String title) throws SQLException {
        Meme meme = new Meme(picture,title);
        getDao(Meme.class).create(meme);
    }

    public List<Meme> loadData() throws SQLException {
        return getDao(Meme.class).queryForAll();
    }
}
