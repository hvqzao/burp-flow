package hvqzao.flow;

import burp.IBurpExtenderCallbacks;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IScanIssue;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.swing.JButton;

import java.util.Random;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import static javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

public class FlowAddNewIssue extends javax.swing.JPanel {

    private final DefaultHighlightPainter p = new DefaultHighlightPainter(new Color(255, 197, 153));
    private final Random rand;
    private final IBurpExtenderCallbacks callbacks;
    private final FlowExtension.FlowEntry flowEntry;
    private final IHttpRequestResponse messageInfo;
    private final DefaultListModel highlightListModel;
    private final PrintWriter stderr;

    /**
     * Creates new form FlowAddIssue
     *
     * @param callbacks
     * @param flowEntry
     */
    public FlowAddNewIssue(final IBurpExtenderCallbacks callbacks, final FlowExtension.FlowEntry flowEntry) {
        this.callbacks = callbacks;
        stderr = FlowExtension.getStderr();

        this.flowEntry = flowEntry;

        initComponents();

        rand = new Random();

        callbacks.customizeUiComponent(severityCombo);
        callbacks.customizeUiComponent(confidenceCombo);
        callbacks.customizeUiComponent(nameField);
        callbacks.customizeUiComponent(issueDetailArea);
        issueDetailArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERS‌​AL_KEYS, null);
        issueDetailArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERS‌​AL_KEYS, null);
        //issueDetailArea.addFocusListener(new FocusListener() {
        //    @Override
        //    public void focusGained(FocusEvent e) {
        //        JTextArea c = (JTextArea) e.getComponent();
        //        c.select(0, c.getText().length());
        //    }
        //
        //    @Override
        //    public void focusLost(FocusEvent e) {
        //        JTextArea c = (JTextArea) e.getComponent();
        //        c.select(0, 0);
        //    }
        //});
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

        highlightListModel = new DefaultListModel();
        highlightList.setModel(highlightListModel);

        messageInfo = flowEntry.getMessageInfo();

        request.customizeUiComponent(callbacks);
        request.setText(messageInfo.getRequest());

        response.customizeUiComponent(callbacks);
        response.setText(messageInfo.getResponse());

        callbacks.customizeUiComponent(addHighlight);
        addHighlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reqRespPane.isVisible()) {
                    try {
                        JTextArea c;
                        boolean isRequest = request.hasFocus();
                        if (isRequest) {
                            c = request.getEditor();
                        } else {
                            c = response.getEditor();
                        }
                        int start = c.getSelectionStart();
                        int end = c.getSelectionEnd();
                        String text = c.getSelectedText();
                        if (text == null) {
                            return;
                        }
                        Object tag;
                        try {
                            tag = c.getHighlighter().addHighlight(start, end, p);
                        } catch (BadLocationException ex) {
                            return;
                        }
                        c.select(0, 0);
                        Highlight highlight = new Highlight(isRequest, tag, start, end, text);
                        highlightListModel.addElement(highlight);
                        highlightList.setSelectedIndex(highlightListModel.indexOf(highlight));
                    } catch (Exception ex) {
                        ex.printStackTrace(stderr);
                    }
                }
            }
        });
        callbacks.customizeUiComponent(removeHighlight);
        removeHighlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = highlightList.getSelectedIndex();
                    Highlight highlight = (Highlight) highlightListModel.get(index);
                    highlightListModel.removeElement(highlight);
                    JTextArea c;
                    if (highlight.isRequest) {
                        c = request.getEditor();
                    } else {
                        c = response.getEditor();
                    }
                    c.getHighlighter().removeHighlight(highlight.tag);
                    if (highlightList.getSelectedIndex() == -1 && highlightListModel.isEmpty() == false) {
                        highlightList.setSelectedIndex(0);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(stderr);
                }
            }
        });
        callbacks.customizeUiComponent(highlightList);
    }

    public boolean dataValidation() {
        if (nameField.getText().length() == 0) {
            nameError.setText("<html><i style='color:#ff6633'>Name must be set!</i></html>");
            return false;
        }
        return true;
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
        try {
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
                    List<int[]> requestMarkers = new ArrayList<>();
                    List<int[]> responseMarkers = new ArrayList<>();
                    for (int i = 0; i < highlightListModel.size(); i++) {
                        Highlight highlight = (Highlight) highlightListModel.get(i);
                        List<int[]> markers;
                        if (highlight.isRequest) {
                            markers = requestMarkers;
                        } else {
                            markers = responseMarkers;
                        }
                        markers.add(new int[]{highlight.start, highlight.end});
                    }
                    return new IHttpRequestResponse[]{callbacks.applyMarkers(messageInfo, requestMarkers, responseMarkers)};
                }

                @Override
                public IHttpService getHttpService() {
                    return flowEntry.getService();
                }
            };
        } catch (Exception ex) {
            ex.printStackTrace(stderr);
            return null;
        }
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
        nameError = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        reqRespPane = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        request = new hvqzao.flow.ui.Editor();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        response = new hvqzao.flow.ui.Editor();
        jPanel4 = new javax.swing.JPanel();
        addHighlight = new javax.swing.JButton();
        removeHighlight = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        highlightList = new javax.swing.JList<>();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        helpButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        helpButton.setMaximumSize(new java.awt.Dimension(24, 24));
        helpButton.setMinimumSize(new java.awt.Dimension(24, 24));
        helpButton.setPreferredSize(new java.awt.Dimension(24, 24));

        jLabel1.setText("<html><b style='color:#ff6633;font-size:10px'>Add new sitemap issue</b></html>");
        jLabel1.setToolTipText("");

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(3);

        jLabel6.setText("Issue background:");

        issueBackgroundArea.setColumns(20);
        issueBackgroundArea.setRows(5);
        jScrollPane1.setViewportView(issueBackgroundArea);

        remediationBackgroundArea.setColumns(20);
        remediationBackgroundArea.setRows(5);
        jScrollPane2.setViewportView(remediationBackgroundArea);

        jLabel7.setText("Remediation background:");

        jLabel8.setText("Issue detail:");

        issueDetailArea.setColumns(20);
        issueDetailArea.setRows(5);
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
        jScrollPane4.setViewportView(remediationDetailArea);

        nameError.setText(" ");
        nameError.setToolTipText("");

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
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(nameField)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(severityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(confidenceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nameError))))
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
                    .addComponent(confidenceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameError))
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

        jLabel10.setText("<html><b style='color:#ff6633;font-size:10px'>Request</b></html>");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
            .addComponent(request, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(request, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jSplitPane2.setTopComponent(jPanel2);

        jLabel11.setText("<html><b style='color:#ff6633;font-size:10px'>Response</b></html>");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 124, Short.MAX_VALUE))
            .addComponent(response, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(response, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel3);

        javax.swing.GroupLayout reqRespPaneLayout = new javax.swing.GroupLayout(reqRespPane);
        reqRespPane.setLayout(reqRespPaneLayout);
        reqRespPaneLayout.setHorizontalGroup(
            reqRespPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reqRespPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane2)
                .addContainerGap())
        );
        reqRespPaneLayout.setVerticalGroup(
            reqRespPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reqRespPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reqRespPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel5);

        addHighlight.setText("Add Highlight");

        removeHighlight.setText("Remove Highlight");

        jScrollPane7.setViewportView(highlightList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addHighlight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(removeHighlight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(addHighlight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeHighlight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addHighlight;
    private javax.swing.JComboBox<String> confidenceCombo;
    private javax.swing.JButton helpButton;
    private javax.swing.JList<String> highlightList;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel nameError;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextArea remediationBackgroundArea;
    private javax.swing.JTextArea remediationDetailArea;
    private javax.swing.JButton removeHighlight;
    private javax.swing.JPanel reqRespPane;
    private hvqzao.flow.ui.Editor request;
    private hvqzao.flow.ui.Editor response;
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

    private class Highlight {

        private final boolean isRequest;
        private final Object tag;
        private final int start;
        private final int end;
        private final String text;

        public Highlight(boolean isRequest, Object tag, int start, int end, String text) {
            this.isRequest = isRequest;
            this.tag = tag;
            this.start = start;
            this.end = end;
            this.text = text;
        }

        @Override
        public String toString() {
            return text + " (" + (isRequest ? "request" : "response") + ")";
        }

    }
}
