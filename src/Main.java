package correcter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Blogger -> https://w3cleverprogrammer.blogspot.com
 * Name -> neeraj sharma
 * email -> nsxylush@gmail.com
 */

public class Main {
    private static final File SEND_FILE = new File("send.txt");
    private static final File ENCODED_FILE = new File("encoded.txt");
    private static final File RECIEVED_FILE = new File("received.txt");
    private static final File DECODED_FILE = new File("decoded.txt");


    public static void main(String[] args) {

        System.out.print("write a mode: ");
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();
        // modes
        modes(mode);
        scanner.close();
    }

    private static void modes(String mode) {
        switch (mode) {
            case "encode":
                encode();
                break;
            case "send":
                send();
                break;
            case "decode":
                decode();
                break;
        }
    }

    private static void encode() {
        try (FileInputStream sendFileInStream = new FileInputStream(Main.SEND_FILE);
             FileOutputStream encodedFileOutStream = new FileOutputStream(ENCODED_FILE)
        )
        {
            byte[] send = sendFileInStream.readAllBytes();
//            EncoderDecoder encoder = new EncoderDecoder(send);

            // all views for send.txt file
            System.out.println("\nsend.txt: ");
            String textView = new String(send);
            System.out.println("text view: " + textView);
            System.out.println("hex view: " + hexView(send));
            System.out.println("binary view: " + binaryView(send) + "\n\n");

            // store encoded data to encoded.txt file
            byte[] encoded = EncoderDecoder.encode(send);
            encodedFileOutStream.write(encoded);
            // all views for the encoded.txt file
            System.out.println("encoded.txt: ");
            System.out.println(EncoderDecoder.expand);
            System.out.println("parity: " +  EncoderDecoder.parity);
            System.out.println("hex view: " + hexView(encoded));

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    private static void decode() {
        try (FileInputStream receivedFileInStream = new FileInputStream(Main.RECIEVED_FILE);
             FileOutputStream decodedFileOutStream = new FileOutputStream(DECODED_FILE))
        {
            byte[] receivedData = receivedFileInStream.readAllBytes();

            // all views for recieved.txt
            System.out.println("\nrecieved.txt: ");
            System.out.println("hex view: " + hexView(receivedData));
            System.out.println("bin view: " + binaryView(receivedData));

            // all views for decoded.txt
            System.out.println("\ndecoded.txt: ");
            byte[] decodedData = EncoderDecoder.decode(receivedData);
            decodedFileOutStream.write(decodedData);

            System.out.println("correct: " + EncoderDecoder.correct);
            System.out.println("decode: " + binaryView(decodedData));
            System.out.println("hex view: " + hexView(decodedData));
            System.out.println("text view: " + new String(decodedData, StandardCharsets.UTF_8));

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void send() {
        try (FileInputStream encodedFileInstream = new FileInputStream(Main.ENCODED_FILE))
        {
            byte[] encodedData = encodedFileInstream.readAllBytes();
            Send send = new Send(encodedData, RECIEVED_FILE);

            // all view for encoded.txt
            System.out.println("\nencoded.txt: ");
            System.out.println("hex view: " + hexView(encodedData));
            System.out.println("bin view: " + binaryView(encodedData));

            // send the data to received.txt file
            byte[] receivedData = send.send();
            // all views for received.txt
            System.out.println("\nreceived.txt: ");
            System.out.println("bin view: " + binaryView(receivedData));
            System.out.println("hex view: " + hexView(receivedData));

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }



    private static String hexView(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            int ubsignB = b & 0xff;
            String s = Integer.toHexString(ubsignB);
            if (s.length() != 2)
                s = "0"+s;
            hex.append(s).append(" ");
        }

        return hex.toString();
    }

    private static String binaryView(byte[] bytes) {
        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            int unsigned = b & 0xff;
            String s = Integer.toBinaryString(unsigned);
            s = "0".repeat(8 - s.length()) + s;

            hex.append(s).append(" ");
        }

        return hex.toString();
    }
}

