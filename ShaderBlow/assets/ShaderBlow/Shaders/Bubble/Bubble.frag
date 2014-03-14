varying vec3 vNdotV;
varying vec2 vTexCoord;
varying vec3 vNormal;

varying vec4 vPosition;
varying vec3 vTransformedNormal;
uniform sampler2D m_ColorMap;

#ifdef SPECULARCOLOR
uniform vec4 m_SpecularColor;
uniform float m_Shininess;
#endif
uniform bool m_UseSpecularNormal;


void main() {
    
    vec4 specularLightWeighting = vec4(0.0, 0.0, 0.0, 0.0);
    #ifdef SPECULARCOLOR
    vec3 eyeDirection = normalize(-vPosition.xyz);
    vec3 reflectionDirection = reflect(vec3(-1.0) * vec3(0.0, 0.0, 1.0), normalize(vTransformedNormal));
    float specLightWeighting = pow(max(dot(reflectionDirection, eyeDirection), 0.0), m_Shininess);
    vec4 specColor = m_SpecularColor;
    
    if(m_UseSpecularNormal){
        specColor = vec4(vNormal, m_SpecularColor.a);
    }
    specularLightWeighting = specColor * vec4(specLightWeighting);
    #endif

    vec4 modulatedColor;

    vec4 soapRefColor = texture2D(m_ColorMap, vTexCoord);

    vec4 groundColor = vec4(0.5, 0.5, 0.5, 0.0);

    modulatedColor.rgb = clamp(vec3(2.0) * soapRefColor.xyz * groundColor.xyz, 0.0, 1.0); 
    modulatedColor.a = ( 1.0 - vNdotV.x ) * 0.5 - 0.01;		
    float opacity =  clamp(4.0 * (groundColor.a * groundColor.a - 0.75), 0.0, 1.0);

    gl_FragColor.rgb = mix(modulatedColor.rgb, groundColor.rgb, opacity) + specularLightWeighting.rgb;
    gl_FragColor.a = modulatedColor.a + opacity ;
    gl_FragColor.a /= 2.0;
    gl_FragColor.a += specularLightWeighting.a;
}

