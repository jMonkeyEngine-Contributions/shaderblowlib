#import "Common/ShaderLib/Skinning.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform float g_Time;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
varying vec2 texCoordAni;
 
   // if these are passed as ints, then it doesn't work for some reason
   uniform int m_numTilesU;
   uniform int m_numTilesV;
   uniform int m_Speed; 

#ifdef FOG
    varying float fog_z;
#endif

#if defined(FOG_SKY)
    varying vec3 I;
    uniform vec3 g_CameraPosition;
    uniform mat4 g_WorldMatrix;
#endif 

void main(){

    vec4 pos = vec4(inPosition, 1.0);

    #ifdef NUM_BONES
      Skinning_Compute(pos);
    #endif 

   gl_Position = g_WorldViewProjectionMatrix * pos;
   texCoordAni = inTexCoord;

	int iNumTilesU = int(m_numTilesU);
	int iNumTilesV = int(m_numTilesV);

	int numTilesTotal = iNumTilesU * iNumTilesV;
	int selectedTile = 0;
	

        selectedTile += int(g_Time*float(m_Speed));

	// the "1 - " bit is because otherwise it goes from right to left
	texCoordAni.x = (float((texCoordAni.x + mod(float(selectedTile),  float(iNumTilesU))) / float(iNumTilesU))); ///selectedTile;
        texCoordAni.y = 1.0 - (texCoordAni.y + float(selectedTile / iNumTilesU)) / float(iNumTilesV); ///selectedTile;





// if (index = 8) index = 3;

//texCoordAni.x = texCoordAni.x / numTilesTotal + float(index) / numTilesTotal;



#if defined(FOG_SKY)
       vec3 worldPos = (g_WorldMatrix * pos).xyz;
       I = normalize( g_CameraPosition -  worldPos  ).xyz;
#endif

#ifdef FOG
    fog_z = gl_Position.z;
#endif

}

