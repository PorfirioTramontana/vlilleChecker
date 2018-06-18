package com.vlille.checker.ui.async;

import android.os.AsyncTask;
import android.util.Log;

import com.vlille.checker.dataset.StationRepository;
import com.vlille.checker.model.Station;
import com.vlille.checker.ui.HomeActivity;
import com.vlille.checker.ui.delegate.StationUpdateDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import it.unina.ptramont.TaskTestUtility;

/**
 * Task to retrieve details from a stations list.
 */
public abstract class AbstractStationsAsyncTask extends AsyncTask<List<Station>, Void, List<Station>> {
    public static Semaphore[] sem = new Semaphore[2];

    private static final String TAG = "AsyncStationTaskUpdater";

    private final HomeActivity homeActivity;
    private final StationUpdateDelegate delegate;

    private boolean plateformUnstableState;
    private boolean platformUpdateIssueState;

    protected AbstractStationsAsyncTask(HomeActivity homeActivity, StationUpdateDelegate delegate) {
        this.homeActivity = homeActivity;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        plateformUnstableState = false;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        plateformUnstableState = false;
    }

    @Override
    protected List<Station> doInBackground(List<Station>... params) {
        Log.d(TAG, "Launch background update...");
        TaskTestUtility.startTask(sem);


        final List<Station> stations = new ArrayList<>(params[0]);
        int countStationsFetchInError = 0;
        int countStationsWithLastUpdateExceedingTwoMinutes = 0;

        StationRepository.fillStationsCache();

        for (Station station : stations) {
            if (isCancelled()) {
                Log.d(TAG, "Task has been cancelled.");
                TaskTestUtility.finishTask(sem);

                return stations;
            }

            station = StationRepository.getStationFromCache(station);
            delegate.update(station);

            if (station.isFetchInError()) {
                countStationsFetchInError++;
            }
            if (station.isLastUpdateTimeExceedTwoMinutes()) {
                countStationsWithLastUpdateExceedingTwoMinutes++;
            }

            publishProgress();
        }

        plateformUnstableState = countStationsFetchInError == stations.size();
        platformUpdateIssueState = countStationsWithLastUpdateExceedingTwoMinutes == stations.size();
        TaskTestUtility.finishTask(sem);

        return stations;
    }

    @Override
    protected void onPostExecute(List<Station> stations) {
        super.onPostExecute(stations);

        if (plateformUnstableState) {
            homeActivity.showPlatformUnstableMessage();
        }

        if (platformUpdateIssueState) {
            homeActivity.showPlatformUpdateIssueMessage();
        }
    }

}
