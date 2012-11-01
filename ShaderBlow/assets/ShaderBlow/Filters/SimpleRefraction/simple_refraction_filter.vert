/*
GLSL conversion of Michael Horsch water demo
http://www.bonzaisoftware.com/wfs.html
Converted by Mars_999
8/20/2005
*/

uniform float m_time;

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrix;
uniform mat3 g_NormalMatrix;

attribute vec4 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inTangent;
attribute vec3 inNormal;

varying vec4 waterTex1;
varying vec4 waterTex2;
varying vec4 position;



void main(void)
{

   vec4 viewSpacePos=g_WorldViewMatrix*inPosition;
   vec3 wvNormal  = normalize(g_NormalMatrix * inNormal);
   vec3 wvTangent = normalize(g_NormalMatrix * inTangent);
   vec3 wvBinormal = cross(wvNormal, wvTangent);
   mat3 tbnMat = mat3(wvTangent, wvBinormal, wvNormal);

    vec4 t1 = vec4(0.0, -m_time, 0.0,0.0);
    vec4 t2 = vec4(0.0, m_time, 0.0,0.0);

    waterTex1 =vec4(inTexCoord,0.0,0.0) + t1;
    waterTex2 =vec4(inTexCoord ,0.0,0.0)+ t2;

    position = g_WorldViewProjectionMatrix * inPosition;
    gl_Position = position;
}
