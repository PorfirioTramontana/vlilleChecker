package com.vlille.checker.ui.widget;

import android.os.AsyncTask;
import android.util.Log;

import com.vlille.checker.dataset.StationRepository;
import com.vlille.checker.model.Station;
import com.vlille.checker.ui.delegate.StationUpdateDelegate;

public class WidgetAsyncTask extends AsyncTask<Station, Void, Station> {

    private static final String TAG = WidgetAsyncTask.class.getSimpleName();

    private StationUpdateDelegate viewDelegate;

    public WidgetAsyncTask(StationUpdateDelegate viewDelegate) {
        this.viewDelegate = viewDelegate;
    }

    @Override
    protected Station doInBackground(Station... stations) {
        Station station = stations[0];

        Log.v(TAG, "Fetch remote info: " + station.getId());

        StationRepository.getStation(station);
        viewDelegate.update(station);

        publishProgress();

        return null;
    }

}
