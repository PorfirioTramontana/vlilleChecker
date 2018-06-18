package it.unina.ptramont.utilityTest;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

public class SpecificUIEvent extends GeneralEvent {

    public static final int SWIPE = 0;
    public static final int REFRESH = 1;
    public static final int UPDATE_STATIONS_LIST = 2;

    public static void execute(int ev) {
        switch (ev) {
            case SWIPE:
                try {
                    Thread.sleep(TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    (mDevice.findObject(new UiSelector().resourceId("com.vlille.checker:id/swipeable_list"))).swipeDown(5);
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case REFRESH:
                try {
                    Thread.sleep(TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    (mDevice.findObject(new UiSelector().descriptionContains("Refresh"))).click();
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case UPDATE_STATIONS_LIST:
                try {
                    Thread.sleep(TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mDevice.pressMenu();
                try {
                    (mDevice.findObject(new UiSelector().textContains("Update stations list"))).click();
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
