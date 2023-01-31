package com.example.passit;

import android.text.Spanned;

public class InputFilter implements android.text.InputFilter {
    private int min, max;

    public InputFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilter(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String newVal = dest.subSequence(0, dstart)
                    + source.subSequence(start, end).toString()
                    + dest.subSequence(dend, dest.length());
            int input = Integer.parseInt(newVal);
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
