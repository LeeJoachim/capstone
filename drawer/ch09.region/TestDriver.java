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

package drawer.ch09.region;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

class FigureDialog extends JDialog {
    DrawerView view; // reference for : DialogPanel draws lines on view;     
    class DialogPanel extends JPanel implements ActionListener {

        JTextField x1Field;
        JTextField y1Field;
        JTextField x2Field;
        JTextField y2Field;
        String[] strings = {"rectangle", "line"};
        JComboBox<String> figures;

        DialogPanel() {
            this.setLayout(new GridLayout(6, 2)); // grid layout

            /* [label :] [ text field ] */
            /* x1 */
            JLabel x1Label = new JLabel(" x1: ");
            x1Label.setFont(new Font("monospace", Font.BOLD, 16));
            x1Label.setHorizontalAlignment(SwingConstants.CENTER);
            add(x1Label);
            /* x1 : [???] */
            x1Field = new JTextField();
            x1Field.setHorizontalAlignment(SwingConstants.CENTER);
            add(x1Field);
            /* y1 */
            JLabel y1Label = new JLabel(" y1: ");
            y1Label.setFont(new Font("monospace", Font.BOLD, 16));
            y1Label.setHorizontalAlignment(SwingConstants.CENTER);
            add(y1Label);
            /* y1 : [???] */
            y1Field = new JTextField();
            y1Field.setHorizontalAlignment(SwingConstants.CENTER);
            add(y1Field);
            /* x2 */
            JLabel x2Label = new JLabel(" x2: ");
            x2Label.setFont(new Font("monospace", Font.BOLD, 16));
            x2Label.setHorizontalAlignment(SwingConstants.CENTER);
            add(x2Label);
            /* x2 : [???] */
            x2Field = new JTextField();
            x2Field.setHorizontalAlignment(SwingConstants.CENTER);
            add(x2Field);
            /* y2 */
            JLabel y2Label = new JLabel(" y2: ");
            y2Label.setFont(new Font("monospace", Font.BOLD, 16));
            y2Label.setHorizontalAlignment(SwingConstants.CENTER);
            add(y2Label);
            /* y2 : [???] */
            y2Field = new JTextField();
            y2Field.setHorizontalAlignment(SwingConstants.CENTER);
            add(y2Field);
            /**/

            /* space */
            JLabel space = new JLabel("");
            add(space);
            /* JComboBox {rectangle, line} */
            figures = new JComboBox<String>(strings);
            add(figures);
            /**/

            /* JButton ok */
            JButton ok = new JButton("ok");
            ok.addActionListener(this);
            add(ok);
            /* JButton cancel */
            JButton cancel = new JButton("cancel");
            cancel.addActionListener(this);
            add(cancel);
            /**/
        }

        private void onOK() {
            int x1, y1, x2, y2;
            
            String selection = (String)this.figures.getSelectedItem();
            try {
                x1 = Integer.parseInt(this.x1Field.getText());
                y1 = Integer.parseInt(this.y1Field.getText());
                x2 = Integer.parseInt(this.x2Field.getText());
                y2 = Integer.parseInt(this.y2Field.getText());
                
            } catch (Exception e) {
                System.out.println("Invalid text field, try again.");
                return;
            }
            Figure nowFigure = null;
            if (selection.equals("rectangle")) {
                nowFigure = new Rectangle(x1, y1, x2, y2);
            } else if (selection.equals("line")) {
                nowFigure = new Line(x1, y1, x2, y2);
            }
            /* make region and add collection obj and repaint*/
            view.addFigures(nowFigure);
            /**/
            /* text reset "" */
            x1Field.setText("");
            y1Field.setText("");
            x2Field.setText("");
            y2Field.setText("");
            /**/
        }
        private void onCancel() {
            FigureDialog.this.setVisible(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            if (name.equals("ok")) {
                onOK();
            } else if (name.equals("cancel")) {
                onCancel();
            }
        }
        
    }
    FigureDialog(String title, DrawerView view) {
        super((Frame)null, title);
        this.view = view;
        setLocation(100, 150);
        
        Container container = getContentPane();
        JPanel panel = new DialogPanel();
        container.add(panel);

        setSize(300, 200);
    }
}

abstract class Figure {
    Polygon region;

    abstract void draw(Graphics g);
    abstract void setX2Y2(int x2, int y2);
    abstract void makeRegion();
    boolean contains(int x, int y) {
        /* defensive */
        if (region == null) return false; // didn't make polygon!
        /**/

        return region.contains(x, y); // cursor is in polygon?
    }
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
    
    public TwoPointFigure(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
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

    @Override
    void makeRegion() {

        /* region mechanism */
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        xPoints[0] = x1;
        yPoints[0] = y1;
        xPoints[1] = x2;
        yPoints[1] = y1;
        xPoints[2] = x2;
        yPoints[2] = y2;
        xPoints[3] = x1;
        yPoints[3] = y2;
        
        /* send coordinates for make polygon box */
        region = new Polygon(xPoints, yPoints, 4);
        /**/
    }

}

class Line extends TwoPointFigure {

    Line(int nowX, int nowY) {
        super(nowX, nowY);
    }
    public Line(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }
    @Override
    void makeRegion() {
        int regionWidth = 6; // control this!

        int x = x1;
        int y = y1;
        int width = x2 - x1;
        int height = y2 - y1;

        /* algorithm for making inclined polygon */
        /* do not touch! */
        int sign_h = 1;
        if (height < 0) sign_h = -1;
        double angle;
        double theta = (width!=0) ? Math.atan((double)(height)/(double)(width)) : sign_h*Math.PI/2.;
        if (theta < 0) theta = theta + 2 * Math.PI;
        angle = (theta + Math.PI / 2.);
        int dx = (int)(regionWidth * Math.cos(angle));
        int dy = (int)(regionWidth * Math.sin(angle));
        int xpoints[] = new int[4];
        int ypoints[] = new int[4];
        xpoints[0] = x + dx;     ypoints[0] = y + dy;
        xpoints[1] = x - dx;     ypoints[1] = y - dy;
        xpoints[2] = x + width - dx; ypoints[2] = y + height - dy;
        xpoints[3] = x + width + dx; ypoints[3] = y + height + dy;
        region = new Polygon(xpoints, ypoints,4);
        /**/
    }
    void draw(Graphics g) {
        g.drawLine(x1, y1, x2, y2);
    }
    
}
class Rectangle extends TwoPointFigure {

    Rectangle(int nowX, int nowY) {
        super(nowX, nowY);
    }
    public Rectangle(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
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

    /* Attributes */
    /* MACRO NUMBER */
    static final int DRAW_RECTANGLE = 1;
    static final int DRAW_LINE = 2;    
    /* flag */
    int whatToDraw;
    /* current figure */
    Figure nowFigure;
    /* polymorphic collection obj */
    /* polymorphic container */
    ArrayList<Figure> figures = new ArrayList<Figure>();
    /**/

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
    public void addFigures(Figure nowFigure) {
        nowFigure.makeRegion();
        figures.add(nowFigure);
        repaint(); // call paintComponent()
    }
    boolean cursorIsInPolygon(int x, int y) {
        for (Figure i : figures) {
            if (i.contains(x, y) == true) {
                return true;
            }
        }
        return false;
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
            nowFigure = new Rectangle(e.getX(), e.getY());
        } else if (whatToDraw == DRAW_LINE) {
            nowFigure = new Line(e.getX(), e.getY());
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        nowFigure.makeRegion(); // set new Polygon(x1y1x2y2)
        /* for checking polygon */
        // Graphics g = getGraphics();
        // g.fillPolygon(nowFigure.region);
        /**/
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
        int x = e.getX();
        int y = e.getY();

        if (cursorIsInPolygon(x, y)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
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

        /* attach MenuBar > Menu > MenuItem */
        JMenuBar menuBar = new JMenuBar();
        super.setJMenuBar(menuBar);

        /* Menu 1. FileMenu */
        JMenu fileMenu = new JMenu("File(F)");
        menuBar.add(fileMenu);
        /* Menu 1. FileMenu > newFile */
        JMenuItem newFile = new JMenuItem("New File (N)");
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

        JMenuItem openFile = new JMenuItem("Open File (O)");
        fileMenu.add(openFile);
        JMenuItem saveFile = new JMenuItem("Save File (S)");
        fileMenu.add(saveFile);
        JMenuItem anotherFile = new JMenuItem("Another File (A)");
        fileMenu.add(anotherFile);
        JMenuItem exit = new JMenuItem("Exit (X)");
        fileMenu.add(exit);
        exit.addActionListener( (e) -> {
            System.out.println("lambda expression!");
        });

        /* Menu 2. figureMenu */
        JMenu figureMenu = new JMenu("Figure(F)");
        menuBar.add(figureMenu);
        
        JMenuItem rectangle = new JMenuItem("Rectangle (R)");
        figureMenu.add(rectangle);
        rectangle.addActionListener((e) -> {
            view.setWhatToDraw(DrawerView.DRAW_RECTANGLE);
        });
        
        JMenuItem line = new JMenuItem("Line (L)");
        figureMenu.add(line);
        line.addActionListener((e) -> {
            view.setWhatToDraw(DrawerView.DRAW_LINE);
        });

        /* Menu 3. toolMenu */
        JMenu toolMenu = new JMenu("Tool(T)");
        menuBar.add(toolMenu);

        /* when it opened, don't focus back to parent frame */
        JMenuItem modalTool = new JMenuItem("Modal (M)");
        toolMenu.add(modalTool);
        modalTool.addActionListener((e) -> {
            FigureDialog dialog = new FigureDialog("Figure Dialog", view);
            dialog.setModal(true);
            dialog.setVisible(true);
        });
        JMenuItem modelessTool = new JMenuItem("Modeless (S)");
        toolMenu.add(modelessTool);
        modelessTool.addActionListener((e) -> {
            FigureDialog dialog = new FigureDialog("Figure Dialog", view);
            dialog.setModal(false);
            dialog.setVisible(true);
        });

        /* Menu final. helpMenu */
        JMenu helpMenu = new JMenu("Help(H)");
        menuBar.add(helpMenu);
        
        JMenuItem drawerInfo = new JMenuItem("Drawer's Info (I)");
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
