package publictransit.in;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class TransitMap extends MapActivity
{
    private MapView mapView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.transitmap);
	mapView = (MapView) findViewById(R.id.mapview);
	final MapController mc = mapView.getController();
	mc.setCenter(new GeoPoint(13100000, 80250000));
	mc.setZoom(12);
    }

    @Override
    protected boolean isRouteDisplayed()
    {
	return false;
    }

}
