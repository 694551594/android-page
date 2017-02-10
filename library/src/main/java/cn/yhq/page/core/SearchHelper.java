package cn.yhq.page.core;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/9.
 */

public class SearchHelper {

    private final static List<String> keywords = new ArrayList<>();

    static {
        keywords.add(".");
        keywords.add("$");
        keywords.add("(");
        keywords.add(")");
        keywords.add("*");
        keywords.add("+");
        keywords.add("[");
        keywords.add("?");
        keywords.add("\\");
        keywords.add("^");
        keywords.add("{");
        keywords.add("|");
    }

    private static Pattern buildPattern(String keyword) {
        if (keywords.contains(keyword)) {
            keyword = "\\" + keyword;
        }
        return Pattern.compile(keyword + "+");
    }

    public static CharSequence match(CharSequence text, String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return text;
        }
        Matcher matcher = buildPattern(keyword).matcher(text);
        SpannableString spannable = new SpannableString(text);// 用于可变字符串
        while (matcher.find()) {
            highlight(spannable, matcher.start(), matcher.end());
        }
        return spannable;
    }

    private static SpannableString highlight(SpannableString text, int start, int end) {
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        text.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

}