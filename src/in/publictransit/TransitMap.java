package in.publictransit;

import in.publictransit.model.Database;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class TransitMap extends MapActivity
{
    private MapView mapView;
    Database db;
    private static final GeoPoint DEFAULT_CENTER = new GeoPoint(13100000,
	    80250000);
    private static final int DEFAULT_ZOOM = 12;
    private static final int STOP_ZOOM = 16;

    private void CenterAndZoomMap(String stopName)
    {
	final MapController mc = mapView.getController();
	if (stopName == null) {
	    mc.setCenter(DEFAULT_CENTER);
	    mc.setZoom(DEFAULT_ZOOM);
	} else {
	    GeoPoint point = db.getLocation(stopName);
	    if (point != null) {
		mc.setCenter(point);
		mc.setZoom(STOP_ZOOM);
	    } else {
		mc.setCenter(DEFAULT_CENTER);
		mc.setZoom(DEFAULT_ZOOM);
	    }
	}
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	Bundle extras = getIntent().getExtras();
	db = new Database(TransitMap.this);
	setContentView(R.layout.transitmap);
	mapView = (MapView) findViewById(R.id.mapview);
	if (extras != null && extras.getString("query_type") != null
		&& extras.getString("query_type").equals("goto_stop")) {
	    CenterAndZoomMap(extras.getString("stop_name"));
	} else {
	    CenterAndZoomMap(null);
	}
    }

    @Override
    public void onNewIntent(Intent newIntent)
    {
	super.onNewIntent(newIntent);
	Bundle extras = newIntent.getExtras();
	if (extras != null && extras.getString("query_type") != null
		&& extras.getString("query_type").equals("goto_stop")) {
	    CenterAndZoomMap(extras.getString("stop_name"));
	} else {
	    CenterAndZoomMap(null);
	}
    }

    @Override
    protected boolean isRouteDisplayed()
    {
	return false;
    }

}
