package gamestate;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

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
   	
   	/**
   	 * set the volume for all sounds
   	 */
   	public static void setVolume(float volume) {
   		
   		Mixer.Info[] infos = AudioSystem.getMixerInfo();
   		for (Mixer.Info info: infos) {
   		    Mixer mixer = AudioSystem.getMixer(info);
   		    Line[] lines = mixer.getSourceLines();
   		    
   		    for(Line line : lines) {
   		    	
   		    	if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
	   		    	FloatControl fc = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
	    			if (fc != null) {
	    			    fc.setValue(-20 + volume*(20));
	    			}}
   		    }
   		}
   	}
   	
   	/**
   	 * mute/unmute sound;
   	 */
   	public static void setMute(boolean mute) {
   		
   		Mixer.Info[] infos = AudioSystem.getMixerInfo();
   		for (Mixer.Info info: infos) {
   		    Mixer mixer = AudioSystem.getMixer(info);
   		    Line[] lines = mixer.getSourceLines();
   		    
   		    for(Line line : lines) {
   		    	BooleanControl bc = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
    			if (bc != null) {
    			    bc.setValue(mute); // true to mute the line, false to unmute
    			}
   		    }
   		}
   	}
}


