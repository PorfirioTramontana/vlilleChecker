package com.vlille.checker.ui.async;

import android.os.AsyncTask;
import android.util.Log;

import com.vlille.checker.dataset.StationRepository;
import com.vlille.checker.model.SetStationsInfo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Retrieve all stations information from the local asset xml.
 */
public class SetStationsInfoAsyncTask extends AsyncTask<Void, Void, SetStationsInfo> {
    //ADDED
    public static Semaphore task_SetStationsInfoAsync_Finish;
    public static Semaphore task_SetStationsInfoAsync_Start;
    //END ADDED
    private SetStationsDelegate delegate;

    public SetStationsInfoAsyncTask(SetStationsDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected SetStationsInfo doInBackground(Void... params) {
        //ADDED
        if (task_SetStationsInfoAsync_Start != null) {
            try {
                task_SetStationsInfoAsync_Start.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task_SetStationsInfoAsync_Start.release();
        }
        //END ADDED

        SetStationsInfo ssi= StationRepository.getSetStationsInfo();

        //ADDED
        if (task_SetStationsInfoAsync_Finish != null) {
            try {
                if (!task_SetStationsInfoAsync_Finish.tryAcquire(15L, TimeUnit.SECONDS)) {
                    Log.d("TEST", "TASK: TIMEOUT task i");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("TEST", "TASK: End task i");
            task_SetStationsInfoAsync_Finish.release();
        }
        //END ADDED

        return ssi;
    }

    @Override
    protected void onPostExecute(SetStationsInfo setStationsInfo) {
        delegate.handleResult(setStationsInfo);

        super.onPostExecute(setStationsInfo);
    }

    public interface SetStationsDelegate {
        void handleResult(SetStationsInfo setStationsInfo);
    }
}