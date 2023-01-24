package drawer.ch04.contentpane;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class DrawerView extends JPanel {

    /* hook function */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // notice!
        g.drawLine(0, 0, 200, 200);
    }
    /**/
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

        /* We need to use Jpanel, and Jframe's content pane.
         * case that coordinate problems happen */
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Graphics g = getGraphics();
                g.drawLine(0, 0, 200, 200);
            }
        });

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
