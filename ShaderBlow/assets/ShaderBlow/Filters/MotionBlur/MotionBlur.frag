uniform sampler2D m_DepthTexture;
uniform sampler2D m_Texture;

uniform mat4 m_CurrentToPreviousMat;

uniform float m_Strength;
uniform int m_BlurSamples;

varying vec2 texCoord;


/** Author: Cord Rehn - jordansg57@gmail.com */
void main() {
  // determine current world pos
  float z = texture2D(m_DepthTexture, texCoord).r;
  vec3 currentPos = vec3(texCoord * 2.0 - 1.0, z * 2.0 -1.0);
  
  
  // determine previous world pos
  vec4 previousPos = m_CurrentToPreviousMat * vec4(currentPos, 1.0);
  previousPos.xy /= previousPos.w;
  
  
  // the blur vector, calculated as the difference between the
  // current and previous world positions multiplied by a scalar
  vec2 blurVector = m_Strength * vec2(currentPos.xy - previousPos.xy);


  // sample over the blurVector to accumulate the final color
  gl_FragColor = texture(m_Texture, texCoord);
   
  for (int i = 1; i < m_BlurSamples; ++i) {
    vec2 offset = blurVector * (float(i) / float(m_BlurSamples - 1) - 0.5); // centered over blurVector
    gl_FragColor += texture(m_Texture, texCoord + offset);
  }
 
  gl_FragColor /= float(m_BlurSamples);
}
