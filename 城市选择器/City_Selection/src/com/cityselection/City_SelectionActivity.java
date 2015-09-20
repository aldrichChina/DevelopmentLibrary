package com.cityselection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class City_SelectionActivity extends Activity {
    /** Called when the activity is first created. */
	
	private File f = new File("/sdcard/weather/db_weather.db"); //���ݿ��ļ�
	
	private Spinner province;  //ʡ��spinner
	private Spinner city;      //����spinner
	
	private List<String> proset=new ArrayList<String>();//ʡ�ݼ���
	private List<String> citset=new ArrayList<String>();//���м���
	
	private int pro_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        province=(Spinner)findViewById(R.id.provinces);
        ArrayAdapter<String> pro_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getProSet());
        province.setAdapter(pro_adapter);
        province.setOnItemSelectedListener(new SelectProvince());
        
        city=(Spinner)findViewById(R.id.city);
        city.setOnItemSelectedListener(new SelectCity());
    }
   
    //ѡ��ı�״̬
    class SelectProvince implements OnItemSelectedListener{
    	public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
    		// TODO Auto-generated method stub
    		//���ʡ��ID
    		pro_id=position;  		
    		city.setAdapter(getAdapter());
    		
    	}
    	public void onNothingSelected(AdapterView<?> arg0) {
    		// TODO Auto-generated method stub
    		
    	}
    }
    
    //���� ѡ��ı�״̬
    class SelectCity implements OnItemSelectedListener{
    	public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
    		// TODO Auto-generated method stub
    		String cityname=parent.getItemAtPosition(position).toString();
    		//ѡ����ʾ
    		Toast.makeText(getApplicationContext(), cityname+" "+getCityNum(position), 2000).show();
    	    
    	  
    	}
    	public void onNothingSelected(AdapterView<?> arg0) {
    		// TODO Auto-generated method stub
    		
    	}
    }
    
    /**
     * ���� ʡ�ݼ���
     */
    public List<String> getProSet(){
       //�����ݿ� 
    	SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(f, null);
 		Cursor cursor=db1.query("provinces", null, null, null, null, null, null);
 		while(cursor.moveToNext()){
 			String pro=cursor.getString(cursor.getColumnIndexOrThrow("name"));
 			proset.add(pro);
 		}
 		cursor.close();
 		db1.close();
    	return proset;
    }
    /**
     * ���� ���м���
     */
    public List<String> getCitSet(int pro_id){
    	//��ճ��м���
    	citset.clear();
       //�����ݿ� 
    	SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(f, null);
 		Cursor cursor=db1.query("citys", null, "province_id="+pro_id, null, null, null, null);
 		while(cursor.moveToNext()){
 			String pro=cursor.getString(cursor.getColumnIndexOrThrow("name"));
 			citset.add(pro);
 		}
 		cursor.close();
 		db1.close();
    	return citset;
    }
    /**
     * ����������
     */
    public ArrayAdapter<String> getAdapter(){
    	  ArrayAdapter<String> adapter1=new ArrayAdapter(this, android.R.layout.simple_spinner_item,getCitSet(pro_id));
          return adapter1;
    }
     /**
      * ���س��б��  �Ա��������Ԥ��api
      */
    public long getCityNum(int position){
    	SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(f, null);
 		Cursor cursor=db1.query("citys", null, "province_id="+pro_id, null, null, null, null);
 		cursor.moveToPosition(position);
 		long citynum=Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("city_num")));
 		cursor.close();
 		db1.close();
 		return citynum;
    }
    
}