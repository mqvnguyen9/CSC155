package a1;

/*
Michael Nguyen
CSC 155
Assignment 1
 */

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/*
This class is responsible for displaying all the GUI required for the assignment. It implements a MouseWheelListener so
scrolling the mouse wheel will zoom in and out.
 */
class Starter extends JFrame implements MouseWheelListener {
    private static final long serialVersionUID = 1L;

    private GLCanvas glCanvas;
    private JButton verticalMovement, circularMovement;

    private StarterModel starterModel; //where all the logic occurs

    private Starter() {
        initGUI();
        initKeyBindingsAndWheel();
        addListeners();
        FPSAnimator animator = new FPSAnimator(glCanvas, 60); //to allow for animation
        animator.start();
    }

    public static void main(String[] args) {
        new Starter();
    }

    /*
    Initializes the glCanvas, adds the model class, which implements GLEventListener, to it, adds the buttons to the
    JPanel, and sets various settings for the JFrame.
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        glCanvas = new GLCanvas();
        glCanvas.addGLEventListener(starterModel = new StarterModel());
        getContentPane().add(glCanvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(); //to allow for easier formatting of the two buttons
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

    /*
    Initializes and adds the command to change the color of the triangle from a solid color to a gradient of three
    colors.
     */
    private void initKeyBindingsAndWheel() {
        ColorCommand colorCommand = new ColorCommand(starterModel);
        InputMap iMap = ((JPanel) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        KeyStroke cKey = KeyStroke.getKeyStroke('c');
        iMap.put(cKey, "color");
        ActionMap actionMap = ((JPanel) getContentPane()).getActionMap();
        actionMap.put("color", colorCommand);
    }

    /*
    Adds the ActionListeners to the two JButtons, which flips flags for vertical and circular movement in the model
    class. Also adds the MouseWheelListener to the JFrame.
     */
    private void addListeners() {
        verticalMovement.addActionListener(e -> {
            starterModel.setVerticalMovement(!starterModel.isVerticalMovement());
            starterModel.setCircularMovement(false);
        });
        circularMovement.addActionListener(e -> {
            starterModel.setCircularMovement(!starterModel.isCircularMovement());
            starterModel.setVerticalMovement(false);
        });
        this.addMouseWheelListener(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        starterModel.setZoomAmount(starterModel.getZoomAmount() + (0.25f * notches));

    }
}
