package com.vlille.checker.ui.osm.overlay.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vlille.checker.ui.osm.overlay.ExtendedOverlayItem;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

/**
 * View that can be displayed on an OSMDroid map, associated to a GeoPoint.
 * Typical usage: cartoon-like bubbles displayed when clicking an overlay item.
 * It mimics the InfoWindow class of Google Maps JavaScript API V3.
 * Main differences are:
 * <ul>
 * <li>Structure and content of the view is let to the responsibility of the caller. </li>
 * <li>The same InfoWindow can be associated to many items. </li>
 * </ul>
 * Known issues:
 * <ul>
 * <li>It disappears when zooming in/out
 * (osmdroid issue #259 on osmdroid 3.0.8, should be fixed in next version). </li>
 * <li>The window is displayed "above" the marker, so the queue of the bubble can hide the marker. </li>
 * </ul>
 * <p>
 * This is an abstract class.
 *
 * @author M.Kergall
 * @see DefaultInfoWindow
 */
public abstract class InfoWindow {

    protected View mView;
    protected boolean mIsVisible = false;
    protected MapView mMapView;
    private int zoomLevel;

    /**
     * @param layoutResId the id of the view resource.
     * @param mapView     the mapview on which is hooked the view
     */
    public InfoWindow(int layoutResId, MapView mapView) {
        mMapView = mapView;
        mIsVisible = false;
        ViewGroup parent = (ViewGroup) mapView.getParent();
        Context context = mapView.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(layoutResId, parent, false);
    }

    /**
     * Returns the Android view. This allows to set its content.
     *
     * @return the Android view
     */
    public View getView() {
        return mView;
    }

    /**
     * open the window at the specified position.
     *
     * @param item    the item on which is hooked the view
     * @param offsetX (&offsetY) the offset of the view to the position, in pixels.
     *                This allows to offset the view from the marker position.
     */
    public void open(ExtendedOverlayItem item, int offsetX, int offsetY) {
        onOpen(item);
        IGeoPoint position = item.getPoint();
        MapView.LayoutParams lp = new MapView.LayoutParams(
                MapView.LayoutParams.WRAP_CONTENT,
                MapView.LayoutParams.WRAP_CONTENT,
                position, MapView.LayoutParams.BOTTOM_CENTER,
                offsetX, offsetY);
        close(); //if it was already opened
        mMapView.addView(mView, lp);
        mIsVisible = true;
    }

    public void close() {
        if (mIsVisible) {
            mIsVisible = false;
            ((ViewGroup) mView.getParent()).removeView(mView);
            onClose();
        }
    }

    public boolean isOpen() {
        return mIsVisible;
    }

    //Abstract methods to implement:
    public abstract void onOpen(ExtendedOverlayItem item);

    public abstract void onClose();

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

}
