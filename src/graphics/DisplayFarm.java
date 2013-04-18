package graphics;

import cell.*;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JPanel;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;




public class DisplayFarm implements GLEventListener

{
    private static final int DRAW_WIDTH  = 200;
    private static final int DRAW_HEIGHT = 200;

    //  private static final int WINDOW_WIDTH  = 500;
    //  private static final int WINDOW_HEIGHT = 500;

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
    private static double lightDistance = 5;

    private GLU glu = new GLU();
    
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;
    
    private static int num_of_soil_types = 7;
    File[] fileList = new File[num_of_soil_types]; 
    private Texture[] textureList = new Texture[num_of_soil_types];

    private Farm farm;
    //private JPanel panel;
    
    private Cell[][][] grid;
    
    private static int centerX = 0;
    private static int centerY = 0;

    public DisplayFarm(Farm farm, JPanel panel)
    {
        this.farm = farm;
        //this.panel = panel;
        this.grid = farm.getGrid();

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);  
        
        GLJPanel glPanel = new GLJPanel();
        glPanel.add(canvas);

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
    
    public void setRange(int minX, int maxX, int minY, int maxY, int minZ, int maxZ)
    {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
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
    
    private GLAutoDrawable globalDrawable;
    
    public void updateFarm()
    {
        display(this.globalDrawable);
    }


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
        
              
        this.fileList[0] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/air_texture.png");
        this.fileList[1] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/clay_texture.png");
        this.fileList[2] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/clayloam_texture.png");
        this.fileList[3] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/loam_texture.png");
        this.fileList[4] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/riverwash_texture.png");
        this.fileList[5] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/sand_texture.png");
        this.fileList[6] = new File("/home/vivek/workspace/JOGL Test 2.0/res/textures/sandloam_texture.png");
        
        try
        {
            for(int i = 0; i < DisplayFarm.num_of_soil_types; i++)
            {
                textureList[i] = TextureIO.newTexture(this.fileList[i],true);
            }
        }
        catch (GLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    private void setMobileLight(GL2 gl, GLU glu)
    {

        float[] lightpos1 = { 
                (float) lightDistance,  
                (float) lightDistance,  
                (float) lightDistance, 
                1  }; // light position

        GLUquadric light = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(light, GLU.GLU_FILL);
        glu.gluQuadricNormals(light, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(light, GLU.GLU_OUTSIDE);
        final int slices = 32;
        final int stacks = 32;

        //
        // Set Lighting position for light 1
        //
        gl.glPushMatrix();
        gl.glTranslated(0, 0, 0);
        gl.glPushMatrix();
        // Makes for some fun rotation.
        gl.glRotatef(lightRotateX, 1.f, 0.f, 0.f);
        gl.glRotatef(lightRotateX, 0.f, 0.f, 1.f);
        gl.glRotatef(lightRotateY, 0.f, 1.f, 0.f);
        gl.glRotatef(lightRotateY, 0.f, 0.f, 1.f);
        gl.glPushMatrix();
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightpos1, 0);
        gl.glTranslated(lightDistance, lightDistance, lightDistance);
        gl.glColor3d(1, 1, 1);
        glu.gluSphere(light, 1, slices, stacks);
        gl.glPushMatrix();
        gl.glPopMatrix();
        gl.glPopMatrix();
    }


    private void draw(GLAutoDrawable drawable) 
    {
        double cell_width_height = Cell.getCellSize();
        double cell_depth = 0;


        GL2 gl = drawable.getGL().getGL2();

//        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_DST_ALPHA);
//        //gl.glBlendFunc(GL2.GL_SRC_COLOR, GL2.GL_ONE_MINUS_SRC_COLOR);
//        gl.glShadeModel(GL2.GL_FLAT);
//        gl.glEnable(GL2.GL_BLEND);
//        gl.glClearColor(0,0,0,0);

        //
        // Clear all buffers enabled for color writing to prevent junk from 
        // being displayed on the parts that are not drawn on.
        //
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        /*
         * I need all the different cell depths here!!!!!!!!!
         */
        
        for(int x = this.minX; x < this.maxX; x++)
        {   
            for(int y = this.minY; y < this.maxY; y++)
            {    
                cell_depth = 0;
                for(int z = this.minZ; z < this.maxZ; z++)
                {
                    cell_depth += this.grid[x][y][z].getDepth();
                    /*
                     * Here set what active texture should be
                     */
                    
                    if(this.grid[x][y][z].getSoil() == Soil.AIR)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[0].getTextureObject(gl));  
                    }
                    else if(this.grid[x][y][z].getSoil() == Soil.GILACLAY)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[1].getTextureObject(gl));  
                    }
                    else if(this.grid[x][y][z].getSoil() == Soil.GILACLAYLOAM)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[2].getTextureObject(gl));  
                    }
                    else if(this.grid[x][y][z].getSoil() == Soil.GILALOAM)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[3].getTextureObject(gl));  
                    }
                    else if(this.grid[x][y][z].getSoil() == Soil.RIVERWASH)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[4].getTextureObject(gl));  
                    }
                    else if(this.grid[x][y][z].getSoil() == Soil.GILASAND)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[5].getTextureObject(gl));  
                    }
                    else if(this.grid[x][y][z].getSoil() == Soil.GILAFINESANDYLOAM)
                    {
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.textureList[6].getTextureObject(gl));  
                    }
                    
                    
                    
      
                    /*
                     * Not sure if I need to normalize blueness.  If I do I need the max water volume of the cell.
                     */
                    //double blueness = .5; //this.grid[(int)x][(int)y][k].getWaterVolume();
                    
                    gl.glLoadIdentity();
                    gl.glBegin(GL2.GL_QUADS); // front
                    //gl.glColor3d(0, 0, blueness);  //water color
                    gl.glNormal3d(0, 0, 1); // Used for lighting FX
                    gl.glVertex3d(x-cell_width_height, y-cell_width_height, z+cell_depth);
                    gl.glVertex3d(x+cell_width_height, y-cell_width_height, z+cell_depth);
                    gl.glVertex3d(x+cell_width_height, y+cell_width_height, z+cell_depth);
                    gl.glVertex3d(x-cell_width_height, y+cell_width_height, z+cell_depth);
                    gl.glEnd();

                    gl.glBegin(GL2.GL_QUADS); // back
                    //gl.glColor3d(0, 0, blueness);  //water color
                    gl.glNormal3d(0, 0, -1); // Used for lighting FX
                    gl.glVertex3d(x-cell_width_height, y+cell_width_height, z-cell_depth);
                    gl.glVertex3d(x+cell_width_height, y+cell_width_height, z-cell_depth);
                    gl.glVertex3d(x+cell_width_height, y-cell_width_height, z-cell_depth);
                    gl.glVertex3d(x-cell_width_height, y-cell_width_height, z-cell_depth);
                    gl.glEnd();

                    gl.glBegin(GL2.GL_QUADS); // left
                    //gl.glColor3d(0, 0, blueness);  //water color
                    gl.glNormal3d(-1, 0, 0); // Used for lighting FX
                    gl.glVertex3d(x-cell_width_height, y-cell_width_height, z-cell_depth);
                    gl.glVertex3d(x-cell_width_height, y-cell_width_height, z+cell_depth);
                    gl.glVertex3d(x-cell_width_height, y+cell_width_height, z+cell_depth);
                    gl.glVertex3d(x-cell_width_height, y+cell_width_height, z-cell_depth);
                    gl.glEnd();

                    gl.glBegin(GL2.GL_QUADS); // right
                    //gl.glColor3d(0, 0, blueness);  //water color
                    gl.glNormal3d(+1, 0, 0); // Used for lighting FX
                    gl.glVertex3d(x+cell_width_height, y+cell_width_height, z-cell_depth);
                    gl.glVertex3d(x+cell_width_height, y+cell_width_height, z+cell_depth);
                    gl.glVertex3d(x+cell_width_height, y-cell_width_height, z+cell_depth);
                    gl.glVertex3d(x+cell_width_height, y-cell_width_height, z-cell_depth);
                    gl.glEnd();

                    gl.glBegin(GL2.GL_QUADS); // top
                    //gl.glColor3d(0, 0, blueness);  //water color
                    gl.glNormal3d(0, 1, 0); // Used for lighting FX
                    gl.glVertex3d(x-cell_width_height, y+cell_width_height, z+cell_depth);
                    gl.glVertex3d(x+cell_width_height, y+cell_width_height, z+cell_depth);
                    gl.glVertex3d(x+cell_width_height, y+cell_width_height, z-cell_depth);
                    gl.glVertex3d(x-cell_width_height, y+cell_width_height, z-cell_depth);
                    gl.glEnd();

                    gl.glBegin(GL2.GL_QUADS); // bottom
                    //gl.glColor3d(0, 0, blueness);  //water color
                    gl.glNormal3d(0, -1, 0); // Used for lighting FX
                    gl.glVertex3d(x-cell_width_height, y-cell_width_height, z-cell_depth);
                    gl.glVertex3d(x+cell_width_height, y-cell_width_height, z-cell_depth);
                    gl.glVertex3d(x+cell_width_height, y-cell_width_height, z+cell_depth);
                    gl.glVertex3d(x-cell_width_height, y-cell_width_height, z+cell_depth);
                    gl.glEnd();
                }

            }

        }





        setCamera(gl, glu, zoomOut);
        //setMobileLight(gl, glu);

        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        //gl.glEnable(GL2.GL_CULL_FACE);

        gl.glFlush();

    } 
    
} 