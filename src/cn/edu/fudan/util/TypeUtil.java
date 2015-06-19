package cn.edu.fudan.util;

/**
 * Created by Dawnwords on 2015/6/19.
 */
public class TypeUtil {

    public static Class string2Class(String type) {
        if ("String".equals(type)) return String.class;
        if ("Int".equals(type)) return int.class;
        if ("Double".equals(type)) return double.class;
        return null;
    }

    public static Parser string2Parser(String type) {
        if ("String".equals(type)) return Parser.StringParser;
        if ("Int".equals(type)) return Parser.IntParser;
        if ("Double".equals(type)) return Parser.DoubleParser;
        return null;
    }

    public interface Parser {
        Object parse(Object o);

        Parser StringParser = new Parser() {
            @Override
            public Object parse(Object o) {
                return o.toString();
            }
        }, IntParser = new Parser() {
            @Override
            public Object parse(Object o) {
                return Integer.parseInt(o.toString());
            }
        }, DoubleParser = new Parser() {
            @Override
            public Object parse(Object o) {
                return Double.parseDouble(o.toString());
            }
        };

    }
}
