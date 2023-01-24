package drawer.ch02.eventhandler;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;

import javax.swing.JFrame;

/* implements event handler
 * listener : interface
 * adapter  : abstract class 
 *  -difference is that it has a nothing contents 
 *   just exist only function body forms by block e.g. {}
 *   it's purpose is that compiler don't throws exceptions
 *   
 *  -and i think adapter is dangerous.
 */


/* case 1 : using independence class */
// class MyMouseListener implements MouseListener {

//     DrawerFrame frame;

//     MyMouseListener(DrawerFrame frame) {
//         this.frame = frame;
//     }

//     @Override
//     public void mouseClicked(MouseEvent e) {
//         System.out.println("clicked");
//     }
//     @Override
//     public void mouseEntered(MouseEvent e) {
//     }
//     @Override
//     public void mouseExited(MouseEvent e) {
//     }
//     @Override
//     public void mousePressed(MouseEvent e) {
//         int x = e.getX();
//         int y = e.getY();
//         Graphics g = frame.getGraphics();
//         /* draw x sign */
//         g.drawLine(x-10, y-10, x+10, y+10);
//         g.drawLine(x-10, y+10, x+10, y-10);
        
//     }
//     @Override
//     public void mouseReleased(MouseEvent e) {
//     }
// }


/* case 3 : using independence class */
// class MyMouseListener extends MouseAdapter {

//     DrawerFrame frame;

//     MyMouseListener(DrawerFrame frame) {
//         this.frame = frame;
//     }

//     @Override
//     public void mousePressed(MouseEvent e) {
//         int x = e.getX();
//         int y = e.getY();
//         Graphics g = frame.getGraphics();
//         /* draw x sign */
//         g.drawLine(x-10, y-10, x+10, y+10);
//         g.drawLine(x-10, y+10, x+10, y-10);
        
//     }
// }

class DrawerFrame extends JFrame {
    DrawerFrame() {
        setTitle("title");
        setSize(700, 500);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* case 1: */
        /* case 3: */
        // this.addMouseListener(new MyMouseListener(this));
        
        /* case 2: frame implements interface */
        /* case 4: anonymous obj and listener*/
        /* case 5: anonymous obj and adapter */

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Graphics g = getGraphics(); // notice : this.getGraphics(); // error
                /* draw x sign */
                g.drawLine(x-10, y-10, x+10, y+10);
                g.drawLine(x-10, y+10, x+10, y-10);
            }
        });
    }
}

        // this.addMouseListener(new MouseListener() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         System.out.println("clicked");
        //     }
        //     @Override
        //     public void mouseEntered(MouseEvent e) {
        //     }
        //     @Override
        //     public void mouseExited(MouseEvent e) {
        //     }
        //     @Override
        //     public void mousePressed(MouseEvent e) {
        //         int x = e.getX();
        //         int y = e.getY();
        //         Graphics g = getGraphics(); // notice : this.getGraphics(); // error
        //         /* draw x sign */
        //         g.drawLine(x-10, y-10, x+10, y+10);
        //         g.drawLine(x-10, y+10, x+10, y-10);
                
        //     }
        //     @Override
        //     public void mouseReleased(MouseEvent e) {
        //     }
        // });
    /* case 2: */
    // @Override
    // public void mouseClicked(MouseEvent e) {
    //     System.out.println("clicked");
    // }
    // @Override
    // public void mouseEntered(MouseEvent e) {
    // }
    // @Override
    // public void mouseExited(MouseEvent e) {
    // }
    // @Override
    // public void mousePressed(MouseEvent e) {
    //     int x = e.getX();
    //     int y = e.getY();
    //     Graphics g = this.getGraphics();
    //     /* draw x sign */
    //     g.drawLine(x-10, y-10, x+10, y+10);
    //     g.drawLine(x-10, y+10, x+10, y-10);
        
    // }
    // @Override
    // public void mouseReleased(MouseEvent e) {
    // }

class TestDriver {
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
