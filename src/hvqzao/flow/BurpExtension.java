// Flow Burp Extension, (c) 2015-2016 Marcin Woloszyn (@hvqzao), Released under MIT license
package hvqzao.flow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.table.TableCellRenderer;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BurpExtension implements IBurpExtender, ITab, IHttpListener, IScopeChangeListener, IExtensionStateListener {

    private final String version = "Flow v1.06 (2016-10-13)";
    //private final String versionFull = "<html>" + version + ", <a href=\"https://github.com/hvqzao/burp-flow\">https://github.com/hvqzao/burp-flow</a>, MIT license</html>";
    private static IBurpExtenderCallbacks callbacks;
    private static IExtensionHelpers helpers;
    private JPanel flowComponent;
    private JSplitPane flowTab;
    private FlowTableModel flowTableModel;
    private TableRowSorter<FlowTableModel> flowTableSorter;
    private final ArrayList<FlowEntry> flow = new ArrayList<>();
    private static final ArrayList<FlowEntry> flowIncomplete = new ArrayList<>();
    private FlowTable flowTable;
    private FlowEntryEditor flowEntryEditor;
    private IMessageEditor flowReqView;
    private IMessageEditor flowRespView;
    private FlowEntry flowCurrentEntry;
    private boolean flowFilterPopupReady;
    private JLabel flowFilter;
    private JCheckBox flowFilterInscope;
    private JCheckBox flowFilterParametrized;
    private JTextField flowFilterSearchField;
    private JCheckBox flowFilterSearchCaseSensitive;
    private JCheckBox flowFilterSearchNegative;
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
    private boolean flowFilterSourceProxyOnlyOrig;
    private boolean flowFilterSourceSpiderOnlyOrig;
    private boolean flowFilterSourceScannerOnlyOrig;
    private boolean flowFilterSourceRepeaterOnlyOrig;
    private boolean flowFilterSourceIntruderOnlyOrig;
    private boolean flowFilterSourceExtenderOnlyOrig;
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
    private SeparateView separateView;
    private JButton flowFilterHelpExt;
    private static final Color COLOR_HIGHLIGHT = new Color(255, 206, 130);
    private static final Color COLOR_DARKGRAY = new Color(240, 240, 240);
    private static final Color COLOR_LIGHTGRAY = new Color(250, 250, 250);

    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {

        // keep a reference to our callbacks object
        BurpExtension.callbacks = callbacks;
        // obtain an extension helpers object
        helpers = callbacks.getHelpers();
        // set extension name
        callbacks.setExtensionName("Flow");
        // detect burp
        burpFree = String.valueOf(callbacks.getBurpVersion()[0]).equals("Burp Suite Free Edition");
        // draw UI
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // images
                ImageIcon iconHelp = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/panel_help.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
                ImageIcon iconDefaults = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/panel_defaults.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
                ImageIcon iconNewWindow = new ImageIcon(new ImageIcon(getClass().getResource("/hvqzao/flow/resources/newwindow.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
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

                        if (!flowFilterSourceProxyOnly.isSelected() && !flowFilterSourceSpiderOnly.isSelected() && !flowFilterSourceScannerOnly.isSelected() && !flowFilterSourceRepeaterOnly.isSelected() && !flowFilterSourceIntruderOnly.isSelected() && !flowFilterSourceExtenderOnly.isSelected()) {
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

                        if (!flowFilterCaptureSourceProxyOnly.isSelected() && !flowFilterCaptureSourceSpiderOnly.isSelected() && !flowFilterCaptureSourceScannerOnly.isSelected() && !flowFilterCaptureSourceRepeaterOnly.isSelected() && !flowFilterCaptureSourceIntruderOnly.isSelected()
                                && !flowFilterCaptureSourceExtenderOnly.isSelected()) {
                            flowFilterCaptureSourceProxyOnlyOrig = flowFilterCaptureSourceProxy.isSelected();
                            flowFilterCaptureSourceSpiderOnlyOrig = flowFilterCaptureSourceSpider.isSelected();
                            flowFilterCaptureSourceScannerOnlyOrig = flowFilterCaptureSourceScanner.isSelected();
                            flowFilterCaptureSourceRepeaterOnlyOrig = flowFilterCaptureSourceRepeater.isSelected();
                            flowFilterCaptureSourceIntruderOnlyOrig = flowFilterCaptureSourceIntruder.isSelected();
                            flowFilterCaptureSourceExtenderOnlyOrig = flowFilterCaptureSourceExtender.isSelected();
                        }
                        flowFilterUpdateDescription();
                    }
                };
                // layout
                final JDialog flowFilterPopupWindow = new JDialog();
                FlowFilterPopup flowFilterPopup = new FlowFilterPopup();
                flowFilterCaptureSourceExtender = flowFilterPopup.getFlowFilterCaptureSourceExtender();
                callbacks.customizeUiComponent(flowFilterCaptureSourceExtender);
                flowFilterCaptureSourceExtenderOnly = flowFilterPopup.getFlowFilterCaptureSourceExtenderOnly();
                callbacks.customizeUiComponent(flowFilterCaptureSourceExtenderOnly);
                flowFilterCaptureSourceIntruder = flowFilterPopup.getFlowFilterCaptureSourceIntruder();
                callbacks.customizeUiComponent(flowFilterCaptureSourceIntruder);
                flowFilterCaptureSourceIntruderOnly = flowFilterPopup.getFlowFilterCaptureSourceIntruderOnly();
                callbacks.customizeUiComponent(flowFilterCaptureSourceIntruderOnly);
                flowFilterCaptureSourceProxy = flowFilterPopup.getFlowFilterCaptureSourceProxy();
                callbacks.customizeUiComponent(flowFilterCaptureSourceProxy);
                flowFilterCaptureSourceProxyOnly = flowFilterPopup.getFlowFilterCaptureSourceProxyOnly();
                callbacks.customizeUiComponent(flowFilterCaptureSourceProxyOnly);
                flowFilterCaptureSourceRepeater = flowFilterPopup.getFlowFilterCaptureSourceRepeater();
                callbacks.customizeUiComponent(flowFilterCaptureSourceRepeater);
                flowFilterCaptureSourceRepeaterOnly = flowFilterPopup.getFlowFilterCaptureSourceRepeaterOnly();
                callbacks.customizeUiComponent(flowFilterCaptureSourceRepeaterOnly);
                flowFilterCaptureSourceScanner = flowFilterPopup.getFlowFilterCaptureSourceScanner();
                callbacks.customizeUiComponent(flowFilterCaptureSourceScanner);
                flowFilterCaptureSourceScannerOnly = flowFilterPopup.getFlowFilterCaptureSourceScannerOnly();
                callbacks.customizeUiComponent(flowFilterCaptureSourceScannerOnly);
                flowFilterCaptureSourceSpider = flowFilterPopup.getFlowFilterCaptureSourceSpider();
                callbacks.customizeUiComponent(flowFilterCaptureSourceSpider);
                flowFilterCaptureSourceSpiderOnly = flowFilterPopup.getFlowFilterCaptureSourceSpiderOnly();
                callbacks.customizeUiComponent(flowFilterCaptureSourceSpiderOnly);
                flowFilterInscope = flowFilterPopup.getFlowFilterInscope();
                callbacks.customizeUiComponent(flowFilterInscope);
                flowFilterParametrized = flowFilterPopup.getFlowFilterParametrized();
                callbacks.customizeUiComponent(flowFilterParametrized);
                flowFilterSearchCaseSensitive = flowFilterPopup.getFlowFilterSearchCaseSensitive();
                callbacks.customizeUiComponent(flowFilterSearchCaseSensitive);
                flowFilterSearchField = flowFilterPopup.getFlowFilterSearchField();
                callbacks.customizeUiComponent(flowFilterSearchField);
                flowFilterSearchNegative = flowFilterPopup.getFlowFilterSearchNegative();
                callbacks.customizeUiComponent(flowFilterSearchNegative);
                flowFilterSourceExtender = flowFilterPopup.getFlowFilterSourceExtender();
                callbacks.customizeUiComponent(flowFilterSourceExtender);
                flowFilterSourceExtenderOnly = flowFilterPopup.getFlowFilterSourceExtenderOnly();
                callbacks.customizeUiComponent(flowFilterSourceExtenderOnly);
                flowFilterSourceIntruder = flowFilterPopup.getFlowFilterSourceIntruder();
                callbacks.customizeUiComponent(flowFilterSourceIntruder);
                flowFilterSourceIntruderOnly = flowFilterPopup.getFlowFilterSourceIntruderOnly();
                callbacks.customizeUiComponent(flowFilterSourceIntruderOnly);
                flowFilterSourceProxy = flowFilterPopup.getFlowFilterSourceProxy();
                callbacks.customizeUiComponent(flowFilterSourceProxy);
                flowFilterSourceProxyOnly = flowFilterPopup.getFlowFilterSourceProxyOnly();
                callbacks.customizeUiComponent(flowFilterSourceProxyOnly);
                flowFilterSourceRepeater = flowFilterPopup.getFlowFilterSourceRepeater();
                callbacks.customizeUiComponent(flowFilterSourceRepeater);
                flowFilterSourceRepeaterOnly = flowFilterPopup.getFlowFilterSourceRepeaterOnly();
                callbacks.customizeUiComponent(flowFilterSourceRepeaterOnly);
                flowFilterSourceScanner = flowFilterPopup.getFlowFilterSourceScanner();
                callbacks.customizeUiComponent(flowFilterSourceScanner);
                flowFilterSourceScannerOnly = flowFilterPopup.getFlowFilterSourceScannerOnly();
                callbacks.customizeUiComponent(flowFilterSourceScannerOnly);
                flowFilterSourceSpider = flowFilterPopup.getFlowFilterSourceSpider();
                callbacks.customizeUiComponent(flowFilterSourceSpider);
                flowFilterSourceSpiderOnly = flowFilterPopup.getFlowFilterSourceSpiderOnly();
                callbacks.customizeUiComponent(flowFilterSourceSpiderOnly);
                flowFilterBottom = flowFilterPopup.getFlowFilterBottom();

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
                flowFilterInscope.addActionListener(flowFilterScopeUpdateAction);
                flowFilterParametrized.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchCaseSensitive.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSearchNegative.addActionListener(flowFilterScopeUpdateAction);

                flowFilterSourceProxy.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceSpider.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceScanner.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceRepeater.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceIntruder.addActionListener(flowFilterScopeUpdateAction);
                flowFilterSourceExtender.addActionListener(flowFilterScopeUpdateAction);
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

                flowFilterCaptureSourceProxy.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceSpider.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceScanner.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceRepeater.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceIntruder.addActionListener(flowFilterCaptureScopeUpdateAction);
                flowFilterCaptureSourceExtender.addActionListener(flowFilterCaptureScopeUpdateAction);
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
                flowFilterHelpPane.setPreferredSize(new Dimension(26, 24));
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
                // flow filterHelpExt popup
                /*
                final JDialog flowFilterHelpExtPopupWindow = new JDialog();
                final JLabel flowFilterHelpExtPopup = new JLabel(version);
                // flowFilterHelpExtPopup.setBorder(BorderFactory.createLineBorder(Color.darkGray));
                // flowFilterHelpExtPopup.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
                flowFilterHelpExtPopup.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray), BorderFactory.createEmptyBorder(2, 20, 2, 20)));
                flowFilterHelpExtPopup.setBackground(Color.lightGray);
                //
                flowFilterHelpExtPopupReady = true;
                flowFilterHelpExt.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (flowFilterHelpExtPopupReady) {
                            flowFilterHelpExtPopupReady = false;
                            flowFilterHelpExtPopupWindow.addWindowFocusListener(new WindowFocusListener() {

                                @Override
                                public void windowLostFocus(WindowEvent e) {
                                    flowFilterHelpExtPopupWindow.dispose();
                                    SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            flowFilterHelpExtPopupReady = true;
                                        }
                                    });
                                }

                                @Override
                                public void windowGainedFocus(WindowEvent e) {
                                }

                            });
                            flowFilterHelpExtPopupWindow.setUndecorated(true);
                            flowFilterHelpExtPopupWindow.add(flowFilterHelpExtPopup);
                            flowFilterHelpExtPopupWindow.pack();
                            // dialog.setLocationRelativeTo(null);
                            Point flowFilterHelpExtPT = flowFilterHelpExt.getLocationOnScreen();
                            int x = (int) flowFilterHelpExtPT.getX() - 2 - flowFilterHelpExtPopup.getWidth() + 2; // - 7;
                            if (x < 0) {
                                x = 0;
                            }
                            flowFilterHelpExtPopupWindow.setLocation(new Point(x, flowFilterHelpExtPT.y + 2)); // + 1
                            // ....requestFocus();
                            flowFilterHelpExtPopupWindow.setVisible(true);
                        }
                    }
                });
                 */
                flowFilterHelpExt.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                if (separateView == null) {
                                    separateView = new SeparateView(burpFrame, "Flow");
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
                // flowTable.getTableHeader().setReorderingAllowed(true);
                // flowTable.setAutoCreateRowSorter(true);
                flowTable.setRowSorter(flowTableSorter);
                for (int i = 0; i < flowTableModel.getColumnCount(); i++) {
                    TableColumn column = flowTable.getColumnModel().getColumn(i);
                    column.setMinWidth(20);
                    column.setPreferredWidth(flowTableModel.getPreferredWidth(i));
                }
                callbacks.customizeUiComponent(flowTable);
                JScrollPane flowTableScroll = new JScrollPane(flowTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                flowTableScroll.setMinimumSize(new Dimension(40, 40));
                callbacks.customizeUiComponent(flowTableScroll);
                flowTable.setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());
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
                callbacks.addSuiteTab(BurpExtension.this);
                // get burp frame
                burpFrame = (JFrame) SwingUtilities.getWindowAncestor(flowTab);
                // register ourselves as an HTTP listener
                callbacks.registerHttpListener(BurpExtension.this);
                // extension state listener
                callbacks.registerExtensionStateListener(BurpExtension.this);
                // scope change listener
                callbacks.registerScopeChangeListener(BurpExtension.this);
                //
                flowFilterSetDefaults();
                // stdout.println("Loaded.");
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

        // only process responses
        /*
        if (!messageIsRequest) {
            if (toolFlagCaptureProcessing(toolFlag)) {
                synchronized (flow) {
                    int row = flow.size();
                    // flow.add(new FlowEntry(toolFlag, callbacks.saveBuffersToTempFiles(messageInfo), helpers.analyzeRequest(messageInfo)));
                    flow.add(new FlowEntry(toolFlag, messageInfo));
                    flowTableModel.fireTableRowsInserted(row, row);
                }
            }
        }
         */
        // process both requests and responses
        if (toolFlagCaptureProcessing(toolFlag)) {
            synchronized (flow) {
                if (messageIsRequest) {
                    int row = flow.size();
                    flow.add(new FlowEntry(toolFlag, messageInfo));
                    flowTableModel.fireTableRowsInserted(row, row);
                    // stdout.println("[+] " + String.valueOf(helpers.analyzeRequest(messageInfo.getHttpService(), messageInfo.getRequest()).getUrl()));
                    // stdout.println("    " + String.valueOf(flowIncomplete.size()));
                } else {
                    FlowEntry flowIncompleteFound = null;
                    ArrayList<FlowEntry> flowIncompleteCleanup = new ArrayList<>();
                    Date outdated = new Date(System.currentTimeMillis() - 5 * 60 * 1000);
                    for (FlowEntry incomplete : flowIncomplete) {
                        if (flowIncompleteFound == null) {
                            if (incomplete.toolFlag == toolFlag && incomplete.incomplete.equals(helpers.bytesToString(messageInfo.getRequest()))) {
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
                        flowIncomplete.remove(flowIncompleteCleanup.get(0));
                        flowIncompleteCleanup.remove(0);
                    }
                    if (flowIncompleteFound != null) {
                        flowIncompleteFound.update(messageInfo);
                        if (flowCurrentEntry == flowIncompleteFound) {
                            flowTable.updateResponse();
                        }
                        int row = flow.indexOf(flowIncompleteFound);
                        flowTableModel.fireTableRowsUpdated(row, row);
                        flowIncomplete.remove(flowIncompleteFound);
                    }
                    // stdout.println("[-] " + String.valueOf(helpers.analyzeRequest(messageInfo.getHttpService(), messageInfo.getRequest()).getUrl()) + " [" + String.valueOf(helpers.analyzeResponse(messageInfo.getResponse()).getStatusCode()) + "]");
                    // stdout.println("    " + String.valueOf(flowIncomplete.size()) + ", match: " + String.valueOf(flowIncompleteFound != null));
                    // stdout.println(String.valueOf(flowIncomplete.size()));
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

    //
    // TODO misc
    //
    static Color cellBackground(int row, boolean isSelected) {
        if (isSelected) {
            return COLOR_HIGHLIGHT;
        } else if (row % 20 == 1) {
            return COLOR_DARKGRAY; // new Color(225, 225, 225);
        } else if (row % 2 == 1) {
            return COLOR_LIGHTGRAY; // new Color(240, 240, 240);
        } else {
            return Color.WHITE;
        }
    }

    void flowFilterCaptureSourceOnly(JCheckBox which) {

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

        flowFilterUpdateDescription();
    }

    void flowFilterSourceOnly(JCheckBox which) {

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
        boolean proxyOnly = flowFilterSourceProxyOnly.isSelected();
        boolean spiderOnly = flowFilterSourceSpiderOnly.isSelected();
        boolean scannerOnly = flowFilterSourceScannerOnly.isSelected();
        boolean repeaterOnly = flowFilterSourceRepeaterOnly.isSelected();
        boolean intruderOnly = flowFilterSourceIntruderOnly.isSelected();
        boolean extenderOnly = flowFilterSourceExtenderOnly.isSelected();
        if (toolFlag == IBurpExtenderCallbacks.TOOL_SPIDER) {
            if (proxyOnly || scannerOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterSourceSpider.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_SCANNER) {
            if (proxyOnly || spiderOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterSourceScanner.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER) {
            if (proxyOnly || spiderOnly || scannerOnly || intruderOnly || extenderOnly || !flowFilterSourceRepeater.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER) {
            if (proxyOnly || spiderOnly || scannerOnly || repeaterOnly || extenderOnly || !flowFilterSourceIntruder.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_EXTENDER) {
            if (proxyOnly || spiderOnly || scannerOnly || repeaterOnly || intruderOnly || !flowFilterSourceExtender.isSelected()) {
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
        boolean proxyOnly = flowFilterCaptureSourceProxyOnly.isSelected();
        boolean spiderOnly = flowFilterCaptureSourceSpiderOnly.isSelected();
        boolean scannerOnly = flowFilterCaptureSourceScannerOnly.isSelected();
        boolean repeaterOnly = flowFilterCaptureSourceRepeaterOnly.isSelected();
        boolean intruderOnly = flowFilterCaptureSourceIntruderOnly.isSelected();
        boolean extenderOnly = flowFilterCaptureSourceExtenderOnly.isSelected();
        if (toolFlag == IBurpExtenderCallbacks.TOOL_SPIDER) {
            if (proxyOnly || scannerOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceSpider.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_SCANNER) {
            if (proxyOnly || spiderOnly || repeaterOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceScanner.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER) {
            if (proxyOnly || spiderOnly || scannerOnly || intruderOnly || extenderOnly || !flowFilterCaptureSourceRepeater.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER) {
            if (proxyOnly || spiderOnly || scannerOnly || repeaterOnly || extenderOnly || !flowFilterCaptureSourceIntruder.isSelected()) {
                process = false;
            }
        } else if (toolFlag == IBurpExtenderCallbacks.TOOL_EXTENDER) {
            if (proxyOnly || spiderOnly || scannerOnly || repeaterOnly || intruderOnly || !flowFilterCaptureSourceExtender.isSelected()) {
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
        flowFilterParametrized.setSelected(false);
        flowFilterSearchField.setText("");
        flowFilterSearchCaseSensitive.setSelected(false);
        flowFilterSearchNegative.setSelected(false);
        // flowFilter
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

    // flow filter update description
    private void flowFilterUpdateDescription() {
        StringBuilder filterDescription = new StringBuilder("Filter: ");
        // filter
        boolean filterAll = true;
        if (flowFilterInscope.isSelected()) {
            filterDescription.append("In-scope, ");
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
        if (flowFilterSourceProxyOnly.isSelected()) {
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
        } else if (flowFilterSourceProxy.isSelected() && flowFilterSourceSpider.isSelected() && (burpFree || flowFilterSourceScanner.isSelected()) && flowFilterSourceRepeater.isSelected() && flowFilterSourceIntruder.isSelected() && flowFilterSourceExtender.isSelected()) {
            filterDescription.append("All sources");
        } else {
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
        if (flowFilterCaptureSourceProxyOnly.isSelected()) {
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
        } else if (flowFilterCaptureSourceProxy.isSelected() && flowFilterCaptureSourceSpider.isSelected() && (burpFree || flowFilterCaptureSourceScanner.isSelected()) && flowFilterCaptureSourceRepeater.isSelected() && flowFilterCaptureSourceIntruder.isSelected() && flowFilterCaptureSourceExtender.isSelected()) {
            filterDescription.append("All sources");
        } else {
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
        flowFilter.setText(filterDescription.toString());
    }

    // flow filter update
    private void flowFilterUpdate() {
        final ArrayList<RowFilter<FlowTableModel, Number>> mergedFilter = new ArrayList<>();
        // manual filter
        RowFilter<FlowTableModel, Number> manualFilter;
        manualFilter = new RowFilter<FlowTableModel, Number>() {
            @Override
            public boolean include(javax.swing.RowFilter.Entry<? extends FlowTableModel, ? extends Number> entry) {
                FlowEntry flowEntry;
                flowEntry = flow.get(entry.getIdentifier().intValue());
                boolean result = true;
                // in-scope
                if (flowFilterInscope.isSelected() && !callbacks.isInScope(flowEntry.url)) {
                    result = false;
                }
                // parametrized
                if (flowFilterParametrized.isSelected() && !flowEntry.hasParams) {
                    result = false;
                }
                // search
                if (flowFilterSearchField.getText().length() != 0) {
                    boolean found;
                    String text = flowFilterSearchField.getText();
                    String req = new String(flowEntry.messageInfoPersisted.getRequest());
                    byte[] response = flowEntry.messageInfoPersisted.getResponse();
                    String resp;
                    if (response != null) {
                        resp = new String(response);
                    } else {
                        resp = "";
                    }
                    if (flowFilterSearchCaseSensitive.isSelected()) {
                        found = Pattern.compile(Pattern.quote(text)).matcher(req).find() || Pattern.compile(Pattern.quote(text)).matcher(resp).find();
                    } else {
                        found = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(req).find() || Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(resp).find();
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
                if (!toolFlagFilterProcessing(flowEntry.toolFlag)) {
                    result = false;
                }
                return result;
            }
        };
        mergedFilter.add(manualFilter);
        // regex filter
        /*
        if (flowFilterSearchField.getText().length() != 0) {
            RowFilter<FlowTableModel, Number> regexFilter;
            try {
                if (flowFilterSearchCaseSensitive.isSelected()) {
                    regexFilter = RowFilter.regexFilter(flowFilterSearchField.getText());
                } else {
                    regexFilter = RowFilter.regexFilter("(?i)" + flowFilterSearchField.getText());
                }
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }

            if (flowFilterSearchNegative.isSelected()) {
                mergedFilter.add(RowFilter.notFilter(regexFilter));
            } else {
                mergedFilter.add(regexFilter);
            }
        }
         */
        // flowTableSorter.setRowFilter(RowFilter.andFilter(mergedFilter));
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                flowTableSorter.setRowFilter(RowFilter.andFilter(mergedFilter));
            }
        });
        flowFilterUpdateDescription();
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
            return 12;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 11:
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
                case 5:
                    return Boolean.class; // "Params"
                case 6:
                    return Integer.class; // "Count"
                case 7:
                    return Integer.class; // "Status"
                case 8:
                    return Integer.class; // "Length"
                //case 9:
                //        return "MIME";
                case 10:
                    return Date.class; // "Date"
                //case 11:
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
                    return 230;
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
                    return 130;
                case 11:
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
                    return "Params";
                case 6:
                    return "Count";
                case 7:
                    return "Status";
                case 8:
                    return "Length";
                case 9:
                    return "MIME";
                case 10:
                    return "Time";
                case 11:
                    return "Comment";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            FlowEntry flowEntry = flow.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return flowEntry.id; // rowIndex + 1;
                case 1:
                    return callbacks.getToolName(flowEntry.toolFlag);
                case 2:
                    return flowEntry.url.getProtocol() + "://" + flowEntry.url.getHost();
                case 3:
                    return flowEntry.method;
                case 4:
                    return flowEntry.url.getPath(); // .toString();
                case 5:
                    return flowEntry.hasParams;
                case 6:
                    return flowEntry.paramCount;
                case 7:
                    if (flowEntry.status != -1) {
                        return flowEntry.status;
                    } else {
                        return "";
                    }
                case 8:
                    if (flowEntry.length != -1) {
                        return flowEntry.length;
                    } else {
                        return "";
                    }
                case 9:
                    return flowEntry.mime;
                case 10:
                    return new SimpleDateFormat("HH:mm:ss d MMM yyyy").format(flowEntry.date);
                case 11:
                    return flowEntry.comment;
                default:
                    return "";
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
    private static final class FlowEntry {

        private static int newID = 1;
        private final int id;
        private final int toolFlag;
        private IHttpRequestResponsePersisted messageInfoPersisted;
        // private IHttpRequestResponse messageInfo;
        private String incomplete;
        private final String method;
        private final URL url;
        private final boolean hasParams;
        private short paramCount;
        private Short status;
        private int length;
        private String mime;
        private final Date date;
        private String comment;

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
        }

        FlowEntry(int toolFlag, IHttpRequestResponse messageInfo) {
            this.id = FlowEntry.newID;
            FlowEntry.newID += 1;
            this.toolFlag = toolFlag;
            // this.messageInfo = messageInfo;
            messageInfoPersisted = callbacks.saveBuffersToTempFiles(messageInfo);
            IRequestInfo requestInfo;
            requestInfo = helpers.analyzeRequest(messageInfo);
            method = requestInfo.getMethod();
            url = requestInfo.getUrl();
            byte[] response = messageInfo.getResponse();
            if (response == null) {
                incomplete = helpers.bytesToString(messageInfo.getRequest());
                flowIncomplete.add(this);
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
    @SuppressWarnings("serial")
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

    @SuppressWarnings("serial")
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
            add(new Separator());
            /*JMenuItem delete = new JMenuItem("Delete");
            delete.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (popupPointedFlowEntry != null) {
                        flowTableModel.remove(popupPointedFlowEntry);
                        popupPointedFlowEntry = null;
                    }
                }
            });
            add(delete);*/
 /*JMenuItem temp = new JMenuItem("temp");
            temp.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    tempy = popupPointedFlowEntry;
                    flowTable.repaint();
                }
            });
            add(temp);*/
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
            JMenuItem clearAll = new JMenuItem("Clear history");
            clearAll.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(burpFrame, "Are you sure you want to clear the history?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                        flowTableModel.removeAll();
                    }
                }
            });
            add(clearAll);
        }
    }

    @SuppressWarnings("serial")
    class FlowTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            // c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
            /*if (!isSelected && tempy != null && flowTable.convertRowIndexToModel(row) == flow.indexOf(tempy)) {
				c.setForeground(Color.blue);
			} else {
				c.setForeground(Color.black);
			}*/
            int modelRow = table.convertRowIndexToModel(row);
            FlowEntry entry = flow.get(modelRow);
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

            c.setBackground(cellBackground(table.getRowCount() - row, isSelected));

            return c;
        }
    }

    //
    // SeparateView
    //
    public class SeparateView extends JDialog {

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
                //
            }
            super.dispose();
        }

        public SeparateView(final Component parent, String title) {
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

    private static class BooleanTableCellRenderer extends JCheckBox implements TableCellRenderer {

        public BooleanTableCellRenderer() {
            super();
            initialize();
        }

        private void initialize() {
            setOpaque(true);
            putClientProperty("JComponent.sizeVariant", "small");
            SwingUtilities.updateComponentTreeUI(this);
            setLayout(new GridBagLayout());
            setMargin(new Insets(0, 0, 0, 0));
            setHorizontalAlignment(JLabel.CENTER);
            setBorderPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            //int modelRow = table.convertRowIndexToModel(row);
            setBackground(cellBackground(table.getRowCount() - row, isSelected));
            if (value instanceof Boolean) {
                setSelected((Boolean) value);
            }
            return this;
        }
    }
}
