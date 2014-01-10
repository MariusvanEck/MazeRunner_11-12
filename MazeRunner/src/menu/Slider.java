package menu;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;

public class Slider extends MenuObject{
	
	private float fraction;
	private Texture[] textures;
	
	public Slider(int minX, int maxX, int minY, int maxY, Texture[] textures) {
		super(minX, maxX, minY, maxY);
		
		this.textures = textures;
		fraction = 1f;
	}

	@Override
	public void display(GL gl) {
		
		if (textures[0] != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			textures[0].enable();
			textures[0].bind();
			//White background color for normal texture view
			gl.glColor3f(255/255f, 255/255f, 255/255f);
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0,0);
				gl.glVertex2f(minX, minY + (maxY-minY)/2);
				gl.glTexCoord2f(1,0);
				gl.glVertex2f(maxX, minY + (maxY-minY)/2);
				gl.glTexCoord2f(1,1);
				gl.glVertex2f(maxX, minY);
				gl.glTexCoord2f(0,1);
				gl.glVertex2f(minX, minY);
			gl.glEnd();
			textures[0].disable();}
			
		if (textures[1] != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			textures[1].enable();
			textures[1].bind();
			//White background color for normal texture view
			gl.glColor3f(255/255f, 255/255f, 255/255f);
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0,0);
				gl.glVertex2f(minX, minY + (maxY-minY)/2);
				gl.glTexCoord2f(1,0);
				gl.glVertex2f(minX + fraction*(maxX - minX), minY + (maxY-minY)/2);
				gl.glTexCoord2f(1,1);
				gl.glVertex2f(minX + fraction*(maxX - minX), minY);
				gl.glTexCoord2f(0,1);
				gl.glVertex2f(minX, minY);
			gl.glEnd();
			textures[1].disable();}
		
		if (textures[2] != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			textures[2].enable();
			textures[2].bind();
			//White background color for normal texture view
			gl.glColor3f(255/255f, 255/255f, 255/255f);
			gl.glBegin(GL.GL_QUADS);
				gl.glTexCoord2f(0,0);
				gl.glVertex2f(minX, maxY);
				gl.glTexCoord2f(1,0);
				gl.glVertex2f(maxX, maxY);
				gl.glTexCoord2f(1,1);
				gl.glVertex2f(maxX, minY + (maxY-minY)/2);
				gl.glTexCoord2f(0,1);
				gl.glVertex2f(minX, minY + (maxY-minY)/2);
			gl.glEnd();
			textures[2].disable();}
		
		
	}
	
	public void update(int minX,int maxX,int minY, int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public void setFraction(float fraction) {
		this.fraction = fraction;
	}
}
