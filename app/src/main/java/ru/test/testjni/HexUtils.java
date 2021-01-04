package ru.test.testjni;

/**
 * 作者: Sunshine
 * 时间: 2017/4/14.
 * 邮箱: 44493547@qq.com
 * 描述:
 */

public class HexUtils {

    /**
     * 十六进制String转换成Byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    public static byte[] shortToByte(short value) {
        byte[] abyte = new byte[2];
        abyte[0] = (byte) ((0xFF00 & value) >> 8);
        abyte[1] = (byte) ((0xFF & value) >> 0);
        return abyte;
    }
    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /* 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static float byteArrayToFloat(byte[] src) {
        return Float.intBitsToFloat(byteArrayToInt(src));
    }

    public static byte[] floatToByteArray(float f) {
        return intToByteArray(Float.floatToIntBits(f));
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static char[] hexs = "0123456789abcdef".toCharArray();

    public static String hexAdd(String hex, int num) {
        int len = hex.length() - 1;
        //是否需要进位
        boolean b = true;
        //参与了运算的位，运算之后的结果字符串，
        //由右向左运算，首先是最后一位参与运算，如果需要进位，那么倒数第二位也要参与运算。以些类推
        String change = "";
        //最终运算结果，由未参与运算的位和参与了运算的位两部分组成
        String result = "";
        while (b && len >= 0) {
            char c = hex.charAt(len);
            int index = indexOfCharArray(hexs, c);
            if (index == -1) {
                return "所传的字符串参数非法！";
            } else {
                int sum = index + num;
                if (sum < 16) {
                    change = hexs[sum] + change;
                    result = hex.substring(0, len) + change;
                    b = false;
                } else {
                    change = hexs[sum % 16] + change;
                    num = sum / 16;
                }
            }
            --len;
        }
        while (b) {
            if (num < 16) {
                change = hexs[num] + change;
                result = change;
                b = false;
            } else {
                change = hexs[num % 16] + change;
                num /= 16;
            }
        }

        return result;
    }

    /**
     * 获取字符在字符数组中的下标
     * 参数一：字符数组
     * 参数二：字符
     * 返回值：字符在字符数组中的下标，如果字符数组中没有该字符，返回-1
     */
    public static int indexOfCharArray(char[] charArray, char c) {
        for (int i = 0, len = charArray.length; i < len; ++i) {
            if (charArray[i] == c) {
                return i;
            }
        }
        return -1;


    }

}
