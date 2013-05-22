uniform float g_Aspect;
uniform vec2 g_Resolution;

uniform sampler2D m_Texture;
uniform vec3 m_CircleCenter;
uniform float m_CircleRadius;

varying vec2 texCoord;

void main() {
    vec3 color = texture2D(m_Texture, texCoord).rgb;
    
    float posX = clamp(m_CircleCenter.x / g_Resolution.x, 0.0, 1.0) * g_Aspect;
    float posY = clamp(m_CircleCenter.y / g_Resolution.y, 0.0, 1.0);
    
	float d = distance(vec2(posX, posY), vec2(texCoord.x * g_Aspect, texCoord.y));
	
	color.rgb *= step(d, m_CircleRadius);

    gl_FragColor.rgb = color;
    gl_FragColor.a = 1.0;
}