package com.example.passit.helpers;

import android.text.Spanned;

public class InputFilterMinMax implements android.text.InputFilter {
    private final int min;
    private final int max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        try {
            String val = spanned.subSequence(0, i2)
                    + charSequence.subSequence(i, i1).toString()
                    + spanned.subSequence(i3, spanned.length());
            int userInput = Integer.parseInt(val);
            if (isInRange(min, max, userInput))
                return null;
        } catch (NumberFormatException e) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
