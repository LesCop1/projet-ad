package fr.bcecb.resources;

import com.google.gson.Gson;
import fr.bcecb.util.Resources;
import fr.bcecb.util.ShaderDescriptor;
import fr.bcecb.util.Shaders;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL45.*;

public class Shader extends LWJGLResource {
    private static final Gson GSON = new Gson();

    public Shader() {
    }

    private int getUniformLocation(String uniformName) {
        return glGetUniformLocation(getLWJGLId(), uniformName);
    }

    public void uniformVec4(String name, Vector4f vector) {
        uniformVec4(getUniformLocation(name), vector);
    }

    private void uniformVec4(int location, Vector4f vector) {
        glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    public void uniformFloat(String name, float value) {
        uniformFloat(getUniformLocation(name), value);
    }

    private void uniformFloat(int location, float value) {
        glUniform1f(location, value);
    }

    public void uniformMat4(String name, Matrix4f matrix) {
        uniformMat4(getUniformLocation(name), matrix);
    }

    private void uniformMat4(int location, Matrix4f matrix) {
        float[] matrixBuffer = new float[16];
        matrix.get(matrixBuffer);
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    @Override
    public void bind() {
        glUseProgram(getLWJGLId());
    }

    @Override
    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public int create(InputStream inputStream) throws IOException {
        return Shaders.compileProgram(GSON.fromJson(Resources.readResource(inputStream), ShaderDescriptor.class));
    }

    @Override
    public void dispose() {
        glDeleteProgram(getLWJGLId());
    }
}