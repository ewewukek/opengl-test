package ewewukek.gl;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL42.*;

public class GLTypes {
    public static final HashMap<String, Integer> glslToIntMap = new HashMap<>();

    public static int toInt(String type) {
        Integer i = glslToIntMap.get(type);
        if (i == null) return 0;
        return i;
    }

    public static int elementType(int type) {
        switch(type) {
            case GL_FLOAT: return GL_FLOAT;
            case GL_FLOAT_VEC2: return GL_FLOAT;
            case GL_FLOAT_VEC3: return GL_FLOAT;
            case GL_FLOAT_VEC4: return GL_FLOAT;
            case GL_FLOAT_MAT2: return GL_FLOAT;
            case GL_FLOAT_MAT3: return GL_FLOAT;
            case GL_FLOAT_MAT4: return GL_FLOAT;
            case GL_FLOAT_MAT2x3: return GL_FLOAT;
            case GL_FLOAT_MAT2x4: return GL_FLOAT;
            case GL_FLOAT_MAT3x2: return GL_FLOAT;
            case GL_FLOAT_MAT3x4: return GL_FLOAT;
            case GL_FLOAT_MAT4x2: return GL_FLOAT;
            case GL_FLOAT_MAT4x3: return GL_FLOAT;
            case GL_UNSIGNED_INT: return GL_UNSIGNED_INT;
            case GL_UNSIGNED_INT_VEC2: return GL_UNSIGNED_INT;
            case GL_UNSIGNED_INT_VEC3: return GL_UNSIGNED_INT;
        }
        return 0;
    }

    public static int elementSize(int type) {
        switch(type) {
            case GL_FLOAT: return 1;
            case GL_FLOAT_VEC2: return 2;
            case GL_FLOAT_VEC3: return 3;
            case GL_FLOAT_VEC4: return 4;
            case GL_FLOAT_MAT2: return 4;
            case GL_FLOAT_MAT3: return 9;
            case GL_FLOAT_MAT4: return 16;
            case GL_FLOAT_MAT2x3: return 6;
            case GL_FLOAT_MAT2x4: return 8;
            case GL_FLOAT_MAT3x2: return 6;
            case GL_FLOAT_MAT3x4: return 12;
            case GL_FLOAT_MAT4x2: return 8;
            case GL_FLOAT_MAT4x3: return 12;
            case GL_UNSIGNED_INT: return 1;
            case GL_UNSIGNED_INT_VEC2: return 2;
            case GL_UNSIGNED_INT_VEC3: return 3;
        }
        return 0;
    }

    public static String toString(int type) {
        switch(type) {
            case GL_FLOAT: return "float";
            case GL_FLOAT_VEC2: return "vec2";
            case GL_FLOAT_VEC3: return "vec3";
            case GL_FLOAT_VEC4: return "vec4";
            case GL_DOUBLE: return "double";
            case GL_DOUBLE_VEC2: return "dvec2";
            case GL_DOUBLE_VEC3: return "dvec3";
            case GL_DOUBLE_VEC4: return "dvec4";
            case GL_INT: return "int";
            case GL_INT_VEC2: return "ivec2";
            case GL_INT_VEC3: return "ivec3";
            case GL_INT_VEC4: return "ivec4";
            case GL_UNSIGNED_INT: return "unsigned int";
            case GL_UNSIGNED_INT_VEC2: return "uvec2";
            case GL_UNSIGNED_INT_VEC3: return "uvec3";
            case GL_UNSIGNED_INT_VEC4: return "uvec4";
            case GL_BOOL: return "bool";
            case GL_BOOL_VEC2: return "bvec2";
            case GL_BOOL_VEC3: return "bvec3";
            case GL_BOOL_VEC4: return "bvec4";
            case GL_FLOAT_MAT2: return "mat2";
            case GL_FLOAT_MAT3: return "mat3";
            case GL_FLOAT_MAT4: return "mat4";
            case GL_FLOAT_MAT2x3: return "mat2x3";
            case GL_FLOAT_MAT2x4: return "mat2x4";
            case GL_FLOAT_MAT3x2: return "mat3x2";
            case GL_FLOAT_MAT3x4: return "mat3x4";
            case GL_FLOAT_MAT4x2: return "mat4x2";
            case GL_FLOAT_MAT4x3: return "mat4x3";
            case GL_DOUBLE_MAT2: return "dmat2";
            case GL_DOUBLE_MAT3: return "dmat3";
            case GL_DOUBLE_MAT4: return "dmat4";
            case GL_DOUBLE_MAT2x3: return "dmat2x3";
            case GL_DOUBLE_MAT2x4: return "dmat2x4";
            case GL_DOUBLE_MAT3x2: return "dmat3x2";
            case GL_DOUBLE_MAT3x4: return "dmat3x4";
            case GL_DOUBLE_MAT4x2: return "dmat4x2";
            case GL_DOUBLE_MAT4x3: return "dmat4x3";
            case GL_SAMPLER_1D: return "sampler1D";
            case GL_SAMPLER_2D: return "sampler2D";
            case GL_SAMPLER_3D: return "sampler3D";
            case GL_SAMPLER_CUBE: return "samplerCube";
            case GL_SAMPLER_1D_SHADOW: return "sampler1DShadow";
            case GL_SAMPLER_2D_SHADOW: return "sampler2DShadow";
            case GL_SAMPLER_1D_ARRAY: return "sampler1DArray";
            case GL_SAMPLER_2D_ARRAY: return "sampler2DArray";
            case GL_SAMPLER_1D_ARRAY_SHADOW: return "sampler1DArrayShadow";
            case GL_SAMPLER_2D_ARRAY_SHADOW: return "sampler2DArrayShadow";
            case GL_SAMPLER_2D_MULTISAMPLE: return "sampler2DMS";
            case GL_SAMPLER_2D_MULTISAMPLE_ARRAY: return "sampler2DMSArray";
            case GL_SAMPLER_CUBE_SHADOW: return "samplerCubeShadow";
            case GL_SAMPLER_BUFFER: return "samplerBuffer";
            case GL_SAMPLER_2D_RECT: return "sampler2DRect";
            case GL_SAMPLER_2D_RECT_SHADOW: return "sampler2DRectShadow";
            case GL_INT_SAMPLER_1D: return "isampler1D";
            case GL_INT_SAMPLER_2D: return "isampler2D";
            case GL_INT_SAMPLER_3D: return "isampler3D";
            case GL_INT_SAMPLER_CUBE: return "isamplerCube";
            case GL_INT_SAMPLER_1D_ARRAY: return "isampler1DArray";
            case GL_INT_SAMPLER_2D_ARRAY: return "isampler2DArray";
            case GL_INT_SAMPLER_2D_MULTISAMPLE: return "isampler2DMS";
            case GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY: return "isampler2DMSArray";
            case GL_INT_SAMPLER_BUFFER: return "isamplerBuffer";
            case GL_INT_SAMPLER_2D_RECT: return "isampler2DRect";
            case GL_UNSIGNED_INT_SAMPLER_1D: return "usampler1D";
            case GL_UNSIGNED_INT_SAMPLER_2D: return "usampler2D";
            case GL_UNSIGNED_INT_SAMPLER_3D: return "usampler3D";
            case GL_UNSIGNED_INT_SAMPLER_CUBE: return "usamplerCube";
            case GL_UNSIGNED_INT_SAMPLER_1D_ARRAY: return "usampler2DArray";
            case GL_UNSIGNED_INT_SAMPLER_2D_ARRAY: return "usampler2DArray";
            case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE: return "usampler2DMS";
            case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY: return "usampler2DMSArray";
            case GL_UNSIGNED_INT_SAMPLER_BUFFER: return "usamplerBuffer";
            case GL_UNSIGNED_INT_SAMPLER_2D_RECT: return "usampler2DRect";
            case GL_IMAGE_1D: return "image1D";
            case GL_IMAGE_2D: return "image2D";
            case GL_IMAGE_3D: return "image3D";
            case GL_IMAGE_2D_RECT: return "image2DRect";
            case GL_IMAGE_CUBE: return "imageCube";
            case GL_IMAGE_BUFFER: return "imageBuffer";
            case GL_IMAGE_1D_ARRAY: return "image1DArray";
            case GL_IMAGE_2D_ARRAY: return "image2DArray";
            case GL_IMAGE_2D_MULTISAMPLE: return "image2DMS";
            case GL_IMAGE_2D_MULTISAMPLE_ARRAY: return "image2DMSArray";
            case GL_INT_IMAGE_1D: return "iimage1D";
            case GL_INT_IMAGE_2D: return "iimage2D";
            case GL_INT_IMAGE_3D: return "iimage3D";
            case GL_INT_IMAGE_2D_RECT: return "iimage2DRect";
            case GL_INT_IMAGE_CUBE: return "iimageCube";
            case GL_INT_IMAGE_BUFFER: return "iimageBuffer";
            case GL_INT_IMAGE_1D_ARRAY: return "iimage1DArray";
            case GL_INT_IMAGE_2D_ARRAY: return "iimage2DArray";
            case GL_INT_IMAGE_2D_MULTISAMPLE: return "iimage2DMS";
            case GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY: return "iimage2DMSArray";
            case GL_UNSIGNED_INT_IMAGE_1D: return "uimage1D";
            case GL_UNSIGNED_INT_IMAGE_2D: return "uimage2D";
            case GL_UNSIGNED_INT_IMAGE_3D: return "uimage3D";
            case GL_UNSIGNED_INT_IMAGE_2D_RECT: return "uimage2DRect";
            case GL_UNSIGNED_INT_IMAGE_CUBE: return "uimageCube";
            case GL_UNSIGNED_INT_IMAGE_BUFFER: return "uimageBuffer";
            case GL_UNSIGNED_INT_IMAGE_1D_ARRAY: return "uimage1DArray";
            case GL_UNSIGNED_INT_IMAGE_2D_ARRAY: return "uimage2DArray";
            case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE: return "uimage2DMS";
            case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY: return "uimage2DMSArray";
            case GL_UNSIGNED_INT_ATOMIC_COUNTER: return "atomic_uint";
        }
        return "[unknown]";
    }

    public static String primitiveTypeToString(int type) {
        switch (type) {
            case GL_POINTS: return "GL_POINTS";
            case GL_LINE_STRIP: return "GL_LINE_STRIP";
            case GL_LINE_LOOP: return "GL_LINE_LOOP";
            case GL_LINES: return "GL_LINES";
            case GL_TRIANGLE_STRIP: return "GL_TRIANGLE_STRIP";
            case GL_TRIANGLE_FAN: return "GL_TRIANGLE_FAN";
            case GL_TRIANGLES: return "GL_TRIANGLES";
        }
        return "[unknown]";
    }

    static {
        glslToIntMap.put("float", GL_FLOAT);
        glslToIntMap.put("vec2", GL_FLOAT_VEC2);
        glslToIntMap.put("vec3", GL_FLOAT_VEC3);
        glslToIntMap.put("vec4", GL_FLOAT_VEC4);
        glslToIntMap.put("double", GL_DOUBLE);
        glslToIntMap.put("dvec2", GL_DOUBLE_VEC2);
        glslToIntMap.put("dvec3", GL_DOUBLE_VEC3);
        glslToIntMap.put("dvec4", GL_DOUBLE_VEC4);
        glslToIntMap.put("int", GL_INT);
        glslToIntMap.put("ivec2", GL_INT_VEC2);
        glslToIntMap.put("ivec3", GL_INT_VEC3);
        glslToIntMap.put("ivec4", GL_INT_VEC4);
        glslToIntMap.put("unsigned int", GL_UNSIGNED_INT);
        glslToIntMap.put("uvec2", GL_UNSIGNED_INT_VEC2);
        glslToIntMap.put("uvec3", GL_UNSIGNED_INT_VEC3);
        glslToIntMap.put("uvec4", GL_UNSIGNED_INT_VEC4);
        glslToIntMap.put("bool", GL_BOOL);
        glslToIntMap.put("bvec2", GL_BOOL_VEC2);
        glslToIntMap.put("bvec3", GL_BOOL_VEC3);
        glslToIntMap.put("bvec4", GL_BOOL_VEC4);
        glslToIntMap.put("mat2", GL_FLOAT_MAT2);
        glslToIntMap.put("mat3", GL_FLOAT_MAT3);
        glslToIntMap.put("mat4", GL_FLOAT_MAT4);
        glslToIntMap.put("mat2x3", GL_FLOAT_MAT2x3);
        glslToIntMap.put("mat2x4", GL_FLOAT_MAT2x4);
        glslToIntMap.put("mat3x2", GL_FLOAT_MAT3x2);
        glslToIntMap.put("mat3x4", GL_FLOAT_MAT3x4);
        glslToIntMap.put("mat4x2", GL_FLOAT_MAT4x2);
        glslToIntMap.put("mat4x3", GL_FLOAT_MAT4x3);
        glslToIntMap.put("dmat2", GL_DOUBLE_MAT2);
        glslToIntMap.put("dmat3", GL_DOUBLE_MAT3);
        glslToIntMap.put("dmat4", GL_DOUBLE_MAT4);
        glslToIntMap.put("dmat2x3", GL_DOUBLE_MAT2x3);
        glslToIntMap.put("dmat2x4", GL_DOUBLE_MAT2x4);
        glslToIntMap.put("dmat3x2", GL_DOUBLE_MAT3x2);
        glslToIntMap.put("dmat3x4", GL_DOUBLE_MAT3x4);
        glslToIntMap.put("dmat4x2", GL_DOUBLE_MAT4x2);
        glslToIntMap.put("dmat4x3", GL_DOUBLE_MAT4x3);
        glslToIntMap.put("sampler1D", GL_SAMPLER_1D);
        glslToIntMap.put("sampler2D", GL_SAMPLER_2D);
        glslToIntMap.put("sampler3D", GL_SAMPLER_3D);
        glslToIntMap.put("samplerCube", GL_SAMPLER_CUBE);
        glslToIntMap.put("sampler1DShadow", GL_SAMPLER_1D_SHADOW);
        glslToIntMap.put("sampler2DShadow", GL_SAMPLER_2D_SHADOW);
        glslToIntMap.put("sampler1DArray", GL_SAMPLER_1D_ARRAY);
        glslToIntMap.put("sampler2DArray", GL_SAMPLER_2D_ARRAY);
        glslToIntMap.put("sampler1DArrayShadow", GL_SAMPLER_1D_ARRAY_SHADOW);
        glslToIntMap.put("sampler2DArrayShadow", GL_SAMPLER_2D_ARRAY_SHADOW);
        glslToIntMap.put("sampler2DMS", GL_SAMPLER_2D_MULTISAMPLE);
        glslToIntMap.put("sampler2DMSArray", GL_SAMPLER_2D_MULTISAMPLE_ARRAY);
        glslToIntMap.put("samplerCubeShadow", GL_SAMPLER_CUBE_SHADOW);
        glslToIntMap.put("samplerBuffer", GL_SAMPLER_BUFFER);
        glslToIntMap.put("sampler2DRect", GL_SAMPLER_2D_RECT);
        glslToIntMap.put("sampler2DRectShadow", GL_SAMPLER_2D_RECT_SHADOW);
        glslToIntMap.put("isampler1D", GL_INT_SAMPLER_1D);
        glslToIntMap.put("isampler2D", GL_INT_SAMPLER_2D);
        glslToIntMap.put("isampler3D", GL_INT_SAMPLER_3D);
        glslToIntMap.put("isamplerCube", GL_INT_SAMPLER_CUBE);
        glslToIntMap.put("isampler1DArray", GL_INT_SAMPLER_1D_ARRAY);
        glslToIntMap.put("isampler2DArray", GL_INT_SAMPLER_2D_ARRAY);
        glslToIntMap.put("isampler2DMS", GL_INT_SAMPLER_2D_MULTISAMPLE);
        glslToIntMap.put("isampler2DMSArray", GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY);
        glslToIntMap.put("isamplerBuffer", GL_INT_SAMPLER_BUFFER);
        glslToIntMap.put("isampler2DRect", GL_INT_SAMPLER_2D_RECT);
        glslToIntMap.put("usampler1D", GL_UNSIGNED_INT_SAMPLER_1D);
        glslToIntMap.put("usampler2D", GL_UNSIGNED_INT_SAMPLER_2D);
        glslToIntMap.put("usampler3D", GL_UNSIGNED_INT_SAMPLER_3D);
        glslToIntMap.put("usamplerCube", GL_UNSIGNED_INT_SAMPLER_CUBE);
        glslToIntMap.put("usampler2DArray", GL_UNSIGNED_INT_SAMPLER_1D_ARRAY);
        glslToIntMap.put("usampler2DArray", GL_UNSIGNED_INT_SAMPLER_2D_ARRAY);
        glslToIntMap.put("usampler2DMS", GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE);
        glslToIntMap.put("usampler2DMSArray", GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY);
        glslToIntMap.put("usamplerBuffer", GL_UNSIGNED_INT_SAMPLER_BUFFER);
        glslToIntMap.put("usampler2DRect", GL_UNSIGNED_INT_SAMPLER_2D_RECT);
        glslToIntMap.put("image1D", GL_IMAGE_1D);
        glslToIntMap.put("image2D", GL_IMAGE_2D);
        glslToIntMap.put("image3D", GL_IMAGE_3D);
        glslToIntMap.put("image2DRect", GL_IMAGE_2D_RECT);
        glslToIntMap.put("imageCube", GL_IMAGE_CUBE);
        glslToIntMap.put("imageBuffer", GL_IMAGE_BUFFER);
        glslToIntMap.put("image1DArray", GL_IMAGE_1D_ARRAY);
        glslToIntMap.put("image2DArray", GL_IMAGE_2D_ARRAY);
        glslToIntMap.put("image2DMS", GL_IMAGE_2D_MULTISAMPLE);
        glslToIntMap.put("image2DMSArray", GL_IMAGE_2D_MULTISAMPLE_ARRAY);
        glslToIntMap.put("iimage1D", GL_INT_IMAGE_1D);
        glslToIntMap.put("iimage2D", GL_INT_IMAGE_2D);
        glslToIntMap.put("iimage3D", GL_INT_IMAGE_3D);
        glslToIntMap.put("iimage2DRect", GL_INT_IMAGE_2D_RECT);
        glslToIntMap.put("iimageCube", GL_INT_IMAGE_CUBE);
        glslToIntMap.put("iimageBuffer", GL_INT_IMAGE_BUFFER);
        glslToIntMap.put("iimage1DArray", GL_INT_IMAGE_1D_ARRAY);
        glslToIntMap.put("iimage2DArray", GL_INT_IMAGE_2D_ARRAY);
        glslToIntMap.put("iimage2DMS", GL_INT_IMAGE_2D_MULTISAMPLE);
        glslToIntMap.put("iimage2DMSArray", GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY);
        glslToIntMap.put("uimage1D", GL_UNSIGNED_INT_IMAGE_1D);
        glslToIntMap.put("uimage2D", GL_UNSIGNED_INT_IMAGE_2D);
        glslToIntMap.put("uimage3D", GL_UNSIGNED_INT_IMAGE_3D);
        glslToIntMap.put("uimage2DRect", GL_UNSIGNED_INT_IMAGE_2D_RECT);
        glslToIntMap.put("uimageCube", GL_UNSIGNED_INT_IMAGE_CUBE);
        glslToIntMap.put("uimageBuffer", GL_UNSIGNED_INT_IMAGE_BUFFER);
        glslToIntMap.put("uimage1DArray", GL_UNSIGNED_INT_IMAGE_1D_ARRAY);
        glslToIntMap.put("uimage2DArray", GL_UNSIGNED_INT_IMAGE_2D_ARRAY);
        glslToIntMap.put("uimage2DMS", GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE);
        glslToIntMap.put("uimage2DMSArray", GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY);
        glslToIntMap.put("atomic_uint", GL_UNSIGNED_INT_ATOMIC_COUNTER);
    }
}