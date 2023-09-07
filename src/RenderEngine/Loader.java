package RenderEngine;

import Models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Loader {

    static List<Integer> vaos = new ArrayList<Integer>();
    static List<Integer> vbos = new ArrayList<Integer>();
    static List<Integer> textures = new ArrayList<Integer>();




    public RawModel loadToVao(float[] vertices, int[] indices, float[] uv) {
        int vaoID = createVao();
        storeDataInAttributeList(vertices , 0, 3);
        storeDataInAttributeList(uv , 1, 2);
        bindIndicesBuffer(indices);
        GL30.glBindVertexArray(0);

        return new RawModel(vaoID, indices.length);
    }

    private int createVao() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    public int loadTexture(String fileName){
        Texture texture = null;

        try {
            texture = TextureLoader.getTexture("PNG", Objects.requireNonNull(TextureLoader.class.getResourceAsStream("/res/" + fileName + ".PNG")));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -4);


        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID = texture.getTextureID();
        textures.add(textureID);

        return textureID;

    }

    private void storeDataInAttributeList(float[] data, int attributeNumber, int dimentions) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, dimentions, GL11.GL_FLOAT , false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {
        for (int vao: vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo: vbos) {
            GL30.glDeleteVertexArrays(vbo);
        }
        for (int texture: textures) {
            GL11.glDeleteTextures(texture);
        }
    }


}
