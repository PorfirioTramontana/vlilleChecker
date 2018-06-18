package com.vlille.checker.ui.async;

import android.os.AsyncTask;

import com.vlille.checker.dataset.StationRepository;
import com.vlille.checker.model.SetStationsInfo;

import java.util.concurrent.Semaphore;

import it.unina.ptramont.TaskTestUtility;

/**
 * Retrieve all stations information from the local asset xml.
 */
public class SetStationsInfoAsyncTask extends AsyncTask<Void, Void, SetStationsInfo> {
    public static Semaphore[] sem = new Semaphore[2];

    private SetStationsDelegate delegate;

    public SetStationsInfoAsyncTask(SetStationsDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected SetStationsInfo doInBackground(Void... params) {
        TaskTestUtility.startTask(sem);
        SetStationsInfo ssi = StationRepository.getSetStationsInfo();
        TaskTestUtility.finishTask(sem);

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