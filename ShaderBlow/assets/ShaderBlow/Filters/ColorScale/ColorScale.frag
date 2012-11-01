uniform sampler2D m_Texture;
uniform vec4 m_FilterColor;
uniform float m_ColorDensity;

varying vec2 texCoord;
 
void main() {
    vec3 color = texture2D(m_Texture, texCoord).rgb;
    vec4 originalColor = vec4(color, 1.0);
    float colorFactor = clamp(m_ColorDensity, 0.0, 1.0);
    
    gl_FragColor = mix(originalColor, m_FilterColor, colorFactor);
}