package com.kurlic.yandexmap;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

/**
 * This example shows how to display and customize user location arrow on the map.
 */
public class MainActivity extends Activity implements UserLocationObjectListener, LocationListener {
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private MapView mapView;
    private UserLocationLayer userLocationLayer;

    final Point moscowPoint = new Point(55.71989101308894, 37.5689757769603);
    final Animation pingAnimation = new Animation(Animation.Type.SMOOTH, 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("cb2b8643-e331-40cc-bcd4-08c7eb895584");
        MapKitFactory.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.mapview);
        mapView.getMap().setRotateGesturesEnabled(true);
        mapView.getMap().move(new CameraPosition(moscowPoint, 14, 0, 0), pingAnimation, null);

        requestLocationPermission();

        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(false);

        userLocationLayer.setObjectListener(this);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {

        /*
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
                */

        /*
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));

         */
        Toast.makeText(getApplicationContext(), "onObjectAdded", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
        Toast.makeText(getApplicationContext(), "onObjectRemoved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {



        /*
        pinIcon.setIcon(
                "icon",
                ImageProvider.fromResource(this, R.drawable.icon),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );
        */

        /*
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.search_result),
                new IconStyle().setAnchor(new PointF(1, 1))
        );

         */

        Point pinIcon = view.getArrow().getGeometry();
        if(pinIcon.getLatitude() != 0) {
            mapView.getMap().move(new CameraPosition(pinIcon, 14, 0, 0), pingAnimation, null);
        }

        view.getAccuracyCircle().setFillColor(Color.BLUE);
        Toast.makeText(getApplicationContext(), "onObjectUpdated", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationUpdated(@NonNull Location location) {

        Point currPoint = location.getPosition();
        mapView.getMap().move(new CameraPosition(currPoint, 14, 0, 0), pingAnimation, null);

    }

    @Override
    public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

    }
}
