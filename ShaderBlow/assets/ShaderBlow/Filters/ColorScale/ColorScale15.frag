#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
uniform vec4 m_FilterColor;
uniform float m_ColorDensity;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 color = getColor(m_Texture, texCoord).rgb;
    vec4 originalColor = vec4(color, 1.0);
    float colorFactor = clamp(m_ColorDensity, 0.0, 1.0);
    
    fragColor = mix(originalColor, m_FilterColor, colorFactor);
}