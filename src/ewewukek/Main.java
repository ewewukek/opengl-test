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

    private long window;

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
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        /// http://www.glfw.org/docs/latest/window_guide.html#window_hints_values
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        System.out.println("Requesting OpenGL 3.2 core");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_FALSE);

        final int WIDTH = 1024;
        final int HEIGHT = 768;

        window = glfwCreateWindow(WIDTH, HEIGHT, WINDOW_TITLE, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (vidmode.width() - WIDTH) / 2,
            (vidmode.height() - HEIGHT) / 2
        );

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1); // v-sync

        glfwShowWindow(window);
    }

    private void run() {
        GL.createCapabilities(); // LWJGL's interoperation with GLFW's OpenGL context

        System.out.println("GL_VENDOR: "+glGetString(GL_VENDOR));
        System.out.println("GL_RENDERER: "+glGetString(GL_RENDERER));
        System.out.println("GL_VERSION: "+glGetString(GL_VERSION));
        System.out.println("GL_SHADING_LANGUAGE_VERSION: "+glGetString(GL_SHADING_LANGUAGE_VERSION));

        /// load stuff

        Shader shader = new Shader("textured");

        Texture texture = new Texture("test/test.png");
        Mesh mesh = new Mesh("test/test");

        /// draw params

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);

        glClearColor(0.125f, 0.125f, 0.125f, 0.0f);

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.use();
            texture.bind();
            mesh.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        shader.dispose();
        texture.dispose();
        mesh.dispose();
    }
}