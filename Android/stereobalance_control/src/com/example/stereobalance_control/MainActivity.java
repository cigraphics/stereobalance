package com.example.stereobalance_control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

public class MainActivity extends Activity implements SensorEventListener{
	private float mXf,mYf,mZf;
	private SensorManager sm1;
	private Sensor mAcc;
	private final float NOISE=(float)2;
	float angle1,angle2;
	float dir;
	String turn;
	WebView p;
	
	private float computeAngle(float axis1,float axis2){
    	//Compute angle from axis values
		float angle=(float)Math.atan(axis1/axis2);
    	return angle;
    }
	
	void launch_http(float dir, String turn,WebView view){
		String rq=new String("10.10.0.236/stereobalance/index.html");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(rq);

		try {
		    // Add your data
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("dir", Float.toString(dir)));
		    nameValuePairs.add(new BasicNameValuePair("turn", turn));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		    // Execute HTTP Post Request
		    HttpResponse response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm1=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcc=sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm1.registerListener(this, mAcc,SensorManager.SENSOR_DELAY_NORMAL);
        String turn=new String();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	sm1.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
    	p=(WebView)findViewById(R.id.page);
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	sm1.unregisterListener(this);
    }
    
    @Override
    public void onAccuracyChanged(Sensor s, int accuracy){
    }
    
    @Override
    public void onSensorChanged(SensorEvent event){
    	angle1=(float)0;
    	angle2=(float)0;
    	float x=event.values[0];
    	float y=event.values[1];
    	float z=event.values[2];
    	String text1=new String();
    	String text2=new String();
    	mXf=x;
    	mYf=y;
    	mZf=z;
	    angle1=this.computeAngle(mZf,mXf);
	    angle2=this.computeAngle(mZf,mYf);
	    float f1=Math.abs(angle1);
	    float f2=Math.abs(angle2);
	    if (f1<NOISE) angle1=(float)0;
	    if (f2<NOISE) angle2=(float)0;
	    if (angle1>0){
	    	dir=1;
	    } else 
	    	if (angle1==0){
	    		dir=0;
	    	} else dir=-1;
	    if (angle2>0){
	    	turn="r";
	    } else if (angle2==0){
	    	turn="0";
	    } else turn="l";
	    launch_http(dir,turn,p);
    }
}