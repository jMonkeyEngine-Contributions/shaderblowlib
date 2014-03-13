/*
GLSL conversion of Michael Horsch water demo
http://www.bonzaisoftware.com/wfs.html
Converted by Mars_999
8/20/2005
*/

uniform float m_timeFlow;

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrix;
uniform mat3 g_NormalMatrix;
uniform float g_Time;

attribute vec4 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inTangent;
attribute vec3 inNormal;

varying vec2 waterTex1;
varying vec2 waterTex2;
varying vec4 position;

#ifdef REFRACT_OBJECTS
varying vec2 texCoord;
#endif

void main(void)
{

//   vec4 viewSpacePos=g_WorldViewMatrix*inPosition;
   vec3 wvNormal  = normalize(g_NormalMatrix * inNormal);
   vec3 wvTangent = normalize(g_NormalMatrix * inTangent);
   vec3 wvBinormal = cross(wvNormal, wvTangent);
   mat3 tbnMat = mat3(wvTangent, wvBinormal, wvNormal);

    float t1 = -g_Time*m_timeFlow;
    float t2 = g_Time*m_timeFlow;

    waterTex1 = inTexCoord + vec2(t1);
    waterTex2 = inTexCoord + vec2(t2);

#ifdef REFRACT_OBJECTS
    texCoord = inTexCoord;
#endif

    position = g_WorldViewProjectionMatrix * inPosition;
    gl_Position = position;
}
