#import "Common/ShaderLib/MultiSample.glsllib"

in vec2 texCoord;
out vec4 fragColor;

uniform COLORTEXTURE m_Texture;

uniform float m_RandomFactor;
uniform float m_RandomScale;
vec2 v1 = vec2(92.0,80.0);
vec2 v2 = vec2(41.0,62.0);

float rand(vec2 co)
{
  return fract(sin(dot(co.xy ,v1)) + cos(dot(co.xy ,v2)) * m_RandomScale);
}

void main()
{ 
  vec2 rnd = vec2(rand(texCoord),rand(texCoord));  
  vec3 tc = texture2D(m_Texture, texCoord + rnd*m_RandomFactor).rgb;  
  fragColor = vec4(tc, 1.0);
}