package org.springbus.test.cmp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Testcmp {

    public static void main(String[] arge) {
        //字符串1  altbe  字符串2  altaf
        String altbe = "", altaf = "";
        List<Integer> beList = rememberSpacing(altbe);
        List<Integer> afList = rememberSpacing(altaf);
        altbe = altbe.replace(" ", "");
        altaf = altaf.replace(" ", "");
        Diff_match_patch diff_match_patch = new Diff_match_patch();
        LinkedList<Diff_match_patch.Diff> t = diff_match_patch.diff_main(altbe, altaf);
        StringBuffer s1 = new StringBuffer();
        StringBuffer s2 = new StringBuffer();
        Integer indexBe = 0;
        Integer indexAf = 0;
        for (Diff_match_patch.Diff diff : t) {
            StringBuffer diffTextBe = new StringBuffer(diff.text);
            StringBuffer diffTextAf = new StringBuffer(diff.text);
            if (diff.operation.toString().equalsIgnoreCase("EQUAL")) {
                addSpacing(beList, indexBe, diffTextBe);
                addSpacing(afList, indexAf, diffTextAf);
                s1.append(diffTextBe);
                s2.append(diffTextAf);
                indexBe += diffTextBe.length();
                indexAf += diffTextAf.length();
            }
            indexBe = appendString2("DELETE", diff, s1, s2, beList, indexBe);
            indexAf = appendString2("INSERT", diff, s2, s1, afList, indexAf);
        }
        System.out.println(s1.toString());
        System.out.println(s2.toString());
    }

    public static void appendString(String type, Diff_match_patch.Diff diff, StringBuffer sbOne, StringBuffer sbTwo) {
        if (type.equals(diff.operation.toString())) {
            if (" ".equals(diff.text)) {
                sbOne.append(" ");
                sbTwo.append(" ");
            } else {
                sbOne.append("<em class='f-required'>").append(diff.text).append("</em>");
            }
        }
    }

    public static List<Integer> rememberSpacing(String str) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < str.length(); i++) {
            if (' ' == str.charAt(i)) {
                list.add(i);
            }
        }
        return list;
    }

    public static void addSpacing(List<Integer> list, Integer index, StringBuffer str) {
        for (Integer o : list) {
            if (o >= index && o < index + str.length()) {
                str.insert(o - index, ' ');
            }
        }
    }

    public static Integer appendString2(String type, Diff_match_patch.Diff diff, StringBuffer sbOne,
                                        StringBuffer sbTwo, List<Integer> list, Integer i) {
        Integer result = i;
        if (type.equals(diff.operation.toString())) {
            StringBuffer sb = new StringBuffer(diff.text);
            for (Integer o : list) {
                if (o >= i && o < i + sb.length()) {
                    sb.insert(o - i, ' ');
                }
            }
            sbOne.append("<em class='f-required'>").append(sb).append("</em>");
            result = i + sb.length();
        }
        return result;
    }

}
