// author: cvlad

varying vec2 uv;
varying vec2 scaledUV;
varying float viewDir;

uniform sampler2D m_noise;
uniform float m_speed;
uniform float m_fallOff;
uniform float m_noiseAmount;
uniform vec4 m_color;

uniform float g_Time;


#ifdef FOG
    varying float fog_z;
    float fogFactor;
    uniform float m_Fog;
#endif


void main() {
    vec4 noiseColor1 = texture2D(m_noise, uv + vec2(g_Time, g_Time * 2.0) * vec2(m_speed));
    vec4 noiseColor2 = texture2D(m_noise, uv - vec2(g_Time * 2.0, g_Time) * vec2(m_speed));
    float x = dot(noiseColor1.rgb, noiseColor2.rgb);
    vec4 noiseColor3 = texture2D(m_noise, uv + vec2(g_Time, g_Time) * vec2(m_speed));
    vec4 noiseColor4 = texture2D(m_noise, uv - vec2(g_Time, g_Time * 1.5) * vec2(m_speed));
    float y = dot(noiseColor3.rgb, noiseColor4.rgb);
    vec2 sUV = fract(scaledUV + vec2(x,y) * vec2(m_noiseAmount));
    vec2 sUV2 = step(0.9, sUV);
    float v = max(sUV2.x,sUV2.y);
    gl_FragColor.rgb = m_color.rgb * vec3(v);

    float alpha = min(m_color.a * v * min(pow(viewDir * 3.0, m_fallOff), 1.0) , 1.0);

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
