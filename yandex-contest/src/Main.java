import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static String removeChatAt(String s, Integer idx) {
        return s.substring(0, idx) + s.substring(idx+1);
    }
    public static String insertCharAt(String s, String ch, Integer idx) {
        return s.substring(0, idx) + ch + s.substring(idx+1);
    }
    public static String Check(String s, Integer cursorIdx) {
        System.out.println("before: " + s);
        var begin = s.indexOf('<');
//        System.out.println("cursorIdx " + cursorIdx + " begin " + begin);
        if (begin == -1) {
//            s = s.substring(0, cursorIdx) + s.
            return s;
        }
        var end = s.indexOf('>');
        var tag = s.substring(begin+1, end);
//        System.out.println("tag: " + tag);
        if (tag.equals("left")) {
            cursorIdx--;
        } else if (tag.equals("right")) {
            cursorIdx++;
        }
        var ax = s.substring(end+1).indexOf("<");
        if (ax != -1) {
            System.out.println("after tag: " + s.substring(end+1, ax));
        }
        s = s.substring(0, begin) + s.substring(end+1);
        System.out.println("removed tag, now: " + s);

        if (tag.equals("bspace")) {
            s = s.substring(0, cursorIdx-1) + s.substring(cursorIdx);
            cursorIdx--;
        } else if (tag.equals("delete")) {
            s = s.substring(0, cursorIdx) + s.substring(cursorIdx+1);
        }
        System.out.println("cursorIdx " + cursorIdx + " begin " + begin);
        System.out.println("after: " + s);
        if (tag.equals("delete")) {
            cursorIdx = cursorIdx + tag.length() + 2;
        }

        System.out.println();
        return Check(s, cursorIdx);
    }
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var need = scanner.nextLine();
        var s = scanner.nextLine();

        var result = Check(s, s.indexOf('<'));
//        System.out.println(result);
        System.out.println(result.equals(need) ? "Yes" : "No");
    }
}