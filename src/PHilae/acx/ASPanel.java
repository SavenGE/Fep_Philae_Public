/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.model.AXSetting;
import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Pecherk
 */
public final class ASPanel extends javax.swing.JPanel {

    private String module;
    private AXSetting setting = new AXSetting();
    private static final ATBox box = new ATBox(APMain.acxLog);
    private TreeMap<String, AXSetting> settings = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates new form MXPanel
     *
     */
    public ASPanel() {
        initComponents();
        initDialog();
    }

    public void initDialog() {
        settingsDialog.setIconImage(APMain.getIconImage());
        settingsDialog.setContentPane(this);
        settingsDialog.pack();
        settingsDialog.setResizable(false);
        settingsDialog.setLocationRelativeTo(APMain.apFrame);
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

        settingsDialog = new javax.swing.JDialog();
        settingTreeScroller = new javax.swing.JScrollPane();
        settingTree = new javax.swing.JTree();
        jSeparator1 = new javax.swing.JSeparator();
        saveButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        closeDialogButton = new javax.swing.JButton();
        detailsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codeField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sysUserField = new javax.swing.JTextField();
        sysDateField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        valueArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        descriptionArea = new javax.swing.JTextArea();

        settingsDialog.setTitle("Settings");
        settingsDialog.setModal(true);
        settingsDialog.setName("settingsDialog"); // NOI18N

        javax.swing.GroupLayout settingsDialogLayout = new javax.swing.GroupLayout(settingsDialog.getContentPane());
        settingsDialog.getContentPane().setLayout(settingsDialogLayout);
        settingsDialogLayout.setHorizontalGroup(
            settingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        settingsDialogLayout.setVerticalGroup(
            settingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        settingsDialog.setContentPane(this);
        settingsDialog.pack();
        settingsDialog.setResizable(false);

        settingTreeScroller.setBorder(null);

        settingTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Settings");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Add Setting");
        treeNode1.add(treeNode2);
        settingTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        settingTree.setAutoscrolls(true);
        settingTree.setCellRenderer(new TRenderer());
        settingTree.setRootVisible(false);
        settingTree.setShowsRootHandles(true);
        settingTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                settingTreeValueChanged(evt);
            }
        });
        settingTreeScroller.setViewportView(settingTree);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveButtonActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        closeDialogButton.setText("Close");
        closeDialogButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeDialogButtonActionPerformed(evt);
            }
        });

        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray), "Setting Details"));

        jLabel1.setText("Setting Code");

        codeField.setBackground(new java.awt.Color(153, 204, 255));
        codeField.setForeground(new java.awt.Color(51, 102, 255));
        codeField.setToolTipText("Setting Code");
        codeField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                codeFieldFocusLost(evt);
            }
        });

        jLabel2.setText("Setting Value");

        jLabel3.setText("Description");

        sysUserField.setEditable(false);
        sysUserField.setForeground(new java.awt.Color(51, 102, 255));
        sysUserField.setToolTipText("System User");

        sysDateField.setEditable(false);
        sysDateField.setForeground(new java.awt.Color(51, 102, 255));
        sysDateField.setToolTipText("Setting Date");

        jLabel8.setText("Date Modified");

        jLabel10.setText("Modified By");

        valueArea.setBackground(new java.awt.Color(153, 204, 255));
        valueArea.setColumns(20);
        valueArea.setLineWrap(true);
        valueArea.setRows(5);
        valueArea.setToolTipText("Setting Value");
        jScrollPane2.setViewportView(valueArea);

        descriptionArea.setBackground(new java.awt.Color(153, 204, 255));
        descriptionArea.setColumns(20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setRows(5);
        descriptionArea.setToolTipText("Setting Description");
        jScrollPane3.setViewportView(descriptionArea);

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codeField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addComponent(sysDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sysUserField, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)))
                .addContainerGap())
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(codeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(sysDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(sysUserField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingTreeScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeDialogButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1))
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
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(closeDialogButton, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(saveButton))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(settingTreeScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void settingTreeValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_settingTreeValueChanged
    {//GEN-HEADEREND:event_settingTreeValueChanged
        // TODO add your handling code here:
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) settingTree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.getUserObject() instanceof AXSetting) {
                displaySetting((AXSetting) selectedNode.getUserObject());
            } else {
                acceptSetting();
            }
        }
    }//GEN-LAST:event_settingTreeValueChanged

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveButtonActionPerformed
    {//GEN-HEADEREND:event_saveButtonActionPerformed
        // TODO add your handling code here:
        if (getWorker().validateFields(settingsDialog, this)) {
            boolean proceed = getSettings().containsKey(codeField.getText().trim())
                    ? (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(settingsDialog, "Are you sure you want to save changes to this setting?", "Confirm Update", JOptionPane.YES_NO_OPTION))
                    : (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(settingsDialog, "Are you sure you want to save this new setting?", "Confirm Save", JOptionPane.YES_NO_OPTION));
            if (proceed) {
                getSetting().setModule(getModule());
                getSetting().setCode(codeField.getText().trim());
                getSetting().setValue(valueArea.getText().equals(getWorker().protectField(getSetting().getValue(), 1, 1)) ? getSetting().getValue() : valueArea.getText().trim());
                getSetting().setDescription(descriptionArea.getText());
                getSetting().setSysDate(new Date());
                getSetting().setSysUser(ULPanel.getUser().getStaffName());
                if (getClient().upsertSetting(getSetting())) {
                    APController.configure(getModule());
                    JOptionPane.showMessageDialog(settingsDialog, "Setting saved successfully.");
                    setSettingTree();
                } else {
                    JOptionPane.showMessageDialog(settingsDialog, "Unable to save setting!", "Saving Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    public void showDialog(String module) {
        // TODO add your handling code here:
        setModule(module);
        setSettingTree();
        settingsDialog.setTitle((getModule().length() > 3 ? getWorker().capitalize(getModule()) : getModule()) + " Settings");
        EventQueue.invokeLater(()
                -> {
            settingsDialog.setVisible(true);
        });
    }

    private void closeDialogButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeDialogButtonActionPerformed
    {//GEN-HEADEREND:event_closeDialogButtonActionPerformed
        // TODO add your handling code here:
        EventQueue.invokeLater(()
                -> {
            settingsDialog.setVisible(false);
        });
    }//GEN-LAST:event_closeDialogButtonActionPerformed

    private void codeFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_codeFieldFocusLost
    {//GEN-HEADEREND:event_codeFieldFocusLost
        // TODO add your handling code here:
        getWorker().selectTreeNode(settingTree, codeField.getText().trim());
    }//GEN-LAST:event_codeFieldFocusLost

    public void setSettingTree() {
        setSettings(getClient().querySettings(getModule()));
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) settingTree.getModel().getRoot();

        rootNode.removeAllChildren();
        rootNode.add(new DefaultMutableTreeNode("Add Setting"));

        for (Object code : getWorker().sortArray(getSettings().keySet().toArray(), true)) {
            rootNode.add(new DefaultMutableTreeNode(getSettings().get(code)));
        }

        getWorker().expandAllNodes(settingTree, AXSetting.class);
    }

    private void acceptSetting() {
        setSetting(new AXSetting());
        getWorker().resetAllFields(this);
        codeField.setEditable(true);
    }

    private void displaySetting(AXSetting setting) {
        setSetting(setting);
        codeField.setEditable(false);
        codeField.setText(setting.getCode());
        descriptionArea.setText(setting.getDescription());
        sysUserField.setText(setting.getSysUser());
        valueArea.setText(setting.isEncrypted() ? getWorker().protectField(setting.getValue(), 1, 1) : setting.getValue());
        sysDateField.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss a").format(setting.getSysDate() != null ? setting.getSysDate() : new Date()));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeDialogButton;
    private javax.swing.JTextField codeField;
    private javax.swing.JTextArea descriptionArea;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton saveButton;
    private javax.swing.JTree settingTree;
    private javax.swing.JScrollPane settingTreeScroller;
    public javax.swing.JDialog settingsDialog;
    private javax.swing.JTextField sysDateField;
    private javax.swing.JTextField sysUserField;
    private javax.swing.JTextArea valueArea;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the settings
     */
    public TreeMap<String, AXSetting> getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(TreeMap<String, AXSetting> settings) {
        this.settings = settings;
    }

    /**
     * @return the worker
     */
    public AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the setting
     */
    public AXSetting getSetting() {
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(AXSetting setting) {
        this.setting = setting;
    }

    private DBPClient getClient() {
        return getBox().getClient();
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
    }
}