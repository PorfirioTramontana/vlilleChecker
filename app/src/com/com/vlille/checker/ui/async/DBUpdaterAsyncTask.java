package com.vlille.checker.ui.async;

import android.os.AsyncTask;
import android.util.Log;

import com.vlille.checker.R;
import com.vlille.checker.db.DBUpdater;
import com.vlille.checker.ui.HomeActivity;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import it.unina.ptramont.TaskTestUtility;

/**
 * An {@link AsyncTask} to refresh stations from vlille.fr.
 */
public class DBUpdaterAsyncTask extends AsyncTask<Void, Void, Boolean> {
    //ADDED
    public static Semaphore[] sem = new Semaphore[2];
    //END ADDED
    private HomeActivity homeActivity;
    private com.vlille.checker.ui.async.AsyncTaskResultListener asyncListener;

    public DBUpdaterAsyncTask(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (asyncListener != null) {
            asyncListener.onAsyncTaskPreExecute();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //ADDED
        TaskTestUtility.startTask(sem);
        //END ADDED

        boolean b = new DBUpdater().update();
        
        //ADDED
        TaskTestUtility.finishTask(sem);
        //END ADDED
        return b;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (asyncListener != null) {
            asyncListener.onAsyncTaskPostExecute(result);
        }

        int resourceId = result
                ?  R.string.data_status_update_done
                : R.string.data_status_uptodate;

        homeActivity.showSnackBarMessage(resourceId);
    }

    public void setAsyncListener(com.vlille.checker.ui.async.AsyncTaskResultListener asyncListener) {
        this.asyncListener = asyncListener;
    }

}