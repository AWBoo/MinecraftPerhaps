package Shaders;

import Entities.Camera;
import Toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram{

    private static final String vertexFile = "vertexShader.txt";
    private static final String fragmentFile = "/Shaders/fragmentShader.txt";

    int location_transformationMatrix;
    int location_projectionMatrix;
    int location_viewMatrix;


    public StaticShader() {
        super(vertexFile, fragmentFile);
    }

    protected void bindAttributes() {
        super.bindAttribute("position", 0);
        super.bindAttribute("textureCoords", 1);

    }

    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocations("transformationMatrix");
        location_projectionMatrix = super.getUniformLocations("projectionMatrix");
        location_viewMatrix = super.getUniformLocations("viewMatrix");


    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix , matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix , matrix);
    }

    public void loadViewMatrix(Camera camera){
        super.loadMatrix(location_viewMatrix , Maths.createViewMatrix(camera));
    }
}
