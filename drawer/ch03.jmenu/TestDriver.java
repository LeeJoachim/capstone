package drawer.ch03.jmenu;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

// import java.awt.Dimension;
// import java.awt.Toolkit;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.InputEvent;
// import java.awt.Image;
// import java.awt.event.KeyEvent;


// import javax.swing.ImageIcon;
// import javax.swing.JFrame;
// import javax.swing.JMenu;
// import javax.swing.JMenuBar;
// import javax.swing.JMenuItem;
// import javax.swing.JOptionPane;
// import javax.swing.KeyStroke;

/* menu is sequential programming */
class DrawerFrame extends JFrame {

    /* the composition of a screen */
    DrawerFrame() {
        setTitle("title");
        
        /* get current screen size */
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();
        int screenWidth  = dimension.width;
        int screenHeight = dimension.height;
        /**/
        /* screen is located screen-center */
        super.setSize(screenWidth*2/3, screenHeight*2/3);
        super.setLocation(screenWidth/6, screenHeight/6);
        /**/
        /* set Icon Image */
        // Toolkit tk = Toolkit.getDefaultToolkit();
        // Image image = tk.getImage("ball.gif");
        // super.setIconImage(image);
        /**/

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
