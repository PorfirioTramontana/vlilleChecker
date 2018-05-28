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
import java.util.concurrent.TimeUnit;

/**
 * Task to retrieve details from a stations list.
 */
public abstract class AbstractStationsAsyncTask extends AsyncTask<List<Station>, Void, List<Station>> {
    //ADDED
    public static Semaphore task_AbstractStationsAsync_Finish;
    public static Semaphore task_AbstractStationsAsync_Start;
    //END ADDED
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
        //ADDED
        if (task_AbstractStationsAsync_Start != null) {
            try {
                task_AbstractStationsAsync_Start.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task_AbstractStationsAsync_Start.release();
        }
        //END ADDED
        
        Log.d(TAG, "Launch background update...");

        final List<Station> stations = new ArrayList<>(params[0]);
        int countStationsFetchInError = 0;
        int countStationsWithLastUpdateExceedingTwoMinutes = 0;

        StationRepository.fillStationsCache();

        for (Station station : stations) {
            if (isCancelled()) {
                Log.d(TAG, "Task has been cancelled.");
                //ADDED
                if (task_AbstractStationsAsync_Finish != null) {
                    try {
                        if (!task_AbstractStationsAsync_Finish.tryAcquire(15L, TimeUnit.SECONDS)) {
                            Log.d("TEST", "TASK: TIMEOUT task i");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("TEST", "TASK: End task i");
                    task_AbstractStationsAsync_Finish.release();
                }
                //END ADDED
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
        //ADDED
        if (task_AbstractStationsAsync_Finish != null) {
            try {
                if (!task_AbstractStationsAsync_Finish.tryAcquire(15L, TimeUnit.SECONDS)) {
                    Log.d("TEST", "TASK: TIMEOUT task i");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("TEST", "TASK: End task i");
            task_AbstractStationsAsync_Finish.release();
        }
        //END ADDED
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
