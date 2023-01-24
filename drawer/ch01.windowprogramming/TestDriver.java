package drawer.ch01.windowprogramming;
/* component/window programming */

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

class DrawerFrame extends JFrame {
    /* the composition of a screen
     * sequential programming
     * tree structure
     */
    DrawerFrame() {
        setTitle("title");
        
        /* get current screen size */
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();
        int screenWidth  = dimension.width;
        int screenHeight = dimension.height;
        /**/
        
        /* screen is located screen-center */
        setSize(screenWidth*2/3, screenHeight*2/3);
        setLocation(screenWidth/6, screenHeight/6);
        /**/
        // setSize(700, 500);
        // setLocation(100, 100);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

class TestDriver {
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
