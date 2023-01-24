// TestDriver.java

// {{MODELER_BEFORE_CLASS(TestDriver) - DO NOT DELETE THIS LINE
// et cetera. 

// Drawer 1.0 for Macintosh
// Organization: Busan Univ. of Foreign Studies
// Author: Lee Joachim
// Date: Friday, January 13, 2023
// Super class:
// Purpose of Drawer class:
// Version: 1.0
/**
*/

// }}MODELER_BEFORE_CLASS

package drawer.ch27.icon;
/* 1. Graphics Object for window */
/* 2. Graphics Object for print */
/* 3. Graphics Object for image */

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.PanelUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import apple.laf.JRSUIConstants.Widget;

import java.io.*;

class FigureIcon implements Icon {
    static int WIDTH = 16;
    static int HEIGHT = 16;
    String figureType;
    FigureIcon(String figureType) {
        this.figureType = figureType;
    }
    @Override
    public int getIconHeight() {
        return WIDTH;
    }
    @Override
    public int getIconWidth() {
        return HEIGHT;
    }
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        if (figureType.equals(DrawerView.figureNames[0])) { // Point
            g.drawOval(x+WIDTH/2-1, y+HEIGHT/2-1, 2, 2);
            g.fillOval(x+WIDTH/2-1, y+HEIGHT/2-1, 2, 2);
        } else if (figureType.equals(DrawerView.figureNames[1])) { // box
            g.drawRect(x, y, WIDTH, HEIGHT);
        } else if (figureType.equals(DrawerView.figureNames[2])) { // line
            g.drawLine(x, y, x+WIDTH, y+HEIGHT);
        } else if (figureType.equals(DrawerView.figureNames[3])) { // circle
            g.drawOval(x, y, WIDTH, HEIGHT);
        } else if (figureType.equals(DrawerView.figureNames[4])) { // kite
            g.drawLine(x,y,x+WIDTH,y+HEIGHT);
			g.drawLine(x+WIDTH/2,y,x+WIDTH/2,y+HEIGHT);
			g.drawLine(x+WIDTH,y,x,y+HEIGHT);
			g.drawLine(x,y+HEIGHT/2,x+WIDTH,y+HEIGHT/2);
			g.drawOval(x+WIDTH/4,y+HEIGHT/4,WIDTH/2,HEIGHT/2);
			g.drawRect(x,y,WIDTH,HEIGHT);
        } else if (figureType.equals(DrawerView.figureNames[5])) { // Text
			Font oldFont = g.getFont();
			g.setFont(new Font("monospace",Font.PLAIN,20));
			g.drawString("T",x+1,y+15);
			g.setFont(oldFont);
		} else { // Undefined
			Font oldFont = g.getFont();
			g.setFont(new Font("monospace",Font.BOLD,20));
			g.drawString("?",x+1,y+15);
			g.setFont(oldFont);
		}   
    }
}
/* MVC Model */
class TreeDialog extends JDialog {
    DrawerView view;

    /* Model */
    class FigureTreeModel implements TreeModel {
        DrawerView v = TreeDialog.this.view; 
        ArrayList<Figure> figures;
        DefaultMutableTreeNode root;

        FigureTreeModel() {
            figures = v.getFigures();
            constructTree();
        }
        public void constructTree() {
            root = new DefaultMutableTreeNode("Figures");
            ArrayList<String> list = new ArrayList<String>();
            DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[DrawerView.figureNames.length];

            for (int i = 0; i < DrawerView.figureNames.length; i++) {
                String s = DrawerView.figureNames[i];
                list.add(s);
                DefaultMutableTreeNode n = new DefaultMutableTreeNode(s);
                nodes[i] = n;

                this.root.add(n);
            }
            for (Figure i : figures) {
                String name = i.getName();
                int index = list.indexOf(name);
                
                nodes[index].add(new DefaultMutableTreeNode(i));
            }
            
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Object getChild(Object parent, int index) {
            DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)parent;
            return pNode.getChildAt(index);
        }

        @Override
        public int getChildCount(Object parent) {
            DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)parent;
            return pNode.getChildCount();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)parent;
            DefaultMutableTreeNode cNode = (DefaultMutableTreeNode)child;
            return pNode.getIndex(cNode);
        }

        @Override
        public Object getRoot() {
            return root;
        }

        @Override
        public boolean isLeaf(Object node) {
            DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)node;
            return pNode.isLeaf();
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            // TODO Auto-generated method stub
            
        }
        
    }

    /* View */
    /* decorate screen */
    /* data interaction through updateUI */
    class FigureTree extends JTree {
        FigureTreeModel model;

        FigureTree() {
            super();
            model = new FigureTreeModel();
            setModel(model);
        }
        /* delegation */
        @Override
        public void updateUI() {
            if (model == null) return;
            model.constructTree();
            super.updateUI();
            /* find root: row_0 and then expand */
            /* locate bottom */
            setExpandedState(getPathForRow(0), true); // essential
            
            /* expanded everyone */
            int length = DrawerView.figureNames.length;
            for (int i = length; i > 0; i--) { // inverse and operate
                setExpandedState(getPathForRow(i), true);
            }
            /**/
        }
    }

    /* event handler - actionPerformed = Controller */
    /* data retouching */
    class DialogPanel extends JPanel implements ActionListener {
        JButton remove;
        JButton done;
        FigureTree tree;

        DialogPanel() {
            setLayout(new BorderLayout());

            tree = new FigureTree();
            JScrollPane sp = new JScrollPane(tree);
            add(sp, BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.add(remove = new JButton("remove"));
            bottom.add(done = new JButton("done"));
            add(bottom, BorderLayout.SOUTH);

            done.addActionListener(this);
            remove.addActionListener(this);
        }
        /* delegation */
        public void updateUI() {
            if (tree == null) return;
            tree.updateUI();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == done) { // get ref
                TreeDialog.this.setVisible(false);
            } else if (e.getSource() == remove) {
                TreePath selectedPath = tree.getSelectionPath();
                if (selectedPath == null) return; // defensive
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
                Object selectedObject = selectedNode.getUserObject();
                if (selectedObject instanceof Figure) { // in the case of references sent, not toString
                    view.removeFigure((Figure)selectedObject);
                    updateUI();
                }
            }
        }
    }
    
    TreeDialog(String title, DrawerView view) {
        super((Frame)null, title);
        this.view = view;
        setLocation(200, 300);
        setSize(400, 300);
        
        Container container = getContentPane();
        JPanel dialogPanel = new DialogPanel();
        container.add(dialogPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                dialogPanel.updateUI();
            }
        });
    }
}

class TableDialog extends JDialog {
    DrawerView view;

    class FigureTableModel implements TableModel {
        DrawerView v = TableDialog.this.view;
        ArrayList<Figure> figures;

        static final String[] columnNames = new String[] {
            "Figure Type", "x1", "y1", "x2", "y2"
        };
        static final Class[] columnTypes = new Class[] {
            String.class, Integer.class, Integer.class, Integer.class, Integer.class,
        };

        FigureTableModel() {
            figures = v.getFigures();
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            // TODO Auto-generated method stub   
        }
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public int getRowCount() {
            return figures.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Figure ptr = figures.get(rowIndex);
            switch(columnIndex) {
                case 0 : return ptr.getName();
                case 1 : return ptr.getX1();
                case 2 : return ptr.getY1();
                case 3 : return (ptr.getX2() > 0) ? ptr.getX2() : null;
                case 4 : return (ptr.getY2() > 0) ? ptr.getY2() : null;
                default : return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            // TODO Auto-generated method stub
            
        }
        
    }

    class FigureTable extends JTable {
        FigureTable() {
            // super(new FigureTableModel());
            super();
            setModel(new FigureTableModel());
            
            DefaultListSelectionModel s
                    = new DefaultListSelectionModel(); // what is selected?
            setSelectionModel(s);

            /* Model : managing abstract informations */
            /* using CellRenderer */

            DefaultTableCellRenderer rendererLeft = new DefaultTableCellRenderer(); // call render or draw
            rendererLeft.setHorizontalAlignment(SwingConstants.LEFT);

            // TableColumnModel columnModel = getColumnModel();
            // TableColumn nameColumn = columnModel.getColumn(0);
            // nameColumn.setCellRenderer(rendererLeft);

            getColumnModel().getColumn(0).setCellRenderer(rendererLeft);
            getColumnModel().getColumn(1).setCellRenderer(rendererLeft);
            getColumnModel().getColumn(2).setCellRenderer(rendererLeft);
            getColumnModel().getColumn(3).setCellRenderer(rendererLeft);
            getColumnModel().getColumn(4).setCellRenderer(rendererLeft);
        }
        int getSelectedIndex() {
            return selectionModel.getLeadSelectionIndex();
        }
    }

    class DialogPanel extends JPanel implements ActionListener {
        JButton remove;
        JButton done;
        FigureTable table;

        DialogPanel() {
            setLayout(new BorderLayout());

            table = new FigureTable();
            JScrollPane sp = new JScrollPane(table);
            add(sp, BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.add(remove = new JButton("remove"));
            bottom.add(done = new JButton("done"));
            add(bottom, BorderLayout.SOUTH);

            done.addActionListener(this);
            remove.addActionListener(this);
        }
        /* delegation */
        public void updateUI() {
            if (table == null) return;
            table.updateUI();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == done) { // get ref
                TableDialog.this.setVisible(false);
            } else if (e.getSource() == remove) {
                if (table.getSelectedIndex() == -1) return; // defensive
                view.removeFigure(table.getSelectedIndex());
                updateUI();
            }
        }
    }
    TableDialog(String title, DrawerView view) {
        super((Frame)null, title);
        this.view = view;
        setLocation(200, 300);
        setSize(400, 300);
        
        Container container = getContentPane();
        JPanel dialogPanel = new DialogPanel();
        container.add(dialogPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                dialogPanel.updateUI();
            }
        });
    }
}

class FigureDialog extends JDialog {
    DrawerView view; // reference for : DialogPanel draws lines on view;     
    class DialogPanel extends JPanel implements ActionListener {

        JTextField x1Field;
        JTextField y1Field;
        JTextField x2Field;
        JTextField y2Field;
        JComboBox<String> figures;

        JRadioButton blackButton;
        JRadioButton redButton;
        JRadioButton greenButton;
        JRadioButton blueButton;
        JRadioButton chooserButton;

        Color selectedColor;

        DialogPanel() {
            this.setLayout(new GridLayout(9, 2)); // grid layout

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
            JLabel space1 = new JLabel("");
            add(space1);
            /**/

            /* JComboBox {rectangle, line} */
            figures = new JComboBox<String>(DrawerView.figureNames);
            add(figures);
            /**/

            /* color buttons */
            ButtonGroup buttonGroup = new ButtonGroup();
            blackButton = new JRadioButton("Black"); 
            blackButton.setHorizontalAlignment(SwingConstants.LEFT);
            blackButton.setSelected(true); // default
            blackButton.addActionListener((e) -> {
                selectedColor = Color.BLACK;
            });
            add(blackButton);
            buttonGroup.add(blackButton);

            redButton = new JRadioButton("Red");
            redButton.setHorizontalAlignment(SwingConstants.LEFT);
            redButton.addActionListener((e) -> {
                selectedColor = Color.RED;
            });
            add(redButton);
            buttonGroup.add(redButton);

            greenButton = new JRadioButton("Grren");
            greenButton.setHorizontalAlignment(SwingConstants.LEFT);
            greenButton.addActionListener((e) -> {
                selectedColor = Color.GREEN;
            });
            add(greenButton);
            buttonGroup.add(greenButton);
            
            blueButton = new JRadioButton("Blue");
            blueButton.setHorizontalAlignment(SwingConstants.LEFT);
            blueButton.addActionListener((e) -> {
                selectedColor = Color.BLUE;
            });
            add(blueButton);
            buttonGroup.add(blueButton);
            
            chooserButton = new JRadioButton("Chooser");
            chooserButton.setHorizontalAlignment(SwingConstants.LEFT);
            chooserButton.addActionListener((e) -> {
                selectedColor = JColorChooser.showDialog(null, "Color Chooser", Color.BLACK);
            });
            add(chooserButton);
            buttonGroup.add(chooserButton);

            /**/
            /* space */
            JLabel space2 = new JLabel("");
            add(space2);
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

            Figure selectedFigure = null;
            if (selection.equals("point")) {
                selectedFigure = new Point(selectedColor, x1, y1);
                selectedFigure.setPopup(view.getPointPopup()); 
            } else if (selection.equals("rectangle")) {
                selectedFigure = new Rectangle(selectedColor, x1, y1, x2, y2);
                selectedFigure.setPopup(view.getRectPopup()); 
            } else if (selection.equals("line")) {
                selectedFigure = new Line(selectedColor, x1, y1, x2, y2);
                selectedFigure.setPopup(view.getLinePopup()); 
            } else if (selection.equals("circle")) {
                selectedFigure = new Circle(selectedColor, x1, y1, x2, y2);
                selectedFigure.setPopup(view.getCirclePopup());   
            } else if (selection.equals("kite")) {
                selectedFigure = new Kite(selectedColor, x1, y1, x2, y2);
                selectedFigure.setPopup(view.getKitePopup());
            }
            /* make region and add collection obj and repaint*/
            view.addFigures(selectedFigure);
            /**/
            /* Inputted text reset "" */
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

        setSize(200, 250);
        setResizable(false);
    }
}
/** functions : drawing, cursor is in polygon? */
abstract class Figure implements Serializable {

    String name = "Figure";

    static final int MOVE_X = 20;
    static final int MOVE_Y = 20;

    Polygon region;
    transient Popup selectedPopup; // do not save or load this ref
    Color color;

    boolean canFill;

    Figure(Color color) {
        this.color = color;
    }
    void showPopup(JPanel component, int x, int y) {
        /* delegation */
        selectedPopup.show(component, x, y);
    }
    abstract void draw(Graphics g);
    abstract void setX2Y2(int x2, int y2);
    abstract void makeRegion();
    abstract void move(int dx, int dy);
    abstract Figure getCopiedFigure();
    void setFillFlag() {
        canFill = false;
    }

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
    void moveXOR(Graphics g, int dx, int dy) {
        draw(g); // disappeared by XOR
        move(dx, dy); // add delta x,y to data members
        draw(g); // coordinates changed. finally draw
    }
    void setPopup(Popup selectedPopup) {
        this.selectedPopup = selectedPopup;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public String getName() {
        return name;
    }

    /* hook and default */
    int getX1() {return -1;}
    int getY1() {return -1;}
    int getX2() {return -1;}
    int getY2() {return -1;}

    @Override
    public String toString() {
        String s = getName() + " [" + getX1() + ", " + getY1();
        if (getX2() < 0) {
            s += "]";
            return s;
        }
        s += ", " + getX2() + ", " + getY2() + "]";
        return s;
    }
}
abstract class OnePointFigure extends Figure {
    int x1;
    int y1;
    
    int GAP = 3;

    OnePointFigure(Color color, int nowX, int nowY) {
        super(color);
        x1 = nowX;
        y1 = nowY;
    }

    void setX1(int x1) {
        this.x1 = x1;
    }
    void setY1(int y1) {
        this.y1 = y1;
    }
    @Override
    void setX2Y2(int x1, int y1) {
        setX1(x1); setY1(y1);
    }
    
    
    @Override
    void makeRegion() {

        /* region mechanism */

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        xPoints[0] = x1-GAP;
        yPoints[0] = y1-GAP;
        xPoints[1] = x1+GAP;
        yPoints[1] = y1-GAP;
        xPoints[2] = x1+GAP;
        yPoints[2] = y1+GAP;
        xPoints[3] = x1-GAP;
        yPoints[3] = y1+GAP;
        
        /* send coordinates for make polygon box */
        region = new Polygon(xPoints, yPoints, 4);
        /**/
    }

    /* parallel translation */
    void move(int dx, int dy) {
        x1 += dx;
        y1 += dy;
    }

    @Override
    public int getX1() {
        return x1;
    }
    @Override
    public int getY1() {
        return y1;
    }
}

class Point extends OnePointFigure {
    String name = "Point";

    Point(Color color, int nowX, int nowY) {
        super(color, nowX, nowY);
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(x1-GAP, y1-GAP, 2*GAP, 2*GAP);
        g.fillOval(x1-GAP, y1-GAP, 2*GAP, 2*GAP);        
    }

    /* data access functions */
    @Override
    Figure getCopiedFigure() {
        Point p = new Point(color, x1, y1);
        p.setPopup(this.selectedPopup);
        p.move(MOVE_X, MOVE_Y);
        return p;
    }
    @Override
    void setFillFlag() {
        canFill = false;
    }
    public String getName() {
        return name;
    }
    /**/    
}

/** has Coordinates values, data access functions, make region */
abstract class TwoPointFigure extends Figure {
    int x1;
    int y1;
    int x2;
    int y2;

    public TwoPointFigure(Color color, int nowX, int nowY) {
        super(color);
        x1 = x2 = nowX;
        y1 = y2 = nowY;
    }
    
    public TwoPointFigure(Color color, int x1, int y1, int x2, int y2) {
        super(color);
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
    void setX1Y1X2Y2 (int x1, int y1, int x2, int y2) {
        this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
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

    /* parallel translation */
    void move(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }

    @Override
    public int getX1() {
        return x1;
    }
    @Override
    public int getY1() {
        return y1;
    }
    @Override
    public int getX2() {
        return x2;
    }
    @Override
    public int getY2() {
        return y2;
    }
}

class Rectangle extends TwoPointFigure {

    String name = "Rectangle";


    Rectangle(Color color, int nowX, int nowY) {
        super(color, nowX, nowY);
    }
    public Rectangle(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
    }

    void draw(Graphics g) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int width = Math.abs(x1-x2);
        int height = Math.abs(y1-y2);
        g.setColor(color);
        g.drawRect(minX, minY, width, height);

        if (canFill)
            g.fillRect(minX+1, minY+1, width-1, height-1);
    }

    /* data access functions */
    @Override
    Figure getCopiedFigure() {
        Rectangle r = new Rectangle(color, x1, y1, x2, y2);
        r.move(MOVE_X, MOVE_Y);
        r.setPopup(this.selectedPopup);
        r.canFill = this.canFill;
        return r;
    }
    @Override
    void setFillFlag() {
        canFill = true;
    }
    @Override
    public String getName() {
        return name;
    }
    /**/
}
class Text extends Rectangle {
    String[] lines;

    Text(Color color, int x1, int y1, int x2, int y2, String[] lines) {
        super(color, x1, y1, x2, y2);
        this.lines = lines;
    }
    @Override
    public void setColor(Color color) {
        super.setColor(color);
    }
    @Override
    void draw(Graphics g) {
        super.draw(g);
        g.setColor(color);
        
        Font f = g.getFont();
        FontMetrics fm = g.getFontMetrics(f);
        int height = fm.getHeight();
        int ascent = fm.getAscent();
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], x1, y1+(ascent+i*height));
        }
    }

    Figure getCopiedFigure() {
        Text newText = new Text(color, x1, y1, x2, y2, lines);
        newText.setPopup(selectedPopup);
        newText.move(MOVE_X, MOVE_Y);
        return newText;
    }


}
class Line extends TwoPointFigure {

    String name = "Line";

    Line(Color color, int nowX, int nowY) {
        super(color, nowX, nowY);
    }
    public Line(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
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
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }
    @Override
    Figure getCopiedFigure() {
        Line l = new Line(color, x1, y1, x2, y2);
        l.move(MOVE_X, MOVE_Y);
        l.setPopup(this.selectedPopup);
        return l;
    }
    @Override
    public String getName() {
        return name;
    }

}
class Circle extends TwoPointFigure {

    String name = "Circle";

    Circle(Color color, int nowX, int nowY) {
        super(color, nowX, nowY);
    }
    public Circle(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
    }
    void draw(Graphics g) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int width = Math.abs(x1-x2);
        int height = Math.abs(y1-y2);
        g.setColor(color);
        g.drawOval(minX, minY, width, height);

        if (canFill)
            g.fillOval(minX, minY, width, height);
    }

    /* data access functions */
    @Override
    Figure getCopiedFigure() {
        Circle c = new Circle(color, x1, y1, x2, y2);
        c.move(MOVE_X, MOVE_Y);
        c.setPopup(this.selectedPopup);
        c.canFill = this.canFill;
        return c;
    }
    @Override
    void setFillFlag() {
        canFill = true;
    }
    @Override
    public String getName() {
        return name;
    }
    /**/
}
class Kite extends Rectangle {

    String name = "Kite";
    Circle circle;
    Line line1;
    Line line2;
    Line line3;
    Line line4;
    ArrayList<Figure> children = new ArrayList<Figure>(); // pattern

    Kite(Color color, int nowX, int nowY) {
        super(color, nowX, nowY);
        circle = new Circle(color, nowX, nowY);
        line1 = new Line(color, nowX, nowY);
        line2 = new Line(color, nowX, nowY);
        line3 = new Line(color, nowX, nowY);
        line4 = new Line(color, nowX, nowY);

        children.add(circle);
        children.add(line1);
        children.add(line2);
        children.add(line3);
        children.add(line4);
    }
    public Kite(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
        int w = Math.abs(x2-x1);
        int h = Math.abs(y2-y1);
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        circle = new Circle(color, x+w/4, y+h/4, x+3*w/4, y+3*h/4);
        line1 = new Line(color, x1, y1, x2, y2);
        line2 = new Line(color, x1+w/2, y1, x1+w/2, y2);
        line3 = new Line(color, x2, y1, x1, y2);
        line4 = new Line(color, x1, y1+h/2, x2, y1+h/2);

        children.add(circle);
        children.add(line1);
        children.add(line2);
        children.add(line3);
        children.add(line4);
    }
    @Override
    public void setColor(Color color) {
        super.setColor(color);

        for (Figure i : children) {
            i.setColor(color);
        }
        /*
        circle.setColor(color);
        line1.setColor(color);
        line2.setColor(color);
        line3.setColor(color);
        line4.setColor(color);
        */
    }
    @Override
    void setX2Y2(int x2, int y2) {
        super.setX2Y2(x2, y2);
        int w = Math.abs(x2-x1);
        int h = Math.abs(y2-y1);
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        circle.setX1Y1X2Y2(x+w/4, y+h/4, x+3*w/4, y+3*h/4);
        line1.setX1Y1X2Y2(x1, y1, x2, y2);
        line2.setX1Y1X2Y2(x+w/2, y, x+w/2, y+h);
        line3.setX1Y1X2Y2(x2, y1, x1, y2);
        line4.setX1Y1X2Y2(x, y+h/2, x+w, y+h/2);
    }
    @Override
    void draw(Graphics g) {
        super.draw(g);
        for (Figure i : children) {
            i.draw(g);
        }
        // circle.draw(g);
        // line1.draw(g);
        // line2.draw(g);
        // line3.draw(g);
        // line4.draw(g);
    }

    @Override
    void move(int dx, int dy) {
        super.move(dx, dy);
        for (Figure i : children) {
            i.move(dx, dy);
        }
        // circle.move(dx, dy);
        // line1.move(dx, dy);
        // line2.move(dx, dy);
        // line3.move(dx, dy);
        // line4.move(dx, dy);
    }

    /* data access functions */
    @Override
    Figure getCopiedFigure() {
        Kite k = new Kite(color, x1, y1, x2, y2);
        k.move(MOVE_X, MOVE_Y);
        k.setPopup(this.selectedPopup);
        k.circle.canFill = this.circle.canFill;
        return k;
    }
    @Override
    void setFillFlag() {
        circle.setFillFlag();
    }
    public String getName() {
        return name;
    }
    /**/
}

class SelectAction extends AbstractAction {
    DrawerView view;

    SelectAction(String name, Icon icon, DrawerView view, int id) {
        putValue(Action.NAME, name);
        putValue(Action.SMALL_ICON, icon);
        putValue("id", id);
        this.view = view;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int id = (int)getValue("id");
        view.setWhatToDraw(id);
        
    } 
}

class Popup implements Serializable {

    JPopupMenu popupMenu;
    
    Popup() {
    }
    Popup(String title) {
        popupMenu = new JPopupMenu();
        popupMenu.add(title); // just label
        popupMenu.addSeparator();
    }
    void show(JPanel component, int x, int y) {
        popupMenu.show(component, x, y);
    } 
}

class MainPopup extends Popup {
    public MainPopup(DrawerView view) {
        super("Figure");

        // JMenuItem pointItem = new JMenuItem("Point (P)");
        // pointItem.addActionListener((e) -> {
        //     view.setWhatToDraw(DrawerView.ID_POINT);
        // });
        // popupMenu.add(pointItem);

        JMenuItem pointItem = new JMenuItem(view.getPointAction());
        popupMenu.add(pointItem);
        JMenuItem rectItem = new JMenuItem(view.getRectAction());
        popupMenu.add(rectItem);
        JMenuItem lineItem = new JMenuItem(view.getLineAction());
        popupMenu.add(lineItem);
        JMenuItem circleItem = new JMenuItem(view.getCircleAction());
        popupMenu.add(circleItem);
        JMenuItem kiteItem = new JMenuItem(view.getKiteAction());
        popupMenu.add(kiteItem);
        JMenuItem textItem = new JMenuItem(view.getTextAction());
        popupMenu.add(textItem);
    }
}

class FigurePopup extends Popup {

    public FigurePopup(DrawerView view, String title, boolean canFillButton) {
        super(title);
        JMenuItem deleteItem = new JMenuItem("Delete (D)");
        deleteItem.addActionListener((e) -> view.deleteFigure());
        popupMenu.add(deleteItem);
        
        JMenuItem copyItem = new JMenuItem("Copy (C)");
        copyItem.addActionListener((e) -> view.copyFigure());
        popupMenu.add(copyItem);

        /* color */
        JMenu colorMenu = new JMenu("Color");
        popupMenu.add(colorMenu);

        JMenuItem blackItem = new JMenuItem("Black");
        blackItem.addActionListener((e) -> view.setColorAndRepaint(Color.BLACK));
        colorMenu.add(blackItem);
        JMenuItem redItem = new JMenuItem("Red");
        redItem.addActionListener((e) -> view.setColorAndRepaint(Color.RED));
        colorMenu.add(redItem);
        JMenuItem greenItem = new JMenuItem("Green");
        greenItem.addActionListener((e) -> view.setColorAndRepaint(Color.GREEN));
        colorMenu.add(greenItem);
        JMenuItem blueItem = new JMenuItem("Blue");
        blueItem.addActionListener((e) -> view.setColorAndRepaint(Color.BLUE));
        colorMenu.add(blueItem);
        JMenuItem chooseItem = new JMenuItem("Chooser");
        chooseItem.addActionListener((e) -> view.showColorChooser());
        colorMenu.add(chooseItem);
        /**/

        /* fill */
        if (canFillButton) {
            JMenuItem fillItem = new JMenuItem("Fill (F)");
            fillItem.addActionListener((e) -> view.fillFigure());
            popupMenu.add(fillItem);
        }
    }
}
/** Everything that happens in the Panel */
class DrawerView extends JPanel implements MouseListener, MouseMotionListener {

    class TextEditor extends JTextArea implements MouseListener, DocumentListener {
        int INIT_WIDTH = 100;
        int INIT_HEIGHT = 150;
        int DELTA = 30;
        Font font;
        FontMetrics fm;
        DrawerView view = DrawerView.this;

        int x, y, width, height;

        TextEditor() {
            super();
            setBackground(view.getBackground());
            getDocument().addDocumentListener(this);
        }

        void start(int x, int y) {
            setText("");
            font = getFont();
            fm = view.getGraphics().getFontMetrics();
            this.x = x;
            this.y = y;
            this.width = INIT_WIDTH;
            this.height = INIT_HEIGHT;

            setBounds(x, y, width, height);
            Graphics g = view.getGraphics();
            g.setColor(Color.BLUE);
            g.drawRect(x, y, INIT_WIDTH-2, INIT_HEIGHT);
            setBorder(BorderFactory.createLineBorder(Color.BLUE));
            setCaretPosition(0); // Caret : blinking prompt

            view.removeMouseListener(view);
            view.removeMouseMotionListener(view);

            view.add(this);
            requestFocus(); // ready input text

            view.addMouseListener(this);
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
            view.remove(this);
            view.removeMouseListener(this);
            view.addMouseListener(view);
            view.addMouseMotionListener(view);

            String text = getText();
            String[] lines = text.split("\n");
            int w;

            if (lines.length == 1 && lines[0].equals("")) return;

            int maxWidth = 0;
            for (int i = 0; i < lines.length; i++) {
                String s = lines[i];
                w = fm.stringWidth(s);
                if (w > maxWidth) maxWidth = w;
            }
            int maxHeight = lines.length * fm.getHeight();

            Text newFigure = new Text(Color.black, x, y, x+maxWidth, y+maxHeight, lines);
            newFigure.setPopup(view.getTextPopup());
            view.addFigures(newFigure);
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            String text = getText();
            String[] lines = text.split("\n");

            int w;
            int maxWidth = 0;
            for (int i = 0; i < lines.length; i++) {
                String s = lines[i];
                w = fm.stringWidth(s);
                if (w > maxWidth) maxWidth = w;
            }
            if (maxWidth > width) {
                width += DELTA;
                setBounds(x, y, width, height);
            }
            int maxHeight = lines.length * fm.getHeight();
            if (maxHeight > height) {
                height += DELTA;
                setBounds(x, y, width, height);
            }
        }


        @Override
        public void removeUpdate(DocumentEvent e) {
            // TODO Auto-generated method stub
            
        }
        

    }
    /* Attributes */
    static String[] figureNames = {"Point", "Rectangle", "Line", "Circle", "Kite", "Text"};

    static ArrayList<String> figureNamesList = new ArrayList<String>();
    /* static initializer */
    /* after compile, called when DrawerView class loaded memory */
    static {
        for (int i = 0; i < figureNames.length; i++) {
            figureNamesList.add(figureNames[i]);
        }
    }

    /**/
    static final int INIT_WIDTH = 3000;
    static final int INIT_HEIGHT = 1500;

    /* MACRO NUMBERS : flag-whatToDraw */
    static final int ID_POINT = 1;
    static final int ID_RECTANGLE = 2;
    static final int ID_LINE = 3;    
    static final int ID_CIRCLE = 4;    
    static final int ID_KITE = 5;    
    static final int ID_TEXT = 6;
    /* MACRO NUMBERS : flag-actionMode */
    static final int NOTHING = 0;    
    static final int DRAWING = 1;    
    static final int MOVING = 2;    
    /* flag */
    int whatToDraw;
    int actionMode;
    boolean isInFigurePopup = false;
    /* current figure */
    Figure selectedFigure;
    /* polymorphic collection obj */
    /* polymorphic container */
    ArrayList<Figure> figures = new ArrayList<Figure>();
    /**/

    /* coordinates for moving
     * for find values of delta
     */
    int xInPG;
    int yInPG;
    /**/

    SelectAction pointAction;
    SelectAction rectAction;
    SelectAction lineAction;
    SelectAction circleAction;
    SelectAction kiteAction;
    SelectAction textAction;

    Popup mainPopup;
    Popup pointPopup;
    Popup rectPopup;
    Popup linePopup;
    Popup circlePopup;
    Popup kitePopup;
    Popup textPopup;

    Popup[] popups = new Popup[figureNames.length];


    /* when you choose color if you want to sustain color and make figures */
    // Color sustainedColor
    Color defaultColor = Color.BLACK;
    /**/

    int canvasWidth = INIT_WIDTH;
    int canvasHeight = INIT_HEIGHT;

    double zoomRatio = 1.0;

    TextEditor textEditor;

    DrawerFrame drawerFrame;

    DrawerView(DrawerFrame drawerFrame) {

        setLayout(null); // deactivate flow layout

        this.drawerFrame = drawerFrame;
        super.addMouseListener(this);
        super.addMouseMotionListener(this);

        textEditor = new TextEditor();

        pointAction = new SelectAction("Point (P)", new FigureIcon(figureNames[0]), this, DrawerView.ID_POINT);
        rectAction = new SelectAction("Rectangle (R)", new FigureIcon(figureNames[1]), this, DrawerView.ID_RECTANGLE);
        lineAction = new SelectAction("Line (L)", new FigureIcon(figureNames[2]), this, DrawerView.ID_LINE);
        circleAction = new SelectAction("Circle (C)", new FigureIcon(figureNames[3]), this, DrawerView.ID_CIRCLE);
        kiteAction = new SelectAction("Kite (K)", new FigureIcon(figureNames[4]), this, DrawerView.ID_KITE);
        textAction = new SelectAction("Text (T)", new FigureIcon(figureNames[5]), this, DrawerView.ID_TEXT);

        /* for economize memory,
         * for manage repeated instantiation
         */
        mainPopup = new MainPopup(this); // address for setWhatToDraw()
        pointPopup = new FigurePopup(this, "Point", false);
        rectPopup = new FigurePopup(this, "Rectangle", true);
        linePopup = new FigurePopup(this, "Line", false);
        circlePopup = new FigurePopup(this, "Circle", true);
        kitePopup = new FigurePopup(this, "Kite", true);
        textPopup = new FigurePopup(this, "Text", false);

        int i = 0;
        popups[i++] = pointPopup;
        popups[i++] = rectPopup;
        popups[i++] = linePopup;
        popups[i++] = circlePopup;
        popups[i++] = kitePopup;
        popups[i++] = textPopup;
        /* canvas size not panel */
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D)g).scale(zoomRatio, zoomRatio);

        for (Figure i : figures) {
            i.draw(g);
        }
    }
    void zoom(int ratio) {
        zoomRatio = (double)ratio/100.0;
        repaint(); // goto paintComponent(Graphics g)

        /* event handler deactivated */
        removeMouseListener(this);
        removeMouseMotionListener(this);

        /* event handler activated */
        if (ratio == 100) {
            addMouseListener(this);
            addMouseMotionListener(this);        
        }
    }

    public void setWhatToDraw(int whatToDraw) {
        this.whatToDraw = whatToDraw;
        drawerFrame.setFigureType(DrawerView.figureNames[whatToDraw-1]);
    }
    /** make region -> figures.add -> repaint */
    public void addFigures(Figure selectedFigure) {
        selectedFigure.makeRegion();
        figures.add(selectedFigure);
        repaint(); // call paintComponent()
    }
    boolean cursorIsInPolygon(int x, int y) {
        for (Figure i : figures) {
            if (i.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
    Figure getPolygon(int x, int y) {
        for (Figure i : figures) {
            if (i.contains(x, y)) {
                return i;
            }
        }
        return null;
    }
    void deleteFigure() {
        /* defensive */
        if (selectedFigure == null) return;
        /**/
        figures.remove(selectedFigure);
        selectedFigure = null;
        repaint();
    }
    void copyFigure() {
        /* defensive */
        if (selectedFigure == null) return;
        /**/
        Figure copiedFigure = selectedFigure.getCopiedFigure();
        addFigures(copiedFigure);
        selectedFigure = copiedFigure;
    }
    void fillFigure() {
        /* defensive */
        if (selectedFigure == null) return;
        /**/
        selectedFigure.setFillFlag();
        repaint();

    }
    void setColorAndRepaint(Color color) {
        /* defensive */
        if (selectedFigure == null) return;
        /**/
        selectedFigure.setColor(color);
        // sustainedColor = color;
        repaint();
    }
    void showColorChooser() {
        Color color = JColorChooser.showDialog(this, "chooser", Color.BLACK);
        setColorAndRepaint(color);
    }
    void removeFigure(int index) {
        /* defensive */
        selectedFigure = null;
        if (index < 0) return;
        /**/
        figures.remove(index);
        repaint();
    }
    void removeFigure(Figure figure) {
        /* defensive */
        selectedFigure = null;
        /**/
        figures.remove(figure);
        repaint();
    }
    void doSave(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(figures); 
            oos.flush(); // print
            oos.close();
            fos.close();
            
        } catch (IOException e) {

        }
    }
    void doOpen(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // ois.writeObject(figures); 
            figures = (ArrayList<Figure>)(ois.readObject());
            ois.close();
            fis.close();

            for (Figure f : figures) {
                String name = f.getName();
                int index = figureNamesList.indexOf(name);
                f.setPopup(popups[index]);
            }
            repaint();
            
        } catch (IOException e) {

        } catch (ClassNotFoundException ex) {

        }
    }
    void doFileNew() {
        figures.clear();
        repaint();
    }

    /* access functions */
    public ArrayList<Figure> getFigures() {
        return figures;
    }
    public Popup getPointPopup() {
        return pointPopup;
    }
    public Popup getRectPopup() {
        return rectPopup;
    }
    public Popup getLinePopup() {
        return linePopup;
    }
    public Popup getCirclePopup() {
        return circlePopup;
    }
    public Popup getKitePopup() {
        return kitePopup;
    }
    public Popup getTextPopup() {
        return textPopup;
    }
    public SelectAction getPointAction() {
        return pointAction;
    }
    public SelectAction getRectAction() {
        return rectAction;
    }
    public SelectAction getLineAction() {
        return lineAction;
    }
    public SelectAction getCircleAction() {
        return circleAction;
    }
    public SelectAction getKiteAction() {
        return kiteAction;
    }
    public SelectAction getTextAction() {
        return textAction;
    }
    /**/

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
        // if (zoomRatio != 1.0) return; 
        
        int x = e.getX();
        int y = e.getY();

        /* PopupTrigger contains 
         * mouse right button or two fingers touch  */
        /* different mechanism each other!
         * case : ms-windows 
         * PopupTrigger exist when mouse released
         * case : macintosh
         * PopupTrigger exist when mouse pressed
         */        
        if (e.isPopupTrigger()) {
            selectedFigure = getPolygon(x, y);
            if (selectedFigure == null) {
                mainPopup.show(this, x, y); // @param : need to parent address
            } else {
                isInFigurePopup = true;
                selectedFigure.showPopup(this, x, y); // selectedPopup.show()
            }
            return;
        }
        /**/

        /* moving sequence */
        if (cursorIsInPolygon(x, y)) {
            selectedFigure = getPolygon(x, y);
            actionMode = MOVING;
            xInPG = x;
            yInPG = y;
            figures.remove(selectedFigure);
            // repaint(); // for XOR doesn't remove original figure
            return;
        }
        /**/

        /* drawing sequence */
        if (whatToDraw == ID_POINT) {   
            actionMode = DRAWING;
            // if (sustainedColor == null) {
            //     selectedFigure = new Rectangle(Color.BLACK, x, y);
            //     selectedFigure.setPopup(rectPopup); 
            //     return;
            // }
            selectedFigure = new Point(defaultColor, x, y);
            selectedFigure.setPopup(pointPopup); 
            return;
        } else if (whatToDraw == ID_RECTANGLE) {   
            actionMode = DRAWING;
            // if (sustainedColor == null) {
            //     selectedFigure = new Rectangle(Color.BLACK, x, y);
            //     selectedFigure.setPopup(rectPopup); 
            //     return;
            // }
            selectedFigure = new Rectangle(defaultColor, x, y);
            selectedFigure.setPopup(rectPopup); 
            return;
        } else if (whatToDraw == ID_LINE) {
            actionMode = DRAWING;
            // if (sustainedColor == null) {
            //     selectedFigure = new Line(Color.BLACK, x, y);
            //     selectedFigure.setPopup(linePopup);
            //     return;
            // }
            selectedFigure = new Line(defaultColor, x, y);
            selectedFigure.setPopup(linePopup);
            return;
        } else if (whatToDraw == ID_CIRCLE) {
            actionMode = DRAWING;
            selectedFigure = new Circle(defaultColor, x, y);
            selectedFigure.setPopup(circlePopup);
            return;
        } else if (whatToDraw == ID_KITE) {
            actionMode = DRAWING;
            selectedFigure = new Kite(defaultColor, x, y);
            selectedFigure.setPopup(kitePopup);
            return;
        } else if (whatToDraw == ID_TEXT) {
            actionMode = NOTHING;
            selectedFigure = null;
            textEditor.start(x,y);
            return;
        } 
        /**/
    }
    @Override
    public void mouseReleased(MouseEvent e) {

        if (isInFigurePopup && selectedFigure != null) {
            isInFigurePopup = false;
            return;
        }
        
        if (selectedFigure != null) { 
            addFigures(selectedFigure);
            selectedFigure = null;
            return;
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        /* defensive */
        /* The popup trigger is unconcerned about the drag. */
        if (e.isPopupTrigger()) return;
        /**/

        /* every drag sequence, coordinates are renewed
         * and then, x, y in POLYGON follow
         * from this, we can obtain amount of moving
         */
        int x = e.getX(); 
        int y = e.getY();

        Graphics g = getGraphics();
        g.setXORMode(getBackground());
        if (actionMode == DRAWING) {
            selectedFigure.drawXOR(g, x, y);
            return;
        } else if (actionMode == MOVING) {
            selectedFigure.moveXOR(g, x-xInPG, y-yInPG); // += dx, dy
            xInPG = x;
            yInPG = y;
            return;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        drawerFrame.setPosition(x + ", " + y + "px");

        if (cursorIsInPolygon(x, y)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            return;
        } else {
            setCursor(Cursor.getDefaultCursor());
            return;
        }
    }
}
class StatusBar extends JPanel {
    JTextField position;
    JTextField figureType;
    JTextField viewSize;

    StatusBar() {
        setLayout(new BorderLayout());
        position = new JTextField("Position", 8);
        position.setEditable(false);
        position.setHorizontalAlignment(JTextField.CENTER);
        
        figureType = new JTextField("Type", 6);
        figureType.setEditable(false);
        figureType.setHorizontalAlignment(JTextField.CENTER);

        viewSize = new JTextField("Size", 12);
        viewSize.setEditable(false);
        viewSize.setHorizontalAlignment(JTextField.CENTER);

        Box westBoxes = Box.createHorizontalBox();
        westBoxes.add(Box.createHorizontalStrut(5));
        westBoxes.add(position);
        westBoxes.add(figureType);
        
        add(westBoxes, "West");
        
        Box eastBoxes = Box.createHorizontalBox();
        eastBoxes.add(viewSize);
        eastBoxes.add(Box.createHorizontalStrut(5));
        
        add(eastBoxes, "East");
        setBorder(BorderFactory.createEtchedBorder());
    }

    /* data access functions */
    public void setPosition(String s) {
        position.setText(s);
    }
    public void setFigureType(String s) {
        figureType.setText(s);
    }
    public void setViewSize(String s) {
        viewSize.setText(s);
    }
    /**/
}
/** Menus && MenuItems have added actionlisteners */
class DrawerFrame extends JFrame {

    DrawerView view;
    StatusBar statusBar;
    FigureDialog dialog;
    TableDialog tableDialog;
    TreeDialog treeDialog;

    String fileName = "noname.jdr"; // jdr extension

    class ZoomBox extends JComboBox implements ActionListener{
        static String[] size = {"100", "80", "50"};
        ZoomBox() {
            super(size);
            setMaximumSize(new Dimension(1000, 1000));
            addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox)e.getSource();
            String ratio = (String)box.getSelectedItem();
            view.zoom(Integer.parseInt(ratio));
        }
        
    }

    class PrintableView implements Printable {
        DrawerView view;
        String fileName;
        
        public PrintableView(DrawerView view, String fileName) {
            this.view = view;
            this.fileName = fileName;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            System.out.println("HERE");

            if (pageIndex > 0) return Printable.NO_SUCH_PAGE; // only print 1-page
            return PAGE_EXISTS;
        }
        
    }


    /* the composition of a screen */
    DrawerFrame() {
        setTitle("Drawer 1.0 for Macintosh - [noname.jdr]");
        
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
        view = new DrawerView(this);
        container.add(view, BorderLayout.CENTER);
        statusBar = new StatusBar();
        container.add(statusBar, BorderLayout.SOUTH);
        JToolBar toolBar = new JToolBar();
        toolBar.add(view.getPointAction());
        toolBar.add(view.getRectAction());
        toolBar.add(view.getLineAction());
        toolBar.add(view.getCircleAction());
        toolBar.add(view.getKiteAction());
        toolBar.add(view.getTextAction());
        toolBar.add(Box.createGlue()); /* space */
        toolBar.add(new ZoomBox());
        container.add(toolBar, BorderLayout.NORTH);
        /* scroll is appeared when canvas size > panel size */
        JScrollPane scrollPane = new JScrollPane(view);
        container.add(scrollPane, BorderLayout.CENTER);

        /* Register Keyboard Action */
        /* when you pressed page down */
        scrollPane.registerKeyboardAction(
                (e) -> { 
                    JScrollBar scrollBar = scrollPane.getVerticalScrollBar(); // vertical
                    scrollBar.setValue(scrollBar.getValue() + scrollBar.getBlockIncrement()); // positive
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100); // changes default 10 to 100

        /* when you pressed page up */
        scrollPane.registerKeyboardAction(
                (e) -> { 
                    JScrollBar scrollBar = scrollPane.getVerticalScrollBar(); // vertical
                    scrollBar.setValue(scrollBar.getValue() - scrollBar.getBlockIncrement()); // negative
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100); // changes default 10 to 100

        /* when you pressed + shift + page down */
        scrollPane.registerKeyboardAction(
                (e) -> { 
                    JScrollBar scrollBar = scrollPane.getHorizontalScrollBar(); // horizontal
                    scrollBar.setValue(scrollBar.getValue() + scrollBar.getBlockIncrement()); // positive
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.SHIFT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(100); // changes default 10 to 100

        /* when you pressed shift + page up */
        scrollPane.registerKeyboardAction(
                (e) -> { 
                    JScrollBar scrollBar = scrollPane.getHorizontalScrollBar(); // horizontal
                    scrollBar.setValue(scrollBar.getValue() - scrollBar.getBlockIncrement()); // negative
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.SHIFT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        scrollPane.getHorizontalScrollBar().setBlockIncrement(100); // changes default 10 to 100
        /**/


        super.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = view.getSize();
                String s = size.width + " x " + size.height + "px";
                statusBar.setViewSize(s);
            }
        });

        /* attach MenuBar > Menu > MenuItem */
        JMenuBar menuBar = new JMenuBar();
        super.setJMenuBar(menuBar);

        /* Menu 1. FileMenu */
        JMenu fileMenu = new JMenu("File(F)");
        menuBar.add(fileMenu);
        /* Menu 1. FileMenu > newFile */
        JMenuItem newFile = new JMenuItem("New File (N)");
        fileMenu.add(newFile);
        newFile.addActionListener((e) -> view.doFileNew());
        /* Menu 1. FileMenu > newFile > add seperator */
        fileMenu.addSeparator();
        /**/

        /* add ActionListener 
         * i.e. can transformed to lambda expression */
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        openFile.addActionListener((e) -> doOpen());
        JMenuItem saveFile = new JMenuItem("Save File (S)");
        fileMenu.add(saveFile);
        saveFile.addActionListener((e) -> view.doSave(fileName));
        JMenuItem anotherFile = new JMenuItem("Another File (A)");
        fileMenu.add(anotherFile);
        anotherFile.addActionListener((e) -> doSaveAs());

        fileMenu.addSeparator();

        JMenuItem printFile = new JMenuItem("Print (P)");
        fileMenu.add(printFile);
        printFile.addActionListener((e) -> doPrint());

        fileMenu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit (X)");
        fileMenu.add(exit);
        exit.addActionListener( (e) -> {
            System.exit(-1);
        });

        /* Menu 2. figureMenu */
        JMenu figureMenu = new JMenu("Figure(F)");
        menuBar.add(figureMenu);

        JMenuItem pointItem = new JMenuItem(view.getPointAction());
        figureMenu.add(pointItem);
        JMenuItem rectItem = new JMenuItem(view.getRectAction());
        figureMenu.add(rectItem);
        JMenuItem lineItem = new JMenuItem(view.getLineAction());
        figureMenu.add(lineItem);
        JMenuItem circleItem = new JMenuItem(view.getCircleAction());
        figureMenu.add(circleItem);
        JMenuItem kiteItem = new JMenuItem(view.getKiteAction());
        figureMenu.add(kiteItem);
        JMenuItem textItem = new JMenuItem(view.getTextAction());
        figureMenu.add(textItem);

        /* Menu 3. toolMenu */
        JMenu toolMenu = new JMenu("Tool(T)");
        menuBar.add(toolMenu);

        /* when it opened, don't focus back to parent frame */
        JMenuItem modalTool = new JMenuItem("Modal (M)");
        toolMenu.add(modalTool);
        modalTool.addActionListener((e) -> {
            /* singleton dialog technique */
            if (dialog == null) {
                dialog = new FigureDialog("Figure Dialog", view);
            }
            dialog.setModal(true);
            dialog.setVisible(true);
        });
        JMenuItem modelessTool = new JMenuItem("Modeless (S)");
        toolMenu.add(modelessTool);
        modelessTool.addActionListener((e) -> {
            if (dialog == null) {
                dialog = new FigureDialog("Figure Dialog", view);
            }
            dialog.setModal(false);
            dialog.setVisible(true);
        });
        JMenuItem tableTool = new JMenuItem("Table (T)");
        toolMenu.add(tableTool);
        tableTool.addActionListener((e) -> {
            if (tableDialog == null) {
                tableDialog = new TableDialog("Table Dialog", view);
                tableDialog.setModal(true);
            }
            tableDialog.setVisible(true);
        });
        JMenuItem treeTool = new JMenuItem("Tree (R)");
        toolMenu.add(treeTool);
        treeTool.addActionListener((e) -> {
            if (treeDialog == null) {
                treeDialog = new TreeDialog("Tree Dialog", view);
                treeDialog.setModal(true);
            }
            treeDialog.setVisible(true);
        });
        JMenu zoomMenu = new JMenu("Zoom (Z)");
        toolMenu.add(zoomMenu);

        JMenuItem zoom100 = new JMenuItem("100%");
        zoomMenu.add(zoom100);
        zoom100.addActionListener((e) -> { 
            view.zoom(100);
        });
        JMenuItem zoom80 = new JMenuItem("80%");
        zoomMenu.add(zoom80);
        zoom80.addActionListener((e) -> {
            view.zoom(80);
        });
        JMenuItem zoom50 = new JMenuItem("50%");
        zoomMenu.add(zoom50);
        zoom50.addActionListener((e) -> {
            view.zoom(50);
        });


        /* Menu final. helpMenu */
        JMenu helpMenu = new JMenu("Help(H)");
        menuBar.add(helpMenu);
        
        JMenuItem drawerInfo = new JMenuItem("Drawer's Info (I)");
        helpMenu.add(drawerInfo);
        drawerInfo.addActionListener( (e) -> {
            JOptionPane.showMessageDialog(null, 
                    "Drawer 1.0 for Macintosh\n\n" + 
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
    /* delegation */
    void setPosition(String s) {
        statusBar.setPosition(s);
    }
    /* delegation */
    void setFigureType(String s) {
        statusBar.setFigureType(s);
    }
    /**/

    void doOpen() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileFilter(new FileNameExtensionFilter("JDrawer file", "jdr"));
        int val = chooser.showOpenDialog(null);
        if (val != JFileChooser.APPROVE_OPTION) return;
        fileName = chooser.getSelectedFile().getPath();
        String temp = chooser.getSelectedFile().getName();
        if (temp.endsWith(".jdr") == false) temp += ".jdr";
        setTitle("Drawer 1.0 for Macintosh - [" + temp + "]");
        view.doOpen(fileName);
    }
    void doSaveAs() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileFilter(new FileNameExtensionFilter("JDrawer file", "jdr"));
        int val = chooser.showSaveDialog(null);
        if (val != JFileChooser.APPROVE_OPTION) return;
        fileName = chooser.getSelectedFile().getPath();
        String temp = chooser.getSelectedFile().getName();
        if (fileName.endsWith(".jdr") == false) {
            fileName += ".jdr";
            temp += ".jdr";
        }
        setTitle("Drawer 1.0 for Macintosh - [" + temp + "]");
        view.doSave(fileName);
    }
    void doPrint() {
        PrinterJob job = PrinterJob.getPrinterJob();

        PageFormat pageFormat = job.defaultPage();
        pageFormat.setOrientation(PageFormat.LANDSCAPE); // Default landscape <-> portrait

        Printable printable = new PrintableView(view, fileName);
        job.setPrintable(printable, pageFormat);

        if (job.printDialog()) {
            try {
                job.print(); // finally goto PrintableView.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, e.toString(), "PrinterException", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
}
/** run */
class TestDriver {

    /* Operations */
    public static void main(String[] args) {
        JFrame drawerFrame = new DrawerFrame();
        drawerFrame.setVisible(true);
    }
}
