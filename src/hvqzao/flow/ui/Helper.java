package hvqzao.flow.ui;

import static hvqzao.flow.FlowExtension.getSortOrder;
import java.awt.Color;

public class Helper {

    private static final Color COLOR_HIGHLIGHT = new Color(255, 197, 153); // new Color(255, 206, 130);
    private static final Color COLOR_DARKGRAY = new Color(240, 240, 240);
    private static final Color COLOR_LIGHTGRAY = new Color(250, 250, 250);

    public static Color cellBackground(int rowCount, int row, boolean isSelected) {
        int r = row;
        if (getSortOrder() < 1) {
            r = rowCount - row;
        }
        if (isSelected) {
            return COLOR_HIGHLIGHT;
        } else if (r % 20 == 1) {
            return COLOR_DARKGRAY; // new Color(225, 225, 225);
        } else if (r % 2 == 1) {
            return COLOR_LIGHTGRAY; // new Color(240, 240, 240);
        } else {
            return Color.WHITE;
        }
    }
}
