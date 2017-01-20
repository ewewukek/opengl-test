package ewewukek;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import ewewukek.gl.*;

public class Main {

    public static final String WINDOW_TITLE = "engine-test";
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;

    private long window;

    int mouseX;
    int mouseY;
    boolean mouseL;
    boolean mouseR;

    Matrix4f projectionMatrix = new Matrix4f();
    Matrix4f viewMatrix = new Matrix4f();
    Matrix3f normalMatrix = new Matrix3f();

    boolean rotate_mesh = false;
    float rotation_pitch = 0.0f;
    float rotation_yaw = 0.0f;
    float scale = 1.0f;

    float parallax_multiplier = 1.0f;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        System.out.println("LWJGL "+Version.getVersion());

        try {
            create_window();
            run();

            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void create_window() {
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        GLFWErrorCallback.createPrint(System.err).set();

        /// http://www.glfw.org/docs/latest/window_guide.html#window_hints_values
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        System.out.println("Requesting OpenGL 3.2 core");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1); // v-sync

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (vidmode.width() - WINDOW_WIDTH) / 2,
            (vidmode.height() - WINDOW_HEIGHT) / 2
        );

        glfwShowWindow(window);

        GL.createCapabilities(); // LWJGL's interoperation with GLFW's OpenGL context

        resize(WINDOW_WIDTH, WINDOW_HEIGHT);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            resize(width, height);
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS )
                rotate_mesh = !rotate_mesh;
            if ( key == GLFW_KEY_ENTER && action == GLFW_PRESS ) {
                rotation_pitch = 0.0f;
                rotation_yaw = 0.0f;
                scale = 1.0f;
            }
            if ( key == GLFW_KEY_P && action == GLFW_PRESS )
                parallax_multiplier = 1.0f - parallax_multiplier;
        });

        glfwSetCursorPosCallback(window, (window, x, y) -> {
            if (mouseL) {
                rotation_pitch += (float)(y - mouseY) * 0.01f;
                rotation_yaw += (float)(x - mouseX) * 0.01f;
            }
            mouseX = (int)x;
            mouseY = (int)y;
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if ( button == 0 ) mouseL = (action == 1);
            if ( button == 1 ) mouseR = (action == 1);
        });

        glfwSetScrollCallback(window, (window, xoff, yoff) -> {
            if (yoff < 0) {
                scale /= 1.1f;
            } else if (yoff > 0) {
                scale *= 1.1f;
            }
        });
    }

    private void resize(int width, int height) {
        final float ratio = (float)width/height;
        final float fovy = (float)Math.toRadians(70);
        projectionMatrix.setPerspective(fovy, ratio, 0.01F, 100.0F);
        glViewport(0, 0, width, height);
    }

    private void run() {
        System.out.println("GL_VENDOR: "+glGetString(GL_VENDOR));
        System.out.println("GL_RENDERER: "+glGetString(GL_RENDERER));
        System.out.println("GL_VERSION: "+glGetString(GL_VERSION));
        System.out.println("GL_SHADING_LANGUAGE_VERSION: "+glGetString(GL_SHADING_LANGUAGE_VERSION));

        /// load stuff

        // Shader shader = new Shader("res/shaders/parallax");

        Shader shader = new Shader("res/shaders/bump");

        System.out.println("uniforms:");
        int count = shader.getUniformCount();
        for (int i=0; i!=count; ++i) {
            System.out.println(shader.getUniform(i));
        }

        Model model = new Model();

        // model.addMesh("default", new Mesh("res/meshes/quad"))
            // .setShader(shader)
            // .setTexture("diffuseMap", new Texture("res/textures/rockwall/rockwall.png"))
            // .setTexture("normalMap", new Texture("res/textures/rockwall/rockwall_normal.png"))
            // .setTexture("heightMap", new Texture("res/textures/rockwall/rockwall_height.png"));

        model.addMesh("default", new Mesh("res/meshes/cube", Mesh.CALCULATE_TBN))
            .setShader(shader)
            .setTexture("diffuseMap", new Texture("res/textures/test/test.png"))
            .setTexture("normalMap", new Texture("res/textures/test/test_n.png"))
            .setTexture("specularMap", new Texture("res/textures/test/test_s.png"));

        shader.use();
        shader.setUniform("lightdir", 0, 0, 1);

        /// axes

        // Shader shader_axes = new Shader("res/shaders/colored");
        // Model model_axes = new Model();
        // model_axes.addMesh("default", new Mesh("res/meshes/axes")).setShader(shader_axes);

        /// draw params

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CCW);

        glEnable(GL_DEPTH_TEST);

        glClearColor(0.125f, 0.125f, 0.125f, 0.0f);

        FloatBuffer fb = BufferUtils.createFloatBuffer(16);

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            viewMatrix.identity();

            viewMatrix.translate(0, 0, -3f);

            viewMatrix.rotateY(rotation_yaw);
            viewMatrix.rotateX(rotation_pitch);

            viewMatrix.scale(scale, scale, scale);

            normalMatrix.identity();

            normalMatrix.rotateY(rotation_yaw);
            normalMatrix.rotateX(rotation_pitch);

            shader.use();

            shader.setUniform("projectionMatrix", projectionMatrix);
            shader.setUniform("viewMatrix", viewMatrix);
            shader.setUniform("normalMatrix", normalMatrix);

            // shader.setUniform("parallax_multiplier", parallax_multiplier);

            if (mouseR) shader.setUniform("lightdir", (mouseX - 512) / 384.0f, (384 - mouseY) / 384.0f, 1);

            if (rotate_mesh) {
                rotation_pitch += -0.01f;
                rotation_yaw += 0.0075f;
            }

            model.draw();

            // viewMatrix.identity();
            // viewMatrix.translate(0,0, -3f);
            // viewMatrix.rotateY(rotation_yaw);
            // viewMatrix.rotateX(rotation_pitch);
            // viewMatrix.scale(0.5f, 0.5f, 0.5f);

            // shader_axes.use();

            // shader_axes.setUniform("projectionMatrix", projectionMatrix);
            // shader_axes.setUniform("viewMatrix", viewMatrix);

            // model_axes.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}