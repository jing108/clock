package www.theboy.wang.clock.view.wheelview;

/**
 * Created by wands_wang on 2017/9/29.
 */

public interface ValueAdapter {
    int maxItems();
    String formatValue(int x);
    int valueMaxLength();
}
