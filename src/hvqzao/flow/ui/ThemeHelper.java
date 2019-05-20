package hvqzao.flow.ui;

import static hvqzao.flow.FlowExtension.getSortOrder;
import java.awt.Color;
import javax.swing.UIManager;

public class ThemeHelper {

    //private static final Color COLOR_NIMBUS_HIGHLIGHT = new Color(255, 197, 153); // new Color(255, 206, 130);
    private static final Color COLOR_NIMBUS_DARKGRAY = new Color(240, 240, 240);
    private static final Color COLOR_NIMBUS_LIGHTGRAY = new Color(250, 250, 250);

    private static final String THEME_NAME = UIManager.getLookAndFeel().getName().toLowerCase();
    private static final boolean DARK_THEME = "darcula".equals(THEME_NAME);

    private static final Color COLOR_TABLE_BACKGROUND = UIManager.getColor("Table.background");
    private static final Color COLOR_TABLE_SELECTIONBACKGROUND = UIManager.getColor("Tree.selectionBackground");

    public static Color cellBackground(int rowCount, int row, boolean isSelected) {
        if (DARK_THEME) {
            if (isSelected) {
                return COLOR_TABLE_SELECTIONBACKGROUND;
            } else {
                return COLOR_TABLE_BACKGROUND;
            }
        }
        int r = row;
        if (getSortOrder() < 1) {
            r = rowCount - row;
        }
        if (isSelected) {
            //return COLOR_NIMBUS_HIGHLIGHT;
            return COLOR_TABLE_SELECTIONBACKGROUND;
        } else if (r % 20 == 1) {
            return COLOR_NIMBUS_DARKGRAY; // new Color(225, 225, 225);
        } else if (r % 2 == 1) {
            return COLOR_NIMBUS_LIGHTGRAY; // new Color(240, 240, 240);
        } else {
            //return COLOR_TABLE_BACKGROUND;
            return Color.WHITE;
        }
    }

    public static boolean isDarkTheme() {
        return DARK_THEME;
    }
}
