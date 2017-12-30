package me.chanchangxing.selector.ui;

import me.chanchangxing.selector.db.Bean;

/**
 * Created by chenchangxing on 2017/12/25.
 */

public interface OnMoveListener {
    void onMove(Bean province, Bean city, Bean district);
}
