/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * POSUsers.java
 *
 * Created on Feb 22, 2012, 12:21:01 AM
 */
package PHilae.acx;

import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.model.AXUser;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Pecherk
 */
public final class AUPanel extends javax.swing.JPanel {

    private AXUser user = new AXUser();
    private TreeMap<String, AXUser> users = new TreeMap<>();
    private final ATBox box = new ATBox(APMain.acxLog);

    /**
     * Creates new form POSUsers
     */
    public AUPanel() {
        initComponents();
        initDialog();
    }

    public void initDialog() {
        getWorker().setCheckBorder(applicationPanel, true);
        getWorker().setCheckBorder(mobilePanel, true);
        getWorker().setCheckBorder(alertPanel, true);

        getWorker().setCheckBorder(mailerPanel, true);
        usersDialog.setIconImage(APMain.getIconImage());
        usersDialog.setContentPane(this);

        usersDialog.pack();
        usersDialog.setResizable(false);
        usersDialog.setLocationRelativeTo(APMain.apFrame);
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

        usersDialog = new javax.swing.JDialog();
        jCheckBox40 = new javax.swing.JCheckBox();
        detailsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        userNameField = new javax.swing.JTextField();
        userNumberField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        statusBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        sysUserField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        sysDateField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        cancelButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        saveButton = new javax.swing.JButton();
        mobilePanel = new javax.swing.JPanel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        applicationPanel = new javax.swing.JPanel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        alertPanel = new javax.swing.JPanel();
        jCheckBox35 = new javax.swing.JCheckBox();
        jCheckBox36 = new javax.swing.JCheckBox();
        jCheckBox37 = new javax.swing.JCheckBox();
        jCheckBox38 = new javax.swing.JCheckBox();
        jCheckBox39 = new javax.swing.JCheckBox();
        userTreeScroller = new javax.swing.JScrollPane();
        userTree = new javax.swing.JTree();
        mailerPanel = new javax.swing.JPanel();
        jCheckBox42 = new javax.swing.JCheckBox();
        jCheckBox43 = new javax.swing.JCheckBox();
        jCheckBox44 = new javax.swing.JCheckBox();
        jCheckBox45 = new javax.swing.JCheckBox();
        jCheckBox46 = new javax.swing.JCheckBox();

        usersDialog.setTitle("PHilae Users");
        usersDialog.setIconImage(PHilae.APMain.getIconImage());
        usersDialog.setModal(true);
        usersDialog.setName("usersDialog"); // NOI18N

        javax.swing.GroupLayout usersDialogLayout = new javax.swing.GroupLayout(usersDialog.getContentPane());
        usersDialog.getContentPane().setLayout(usersDialogLayout);
        usersDialogLayout.setHorizontalGroup(
            usersDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        usersDialogLayout.setVerticalGroup(
            usersDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jCheckBox40.setText("PS ~ Shutdown");

        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray), "User Details"));

        jLabel1.setText("Staff Name");

        nameField.setEditable(false);
        nameField.setToolTipText("Staff Name");

        userNameField.setToolTipText("Login Name");
        ((AbstractDocument) userNameField.getDocument()).setDocumentFilter(new UCFilter());
        userNameField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                userNameFieldFocusLost(evt);
            }
        });

        userNumberField.setEditable(false);
        userNumberField.setToolTipText("Staff Number");

        jLabel7.setText("User Status");

        statusBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A~Active", "C~Closed" }));
        statusBox.setToolTipText("User Status");

        jLabel8.setText("Date Modified");

        sysUserField.setEditable(false);
        sysUserField.setForeground(new java.awt.Color(51, 102, 255));
        sysUserField.setToolTipText("System User");

        jLabel10.setText("Staff No.");

        jLabel11.setText("Modified By");

        sysDateField.setEditable(false);
        sysDateField.setForeground(new java.awt.Color(51, 102, 255));
        sysDateField.setToolTipText("Date Modified");

        jLabel2.setText("Login Name");

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addComponent(userNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField))
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addComponent(statusBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sysDateField))
                    .addComponent(nameField)
                    .addComponent(sysUserField))
                .addContainerGap())
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(userNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(sysDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(sysUserField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancelButton.setText("Close");
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveButtonActionPerformed(evt);
            }
        });

        mobilePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray), "Mobile Privileges"));

        jCheckBox7.setText("MS ~ Settings");

        jCheckBox8.setText("ME ~ Schemes");

        jCheckBox9.setText("MC ~ Charges");

        jCheckBox10.setText("MU ~ Suspend");

        javax.swing.GroupLayout mobilePanelLayout = new javax.swing.GroupLayout(mobilePanel);
        mobilePanel.setLayout(mobilePanelLayout);
        mobilePanelLayout.setHorizontalGroup(
            mobilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mobilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mobilePanelLayout.setVerticalGroup(
            mobilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mobilePanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(mobilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8)
                    .addComponent(jCheckBox9)
                    .addComponent(jCheckBox10))
                .addGap(0, 0, 0))
        );

        applicationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray), "Application Privileges"));

        jCheckBox11.setText("AU ~ Users");

        jCheckBox12.setText("AE ~ Encryption");

        jCheckBox25.setText("AS ~ Shutdown");

        javax.swing.GroupLayout applicationPanelLayout = new javax.swing.GroupLayout(applicationPanel);
        applicationPanel.setLayout(applicationPanelLayout);
        applicationPanelLayout.setHorizontalGroup(
            applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(applicationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox25, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        applicationPanelLayout.setVerticalGroup(
            applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(applicationPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox11)
                    .addComponent(jCheckBox12)
                    .addComponent(jCheckBox25))
                .addGap(5, 5, 5))
        );

        alertPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray), "Alert Privileges"));

        jCheckBox35.setText("LS ~ Settings");

        jCheckBox36.setText("LA ~ Alerts");

        jCheckBox37.setText("LE ~ Schemes");

        jCheckBox38.setText("LC ~ Charges");

        jCheckBox39.setText("LU ~ Suspend");

        javax.swing.GroupLayout alertPanelLayout = new javax.swing.GroupLayout(alertPanel);
        alertPanel.setLayout(alertPanelLayout);
        alertPanelLayout.setHorizontalGroup(
            alertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alertPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox35, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox36, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox37, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox38, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox39, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        alertPanelLayout.setVerticalGroup(
            alertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alertPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(alertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox35)
                    .addComponent(jCheckBox36)
                    .addComponent(jCheckBox37)
                    .addComponent(jCheckBox38)
                    .addComponent(jCheckBox39))
                .addGap(5, 5, 5))
        );

        userTreeScroller.setBorder(null);
        userTreeScroller.setBorder(null);

        userTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("PHilae Users");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Add User");
        treeNode1.add(treeNode2);
        userTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        userTree.setCellRenderer(new PHilae.acx.TRenderer());
        userTree.setRootVisible(false);
        userTree.setShowsRootHandles(true);
        userTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                userTreeValueChanged(evt);
            }
        });
        userTreeScroller.setViewportView(userTree);

        mailerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray), "Mailer Privileges"));

        jCheckBox42.setText("SS ~ Settings");

        jCheckBox43.setText("SA ~ Tasks");

        jCheckBox44.setText("SE ~ Schemes");

        jCheckBox45.setText("SC ~ Charges");

        jCheckBox46.setText("SU ~ Suspend");

        javax.swing.GroupLayout mailerPanelLayout = new javax.swing.GroupLayout(mailerPanel);
        mailerPanel.setLayout(mailerPanelLayout);
        mailerPanelLayout.setHorizontalGroup(
            mailerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mailerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox42, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox43, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox44, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox45, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox46, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mailerPanelLayout.setVerticalGroup(
            mailerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mailerPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(mailerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox42)
                    .addComponent(jCheckBox43)
                    .addComponent(jCheckBox44)
                    .addComponent(jCheckBox45)
                    .addComponent(jCheckBox46))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userTreeScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(applicationPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alertPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mobilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mailerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(detailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applicationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mobilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alertPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mailerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2)
                            .addComponent(cancelButton)))
                    .addComponent(userTreeScroller))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void userTreeValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_userTreeValueChanged
    {//GEN-HEADEREND:event_userTreeValueChanged
        // TODO add your handling code here:
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.getUserObject() instanceof AXUser) {
                displayUser((AXUser) selectedNode.getUserObject());
            } else {
                acceptUser();
            }
        }
}//GEN-LAST:event_userTreeValueChanged

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        EventQueue.invokeLater(()
                -> {
            usersDialog.setVisible(false);
        });
}//GEN-LAST:event_cancelButtonActionPerformed

    public void showDialog() {
        setUserTree();
        EventQueue.invokeLater(()
                -> {
            usersDialog.setVisible(true);
        });
    }

    private void acceptUser() {
        setUser(new AXUser());
        getWorker().resetAllFields(this);
        userNameField.setEditable(true);
        saveButton.setEnabled(false);
    }

    private void displayUser(AXUser user) {
        setUser(user);
        getWorker().resetAllFields(this);
        userNameField.setEditable(false);
        userNumberField.setText(user.getUserNumber());
        userNameField.setText(user.getUserName());
        nameField.setText(user.getStaffName());
        getWorker().selectBoxValue(statusBox, user.getStatus());
        sysUserField.setText(user.getSysUser());
        sysDateField.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss a").format(user.getSysDate() != null ? user.getSysDate() : new Date()));
        saveButton.setEnabled(!getWorker().isBlank(userNumberField.getText()));
        processRoles(this, true);
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveButtonActionPerformed
    {//GEN-HEADEREND:event_saveButtonActionPerformed
        // TODO add your handling code here:
        if (getWorker().validateFields(usersDialog, detailsPanel)) {
            boolean proceed = getUsers().containsKey(userNumberField.getText().trim())
                    ? (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(usersDialog, "Are you sure you want to save changes to this user?", "Confirm Update", JOptionPane.YES_NO_OPTION))
                    : (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(usersDialog, "Are you sure you want to save this new user?", "Confirm Save", JOptionPane.YES_NO_OPTION));
            if (proceed) {
                getUser().setUserNumber(userNumberField.getText().trim());
                getUser().setUserName(userNameField.getText().trim());
                getUser().setStaffName(nameField.getText().trim());
                getUser().setStatus(getWorker().getBoxValue(statusBox));
                getUser().setSysDate(new Date());
                getUser().setSysUser(ULPanel.getUser().getStaffName());
                getUser().getRoles().clear();
                processRoles(this, false);
                if (getClient().upsertUser(getUser())) {
                    JOptionPane.showMessageDialog(usersDialog, "User saved successfully.");
                    setUserTree();
                } else {
                    JOptionPane.showMessageDialog(usersDialog, "Unable to save user!", "Saving Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
}//GEN-LAST:event_saveButtonActionPerformed

    private void userNameFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_userNameFieldFocusLost
    {//GEN-HEADEREND:event_userNameFieldFocusLost
        // TODO add your handling code here:
        findUser(userNameField.getText().trim());
    }//GEN-LAST:event_userNameFieldFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel alertPanel;
    private javax.swing.JPanel applicationPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox35;
    private javax.swing.JCheckBox jCheckBox36;
    private javax.swing.JCheckBox jCheckBox37;
    private javax.swing.JCheckBox jCheckBox38;
    private javax.swing.JCheckBox jCheckBox39;
    private javax.swing.JCheckBox jCheckBox40;
    private javax.swing.JCheckBox jCheckBox42;
    private javax.swing.JCheckBox jCheckBox43;
    private javax.swing.JCheckBox jCheckBox44;
    private javax.swing.JCheckBox jCheckBox45;
    private javax.swing.JCheckBox jCheckBox46;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mailerPanel;
    private javax.swing.JPanel mobilePanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox statusBox;
    private javax.swing.JTextField sysDateField;
    private javax.swing.JTextField sysUserField;
    private javax.swing.JTextField userNameField;
    private javax.swing.JTextField userNumberField;
    private javax.swing.JTree userTree;
    private javax.swing.JScrollPane userTreeScroller;
    public javax.swing.JDialog usersDialog;
    // End of variables declaration//GEN-END:variables

    public void setUserTree() {
        setUsers(getClient().queryUsers());
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) userTree.getModel().getRoot();

        rootNode.removeAllChildren();
        rootNode.add(new DefaultMutableTreeNode("Add User"));

        for (String userName : getWorker().sortArray(getUsers().keySet().toArray(new String[0]), true)) {
            rootNode.add(new DefaultMutableTreeNode(getUsers().get(userName)));
        }

        getWorker().expandAllNodes(userTree, AXUser.class);
    }

    public void processRoles(JPanel panel, boolean display) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JCheckBox && ((JCheckBox) component).getText().contains("~")) {
                String boxValue = ((JCheckBox) component).getText().split("~")[0].trim();
                if (display) {
                    ((JCheckBox) component).setSelected(getUser().getRoles().contains(boxValue));
                } else if (((JCheckBox) component).isSelected()) {
                    getUser().getRoles().add(boxValue);
                }
            } else if (component instanceof JPanel && component.isEnabled()) {
                processRoles((JPanel) component, display);
            }
        }
    }

    private void findUser(String userName) throws HeadlessException {
        if (!getWorker().isBlank(userName) && getUsers().containsKey(userName)) {
            getWorker().selectTreeNode(userTree, getUsers().get(userName));
        } else if (!getWorker().isBlank(userName)) {
            AXUser aXUser = getClient().queryUser(userName, false);
            if (getWorker().isBlank(aXUser.getStaffName())) {
                JOptionPane.showMessageDialog(usersDialog, "User not found!", "Unknown User", JOptionPane.ERROR_MESSAGE);
            } else {
                displayUser(aXUser);
            }
        }
    }

    private DBPClient getClient() {
        return getBox().getClient();
    }

    /**
     * @return the worker
     */
    public AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the box
     */
    public ATBox getBox() {
        return box;
    }

    /**
     * @return the user
     */
    public AXUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(AXUser user) {
        this.user = user;
    }

    /**
     * @return the users
     */
    public TreeMap<String, AXUser> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(TreeMap<String, AXUser> users) {
        this.users = users;
    }
}
