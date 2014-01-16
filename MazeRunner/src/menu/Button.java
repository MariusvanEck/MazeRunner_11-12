package menu;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Button extends MenuObject{
	private Texture texture[];
	
	/**
	 * Button constructor
	 * @param text The text on the button
	 * @param gl The GL to draw to
	 * @param minX The left x
	 * @param maxX the right x
	 * @param minY the bottom y
	 * @param maxY the top y
	 */
	public Button(int minX,int maxX,int minY,int maxY, Texture texture){
		super(minX,maxX,minY,maxY);
		this.texture = new Texture[1];
		this.texture[0] = texture;
	}
	
	public Button(int minX,int maxX,int minY,int maxY,String name){
		super(minX,maxX,minY,maxY);
		
		this.texture = new Texture[name.length()];
		try{
			for(int i = 0; i < name.length();i++){
				switch(name.charAt(i)){
					case 'a': case 'A':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/a.png"), false);
						break;
					case 'b': case 'B':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/b.png"), false);
						break;
					case 'c': case 'C':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/c.png"), false);
						break;
					case 'd': case 'D':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/d.png"), false);
						break;
					case 'e': case 'E':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/e.png"), false);
						break;
					case 'f': case 'F':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/f.png"), false);
						break;
					case 'g': case 'G':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/g.png"), false);
						break;
					case 'h': case 'H':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/h.png"), false);
						break;
					case 'i': case 'I':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/i.png"), false);
						break;
					case 'j': case 'J':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/j.png"), false);
						break;
					case 'k': case 'K':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/k.png"), false);
						break;
					case 'l': case 'L':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/l.png"), false);
						break;
					case 'm': case 'M':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/m.png"), false);
						break;
					case 'n': case 'N':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/n.png"), false);
						break;
					case 'o': case 'O':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/o.png"), false);
						break;
					case 'p': case 'P':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/p.png"), false);
						break;
					case 'q': case 'Q':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/q.png"), false);
						break;
					case 'r': case 'R':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/r.png"), false);
						break;
					case 's': case 'S':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/s.png"), false);
						break;
					case 't': case 'T':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/t.png"), false);
						break;
					case 'u': case 'U':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/u.png"), false);
						break;
					case 'v': case 'V':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/v.png"), false);
						break;
					case 'w': case 'W':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/w.png"), false);
						break;
					case 'x': case 'X':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/x.png"), false);
						break;
					case 'y': case 'Y':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/y.png"), false);
						break;
					case 'z': case 'Z':
						texture[i] = TextureIO.newTexture(new File("textures/Alfabet/z.png"), false);
						break;
				}
			}
		} catch (IOException e) {
			System.err.println("Button: Loading alfabet failed!\n\t" + e.getMessage());
		}
		
	}
	
	
	/**
	 * Draw the button
	 */
	public void display(GL gl){
		if(texture.length == 1){
				if(texture[0] != null){
					gl.glEnable(GL.GL_BLEND);
					gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
					texture[0].enable();
					texture[0].bind();
					//White background color for normal texture view
					gl.glColor3f(255/255f, 255/255f, 255/255f);
					if(!selected){
						gl.glColor3f(128/255f, 128/255f, 128/255f);
					}
					gl.glBegin(GL.GL_QUADS);
						gl.glTexCoord2f(0,0);
						gl.glVertex2f(minX, maxY);
						gl.glTexCoord2f(1,0);
						gl.glVertex2f(maxX, maxY);
						gl.glTexCoord2f(1,1);
						gl.glVertex2f(maxX, minY);
						gl.glTexCoord2f(0,1);
						gl.glVertex2f(minX, minY);
					gl.glEnd();
					texture[0].disable();
					gl.glColor3f(255/255f, 255/255f, 255/255f);
				}
				return;
		}
		for(int i = 0; i < texture.length && i < 8; i++){
			if (texture[i] != null){
				gl.glEnable(GL.GL_BLEND);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				texture[i].enable();
				texture[i].bind();
				//White background color for normal texture view
				gl.glColor3f(255/255f, 255/255f, 255/255f);
				if(!selected){
					gl.glColor3f(128/255f, 128/255f, 128/255f);
				}
				int sizeX = (maxX-minX)/8;
				gl.glBegin(GL.GL_QUADS);
					gl.glTexCoord2f(0,0);
					gl.glVertex2f(minX+sizeX*i, maxY);
					gl.glTexCoord2f(1,0);
					gl.glVertex2f(minX+sizeX*(i+1), maxY);
					gl.glTexCoord2f(1,1);
					gl.glVertex2f(minX+sizeX*(i+1), minY);
					gl.glTexCoord2f(0,1);
					gl.glVertex2f(minX+sizeX*i, minY);
				gl.glEnd();
				texture[i].disable();
				gl.glColor3f(255/255f, 255/255f, 255/255f);
			}
		}
	}
	public void update(int minX,int maxX,int minY, int maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
}
