package a1;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Scanner;

class Starter extends JFrame implements MouseWheelListener {
    private static final long serialVersionUID = 1L;

    private GLCanvas glCanvas;
    private JButton verticalMovement, circularMovement;

    private StarterModel starterModel;

    private Starter() {
        initGUI();
        initKeyBindingsAndWheel();
        addButtonListeners();
        FPSAnimator animator = new FPSAnimator(glCanvas, 60);
        animator.start();
    }

    public static void main(String[] args) {
        new Starter();
    }

    private void initGUI() {
        setLayout(new BorderLayout());

        glCanvas = new GLCanvas();
        glCanvas.addGLEventListener(starterModel = new StarterModel());
        getContentPane().add(glCanvas, BorderLayout.CENTER);

        this.addMouseWheelListener(this);

        JPanel buttonPanel = new JPanel();
        verticalMovement = new JButton("Vertical Movement");
        circularMovement = new JButton("Circular Movement");
        buttonPanel.add(verticalMovement);
        buttonPanel.add(circularMovement);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setSize(new Dimension(500, 500));
        setResizable(false);
        setTitle("a1.Starter Application");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initKeyBindingsAndWheel() {
        ColorCommand colorCommand = new ColorCommand(starterModel);
        InputMap iMap = ((JPanel) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        KeyStroke cKey = KeyStroke.getKeyStroke('c');
        iMap.put(cKey, "color");
        ActionMap actionMap = ((JPanel) getContentPane()).getActionMap();
        actionMap.put("color", colorCommand);
    }

    private void addButtonListeners() {
        verticalMovement.addActionListener(e -> {
            starterModel.setVerticalMovement(!starterModel.isVerticalMovement());
            starterModel.setCircularMovement(false);
        });
        circularMovement.addActionListener(e -> {
            starterModel.setCircularMovement(!starterModel.isVerticalMovement());
            starterModel.setVerticalMovement(false);
        });
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
