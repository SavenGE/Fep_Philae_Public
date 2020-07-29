/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author Pecherk
 */
public class CBBorder implements Border, MouseListener, MouseMotionListener, SwingConstants {

    private final int offset = 5;
    private final Component component;
    private final JComponent container;
    private Rectangle rect;
    private final Border border;
    private boolean mouseEntered = false;

    public CBBorder(Component component, JComponent container, Border border) {
        this.component = component;
        this.container = container;
        this.border = border;
        setListeners(container);
    }

    private void setListeners(JComponent container) {
        container.addMouseListener(this);
        container.addMouseMotionListener(this);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets borderInsets = border.getBorderInsets(c);
        int temp = (getBorderInsets(c).top - borderInsets.top) / 2;

        border.paintBorder(c, g, x, y + temp, width, height - temp);
        Dimension size = getComponent().getPreferredSize();

        g.setFont(c.getFont());
        g.setColor(c.getForeground());

        rect = new Rectangle(offset, 0, size.width, size.height);
        SwingUtilities.paintComponent(g, getComponent(), (Container) c, rect);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        Insets insets = border.getBorderInsets(c);
        insets.top = Math.max(insets.top, getComponent().getPreferredSize().height);
        return insets;
    }

    private void dispatchEvent(MouseEvent me, int id) {
        Point pt = me.getPoint();
        pt.translate(-offset, 0);

        getComponent().setSize(rect.width, rect.height);
        getComponent().dispatchEvent(new MouseEvent(getComponent(), id, me.getWhen(), me.getModifiers(), pt.x, pt.y, me.getClickCount(), me.isPopupTrigger(), me.getButton()));

        if (!component.isValid()) {
            container.repaint();
        }
    }

    private void dispatchEvent(MouseEvent me) {
        if (rect != null && rect.contains(me.getX(), me.getY())) {
            dispatchEvent(me, me.getID());
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (rect != null) {
            if (mouseEntered == false && rect.contains(me.getX(), me.getY())) {
                mouseEntered = true;
                dispatchEvent(me, MouseEvent.MOUSE_ENTERED);
            } else if (mouseEntered == true) {
                if (rect.contains(me.getX(), me.getY()) == false) {
                    mouseEntered = false;
                    dispatchEvent(me, MouseEvent.MOUSE_EXITED);
                } else {
                    dispatchEvent(me, MouseEvent.MOUSE_MOVED);
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (mouseEntered) {
            mouseEntered = false;
            dispatchEvent(me, MouseEvent.MOUSE_EXITED);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        dispatchEvent(me);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * @return the component
     */
    public Component getComponent() {
        return component;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
