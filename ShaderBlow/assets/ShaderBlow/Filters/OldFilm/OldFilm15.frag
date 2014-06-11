#import "Common/ShaderLib/MultiSample.glsllib"

uniform float g_Time;

uniform COLORTEXTURE m_Texture;
uniform vec4 m_FilterColor;
uniform float m_ColorDensity;
uniform float m_RandomValue;
uniform float m_NoiseDensity;
uniform float m_ScratchDensity;
uniform float m_InnerVignetting;
uniform float m_OuterVignetting;

in vec2 texCoord;

out vec4 fragColor;

// Computes the overlay between the source and destination colours.
vec3 overlay(vec3 src, vec3 dst)
{
    return vec3((dst.x <= 0.5) ? (2.0 * src.x * dst.x) : (1.0 - 2.0 * (1.0 - dst.x) * (1.0 - src.x)),
                (dst.y <= 0.5) ? (2.0 * src.y * dst.y) : (1.0 - 2.0 * (1.0 - dst.y) * (1.0 - src.y)),
                (dst.z <= 0.5) ? (2.0 * src.z * dst.z) : (1.0 - 2.0 * (1.0 - dst.z) * (1.0 - src.z)));
}

// 2D Noise by Ian McEwan, Ashima Arts.
vec3 mod289(vec3 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec2 mod289(vec2 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec3 permute(vec3 x) { return mod289(((x*34.0)+1.0)*x); }
float snoise (vec2 v)
{
    const vec4 C = vec4(0.211324865405187,  // (3.0-sqrt(3.0))/6.0
                        0.366025403784439,  // 0.5*(sqrt(3.0)-1.0)
                        -0.577350269189626, // -1.0 + 2.0 * C.x
                        0.024390243902439); // 1.0 / 41.0

    // First corner
    vec2 i  = floor(v + dot(v, C.yy) );
    vec2 x0 = v -   i + dot(i, C.xx);

    // Other corners
    vec2 i1;
    i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec4 x12 = x0.xyxy + C.xxzz;
    x12.xy -= i1;

    // Permutations
    i = mod289(i); // Avoid truncation effects in permutation
    vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))
        + i.x + vec3(0.0, i1.x, 1.0 ));

    vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);
    m = m*m ;
    m = m*m ;

    // Gradients: 41 points uniformly over a line, mapped onto a diamond.
    // The ring size 17*17 = 289 is close to a multiple of 41 (41*7 = 287)

    vec3 x = 2.0 * fract(p * C.www) - 1.0;
    vec3 h = abs(x) - 0.5;
    vec3 ox = floor(x + 0.5);
    vec3 a0 = x - ox;

    // Normalise gradients implicitly by scaling m
    // Approximation of: m *= inversesqrt( a0*a0 + h*h );
    m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );

    // Compute final noise value at P
    vec3 g;
    g.x  = a0.x  * x0.x  + h.x  * x0.y;
    g.yz = a0.yz * x12.xz + h.yz * x12.yw;
    return 130.0 * dot(m, g);
}

void main() {
    float colorFactor = clamp(m_ColorDensity, 0.0, 1.0);
    
    vec3 color = getColor(m_Texture, texCoord).rgb;
    vec3 finalColour = color;
     
    if (colorFactor > 0.0) {
	    // Convert to grayscale
	    float gray = (color.r + color.g + color.b) / 3.0;
	    vec3 grayscale = vec3(gray);
	
	    // Apply overlay
	    finalColour = overlay(m_FilterColor.rgb, grayscale);
	
	    // Lerp final colour
	    finalColour = grayscale + colorFactor * (finalColour - grayscale);
    }    

    // Add noise
    float noiseFactor = clamp(m_NoiseDensity, 0.0, 1.0);
    float noise = snoise(texCoord * vec2(1024.0 + m_RandomValue * 512.0, 1024.0 + m_RandomValue * 512.0)) * 0.5;
    finalColour += noise * noiseFactor;

    // Apply scratches
    float scratchFactor = clamp(m_ScratchDensity, 0.0, 1.0);
    if ( m_RandomValue < scratchFactor )
    {
        // Pick a random spot to show scratches
        float dist = 1.0 / scratchFactor;
        float d = distance(texCoord, vec2(m_RandomValue * dist, m_RandomValue * dist));
        if ( d < 0.4 )
        {
            // Generate the scratch
            float xPeriod = 8.0;
            float yPeriod = 1.0;
            float pi = 3.141592;
            float phase = g_Time;
            float turbulence = snoise(texCoord * (2.5 * (2.0 - m_RandomValue)));
            float vScratch = 0.5 + (sin(((texCoord.x * xPeriod + texCoord.y * yPeriod + turbulence)) * pi + phase) * 0.5);
            vScratch = clamp((vScratch * 10000.0) + 0.35, 0.0, 1.0);

            finalColour.rgb *= vScratch;
        }
    }
    
	// Apply vignetting
	// Max distance from center to corner is ~0.7. Scale that to 1.0.
	float d = distance(vec2(0.5, 0.5), texCoord) * 1.414213;
	float vignetting = clamp((m_OuterVignetting - d) / (m_OuterVignetting - m_InnerVignetting), 0.0, 1.0);
	finalColour.xyz *= vignetting;
	
    // Apply colour
    fragColor.rgb = finalColour;
    fragColor.a = 1.0;
}