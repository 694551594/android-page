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

    public final static List<Character> EscapeKeywords = new ArrayList<>();

    static {
        EscapeKeywords.add('.');
        EscapeKeywords.add('$');
        EscapeKeywords.add('(');
        EscapeKeywords.add(')');
        EscapeKeywords.add('*');
        EscapeKeywords.add('+');
        EscapeKeywords.add('[');
        EscapeKeywords.add('?');
        EscapeKeywords.add('\\');
        EscapeKeywords.add('^');
        EscapeKeywords.add('{');
        EscapeKeywords.add('|');
    }

    public static Pattern buildPattern(String keyword) {
        List<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        return buildPattern(keywords);
    }

    public static Pattern buildPattern(List<String> keywords) {
        String pattern = "";
        for (String keyword : keywords) {
            StringBuffer buffer = new StringBuffer();
            for (char c : keyword.toCharArray()) {
                if (EscapeKeywords.contains(c)) {
                    buffer.append("\\" + c);
                } else {
                    buffer.append(c);
                }
            }
            pattern += "((" + buffer.toString() + ")+)|";
        }
        return Pattern.compile(pattern.substring(0, pattern.length() - 1));
    }

    public static CharSequence match(CharSequence text, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return text;
        }
        Matcher matcher = buildPattern(keywords).matcher(text);
        SpannableString spannable = new SpannableString(text);// 用于可变字符串
        while (matcher.find()) {
            highlight(spannable, matcher.start(), matcher.end());
        }
        return spannable;
    }

    public static CharSequence match(CharSequence text, String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return text;
        }
        List<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        return match(text, keywords);
    }

    private static SpannableString highlight(SpannableString text, int start, int end) {
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        text.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

}