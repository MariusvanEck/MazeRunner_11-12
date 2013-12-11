package model;

import gamestate.GameStateManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;



public class ShaderDemo {
	
	GL gl =  GameStateManager.getGl();
	
	public void Demo(){
		
		int shaderProgram = gl.glCreateProgram();
		int vertexShader = gl.glCreateShader(GL.GL_VERTEX_SHADER);
		int fragmentShader = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
		
		String vertexShaderSource = new String();
		String fragmentShaderSource = new String();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("src/model/shader.vert"));
			String line;
			
			while((line = reader.readLine()) != null){
				vertexShaderSource += line;
				vertexShaderSource += "\n";
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Vertex shader wassn't loaded properly.");
			return;
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("src/model/shader.frag"));
			String line;
			
			while((line = reader.readLine()) != null){
				fragmentShaderSource += line;
				fragmentShaderSource += "\n";
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Fragment shader wassn't loaded properly.");
			return;
		}
		IntBuffer temp = IntBuffer.allocate(1);
		gl.glShaderSource(vertexShader, 1, new String[]{vertexShaderSource},null);
		gl.glCompileShader(vertexShader);
		//TODO: kijken of dit ook daadwerkelijk een error geeft als het niet kan compilen
		gl.glGetShaderiv(vertexShader, GL.GL_COMPILE_STATUS, temp);
		if(temp.get(0) == GL.GL_FALSE)
			System.err.println("Vertex shader wasn't able to be compiled corectly.");
		
		gl.glShaderSource(fragmentShader,1,new String[]{fragmentShaderSource},null);
		gl.glCompileShader(fragmentShader);
		//TODO: kijken of dit ook daadwerkelijk een error geeft als het niet kan compilen
		gl.glGetShaderiv(vertexShader, GL.GL_COMPILE_STATUS, temp);
		if(temp.get(0) == GL.GL_FALSE)
			System.err.println("Fragment shader wasn't able to be compiled corectly.");
		
		gl.glAttachShader(shaderProgram, vertexShader);
		gl.glAttachShader(shaderProgram, fragmentShader);
		
		gl.glLinkProgram(shaderProgram);
		gl.glValidateProgram(shaderProgram);
		
		
		gl.glUseProgram(shaderProgram);
		
		
		gl.glDeleteProgram(shaderProgram);
		gl.glDeleteShader(vertexShader);
		gl.glDeleteShader(fragmentShader);
		
	}
	
//	private String[] asStrings(StringBuilder builder){
//		return builder.toString().split("\n");
//	}
}
