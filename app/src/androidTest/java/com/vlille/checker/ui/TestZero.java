package com.vlille.checker.ui;


import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.vlille.checker.ui.async.AbstractStationsAsyncTask;
import com.vlille.checker.ui.async.DBUpdaterAsyncTask;
import com.vlille.checker.ui.async.SetStationsInfoAsyncTask;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unina.ptramont.utilityTest.GeneralEvent;
import it.unina.ptramont.utilityTest.SpecificUIEvent;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TestZero {

    @BeforeClass
    public static void startup() {
        GeneralEvent.setTime(GeneralEvent.NORMAL);
    }

    @Test
    public void ZeroTest() throws InterruptedException {
        GeneralEvent.declareandSetSemaphore(AbstractStationsAsyncTask.sem);
        GeneralEvent.declareandSetSemaphore(DBUpdaterAsyncTask.sem);
        GeneralEvent.declareandSetSemaphore(SetStationsInfoAsyncTask.sem);
        GeneralEvent.start(AbstractStationsAsyncTask.sem);
        GeneralEvent.finish(AbstractStationsAsyncTask.sem);
        GeneralEvent.start(DBUpdaterAsyncTask.sem);
        GeneralEvent.finish(DBUpdaterAsyncTask.sem);
        GeneralEvent.start(SetStationsInfoAsyncTask.sem);
        GeneralEvent.finish(SetStationsInfoAsyncTask.sem);


        GeneralEvent.startApp("com.vlille.checker");
        GeneralEvent.pause19();
        GeneralEvent.resume19();
        GeneralEvent.doubleRotation();
        SpecificUIEvent.execute(SpecificUIEvent.SWIPE);
        SpecificUIEvent.execute(SpecificUIEvent.REFRESH);
        SpecificUIEvent.execute(SpecificUIEvent.UPDATE_STATIONS_LIST);
    }



    @After
    public void tearDown() {
        Log.d("TEST", "End test");
    }


}
