package correcter;

import java.util.Arrays;

public class EncoderDecoder {

    public static StringBuilder expand = new StringBuilder();
    public static StringBuilder parity = new StringBuilder();
    public static StringBuilder correct = new StringBuilder();

    public static void main(String[] args) {
        String hello = "hello";
        byte[] encoded = encode(hello.getBytes());
        System.out.println("data: " + Arrays.toString(hello.getBytes()));
        System.out.println(" encoded: " + Arrays.toString(encoded) );

        System.out.println(expand); System.out.println(parity);

        System.out.println("decoded: " +Arrays.toString(decode(encoded)) );

    }

    public static byte[] encode(byte[] data) {

        byte[] encodedData = new byte[data.length*2];
        int index = 0;

        for (byte byt : data) {
            // divide the byt in temp1 , temp2
           byte temp1 =  (byte) ((byt & (15 << 4)) >> 4);
           byte temp2 =  (byte) (byt & 15);

           // hamming code for  temp1
            encodedData[index++] = createHammingWord(temp1);

            // hamming code for temp2
            encodedData[index++] = createHammingWord(temp2);
        }

        return encodedData;
    }
    public  static byte[] decode (byte[] data) {
        byte[] decodedData = new byte[data.length/2];
        int index = 0;

        for (int i = 0; i < data.length; i += 2) {
           decodedData[index++] = decode(errorCorrection(data[i]), errorCorrection(data[i+1])) ;
        }
        return  decodedData;
    }


    // data: 4 bits data , max : 00001111
    private  static  byte createHammingWord (byte data) {
        byte p1, p2, a, p4, b, c, d;

        a =  (byte) ((data & (1 << 3)) >> 3);
        b =  (byte) ((data & (1 << 2)) >> 2);
        c =  (byte) ((data & (1 << 1)) >> 1);
        d =  (byte) (data & 1);

        p1 = (byte) (a ^ b ^ d);
        p2 = (byte) (a ^ c ^ d);
        p4 = (byte) (b ^ c ^ d);

        expand.append("..").append(a).append(".").append(b).append(c).append(d).append(". ");
        parity.append(p1 + "" + p2).append(a).append(p4).append(b).append(c).append(d).append("0 ");

        return  (byte) (p1 << 7 | p2 << 6 | a << 5| p4 << 4 | b << 3 | c << 2| d << 1);
    }
    private  static  byte errorCorrection (byte data) {
        byte p1, p2, a, p4, b, c, d;

        // read all first 7 bits
        p1 = (byte) ((data & (1 << 7)) >> 7);
        p2 = (byte) ((data & (1 << 6)) >> 6);
        a = (byte) ((data & (1 << 5)) >> 5);
        p4 = (byte) ((data & (1 << 4)) >> 4);
        b = (byte) ((data & (1 << 3)) >> 3);
        c = (byte) ((data & (1 << 2)) >> 2);
        d = (byte) ((data & (1 << 1)) >> 1);

        // calculate the position of error
        int p = 0;

        if ((a ^ b ^ d ^ p1) == 1)
            p += 1;

        if ((a ^ c ^ d ^ p2) == 1)
            p += 2;

        if ((c ^ b ^ d ^ p4) == 1)
            p += 4;

        byte correctData = (byte) (data ^ (1 << (8 - p)));

        correct.append(Integer.toBinaryString(correctData & 0xff)).append(" ");
        return  correctData;
    }
    private  static  byte decode (byte byt1, byte byt2) {
        return (byte)
                (       ((byt1 & (1 << 5)) << 2) |
                        ((byt1 & (1 << 3)) << 3) |
                        ((byt1 & (1 << 2)) << 3) |
                        ((byt1 & (1 << 1)) << 3) |
                        ((byt2 & (1 << 5)) >>> 2) |
                        ((byt2 & (1 << 3)) >>> 1) |
                        ((byt2 & (1 << 2)) >>> 1) |
                        ((byt2 & (1 << 1)) >>> 1)
                );
    }



}
