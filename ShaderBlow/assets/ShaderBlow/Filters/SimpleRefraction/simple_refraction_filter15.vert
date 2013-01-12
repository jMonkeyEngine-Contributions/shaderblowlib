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
uniform float g_Time;

in vec4 inPosition;
in vec2 inTexCoord;
in vec3 inTangent;
in vec3 inNormal;

out vec2 waterTex1;
out vec2 waterTex2;
out vec4 position;

#ifdef REFRACT_OBJECTS
out vec2 texCoord;
#endif

void main(void)
{

 //  vec4 viewSpacePos=g_WorldViewMatrix*inPosition;
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
