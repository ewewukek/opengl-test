package ewewukek.gl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import ewewukek.common.IDisposable;

public class Texture implements IDisposable {
    private int texture;

    protected Texture() {}

    public Texture(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("texture not found "+path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("could not load texture "+path);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(4 * width * height);
        for (int y = height - 1; y >= 0; --y) {
            for (int x = 0; x != width; ++x) {
                int pixel = pixels[y*width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                buffer.put((byte) ((pixel >> 8) & 0xFF)); // G
                buffer.put((byte) (pixel & 0xFF)); // B
                buffer.put((byte) ((pixel >> 24) & 0XFF)); // A
            }
        }
        buffer.flip();

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind(int index) {
        if (index < 0 || index > 31)
            throw new IndexOutOfBoundsException("texture index out of bounds: "+index);
        glActiveTexture(GL_TEXTURE0 + index);
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    @Override
    public void dispose() {
        if (texture != 0) { glDeleteTextures(texture); texture = 0; }
    }
}