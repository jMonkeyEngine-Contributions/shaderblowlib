#import "Common/ShaderLib/Skinning.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrix;
attribute vec3 inPosition;

// uniform int m_CollisionNum;
uniform vec3 m_Collisions[4];
varying float dists[4];

#ifdef HAS_COLORMAP
    attribute vec2 inTexCoord;
    varying vec2 texCoord1;
#endif

void main(){

    #ifdef HAS_COLORMAP
        texCoord1 = inTexCoord;
    #endif


        #ifdef WORK
	for (int i=0;i<4;i++) {
		dists[i]=distance(inPosition,m_Collisions[i]);
	}
        #endif


        vec4 pos = vec4(inPosition, 1.0);
        #ifdef NUM_BONES
          Skinning_Compute(pos);
        #endif

    gl_Position = g_WorldViewProjectionMatrix * pos;

}

