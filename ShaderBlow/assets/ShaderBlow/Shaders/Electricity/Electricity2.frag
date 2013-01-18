// author: cvlad

varying vec2 uv;

uniform sampler2D m_noise;
uniform vec4 m_color;
uniform float m_noiseAmount;
uniform float m_speed;
uniform float m_lines;
uniform float m_fallOff;

uniform float g_Time;

varying float ypos;
varying float viewAngle;


#ifdef FOG
    varying float fog_z;
    float fogFactor;
    uniform float m_Fog;
#endif


void main() {
    vec2 uvOffset1 = vec2(g_Time,g_Time * 2.0) * vec2(m_speed);
    vec2 uvOffset2 = -vec2(g_Time,g_Time) * vec2(m_speed);
    vec4 noiseColor1 = texture2D(m_noise, uv + uvOffset1);
    vec4 noiseColor2 = texture2D(m_noise, uv + uvOffset2);
    float noise = (dot(noiseColor1, noiseColor2) - 1.0) * m_noiseAmount;
    float c = sin((ypos*m_lines + g_Time * m_speed + noise) * 100.0);
    vec4 col = vec4(c, c, c, c);
    noiseColor1 = texture2D(m_noise, uv * vec2(6.0) + uvOffset1);
    noiseColor2 = texture2D(m_noise, uv * vec2(6.0) + uvOffset2);
    col.a *= clamp(1.3 - max(noiseColor1.g, noiseColor2.g), 0.0, 1.0) * pow(viewAngle, m_fallOff) * 15.0;
    gl_FragColor.rgb =  col.rgb * m_color.rgb;
    float alpha = min(col.a * m_color.a, 1.0);

    if(alpha < 0.015){
        discard;
    }

    gl_FragColor.a = alpha;


    #ifdef FOG

        float fogDistance = m_Fog;
        float depth = (fog_z - fogDistance) / fogDistance;
        depth = max(depth, 0.0);
        fogFactor = exp2(-depth * depth);
        fogFactor = clamp(fogFactor, 0.05, 1.0);

        gl_FragColor.a = mix(0.0, gl_FragColor.a, fogFactor);

    #endif
}
