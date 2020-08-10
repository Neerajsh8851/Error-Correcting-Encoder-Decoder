package correcter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Send {

    private final byte[] data;
    private final File destFile;

    public Send(byte[] data, File destFile) {
        this.data = data;
        this.destFile = destFile;
    }

    public byte[] send() {
        Random rand = new Random();
        byte[] sendData = new byte[data.length];
        for(int i = 0; i < data.length; i++ )
            sendData[i] = simulateError(data[i], rand.nextInt(8));

        // save the data to the destination
        try (FileOutputStream fos = new FileOutputStream(destFile))
        {
            fos.write(sendData);
            return sendData;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return data;
        }
    }

    private byte simulateError(byte b, int r) {
        return (byte)(b ^ (1 << r));
    }

}
