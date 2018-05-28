package com.vlille.checker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.vlille.checker.ui.async.AbstractStationsAsyncTask;
import com.vlille.checker.ui.async.DBUpdaterAsyncTask;
import com.vlille.checker.ui.async.SetStationsInfoAsyncTask;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Semaphore;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class UIAutomatorAsyncTaskZeroTest {

    private static final String BASIC_SAMPLE_PACKAGE
            = "com.vlille.checker";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;

    private static void DoSwipe(UiObject u) {
        UiDevice deviceInstance = UiDevice.getInstance();
        int dHeight =  deviceInstance.getDisplayHeight();
        int dWidth = deviceInstance.getDisplayWidth();
        int xScrollPosition = dWidth/2;
        int yScrollStop = 200+dHeight/2;
        UiDevice.getInstance().swipe(
                xScrollPosition,
                0,
                xScrollPosition,
                yScrollStop,
                100
        );
    }


    @Test
    public void ZeroTest() throws InterruptedException, UiObjectNotFoundException, RemoteException {
        // Test di prova

        //Semaphore Declaration
        SetStationsInfoAsyncTask.task_SetStationsInfoAsync_Start = new Semaphore(1);
        SetStationsInfoAsyncTask.task_SetStationsInfoAsync_Finish = new Semaphore(1);
        DBUpdaterAsyncTask.task_DBUpdaterAsync_Finish = new Semaphore(1);
        DBUpdaterAsyncTask.task_DBUpdaterAsync_Start = new Semaphore(1);
        AbstractStationsAsyncTask.task_AbstractStationsAsync_Start=new Semaphore(1);
        AbstractStationsAsyncTask.task_AbstractStationsAsync_Finish=new Semaphore(1);

        //Set Semaphores
        SetStationsInfoAsyncTask.task_SetStationsInfoAsync_Start.acquire();
        SetStationsInfoAsyncTask.task_SetStationsInfoAsync_Finish.acquire();
        DBUpdaterAsyncTask.task_DBUpdaterAsync_Finish.acquire();
        DBUpdaterAsyncTask.task_DBUpdaterAsync_Start.acquire();
        AbstractStationsAsyncTask.task_AbstractStationsAsync_Start.acquire();
        AbstractStationsAsyncTask.task_AbstractStationsAsync_Finish.acquire();

        // Initialize UiDevice instance and Start application
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT * 100);
        Log.d("TEST", "TEST: Application Started");



        //Start SetStationsInfoAsync
        SetStationsInfoAsyncTask.task_SetStationsInfoAsync_Start.release();
        Log.d("TEST", "TEST: Start SetStationsInfoAsync");

        //Finish SetStationsInfoAsync
        Thread.sleep(1000);
        Log.d("TEST", "TEST: Finish SetStationsInfoAsync");
        SetStationsInfoAsyncTask.task_SetStationsInfoAsync_Finish.release();

        //Start DBUpdaterAsync
        Thread.sleep(1000);
        Log.d("TEST", "TEST: Start DBUpdaterAsyncAsync");
        DBUpdaterAsyncTask.task_DBUpdaterAsync_Start.release();

        //Finish DBUpdaterAsync
        Thread.sleep(1000);
        Log.d("TEST", "TEST: Finish DBUpdaterAsyncAsync");
        DBUpdaterAsyncTask.task_DBUpdaterAsync_Finish.release();

        //Swipe
        Thread.sleep(2000);
        (mDevice.findObject(new UiSelector().resourceId("com.vlille.checker:id/swipeable_list"))).swipeDown(5);

        //Start AbstractStationsAsync
        AbstractStationsAsyncTask.task_AbstractStationsAsync_Start.release();
        Log.d("TEST", "TEST: Start AbstractStationsAsyncAsync");

        //Finish AbstractStationsAsync
        Thread.sleep(1000);
        Log.d("TEST", "TEST: Finish AbstractStationsAsyncAsync");
        AbstractStationsAsyncTask.task_AbstractStationsAsync_Finish.release();

        //Pause app
        Thread.sleep(1000);
        mDevice.pressHome();

        //Resume app
        Thread.sleep(1000);
        mDevice.pressRecentApps();
        Thread.sleep(1000);
        mDevice.pressRecentApps();

        //Click on Refresh
        Thread.sleep(2000);
        (mDevice.findObject(new UiSelector().descriptionContains("Refresh"))).click();

        //Click on Update Stations List
        Thread.sleep(2000);
        mDevice.pressMenu();
        (mDevice.findObject(new UiSelector().textContains("Update stations list"))).click();
        //End Test
    }


}


