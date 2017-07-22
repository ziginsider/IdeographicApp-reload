package app;

import android.app.Application;

import data.DatabaseHandlerExternal;
import data.DatabaseHandlerInner;

/**
 * Created by zigin on 19.07.2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private DatabaseHandlerExternal dbConnExternal;
    private DatabaseHandlerInner dbConnInner;


    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }



    public static synchronized AppController getInstance() {
        return  mInstance;
    }

    public DatabaseHandlerExternal getSQLiteConnectionExternal() {
        if (dbConnExternal == null) {
            dbConnExternal = new DatabaseHandlerExternal(getApplicationContext());
        }
        return dbConnExternal;
    }


    public DatabaseHandlerInner getSQLiteConnectionInner() {
        if (dbConnInner == null) {
            dbConnInner = new DatabaseHandlerInner(getApplicationContext());
        }
        return dbConnInner;
    }

    public void closeSQLiteConnects() {
        if (dbConnExternal != null) {
            dbConnExternal.close();
        }
        if (dbConnInner != null) {
            dbConnInner.close();
        }
    }
}
