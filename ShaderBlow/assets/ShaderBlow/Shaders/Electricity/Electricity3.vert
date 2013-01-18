// author: cvlad

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrixInverse;
attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

varying vec2 uv;
varying vec2 scaledUV;
varying float viewDir;

uniform float m_width;
uniform vec2 m_texScale;

#ifdef FOG
    varying float fog_z;
#endif


void main() { 
    vec4 vertex = vec4(inPosition, 1.0) + vec4(inNormal * m_width, 0.0);
    gl_Position = g_WorldViewProjectionMatrix * vertex;
    scaledUV = inTexCoord * vec2(m_texScale);
    uv = inTexCoord;
    vec4 objectSpaceCamPos = g_WorldViewMatrixInverse * vec4(0.0, 0.0, 0.0, 1.0);
    viewDir = 1.0 - abs(dot(inNormal, normalize(objectSpaceCamPos.xyz - inPosition)));

    #ifdef FOG
        fog_z = gl_Position.z;
    #endif

}