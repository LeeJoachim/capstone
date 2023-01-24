package drawer.ch06.saveboxes;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

class Box {
    int x1;
    int y1;
    int x2;
    int y2;

    Box(int currentX, int currentY) {
        x1 = x2 = currentX;
        y1 = y2 = currentY;
    }

    void drawRectangle(Graphics g) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int width = Math.abs(x1-x2);
        int height = Math.abs(y1-y2);
        g.drawRect(minX, minY, width, height);
    }
    /** it is fuction has rubber-banding mechanism
     *  need to declare function g.setXORMode(getBackground()); 
     */
    void drawRectXOR(Graphics g, int nowX, int nowY) {
        /* overlapped 1 : 1 == 0 */
        /* erase */
        drawRectangle(g);
        /* send current location that mouse cursor located */
        setX2Y2(nowX, nowY);
        /* drawRectangle */
        drawRectangle(g);
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

class DrawerView extends JPanel implements MouseListener, MouseMotionListener {

    Box box;
    ArrayList<Box> boxes = new ArrayList<Box>();
    
    DrawerView() {
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /* traverse */
        /* 1. Basic Array : old style */
        // Box[] arr = (Box[])boxes.toArray();
        // for (int i = 0; i < arr.length; i++) {
        //     arr[i].drawRectangle(g);
        // }

        /* 2. Iterator or listIterator style */
        // Iterator<Box> iter = boxes.iterator();
        // while(iter.hasNext()) {
        //     Box box = iter.next();
        //     box.drawRectangle(g);
        // }

        /* 3. for-each syntax - Notice! : Not ensures traverse order
         * for traverse every collection obj 
         * e.g. LinkedList, ArrayList, Vector, etc.
         */
        // for (Box i : boxes) {
        //     i.drawRectangle(g);
        // }

        /* 4. for syntax - geti */
        /* when use linked list, 'traverse burden' is high if you use get(i) */
        for (int i = 0; i < boxes.size(); i++) {
            boxes.get(i).drawRectangle(g);
        }
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
        /* init box's data members */
        box = new Box(e.getX(), e.getY());
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        boxes.add(box);
        box = null;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();
        g.setXORMode(getBackground());
        box.drawRectXOR(g, e.getX(), e.getY());
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
        setTitle("title");
        
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
        // super.add(drawerView); ???
        

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

        /* Menu 2. helpMenu */
        JMenu helpMenu = new JMenu("Help(H)");
        menuBar.add(helpMenu);

        JMenuItem drawerInfo = new JMenuItem("Drawer's Info(I)");
        helpMenu.add(drawerInfo);
        drawerInfo.addActionListener( (e) -> {
            JOptionPane.showMessageDialog(null, 
                        "==Drawer Info==\nAuthor: Lee Gun-Ho", 
                        "drawer's Info", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        );
        
        /* EXIT */
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

class TestDriver {
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
