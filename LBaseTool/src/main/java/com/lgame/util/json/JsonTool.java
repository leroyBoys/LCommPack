package com.lgame.util.json;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/16.
 */
public class JsonTool {

    public static String bean2json(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {}
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = getJsonFromBean(props[i].getName());
                    String value = getJsonFromBean(props[i].getReadMethod().invoke(bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {}
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String getJsonFromCollection(Collection<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(getJsonFromBean(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static <T> String getJsonFromArray(T... array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(getJsonFromBean(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }


    public static String getJsonFromArray(int[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (int i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromDArray(double[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (double i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromBArray(byte[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (byte i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromCArray(char[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (char i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromBArray(boolean[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (boolean i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromFArray(float[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (float i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromLArray(long[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (long i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String getJsonFromSArray(short[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (short i : array) {
                json.append(i);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
            return json.toString();
        }
        json.append("]");
        return json.toString();
    }

    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(getJsonFromBean(key));
                json.append(":");
                json.append(getJsonFromBean(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    private static String string2json(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= '\u0000' && ch <= '\u001F') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }

    public static String getJsonFromBean(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
           // json.append("\"\"");
            json.append("null");
        } else if (obj instanceof String ||
                obj instanceof Integer ||
                obj instanceof Float  ||
                obj instanceof Boolean ||
                obj instanceof Short ||
                obj instanceof Double ||
                obj instanceof Long ||
                obj instanceof BigDecimal ||
                obj instanceof BigInteger ||
                obj instanceof Byte) {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        } else if(obj instanceof int[]){
            json.append(getJsonFromArray((int[]) obj));
        } else if(obj instanceof double[]){
            json.append(getJsonFromDArray((double[]) obj));
        } else if(obj instanceof float[]){
            json.append(getJsonFromFArray((float[]) obj));
        }else if(obj instanceof byte[]){
            json.append(getJsonFromBArray((byte[]) obj));
        }else if(obj instanceof char[]){
            json.append(getJsonFromCArray((char[]) obj));
        }else if(obj instanceof boolean[]){
            json.append(getJsonFromBArray((boolean[]) obj));
        }else if(obj instanceof long[]){
            json.append(getJsonFromLArray((long[]) obj));
        }else if(obj instanceof short[]){
            json.append(getJsonFromSArray((short[]) obj));
        }else if (obj instanceof Object[]) {
            json.append(getJsonFromArray((Object[]) obj));
        } else if (obj instanceof Collection) {
            json.append(getJsonFromCollection((Collection<?>) obj));
        } else if (obj instanceof Map) {
            json.append(map2json((Map<?, ?>) obj));
        } else {
            json.append(bean2json(obj));
        }
        return json.toString();
    }
}
