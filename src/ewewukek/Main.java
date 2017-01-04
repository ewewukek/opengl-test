package ewewukek;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Matrix4f;

import ewewukek.gl.*;

public class Main {

    public static final String WINDOW_TITLE = "engine-test";
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;

    private long window;

    int mouseX;
    int mouseY;

    Matrix4f projectionMatrix = new Matrix4f();
    Matrix4f viewMatrix = new Matrix4f();

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
        });

        glfwSetCursorPosCallback(window, (window, x, y) -> {
            mouseX = (int)x;
            mouseY = (int)y;
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
        });

        glfwSetScrollCallback(window, (window, xoff, yoff) -> {
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

        Shader shader = new Shader("textured");

        Texture test_tex = new Texture("test/test.png");
        Mesh test_mesh = new Mesh("test/test");

        // Texture head_tex = new Texture("archer/head.png");
        // Mesh head_mesh = new Mesh("archer/head");
        // Texture upper_tex = new Texture("archer/upper.png");
        // Mesh upper_mesh = new Mesh("archer/upper");
        // Texture lower_tex = new Texture("archer/lower.png");
        // Mesh lower_mesh = new Mesh("archer/lower");
        // Texture bow_tex = new Texture("archer/bow.png");
        // Mesh bow_mesh = new Mesh("archer/bow");

        /// draw params

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CCW);

        glEnable(GL_DEPTH_TEST);

        glClearColor(0.125f, 0.125f, 0.125f, 0.0f);

        float pitch = 0;
        float yaw = 0;

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            viewMatrix.identity();

            // viewMatrix.translate(0, -0.85F, -2F);
            // viewMatrix.scale(0.015F, 0.015F, 0.015F);

            viewMatrix.translate(0, 0, -3f);
            viewMatrix.rotateY(yaw);
            viewMatrix.rotateX(pitch);

            // viewMatrix.rotateY(-0.35F);
            // viewMatrix.rotateX(0.25F);

            shader.use();
            shader.setProjectionMatrix(projectionMatrix);
            shader.setViewMatrix(viewMatrix);

            pitch += -0.01f;
            yaw += 0.0075f;

            test_tex.bind(0);
            test_mesh.draw();

            // head_tex.bind(0);
            // head_mesh.draw();
            // upper_tex.bind(0);
            // upper_mesh.draw();
            // lower_tex.bind(0);
            // lower_mesh.draw();
            // bow_tex.bind(0);
            // bow_mesh.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        shader.dispose();

        // test_tex.dispose();
        // test_mesh.dispose();

        // head_tex.dispose();
        // head_mesh.dispose();
        // upper_tex.dispose();
        // upper_mesh.dispose();
        // lower_tex.dispose();
        // lower_mesh.dispose();
        // bow_tex.dispose();
        // bow_mesh.dispose();
    }
}