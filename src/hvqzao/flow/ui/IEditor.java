package hvqzao.flow.ui;

import burp.IBurpExtenderCallbacks;
import javax.swing.JTextArea;

public interface IEditor {

    public void customizeUiComponent(final IBurpExtenderCallbacks callbacks);

    public void setText(byte[] message);

    public JTextArea getEditor();

}
