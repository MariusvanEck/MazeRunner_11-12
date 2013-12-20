package gamestate;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

   private AudioClip clip;
   private Thread soundThread;
   
   /** 
    * create a new sound
    */
   	public Sound(String name) {
   		try {
   			URL url = new URL("file:sound/" + name);
   			clip = Applet.newAudioClip(url);} 
   		catch (Throwable e){e.printStackTrace();}
   	}
   
   /** 
    * play the sound
    */
   	public void play() {
   		try {
   			soundThread = 	new Thread(){
        	 				public void run(){clip.play();}
           					};
         
           	soundThread.start();}
   		catch(Throwable e){e.printStackTrace();}
   	}
   
   /**
    * loop the sound
    */
   	public void loop() {
	      try{
	         soundThread = 	new Thread(){
	        	 			public void run(){clip.loop();}
	         				};
	         				
	         soundThread.start();}
	      catch(Throwable e){e.printStackTrace();}
   }
   	
   	/**
   	 * stop the sound;
   	 */
   	public void stop() {
	      try{
	         soundThread = 	new Thread(){
	        	 			public void run(){clip.stop();}
	         				};
	         				
	         soundThread.start();}
	      catch(Throwable e){e.printStackTrace();}
 }
}


