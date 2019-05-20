package hvqzao.flow;

import burp.IBurpExtenderCallbacks;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public final class FlowFilterPopup extends javax.swing.JPanel {

    /**
     * Creates new form FlowTablePane
     */
    public FlowFilterPopup() {
        initComponents();
        TitledBorder flowFilterByReqTypeBorder = (TitledBorder) getFlowFilterByReqType().getBorder();
        flowFilterByReqTypeBorder.setTitleFont(flowFilterByReqTypeBorder.getTitleFont().deriveFont(Font.PLAIN));

        TitledBorder flowFilterBySearchBorder = (TitledBorder) getFlowFilterBySearch().getBorder();
        flowFilterBySearchBorder.setTitleFont(flowFilterBySearchBorder.getTitleFont().deriveFont(Font.PLAIN));

        TitledBorder flowFilterBySourceBorder = (TitledBorder) getFlowFilterBySource().getBorder();
        flowFilterBySourceBorder.setTitleFont(flowFilterBySourceBorder.getTitleFont().deriveFont(Font.PLAIN));

        TitledBorder flowFilterCaptureBySourceBorder = (TitledBorder) getFlowFilterCaptureBySource().getBorder();
        flowFilterCaptureBySourceBorder.setTitleFont(flowFilterCaptureBySourceBorder.getTitleFont().deriveFont(Font.PLAIN));

        IBurpExtenderCallbacks callbacks = FlowExtension.getCallbacks();
        callbacks.customizeUiComponent(FlowFilterCaptureSourceExtender);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceExtenderOnly);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceIntruder);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceIntruderOnly);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceTarget);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceTargetOnly);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceProxy);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceProxyOnly);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceRepeater);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceRepeaterOnly);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceScanner);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceScannerOnly);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceSpider);
        callbacks.customizeUiComponent(FlowFilterCaptureSourceSpiderOnly);
        callbacks.customizeUiComponent(FlowFilterInscope);
        callbacks.customizeUiComponent(FlowFilterOutofscope);
        callbacks.customizeUiComponent(FlowFilterParametrized);
        callbacks.customizeUiComponent(FlowFilterSearchCaseSensitive);
        callbacks.customizeUiComponent(FlowFilterSearchField);
        callbacks.customizeUiComponent(FlowFilterSearchNegative);
        callbacks.customizeUiComponent(FlowFilterSearchRegex);
        callbacks.customizeUiComponent(FlowFilterSearchRequest);
        callbacks.customizeUiComponent(FlowFilterSearchResponse);
        callbacks.customizeUiComponent(FlowFilterSourceExtender);
        callbacks.customizeUiComponent(FlowFilterSourceExtenderOnly);
        callbacks.customizeUiComponent(FlowFilterSourceIntruder);
        callbacks.customizeUiComponent(FlowFilterSourceIntruderOnly);
        callbacks.customizeUiComponent(FlowFilterSourceTarget);
        callbacks.customizeUiComponent(FlowFilterSourceTargetOnly);
        callbacks.customizeUiComponent(FlowFilterSourceProxy);
        callbacks.customizeUiComponent(FlowFilterSourceProxyOnly);
        callbacks.customizeUiComponent(FlowFilterSourceRepeater);
        callbacks.customizeUiComponent(FlowFilterSourceRepeaterOnly);
        callbacks.customizeUiComponent(FlowFilterSourceScanner);
        callbacks.customizeUiComponent(FlowFilterSourceScannerOnly);
        callbacks.customizeUiComponent(FlowFilterSourceSpider);
        callbacks.customizeUiComponent(FlowFilterSourceSpiderOnly);
    }

    public JButton getFlowFilterDefaults() {
        return flowFilterDefaults;
    }

    public JButton getFlowFilterHelp() {
        return flowFilterHelp;
    }

    public JPanel getFlowFilterByReqType() {
        return FlowFilterByReqType;
    }

    public JPanel getFlowFilterBySearch() {
        return FlowFilterBySearch;
    }

    public JPanel getFlowFilterBySource() {
        return FlowFilterBySource;
    }

    public JPanel getFlowFilterCaptureBySource() {
        return FlowFilterCaptureBySource;
    }

    public JCheckBox getFlowFilterCaptureSourceExtender() {
        return FlowFilterCaptureSourceExtender;
    }

    public JCheckBox getFlowFilterCaptureSourceExtenderOnly() {
        return FlowFilterCaptureSourceExtenderOnly;
    }

    public JCheckBox getFlowFilterCaptureSourceIntruder() {
        return FlowFilterCaptureSourceIntruder;
    }

    public JCheckBox getFlowFilterCaptureSourceIntruderOnly() {
        return FlowFilterCaptureSourceIntruderOnly;
    }

    public JCheckBox getFlowFilterCaptureSourceTarget() {
        return FlowFilterCaptureSourceTarget;
    }

    public JCheckBox getFlowFilterCaptureSourceTargetOnly() {
        return FlowFilterCaptureSourceTargetOnly;
    }

    public JCheckBox getFlowFilterCaptureSourceProxy() {
        return FlowFilterCaptureSourceProxy;
    }

    public JCheckBox getFlowFilterCaptureSourceProxyOnly() {
        return FlowFilterCaptureSourceProxyOnly;
    }

    public JCheckBox getFlowFilterCaptureSourceRepeater() {
        return FlowFilterCaptureSourceRepeater;
    }

    public JCheckBox getFlowFilterCaptureSourceRepeaterOnly() {
        return FlowFilterCaptureSourceRepeaterOnly;
    }

    public JCheckBox getFlowFilterCaptureSourceScanner() {
        return FlowFilterCaptureSourceScanner;
    }

    public JCheckBox getFlowFilterCaptureSourceScannerOnly() {
        return FlowFilterCaptureSourceScannerOnly;
    }

    public JCheckBox getFlowFilterCaptureSourceSpider() {
        return FlowFilterCaptureSourceSpider;
    }

    public JCheckBox getFlowFilterCaptureSourceSpiderOnly() {
        return FlowFilterCaptureSourceSpiderOnly;
    }

    public JCheckBox getFlowFilterInscope() {
        return FlowFilterInscope;
    }

    public JCheckBox getFlowFilterOutofscope() {
        return FlowFilterOutofscope;
    }

    public JCheckBox getFlowFilterParametrized() {
        return FlowFilterParametrized;
    }

    public JCheckBox getFlowFilterSearchCaseSensitive() {
        return FlowFilterSearchCaseSensitive;
    }

    public JTextField getFlowFilterSearchField() {
        return FlowFilterSearchField;
    }

    public JCheckBox getFlowFilterSearchNegative() {
        return FlowFilterSearchNegative;
    }

    public JCheckBox getFlowFilterSearchRegex() {
        return FlowFilterSearchRegex;
    }

    public JCheckBox getFlowFilterSearchRequest() {
        return FlowFilterSearchRequest;
    }

    public JCheckBox getFlowFilterSearchResponse() {
        return FlowFilterSearchResponse;
    }

    public JCheckBox getFlowFilterSourceTarget() {
        return FlowFilterSourceTarget;
    }

    public JCheckBox getFlowFilterSourceTargetOnly() {
        return FlowFilterSourceTargetOnly;
    }
    
    public JCheckBox getFlowFilterSourceExtender() {
        return FlowFilterSourceExtender;
    }

    public JCheckBox getFlowFilterSourceExtenderOnly() {
        return FlowFilterSourceExtenderOnly;
    }

    public JCheckBox getFlowFilterSourceIntruder() {
        return FlowFilterSourceIntruder;
    }

    public JCheckBox getFlowFilterSourceIntruderOnly() {
        return FlowFilterSourceIntruderOnly;
    }

    public JCheckBox getFlowFilterSourceProxy() {
        return FlowFilterSourceProxy;
    }

    public JCheckBox getFlowFilterSourceProxyOnly() {
        return FlowFilterSourceProxyOnly;
    }

    public JCheckBox getFlowFilterSourceRepeater() {
        return FlowFilterSourceRepeater;
    }

    public JCheckBox getFlowFilterSourceRepeaterOnly() {
        return FlowFilterSourceRepeaterOnly;
    }

    public JCheckBox getFlowFilterSourceScanner() {
        return FlowFilterSourceScanner;
    }

    public JCheckBox getFlowFilterSourceScannerOnly() {
        return FlowFilterSourceScannerOnly;
    }

    public JCheckBox getFlowFilterSourceSpider() {
        return FlowFilterSourceSpider;
    }

    public JCheckBox getFlowFilterSourceSpiderOnly() {
        return FlowFilterSourceSpiderOnly;
    }

    public JPanel getFlowFilterBottom() {
        return FlowFilterBottom;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        flowFilterHelp = new javax.swing.JButton();
        flowFilterDefaults = new javax.swing.JButton();
        FlowFilterByReqType = new javax.swing.JPanel();
        FlowFilterInscope = new javax.swing.JCheckBox();
        FlowFilterParametrized = new javax.swing.JCheckBox();
        FlowFilterOutofscope = new javax.swing.JCheckBox();
        FlowFilterBySearch = new javax.swing.JPanel();
        FlowFilterSearchField = new javax.swing.JTextField();
        FlowFilterSearchCaseSensitive = new javax.swing.JCheckBox();
        FlowFilterSearchNegative = new javax.swing.JCheckBox();
        FlowFilterSearchRegex = new javax.swing.JCheckBox();
        FlowFilterSearchRequest = new javax.swing.JCheckBox();
        FlowFilterSearchResponse = new javax.swing.JCheckBox();
        FlowFilterBySource = new javax.swing.JPanel();
        FlowFilterSourceTarget = new javax.swing.JCheckBox();
        FlowFilterSourceSpider = new javax.swing.JCheckBox();
        FlowFilterSourceScanner = new javax.swing.JCheckBox();
        FlowFilterSourceRepeater = new javax.swing.JCheckBox();
        FlowFilterSourceIntruder = new javax.swing.JCheckBox();
        FlowFilterSourceExtender = new javax.swing.JCheckBox();
        FlowFilterSourceTargetOnly = new javax.swing.JCheckBox();
        FlowFilterSourceSpiderOnly = new javax.swing.JCheckBox();
        FlowFilterSourceScannerOnly = new javax.swing.JCheckBox();
        FlowFilterSourceRepeaterOnly = new javax.swing.JCheckBox();
        FlowFilterSourceIntruderOnly = new javax.swing.JCheckBox();
        FlowFilterSourceExtenderOnly = new javax.swing.JCheckBox();
        FlowFilterSourceProxy = new javax.swing.JCheckBox();
        FlowFilterSourceProxyOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureBySource = new javax.swing.JPanel();
        FlowFilterCaptureSourceTarget = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceTargetOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceSpider = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceScanner = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceRepeater = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceIntruder = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceExtender = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceSpiderOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceScannerOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceRepeaterOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceIntruderOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceExtenderOnly = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceProxy = new javax.swing.JCheckBox();
        FlowFilterCaptureSourceProxyOnly = new javax.swing.JCheckBox();
        FlowFilterBottom = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Table.background"), 2), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.darkGray), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))));

        flowFilterHelp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        flowFilterHelp.setMaximumSize(new java.awt.Dimension(24, 24));
        flowFilterHelp.setMinimumSize(new java.awt.Dimension(24, 24));
        flowFilterHelp.setPreferredSize(new java.awt.Dimension(24, 24));

        flowFilterDefaults.setInheritsPopupMenu(true);
        flowFilterDefaults.setMargin(new java.awt.Insets(0, 0, 0, 0));
        flowFilterDefaults.setMaximumSize(new java.awt.Dimension(24, 24));
        flowFilterDefaults.setMinimumSize(new java.awt.Dimension(24, 24));
        flowFilterDefaults.setPreferredSize(new java.awt.Dimension(24, 24));

        FlowFilterByReqType.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter by request type"));

        FlowFilterInscope.setText("Show only in-scope");

        FlowFilterParametrized.setText("Show only parametrized requests");

        FlowFilterOutofscope.setText("out-of-scope items");

        javax.swing.GroupLayout FlowFilterByReqTypeLayout = new javax.swing.GroupLayout(FlowFilterByReqType);
        FlowFilterByReqType.setLayout(FlowFilterByReqTypeLayout);
        FlowFilterByReqTypeLayout.setHorizontalGroup(
            FlowFilterByReqTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterByReqTypeLayout.createSequentialGroup()
                .addGroup(FlowFilterByReqTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FlowFilterByReqTypeLayout.createSequentialGroup()
                        .addComponent(FlowFilterInscope)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FlowFilterOutofscope))
                    .addComponent(FlowFilterParametrized))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        FlowFilterByReqTypeLayout.setVerticalGroup(
            FlowFilterByReqTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterByReqTypeLayout.createSequentialGroup()
                .addGroup(FlowFilterByReqTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterInscope)
                    .addComponent(FlowFilterOutofscope))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterParametrized))
        );

        FlowFilterInscope.getAccessibleContext().setAccessibleName("FlowFilterInscope");

        FlowFilterBySearch.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter by search term"));

        FlowFilterSearchCaseSensitive.setText("Case sensitive");

        FlowFilterSearchNegative.setText("Negative search");

        FlowFilterSearchRegex.setText("Regex search");

        FlowFilterSearchRequest.setText("Request");

        FlowFilterSearchResponse.setText("Response");

        javax.swing.GroupLayout FlowFilterBySearchLayout = new javax.swing.GroupLayout(FlowFilterBySearch);
        FlowFilterBySearch.setLayout(FlowFilterBySearchLayout);
        FlowFilterBySearchLayout.setHorizontalGroup(
            FlowFilterBySearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterBySearchLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(FlowFilterBySearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FlowFilterSearchCaseSensitive)
                    .addComponent(FlowFilterSearchRegex))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FlowFilterBySearchLayout.createSequentialGroup()
                        .addComponent(FlowFilterSearchRequest)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FlowFilterSearchResponse))
                    .addComponent(FlowFilterSearchNegative))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(FlowFilterBySearchLayout.createSequentialGroup()
                .addComponent(FlowFilterSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        FlowFilterBySearchLayout.setVerticalGroup(
            FlowFilterBySearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterBySearchLayout.createSequentialGroup()
                .addComponent(FlowFilterSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSearchCaseSensitive)
                    .addComponent(FlowFilterSearchNegative))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSearchRegex)
                    .addComponent(FlowFilterSearchRequest)
                    .addComponent(FlowFilterSearchResponse)))
        );

        FlowFilterBySource.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter search"));

        FlowFilterSourceTarget.setText("Target");

        FlowFilterSourceSpider.setText("Spider");

        FlowFilterSourceScanner.setText("Scanner");

        FlowFilterSourceRepeater.setText("Repeater");

        FlowFilterSourceIntruder.setText("Intruder");

        FlowFilterSourceExtender.setText("Extender");

        FlowFilterSourceTargetOnly.setText("Only");

        FlowFilterSourceSpiderOnly.setText("Only");

        FlowFilterSourceScannerOnly.setText("Only");

        FlowFilterSourceRepeaterOnly.setText("Only");

        FlowFilterSourceIntruderOnly.setText("Only");

        FlowFilterSourceExtenderOnly.setText("Only");

        FlowFilterSourceProxy.setText("Proxy");

        FlowFilterSourceProxyOnly.setText("Only");

        javax.swing.GroupLayout FlowFilterBySourceLayout = new javax.swing.GroupLayout(FlowFilterBySource);
        FlowFilterBySource.setLayout(FlowFilterBySourceLayout);
        FlowFilterBySourceLayout.setHorizontalGroup(
            FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceExtender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceExtenderOnly))
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceTarget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceTargetOnly))
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceSpider)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceSpiderOnly))
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceScanner)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceScannerOnly))
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceRepeater)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceRepeaterOnly))
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceIntruder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceIntruderOnly))
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterSourceProxy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterSourceProxyOnly))
        );
        FlowFilterBySourceLayout.setVerticalGroup(
            FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterBySourceLayout.createSequentialGroup()
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceTarget)
                    .addComponent(FlowFilterSourceTargetOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceProxy)
                    .addComponent(FlowFilterSourceProxyOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceSpider)
                    .addComponent(FlowFilterSourceSpiderOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceScanner)
                    .addComponent(FlowFilterSourceScannerOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceRepeater)
                    .addComponent(FlowFilterSourceRepeaterOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceIntruder)
                    .addComponent(FlowFilterSourceIntruderOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterSourceExtender)
                    .addComponent(FlowFilterSourceExtenderOnly)))
        );

        FlowFilterCaptureBySource.setBorder(javax.swing.BorderFactory.createTitledBorder("Capture source"));

        FlowFilterCaptureSourceTarget.setText("Target");

        FlowFilterCaptureSourceTargetOnly.setText("Only");

        FlowFilterCaptureSourceSpider.setText("Spider");

        FlowFilterCaptureSourceScanner.setText("Scanner");

        FlowFilterCaptureSourceRepeater.setText("Repeater");

        FlowFilterCaptureSourceIntruder.setText("Intruder");

        FlowFilterCaptureSourceExtender.setText("Extender");

        FlowFilterCaptureSourceSpiderOnly.setText("Only");

        FlowFilterCaptureSourceScannerOnly.setText("Only");

        FlowFilterCaptureSourceRepeaterOnly.setText("Only");

        FlowFilterCaptureSourceIntruderOnly.setText("Only");

        FlowFilterCaptureSourceExtenderOnly.setText("Only");

        FlowFilterCaptureSourceProxy.setText("Proxy");

        FlowFilterCaptureSourceProxyOnly.setText("Only");

        javax.swing.GroupLayout FlowFilterCaptureBySourceLayout = new javax.swing.GroupLayout(FlowFilterCaptureBySource);
        FlowFilterCaptureBySource.setLayout(FlowFilterCaptureBySourceLayout);
        FlowFilterCaptureBySourceLayout.setHorizontalGroup(
            FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceTarget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceTargetOnly))
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceExtender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceExtenderOnly))
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceSpider)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceSpiderOnly))
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceScanner)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceScannerOnly))
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceRepeater)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceRepeaterOnly))
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceIntruder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceIntruderOnly))
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addComponent(FlowFilterCaptureSourceProxy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FlowFilterCaptureSourceProxyOnly))
        );
        FlowFilterCaptureBySourceLayout.setVerticalGroup(
            FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FlowFilterCaptureBySourceLayout.createSequentialGroup()
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceTarget)
                    .addComponent(FlowFilterCaptureSourceTargetOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceProxy)
                    .addComponent(FlowFilterCaptureSourceProxyOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceSpider)
                    .addComponent(FlowFilterCaptureSourceSpiderOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceScanner)
                    .addComponent(FlowFilterCaptureSourceScannerOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceRepeater)
                    .addComponent(FlowFilterCaptureSourceRepeaterOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceIntruder)
                    .addComponent(FlowFilterCaptureSourceIntruderOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FlowFilterCaptureBySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FlowFilterCaptureSourceExtender)
                    .addComponent(FlowFilterCaptureSourceExtenderOnly))
                .addContainerGap())
        );

        javax.swing.GroupLayout FlowFilterBottomLayout = new javax.swing.GroupLayout(FlowFilterBottom);
        FlowFilterBottom.setLayout(FlowFilterBottomLayout);
        FlowFilterBottomLayout.setHorizontalGroup(
            FlowFilterBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        FlowFilterBottomLayout.setVerticalGroup(
            FlowFilterBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(flowFilterHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(flowFilterDefaults, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FlowFilterBySearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FlowFilterByReqType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FlowFilterBySource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FlowFilterCaptureBySource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FlowFilterBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(FlowFilterCaptureBySource, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FlowFilterBySource, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FlowFilterBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(flowFilterHelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flowFilterDefaults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(FlowFilterByReqType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FlowFilterBySearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        flowFilterDefaults.getAccessibleContext().setAccessibleDescription("");
        FlowFilterBySource.getAccessibleContext().setAccessibleName("Filter source");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FlowFilterBottom;
    private javax.swing.JPanel FlowFilterByReqType;
    private javax.swing.JPanel FlowFilterBySearch;
    private javax.swing.JPanel FlowFilterBySource;
    private javax.swing.JPanel FlowFilterCaptureBySource;
    private javax.swing.JCheckBox FlowFilterCaptureSourceExtender;
    private javax.swing.JCheckBox FlowFilterCaptureSourceExtenderOnly;
    private javax.swing.JCheckBox FlowFilterCaptureSourceIntruder;
    private javax.swing.JCheckBox FlowFilterCaptureSourceIntruderOnly;
    private javax.swing.JCheckBox FlowFilterCaptureSourceProxy;
    private javax.swing.JCheckBox FlowFilterCaptureSourceProxyOnly;
    private javax.swing.JCheckBox FlowFilterCaptureSourceRepeater;
    private javax.swing.JCheckBox FlowFilterCaptureSourceRepeaterOnly;
    private javax.swing.JCheckBox FlowFilterCaptureSourceScanner;
    private javax.swing.JCheckBox FlowFilterCaptureSourceScannerOnly;
    private javax.swing.JCheckBox FlowFilterCaptureSourceSpider;
    private javax.swing.JCheckBox FlowFilterCaptureSourceSpiderOnly;
    private javax.swing.JCheckBox FlowFilterCaptureSourceTarget;
    private javax.swing.JCheckBox FlowFilterCaptureSourceTargetOnly;
    private javax.swing.JCheckBox FlowFilterInscope;
    private javax.swing.JCheckBox FlowFilterOutofscope;
    private javax.swing.JCheckBox FlowFilterParametrized;
    private javax.swing.JCheckBox FlowFilterSearchCaseSensitive;
    private javax.swing.JTextField FlowFilterSearchField;
    private javax.swing.JCheckBox FlowFilterSearchNegative;
    private javax.swing.JCheckBox FlowFilterSearchRegex;
    private javax.swing.JCheckBox FlowFilterSearchRequest;
    private javax.swing.JCheckBox FlowFilterSearchResponse;
    private javax.swing.JCheckBox FlowFilterSourceExtender;
    private javax.swing.JCheckBox FlowFilterSourceExtenderOnly;
    private javax.swing.JCheckBox FlowFilterSourceIntruder;
    private javax.swing.JCheckBox FlowFilterSourceIntruderOnly;
    private javax.swing.JCheckBox FlowFilterSourceProxy;
    private javax.swing.JCheckBox FlowFilterSourceProxyOnly;
    private javax.swing.JCheckBox FlowFilterSourceRepeater;
    private javax.swing.JCheckBox FlowFilterSourceRepeaterOnly;
    private javax.swing.JCheckBox FlowFilterSourceScanner;
    private javax.swing.JCheckBox FlowFilterSourceScannerOnly;
    private javax.swing.JCheckBox FlowFilterSourceSpider;
    private javax.swing.JCheckBox FlowFilterSourceSpiderOnly;
    private javax.swing.JCheckBox FlowFilterSourceTarget;
    private javax.swing.JCheckBox FlowFilterSourceTargetOnly;
    private javax.swing.JButton flowFilterDefaults;
    private javax.swing.JButton flowFilterHelp;
    // End of variables declaration//GEN-END:variables
}
