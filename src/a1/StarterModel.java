package a1;

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

public class StarterModel implements GLEventListener {
    private int rendering_program;
    @SuppressWarnings("CanBeFinal")
    private int[] vao = new int[1];

    private float y = 0.0f;
    private float degrees = 0.0f;
    private float yInc = 0.01f;

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

    @Override
    public void display(GLAutoDrawable arg0) {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        float[] color = {0.0f, 0.0f, 0.0f, 1.0f};
        FloatBuffer bgBuffer = Buffers.newDirectFloatBuffer(color);
        gl.glClearBufferfv(gl.GL_COLOR, 0, bgBuffer);

        if (isVerticalMovement) {
            y += yInc;
            if (y > 1.0f || y < -1.0f) {
                yInc *= -1;
            }
            int offset_loc = gl.glGetUniformLocation(rendering_program, "yOffset");
            gl.glProgramUniform1f(rendering_program, offset_loc, y);
        }
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

        int isGradientLoc = gl.glGetUniformLocation(rendering_program, "isGradient");
        gl.glProgramUniform1i(rendering_program, isGradientLoc, isGradient);

        gl.glUseProgram(rendering_program);
        gl.glDrawArrays(gl.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable arg0) {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        ErrorChecker.displaySystemInfo();

        rendering_program = createShaderProgram();
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
    }

    private int createShaderProgram() {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
        int[] linked = new int[1];

        String vshaderSource[] = readShaderFile("./a1/vertex.shader");
        String fshaderSource[] = readShaderFile("./a1/fragment.shader");

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

        if ((vertCompiled[0] != 1) || fragCompiled[0] != 1) {
            System.out.println("\nCompilation error; return-flags");
            System.out.println("vertCompiled = " + vertCompiled[0] + "; fragCompiled = " + fragCompiled[0]);
        } else {
            System.out.println("Successful compilation");
        }

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

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
        // TODO Auto-generated method stub

    }
}
