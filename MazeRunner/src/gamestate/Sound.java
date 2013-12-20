package gamestate;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

   private AudioClip clip;
   
   public Sound(String name){
      try
      {
    	 URL url = new URL("file:sound/" + name);
         clip = Applet.newAudioClip(url);
      }catch (Throwable e){
         e.printStackTrace();
      }
   }
   
   public void play(){
      try{
         new Thread(){
            public void run(){
            	clip.play();
            }
         }.start();
      }catch(Throwable e){
         e.printStackTrace();
      }
   }
}


