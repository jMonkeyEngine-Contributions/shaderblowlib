// author: cvlad

varying vec2 uv;
varying float viewDir;

uniform sampler2D m_noise;
uniform sampler2D m_ramp;
uniform float m_speed;
uniform float m_fallOff;
uniform vec4 m_outlineColor;
uniform float m_outlineColorFallOff;
uniform float m_noiseAmount;

uniform float g_Time;


#ifdef FOG
    varying float fog_z;
    float fogFactor;
    uniform float m_Fog;
#endif


void main() {
    vec2 UV = uv;
    UV.y *= 3.0;
    UV.x *= 10.0;
    vec4 noiseColor1 = texture2D(m_noise, UV + vec2(g_Time, g_Time * 2.0)*vec2(m_speed));
    vec4 noiseColor2 = texture2D(m_noise, UV - vec2(g_Time * 2.0, g_Time)*vec2(m_speed));
    float x = pow(viewDir, m_fallOff) * (dot(noiseColor1, noiseColor2)) * m_noiseAmount;
    vec4 col = texture2D(m_ramp, vec2(x, x*0.5));
    vec4 outline = m_outlineColor;
    outline.a *= pow(viewDir, m_outlineColorFallOff);
    gl_FragColor.rgb = col.rgb + outline.rgb;
    float alpha = min(min(outline.a, col.a), 1.0);

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
