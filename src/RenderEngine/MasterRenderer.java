package RenderEngine;

import Entities.Camera;
import Entities.Entity;
import Models.TexturedModel;
import Shaders.StaticShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    Matrix4f projectionMatrix;

    private static final float POV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 100000f;

    StaticShader shader = new StaticShader();
    EntityRender renderer = new EntityRender();

    Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

    public MasterRenderer() {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }


    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0.4f, 0.7f, 1f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Camera camera) {
        prepare();
        shader.start();
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

        entities.clear();

    }

    public void addEntity(Entity entity){

        TexturedModel model = entity.getModel();

        List<Entity> batch = entities.get(model);
        if(batch != null){
            batch.add(entity);
        }else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(model, newBatch);
        }
    }

    public void createProjectionMatrix() {

        projectionMatrix = new Matrix4f();

        float aspect = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) (1f / Math.tan(Math.toRadians(POV / 2f)));
        float xScale = yScale / aspect;
        float zp = FAR_PLANE + NEAR_PLANE;
        float zm = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -zp / zm;
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -(2 * FAR_PLANE * NEAR_PLANE) / zm;
        projectionMatrix.m33 = 0;


    }
}
