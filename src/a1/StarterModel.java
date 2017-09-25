package a1;

/*
Michael Nguyen
CSC 155
Assignment 1
 */

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Scanner;

/*
This class implements GLEventListener, and thus, implements all the logic to actually render the code.
 */
public class StarterModel implements GLEventListener {
    private int rendering_program;
    @SuppressWarnings("CanBeFinal")
    private int[] vao = new int[1];

    private float y = 0.0f;
    private float degrees = 0.0f;
    private float yInc = 0.01f;
    private float zoomAmount = 1.0f;

    private boolean isVerticalMovement = false;
    private boolean isCircularMovement = false;
    private int isGradient = 0;

    public int isGradient() {
        return isGradient;
    }

    public void setGradient(int gradient) {
        isGradient = gradient;
    }

    public boolean isVerticalMovement() {
        return isVerticalMovement;
    }

    public void setVerticalMovement(boolean verticalMovement) {
        isVerticalMovement = verticalMovement;
    }

    public boolean isCircularMovement() {
        return isCircularMovement;
    }

    public void setCircularMovement(boolean circularMovement) {
        isCircularMovement = circularMovement;
    }

    public float getZoomAmount() {
        return zoomAmount;
    }

    public void setZoomAmount(float zoomAmount) {
        this.zoomAmount = zoomAmount;
    }

    /*
    The display method is called every frame to update what is being displayed in the GLCanvas.
     */
    @Override
    public void display(GLAutoDrawable arg0) {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        //Clear the screen to black to allow for clean animation.
        float[] color = {0.0f, 0.0f, 0.0f, 1.0f};
        FloatBuffer bgBuffer = Buffers.newDirectFloatBuffer(color);
        gl.glClearBufferfv(gl.GL_COLOR, 0, bgBuffer);

        //If the vertical movement flag is set, pass a uniform variable to the shader code to move y coordinates up and
        // down.
        if (isVerticalMovement) {
            y += yInc;
            if (y > 1.0f || y < -1.0f) {
                yInc *= -1;
            }
            int offset_loc = gl.glGetUniformLocation(rendering_program, "yOffset");
            gl.glProgramUniform1f(rendering_program, offset_loc, y);
        }

        /*
        If the circular movement flag is set, calculate the new x and y coordinates using cosine on a ever growing
        degree value. Then pass these new coordinates uniform variables to the shader code.
         */
        if (isCircularMovement) {
            float degreeInc = 0.01f;
            degrees += degreeInc;
            float x = (float) Math.cos((double) degrees);
            y = (float) Math.sin((double) degrees);

            int xOffsetLoc = gl.glGetUniformLocation(rendering_program, "xOffset");
            int yOffsetLoc = gl.glGetUniformLocation(rendering_program, "yOffset");
            gl.glProgramUniform1f(rendering_program, xOffsetLoc, x);
            gl.glProgramUniform1f(rendering_program, yOffsetLoc, y);
        }

        //Set the level of zoom based on how many times the scroll wheel was moved.
        int zoomAmount_loc = gl.glGetUniformLocation(rendering_program, "zoomAmount");
        gl.glProgramUniform1f(rendering_program, zoomAmount_loc, zoomAmount);

        //Set whether the fragment shader will be set to render one solid color or a gradient.
        int isGradientLoc = gl.glGetUniformLocation(rendering_program, "isGradient");
        gl.glProgramUniform1i(rendering_program, isGradientLoc, isGradient);

        //Draw the triangle.
        gl.glUseProgram(rendering_program);
        gl.glDrawArrays(gl.GL_TRIANGLES, 0, 3);
    }

    //Not used in Assignment 1
    @Override
    public void dispose(GLAutoDrawable arg0) { }

    /*
    This method is called once when initializing the code. It calls a static method in the ErrorChecker class to display
    information about the system the code is currently running on. It then calls a private method createShaderProgram to
    load all the shader code.
     */
    @Override
    public void init(GLAutoDrawable arg0) {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        ErrorChecker.displaySystemInfo();

        rendering_program = createShaderProgram();
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
    }

    /*
    Where the magic happens. Reads in the shader files, checks if there are errors, creates the rendering_program,
    links the shaders to the program, and returns the program to be used in the display() method.
     */
    private int createShaderProgram() {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
        int[] linked = new int[1];

        String vshaderSource[] = readShaderFile("./a1/vertex.shader");
        String fshaderSource[] = readShaderFile("./a1/fragment.shader");

        //First tries to create the vertex shader.
        int vShader = gl.glCreateShader(gl.GL_VERTEX_SHADER);
        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
        gl.glCompileShader(vShader);
        ErrorChecker.checkOpenGLError();
        gl.glGetShaderiv(vShader, gl.GL_COMPILE_STATUS, vertCompiled, 0);
        if (vertCompiled[0] == 1) {
            System.out.println("... vertex compilation success.");
        } else {
            System.out.println("... vertex compilation failed.");
            ErrorChecker.printShaderLog(vShader);
        }

        //Then tries to create the fragment shader.
        int fShader = gl.glCreateShader(gl.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);
        gl.glCompileShader(fShader);
        ErrorChecker.checkOpenGLError();
        gl.glGetShaderiv(fShader, gl.GL_COMPILE_STATUS, fragCompiled, 0);
        if (fragCompiled[0] == 1) {
            System.out.println("... fragment compilation success.");
        } else {
            System.out.println("... fragment compilation failed.");
            ErrorChecker.printShaderLog(fShader);
        }

        //Check to see if there are any errors along the way.
        if ((vertCompiled[0] != 1) || fragCompiled[0] != 1) {
            System.out.println("\nCompilation error; return-flags");
            System.out.println("vertCompiled = " + vertCompiled[0] + "; fragCompiled = " + fragCompiled[0]);
        } else {
            System.out.println("Successful compilation");
        }

        //create the rendering program and links the shaders to it.
        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
        gl.glLinkProgram(vfprogram);
        ErrorChecker.checkOpenGLError();
        gl.glGetProgramiv(vfprogram, gl.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 1) {
            System.out.println(" ... linking success.");
        } else {
            System.out.println(" ... linking failed");
            ErrorChecker.printProgramLog(vfprogram);
        }
        return vfprogram;
    }

    /*
    Reads in a file and saves each line into a ArrayList, which is then converted to a String[]. If the code is not
    found, it will try modifying the file directory to find it.
     */
    private String[] readShaderFile(String fileName) {
        ArrayList<String> arrayList = new ArrayList<>();
        String[] finalArray;
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNextLine()) {
                arrayList.add(sc.nextLine() + "\n");
            }
            sc.close();
            finalArray = arrayList.toArray(new String[arrayList.size()]);
        } catch (FileNotFoundException e) {
            finalArray = readShaderFile(fileName.replaceFirst(".", "./src"));
        }
        return finalArray;
    }

    //Not used in Assignment 1
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) { }
}
