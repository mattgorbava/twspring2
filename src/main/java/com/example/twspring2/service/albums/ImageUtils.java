package com.example.twspring2.service.albums;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {
    public static final int BITE_SIZE = 4 * 1024;

    public static byte[] compressImage(byte[] image) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(image);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(image.length);
        byte[] buffer = new byte[BITE_SIZE];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();

        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] compressedImage) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedImage);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedImage.length);
        byte[] buffer = new byte[BITE_SIZE];

        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();

        return outputStream.toByteArray();
    }
}
