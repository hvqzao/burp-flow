// Flow Burp Extension, (c) 2015-2019 Marcin Woloszyn (@hvqzao), Released under MIT license
package hvqzao.flow;

import hvqzao.flow.ui.DialogWrapper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import burp.IBurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IExtensionStateListener;
import burp.IHttpListener;
import burp.IHttpRequestResponse;
import burp.IHttpRequestResponsePersisted;
import burp.IHttpService;
import burp.IMessageEditor;
import burp.IMessageEditorController;
import burp.IParameter;
import burp.IRequestInfo;
import burp.IResponseInfo;
import burp.IScopeChangeListener;
import burp.ITab;
import hvqzao.flow.ui.BooleanTableCellRenderer;
import hvqzao.flow.ui.ThemeHelper;
import java.awt.Dialog;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

public class FlowExtension implements IBurpExtender, ITab, IHttpListener, IScopeChangeListener, IExtensionStateListener {

    private final String version = "Flow v1.24 (2019-05-20)";
    // Changes in v1.24:
    // - rows coloring is now disabled on dark theme
    // - reflections count display limit introduced
    //
    // Changes in v1.23:
    // - Center "Add new sitemap issue" dialog, resize according to preferred size
    // - Filter by search term does not break window anymore when search is too long
    // - Displaying path instead of pathQuery in URL column
    // - Added out-of-scope filter
    //
    //private final String versionFull = "<html>" + version + ", <a href=\"https://github.com/hvqzao/burp-flow\">https://github.com/hvqzao/burp-flow</a>, MIT license</html>";
    private static IBurpExtenderCallbacks callbacks;
    private static IExtensionHelpers helpers;
    private JPanel flowComponent;
    private JSplitPane flowTab;
    private FlowTableModel flowTableModel;
    private TableRowSorter<FlowTableModel> flowTableSorter;
    private final ArrayList<FlowEntry> flow = new ArrayList<>();
    private static final ArrayList<FlowEntry> FLOW_INCOMPLETE = new ArrayList<>();
    private FlowTable flowTable;
    private FlowEntryEditor flowEntryEditor;
    private IMessageEditor flowReqView;
    private IMessageEditor flowRespView;
    private FlowEntry flowCurrentEntry;
    private boolean flowFilterPopupReady;
    private JLabel flowFilter;
    private JCheckBox flowFilterInscope;
    private JCheckBox flowFilterOutofscope;
    private JCheckBox flowFilterParametrized;
    private JTextField flowFilterSearchField;
    private JCheckBox flowFilterSearchCaseSensitive;
    private JCheckBox flowFilterSearchNegative;
    private JCheckBox flowFilterSearchRegex;
    private JCheckBox flowFilterSearchRequest;
    private JCheckBox flowFilterSearchResponse;
    private JCheckBox flowFilterSourceTarget;
    private JCheckBox flowFilterSourceTargetOnly;
    private JCheckBox flowFilterSourceProxy;
    private JCheckBox flowFilterSourceProxyOnly;
    private JCheckBox flowFilterSourceSpider;
    private JCheckBox flowFilterSourceSpiderOnly;
    private JCheckBox flowFilterSourceScanner;
    private JCheckBox flowFilterSourceScannerOnly;
    private JCheckBox flowFilterSourceRepeater;
    private JCheckBox flowFilterSourceRepeaterOnly;
    private JCheckBox flowFilterSourceIntruder;
    private JCheckBox flowFilterSourceIntruderOnly;
    private JCheckBox flowFilterSourceExtender;
    private JCheckBox flowFilterSourceExtenderOnly;
    private JPanel flowFilterBottom;
    private FlowTablePopup flowTablePopup;
    private FlowEntry popupPointedFlowEntry;
    private FlowTableCellRenderer flowTableCellRenderer;
    private JFrame burpFrame;
    private boolean burpFree;
    // private boolean flowFilterHelpExtPopupReady;
    private boolean flowFilterSourceTargetOnlyOrig;
    private boolean flowFilterSourceProxyOnlyOrig;
    private boolean flowFilterSourceSpiderOnlyOrig;
    private boolean flowFilterSourceScannerOnlyOrig;
    private boolean flowFilterSourceRepeaterOnlyOrig;
    private boolean flowFilterSourceIntruderOnlyOrig;
    private boolean flowFilterSourceExtenderOnlyOrig;
    private JCheckBox flowFilterCaptureSourceTarget;
    private JCheckBox flowFilterCaptureSourceTargetOnly;
    private JCheckBox flowFilterCaptureSourceProxy;
    private JCheckBox flowFilterCaptureSourceProxyOnly;
    private JCheckBox flowFilterCaptureSourceSpider;
    private JCheckBox flowFilterCaptureSourceSpiderOnly;
    private JCheckBox flowFilterCaptureSourceScanner;
    private JCheckBox flowFilterCaptureSourceScannerOnly;
    private JCheckBox flowFilterCaptureSourceRepeater;
    private JCheckBox flowFilterCaptureSourceRepeaterOnly;
    private JCheckBox flowFilterCaptureSourceIntruder;
    private JCheckBox flowFilterCaptureSourceIntruderOnly;
    private JCheckBox flowFilterCaptureSourceExtender;
    private JCheckBox flowFilterCaptureSourceExtenderOnly;
    private boolean flowFilterCaptureSourceTargetOnlyOrig;
    private boolean flowFilterCaptureSourceProxyOnlyOrig;
    private boolean flowFilterCaptureSourceSpiderOnlyOrig;
    private boolean flowFilterCaptureSourceScannerOnlyOrig;
    private boolean flowFilterCaptureSourceRepeaterOnlyOrig;
    private boolean flowFilterCaptureSourceIntruderOnlyOrig;
    private boolean flowFilterCaptureSourceExtenderOnlyOrig;
    private final ArrayList<JDialog> dialogs = new ArrayList<>();
    // private static final int adHeight = 42;
    // private String[] ads = {
    // "<html><p style=\"text-align: center\">Generate beautiful, concise, MS Word-templated reports from Burp, HP WebInspect or just manually written yaml/json files in minutes! It is completely cost-free, opensource, solution! Visit <a href=\"http://github.com/hvqzao/report-ng\">http://github.com/hvqzao/report-ng</a> to learn more.</p></html>",
    // "<html><p style=\"text-align: center\">Are you tired seeing of too many Burp UI main tabs because of number of extensions Installed? Check out \"Wildcard\" solution <a href=\"http://github.com/hvqzao/burp-wildcard\">http://github.com/hvqzao/burp-wildcard</a> - an extension banned from official BApp Store because of UI *hacking*! :)</p></html>",
    // version };
    // private int adIndex = 0;
    // private JLabel flowFilterAd;
    private SeparateViewDialog separateView;
    private JButton flowFilterHelpExt;
    private ImageIcon iconHelp;
    private boolean modalResult;
    private int modalMode;
    private int mode = 0;
    private JScrollPane flowTableScroll;
    private boolean autoDelete = false;
    private int autoDeleteKeep = 1000;
    private boolean autoPopulate = false;
    private boolean showReflections = true;
    private int showReflectionsCount;
    private final int showReflectionsCountMax = 50;
    private final int showReflectionsCountDefault = 10;
    private boolean modalAutoPopulate;
    private boolean modalAutoDelete;
    private int modalAutoDeleteKeep;
    private boolean modalShowReflections;
    private int modalShowReflectionsCount;
    private static PrintWriter stderr;
    private FilterWorker flowFilterWorker;
    private static int sortOrder;
    private String flowFilterText = "";
    private static FlowExtension instance;

    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {
        instance = this;
        FlowExtension.callbacks = callbacks;
        helpers = callbacks.getHelpers();
        stderr = new PrintWriter(callbacks.getStderr(), true);
        // set extension name
        callbacks.setExtensionName("Flow");
        // detect burp
        burpFree = String.valueOf(callbacks.getBurpVersion()[0]).equals("Burp Suite Free Edition") || String.valueOf(callbacks.getBurpVersion()[0]).equals("Burp Suite Community Edition");
        // draw UI
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // images
                iconHelp = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/panel_help.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
                ImageIcon iconDefaults = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/panel_defaults.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
                ImageIcon iconNewWindow = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/newwindow.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
                ImageIcon iconCheckbox = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/checkbox.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
                Dimension iconDimension = new Dimension(24, 24);
                
                // flow tab prolog: vertical split
                flowTab = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                callbacks.customizeUiComponent(flowTab);
                // top: table
                JPanel flowTablePane = new JPanel();
                flowTablePane.setLayout(new BorderLayout());
                // filter
                final JPanel flowFilterPane = new JPanel();
                flowFilterPane.setLayout(new BorderLayout());
                flowFilterPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                // JTextField flowFilter = new JTextField("");
                flowFilter = new JLabel(""); // "Filter: Showing all items");
                flowFilter.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                flowFilterPane.add(flowFilter, BorderLayout.CENTER);

                // filter popup
                // actions
                ActionListener flowFilterScopeUpdateAction = new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (!flowFilterSourceTargetOnly.isSelected() && !flowFilterSourceProxyOnly.isSelected() && !flowFilterSourceSpiderOnly.isSelected() && !flowFilterSourceScannerOnly.isSelected() && !flowFilterSourceRepeaterOnly.isSelected() && !flowFilterSourceIntruderOnly.isSelected() && !flowFilterSourceExtenderOnly.isSelected()) {
                            flowFilterSourceTargetOnlyOrig = flowFilterSourceTarget.isSelected();
                            flowFilterSourceProxyOnlyOrig = flowFilterSourceProxy.isSelected();
                            flowFilterSourceSpiderOnlyOrig = flowFilterSourceSpider.isSelected();
                            flowFilterSourceScannerOnlyOrig = flowFilterSourceScanner.isSelected();
                            flowFilterSourceRepeaterOnlyOrig = flowFilterSourceRepeater.isSelected();
                            flowFilterSourceIntruderOnlyOrig = flowFilterSourceIntruder.isSelected();
                            flowFilterSourceExtenderOnlyOrig = flowFilterSourceExtender.isSelected();
                        }
                        flowFilterUpdate();
                    }
                };
                ActionListener flowFilterCaptureScopeUpdateAction = new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (!flowFilterCaptureSourceTargetOnly.isSelected() && !flowFilterCaptureSourceProxyOnly.isSelected() && !flowFilterCaptureSourceSpiderOnly.isSelected() && !flowFilterCaptureSourceScannerOnly.isSelected() && !flowFilterCaptureSourceRepeaterOnly.isSelected() && !flowFilterCaptureSourceIntruderOnly.isSelected() && !flowFilterCaptureSourceExtenderOnly.isSelected()) {
                            flowFilterCaptureSourceTargetOnlyOrig = flowFilterCaptureSourceTarget.isSelected();
                            flowFilterCaptureSourceProxyOnlyOrig = flowFilterCaptureSourceProxy.isSelected();
                            flowFilterCaptureSourceSpiderOnlyOrig = flowFilterCaptureSourceSpider.isSelected();
                            flowFilterCaptureSourceScannerOnlyOrig = flowFilterCaptureSourceScanner.isSelected();
                            flowFilterCaptureSourceRepeaterOnlyOrig = flowFilterCaptureSourceRepeater.isSelected();
                            flowFilterCaptureSourceIntruderOnlyOrig = flowFilterCaptureSourceIntruder.isSelected();
                            flowFilterCaptureSourceExtenderOnlyOrig = flowFilterCaptureSourceExtender.isSelected();
                        }
                        flowFilterUpdateDescription(true);
                    }
                };
                // layout
                final JDialog flowFilterPopupWindow = new JDialog();
                FlowFilterPopup flowFilterPopup = new FlowFilterPopup();
                flowFilterCaptureSourceTarget = flowFilterPopup.getFlowFilterCaptureSourceTarget();
                flowFilterCaptureSourceTargetOnly = flowFilterPopup.getFlowFilterCaptureSourceTargetOnly();
                flowFilterCaptureSourceProxy = flowFilterPopup.getFlowFilterCaptureSourceProxy();
                flowFilterCaptureSourceProxyOnly = flowFilterPopup.getFlowFilterCaptureSourceProxyOnly();
                flowFilterCaptureSourceSpider = flowFilterPopup.getFlowFilterCaptureSourceSpider();
                flowFilterCaptureSourceSpiderOnly = flowFilterPopup.getFlowFilterCaptureSourceSpiderOnly();
                flowFilterCaptureSourceScanner = flowFilterPopup.getFlowFilterCaptureSourceScanner();
                flowFilterCaptureSourceScannerOnly = flowFilterPopup.getFlowFilterCaptureSourceScannerOnly();
                flowFilterCaptureSourceRepeater = flowFilterPopup.getFlowFilterCaptureSourceRepeater();
                flowFilterCaptureSourceRepeaterOnly = flowFilterPopup.getFlowFilterCaptureSourceRepeaterOnly();
                flowFilterCaptureSourceIntruder = flowFilterPopup.getFlowFilterCaptureSourceIntruder();
                flowFilterCaptureSourceIntruderOnly = flowFilterPopup.getFlowFilterCaptureSourceIntruderOnly();
                flowFilterCaptureSourceExtender = flowFilterPopup.getFlowFilterCaptureSourceExtender();
                flowFilterCaptureSourceExtenderOnly = flowFilterPopup.getFlowFilterCaptureSourceExtenderOnly();
                flowFilterInscope = flowFilterPopup.getFlowFilterInscope();
                flowFilterOutofscope = flowFilterPopup.getFlowFilterOutofscope();
                flowFilterParametrized = flowFilterPopup.getFlowFilterParametrized();
                flowFilterSearchCaseSensitive = flowFilterPopup.getFlowFilterSearchCaseSensitive();
                flowFilterSearchField = flowFilterPopup.getFlowFilterSearchField();
                flowFilterSearchNegative = flowFilterPopup.getFlowFilterSearchNegative();
                flowFilterSearchRegex = flowFilterPopup.getFlowFilterSearchRegex();
                flowFilterSearchRequest = flowFilterPopup.getFlowFilterSearchRequest();
                flowFilterSearchResponse = flowFilterPopup.getFlowFilterSearchResponse();
                flowFilterSourceExtender = flowFilterPopup.getFlowFilterSourceExtender();
                flowFilterSourceExtenderOnly = flowFilterPopup.getFlowFilterSourceExtenderOnly();
                flowFilterSourceIntruder = flowFilterPopup.getFlowFilterSourceIntruder();
                flowFilterSourceIntruderOnly = flowFilterPopup.getFlowFilterSourceIntruderOnly();
                flowFilterSourceTarget = flowFilterPopup.getFlowFilterSourceTarget();
                flowFilterSourceTargetOnly = flowFilterPopup.getFlowFilterSourceTargetOnly();
                flowFilterSourceProxy = flowFilterPopup.getFlowFilterSourceProxy();
                flowFilterSourceProxyOnly = flowFilterPopup.getFlowFilterSourceProxyOnly();
                flowFilterSourceRepeater = flowFilterPopup.getFlowFilterSourceRepeater();
                flowFilterSourceRepeaterOnly = flowFilterPopup.getFlowFilterSourceRepeaterOnly();
                flowFilterSourceScanner = flowFilterPopup.getFlowFilterSourceScanner();
                flowFilterSourceScannerOnly = flowFilterPopup.getFlowFilterSourceScannerOnly();
                flowFilterSourceSpider = flowFilterPopup.getFlowFilterSourceSpider();
                flowFilterSourceSpiderOnly = flowFilterPopup.getFlowFilterSourceSpiderOnly();
                flowFilterBottom = flowFilterPopup.getFlowFilterBottom();

                flowFilterSourceTarget.setSelected(true); // TODO?
                flowFilterSourceTargetOnlyOrig = true;
                //flowFilterSourceProxy.setSelected(true); // TODO?
                flowFilterSourceProxyOnlyOrig = true;
                flowFilterSourceSpider.setSelected(true);
                flowFilterSourceSpiderOnlyOrig = true;
                flowFilterSourceScanner.setEnabled(!burpFree);
                flowFilterSourceScanner.setSelected(!burpFree);
                flowFilterSourceScannerOnly.setEnabled(!burpFree);
                flowFilterSourceScannerOnlyOrig = !burpFree;
                flowFilterSourceRepeater.setSelected(true);
                flowFilterSourceRepeaterOnlyOrig = true;
                flowFilterSourceIntruder.setSelected(true);
                flowFilterSourceIntruderOnlyOrig = true;
                flowFilterSourceExtender.setSelected(true);
                flowFilterSourceExtenderOnlyOrig = true;
                flowFilterCaptureSourceScanner.setEnabled(!burpFree);
                flowFilterCaptureSourceScanner.setSelected(!burpFree);
                flowFilterCaptureSourceScannerOnly.setEnabled(!burpFree);
                flowFilterCaptureSourceScannerOnly.setSelected(!burpFree);
                flowFilterCaptureSourceScannerOnlyOrig = !burpFree;

                JButton flowFilterHelp;
                flowFilterHelp = flowFilterPopup.getFlowFilterHelp();
                flowFilterHelp.setIcon(iconHelp);
                flowFilterHelp.setEnabled(false);
                callbacks.customizeUiComponent(flowFilterHelp);

                JButton flowFilterDefaults;
                flowFilterDefaults = flowFilterPopup.getFlowFilterDefaults();
                flowFilterDefaults.setIcon(iconDefaults);
                callbacks.customizeUiComponent(flowFilterDefaults);
                flowFilterDefaults.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSetDefaults();
                    }
                });

                flowFilterSearchField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        process();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        process();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        process();
                    }

                    private void process() {
                        flowFilterUpdate();
                    }
                });
                //flowFilterInscope.addActionListener(flowFilterScopeUpdateAction);
                //flowFilterOutofscope.addActionListener(flowFilterScopeUpdateAction);
                flowFilterInscope.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterOutofscope.setSelected(false);
                        flowFilterUpdate();
                    }
                });
                flowFilterOutofscope.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterInscope.setSelected(false);
                        flowFilterUpdate();
                    }
                });
                flowFilterParametrized.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchCaseSensitive.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchNegative.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchRegex.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchRequest.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchResponse.addActionListener(flowFilterScopeUpdateAction);

                flowFilterSourceTarget.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceProxy.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceSpider.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceScanner.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceRepeater.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceIntruder.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceExtender.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceTargetOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceTargetOnly);
                    }
                });
                flowFilterSourceProxyOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceProxyOnly);
                    }
                });
                flowFilterSourceSpiderOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceSpiderOnly);
                    }
                });
                flowFilterSourceScannerOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceScannerOnly);
                    }
                });
                flowFilterSourceRepeaterOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceRepeaterOnly);
                    }
                });
                flowFilterSourceIntruderOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceIntruderOnly);
                    }
                });
                flowFilterSourceExtenderOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterSourceOnly(flowFilterSourceExtenderOnly);
                    }
                });

                flowFilterCaptureSourceTarget.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceProxy.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceSpider.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceScanner.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceRepeater.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceIntruder.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceExtender.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceTargetOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceTargetOnly);
                    }
                });
                flowFilterCaptureSourceProxyOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceProxyOnly);
                    }
                });
                flowFilterCaptureSourceSpiderOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceSpiderOnly);
                    }
                });
                flowFilterCaptureSourceScannerOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceScannerOnly);
                    }
                });
                flowFilterCaptureSourceRepeaterOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceRepeaterOnly);
                    }
                });
                flowFilterCaptureSourceIntruderOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceIntruderOnly);
                    }
                });
                flowFilterCaptureSourceExtenderOnly.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flowFilterCaptureSourceOnly(flowFilterCaptureSourceExtenderOnly);
                    }
                });

                flowFilterPopupReady = true;
                flowFilterPopupWindow.setUndecorated(true);
                flowFilterPopupWindow.add(flowFilterPopup);
                flowFilterPopupWindow.pack();
                flowFilter.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (flowFilterPopupReady) {
                            flowFilterPopupReady = false;
                            flowFilterPopupWindow.addWindowFocusListener(new WindowFocusListener() {

                                @Override
                                public void windowLostFocus(WindowEvent e) {
                                    flowFilterPopupWindow.setVisible(false);
                                    //flowFilterPopupWindow.dispose();
                                    SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            flowFilterPopupReady = true;
                                        }
                                    });
                                }

                                @Override
                                public void windowGainedFocus(WindowEvent e) {
                                }

                            });
                            // flowFilterAd.setText(ads[adIndex]);
                            // adIndex += 1;
                            // if (adIndex >= ads.length) {
                            // adIndex = 0;
                            // }
                            // dialog.setLocationRelativeTo(null);
                            Point flowFilterPT = flowFilter.getLocationOnScreen();
                            flowFilterPopupWindow.setLocation(new Point((int) flowFilterPT.getX() - 2, flowFilterPT.y + flowFilter.getHeight() + 1));
                            flowFilterBottom.requestFocus();
                            flowFilterPopupWindow.setVisible(true);
                            //flowFilterPopupWindow.repaint();
                            //flowTab.repaint();
                        }
                    }
                });

                // filter help
                JPanel flowFilterHelpPane = new JPanel();
                flowFilterHelpPane.setBorder(BorderFactory.createEmptyBorder(-4, 4, 0, 0));
                flowFilterHelpPane.setPreferredSize(new Dimension(26 + 10 + 26, 24));
                //
                JButton flowFilterHelpOpt = new JButton(iconCheckbox);
                flowFilterHelpOpt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                flowFilterHelpOpt.setPreferredSize(iconDimension);
                flowFilterHelpOpt.setMaximumSize(iconDimension);
                callbacks.customizeUiComponent(flowFilterHelpOpt);
                flowFilterHelpPane.add(flowFilterHelpOpt);
                // final JButton flowFilterHelpExt = new JButton(iconHelp);
                flowFilterHelpExt = new JButton(iconNewWindow);
                flowFilterHelpExt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                flowFilterHelpExt.setPreferredSize(iconDimension);
                flowFilterHelpExt.setMaximumSize(iconDimension);
                flowFilterHelpExt.setToolTipText(version);
                callbacks.customizeUiComponent(flowFilterHelpExt);
                flowFilterHelpExt.setEnabled(true);
                flowFilterHelpPane.add(flowFilterHelpExt);
                flowFilterPane.add(flowFilterHelpPane, BorderLayout.LINE_END);
                flowTablePane.add(flowFilterPane, BorderLayout.PAGE_START);
                //// flow filterHelpExt popup
                //final JDialog flowFilterHelpExtPopupWindow = new JDialog();
                //final JLabel flowFilterHelpExtPopup = new JLabel(version);
                //// flowFilterHelpExtPopup.setBorder(BorderFactory.createLineBorder(Color.darkGray));
                //// flowFilterHelpExtPopup.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
                //flowFilterHelpExtPopup.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray), BorderFactory.createEmptyBorder(2, 20, 2, 20)));
                //flowFilterHelpExtPopup.setBackground(Color.lightGray);
                ////
                //flowFilterHelpExtPopupReady = true;
                //flowFilterHelpExt.addMouseListener(new MouseAdapter() {
                //    public void mousePressed(MouseEvent e) {
                //        if (flowFilterHelpExtPopupReady) {
                //            flowFilterHelpExtPopupReady = false;
                //            flowFilterHelpExtPopupWindow.addWindowFocusListener(new WindowFocusListener() {
                //
                //                @Override
                //                public void windowLostFocus(WindowEvent e) {
                //                    flowFilterHelpExtPopupWindow.dispose();
                //                    SwingUtilities.invokeLater(new Runnable() {
                //
                //                        @Override
                //                        public void run() {
                //                            flowFilterHelpExtPopupReady = true;
                //                        }
                //                    });
                //                }
                //
                //                @Override
                //                public void windowGainedFocus(WindowEvent e) {
                //                }
                //
                //            });
                //            flowFilterHelpExtPopupWindow.setUndecorated(true);
                //            flowFilterHelpExtPopupWindow.add(flowFilterHelpExtPopup);
                //            flowFilterHelpExtPopupWindow.pack();
                //            // dialog.setLocationRelativeTo(null);
                //            Point flowFilterHelpExtPT = flowFilterHelpExt.getLocationOnScreen();
                //            int x = (int) flowFilterHelpExtPT.getX() - 2 - flowFilterHelpExtPopup.getWidth() + 2; // - 7;
                //            if (x < 0) {
                //                x = 0;
                //            }
                //            flowFilterHelpExtPopupWindow.setLocation(new Point(x, flowFilterHelpExtPT.y + 2)); // + 1
                //            // ....requestFocus();
                //            flowFilterHelpExtPopupWindow.setVisible(true);
                //        }
                //    }
                //});
                flowFilterHelpExt.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                if (separateView == null) {
                                    separateView = new SeparateViewDialog(burpFrame, "Flow");
                                }
                            }
                        });
                    }

                });

                // table
                flowTableModel = new FlowTableModel();
                flowTableSorter = new TableRowSorter<>(flowTableModel);
                flowTable = new FlowTable(flowTableModel);
                flowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                //flowTable.setAutoCreateRowSorter(true);
                flowTable.setRowSorter(flowTableSorter);
                for (int i = 0; i < flowTableModel.getColumnCount(); i++) {
                    TableColumn column = flowTable.getColumnModel().getColumn(i);
                    column.setMinWidth(20);
                    column.setPreferredWidth(flowTableModel.getPreferredWidth(i));
                }
                flowTableSorter.addRowSorterListener(new RowSorterListener() {
                    @Override
                    public void sorterChanged(RowSorterEvent e) {
                        if (e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
                            List<? extends RowSorter.SortKey> keys = flowTableSorter.getSortKeys();
                            if (keys.isEmpty() == false) {
                                RowSorter.SortKey key = keys.get(0);
                                int order = 0;
                                if (key.getSortOrder() == SortOrder.ASCENDING) {
                                    order = 1;
                                }
                                if (key.getSortOrder() == SortOrder.DESCENDING) {
                                    order = -1;
                                }
                                sortOrder = order;
                            }
                        }
                    }
                });
                callbacks.customizeUiComponent(flowTable);
                flowTableScroll = new JScrollPane(flowTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                flowTableScroll.setMinimumSize(new Dimension(40, 40));
                callbacks.customizeUiComponent(flowTableScroll);
                flowTable.setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());
                flowTable.getTableHeader().setReorderingAllowed(true);
                // flowTab.setTopComponent(flowTableScroll);
                flowTablePane.add(flowTableScroll, BorderLayout.CENTER);
                flowTab.setTopComponent(flowTablePane);
                // sort index column in descending order
                flowTableSorter.toggleSortOrder(0);
                flowTableSorter.toggleSortOrder(0);
                // flowTable popup
                flowTablePopup = new FlowTablePopup();
                flowTable.setComponentPopupMenu(flowTablePopup);
                // flowTable renderer
                flowTableCellRenderer = new FlowTableCellRenderer();
                flowTable.setDefaultRenderer(Object.class, flowTableCellRenderer);
                // flow bottom prolog: request & response
                JSplitPane flowViewPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                flowViewPane.setResizeWeight(.5d);
                callbacks.customizeUiComponent(flowViewPane);
                flowEntryEditor = new FlowEntryEditor();
                // req
                JPanel flowReqPane = new JPanel();
                flowReqPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                flowReqPane.setLayout(new BorderLayout());
                // JLabel flowReqLabel = new JLabel("<html><b style='color:#e58900;font-size:10px'>Request</b></html>");
                // flowReqLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                // flowReqPane.add(flowReqLabel, BorderLayout.PAGE_START);
                //
                flowViewPane.setLeftComponent(flowReqPane);
                flowReqView = callbacks.createMessageEditor((IMessageEditorController) flowEntryEditor, false);
                flowReqPane.add(flowReqView.getComponent(), BorderLayout.CENTER);
                // resp
                JPanel flowRespPane = new JPanel();
                flowRespPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                flowRespPane.setLayout(new BorderLayout());
                // JLabel flowRespLabel = new JLabel("<html><b style='color:#e58900;font-size:10px'>Response</b></html>");
                // flowRespLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                // flowRespPane.add(flowRespLabel, BorderLayout.PAGE_START);
                flowViewPane.setRightComponent(flowRespPane);
                flowRespView = callbacks.createMessageEditor(flowEntryEditor, false);
                flowRespPane.add(flowRespView.getComponent(), BorderLayout.CENTER);
                // flow bottom epilog
                flowTab.setBottomComponent(flowViewPane);
                flowComponent = new JPanel();
                flowComponent.setLayout(new BoxLayout(flowComponent, BoxLayout.X_AXIS));
                flowComponent.add(flowTab);
                separateView = null;
                callbacks.addSuiteTab(FlowExtension.this);
                // get burp frame
                burpFrame = (JFrame) SwingUtilities.getWindowAncestor(flowTab);
                //
                flowFilterHelpOpt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (showOptionsDialog()) {
                            if (mode != modalMode) {
                                mode = modalMode;
                                callbacks.saveExtensionSetting("mode", String.valueOf(mode));
                                flowFilterUpdate();
                            }
                            if (autoPopulate != modalAutoPopulate) {
                                autoPopulate = modalAutoPopulate;
                                callbacks.saveExtensionSetting("autoPopulateProxy", autoPopulate ? "1" : "0");
                            }
                            if (autoDeleteKeep != modalAutoDeleteKeep) {
                                autoDeleteKeep = modalAutoDeleteKeep;
                                callbacks.saveExtensionSetting("autoDeleteKeep", String.valueOf(autoDeleteKeep));
                            }
                            if (autoDelete != modalAutoDelete) {
                                autoDelete = modalAutoDelete;
                                callbacks.saveExtensionSetting("autoDelete", autoDelete ? "1" : "0");
                                if (autoDelete) {
                                    triggerAutoDelete();
                                    flowTableSorter.sort();
                                }
                            }
                            if (showReflections != modalShowReflections) {
                                showReflections = modalShowReflections;
                                callbacks.saveExtensionSetting("showReflections", showReflections ? "1" : "0");
                            }
                            if (showReflectionsCount != modalShowReflectionsCount) {
                                showReflectionsCount = modalShowReflectionsCount;
                                callbacks.saveExtensionSetting("showReflectionsCount", String.valueOf(showReflectionsCount));
                            }
                        }
                    }
                });

                // register ourselves as an HTTP listener
                callbacks.registerHttpListener(FlowExtension.this);
                // extension state listener
                callbacks.registerExtensionStateListener(FlowExtension.this);
                // scope change listener
                callbacks.registerScopeChangeListener(FlowExtension.this);
                //
                if ("1".equals(callbacks.loadExtensionSetting("mode"))) {
                    mode = 1;
                }
                //
                //autoPopulate = "0".equals(callbacks.loadExtensionSetting("autoPopulate")) == false; // will default to true if not set
                autoPopulate = "1".equals(callbacks.loadExtensionSetting("autoPopulateProxy"));
                autoDeleteKeep = validateAutoDeleteKeep(callbacks.loadExtensionSetting("autoDeleteKeep"));
                autoDelete = "1".equals(callbacks.loadExtensionSetting("autoDelete"));
                showReflections = "0".equals(callbacks.loadExtensionSetting("showReflections")) == false;
                showReflectionsCount = parseReflectionsCount(callbacks.loadExtensionSetting("showReflectionsCount"));
                //

                //
                flowFilterSetDefaults();
                callbacks.printOutput(version);
                if (autoPopulate) {
                    //callbacks.printOutput("Initializing extension with contents of Burp Proxy...");
                    (new PopulateWorker(callbacks.getProxyHistory())).execute();
                }

                //Set<Map.Entry<Object, Object>> entries = UIManager.getLookAndFeelDefaults().entrySet();
                //for (Map.Entry<Object, Object> entry : entries) {
                //    if (entry.getValue() instanceof Color) {
                //        stderr.println(String.valueOf(entry.getKey()) + "\t" + String.valueOf(UIManager.getColor(entry.getKey())) + "\t" + String.valueOf(entry.getValue()));
                //    }
                //}

                //callbacks.printOutput("Loaded.");
                // TODO end main
            }
        });
    }

    //
    // implement ITab
    //
    @Override
    public String getTabCaption() {
        return "Flow";
    }

    @Override
    public Component getUiComponent() {
        return flowComponent;
    }

    //
    // implement IHttpListener
    //
    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {

        if (toolFlagCaptureProcessing(toolFlag)) {
            synchronized (flow) {
                if (messageIsRequest) {
                    // both complete and incomplete requests
                    if (mode == 1) {
                        int row = flow.size();
                        try {
                            flow.add(new FlowEntry(toolFlag, messageInfo));
                            flowTableModel.fireTableRowsInserted(row, row);
                        } catch (Exception ex) {
                            // do nothing
                            //ex.printStackTrace(stderr);
                        }
                        triggerAutoDelete();
                    }
                    //stdout.println("[+] " + String.valueOf(helpers.analyzeRequest(messageInfo.getHttpService(), messageInfo.getRequest()).getUrl()));
                    //stdout.println("    " + String.valueOf(flowIncomplete.size()));
                } else {
                    // only requests with responses
                    if (mode == 0) {
                        int row = flow.size();
                        flow.add(new FlowEntry(toolFlag, messageInfo));
                        flowTableModel.fireTableRowsInserted(row, row);
                        triggerAutoDelete();
                    }
                    if (FLOW_INCOMPLETE.size() > 0) {
                        FlowEntry flowIncompleteFound = null;
                        ArrayList<FlowEntry> flowIncompleteCleanup = new ArrayList<>();
                        Date outdated = new Date(System.currentTimeMillis() - 5 * 60 * 1000);
                        for (FlowEntry incomplete : FLOW_INCOMPLETE) {
                            if (flowIncompleteFound == null) {
                                if (incomplete.toolFlag == toolFlag && incomplete.isIncomplete() && incomplete.incomplete.equals(helpers.bytesToString(messageInfo.getRequest()))) {
                                    flowIncompleteFound = incomplete;
                                }
                            }
                            if (incomplete.date.before(outdated)) {
                                flowIncompleteCleanup.add(incomplete);
                            }
                        }
                        if (flowIncompleteFound != null && flowIncompleteCleanup.contains(flowIncompleteFound)) {
                            flowIncompleteCleanup.remove(flowIncompleteFound);
                        }
                        while (!flowIncompleteCleanup.isEmpty()) {
                            FLOW_INCOMPLETE.remove(flowIncompleteCleanup.get(0));
                            flowIncompleteCleanup.remove(0);
                        }
                        if (flowIncompleteFound != null) {
                            flowIncompleteFound.update(messageInfo);
                            if (flowCurrentEntry == flowIncompleteFound) {
                                flowTable.updateResponse();
                            }
                            int row = flow.indexOf(flowIncompleteFound);
                            //callbacks.printError(String.valueOf(row) + " " + String.valueOf(flow.size()));
                            if (mode == 0) {
                                // TODO refresh tableSorter filter trigger to display it
                                flowTableSorter.sort();
                            }
                            //flowTableModel.fireTableDataChanged();
                            ////callbacks.printError(String.valueOf(flow.size()) + ", " + String.valueOf(row));
                            //flowTableModel.fireTableRowsInserted(row, row);
                            //triggerAutoDelete();
                            //flowTableModel.fireTableRowsUpdated(0, row);
                            //} else {
                            flowTableModel.fireTableRowsUpdated(row, row);
                            //}
                            FLOW_INCOMPLETE.remove(flowIncompleteFound);
                            //
                            //flowTableModel.fireTableDataChanged();
                        }
                        //stdout.println("[-] " + String.valueOf(helpers.analyzeRequest(messageInfo.getHttpService(), messageInfo.getRequest()).getUrl()) + " [" + String.valueOf(helpers.analyzeResponse(messageInfo.getResponse()).getStatusCode()) + "]");
                        //stdout.println("    " + String.valueOf(flowIncomplete.size()) + ", match: " + String.valueOf(flowIncompleteFound != null));
                        //callbacks.printOutput(String.valueOf(FLOW_INCOMPLETE.size()));
                    }
                }
            }
        }
    }

//
// implement IScopeChangeListener
//
    @Override
    public void scopeChanged() {
        if (flowFilterInscope.isSelected()) {
            flowFilterUpdate();
        }
    }

    //
    // implement IExtensionStateListener
    //
    @Override
    public void extensionUnloaded() {
        while (!dialogs.isEmpty()) {
            dialogs.get(0).dispose();
        }
    }

    /**
     * Flow table populating SwingWorker.
     *
     */
    class PopulateWorker extends SwingWorker<Object, FlowEntry> {

        private final IHttpRequestResponse[] history;
        private final PrintWriter stdout;

        public PopulateWorker(IHttpRequestResponse[] history) {
            this.history = history;
            stdout = new PrintWriter(callbacks.getStdout(), true);
            stdout.print("Populating log with Burp Proxy history, Please wait... ");
            stdout.flush();
        }

        @Override
        protected Object doInBackground() throws Exception {
            if (history.length > 0) {
                int start = 0;
                if (autoDelete) {
                    start = history.length - autoDeleteKeep;
                    if (start < 0) {
                        start = 0;
                    }
                }
                for (int i = start; i < history.length; i++) {
                    publish(new FlowEntry(IBurpExtenderCallbacks.TOOL_PROXY, history[i]));
                }
            }
            return null;
        }

        @Override
        protected void process(List<FlowEntry> chunks) {
            synchronized (flow) {
                int from = flow.size();
                flow.addAll(chunks);
                int to = flow.size();
                try {
                    flowTableModel.fireTableRowsInserted(from, to - 1);
                } catch (Exception ex) {
                    ex.printStackTrace(stderr);
                }
            }
        }

        @Override
        protected void done() {
            flowTable.repaint();
            stdout.println("done.");
        }
    }

    public static IBurpExtenderCallbacks getCallbacks() {
        return callbacks;
    }

    //
    // misc
    //
    private void triggerAutoDelete() {
        if (autoDelete) {
            while (flow.size() > autoDeleteKeep) {
                flow.remove(0);
                flowTableModel.fireTableRowsDeleted(0, 0);
            }
        }
    }

    private int validateAutoDeleteKeep(String number) {
        int output;
        final int DEFAULT = 1000;
        if (number == null) {
            output = DEFAULT;
        } else {
            try {
                output = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                output = DEFAULT;
            }
        }
        if (output < 50) {
            output = 50;
        }
        return output;
    }

    private boolean showAddNewIssueDialog() {
        final JDialog dialog = new JDialog(burpFrame, "Add New Sitemap Issue", Dialog.ModalityType.DOCUMENT_MODAL);
        DialogWrapper wrapper = new DialogWrapper();
        final FlowAddNewIssue addNewIssue = new FlowAddNewIssue(callbacks, popupPointedFlowEntry);
        // customize options pane
        JButton help = addNewIssue.getHelpButton();
        help.setIcon(iconHelp);
        help.setEnabled(false);
        callbacks.customizeUiComponent(help);
        //
        // wrap optionsPane
        wrapper.getScrollPane().getViewport().add(addNewIssue);
        //dialog.setBounds(100, 100, 1070, 670);
        Dimension dim = ((JComponent) addNewIssue).getPreferredSize();
        dialog.setBounds(100, 100, (int) Math.round(dim.getWidth() * 1.33), (int) Math.round(dim.getHeight() * 1.50));
        dialog.setContentPane(wrapper);
        //
        modalResult = false;
        // presets here
        JButton ok = wrapper.getOkButton();
        callbacks.customizeUiComponent(ok);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addNewIssue.dataValidation()) {
                    modalResult = true;
                    // FUTURE: multiple entries with same IHttpService?
                    //int[] rows = flowTable.getSelectedRows();
                    //for (int i = 0; i < rows.length; i++) {
                    //    rows[i] = flowTable.convertRowIndexToModel(rows[i]);
                    //}
                    //FlowEntry[] selected = new FlowEntry[rows.length];
                    //for (int i=0 ; i < rows.length ; i++) {
                    //    selected[i] = flow.get(rows[i]);
                    //}
                    callbacks.addScanIssue(addNewIssue.getIssue());
                    dialog.dispose();
                }
            }
        });
        JButton cancel = wrapper.getCancelButton();
        callbacks.customizeUiComponent(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        //
        return modalResult;
    }

    private boolean showOptionsDialog() {
        final JDialog dialog = new JDialog(burpFrame, "Flow Extension Options", Dialog.ModalityType.DOCUMENT_MODAL);
        DialogWrapper wrapper = new DialogWrapper();
        final FlowOptionsPane optionsPane = new FlowOptionsPane(callbacks);
        // customize options pane
        JButton modeHelp = optionsPane.getModeHelp();
        modeHelp.setIcon(iconHelp);
        modeHelp.setEnabled(false);
        callbacks.customizeUiComponent(modeHelp);
        //
        JButton miscHelp = optionsPane.getMiscHelp();
        miscHelp.setIcon(iconHelp);
        miscHelp.setEnabled(false);
        callbacks.customizeUiComponent(miscHelp);
        //
        // wrap optionsPane
        wrapper.getScrollPane().getViewport().add(optionsPane);
        dialog.setBounds(100, 100, 526, 500);
        dialog.setContentPane(wrapper);
        //
        modalResult = false;
        if (mode == 0 && optionsPane.getMode2().isSelected()) {
            optionsPane.getMode1().setSelected(true);
        }
        optionsPane.getAutoPopulate().setSelected(autoPopulate);
        optionsPane.getAutoDelete().setSelected(autoDelete);
        optionsPane.getAutoDeleteKeep().setText(String.valueOf(autoDeleteKeep));
        optionsPane.getShowReflections().setSelected(showReflections);
        optionsPane.getShowReflectionsCount().setText(String.valueOf(showReflectionsCount));
        //
        JButton ok = wrapper.getOkButton();
        callbacks.customizeUiComponent(ok);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modalResult = true;
                modalMode = optionsPane.getMode2().isSelected() ? 1 : 0;
                modalAutoPopulate = optionsPane.getAutoPopulate().isSelected();
                modalAutoDelete = optionsPane.getAutoDelete().isSelected();
                modalAutoDeleteKeep = validateAutoDeleteKeep(optionsPane.getAutoDeleteKeep().getText());
                modalShowReflections = optionsPane.getShowReflections().isSelected();
                modalShowReflectionsCount = parseReflectionsCount(optionsPane.getShowReflectionsCount().getText());
                dialog.dispose();
            }
        });
        JButton cancel = wrapper.getCancelButton();
        callbacks.customizeUiComponent(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.setLocationRelativeTo(flowTableScroll);
        dialog.setVisible(true);
        //
        return modalResult;
    }

    private void flowFilterCaptureSourceOnly(JCheckBox which) {

        if (which != flowFilterCaptureSourceTargetOnly && flowFilterCaptureSourceTargetOnly.isSelected()) {
            flowFilterCaptureSourceTargetOnly.setSelected(false);
            flowFilterCaptureSourceTarget.setSelected(flowFilterCaptureSourceTargetOnlyOrig);
        }
        if (which != flowFilterCaptureSourceProxyOnly && flowFilterCaptureSourceProxyOnly.isSelected()) {
            flowFilterCaptureSourceProxyOnly.setSelected(false);
            flowFilterCaptureSourceProxy.setSelected(flowFilterCaptureSourceProxyOnlyOrig);
        }
        if (which != flowFilterCaptureSourceSpiderOnly && flowFilterCaptureSourceSpiderOnly.isSelected()) {
            flowFilterCaptureSourceSpiderOnly.setSelected(false);
            flowFilterCaptureSourceSpider.setSelected(flowFilterCaptureSourceSpiderOnlyOrig);
        }
        if (which != flowFilterCaptureSourceScannerOnly && flowFilterCaptureSourceScannerOnly.isSelected()) {
            flowFilterCaptureSourceScannerOnly.setSelected(false);
            flowFilterCaptureSourceScanner.setSelected(flowFilterCaptureSourceScannerOnlyOrig);
        }
        if (which != flowFilterCaptureSourceRepeaterOnly && flowFilterCaptureSourceRepeaterOnly.isSelected()) {
            flowFilterCaptureSourceRepeaterOnly.setSelected(false);
            flowFilterCaptureSourceRepeater.setSelected(flowFilterCaptureSourceRepeaterOnlyOrig);
        }
        if (which != flowFilterCaptureSourceIntruderOnly && flowFilterCaptureSourceIntruderOnly.isSelected()) {
            flowFilterCaptureSourceIntruderOnly.setSelected(false);
            flowFilterCaptureSourceIntruder.setSelected(flowFilterCaptureSourceIntruderOnlyOrig);
        }
        if (which != flowFilterCaptureSourceExtenderOnly && flowFilterCaptureSourceExtenderOnly.isSelected()) {
            flowFilterCaptureSourceExtenderOnly.setSelected(false);
            flowFilterCaptureSourceExtender.setSelected(flowFilterCaptureSourceExtenderOnlyOrig);
        }

        if (which == flowFilterCaptureSourceTargetOnly && !flowFilterCaptureSourceTargetOnly.isSelected()) {
            flowFilterCaptureSourceTarget.setSelected(flowFilterCaptureSourceTargetOnlyOrig);
        }
        if (which == flowFilterCaptureSourceProxyOnly && !flowFilterCaptureSourceProxyOnly.isSelected()) {
            flowFilterCaptureSourceProxy.setSelected(flowFilterCaptureSourceProxyOnlyOrig);
        }
        if (which == flowFilterCaptureSourceSpiderOnly && !flowFilterCaptureSourceSpiderOnly.isSelected()) {
            flowFilterCaptureSourceSpider.setSelected(flowFilterCaptureSourceSpiderOnlyOrig);
        }
        if (which == flowFilterCaptureSourceScannerOnly && !flowFilterCaptureSourceScannerOnly.isSelected()) {
            flowFilterCaptureSourceScanner.setSelected(flowFilterCaptureSourceScannerOnlyOrig);
        }
        if (which == flowFilterCaptureSourceRepeaterOnly && !flowFilterCaptureSourceRepeaterOnly.isSelected()) {
            flowFilterCaptureSourceRepeater.setSelected(flowFilterCaptureSourceRepeaterOnlyOrig);
        }
        if (which == flowFilterCaptureSourceIntruderOnly && !flowFilterCaptureSourceIntruderOnly.isSelected()) {
            flowFilterCaptureSourceIntruder.setSelected(flowFilterCaptureSourceIntruderOnlyOrig);
        }
        if (which == flowFilterCaptureSourceExtenderOnly && !flowFilterCaptureSourceExtenderOnly.isSelected()) {
            flowFilterCaptureSourceExtender.setSelected(flowFilterCaptureSourceExtenderOnlyOrig);
        }

        if (which.isSelected()) {
            flowFilterCaptureSourceTarget.setEnabled(false);
            flowFilterCaptureSourceTarget.setSelected(which == flowFilterCaptureSourceTargetOnly);
            flowFilterCaptureSourceProxy.setEnabled(false);
            flowFilterCaptureSourceProxy.setSelected(which == flowFilterCaptureSourceProxyOnly);
            flowFilterCaptureSourceSpider.setEnabled(false);
            flowFilterCaptureSourceSpider.setSelected(which == flowFilterCaptureSourceSpiderOnly);
            flowFilterCaptureSourceScanner.setEnabled(false);
            flowFilterCaptureSourceScanner.setSelected(which == flowFilterCaptureSourceScannerOnly);
            flowFilterCaptureSourceRepeater.setEnabled(false);
            flowFilterCaptureSourceRepeater.setSelected(which == flowFilterCaptureSourceRepeaterOnly);
            flowFilterCaptureSourceIntruder.setEnabled(false);
            flowFilterCaptureSourceIntruder.setSelected(which == flowFilterCaptureSourceIntruderOnly);
            flowFilterCaptureSourceExtender.setEnabled(false);
            flowFilterCaptureSourceExtender.setSelected(which == flowFilterCaptureSourceExtenderOnly);
        } else {
            flowFilterCaptureSourceTarget.setSelected(flowFilterCaptureSourceTargetOnlyOrig);
            flowFilterCaptureSourceTarget.setEnabled(true);
            flowFilterCaptureSourceProxy.setSelected(flowFilterCaptureSourceProxyOnlyOrig);
            flowFilterCaptureSourceProxy.setEnabled(true);
            flowFilterCaptureSourceSpider.setSelected(flowFilterCaptureSourceSpiderOnlyOrig);
            flowFilterCaptureSourceSpider.setEnabled(true);
            flowFilterCaptureSourceScanner.setSelected(flowFilterCaptureSourceScannerOnlyOrig);
            flowFilterCaptureSourceScanner.setEnabled(!burpFree);
            flowFilterCaptureSourceRepeater.setSelected(flowFilterCaptureSourceRepeaterOnlyOrig);
            flowFilterCaptureSourceRepeater.setEnabled(true);
            flowFilterCaptureSourceIntruder.setSelected(flowFilterCaptureSourceIntruderOnlyOrig);
            flowFilterCaptureSourceIntruder.setEnabled(true);
            flowFilterCaptureSourceExtender.setSelected(flowFilterCaptureSourceExtenderOnlyOrig);
            flowFilterCaptureSourceExtender.setEnabled(true);
        }

        flowFilterUpdateDescription(true);
    }

    void flowFilterSourceOnly(JCheckBox which) {

        if (which != flowFilterSourceTargetOnly && flowFilterSourceTargetOnly.isSelected()) {
            flowFilterSourceTargetOnly.setSelected(false);
            flowFilterSourceTarget.setSelected(flowFilterSourceTargetOnlyOrig);
        }
        if (which != flowFilterSourceProxyOnly && flowFilterSourceProxyOnly.isSelected()) {
            flowFilterSourceProxyOnly.setSelected(false);
            flowFilterSourceProxy.setSelected(flowFilterSourceProxyOnlyOrig);
        }
        if (which != flowFilterSourceSpiderOnly && flowFilterSourceSpiderOnly.isSelected()) {
            flowFilterSourceSpiderOnly.setSelected(false);
            flowFilterSourceSpider.setSelected(flowFilterSourceSpiderOnlyOrig);
        }
        if (which != flowFilterSourceScannerOnly && flowFilterSourceScannerOnly.isSelected()) {
            flowFilterSourceScannerOnly.setSelected(false);
            flowFilterSourceScanner.setSelected(flowFilterSourceScannerOnlyOrig);
        }
        if (which != flowFilterSourceRepeaterOnly && flowFilterSourceRepeaterOnly.isSelected()) {
            flowFilterSourceRepeaterOnly.setSelected(false);
            flowFilterSourceRepeater.setSelected(flowFilterSourceRepeaterOnlyOrig);
        }
        if (which != flowFilterSourceIntruderOnly && flowFilterSourceIntruderOnly.isSelected()) {
            flowFilterSourceIntruderOnly.setSelected(false);
            flowFilterSourceIntruder.setSelected(flowFilterSourceIntruderOnlyOrig);
        }
        if (which != flowFilterSourceExtenderOnly && flowFilterSourceExtenderOnly.isSelected()) {
            flowFilterSourceExtenderOnly.setSelected(false);
            flowFilterSourceExtender.setSelected(flowFilterSourceExtenderOnlyOrig);
        }

        if (which == flowFilterSourceTargetOnly && !flowFilterSourceTargetOnly.isSelected()) {
            flowFilterSourceTarget.setSelected(flowFilterSourceTargetOnlyOrig);
        }
        if (which == flowFilterSourceProxyOnly && !flowFilterSourceProxyOnly.isSelected()) {
            flowFilterSourceProxy.setSelected(flowFilterSourceProxyOnlyOrig);
        }
        if (which == flowFilterSourceSpiderOnly && !flowFilterSourceSpiderOnly.isSelected()) {
            flowFilterSourceSpider.setSelected(flowFilterSourceSpiderOnlyOrig);
        }
        if (which == flowFilterSourceScannerOnly && !flowFilterSourceScannerOnly.isSelected()) {
            flowFilterSourceScanner.setSelected(flowFilterSourceScannerOnlyOrig);
        }
        if (which == flowFilterSourceRepeaterOnly && !flowFilterSourceRepeaterOnly.isSelected()) {
            flowFilterSourceRepeater.setSelected(flowFilterSourceRepeaterOnlyOrig);
        }
        if (which == flowFilterSourceIntruderOnly && !flowFilterSourceIntruderOnly.isSelected()) {
            flowFilterSourceIntruder.setSelected(flowFilterSourceIntruderOnlyOrig);
        }
        if (which == flowFilterSourceExtenderOnly && !flowFilterSourceExtenderOnly.isSelected()) {
            flowFilterSourceExtender.setSelected(flowFilterSourceExtenderOnlyOrig);
        }

        if (which.isSelected()) {
            flowFilterSourceTarget.setEnabled(false);
            flowFilterSourceTarget.setSelected(which == flowFilterSourceTargetOnly);
            flowFilterSourceProxy.setEnabled(false);
            flowFilterSourceProxy.setSelected(which == flowFilterSourceProxyOnly);
            flowFilterSourceSpider.setEnabled(false);
            flowFilterSourceSpider.setSelected(which == flowFilterSourceSpiderOnly);
            flowFilterSourceScanner.setEnabled(false);
            flowFilterSourceScanner.setSelected(which == flowFilterSourceScannerOnly);
            flowFilterSourceRepeater.setEnabled(false);
            flowFilterSourceRepeater.setSelected(which == flowFilterSourceRepeaterOnly);
            flowFilterSourceIntruder.setEnabled(false);
            flowFilterSourceIntruder.setSelected(which == flowFilterSourceIntruderOnly);
            flowFilterSourceExtender.setEnabled(false);
            flowFilterSourceExtender.setSelected(which == flowFilterSourceExtenderOnly);
        } else {
            flowFilterSourceTarget.setSelected(flowFilterSourceTargetOnlyOrig);
            flowFilterSourceTarget.setEnabled(true);
            flowFilterSourceProxy.setSelected(flowFilterSourceProxyOnlyOrig);
            flowFilterSourceProxy.setEnabled(true);
            flowFilterSourceSpider.setSelected(flowFilterSourceSpiderOnlyOrig);
            flowFilterSourceSpider.setEnabled(true);
            flowFilterSourceScanner.setSelected(flowFilterSourceScannerOnlyOrig);
            flowFilterSourceScanner.setEnabled(!burpFree);
            flowFilterSourceRepeater.setSelected(flowFilterSourceRepeaterOnlyOrig);
            flowFilterSourceRepeater.setEnabled(true);
            flowFilterSourceIntruder.setSelected(flowFilterSourceIntruderOnlyOrig);
            flowFilterSourceIntruder.setEnabled(true);
            flowFilterSourceExtender.setSelected(flowFilterSourceExtenderOnlyOrig);
            flowFilterSourceExtender.setEnabled(true);
        }

        flowFilterUpdate();
    }

    private boolean toolFlagFilterProcessing(int toolFlag) {
        boolean process = true;
        boolean targetOnly = flowFilterSourceTargetOnly.isSelected();
        boolean proxyOnly = flowFilterSourceProxyOnly.isSelected();
        boolean spiderOnly = flowFilterSourceSpiderOnly.isSelected();
        boolean scannerOnly = flowFilterSourceScannerOnly.isSelected();
        boolean repeaterOnly = flowFilterSourceRepeaterOnly.isSelected();
        boolean intruderOnly = flowFilterSourceIntruderOnly.isSelected();
        boolean extenderOnly = flowFilterSourceExtenderOnly.isSelected();
        if (toolFlag == IBurpExtenderCallbacks.TOOL_TARGET) {
            if (proxyOnly || spiderOnly || scannerOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterSourceTarget.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_SPIDER) {
            if (targetOnly || proxyOnly || scannerOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterSourceSpider.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_SCANNER) {
            if (targetOnly || proxyOnly || spiderOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterSourceScanner.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER) {
            if (targetOnly || proxyOnly || spiderOnly || scannerOnly || intruderOnly || extenderOnly || !flowFilterSourceRepeater.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER) {
            if (targetOnly || proxyOnly || spiderOnly || scannerOnly || repeaterOnly || extenderOnly || !flowFilterSourceIntruder.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_EXTENDER) {
            if (targetOnly || proxyOnly || spiderOnly || scannerOnly || repeaterOnly || intruderOnly || !flowFilterSourceExtender.isSelected()) {
                process = false;
            }
        } else if (!flowFilterSourceProxy.isSelected()) { // "proxy" processes
            // all not mentioned
            // above
            process = false;
        }
        return process;
    }

    private boolean toolFlagCaptureProcessing(int toolFlag) {
        boolean process = true;
        boolean targetOnly = flowFilterCaptureSourceTargetOnly.isSelected();
        boolean proxyOnly = flowFilterCaptureSourceProxyOnly.isSelected();
        boolean spiderOnly = flowFilterCaptureSourceSpiderOnly.isSelected();
        boolean scannerOnly = flowFilterCaptureSourceScannerOnly.isSelected();
        boolean repeaterOnly = flowFilterCaptureSourceRepeaterOnly.isSelected();
        boolean intruderOnly = flowFilterCaptureSourceIntruderOnly.isSelected();
        boolean extenderOnly = flowFilterCaptureSourceExtenderOnly.isSelected();
        if (toolFlag == IBurpExtenderCallbacks.TOOL_TARGET) {
            if (proxyOnly || spiderOnly || scannerOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceTarget.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_SPIDER) {
            if (targetOnly || proxyOnly || scannerOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceSpider.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_SCANNER) {
            if (targetOnly || proxyOnly || spiderOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceScanner.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER) {
            if (targetOnly || proxyOnly || spiderOnly || scannerOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceRepeater.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER) {
            if (targetOnly || proxyOnly || spiderOnly || scannerOnly || repeaterOnly || extenderOnly || !flowFilterCaptureSourceIntruder.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_EXTENDER) {
            if (targetOnly || proxyOnly || spiderOnly || scannerOnly || repeaterOnly || intruderOnly || !flowFilterCaptureSourceExtender.isSelected()) {
                process = false;
            }
        } else if (!flowFilterCaptureSourceProxy.isSelected()) { // "proxy" processes
            // all not mentioned
            // above
            process = false;
        }
        return process;
    }

    // flow filter default
    private void flowFilterSetDefaults() {
        flowFilterInscope.setSelected(false);
        flowFilterOutofscope.setSelected(false);
        flowFilterParametrized.setSelected(false);
        flowFilterSearchField.setText("");
        flowFilterSearchCaseSensitive.setSelected(false);
        flowFilterSearchNegative.setSelected(false);
        flowFilterSearchRegex.setSelected(false);
        flowFilterSearchRequest.setSelected(false);
        flowFilterSearchResponse.setSelected(false);
        // flowFilter
        flowFilterSourceTarget.setSelected(true);
        flowFilterSourceTarget.setEnabled(true);
        flowFilterSourceTargetOnly.setSelected(false);
        flowFilterSourceTargetOnlyOrig = true;
        flowFilterSourceProxy.setSelected(true);
        flowFilterSourceProxy.setEnabled(true);
        flowFilterSourceProxyOnly.setSelected(false);
        flowFilterSourceProxyOnlyOrig = true;
        flowFilterSourceSpider.setSelected(true);
        flowFilterSourceSpider.setEnabled(true);
        flowFilterSourceSpiderOnly.setSelected(false);
        flowFilterSourceSpiderOnlyOrig = true;
        flowFilterSourceScanner.setSelected(!burpFree);
        flowFilterSourceScanner.setEnabled(!burpFree);
        flowFilterSourceScannerOnly.setSelected(false);
        flowFilterSourceScannerOnlyOrig = !burpFree;
        flowFilterSourceRepeater.setSelected(true);
        flowFilterSourceRepeater.setEnabled(true);
        flowFilterSourceRepeaterOnly.setSelected(false);
        flowFilterSourceRepeaterOnlyOrig = true;
        flowFilterSourceIntruder.setSelected(true);
        flowFilterSourceIntruder.setEnabled(true);
        flowFilterSourceIntruderOnly.setSelected(false);
        flowFilterSourceIntruderOnlyOrig = true;
        flowFilterSourceExtender.setSelected(true);
        flowFilterSourceExtender.setEnabled(true);
        flowFilterSourceExtenderOnly.setSelected(false);
        flowFilterSourceExtenderOnlyOrig = true;
        // filterFilterCapture
        flowFilterCaptureSourceTarget.setSelected(true);
        flowFilterCaptureSourceTarget.setEnabled(true);
        flowFilterCaptureSourceTargetOnly.setSelected(false);
        flowFilterCaptureSourceTargetOnlyOrig = true;
        flowFilterCaptureSourceProxy.setSelected(true);
        flowFilterCaptureSourceProxy.setEnabled(true);
        flowFilterCaptureSourceProxyOnly.setSelected(false);
        flowFilterCaptureSourceProxyOnlyOrig = true;
        flowFilterCaptureSourceSpider.setSelected(true);
        flowFilterCaptureSourceSpider.setEnabled(true);
        flowFilterCaptureSourceSpiderOnly.setSelected(false);
        flowFilterCaptureSourceSpiderOnlyOrig = true;
        flowFilterCaptureSourceScanner.setSelected(!burpFree);
        flowFilterCaptureSourceScanner.setEnabled(!burpFree);
        flowFilterCaptureSourceScannerOnly.setSelected(false);
        flowFilterCaptureSourceScannerOnlyOrig = !burpFree;
        flowFilterCaptureSourceRepeater.setSelected(true);
        flowFilterCaptureSourceRepeater.setEnabled(true);
        flowFilterCaptureSourceRepeaterOnly.setSelected(false);
        flowFilterCaptureSourceRepeaterOnlyOrig = true;
        flowFilterCaptureSourceIntruder.setSelected(true);
        flowFilterCaptureSourceIntruder.setEnabled(true);
        flowFilterCaptureSourceIntruderOnly.setSelected(false);
        flowFilterCaptureSourceIntruderOnlyOrig = true;
        flowFilterCaptureSourceExtender.setSelected(true);
        flowFilterCaptureSourceExtender.setEnabled(true);
        flowFilterCaptureSourceExtenderOnly.setSelected(false);
        flowFilterCaptureSourceExtenderOnlyOrig = true;
        //
        flowFilterBottom.requestFocus();
        flowFilterUpdate();
    }

    // flow filter description update
    private void flowFilterUpdateDescription(boolean show) {
        StringBuilder filterDescription = new StringBuilder("Filter: ");
        // filter
        boolean filterAll = true;
        if (flowFilterInscope.isSelected()) {
            filterDescription.append("In-scope, ");
            filterAll = false;
        }
        if (flowFilterOutofscope.isSelected()) {
            filterDescription.append("Out-of-scope, ");
            filterAll = false;
        }
        if (flowFilterParametrized.isSelected()) {
            filterDescription.append("Parametrized only, ");
            filterAll = false;
        }
        if (flowFilterSearchField.getText().length() != 0) {
            filterDescription.append("\"").append(flowFilterSearchField.getText()).append("\"");
            StringBuffer searchAttrib = new StringBuffer(" (");
            boolean searchHasAttr = false;
            if (flowFilterSearchCaseSensitive.isSelected()) {
                searchAttrib.append("case sensitive");
                searchHasAttr = true;
            }
            if (flowFilterSearchRegex.isSelected()) {
                if (searchHasAttr) {
                    searchAttrib.append(", ");
                }
                searchAttrib.append("regex");
                searchHasAttr = true;
            }
            if (flowFilterSearchRequest.isSelected() && flowFilterSearchResponse.isSelected()) {
                if (searchHasAttr) {
                    searchAttrib.append(", ");
                }
                searchAttrib.append("in request & response");
                searchHasAttr = true;
            } else {
                if (flowFilterSearchRequest.isSelected()) {
                    if (searchHasAttr) {
                        searchAttrib.append(", ");
                    }
                    searchAttrib.append("in request");
                    searchHasAttr = true;
                }
                if (flowFilterSearchResponse.isSelected()) {
                    if (searchHasAttr) {
                        searchAttrib.append(", ");
                    }
                    searchAttrib.append("in response");
                    searchHasAttr = true;
                }
            }
            if (flowFilterSearchNegative.isSelected()) {
                if (searchHasAttr) {
                    searchAttrib.append(", ");
                }
                searchAttrib.append("negative");
                searchHasAttr = true;
            }
            if (searchHasAttr) {
                filterDescription.append(searchAttrib).append(")");
            }
            filterDescription.append(", ");
            filterAll = false;
        }
        if (filterAll) {
            filterDescription.append("All, ");
        }
        // filter sources
        int filterSources = 0;
        if (flowFilterSourceTargetOnly.isSelected()) {
            filterDescription.append("Target only");
        } else if (flowFilterSourceProxyOnly.isSelected()) {
            filterDescription.append("Proxy only");
        } else if (flowFilterSourceSpiderOnly.isSelected()) {
            filterDescription.append("Spider only");
        } else if (flowFilterSourceScannerOnly.isSelected()) {
            filterDescription.append("Scanner only");
        } else if (flowFilterSourceRepeaterOnly.isSelected()) {
            filterDescription.append("Repeater only");
        } else if (flowFilterSourceIntruderOnly.isSelected()) {
            filterDescription.append("Intruder only");
        } else if (flowFilterSourceExtenderOnly.isSelected()) {
            filterDescription.append("Extender only");
        } else if (flowFilterSourceTarget.isSelected() && flowFilterSourceProxy.isSelected() && flowFilterSourceSpider.isSelected() && (burpFree || flowFilterSourceScanner.isSelected()) && flowFilterSourceRepeater.isSelected() && flowFilterSourceIntruder.isSelected() && flowFilterSourceExtender.isSelected()) {
            filterDescription.append("All sources");
        } else {
            if (flowFilterSourceTarget.isSelected()) {
                filterDescription.append("T");
                filterSources += 1;
            }
            if (flowFilterSourceProxy.isSelected()) {
                filterDescription.append("P");
                filterSources += 1;
            }
            if (flowFilterSourceSpider.isSelected()) {
                filterDescription.append("S");
                filterSources += 1;
            }
            if (flowFilterSourceScanner.isSelected()) {
                filterDescription.append("C");
                filterSources += 1;
            }
            if (flowFilterSourceRepeater.isSelected()) {
                filterDescription.append("R");
                filterSources += 1;
            }
            if (flowFilterSourceIntruder.isSelected()) {
                filterDescription.append("I");
                filterSources += 1;
            }
            if (flowFilterSourceExtender.isSelected()) {
                filterDescription.append("E");
                filterSources += 1;
            }
            if (filterSources == 0) {
                filterDescription.append("None");
            }
        }
        // filter capture
        filterDescription.append(", Capture: ");
        // filter sources
        int FilterCaptureSources = 0;
        if (flowFilterCaptureSourceTargetOnly.isSelected()) {
            filterDescription.append("Target only");
        } else if (flowFilterCaptureSourceProxyOnly.isSelected()) {
            filterDescription.append("Proxy only");
        } else if (flowFilterCaptureSourceSpiderOnly.isSelected()) {
            filterDescription.append("Spider only");
        } else if (flowFilterCaptureSourceScannerOnly.isSelected()) {
            filterDescription.append("Scanner only");
        } else if (flowFilterCaptureSourceRepeaterOnly.isSelected()) {
            filterDescription.append("Repeater only");
        } else if (flowFilterCaptureSourceIntruderOnly.isSelected()) {
            filterDescription.append("Intruder only");
        } else if (flowFilterCaptureSourceExtenderOnly.isSelected()) {
            filterDescription.append("Extender only");
        } else if (flowFilterCaptureSourceTarget.isSelected() && flowFilterCaptureSourceProxy.isSelected() && flowFilterCaptureSourceSpider.isSelected() && (burpFree || flowFilterCaptureSourceScanner.isSelected()) && flowFilterCaptureSourceRepeater.isSelected() && flowFilterCaptureSourceIntruder.isSelected() && flowFilterCaptureSourceExtender.isSelected()) {
            filterDescription.append("All sources");
        } else {
            if (flowFilterCaptureSourceTarget.isSelected()) {
                filterDescription.append("T");
                FilterCaptureSources += 1;
            }
            if (flowFilterCaptureSourceProxy.isSelected()) {
                filterDescription.append("P");
                FilterCaptureSources += 1;
            }
            if (flowFilterCaptureSourceSpider.isSelected()) {
                filterDescription.append("S");
                FilterCaptureSources += 1;
            }
            if (flowFilterCaptureSourceScanner.isSelected()) {
                filterDescription.append("C");
                FilterCaptureSources += 1;
            }
            if (flowFilterCaptureSourceRepeater.isSelected()) {
                filterDescription.append("R");
                FilterCaptureSources += 1;
            }
            if (flowFilterCaptureSourceIntruder.isSelected()) {
                filterDescription.append("I");
                FilterCaptureSources += 1;
            }
            if (flowFilterCaptureSourceExtender.isSelected()) {
                filterDescription.append("E");
                FilterCaptureSources += 1;
            }
            if (FilterCaptureSources == 0) {
                filterDescription.append("None");
            }
        }
        flowFilterText = filterDescription.toString();
        if (show && (flowFilterWorker == null || flowFilterWorker.isDone())) {
            flowFilter.setText(flowFilterText);
        } else {
            flowFilter.setText(flowFilterText + " (Please wait...)");
        }
    }

    // flow filter update
    private void flowFilterUpdate() {
        final ArrayList<RowFilter<FlowTableModel, Number>> mergedFilter = new ArrayList<>();
        // manual filter
        RowFilter<FlowTableModel, Number> manualFilter;
        // filter logic
        final String text = flowFilterSearchField.getText();
        final Pattern pattern;
        if (flowFilterSearchRegex.isSelected()) {
            if (flowFilterSearchCaseSensitive.isSelected()) {
                pattern = Pattern.compile(text);
            } else {
                pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
            }
        } else {
            if (flowFilterSearchCaseSensitive.isSelected()) {
                pattern = Pattern.compile(Pattern.quote(text));
            } else {
                pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE);
            }
        }
        // process
        manualFilter = new RowFilter<FlowTableModel, Number>() {
            @Override
            public boolean include(javax.swing.RowFilter.Entry<? extends FlowTableModel, ? extends Number> entry) {
                FlowEntry flowEntry;
                flowEntry = flow.get(entry.getIdentifier().intValue());
                boolean result = true;
                // mode
                if (result && mode == 0 && flowEntry.isIncomplete()) {
                    result = false;
                }
                // in-scope
                if (result && flowFilterInscope.isSelected() && !callbacks.isInScope(flowEntry.url)) {
                    result = false;
                }
                // out-of-scope
                if (result && flowFilterOutofscope.isSelected() && callbacks.isInScope(flowEntry.url)) {
                    result = false;
                }
                // parametrized
                if (result && flowFilterParametrized.isSelected() && !flowEntry.hasParams) {
                    result = false;
                }
                // search
                if (result && flowFilterSearchField.getText().length() != 0) {
                    boolean found;
                    String req = new String(flowEntry.messageInfoPersisted.getRequest());
                    byte[] response = flowEntry.messageInfoPersisted.getResponse();
                    String resp;
                    if (response != null) {
                        resp = new String(response);
                    } else {
                        resp = "";
                    }
                    boolean foundInReq = pattern.matcher(req).find();
                    boolean foundInResp = pattern.matcher(resp).find();
                    if (flowFilterSearchRequest.isSelected() && flowFilterSearchResponse.isSelected()) {
                        found = foundInReq && foundInResp;
                    } else if (flowFilterSearchRequest.isSelected()) {
                        found = foundInReq;
                    } else if (flowFilterSearchResponse.isSelected()) {
                        found = foundInResp;
                    } else {
                        found = foundInReq || foundInResp;
                    }
                    if (flowFilterSearchNegative.isSelected()) {
                        if (found) {
                            result = false;
                        }
                    } else if (!found) {
                        result = false;
                    }
                }
                // capture
                if (result && !toolFlagFilterProcessing(flowEntry.toolFlag)) {
                    result = false;
                }
                return result;
            }
        };
        mergedFilter.add(manualFilter);
        // regex filter
        //if (flowFilterSearchField.getText().length() != 0) {
        //    RowFilter<FlowTableModel, Number> regexFilter;
        //    try {
        //        if (flowFilterSearchCaseSensitive.isSelected()) {
        //            regexFilter = RowFilter.regexFilter(flowFilterSearchField.getText());
        //        } else {
        //            regexFilter = RowFilter.regexFilter("(?i)" + flowFilterSearchField.getText());
        //        }
        //    } catch (java.util.regex.PatternSyntaxException e) {
        //        return;
        //    }
        //
        //    if (flowFilterSearchNegative.isSelected()) {
        //        mergedFilter.add(RowFilter.notFilter(regexFilter));
        //    } else {
        //        mergedFilter.add(regexFilter);
        //    }
        //}
        // flowTableSorter.setRowFilter(RowFilter.andFilter(mergedFilter));
        //SwingUtilities.invokeLater(new Runnable() {
        //
        //    @Override
        //    public void run() {
        //        flowTableSorter.setRowFilter(RowFilter.andFilter(mergedFilter));
        //    }
        //});
        flowFilterUpdateDescription(false);
        if (flowFilterWorker != null) {
            try {
                flowFilterWorker.cancel(true);
            } catch (Exception ex) {
                // do nothing
            }
        }
        (flowFilterWorker = new FilterWorker(RowFilter.andFilter(mergedFilter))).execute();
    }

    class FilterWorker extends SwingWorker<Object, Object> {

        private final RowFilter<FlowTableModel, Number> filter;

        public FilterWorker(RowFilter<FlowTableModel, Number> filter) {
            this.filter = filter;
        }

        @Override
        protected Object doInBackground() throws Exception {
            flowTableSorter.setRowFilter(filter);
            return null;
        }

        @Override
        protected void done() {
            flowFilter.setText(flowFilterText);
            flowTable.repaint();
        }
    }

    private static String abbreviateMiddle(String str, String middle, int length) {
        if (str.length() == 0 || middle.length() == 0) {
            return str;
        }

        if (length >= str.length() || length < middle.length() + 2) {
            return str;
        }

        int targetSting = length - middle.length();
        int startOffset = targetSting / 2 + targetSting % 2;
        int endOffset = str.length() - targetSting / 2;

        StringBuilder builder = new StringBuilder(length);
        builder.append(str.substring(0, startOffset));
        builder.append(middle);
        builder.append(str.substring(endOffset));

        return builder.toString();

    }

    //
    // flowTableModel (AbstractTableModel) class
    //
    // class FlowTableModel extends DefaultTableModel {
    class FlowTableModel extends AbstractTableModel {

        // private static final Object[][] flowTableSpecs = {{"Index","#",40,Integer}, ...
        @Override
        public int getRowCount() {
            return flow.size();
        }

        @Override
        public int getColumnCount() {
            return 13;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 12:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Integer.class; // "#"
                //case 1:
                //        return "Tool";
                //case 2:
                //        return "Host";
                //case 3:
                //        return "Method";
                //case 4:
                //        return "URL";
                //case 5:
                //        return "Reflect";
                case 6:
                    return Boolean.class; // "Params"
                case 7:
                    return Integer.class; // "Count"
                case 8:
                    return Integer.class; // "Status"
                case 9:
                    return Integer.class; // "Length"
                //case 10:
                //        return "MIME";
                case 11:
                    return Date.class; // "Date"
                //case 12:
                //        return "Comment";
                default:
                    return String.class;
            }
        }

        public int getPreferredWidth(int column) {
            switch (column) {
                case 0:
                    return 40;
                case 1:
                    return 60;
                case 2:
                    return 150;
                case 3:
                    return 55;
                case 4:
                    return 430;
                case 5:
                    return 55;
                case 6:
                    return 55;
                case 7:
                    return 55;
                case 8:
                    return 55;
                case 9:
                    return 55;
                case 10:
                    return 55;
                case 11:
                    return 130;
                case 12:
                    return 160;
                default:
                    return 20;
            }
        }

        @Override
        public String getColumnName(int column) {
            // return (String) flowTableCols[column][0];
            switch (column) {
                case 0:
                    return "#";
                case 1:
                    return "Tool";
                case 2:
                    return "Host";
                case 3:
                    return "Method";
                case 4:
                    return "URL";
                case 5:
                    return "Reflect";
                case 6:
                    return "Params";
                case 7:
                    return "Count";
                case 8:
                    return "Status";
                case 9:
                    return "Length";
                case 10:
                    return "MIME";
                case 11:
                    return "Time";
                case 12:
                    return "Comment";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            try {
                FlowEntry flowEntry = flow.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return flowEntry.id; // rowIndex + 1;
                    case 1:
                        return callbacks.getToolName(flowEntry.toolFlag);
                    case 2:
                        return new StringBuilder(flowEntry.url.getProtocol()).append("://").append(flowEntry.url.getHost()).toString();
                    case 3:
                        return flowEntry.method;
                    case 4:
                        return flowEntry.getPath(); //flowEntry.getQueryPath();
                    case 5:
                        return new String(new char[flowEntry.getReflections().size()]).replace("\0", "|");
                    case 6:
                        return flowEntry.hasParams;
                    case 7:
                        return flowEntry.paramCount;
                    case 8:
                        return flowEntry.status;
                    case 9:
                        return flowEntry.length;
                    case 10:
                        return flowEntry.mime;
                    case 11:
                        return flowEntry.date; //new SimpleDateFormat("HH:mm:ss d MMM yyyy").format(flowEntry.date);
                    case 12:
                        return flowEntry.comment;
                    default:
                        return "";
                }
            } catch (Exception ex) {
                ex.printStackTrace(stderr);
                return null;
            }
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int colIndex) {
            FlowEntry flowEntry = flow.get(rowIndex);
            flowEntry.comment = (String) value;
            fireTableCellUpdated(rowIndex, colIndex);
        }

        public void removeRowAt(int rowIndex) {
            flow.remove(rowIndex);
            fireTableDataChanged();
        }

        public void remove(FlowEntry entry) {
            flow.remove(entry);
            fireTableDataChanged();
        }

        public void removeAll() {
            flow.clear();
            fireTableDataChanged();
        }

        public void removeAll(int[] rows) {
            for (int i = 0; i < rows.length; i++) {
                flow.remove(rows[i]);
            }
            fireTableDataChanged();
        }
    }

    //
    // FlowEntry class
    //
    public static final class FlowEntry {

        private static int newID = 1;
        private final int id;
        private final int toolFlag;
        private IHttpRequestResponsePersisted messageInfoPersisted;
        //private IHttpRequestResponse messageInfo;
        private String incomplete;
        private final String method;
        private final URL url;
        private final boolean hasParams;
        private int paramCount;
        private int status;
        private int length;
        private String mime;
        private final Date date;
        private String comment;
        private final String path;
        private final String queryPath;
        private final ArrayList<IParameter> reflections;
        private final IHttpService service;

        //public String serialize() {
        //    return "";
        //}
        //public static FlowEntry deserialize() {
        //    return new FlowEntry();
        //}
        void update(IHttpRequestResponse messageInfo) {
            // status = 999;
            // stdout.println("    Update");
            byte[] response = messageInfo.getResponse();
            if (response != null) {
                incomplete = null;
                messageInfoPersisted = callbacks.saveBuffersToTempFiles(messageInfo);
                onResponse(response);
            }
        }

        void onResponse(byte[] response) {
            IResponseInfo responseInfo = helpers.analyzeResponse(response);
            status = responseInfo.getStatusCode();
            length = messageInfoPersisted.getResponse().length - responseInfo.getBodyOffset();
            mime = responseInfo.getStatedMimeType();

            IRequestInfo requestInfo = helpers.analyzeRequest(messageInfoPersisted);
            String responseBody = helpers.bytesToString(response).substring(responseInfo.getBodyOffset());
            for (IParameter requestParam : requestInfo.getParameters()) {
                // exclude cookies
                if (requestParam.getType() != IParameter.PARAM_COOKIE) {
                    // parameter value has at least 3 chars
                    String value = requestParam.getValue();
                    // reflected in response?
                    if (value.length() > 3 && responseBody.contains(value)) {
                        reflections.add(requestParam);
                    }
                }
            }
        }

        FlowEntry(int toolFlag, IHttpRequestResponse messageInfo) {
            this.id = FlowEntry.newID;
            FlowEntry.newID += 1;
            this.toolFlag = toolFlag;
            //this.messageInfo = messageInfo;
            messageInfoPersisted = callbacks.saveBuffersToTempFiles(messageInfo);
            IRequestInfo requestInfo;
            requestInfo = helpers.analyzeRequest(messageInfo);
            method = requestInfo.getMethod();
            url = requestInfo.getUrl();
            service = messageInfo.getHttpService();
            reflections = new ArrayList<>();
            path = url.getPath();
            {
                StringBuilder pathBuilder = new StringBuilder(url.getPath());
                String query = url.getQuery();
                if (query != null) {
                    pathBuilder.append("?").append(query);
                }
                queryPath = pathBuilder.toString();
            }
            byte[] response = messageInfo.getResponse();
            if (response == null) {
                incomplete = helpers.bytesToString(messageInfo.getRequest());
                FLOW_INCOMPLETE.add(FlowEntry.this);
            }
            boolean hasGetParams = url.getQuery() != null;
            boolean hasPostParams = messageInfoPersisted.getRequest().length - requestInfo.getBodyOffset() > 0;
            hasParams = hasGetParams || hasPostParams;
            if (hasParams) {
                for (IParameter param : requestInfo.getParameters()) {
                    byte type = param.getType();
                    if (type == IParameter.PARAM_BODY || type == IParameter.PARAM_URL) {
                        paramCount += 1;
                    }
                }
            }
            if (response != null) {
                onResponse(response);
            } else {
                status = -1;
                length = -1;
                mime = "";
            }
            date = new Date();
            comment = "";
        }

        public boolean isIncomplete() {
            return incomplete != null;
        }

        /**
         * Returns URL's path along with query string (if present)
         *
         * @return
         */
        public String getQueryPath() {
            return queryPath;
        }

        public String getPath() {
            return path;
        }

        public ArrayList<IParameter> getReflections() {
            return reflections;
        }

        public URL getUrl() {
            return url;
        }

        public IHttpRequestResponse getMessageInfo() {
            return (IHttpRequestResponse) messageInfoPersisted;
        }

        public IHttpService getService() {
            return service;
        }
    }

    //
    // FlowEntryEditor (IMessageEditorController) class
    //
    class FlowEntryEditor implements IMessageEditorController {

        @Override
        public IHttpService getHttpService() {
            return flowCurrentEntry.messageInfoPersisted.getHttpService();
        }

        @Override
        public byte[] getRequest() {
            return flowCurrentEntry.messageInfoPersisted.getRequest();
        }

        @Override
        public byte[] getResponse() {
            return flowCurrentEntry.messageInfoPersisted.getResponse();
        }

    }

    //
    // Table (JTable) class to handle cell selection
    //
    private class FlowTable extends JTable {

        public FlowTable(TableModel tableModel) {
            super(tableModel);
        }

        public void updateResponse() {
            byte[] response = flowCurrentEntry.messageInfoPersisted.getResponse();
            if (response != null) {
                flowRespView.setMessage(flowCurrentEntry.messageInfoPersisted.getResponse(), false);
            } else {
                flowRespView.setMessage(new byte[0], false);
            }
        }

        @Override
        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
            FlowEntry flowEntry = flow.get(flowTable.convertRowIndexToModel(row));
            flowReqView.setMessage(flowEntry.messageInfoPersisted.getRequest(), true);
            flowCurrentEntry = flowEntry;
            updateResponse();
            super.changeSelection(row, col, toggle, extend);
        }

        @Override
        public JPopupMenu getComponentPopupMenu() {
            Point pt = getMousePosition();
            if (pt != null) {
                popupPointedFlowEntry = flow.get(flowTable.convertRowIndexToModel(rowAtPoint(pt)));
                flowTablePopup.getHeader().setText(abbreviateMiddle(popupPointedFlowEntry.url.toString(), "...", 70));
            } else {
                popupPointedFlowEntry = null;
            }
            // scope add/remove options visibility
            int selectedCount = flowTable.getSelectedRows().length;
            flowTablePopup.scopeSeparator.setVisible(selectedCount > 0);
            if (selectedCount > 0) {
                if (selectedCount > 1) {
                    flowTablePopup.scopeAdd.setVisible(true);
                    flowTablePopup.scopeExclude.setVisible(true);
                } else {
                    boolean inScope = callbacks.isInScope(flow.get(flowTable.convertRowIndexToModel(flowTable.getSelectedRows()[0])).url);
                    flowTablePopup.scopeAdd.setVisible(!inScope);
                    flowTablePopup.scopeExclude.setVisible(inScope);
                }
            } else {
                flowTablePopup.scopeAdd.setVisible(false);
                flowTablePopup.scopeExclude.setVisible(false);
            }
            //
            return super.getComponentPopupMenu();
        }
    }

    class FlowTablePopup extends JPopupMenu {

        private final JMenuItem header;
        final Separator scopeSeparator;
        final JMenuItem scopeAdd;
        final JMenuItem scopeExclude;

        public JMenuItem getHeader() {
            return header;
        }

        public FlowTablePopup() {
            header = new JMenuItem();
            scopeSeparator = new Separator();
            scopeAdd = new JMenuItem("Add to scope");
            scopeAdd.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i : flowTable.getSelectedRows()) {
                        callbacks.includeInScope(flow.get(flowTable.convertRowIndexToModel(i)).url);
                    }
                }
            });
            scopeExclude = new JMenuItem("Remove from scope");
            scopeExclude.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i : flowTable.getSelectedRows()) {
                        callbacks.excludeFromScope(flow.get(flowTable.convertRowIndexToModel(i)).url);
                    }
                }
            });
            initialize();
        }

        private void initialize() {
            add(header);
            add(scopeSeparator);
            add(scopeAdd);
            add(scopeExclude);
            add(new Separator());

            JMenuItem doAnActiveScan = new JMenuItem("Do an active scan");
            doAnActiveScan.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    IHttpService service = popupPointedFlowEntry.messageInfoPersisted.getHttpService();
                    callbacks.doActiveScan(service.getHost(), service.getPort(), "https".equals(service.getProtocol()), popupPointedFlowEntry.messageInfoPersisted.getRequest());
                }
            });
            add(doAnActiveScan);
            JMenuItem doAPassiveScan = new JMenuItem("Do a passive scan");
            doAPassiveScan.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    IHttpService service = popupPointedFlowEntry.messageInfoPersisted.getHttpService();
                    callbacks.doPassiveScan(service.getHost(), service.getPort(), "https".equals(service.getProtocol()), popupPointedFlowEntry.messageInfoPersisted.getRequest(), popupPointedFlowEntry.messageInfoPersisted.getResponse());
                }
            });
            add(doAPassiveScan);
            JMenuItem sendToIntruder = new JMenuItem("Send to Intruder");
            sendToIntruder.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    IHttpService service = popupPointedFlowEntry.messageInfoPersisted.getHttpService();
                    callbacks.sendToIntruder(service.getHost(), service.getPort(), "https".equals(service.getProtocol()), popupPointedFlowEntry.messageInfoPersisted.getRequest());
                }
            });
            add(sendToIntruder);
            JMenuItem sendToRepeater = new JMenuItem("Send to Repeater");
            sendToRepeater.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    IHttpService service = popupPointedFlowEntry.messageInfoPersisted.getHttpService();
                    callbacks.sendToRepeater(service.getHost(), service.getPort(), "https".equals(service.getProtocol()), popupPointedFlowEntry.messageInfoPersisted.getRequest(), null);
                }
            });
            add(sendToRepeater);
            JMenu history = new JMenu("History");
            add(history);

            //JMenuItem saveHistory = new JMenuItem("Save history");
            //saveHistory.addActionListener(new ActionListener() {
            //    @Override
            //    public void actionPerformed(ActionEvent e) {
            //        // TODO
            //    }
            //});
            //history.add(saveHistory);
            //JMenuItem loadHistory = new JMenuItem("Load history");
            //loadHistory.addActionListener(new ActionListener() {
            //    @Override
            //    public void actionPerformed(ActionEvent e) {
            //        // TODO
            //    }
            //});
            //history.add(loadHistory);
            JMenuItem clearAll = new JMenuItem("Clear history");
            clearAll.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(burpFrame, "Are you sure you want to clear the history?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                        flowTableModel.removeAll();
                    }
                }
            });
            history.add(clearAll);

            add(new Separator());
            JMenuItem addNewIssue = new JMenuItem("Add new sitemap issue");
            addNewIssue.setEnabled(burpFree == false);
            addNewIssue.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //int[] rows = flowTable.getSelectedRows();
                    //for (int i = 0; i < rows.length; i++) {
                    //    rows[i] = flowTable.convertRowIndexToModel(rows[i]);
                    //}
                    showAddNewIssueDialog();
                }

            });
            add(addNewIssue);
            add(new Separator());

            //JMenuItem delete = new JMenuItem("Delete");
            //delete.addActionListener(new ActionListener() {
            //
            //    @Override
            //    public void actionPerformed(ActionEvent e) {
            //        if (popupPointedFlowEntry != null) {
            //            flowTableModel.remove(popupPointedFlowEntry);
            //            popupPointedFlowEntry = null;
            //        }
            //    }
            //});
            //add(delete);
            //
            //JMenuItem temp = new JMenuItem("temp");
            //temp.addActionListener(new ActionListener() {
            //
            //    @Override
            //    public void actionPerformed(ActionEvent e) {
            //        tempy = popupPointedFlowEntry;
            //        flowTable.repaint();
            //    }
            //});
            //add(temp);
            JMenuItem deleteSelected = new JMenuItem("Delete selected");
            deleteSelected.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] rows = flowTable.getSelectedRows();
                    for (int i = 0; i < rows.length; i++) {
                        rows[i] = flowTable.convertRowIndexToModel(rows[i]);
                    }
                    flowTableModel.removeAll(rows);
                }
            });
            add(deleteSelected);
            JMenuItem copyURL = new JMenuItem("Copy URL");
            copyURL.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(popupPointedFlowEntry.url.toString()), null);
                }
            });
            add(copyURL);
        }
    }

    private String paramType(byte type) {
        String result;
        switch (type) {
            case IParameter.PARAM_BODY:
                result = "BODY";
                break;
            case IParameter.PARAM_COOKIE:
                result = "COOKIE";
                break;
            case IParameter.PARAM_JSON:
                result = "JSON";
                break;
            case IParameter.PARAM_MULTIPART_ATTR:
                result = "MULTIPART_ATTR";
                break;
            case IParameter.PARAM_URL:
                result = "URL";
                break;
            case IParameter.PARAM_XML:
                result = "XML";
                break;
            case IParameter.PARAM_XML_ATTR:
                result = "XML_ATTR";
                break;
            default:
                result = Byte.toString(type);
        }
        return result;
    }

    class FlowTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        protected void setValue(Object value) {
            if (value instanceof Date) {
                value = new SimpleDateFormat("HH:mm:ss d MMM yyyy").format(value);
            }
            if ((value instanceof Integer) && ((Integer) value == -1)) {
                value = "";
            }
            super.setValue(value);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int modelRow = table.convertRowIndexToModel(row);
            FlowEntry entry = flow.get(modelRow);
            if (ThemeHelper.isDarkTheme() == false) {
                // c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                //if (!isSelected && tempy != null && flowTable.convertRowIndexToModel(row) == flow.indexOf(tempy)) {
                //    c.setForeground(Color.blue);
                //} else {
                //    c.setForeground(Color.black);
                //}
                int r = 0, g = 0, b = 0;
                if (entry.status == 404 || entry.status == 403) {
                    r += 196;
                    g += 128;
                    b += 128;
                }
                if (entry.status == 500) {
                    g += 196;
                }
                if (entry.toolFlag != IBurpExtenderCallbacks.TOOL_PROXY) {
                    b += 196;
                }
                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }
                c.setForeground(new Color(r, g, b));

                c.setBackground(ThemeHelper.cellBackground(table.getRowCount(), row, isSelected));
            }

            final ArrayList<String> reflections = new ArrayList<>();
            final ArrayList<IParameter> allReflections = entry.getReflections();
            int reflectionsAmount = allReflections.size();
            if (reflectionsAmount > showReflectionsCount) {
                reflectionsAmount = showReflectionsCount;
            }
            if (allReflections.size() > 0) {
                for (IParameter reflection : allReflections.subList(0, reflectionsAmount)) {
                    String param = reflection.getName();
                    if (param.length() > 50) {
                        param = param.substring(0, 50 - 3) + "...";
                    }
                    String val = reflection.getValue();
                    if (val.length() > 50) {
                        val = val.substring(0, 50 - 3) + "...";
                    }
                    reflections.add(new StringBuilder(" &nbsp; &nbsp; (").append(paramType(reflection.getType())).append(") ").append(param).append("=").append(val).toString());
                }
                if (allReflections.size() > showReflectionsCount) {
                    reflections.add("...");
                }
                ((JLabel) c).setToolTipText(new StringBuilder("<html>Reflections:<br/>").append(String.join("<br/>", reflections)).append("</html>").toString());
            } else {
                ((JLabel) c).setToolTipText(null);
            }

            return c;
        }

    }

    public class SeparateViewDialog extends JDialog {

        private final Component parent;
        private final String title;

        @Override
        public void dispose() {
            if (dialogs.contains(this)) {
                dialogs.remove(this);
            }
            separateView = null;
            try {
                flowFilterHelpExt.setEnabled(true);
                flowComponent.add(flowTab);
                flowComponent.revalidate();
                flowComponent.repaint();
            } catch (Exception ex) {
                // do nothing
            }
            super.dispose();
        }

        public SeparateViewDialog(final Component parent, String title) {
            flowFilterHelpExt.setEnabled(false);
            flowComponent.remove(flowTab);
            flowComponent.revalidate();
            flowComponent.repaint();
            this.parent = parent;
            this.title = title;
            initialize();
        }

        private void initialize() {
            setTitle(title);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setBounds(parent.getBounds());
            setContentPane(flowTab);
            dialogs.add(this);
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    public static int getSortOrder() {
        return sortOrder;
    }

    public static FlowExtension getInstance() {
        return instance;
    }

    public static PrintWriter getStderr() {
        return stderr;
    }

    private int parseReflectionsCount(String text) {
        int result = showReflectionsCountDefault;
        if (text != null) {
            try {
                showReflectionsCount = Integer.parseInt(text);
                if (showReflectionsCount > showReflectionsCountMax) {
                    showReflectionsCount = showReflectionsCountMax;
                }
                if (showReflectionsCount < 0) {
                    showReflectionsCount = 0;
                }
            } catch (NumberFormatException ex) {
                // do nothing
            }
        }
        return result;
    }
}
