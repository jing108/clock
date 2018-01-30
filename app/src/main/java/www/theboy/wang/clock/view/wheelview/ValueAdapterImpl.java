package www.theboy.wang.clock.view.wheelview;

/**
 * Created by wands_wang on 2017/9/29.
 */

public class ValueAdapterImpl implements ValueAdapter {

    private int min;
    private int max;
    private String format;

    public ValueAdapterImpl(int max, String format) {
        min = 0;
        this.max = max;
        this.format = format;
    }

    @Override
    public int maxItems() {
        return max - min + 1;
    }

    @Override
    public String formatValue(int value) {
        String result = null;
        if (value > 0 && value < maxItems()) {
            if (format != null) {
                result = String.format(format, min + value);
            } else {
                result = String.valueOf(min + value);
            }
        }
        return result;
    }

    @Override
    public int valueMaxLength() {
        int length = String.valueOf(Math.max(Math.abs(max), Math.abs(min))).length();
        if (min < 0) {
            length++;
        }
        return length;
    }
}
