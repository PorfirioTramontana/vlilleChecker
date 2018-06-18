package com.vlille.checker.ui.async;

import android.os.AsyncTask;

import com.vlille.checker.R;
import com.vlille.checker.db.DBUpdater;
import com.vlille.checker.ui.HomeActivity;

import java.util.concurrent.Semaphore;

import it.unina.ptramont.TaskTestUtility;

/**
 * An {@link AsyncTask} to refresh stations from vlille.fr.
 */
public class DBUpdaterAsyncTask extends AsyncTask<Void, Void, Boolean> {
    public static Semaphore[] sem = new Semaphore[2];

    private HomeActivity homeActivity;
    private AsyncTaskResultListener asyncListener;

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
        TaskTestUtility.startTask(sem);
        Boolean b = new DBUpdater().update();
        TaskTestUtility.finishTask(sem);

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

    public void setAsyncListener(AsyncTaskResultListener asyncListener) {
        this.asyncListener = asyncListener;
    }

}