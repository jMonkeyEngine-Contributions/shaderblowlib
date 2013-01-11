uniform mat4 g_ViewMatrix;
uniform mat4 g_ProjectionMatrix;
// uniform mat4 g_WorldMatrix;

// uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;
// attribute vec3 inNormal;

#if defined(HAS_COLORMAP) || (defined(HAS_LIGHTMAP) && !defined(SEPARATE_TEXCOORD))
    #define NEED_TEXCOORD1
#endif

attribute vec2 inTexCoord;
attribute vec2 inTexCoord2;
attribute vec4 inColor;

varying vec2 texCoord1;
varying vec2 texCoord2;

varying vec4 vertColor;
// varying vec3 direction;

void main(){
    #ifdef NEED_TEXCOORD1
        texCoord1 = inTexCoord;
    #endif

    #ifdef SEPARATE_TEXCOORD
        texCoord2 = inTexCoord2;
    #endif

    #ifdef HAS_VERTEXCOLOR
        vertColor = inColor;
    #endif

    // set w coordinate to 0
    vec4 pos = vec4(inPosition, 0.0);

    // compute rotation only for view matrix
    pos = g_ViewMatrix * pos;

    // now find projection
    pos.w = 1.0;
    gl_Position = g_ProjectionMatrix * pos;

 //   vec4 normal = vec4(inNormal* vec4(1.0), 0.0);
 //   direction = (g_WorldMatrix * normal).xyz;


  //  gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}