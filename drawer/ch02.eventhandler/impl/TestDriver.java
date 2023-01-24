package drawer.ch02.eventhandler.impl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;

import javax.swing.JFrame;
/*
class MyMouseListener implements MouseListener { // or extends MouseAdapter

    DrawerFrame frame;
    public MyMouseListener(DrawerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
*/

class DrawerFrame extends JFrame {
    DrawerFrame() {
        setTitle("title");
        setSize(700, 500);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(new MouseListenerImplementation());
    }

    /* inner class, nested class */
    private final class MouseListenerImplementation implements MouseListener { // or extends MouseAdapter
    
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
    }

}

class TestDriver {
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
