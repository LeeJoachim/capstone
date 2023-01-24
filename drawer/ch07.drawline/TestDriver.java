// TestDriver.java

// {{MODELER_BEFORE_CLASS(TestDriver) - DO NOT DELETE THIS LINE
// et cetera. 

// Drawer 1.0 Information
// Organization: Busan Univ. of Foreign Studies
// Author: Lee Joachim
// Date: Friday, January 13, 2023
// Super class:
// Purpose of Drawer class:
// Version: 1.0
/**
*/

// }}MODELER_BEFORE_CLASS

package drawer.ch07.drawline;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

/* the codes are used and improved 
 * by skills that inheritance, dynamic binding, polymorphic container.
 */

abstract class Figure {
    abstract void draw(Graphics g);
    abstract void setX2Y2(int x2, int y2);
    void drawXOR(Graphics g, int nowX, int nowY) {
        /* overlapped 1 : 1 == 0 */
        /* erased by XOR */
        draw(g);
        /* send current location that mouse cursor located */
        /* etc. x1,y1 : when you pressed your cursor, values are fixed */
        setX2Y2(nowX, nowY);
        /* draw */
        draw(g);
    }
}

abstract class OnePointFigure extends Figure {
}
abstract class TwoPointFigure extends Figure {
    int x1;
    int y1;
    int x2;
    int y2;

    public TwoPointFigure(int nowX, int nowY) {
        x1 = x2 = nowX;
        y1 = y2 = nowY;
    }
    public void setX2(int x2) {
        this.x2 = x2;
    }
    public void setY2(int y2) {
        this.y2 = y2;
    }
    void setX2Y2(int x2, int y2) {
        setX2(x2); setY2(y2); // more safe!
    }
}

class Line extends TwoPointFigure {

    Line(int nowX, int nowY) {
        super(nowX, nowY);
    }
    void draw(Graphics g) {
        g.drawLine(x1, y1, x2, y2);
    }
    
}
class Box extends TwoPointFigure {

    Box(int nowX, int nowY) {
        super(nowX, nowY);
    }

    void draw(Graphics g) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int width = Math.abs(x1-x2);
        int height = Math.abs(y1-y2);
        g.drawRect(minX, minY, width, height);
    }
}

class DrawerView extends JPanel implements MouseListener, MouseMotionListener {

    /* MACRO NUMBER */
    static final int DRAW_RECTANGLE = 1;
    static final int DRAW_LINE = 2;
    /* flag */
    int whatToDraw;
    /**/

    Figure nowFigure;

    /* polymorphic collection obj */
    /* polymorphic container */
    ArrayList<Figure> figures = new ArrayList<Figure>();
    
    DrawerView() {
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Figure i : figures) {
            i.draw(g);
        }
    }

    public void setWhatToDraw(int whatToDraw) {
        this.whatToDraw = whatToDraw;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mousePressed(MouseEvent e) {

        /* this is first divergence and can not remove these 'if syntaxes'
         * i.e. this mechanism is essential
         */
        if (whatToDraw == DRAW_RECTANGLE) {   
            nowFigure = new Box(e.getX(), e.getY());
        } else if (whatToDraw == DRAW_LINE) {
            nowFigure = new Line(e.getX(), e.getY());
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        figures.add(nowFigure);
        nowFigure = null;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();
        g.setXORMode(getBackground());
        nowFigure.drawXOR(g, e.getX(), e.getY());
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}

class DrawerFrame extends JFrame {

    DrawerView view;

    /* the composition of a screen */
    DrawerFrame() {
        setTitle("Drawer 1.0");
        
        /* get current screen size */
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();
        int screenWidth  = dimension.width;
        int screenHeight = dimension.height;
        /**/
        /* screen is located center */
        super.setSize(screenWidth*2/3, screenHeight*2/3);
        super.setLocation(screenWidth/6, screenHeight/6);
        /**/

        /* add 'frame's content pane' and 'drawer view'  */
        /* ContentPane */
        Container container = super.getContentPane();
        view = new DrawerView();
        container.add(view);
        

        /* what is that mean? writing this*/
        // view = new DrawerView();
        // super.add(view);
        

        /* attach MenuBar > Menu > MenuItem */
        JMenuBar menuBar = new JMenuBar();
        super.setJMenuBar(menuBar);

        /* Menu 1. FileMenu */
        JMenu fileMenu = new JMenu("File(F)");
        menuBar.add(fileMenu);
        /* Menu 1. FileMenu > newFile */
        JMenuItem newFile = new JMenuItem("New File(N)");
        fileMenu.add(newFile);
        /* Menu 1. FileMenu > newFile > add seperator */
        fileMenu.addSeparator();
        /**/


        /* add ActionListener 
         * i.e. can transformed to lambda expression */
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ok!");
            }
        });
        /**/

        /* setters */
        /* set Icon */
        newFile.setIcon(new ImageIcon("new.gif"));
        /* set mnemonic 'N' */
        newFile.setMnemonic('N');
        /* set Accelerator 'ctrl + n' */
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        /**/

        JMenuItem openFile = new JMenuItem("Open File(O)");
        fileMenu.add(openFile);
        JMenuItem saveFile = new JMenuItem("Save File(S)");
        fileMenu.add(saveFile);
        JMenuItem anotherFile = new JMenuItem("Another File(A)");
        fileMenu.add(anotherFile);
        JMenuItem exit = new JMenuItem("Exit(X)");
        fileMenu.add(exit);
        exit.addActionListener( (e) -> {
            System.out.println("lambda expression!");
        });

        /* Menu 2. figureMenu */
        JMenu figureMenu = new JMenu("Figure(F)");
        menuBar.add(figureMenu);
        
        JMenuItem rectangle = new JMenuItem("Rectangle(R)");
        figureMenu.add(rectangle);
        rectangle.addActionListener((e) -> {
            view.setWhatToDraw(DrawerView.DRAW_RECTANGLE);
        });
        
        JMenuItem line = new JMenuItem("Line(L)");
        figureMenu.add(line);
        line.addActionListener((e) -> {
            view.setWhatToDraw(DrawerView.DRAW_LINE);
        });

        /* Menu 3. helpMenu */
        JMenu helpMenu = new JMenu("Help(H)");
        menuBar.add(helpMenu);
        
        JMenuItem drawerInfo = new JMenuItem("Drawer's Info(I)");
        helpMenu.add(drawerInfo);
        drawerInfo.addActionListener( (e) -> {
            JOptionPane.showMessageDialog(null, 
                    "Drawer 1.0 Information\n\n" + 
                    "Organization: Busan Univ. of Foreign Studies\n" +
                    "Author: Lee Joachim\n" +
                    "Date: Friday, January 13, 2023\n" +
                    "Super class:\n" +
                    "Purpose of Drawer class:\n" +
                    "Version: 1.0\n", 
                    "drawer's Info", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        );
        
        /* EXIT */
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

class TestDriver {

    /* Operations */
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
