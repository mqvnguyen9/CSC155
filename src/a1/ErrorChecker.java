package a1;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.glu.GLU;

class ErrorChecker {
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
}
