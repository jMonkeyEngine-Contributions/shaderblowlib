#define TWO_PI 6.28318 
 
#ifdef HAS_COLOR
    uniform vec4 m_Color; 
#endif
 
#ifdef HAS_COLORMAP
  uniform sampler2D m_ColorMap;
#endif

uniform sampler2D m_TextureAtlas; 
uniform sampler2D m_NoiseTex; 

uniform float     g_Time;
 
uniform float     m_ScaleFactor; 
uniform float     m_Percentage; 
uniform float     m_SamplesPerCell; 
uniform float     m_RO1;
uniform float     m_NumImages;
 
uniform bool      m_RandomScale; 
uniform bool      m_RandomRotate;

uniform bool      m_Animated;
uniform bool      m_UseAtlasColors;
 
varying vec2      texCoord; 
 
void main() 
{
    vec4 color = vec4(1.0);

    #ifdef HAS_COLORMAP
        color *= texture2D (m_ColorMap, texCoord);
    #endif

    #ifdef HAS_COLOR
        color *= m_Color;
    #endif

    texCoord *= m_ScaleFactor;
    vec2 cell = floor(texCoord); 
    vec2 offset = texCoord - cell; 
 
    for (int i = -1; i <= int (m_RandomRotate); i++) { 
        for (int j = -1; j <= int (m_RandomRotate); j++) { 
            vec2 currentCell   = cell + vec2(float(i), float(j)); 
            vec2 currentOffset = offset - vec2(float(i), float(j)); 
         
            vec2 randomUV = currentCell * vec2(m_RO1); 
         
            for (int k = 0; k < int (m_SamplesPerCell); k++) { 
                vec4 random = texture2D(m_NoiseTex, randomUV * (m_Animated ? g_Time * 0.05 : 1.0)); 
                randomUV   += random.ba; 
             
               if (random.r < m_Percentage) { 
                    vec2 glyphIndex; 
                    mat2 rotator; 
                    vec2 index; 
                    float rotationAngle, cosRot, sinRot; 
                 
                    index.s = floor(random.b * m_NumImages); 
                    index.t = floor(random.g * m_NumImages); 
                 
                    if (m_RandomRotate) { 
                        rotationAngle = TWO_PI * random.g; 
                        cosRot = cos(rotationAngle); 
                        sinRot = sin(rotationAngle); 
                        rotator[0] = vec2(cosRot, sinRot); 
                        rotator[1] = vec2(-sinRot, cosRot); 
                        glyphIndex = -rotator * (currentOffset - random.rg); 
                    } 
                    else { 
                        glyphIndex = currentOffset - random.rg; 
                    } 
 
                    if (m_RandomScale) {
                        glyphIndex /= vec2(0.5 * random.r + 0.5); 
                    }
 
                    glyphIndex = (clamp(glyphIndex, 0.0, 1.0) + index) * (1 / m_NumImages); 
 
                    vec4 image = texture2D(m_TextureAtlas, glyphIndex); 
 
                    if (image.r != 1.0) {
                        if (m_UseAtlasColors) {
                           // color.rgb = mix(.rgb, color.rgb, 0.01);
                            color.rgb = mix(image.rgb, color.rgb, image.r);
                        } else {
                            color.rgb = mix(random.rgb, color.rgb, image.r);
                        }
                    }
                } 
            } 
        } 
    } 
 
    gl_FragColor = color; 
}