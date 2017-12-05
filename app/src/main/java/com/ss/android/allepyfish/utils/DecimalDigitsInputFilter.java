package com.ss.android.allepyfish.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 11/5/2017.
 */

public class DecimalDigitsInputFilter implements InputFilter {

    private int mDigitsBeforeZero;
    private int mDigitsAfterZero;
    private Pattern mPattern;

    private static final int DIGITS_BEFORE_ZERO_DEFAULT = 100;
    private static final int DIGITS_AFTER_ZERO_DEFAULT = 100;

    Pattern NUMBERS_WITH_DOTS = Pattern.compile("^\\d+(\\.\\d+)*$");



    public DecimalDigitsInputFilter(Integer digitsBeforeZero, Integer digitsAfterZero) {
        this.mDigitsBeforeZero = (digitsBeforeZero != null ? digitsBeforeZero : DIGITS_BEFORE_ZERO_DEFAULT);
        this.mDigitsAfterZero = (digitsAfterZero != null ? digitsAfterZero : DIGITS_AFTER_ZERO_DEFAULT);
        mPattern = Pattern.compile("-?[0-9]{0," + (mDigitsBeforeZero) + "}+((\\.[0-9]{0," + (mDigitsAfterZero)+ "})?)||(\\.)?");
//        mPattern = Pattern.compile("(^(\\+|\\-)(0|([1-9][0-9]*))(\\.[0-9]{1,2})?$)|(^(0{0,1}|([1-9][0-9]*))(\\.[0-9]{1,2})?$)");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String replacement = source.subSequence(start, end).toString();
        String newVal = dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length()).toString();
        Matcher matcher = mPattern.matcher(newVal);
        if (matcher.matches())
            return null;

        if (TextUtils.isEmpty(source))
            return dest.subSequence(dstart, dend);
        else
            return "";
    }
}