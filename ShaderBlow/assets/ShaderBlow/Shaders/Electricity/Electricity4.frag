// author: cvlad

varying vec2 uv;
varying float viewDir;

uniform sampler2D m_noise;
uniform float m_speed;
uniform float m_noiseAmount;
uniform vec4 m_color;
uniform float m_thickness;
uniform float m_fallOff;

uniform float g_Time;


#ifdef FOG
    varying float fog_z;
    float fogFactor;
    uniform float m_Fog;
#endif


void main() {
    vec4 noiseColor1 = texture2D(m_noise, uv * vec2(3.0) + vec2(g_Time, g_Time * 2.0) * vec2(m_speed));
    vec4 noiseColor2 = texture2D(m_noise, uv * vec2(3.0) - vec2(g_Time * 2.0, g_Time) * vec2(m_speed));
    float x = dot(noiseColor1, noiseColor2);
    vec4 noiseColor3 = texture2D(m_noise, uv*2.0 + vec2(x,x) * vec2(m_noiseAmount) + vec2(g_Time, g_Time) * vec2(m_speed));
    vec4 noiseColor4 = texture2D(m_noise, uv*2.0 + vec2(x,x) * vec2(m_noiseAmount) - vec2(g_Time, g_Time * 1.5)*vec2(m_speed));
    vec3 r = (noiseColor1.xyz + noiseColor2.xyz + noiseColor3.xyz + noiseColor4.xyz) / vec3(4.0);
    float rr = (r.g + r.b) / 2.0;
    rr = max(rr - 0.03, 0.0);
    rr = pow(rr + 0.4, 30.0);
    rr = step(0.5 - m_thickness, rr) * step(rr, 0.5 + m_thickness);
    gl_FragColor.rgb = m_color.rgb * vec3(rr);
    float alpha = min(m_color.a * rr * min(pow(viewDir * 3.0, m_fallOff), 1.0), 1.0);

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
