// author: cvlad

varying vec2 uv;

uniform sampler2D m_noise;
uniform float m_speed;
uniform float m_noiseAmount;
uniform vec4 m_color;
uniform float m_thickness;
uniform float m_layer;

uniform float g_Time;


#ifdef FOG
    varying float fog_z;
    float fogFactor;
    uniform float m_Fog;
#endif


void main() {
    vec2 UV = uv;
    
    vec4 noiseColor1 = texture2D(m_noise, uv * vec2(4.0) + vec2(g_Time, g_Time * 2.0) * vec2(0.5));
    vec4 noiseColor2 = texture2D(m_noise, uv * vec2(4.0) - vec2(g_Time * 3.0, -g_Time) * vec2(0.5));

    float noiseDotted = dot(noiseColor1,noiseColor2);

    float a = noiseDotted;

    float u = UV.x + UV.y;
    
    float s = sin(g_Time + u * noiseDotted);
    
    float o = g_Time + (cos(u + noiseDotted + m_layer + fract(g_Time) + s) * noiseDotted) * 0.1;

    vec2 t = step((0.8 - m_thickness + m_layer * m_thickness) * m_noiseAmount, fract(UV + vec2(o, o) )) ;

    gl_FragColor = vec4(m_color.r, m_color.g, m_color.b, m_color.a * t.r * t.g *(s + 0.5)*(0.9 - fract(u + g_Time * m_speed)));
    float alpha  = min (gl_FragColor.a, 1.0);

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