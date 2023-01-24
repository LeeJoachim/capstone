package drawer.ch02.eventhandler.anonymous;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/* anonymous object
 * 
 * new MouseListener() { }; -> compiler checks if semicolon is absent
 * i.d. a newly-syntax
 */
class DrawerFrame extends JFrame {
    DrawerFrame() {
        setTitle("title");
        setSize(700, 500);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Graphics g = getGraphics();
                /* draw x sign */
                g.drawLine(x-10, y-10, x+10, y+10);
                g.drawLine(x-10, y+10, x+10, y-10);
            }    
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

        });
    }
}

class TestDriver {
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
