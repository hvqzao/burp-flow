package hvqzao.flow;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IScanIssue;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JButton;

import java.util.Random;
import java.util.Set;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

public class FlowAddNewIssue extends javax.swing.JPanel {

    private final Random rand;
    private final FlowExtension.FlowEntry flowEntry;
    private final DefaultCaret requestCaret;
    private final DefaultCaret responseCaret;

    /**
     * Creates new form FlowAddIssue
     *
     * @param callbacks
     * @param flowEntry
     */
    public FlowAddNewIssue(final IBurpExtenderCallbacks callbacks, final FlowExtension.FlowEntry flowEntry) {

        this.flowEntry = flowEntry;

        initComponents();

        rand = new Random();

        callbacks.customizeUiComponent(severityCombo);
        callbacks.customizeUiComponent(confidenceCombo);
        callbacks.customizeUiComponent(nameField);
        callbacks.customizeUiComponent(issueDetailArea);
        issueDetailArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERS‌​AL_KEYS, null);
        issueDetailArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERS‌​AL_KEYS, null);
        callbacks.customizeUiComponent(remediationDetailArea);
        remediationDetailArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERS‌​AL_KEYS, null);
        remediationDetailArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERS‌​AL_KEYS, null);
        callbacks.customizeUiComponent(issueBackgroundArea);
        issueBackgroundArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERS‌​AL_KEYS, null);
        issueBackgroundArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERS‌​AL_KEYS, null);
        callbacks.customizeUiComponent(remediationBackgroundArea);
        remediationBackgroundArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERS‌​AL_KEYS, null);
        remediationBackgroundArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERS‌​AL_KEYS, null);
        
        jSplitPane2.setDividerLocation(0.5d);
        
        IExtensionHelpers helpers = callbacks.getHelpers();
        
        callbacks.customizeUiComponent(request);
        requestCaret = (DefaultCaret) request.getCaret();
        requestCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        byte[] requestMessage = flowEntry.getMessageInfo().getRequest();
        if (requestMessage != null) {
            request.setText(helpers.bytesToString(requestMessage));
        }
        requestCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //request.setEditable(false);
        
        callbacks.customizeUiComponent(response);
        responseCaret = (DefaultCaret) response.getCaret();
        responseCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        byte[] responseMessage = flowEntry.getMessageInfo().getResponse();
        if (responseMessage != null) {
            response.setText(helpers.bytesToString(responseMessage));
        }
        responseCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //response.setEditable(false);
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    /**
     * Get {@link IScanIssue} object.
     *
     * @return
     */
    public IScanIssue getIssue() {
        //IHttpRequestResponsePersisted reqResp = flowEntry.getMessageInfoPersisted();
        return new IScanIssue() {
            @Override
            public URL getUrl() {
                return flowEntry.getUrl();
            }

            @Override
            public String getIssueName() {
                return nameField.getText();
            }

            @Override
            public int getIssueType() {
                return 1073741822 + rand.nextInt(1073741822);
            }

            @Override
            public String getSeverity() {
                return (String) severityCombo.getSelectedItem();
            }

            @Override
            public String getConfidence() {
                return (String) confidenceCombo.getSelectedItem();
            }

            @Override
            public String getIssueBackground() {
                return issueBackgroundArea.getText();
            }

            @Override
            public String getRemediationBackground() {
                return remediationBackgroundArea.getText();
            }

            @Override
            public String getIssueDetail() {
                return issueDetailArea.getText();
            }

            @Override
            public String getRemediationDetail() {
                return remediationDetailArea.getText();
            }

            @Override
            public IHttpRequestResponse[] getHttpMessages() {
                return new IHttpRequestResponse[]{flowEntry.getMessageInfo()};
            }

            @Override
            public IHttpService getHttpService() {
                return flowEntry.getService();
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        helpButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        issueBackgroundArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        remediationBackgroundArea = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        issueDetailArea = new javax.swing.JTextArea();
        severityCombo = new javax.swing.JComboBox<>();
        nameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        confidenceCombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        remediationDetailArea = new javax.swing.JTextArea();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        request = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        response = new javax.swing.JTextArea();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        helpButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        helpButton.setMaximumSize(new java.awt.Dimension(24, 24));
        helpButton.setMinimumSize(new java.awt.Dimension(24, 24));
        helpButton.setPreferredSize(new java.awt.Dimension(24, 24));

        jLabel1.setText("<html><b style='color:#e58900;font-size:10px'>Add new sitemap issue</b></html>");
        jLabel1.setToolTipText("");

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(3);

        jLabel6.setText("Issue background:");

        issueBackgroundArea.setColumns(20);
        issueBackgroundArea.setRows(5);
        issueBackgroundArea.setText("...");
        jScrollPane1.setViewportView(issueBackgroundArea);

        remediationBackgroundArea.setColumns(20);
        remediationBackgroundArea.setRows(5);
        remediationBackgroundArea.setText("...");
        jScrollPane2.setViewportView(remediationBackgroundArea);

        jLabel7.setText("Remediation background:");

        jLabel8.setText("Issue detail:");

        issueDetailArea.setColumns(20);
        issueDetailArea.setRows(5);
        issueDetailArea.setText("...");
        jScrollPane3.setViewportView(issueDetailArea);

        severityCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "High", "Medium", "Low", "Information" }));

        jLabel2.setText("This form allows adding new, custom finding.");

        confidenceCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Certain", "Firm", "Tentative" }));

        jLabel3.setText("Severity:");

        jLabel4.setText("Confidence:");

        jLabel9.setText("Remediation detail:");

        jLabel5.setText("Name:");

        remediationDetailArea.setColumns(20);
        remediationDetailArea.setRows(5);
        remediationDetailArea.setText("...");
        jScrollPane4.setViewportView(remediationDetailArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(nameField)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(confidenceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(severityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(severityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(confidenceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerLocation(185);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.5);

        jLabel10.setText("<html><b style='color:#e58900;font-size:10px'>Request</b></html>");

        request.setColumns(20);
        request.setRows(5);
        jScrollPane5.setViewportView(request);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel2);

        jLabel11.setText("<html><b style='color:#e58900;font-size:10px'>Response</b></html>");

        response.setColumns(20);
        response.setRows(5);
        jScrollPane6.setViewportView(response);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 359, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel3);

        jSplitPane1.setRightComponent(jSplitPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> confidenceCombo;
    private javax.swing.JButton helpButton;
    private javax.swing.JTextArea issueBackgroundArea;
    private javax.swing.JTextArea issueDetailArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextArea remediationBackgroundArea;
    private javax.swing.JTextArea remediationDetailArea;
    private javax.swing.JTextArea request;
    private javax.swing.JTextArea response;
    private javax.swing.JComboBox<String> severityCombo;
    // End of variables declaration//GEN-END:variables

    public static class TransferFocus {

        /**
         * Patch the behaviour of a component. TAB transfers focus to the next
         * focusable component, SHIFT+TAB transfers focus to the previous
         * focusable component.
         *
         * @param c The component to be patched.
         */
        public static void patch(Component c) {
            Set<KeyStroke> strokes = new HashSet<>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
            c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
            strokes = new HashSet<>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
            c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
        }
    }
}
