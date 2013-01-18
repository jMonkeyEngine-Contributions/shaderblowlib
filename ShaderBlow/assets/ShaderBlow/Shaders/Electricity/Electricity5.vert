// author: cvlad

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrixInverse;
attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

varying vec2 uv;
uniform float m_width;

#ifdef FOG
    varying float fog_z;
#endif



void main() { 
    vec4 vertex = vec4(inPosition, 1.0) + vec4(inNormal * vec3(m_width), 0.0);
    gl_Position = g_WorldViewProjectionMatrix * vertex;
    uv = inTexCoord;

    #ifdef FOG
        fog_z = gl_Position.z;
    #endif

}