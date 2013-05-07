package graphics;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Random;
import cell.*;


public class DisplayFarm implements GLEventListener
{

    private int DRAW_WIDTH  = 200;
	private int DRAW_HEIGHT = 200;

	// Recommended to initialize zoomOut the same as draw width, height and 
	// depth averaged together.
	private static int zoomOut = 150;

	// the Camera will always look at (0, 0, 0) but it can rotate around that
	// point. We use the mouse coordinates to adjust rotation.
	private static int vert_rotate = 0;
	private static int hor_rotate = 0;
	private static int mouseX = 0;
	private static int mouseY = 0;

	// The light's rotation
	private static int lightRotateX = 0;
	private static int lightRotateY = 0;

	// The distance of the light from the origin
	private static double lightDistance = 100;

	private static int centerX = 0;
	private static int centerY = 0;

	private GLU glu = new GLU();

	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	private int minZ;
	private int maxZ;

	private Farm farm;
	private JPanel panel;

	private Cell[][][] grid;

	public DisplayFarm(Farm farm, JPanel panel) 
	{
		this.farm = farm;
		this.panel = panel;
		this.grid = farm.getGrid();
		
		this.panel.setSize(500, 500);
		
		this.DRAW_HEIGHT = this.panel.getHeight();
		this.DRAW_WIDTH = this.panel.getWidth();

		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);    

		this.panel.setLayout(new BorderLayout());
		this.panel.add(canvas, BorderLayout.CENTER);

		System.out.println("Directions:");
		System.out.println("  -Rotate: click and drag the mouse in the window.");
		System.out.println("  -Zoom: use the mouse wheel to control zoom."); 

		canvas.addGLEventListener(this);

		Animator animator = new Animator(canvas);
		animator.add(canvas);
		animator.start();

		// So that the user does not have to click before controlling
		// camera, lighting, etc.
		canvas.requestFocus();


		// Mouse wheel zoom
		canvas.addMouseWheelListener(new MouseWheelListener() 
		{
			public void mouseWheelMoved(MouseWheelEvent e) 
			{
				int rotation = e.getWheelRotation();
				if (rotation < 0) zoomOut -= 10;
				else  zoomOut += 10;
			} // mouseWheelMoved
		}); // MouseWheelListener


		// Mouse click and drag for camera movement
		canvas.addMouseListener( new MouseListener()
		{
			public void mousePressed(MouseEvent e) 
			{
				//
				// We only care about the mouse coordinates when the mouse is
				// clicked. This and the mouseDragged event are the only two
				// places where mouseX and mouseY are updated. This event
				// only happens once when the mouse button is pressed down.
				//
				mouseX = e.getX();
				mouseY = e.getY();
			} // mousePressed
			// These methods are unneeded for this program.
			public void mouseClicked  (MouseEvent e) {}
			public void mouseEntered  (MouseEvent e) {}
			public void mouseExited   (MouseEvent e) {}
			public void mouseReleased (MouseEvent e) {}
		}); // addMouseListener

		canvas.addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				if ((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0) 
				{
					centerX = (mouseX - e.getX())/10;
					centerY = (e.getY() - mouseY)/10;
					return;
				}

				// Integers mx and my are used as the current mouse X and Y whereas
				// the mouseX and mouseY integers are treated as the mouse's previous
				// X and Y coordinates.
				int mx = e.getX();
				int my = e.getY();

				// Rotation around the x-axis as the mouse moves up/down
				vert_rotate += (mouseY - my);
				vert_rotate = vert_rotate % 360; // We don't need rotation above 360

				// If vert_rotate is negative, wrap it around from 360
				if (vert_rotate < 0) vert_rotate = 360 + vert_rotate;


				// Rotation around the y-axis as the mouse moves right/left
				hor_rotate  += (mouseX - mx);
				hor_rotate = hor_rotate % 360;   // We don't need rotation above 360

				// If hor_rotate is negative, wrap it around from 360
				if (hor_rotate < 0) hor_rotate = 360 + hor_rotate;

				// Set the MouseY and MouseY values to the current mouse coordinates. 
				mouseX = e.getX();
				mouseY = e.getY();

			} // mouseDragged
			public void mouseMoved(MouseEvent arg0) {}
		}); // MouseMotionListener




	} 


	/**
	 * Synopsis: the controller of the program. It calls the update method
	 * and the draw method.
	 * @param GLAutoDrawable the drawable area of the program
	 */
	public void display(GLAutoDrawable drawable)
	{
		draw(drawable);
	} // display


	/**
	 * Unused method required by GLEventListener
	 */
	public void dispose(GLAutoDrawable drawable){}


	/**
	 * Synopsis: this initializes the GL2. Sets some important flags and values
	 * necessary for the program to run. This also initialized the ArrayList
	 * particles.
	 * @param drawable the drawable area of the program
	 */
	public void init(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();

		//
		// Enable v-sync. This is not guaranteed, some platforms/cards will 
		// ignore this.
		//
		gl.setSwapInterval(1); // 1 to enable, 0 to disable.

		//
		// Enable culling so as to not draw the inside of the cube.
		//
		gl.glEnable(GL2.GL_CULL_FACE);

		//
		// Enable lighting and use glColor instead of glMaterial for surface
		// coloring.
		//
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT);
		gl.glEnable (GL2.GL_COLOR_MATERIAL);

		//
		// Lighting properties
		// 
		// Ambient light gives each cube it's glow. Diffuse light gives each 
		// side of the cube a different shade.
		//
		float[] lightpos0 = {  10,  20,  30, 1  }; // light position
		float[] ambient0  = { .3f, .3f, .3f, 1f }; // moderate ambient light
		float[] diffuse0  = { .1f, .1f, .1f, 1f }; // low diffuse light


		float[] lightpos1 = { 
				(float) lightDistance,  
				(float) lightDistance,  
				(float) lightDistance, 
				1  }; // light position
		float[] ambient1  = { .0f, .0f, .0f, 1f }; // no ambient light
		float[] diffuse1  = { .5f, .5f, .5f, 1f }; // low diffuse light

		//
		// Set Lighting properties for light 0
		//
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  ambient0,  0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  diffuse0,  0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightpos0, 0);

		//
		// Set Lighting properties for light 1
		//
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT,  ambient1,  0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE,  diffuse1,  0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightpos1, 0);

		//
		// Enable Alpha Transparency.
		//
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		gl.glClearColor(0,0,0,0);

		//
		// Enable depth testing.
		//
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);




	} // init


	/**
	 * Unused method required by GLEventListener
	 */
	public void reshape(
			GLAutoDrawable drawable, 
			int x, 
			int y, 
			int width, 
			int height){}

	/**
	 * Synopsis: sets the camera location and perspective.
	 * @param gl the instance of GL2 you created
	 * @param glu the instance of GLU you created
	 * @param distance the distance the camera needs to be from the origin
	 *        (try zoomOut)
	 */
	private void setCamera(GL2 gl, GLU glu, float distance) 
	{
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) DRAW_WIDTH / (float) DRAW_HEIGHT;
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(0, 0, distance, centerX, centerY, 0, 0, 1, 0);
		gl.glRotatef(vert_rotate, -1.f, 0.f, 0.f);
		gl.glRotatef(hor_rotate, 0.f, -1.f, 0.f);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	} // setCamera



	private void doLighting( GL2 gl )
	{
		float lightPos[] = new float[4];

		lightPos[0] = (float) lightDistance*1;
		lightPos[1] = (float) lightDistance*1;
		lightPos[2] = (float) lightDistance*1;
		lightPos[3] = 1;
		gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_LIGHT0);
		float[] noAmbient ={ 0.1f, 0.1f, 0.1f, 1f }; // low ambient light
		float[] spec =    { 1f, 0.6f, 0f, 1f }; // low ambient light
		float[] diffuse ={ 1f, 1f, 1f, 1f };
		// properties of the light
		gl.glLightfv( gl.GL_LIGHT0, gl.GL_POSITION, lightPos, 0 );
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, noAmbient, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, spec, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diffuse, 0);

		float lightPos2[] = new float[4];

		lightPos2[0] = (float) lightDistance*-1;
		lightPos2[1] = (float) lightDistance*-1;
		lightPos2[2] = (float) lightDistance*-1;
		lightPos2[3] = 1;
		gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_LIGHT1);

		gl.glLightfv( gl.GL_LIGHT1, gl.GL_POSITION, lightPos2, 0 );
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_AMBIENT, noAmbient, 0);
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_SPECULAR, spec, 0);
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE, diffuse, 0);

	}

	public void setRange(int minX, int maxX, int minY, int maxY, int minZ, int maxZ)
	{
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

	private void makeBack(GL2 gl, double cell_width_height, double cell_depth)
	{
		//back
		for(int x = this.minX; x <= this.maxX; x++)
		{
			for(int y = this.minY; y <= this.maxY; y++)
			{
				int z = this.minZ;

				this.makeCube(gl, x, y, z, cell_width_height, cell_depth);
			}
		}
	}

	private void makeFront(GL2 gl, double cell_width_height, double cell_depth)
	{
		//front
		for(int x = this.minX; x <= this.maxX; x++)
		{
			for(int y = this.minY; y <= this.maxY; y++)
			{
				int z = this.maxZ;

				this.makeCube(gl, x, y, z, cell_width_height, cell_depth);
			}
		}
	}

	private void makeLeft(GL2 gl, double cell_width_height, double cell_depth)
	{
		//left
		for(int y = this.minY; y <= this.maxY; y++)
		{
			for(int z = this.minZ; z <= this.maxZ; z++)
			{
				int x = this.minX;

				this.makeCube(gl, x, y, z, cell_width_height, cell_depth);
			}
		}
	}

	private void makeRight(GL2 gl, double cell_width_height, double cell_depth)
	{
		//right
		for(int y = this.minY; y <= this.maxY; y++)
		{
			for(int z = this.minZ; z <= this.maxZ; z++)
			{
				int x = this.maxX;

				this.makeCube(gl, x, y, z, cell_width_height, cell_depth);
			}
		}
	}

	private void makeTop(GL2 gl, double cell_width_height, double cell_depth)
	{
		//bottom
		for(int x = this.minX; x <= this.maxX; x++)
		{
			for(int z = this.minZ; z <= this.maxZ; z++)
			{
				int y = this.maxY;

				this.makeCube(gl, x, y, z, cell_width_height, cell_depth);
			}
		}

	}

	private void makeBottom(GL2 gl, double cell_width_height, double cell_depth)
	{
		//top
		for(int x = this.minX; x <= this.maxX; x++)
		{
			for(int z = this.minZ; z <= this.maxZ; z++)
			{
				int y = this.minY;

				this.makeCube(gl, x, y, z, cell_width_height, cell_depth);
			}
		}
	}

	void interpolateColor(GL2 gl, int x, int y, int z, double r0, double g0, double b0, double r1, double g1, double b1)
	{
		float speed = (float) .0001; // how fast to move the pattern
		float spacing = (float) 25; // how wide to make the pattern
		double tick = System.currentTimeMillis();
		double c = Math.sin((x+y+z+tick*speed)*spacing)*.5+.5;
		double r = (r1-r0)*c+r0;
		double g = (g1-g0)*c+g0;
		double b = (b1-b0)*c+b0;
		gl.glColor3d(r,g,b);
	}

	private Color getColor(Cell cell)
	{
		if(cell.getPlant() != null)
		{
			Cell p = cell;
			if(p.getPlant() == Plant.PINTOBEANS)
			{
				return new Color(0xCC, 0, 0); //dark red
			}
			else if(p.getPlant() == Plant.SUNFLOWER)
			{
				return new Color(0xFF, 0x66, 0); //dark orange
			}
			else if(p.getPlant() == Plant.AMARANTH)
			{
				return new Color(0x99, 0, 0); //maroon
			}
			else if(p.getPlant() == Plant.CHILE)
			{
				return new Color(0xFF, 0, 0); //red
			}
			else if(p.getPlant() == Plant.SWEETCORN)
			{
				return new Color(0xFF, 0xFF, 0); //yellow
			}
			else if(p.getPlant() == Plant.SUMMERSQUASH)
			{
				return new Color(0x66, 0xFF, 0x33); //light green
			}
			else if(p.getPlant() == Plant.WINTERSQUASH)
			{
				return new Color(0xFF, 0xCC, 0x33); //light orange
			}
			else if(p.getPlant() == Plant.POTATOES)
			{
				return new Color(0x33, 0x99, 0); //dark green
			}
			else if(p.getPlant() == Plant.SWEETPEPPER)
			{
				return new Color(0xCC, 0xFF, 0); //lime yellow
			}
		}
		else
		{
			if(cell.getSoil() == Soil.AIR)
			{
				return new Color(0, 0, 0); //black
			}
			else if(cell.getSoil() == Soil.GILACLAY)
			{
				//return new Color(0x99, 0x33, 0x33); //dark clay
				return new Color(255,0,0);
			}
			else if(cell.getSoil() == Soil.GILACLAYLOAM)
			{
				return new Color(0xCC, 0x66, 0x33); //light clay
			}
			else if(cell.getSoil() == Soil.GILAFINESANDYLOAM)
			{
				return new Color(0xCC, 0x99, 0); //light brown
			}
			else if(cell.getSoil() == Soil.GILALOAM)
			{
				return new Color(0x99, 0x99, 0x99); //gray
			}
			else if(cell.getSoil() == Soil.GILASAND)
			{
				return new Color(0x99, 0x33, 0); //dark brown
			}
			else if(cell.getSoil() == Soil.RIVERWASH)
			{
				return new Color(0, 0, 0); //black
			}
		}

		return null;
	}

	private void makeCube(GL2 gl, int x, int y, int z, double cell_width_height, double cell_depth)
	{

		Double blueness = this.grid[x][y][z].getWaterVolume()/this.grid[x][y][z].getSoil().getWaterCapacity();

		Color cellColor = this.getColor(this.grid[x][y][z]);

		interpolateColor(gl, x, y, z, cellColor.red, cellColor.green, cellColor.blue, 0, 0, blueness);

		gl.glLoadIdentity();
		gl.glBegin(GL2.GL_QUADS); // front
		gl.glNormal3d(0, 0, 1); // Used for lighting FX
		gl.glVertex3d(x-cell_width_height, y-cell_width_height, z+cell_depth);
		gl.glVertex3d(x+cell_width_height, y-cell_width_height, z+cell_depth);
		gl.glVertex3d(x+cell_width_height, y+cell_width_height, z+cell_depth);
		gl.glVertex3d(x-cell_width_height, y+cell_width_height, z+cell_depth);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // back
		gl.glNormal3d(0, 0, -1); // Used for lighting FX
		gl.glVertex3d(x-cell_width_height, y+cell_width_height, z-cell_depth);
		gl.glVertex3d(x+cell_width_height, y+cell_width_height, z-cell_depth);
		gl.glVertex3d(x+cell_width_height, y-cell_width_height, z-cell_depth);
		gl.glVertex3d(x-cell_width_height, y-cell_width_height, z-cell_depth);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // left
		gl.glNormal3d(-1, 0, 0); // Used for lighting FX
		gl.glVertex3d(x-cell_width_height, y-cell_width_height, z-cell_depth);
		gl.glVertex3d(x-cell_width_height, y-cell_width_height, z+cell_depth);
		gl.glVertex3d(x-cell_width_height, y+cell_width_height, z+cell_depth);
		gl.glVertex3d(x-cell_width_height, y+cell_width_height, z-cell_depth);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // right
		gl.glNormal3d(+1, 0, 0); // Used for lighting FX
		gl.glVertex3d(x+cell_width_height, y+cell_width_height, z-cell_depth);
		gl.glVertex3d(x+cell_width_height, y+cell_width_height, z+cell_depth);
		gl.glVertex3d(x+cell_width_height, y-cell_width_height, z+cell_depth);
		gl.glVertex3d(x+cell_width_height, y-cell_width_height, z-cell_depth);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // top
		gl.glNormal3d(0, 1, 0); // Used for lighting FX
		gl.glVertex3d(x-cell_width_height, y+cell_width_height, z+cell_depth);
		gl.glVertex3d(x+cell_width_height, y+cell_width_height, z+cell_depth);
		gl.glVertex3d(x+cell_width_height, y+cell_width_height, z-cell_depth);
		gl.glVertex3d(x-cell_width_height, y+cell_width_height, z-cell_depth);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // bottom
		gl.glNormal3d(0, -1, 0); // Used for lighting FX
		gl.glVertex3d(x-cell_width_height, y-cell_width_height, z-cell_depth);
		gl.glVertex3d(x+cell_width_height, y-cell_width_height, z-cell_depth);
		gl.glVertex3d(x+cell_width_height, y-cell_width_height, z+cell_depth);
		gl.glVertex3d(x-cell_width_height, y-cell_width_height, z+cell_depth);
		gl.glEnd();

	}


	private void draw(GLAutoDrawable drawable) 
	{
		double cell_width_height = 1;
		double cell_depth = 1;


		GL2 gl = drawable.getGL().getGL2();


		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		int count = 0;


		if((this.vert_rotate >= 0) && (this.vert_rotate < 180))
		{
			this.makeBottom(gl, cell_width_height, cell_depth);
			count++;
		}
		if((this.vert_rotate >= 180) && (this.vert_rotate < 360))
		{
			this.makeTop(gl, cell_width_height, cell_depth);
			count++;
		}

		if((this.vert_rotate >= 90) && (this.vert_rotate < 270))
		{
			if(((this.hor_rotate >= 270) && (this.hor_rotate < 360)) || ((this.hor_rotate >= 0) && (this.hor_rotate < 90)))
			{
				this.makeBack(gl, cell_width_height, cell_depth);
				count++;
			}
			if(((this.hor_rotate >= 0) && (this.hor_rotate < 90)) || ((this.hor_rotate >= 90) && (this.hor_rotate < 180)))
			{
				this.makeLeft(gl, cell_width_height, cell_depth);
				count++;
			}
			if(((this.hor_rotate >= 90) && (this.hor_rotate < 180)) || ((this.hor_rotate >= 180) && (this.hor_rotate < 270)))
			{
				this.makeFront(gl, cell_width_height, cell_depth);
				count++;
			}
			if(((this.hor_rotate >= 180) && (this.hor_rotate < 270)) || ((this.hor_rotate >= 270) && (this.hor_rotate < 360)))
			{
				this.makeRight(gl, cell_width_height, cell_depth);
				count++;
			}
		}
		else
		{

			if(((this.hor_rotate >= 270) && (this.hor_rotate < 360)) || ((this.hor_rotate >= 0) && (this.hor_rotate < 90)))
			{
				this.makeFront(gl, cell_width_height, cell_depth);
				count++;
			}
			if(((this.hor_rotate >= 0) && (this.hor_rotate < 90)) || ((this.hor_rotate >= 90) && (this.hor_rotate < 180)))
			{
				this.makeRight(gl, cell_width_height, cell_depth);
				count++;
			}
			if(((this.hor_rotate >= 90) && (this.hor_rotate < 180)) || ((this.hor_rotate >= 180) && (this.hor_rotate < 270)))
			{
				this.makeBack(gl, cell_width_height, cell_depth);
				count++;
			}
			if(((this.hor_rotate >= 180) && (this.hor_rotate < 270)) || ((this.hor_rotate >= 270) && (this.hor_rotate < 360)))
			{
				this.makeLeft(gl, cell_width_height, cell_depth);
				count++;
			}
		}


		setCamera(gl, glu, zoomOut);

		doLighting(gl);

		//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

		//System.out.println("Vertical Rotation: " + this.vert_rotate + " Horizontal Rotation: " + this.hor_rotate + " Count = " + count);

		gl.glFlush();

	} 

	public static void main(String[] args)
	{
		
	}
} 
