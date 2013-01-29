package com.example.blueui;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public  class MainActivity<readThread> extends Activity implements SensorEventListener{
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	  private static final String TAG = "BLUEUI";
	  private static String address = "00:12:10:11:12:62";
	  static byte[] ask = { 64, 83, -65 };//电池查询
	  private static byte[] getNumber;
	  byte Back = -1;
	  int Battery;
	//  int BtnHeight = 100;
	 // int BtnWidth = 176;
	  byte Direction = -93;
	  byte Enter = -65;
	  byte Fast = 2;
	  byte Head = 64;
	  byte Move = -94;
	  byte Slow = 1;
	  byte Stop = 0;
	  int Velocity;
	  boolean link=false;
	  int count = 0;
	  final Handler handler = new Handler();
	//  Handler handler=new Handler(); 
      Runnable runnable;
	  
	  public BluetoothAdapter mBluetoothAdapter = null;
	  
	  public BluetoothSocket btSocket = null;
	  public OutputStream outStream = null;
	  public InputStream mmInStream= null;
	  ProgressBar progressBar = null;
	  SeekBar seekBarPWM;
	  private Sensor mAccelerometer;
	  private SensorManager mSensorManager;
	  TextView textBattery;
	  
	  private readThread mreadThread = null;//读取数据线程

	  Button mButtonBack;
	  Button mButtonFast;
	  Button mButtonSlow;
	  Button mButtonStop;
	  Button mButtonShow;
	  
	  public void DisplayToast(String paramString)
	  {
	    Toast localToast = Toast.makeText(this, paramString, 1);
	    localToast.setGravity(48, 0, 220);
	    localToast.show();
	  }

	 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter == null)
        {
        	Toast.makeText(this, "蓝牙设备不可用，请打开蓝牙！", Toast.LENGTH_LONG).show();
          finish();
          return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
        	Toast.makeText(this,  "请打开蓝牙并重新运行程序！", Toast.LENGTH_LONG).show();
        	finish();
		//	mBluetoothAdapter.enable();
			return;
        }
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        this.seekBarPWM = ((SeekBar)findViewById(R.id.seekBar));
        this.progressBar = ((ProgressBar)findViewById(R.id.progressBar1));
        this.textBattery = ((TextView)findViewById(R.id.textView1));
        this.mButtonShow=((Button)findViewById(R.id.btnShow));
        mButtonShow.setText("连接中 未连接上");
        this.mButtonFast = ((Button)findViewById(R.id.btnFast));
        this.mButtonSlow = ((Button)findViewById(R.id.btnSlow));
        this.mButtonBack = ((Button)findViewById(R.id.btnBack));
        this.mButtonStop = ((Button)findViewById(R.id.btnStop));
        mButtonStop.setBackgroundResource(R.drawable.stopon);
        this.mButtonFast.setOnTouchListener(new View.OnTouchListener()
        {
          public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
          {
            switch (paramMotionEvent.getAction())
            {
          //  default:
            case MotionEvent.ACTION_DOWN:
            {

                // return false;
            	if(link){
            		mButtonSlow.setBackgroundResource(R.drawable.slow1);
          		  mButtonStop.setBackgroundResource(R.drawable.stop);
          		  mButtonBack.setBackgroundResource(R.drawable.back);
          		  mButtonFast.setBackgroundResource(R.drawable.faston);
            	}
                 MainActivity.this.Velocity = 2;
                 byte[] arrayOfByte2 = new byte[4];
                 arrayOfByte2[0] = MainActivity.this.Head;
                 arrayOfByte2[1] = MainActivity.this.Move;
                 arrayOfByte2[2] = MainActivity.this.Fast;
                 arrayOfByte2[3] = MainActivity.this.Enter;
                 try
                 {
                   MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                   try
                   {
                   MainActivity.this.outStream.write(arrayOfByte2);
                   }
                   catch (IOException localIOException4)
                   {
                   }
        //           continue;
                 
                 }
                 catch (IOException localIOException3)
                 {
               //    break label104;
                 }
              
            }
            case MotionEvent.ACTION_UP:
            {

                // return false;
                 MainActivity.this.Velocity = 2;
                 byte[] arrayOfByte2 = new byte[4];
                 arrayOfByte2[0] = MainActivity.this.Head;
                 arrayOfByte2[1] = MainActivity.this.Move;
                 arrayOfByte2[2] = MainActivity.this.Fast;
                 arrayOfByte2[3] = MainActivity.this.Enter;
                 try
                 {
                   MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                   try
                   {
                   MainActivity.this.outStream.write(arrayOfByte2);
                   }
                   catch (IOException localIOException4)
                   {
                   }
        //           continue;
                 
                 }
                 catch (IOException localIOException3)
                 {
               //    break label104;
                 }
              
            }
            }
           return false;
          }
        });
        
        
       
//      this.mButtonSlow.setHeight(this.BtnHeight);
//      this.mButtonSlow.setWidth(this.BtnWidth);
      this.mButtonSlow.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          switch (paramMotionEvent.getAction())
          {
        
          case MotionEvent.ACTION_DOWN:
          {

     	     //       return false;
        	  if(link)
        	  {
        		  mButtonSlow.setBackgroundResource(R.drawable.slow1on);
        		  mButtonStop.setBackgroundResource(R.drawable.stop);
        		  mButtonBack.setBackgroundResource(R.drawable.back);
        		  mButtonFast.setBackgroundResource(R.drawable.fast);
        	  }
        		  MainActivity.this.Velocity = 1;
     	            byte[] arrayOfByte2 = new byte[4];
     	            arrayOfByte2[0] = MainActivity.this.Head;
     	            arrayOfByte2[1] = MainActivity.this.Move;
     	            arrayOfByte2[2] = MainActivity.this.Slow;
     	            arrayOfByte2[3] = MainActivity.this.Enter;
     	            try
     	            {
     	              MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
     	              try
     	              {
     	                 MainActivity.this.outStream.write(arrayOfByte2);
     	              }
     	              catch (IOException localIOException4)
     	              {
     	              }
     	     //         continue;
     	             
     	              
     	            
     	            }
     	            catch (IOException localIOException3)
     	            {
     	    //          break label104;
     	            }
     	          
       }
        	  
          case MotionEvent.ACTION_UP:
          {

      	     //       return false;
      	            MainActivity.this.Velocity = 1;
      	            byte[] arrayOfByte2 = new byte[4];
      	            arrayOfByte2[0] = MainActivity.this.Head;
      	            arrayOfByte2[1] = MainActivity.this.Move;
      	            arrayOfByte2[2] = MainActivity.this.Slow;
      	            arrayOfByte2[3] = MainActivity.this.Enter;
      	            try
      	            {
      	              MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
      	              try
      	              {
      	                 MainActivity.this.outStream.write(arrayOfByte2);
      	              }
      	              catch (IOException localIOException4)
      	              {
      	              }
      	     //         continue;
      	             
      	              
      	            
      	            }
      	            catch (IOException localIOException3)
      	            {
      	    //          break label104;
      	            }
      	          
        }
          }
         return false;
        }
      });
      
      
      //   this.mButtonBack.setHeight(this.BtnHeight);
     //    this.mButtonBack.setWidth(this.BtnWidth);
         this.mButtonBack.setOnTouchListener(new View.OnTouchListener()
         {
           public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
           {
             switch (paramMotionEvent.getAction())
             {
           
             case MotionEvent.ACTION_DOWN:
             {

                //     return false;
            	 if(link){
            	  mButtonSlow.setBackgroundResource(R.drawable.slow1);
           		  mButtonStop.setBackgroundResource(R.drawable.stop);
           		  mButtonBack.setBackgroundResource(R.drawable.backon);
           		  mButtonFast.setBackgroundResource(R.drawable.fast);
            		 
            	 }
            		
                        MainActivity.this.Velocity = -1;
                        byte[] arrayOfByte2 = new byte[4];
                        arrayOfByte2[0] = MainActivity.this.Head;
                        arrayOfByte2[1] = MainActivity.this.Move;
                        arrayOfByte2[2] = MainActivity.this.Back;
                        arrayOfByte2[3] = MainActivity.this.Enter;
                        try
                        {
                          MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                          try
                          {
                             MainActivity.this.outStream.write(arrayOfByte2);
                          }
                          catch (IOException localIOException4)
                          {
                          }
                 //         continue;
                        
                        }
                        catch (IOException localIOException3)
                        {
                 //         break label104;
                        }
                      
             }
             case MotionEvent.ACTION_UP:
             {

                 //       return false;
                         MainActivity.this.Velocity = -1;
                         byte[] arrayOfByte2 = new byte[4];
                         arrayOfByte2[0] = MainActivity.this.Head;
                         arrayOfByte2[1] = MainActivity.this.Move;
                         arrayOfByte2[2] = MainActivity.this.Back;
                         arrayOfByte2[3] = MainActivity.this.Enter;
                         try
                         {
                           MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                           try
                           {
                              MainActivity.this.outStream.write(arrayOfByte2);
                           }
                           catch (IOException localIOException4)
                           {
                           }
                  //         continue;
                         
                         }
                         catch (IOException localIOException3)
                         {
                  //         break label104;
                         }
                       
              }
             }
             return false;
           
           }
         });
         
         
   //      this.mButtonStop.setHeight(140);
  //       this.mButtonStop.setWidth(this.BtnWidth);
         final Drawable drawableStop =mButtonStop.getBackground();

         this.mButtonStop.setOnTouchListener(new View.OnTouchListener()
         {
        	
           public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
           {
             switch (paramMotionEvent.getAction())
             {
             
             case	 MotionEvent.ACTION_DOWN:
             {

                 //         return false;
            	 if(link){
            		 mButtonSlow.setBackgroundResource(R.drawable.slow1);
           		  mButtonStop.setBackgroundResource(R.drawable.stopon);
           		  mButtonBack.setBackgroundResource(R.drawable.back);
           		  mButtonFast.setBackgroundResource(R.drawable.fast);
            	 }
                          MainActivity.this.Velocity = 0;
                          byte[] arrayOfByte2 = new byte[4];
                          arrayOfByte2[0] = MainActivity.this.Head;
                          arrayOfByte2[1] = MainActivity.this.Move;
                          arrayOfByte2[2] = MainActivity.this.Stop;
                          arrayOfByte2[3] = MainActivity.this.Enter;
                          try
                          {
                            MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                            try
                            {
                               MainActivity.this.outStream.write(arrayOfByte2);
                            }
                            catch (IOException localIOException4)
                            {
                            }
                     //       continue;
                           
                           
                          }
                          catch (IOException localIOException3)
                          {
                       //     break label104;
                          }
                        
             }
             case MotionEvent.ACTION_UP:
             {

            	

                 //         return false;
                          MainActivity.this.Velocity = 0;
                          byte[] arrayOfByte2 = new byte[4];
                          arrayOfByte2[0] = MainActivity.this.Head;
                          arrayOfByte2[1] = MainActivity.this.Move;
                          arrayOfByte2[2] = MainActivity.this.Stop;
                          arrayOfByte2[3] = MainActivity.this.Enter;
                          try
                          {
                            MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                            try
                            {
                               MainActivity.this.outStream.write(arrayOfByte2);
                            }
                            catch (IOException localIOException4)
                            {
                            }
                     //       continue;
                           
                           
                          }
                          catch (IOException localIOException3)
                          {
                       //     break label104;
                          }
                        
             }
             }
             return false;
           }
         });
      //   MainActivity.this.progressBar.setProgress(50);
      //  Handler handler=new Handler(); 
         	runnable=new Runnable() { 
            
             public void run() { 
                 // TODO Auto-generated method stub 
                 //要做的事情 
            //	 MainActivity.this.progressBar.setProgress(50);
            	 
                 try
                 {
                   MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
                   try
                   {
                     MainActivity.this.outStream.write(MainActivity.ask);
                     //这个仅仅显示上一次的电量
                     MainActivity.this.progressBar.setProgress(MainActivity.this.count);
                    // MainActivity.this.progressBar.setProgress(50);
                     MainActivity.this.textBattery.setText(String.valueOf(MainActivity.this.count));
                   //  return;
                   }
                   catch (IOException localIOException2)
                   {
            //         break label30;
                   }
                 }
                 catch (IOException localIOException1)
                 {
          //         break label17;
                 }
               
                 handler.postDelayed(this, 2000); 
             } 
         };
       

         handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
    }
    
 
    @Override
	public void onResume() {
	
		super.onResume();

		 mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	//	mySensorManager.registerListener(// 调用方法为SensorManager注册监听器
	//			mySensorEventListener, // 实现了SensorEventListener接口的监听器对象
	//				mySensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), // Sensor对象
	//			SensorManager.SENSOR_DELAY_UI // 系统传递SensorEvent事件的频度
	//				);
		
		DisplayToast("正在尝试连接智能小车，请稍后····");
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        try {

           btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {

             DisplayToast("套接字创建失败！");
        }
        DisplayToast("成功连接智能小车！正在连接套接字");
        mBluetoothAdapter.cancelDiscovery();
        try {

                btSocket.connect();
                DisplayToast("连接成功建立，数据连接打开！");
                mButtonShow.setText("连接上");
             //   mButtonShow.setBackgroundResource(R.drawable.onstop);
                link=true;
                mreadThread = new readThread();  
                mreadThread.start();  
              

        } catch (IOException e) {

                try {
                	btSocket.close();

                } catch (IOException e2) {

                        
                        DisplayToast("连接没有建立，无法关闭套接字！");
                }
        }
   }
	
    @Override
	public void onPause() {
	//	sensorManager.unregisterListener(lsn); // 取消注册监听器
		 mSensorManager.unregisterListener(this);
		 handler.removeCallbacks(runnable);
		super.onPause();
		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			btSocket.close();
		
		} catch (IOException e2) {

			DisplayToast("套接字关闭失败！");
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }



	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}



	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
//	     paramSensorEvent.values[0];
        float f = arg0.values[1];
   //     paramSensorEvent.values[2];
   //    MainActivity.this.setTitle(":" + MainActivity.getNumber + "button is" + MainActivity.this.Velocity);
  //      MainActivity.this.setTitle(":" + MainActivity.getNumber + "count is" + MainActivity.this.count);
        MainActivity.this.setTitle("小车蓝牙控制程序");
        int i = (int)f;
        int j = 100 * (int)(5.0F * f) / 80;
        if (j >= 50)
          j = 50;
        if (j <= -50)
          j = -50;
        int k = j + 50;
        MainActivity.this.seekBarPWM.setProgress(k);
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = MainActivity.this.Head;
        arrayOfByte[1] = MainActivity.this.Direction;
        arrayOfByte[2] = (byte)i;
        arrayOfByte[3] = MainActivity.this.Enter;
        try
        {
          MainActivity.this.outStream = MainActivity.this.btSocket.getOutputStream();
          try
          {
             MainActivity.this.outStream.write(arrayOfByte);
             return;
          }
          catch (IOException localIOException2)
          {
   //         break label197;
          }
        }
        catch (IOException localIOException1)
        {
  //        break label185;
        }
      }
	
	
	
	
	 private class readThread extends Thread
	  {
	    private readThread()
	    {
	    }

	    public void run()
	    {
	    	
	    	
	    	   byte[] buffer = new byte[1024];  
	           int bytes;  
	     //      InputStream mmInStream = null;  
	             
	      //     if(MainActivity.this.btSocket!=null)
	           {
	        	   try {
	                	mmInStream = btSocket.getInputStream();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}  
	           }
	      //  } catch (IOException e1) {  
	            // TODO Auto-generated catch block  
	      //      e1.printStackTrace();  
	      //  }     
	            while (true) {  
	               try {  
	                   // Read from the InputStream  
	                   if( (bytes = mmInStream.read(buffer)) > 0 )  //返回长度
	                   {  
	                    byte[] buf_data = new byte[bytes];  
	                    for(int i=0; i<bytes; i++)  
	                    {  
	                        buf_data[i] = buffer[i];  
	                    }  
	                    getNumber = null;
	                    getNumber = buf_data;
	                    if(bytes>=3&&(int)buf_data[2]>=0)
	                    {
	                    	count=(int)buf_data[2];
	                    	
	                    }
	                   }  
	               } catch (IOException e) {  
	                try {  
	                	
	                    mmInStream.close();  
	                } catch (IOException e1) {  
	                    // TODO Auto-generated catch block  
	                    e1.printStackTrace();  
	                }  
	                   break;  
	               }  
	           } 
	    
	    }
	  }

		
	}


	// public void onAccuracyChanged(Sensor sensor, int accuracy) {
		 
    // }
    
	//public void onSensorChanged(SensorEvent event) {
		
  //    }
	


    

