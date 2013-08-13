uniform sampler2D m_Texture;
uniform sampler2D m_Noise;
uniform sampler2D m_Mask;

uniform vec4 m_Color;
uniform float g_Time;

// Can be uniforms
float luminanceThreshold = 0.2;
float colorAmplification = 4.0;
float effectCoverage = 1.0;

varying vec2 texCoord;
 
void main() {
  vec4 finalColor;
  // Set effectCoverage to 1.0 for normal use.  
  if (texCoord.x < effectCoverage) {
    vec2 uv;           
    uv.x = 0.1*sin(g_Time*50.0);                                 
    uv.y = 0.1*cos(g_Time*50.0);                                 
    float m = texture2D(m_Mask, texCoord).r;
    vec3 n = texture2D(m_Noise, texCoord + uv).rgb;
    vec3 c = texture2D(m_Texture, texCoord + (n.xy*0.005)).rgb;
  
    float lum = dot(vec3(0.30, 0.59, 0.11), c);
    if (lum < luminanceThreshold) {
      c *= colorAmplification; 
    }
  
    finalColor.rgb = (c + (n*0.9)) * vec3(m_Color) * m;
   }
   else {
    finalColor = texture2D(m_Texture, texCoord);
   }

  gl_FragColor.rgb = finalColor.rgb;
  gl_FragColor.a = 1.0;
}