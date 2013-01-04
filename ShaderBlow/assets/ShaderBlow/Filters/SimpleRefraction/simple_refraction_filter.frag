/*
GLSL conversion of Michael Horsch water demo
http://www.bonzaisoftware.com/wfs.html
Converted by Mars_999
8/20/2005
*/

uniform sampler2D m_Texture;
uniform sampler2D m_water_dudvmap;
uniform float m_distortionScale;
uniform float m_distortionMix;
uniform float m_texScale;

varying vec4 waterTex1; //moving texcoords
varying vec4 waterTex2; //moving texcoords
varying vec4 position; //for projection

void main()
{
 

     vec4 disdis = texture2D(m_water_dudvmap, vec2(waterTex2.xy * vec2(m_texScale)));
     vec4 fdist = texture2D(m_water_dudvmap, vec2(waterTex1.xy + disdis.xy*vec2(m_distortionMix)));
     fdist =normalize( fdist * 2.0 - 1.0)* vec4(m_distortionScale);


     
     vec4 projCoord = position / position.w;
     projCoord =(projCoord+1.0)*0.5 + fdist;
     projCoord = clamp(projCoord, 0.001, 0.999);

     vec4 refr = texture2D(m_Texture, vec2(projCoord));
 
    gl_FragColor = refr;
}
