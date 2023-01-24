package drawer.ch05.graphics;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class DrawerView extends JPanel implements MouseListener, MouseMotionListener {

    int startX;
    int startY;
    int oldX;
    int oldY;

    DrawerView() {
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
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
        /* init */
        startX = e.getX();
        startY = e.getY();
        oldX = startX;
        oldY = startY;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    /* rubber-band technique */
    @Override
    public void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();
        /* XOR
         * 1 : 1 == 0;
         * 0 : 0 == 0;
         * else 1;
         */
        g.setXORMode(getBackground());
        /* overlapped */
        int minX = Math.min(startX, oldX);
        int minY = Math.min(startY, oldY);
        int width = Math.abs(startX-oldX);
        int height = Math.abs(startY-oldY);
        g.drawRect(minX, minY, width, height);
        /**/
        /* location that mouse pointer located */
        int endX = e.getX();
        int endY = e.getY();
        minX = Math.min(startX, endX);
        minY = Math.min(startY, endY);
        width = Math.abs(startX-endX);
        height = Math.abs(startY-endY);
        g.drawRect(minX, minY, width, height); // draw
        /* set and waiting */
        oldX = endX;
        oldY = endY;
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
