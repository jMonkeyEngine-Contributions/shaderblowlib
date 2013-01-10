varying float fog_z;
uniform float m_Distance;

void main() {

#ifdef DO_REFRACT
gl_FragColor = vec4(1.0);
        float fogDistance = m_Distance;
        float depth = (fog_z - fogDistance) / fogDistance;
        depth = max(depth, 0.0);
        float fogFactor = exp2(-depth * depth);
        fogFactor = clamp(fogFactor, 0.05, 1.0);
gl_FragColor.rgb = mix(vec3(0.0), gl_FragColor.rgb, vec3(fogFactor));
#else
gl_FragColor = vec4(0.0);
#endif
}