/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.acx.BTBorder;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author Pecherk
 */
public class AFPanel extends javax.swing.JPanel {

    protected BTBorder border;
    protected Component contentPane;
    protected JComponent component;

    /**
     * Creates new form RFPanel
     *
     * @param titleComponent
     * @param contentPane
     */
    public AFPanel(JComponent titleComponent, Component contentPane) {
        initComponents();
        this.component = titleComponent;
        this.contentPane = contentPane;
        prepare();
    }

    private void prepare() {
        border = new BTBorder(component);
        setBorder(border);
        add(component);
        add(contentPane);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void doLayout() {
        Insets insets = getInsets();
        Rectangle rectangle = getBounds();
        rectangle.x = rectangle.y = 0;

        Rectangle compR = border.getComponentRect(rectangle, insets);
        component.setBounds(compR);
        rectangle.x += insets.left + 5;
        rectangle.y += insets.top + 5;

        rectangle.width -= (insets.left + insets.right + 10);
        rectangle.height -= (insets.top + insets.bottom + 10);
        contentPane.setBounds(rectangle);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}