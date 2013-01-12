#import "Common/ShaderLib/MultiSample.glsllib"

/*
GLSL conversion of Michael Horsch water demo
http://www.bonzaisoftware.com/wfs.html
Converted by Mars_999
8/20/2005
*/

uniform COLORTEXTURE m_Texture;

#ifdef REFRACT_OBJECTS
uniform COLORTEXTURE m_GrayTex;
in vec2 texCoord;
#endif

uniform sampler2D m_water_dudvmap;
uniform float m_distortionScale;
uniform float m_distortionMix;
uniform float m_texScale;

in vec2 waterTex1; //moving texcoords
in vec2 waterTex2; //moving texcoords
in vec4 position; //for projection

out vec4 fragColor;

void main()
{
 
#ifdef REFRACT_OBJECTS
    vec4 grayT = texture2D(m_GrayTex, texCoord.xy);
#endif

float distortion; 


#ifdef REFRACT_OBJECTS
    #ifdef REFRACT_FALOFF
    distortion = m_distortionScale * grayT.r;
    #else
    distortion = m_distortionScale*grayT.g;
    #endif
#else
    distortion = m_distortionScale;
#endif


     vec3 disdis = texture2D(m_water_dudvmap, vec2(waterTex2.xy * vec2(m_texScale))).xyz;
     vec3 fdist = texture2D(m_water_dudvmap, vec2(waterTex1.xy + disdis.xy*vec2(m_distortionMix))).xyz;
     fdist =normalize( fdist * vec3(2.0) - vec3(1.0))* vec3(distortion);

     vec2 projCoord = position.xy / vec2(position.w);
     projCoord =(projCoord+1.0)*0.5 + fdist.xy;
     projCoord = clamp(projCoord, 0.001, 0.999);

     vec4 refr = texture2D(m_Texture, vec2(projCoord));


#if defined(REFRACT_OBJECTS)  && defined(REFRACT_DETAIL)
     vec3 disdis2 = texture2D(m_water_dudvmap, vec2(waterTex2.xy * vec2(m_texScale) * vec2(10.0))).xyz;
     vec3 fdist2 = texture2D(m_water_dudvmap, vec2(waterTex1.xy + disdis2.xy*vec2(m_distortionMix))).xyz;
     fdist2 =normalize( fdist2 * vec3(2.0) - vec3(1.0))* vec3(distortion * 0.3);

     vec2 projCoord2 = position.xy / vec2(position.w);
     projCoord2 =(projCoord2+1.0)*0.5 + fdist2.xy;
     projCoord2 = clamp(projCoord2, 0.001, 0.999);

     vec4 refr2 = texture2D(m_Texture, vec2(projCoord2));


     vec4 refr3;
     refr3.rgb = mix(refr2.rgb, refr.rgb, grayT.r);

    fragColor.rgb = refr3.rgb;
    fragColor.a = refr;

#else
    fragColor = refr;
#endif

}
