package a1;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.glu.GLU;

/*
This is a static utility class that prints various types of information to the console. Can be useful to determine there
was a program somewhere along the process of creating and linking the shaders. Also has a method to display system info.
 */
class ErrorChecker {

    //Checks to see if there are any errors during shader creation.
    public static void printShaderLog(int shader) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] len = new int[1];
        int[] chWritten = new int[1];
        byte[] log;

        gl.glGetShaderiv(shader, GL4.GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0) {
            log = new byte[len[0]];
            gl.glGetShaderInfoLog(shader, len[0], chWritten, 0, log, 0);
            System.out.println("Shader Info Log: ");
            for (byte i : log) {
                System.out.print((char) i);
            }
            System.out.println();
        }
    }

    //Checks to see if there are any errors during program compilation
    public static void printProgramLog(int prog) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] len = new int[1];
        int[] chWritten = new int[1];
        byte[] log;

        gl.glGetProgramiv(prog, gl.GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0) {
            log = new byte[len[0]];
            gl.glGetProgramInfoLog(prog, len[0], chWritten, 0, log, 0);
            System.out.println("program Info Log: ");
            for (byte i : log) {
                System.out.print((char) log[i]);
            }
            System.out.println();
        }
    }

    //Checks if there are any general errors with OpenGL
    public static boolean checkOpenGLError() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        boolean foundError = false;
        GLU glu = new GLU();
        int glError = gl.glGetError();
        while (glError != GL4.GL_NO_ERROR) {
            System.err.println("glError: " + glu.gluErrorString(glError));
            foundError = true;
            glError = gl.glGetError();
        }
        return foundError;
    }

    //Displays system info, including OpenGL version, vender, video card, JOGL version, and Java version.
    public static void displaySystemInfo() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        System.out.println("Open GL Info:");
        System.out.println(gl.glGetString(gl.GL_VERSION));
        System.out.println(gl.glGetString(gl.GL_VENDOR));
        System.out.println(gl.glGetString(gl.GL_RENDERER) + "\n");
        System.out.println("JOGL Info:");
        System.out.println(Package.getPackage("com.jogamp.opengl").getImplementationVersion() + "\n");
        System.out.println("Java Version:");
        System.out.println(System.getProperty("java.version") + "\n");
    }
}
