package com.vlille.checker.ui.async;

import android.os.AsyncTask;
import android.util.Log;

import com.vlille.checker.dataset.StationRepository;
import com.vlille.checker.model.SetStationsInfo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import it.unina.ptramont.TaskTestUtility;

/**
 * Retrieve all stations information from the local asset xml.
 */
public class SetStationsInfoAsyncTask extends AsyncTask<Void, Void, SetStationsInfo> {
    //ADDED
    public static Semaphore[] sem = new Semaphore[2];
    //END ADDED
    private SetStationsDelegate delegate;

    public SetStationsInfoAsyncTask(SetStationsDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected SetStationsInfo doInBackground(Void... params) {
        //ADDED
        TaskTestUtility.startTask(sem);

        //END ADDED

        SetStationsInfo ssi= StationRepository.getSetStationsInfo();

        //ADDED
        TaskTestUtility.finishTask(sem);
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