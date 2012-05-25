package sound;


/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;


public class MP3  implements Runnable {
    private String filename;
    private Player player;
    private boolean loop;

    // constructor that takes the name of an MP3 file
    public MP3(String filename, boolean loop) {
        this.filename = filename;
        this.loop = loop;
    }

    public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play() {
    		
    	while (loop)
    	{
	        try {
	            FileInputStream fis     = new FileInputStream(filename);
	            BufferedInputStream bis = new BufferedInputStream(fis);
	            player = new Player(bis);
	        }
	        catch (Exception e) {
	            System.out.println("Problem playing file " + filename);
	            System.out.println(e);
	        }
	
	        // run in new thread to play in background
	        Thread t = new Thread() {
	            public void run() {
	            	
	            		try { player.play(); }
	            		catch (Exception e) { System.out.println(e); }
	            	
	            }
	        };
	        t.start();
	        try {
				t.join();
				t=null;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
    	}
	        
	        
	        	
    }
    
    @Override
    public void run(){
    	play();
    }


    // test client
//    public static void main(String[] args) {
//        String filename = args[0];
//        MP3 mp3 = new MP3(filename);
//        mp3.play();
//
//        // do whatever computation you like, while music plays
//        int N = 4000;
//        double sum = 0.0;
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                sum += Math.sin(i + j);
//            }
//        }
//        System.out.println(sum);
//
//        // when the computation is done, stop playing it
//        mp3.close();
//
//        // play from the beginning
//        mp3 = new MP3(filename);
//        mp3.play();
//
//    }

}