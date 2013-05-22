#import "Common/ShaderLib/MultiSample.glsllib"

uniform float g_Aspect;
uniform vec2 g_Resolution;

uniform COLORTEXTURE m_Texture;
uniform vec3 m_CircleCenter;
uniform float m_CircleRadius;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec3 color = getColor(m_Texture, texCoord).rgb;
    
    float posX = clamp(m_CircleCenter.x / g_Resolution.x, 0.0, 1.0) * g_Aspect;
    float posY = clamp(m_CircleCenter.y / g_Resolution.y, 0.0, 1.0);
    
	float d = distance(vec2(posX, posY), vec2(texCoord.x * g_Aspect, texCoord.y));
	
	color.rgb *= step(d, m_CircleRadius);

    fragColor.rgb = color;
    fragColor.a = 1.0;
}