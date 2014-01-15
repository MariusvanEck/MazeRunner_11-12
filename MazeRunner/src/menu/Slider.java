package menu;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;

public class Slider extends MenuObject{
	
	private float fraction;
	private Texture[] sliderTextures;
	private Texture nameTexture;
	
	public Slider(int minX, int maxX, int minY, int maxY, Texture[] sliderTextures, Texture nameTexture) {
		super(minX, maxX, minY, maxY);
		
		this.nameTexture = nameTexture;
		this.sliderTextures = sliderTextures;
		fraction = 1f;
	}

	@Override
	public void display(GL gl) {
		
		if (sliderTextures[0] != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			sliderTextures[0].enable();
			sliderTextures[0].bind();
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
			sliderTextures[0].disable();}
			
		if (sliderTextures[1] != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			sliderTextures[1].enable();
			sliderTextures[1].bind();
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
			sliderTextures[1].disable();}
		
		if (nameTexture != null){
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			nameTexture.enable();
			nameTexture.bind();
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
			nameTexture.disable();}
		
		
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
	
	public float getFraction() {
		return fraction;
	}
}
