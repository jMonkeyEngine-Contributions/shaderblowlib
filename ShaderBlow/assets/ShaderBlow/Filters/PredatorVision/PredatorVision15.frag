#import "Common/ShaderLib/MultiSample.glsllib"

in vec2 texCoord;
out vec4 fragColor;

uniform COLORTEXTURE m_Texture;
uniform float m_ScreenWidth;

void main() 
{ 
  vec2 uv = texCoord.xy;
  
  vec3 tc = vec3(1.0, 0.0, 0.0);
  if (uv.x < (m_ScreenWidth-0.005)) {
    vec3 pixcol = texture2D(m_Texture, uv).rgb;
    vec3 colors[3];
    colors[0] = vec3(0.,0.,1.);
    colors[1] = vec3(1.,1.,0.);
    colors[2] = vec3(1.,0.,0.);
    float lum = dot(vec3(0.30, 0.59, 0.11), pixcol.rgb);
    int ix = (lum < 0.5)? 0:1;
    tc = mix(colors[ix],colors[ix+1],(lum-float(ix)*0.5)/0.5);
  }
  else if (uv.x>=(m_ScreenWidth+0.005)) {
    tc = texture2D(m_Texture, uv).rgb;
  }
    fragColor = vec4(tc, 1.0);
}