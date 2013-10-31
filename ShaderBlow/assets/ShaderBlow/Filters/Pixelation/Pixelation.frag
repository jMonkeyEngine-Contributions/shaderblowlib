uniform sampler2D m_Texture; // 0 - final scene image

varying vec2 texCoord;

uniform float m_ResX;
uniform float m_ResY;

void main() 
{ 	
	vec2 uv = texCoord.xy;
 	vec2 coord = vec2(m_ResX * floor(uv.x / m_ResX),
                  	  m_ResY * floor(uv.y / m_ResY));

 	vec3 tc = texture2D(m_Texture, coord).rgb;

	gl_FragColor = vec4(tc, 1.0);
}