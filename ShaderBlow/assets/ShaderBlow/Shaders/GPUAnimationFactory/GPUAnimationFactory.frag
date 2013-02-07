#import "Common/ShaderLib/Parallax.glsllib"
#import "Common/ShaderLib/Optics.glsllib"
 
#define ATTENUATION
//#define HQ_ATTENUATION
varying float fog_z;
uniform int m_FogMode;
uniform bool m_ExcludeSky;
uniform vec4 m_FogColor;
uniform float m_FogStartDistance;
uniform float m_FogEndDistance;
uniform float m_FogDensity;
 
varying vec2 texCoord;
#ifdef SEPARATE_TEXCOORD
  varying vec2 texCoord2;
#endif
 
varying vec3 AmbientSum;
varying vec4 DiffuseSum;
varying vec3 SpecularSum;
 
uniform float m_Scale;
 
uniform sampler2D m_Texture;
uniform float g_Time;
 
const float pi = 3.14159;
const vec2 center = vec2(0.5);
const float threshold = 0.0;
 
varying vec2 animTexCoord1;
varying vec2 animTexCoord2;
 
#ifndef VERTEX_LIGHTING
  uniform vec4 g_LightDirection;
  //varying vec3 vPosition;
  varying vec3 vViewDir;
  varying vec4 vLightDir;
  varying vec3 lightVec;
#else
  varying vec2 vertexLightValues;
#endif
 
#ifdef DIFFUSEMAP
  uniform sampler2D m_DiffuseMap;
#endif
 
#ifdef SPECULARMAP
  uniform sampler2D m_SpecularMap;
#endif
 
#ifdef PARALLAXMAP
  uniform sampler2D m_ParallaxMap;  
#endif
#if (defined(PARALLAXMAP) || (defined(NORMALMAP_PARALLAX) && defined(NORMALMAP))) && !defined(VERTEX_LIGHTING) 
    uniform float m_ParallaxHeight;
#endif
 
#ifdef LIGHTMAP
  uniform sampler2D m_LightMap;
#endif
   
#ifdef NORMALMAP
  uniform sampler2D m_NormalMap;   
#else
  varying vec3 vNormal;
#endif
 
#ifdef ALPHAMAP
  uniform sampler2D m_AlphaMap;
#endif
 
#ifdef COLORRAMP
  uniform sampler2D m_ColorRamp;
#endif
 
uniform float m_AlphaDiscardThreshold;
 
#ifndef VERTEX_LIGHTING
uniform float m_Shininess;
 
#ifdef HQ_ATTENUATION
uniform vec4 g_LightPosition;
#endif
 
#ifdef USE_REFLECTION 
    uniform float m_ReflectionPower;
    uniform float m_ReflectionIntensity;
    varying vec4 refVec;
 
    uniform ENVMAP m_EnvMap;
#endif
 
#ifdef IS_MOVING
//  uniform float m_MoveF_Speed;
#endif
 
float tangDot(in vec3 v1, in vec3 v2){
    float d = dot(v1,v2);
    #ifdef V_TANGENT
        d = 1.0 - d*d;
        return step(0.0, d) * sqrt(d);
    #else
        return d;
    #endif
}
 
float lightComputeDiffuse(in vec3 norm, in vec3 lightdir, in vec3 viewdir){
    #ifdef MINNAERT
        float NdotL = max(0.0, dot(norm, lightdir));
        float NdotV = max(0.0, dot(norm, viewdir));
        return NdotL * pow(max(NdotL * NdotV, 0.1), -1.0) * 0.5;
    #else
        return max(0.0, dot(norm, lightdir));
    #endif
}
 
float lightComputeSpecular(in vec3 norm, in vec3 viewdir, in vec3 lightdir, in float shiny){
    // NOTE: check for shiny <= 1 removed since shininess is now 
    // 1.0 by default (uses matdefs default vals)
    #ifdef LOW_QUALITY
       // Blinn-Phong
       // Note: preferably, H should be computed in the vertex shader
       vec3 H = (viewdir + lightdir) * vec3(0.5);
       return pow(max(tangDot(H, norm), 0.0), shiny);
    #elif defined(WARDISO)
        // Isotropic Ward
        vec3 halfVec = normalize(viewdir + lightdir);
        float NdotH  = max(0.001, tangDot(norm, halfVec));
        float NdotV  = max(0.001, tangDot(norm, viewdir));
        float NdotL  = max(0.001, tangDot(norm, lightdir));
        float a      = tan(acos(NdotH));
        float p      = max(shiny/128.0, 0.001);
        return NdotL * (1.0 / (4.0*3.14159265*p*p)) * (exp(-(a*a)/(p*p)) / (sqrt(NdotV * NdotL)));
    #else
       // Standard Phong
       vec3 R = reflect(-lightdir, norm);
       return pow(max(tangDot(R, viewdir), 0.0), shiny);
    #endif
}
 
vec2 computeLighting(in vec3 wvNorm, in vec3 wvViewDir, in vec3 wvLightDir){
   float diffuseFactor = lightComputeDiffuse(wvNorm, wvLightDir, wvViewDir);
   float specularFactor = lightComputeSpecular(wvNorm, wvViewDir, wvLightDir, m_Shininess);
 
   #ifdef HQ_ATTENUATION
    float att = clamp(1.0 - g_LightPosition.w * length(lightVec), 0.0, 1.0);
   #else
    float att = vLightDir.w;
   #endif
 
   if (m_Shininess <= 1.0) {
       specularFactor = 0.0; // should be one instruction on most cards ..
   }
 
   specularFactor *= diffuseFactor;
 
   return vec2(diffuseFactor, specularFactor) * vec2(att);
}
#endif
 
#ifdef USE_DEFORM
    #ifdef USE_FWAVE
        uniform float m_DeformF_Wave_SizeX;
        uniform float m_DeformF_Wave_SizeY;
        uniform float m_DeformF_Wave_DepthX;
        uniform float m_DeformF_Wave_DepthY;
        uniform float m_DeformF_Wave_SpeedX;
        uniform float m_DeformF_Wave_SpeedY;
    #endif
    #ifdef USE_FWARP
        uniform float m_DeformF_Warp_SizeX;
        uniform float m_DeformF_Warp_SizeY;
        uniform float m_DeformF_Warp_DepthX;
        uniform float m_DeformF_Warp_DepthY;
        uniform float m_DeformF_Warp_SpeedX;
        uniform float m_DeformF_Warp_SpeedY;
    #endif
    #ifdef USE_FMIXER
        uniform float m_DeformF_Mixer_SizeX;
        uniform float m_DeformF_Mixer_SizeY;
        uniform float m_DeformF_Mixer_DepthX;
        uniform float m_DeformF_Mixer_DepthY;
        uniform float m_DeformF_Mixer_SpeedX;
        uniform float m_DeformF_Mixer_SpeedY;
    #endif
#endif
 
// Stops edge tearing/pulling
vec2 deformFGradientDistance(in vec2 vecOut, in bool invert) {
    float distX = distance(vecOut.x, center.x);
    float distY = distance(vecOut.y, center.y);
    if (!invert) {
    //  distX = -distX;
    //  distY = -distY;
        distX = sin(distX * pi) * center.x;
        distY = sin(distY * pi) * center.y;
    } else {
        distX = cos(distX * pi) * center.x;
        distY = cos(distY * pi) * center.y;
    }
    return vec2(distX,distY);
}
float deformFAngleFromCenter(in vec2 vecOut) {
    return atan(center.y-vecOut.y,center.x-vecOut.x);//-(pi/2);
}
 
// Image distortions
#ifdef USE_FWAVE
vec2 deformFRipple(in vec2 texCoords) {
    vec2 vecOut = texCoords;
    vec2 dist = deformFGradientDistance(vecOut, true);
    // Centered Ripple //
     
    vec2 tc = vecOut;
    vec2 p = -1.0 + 2.0 * tc;
    float len = length(p);
    vecOut.x = center.x*cos(len*m_DeformF_Wave_SizeX-(g_Time*m_DeformF_Wave_SpeedX)*m_DeformF_Wave_DepthX)*(m_DeformF_Wave_DepthX*0.01);
    vecOut.y = center.y*cos(len*m_DeformF_Wave_SizeY-(g_Time*m_DeformF_Wave_SpeedY)*m_DeformF_Wave_DepthY)*(m_DeformF_Wave_DepthY*0.01);
     
    vecOut *= dist;
     
    return vecOut;
}
#endif
#ifdef USE_FBREATH
vec2 deformFBreath(in vec2 texCoords, in vec2 texSize, in float len) {
    vec2 dist = deformFGradientDistance(texCoords,true);
    vec2 vecOut = vec2(0.0);
    vecOut = texSize/len*dot(texSize/len,texSize)*(sin(g_Time))*4.0*dist;
    return vecOut;
}
#endif
#ifdef USE_FWARP
vec2 deformFWarp(in vec2 texCoords) {
    vec2 vecOut = texCoords;
     
    vec2 dist = deformFGradientDistance(vecOut, true);
     
    vecOut.x = sin(dist.x*dist.y);
    vecOut.x *= sin(g_Time*m_DeformF_Warp_SpeedX);
    vecOut.x *= (m_DeformF_Warp_SizeX);
    vecOut.x *= (m_DeformF_Warp_DepthX);
    vecOut.x *= center.x;
     
    vecOut.y = sin(dist.x*dist.y);
    vecOut.y *= sin(g_Time*m_DeformF_Warp_SpeedY);
    vecOut.y *= (m_DeformF_Warp_SizeY);
    vecOut.y *= (m_DeformF_Warp_DepthY);
    vecOut.y *= center.y;
     
    return vecOut;
}
#endif
#ifdef USE_FMIXER
vec2 deformFMixer(in vec2 texCoords, in vec2 texSize, in float len) {
    float i;
    vec2 vecOut = texCoords;
    vec2 center = (texSize/len);
    vec2 outter = -center;
    #ifdef USE_ANIMATION
        vecOut.x = (outter.x+m_DeformF_Mixer_DepthX+(mod(g_Time,i)*m_DeformF_Mixer_SpeedX))*m_DeformF_Mixer_SizeX;
        vecOut.y = (outter.y+m_DeformF_Mixer_DepthY+(mod(g_Time,i)*m_DeformF_Mixer_SpeedY))*m_DeformF_Mixer_SizeY;
    #else
        vecOut.x = (outter.x+m_DeformF_Mixer_DepthX+(m_DeformF_Mixer_SpeedX))*m_DeformF_Mixer_SizeX;
        vecOut.y = (outter.y+m_DeformF_Mixer_DepthY+(m_DeformF_Mixer_SpeedY))*m_DeformF_Mixer_SizeY;
    #endif
    return vecOut;
}
#endif
#ifdef USE_FFISHEYE
vec2 deformFFisheye(in vec2 texCoords, in vec2 texSize, in float len) {
//  vec2 dist = deformFGradientDistance(texCoords,false);
    vec2 vecOut = vec2(0.0);
//  vecOut = dist;
    return vecOut;
}
#endif
 
void main(){
    vec4 color = vec4(1.0);
    vec2 texCoordFinal = animTexCoord1;
 
    vec2 p = -0.1 + 0.2 * animTexCoord2;
    float len = length(p);
    #ifdef USE_DEFORM
        #ifdef USE_FWAVE
            texCoordFinal = animTexCoord1 + deformFRipple(animTexCoord2);
        #endif
        #ifdef USE_FBREATH
            texCoordFinal = animTexCoord1 + deformFBreath(animTexCoord2, p, len);
        #endif
        #ifdef USE_FWARP
            texCoordFinal = animTexCoord1 + deformFWarp(animTexCoord2);
        #endif
        #ifdef USE_FMIXER
            texCoordFinal = animTexCoord1 + deformFMixer(animTexCoord2, p, len);
        #endif
        #ifdef USE_FFISHEYE
            texCoordFinal = animTexCoord1 + deformFFisheye(animTexCoord2, p, len);
        #endif
    //  color *= texture2D(m_Texture, animTexCoord1);
    #else
    //  color *= texture2D(m_Texture, animTexCoord1);
    #endif
     
    vec2 newTexCoord;
      
    #if (defined(PARALLAXMAP) || (defined(NORMALMAP_PARALLAX) && defined(NORMALMAP))) && !defined(VERTEX_LIGHTING) 
      
       #ifdef STEEP_PARALLAX
           #ifdef NORMALMAP_PARALLAX
               //parallax map is stored in the alpha channel of the normal map         
               texCoordFinal = steepParallaxOffset(m_NormalMap, vViewDir, texCoordFinal, m_ParallaxHeight);
           #else
               //parallax map is a texture
               texCoordFinal = steepParallaxOffset(m_ParallaxMap, vViewDir, texCoordFinal, m_ParallaxHeight);         
           #endif
       #else
           #ifdef NORMALMAP_PARALLAX
               //parallax map is stored in the alpha channel of the normal map         
               texCoordFinal = classicParallaxOffset(m_NormalMap, vViewDir, texCoordFinal, m_ParallaxHeight);
           #else
               //parallax map is a texture
               texCoordFinal = classicParallaxOffset(m_ParallaxMap, vViewDir, texCoordFinal, m_ParallaxHeight);
           #endif
       #endif
    #else
       texCoordFinal = texCoordFinal;    
    #endif
     
   #ifdef DIFFUSEMAP
      vec4 diffuseColor = texture2D(m_DiffuseMap, texCoordFinal*m_Scale);
    #else
      vec4 diffuseColor = vec4(1.0);
    #endif
 
    float alpha = DiffuseSum.a * diffuseColor.a;
    #ifdef ALPHAMAP
       alpha = alpha * texture2D(m_AlphaMap, texCoordFinal*m_Scale).r;
    #endif

    if(alpha < m_AlphaDiscardThreshold){
        discard;
    }
 
    #ifndef VERTEX_LIGHTING
        float spotFallOff = 1.0;
 
        #if __VERSION__ >= 110
          // allow use of control flow
          if(g_LightDirection.w != 0.0){
        #endif
 
          vec3 L       = normalize(lightVec.xyz);
          vec3 spotdir = normalize(g_LightDirection.xyz);
          float curAngleCos = dot(-L, spotdir);             
          float innerAngleCos = floor(g_LightDirection.w) * 0.001;
          float outerAngleCos = fract(g_LightDirection.w);
          float innerMinusOuter = innerAngleCos - outerAngleCos;
          spotFallOff = (curAngleCos - outerAngleCos) / innerMinusOuter;
 
          #if __VERSION__ >= 110
              if(spotFallOff <= 0.0){
                  gl_FragColor.rgb = AmbientSum * diffuseColor.rgb;
                  gl_FragColor.a   = alpha;
                  return;
              }else{
                  spotFallOff = clamp(spotFallOff, 0.0, 1.0);
              }
             }
          #else
             spotFallOff = clamp(spotFallOff, step(g_LightDirection.w, 0.001), 1.0);
          #endif
     #endif
  
    // ***********************
    // Read from textures
    // ***********************
    #if defined(NORMALMAP) && !defined(VERTEX_LIGHTING)
      vec4 normalHeight = texture2D(m_NormalMap, newTexCoord*m_Scale);
      vec3 normal = (normalHeight.xyz * vec3(2.0) - vec3(1.0));
      #ifdef LATC
        normal.z = sqrt(1.0 - (normal.x * normal.x) - (normal.y * normal.y));
      #endif
      //normal.y = -normal.y;
    #elif !defined(VERTEX_LIGHTING)
      vec3 normal = vNormal;
      #if !defined(LOW_QUALITY) && !defined(V_TANGENT)
         normal = normalize(normal);
      #endif
    #endif
 
    #ifdef SPECULARMAP
      vec4 specularColor = texture2D(m_SpecularMap, newTexCoord*m_Scale);
    #else
      vec4 specularColor = vec4(1.0);
    #endif
 
    #ifdef LIGHTMAP
       vec3 lightMapColor;
       #ifdef SEPARATE_TEXCOORD
          lightMapColor = texture2D(m_LightMap, texCoord2*m_Scale).rgb;
       #else
          lightMapColor = texture2D(m_LightMap, texCoord*m_Scale).rgb;
       #endif
       specularColor.rgb *= lightMapColor;
       diffuseColor.rgb  *= lightMapColor;
    #endif
 
    #ifdef VERTEX_LIGHTING
       vec2 light = vertexLightValues.xy;
       #ifdef COLORRAMP
           light.x = texture2D(m_ColorRamp, vec2(light.x, 0.0)).r;
           light.y = texture2D(m_ColorRamp, vec2(light.y, 0.0)).r;
       #endif
 
       color.rgb =  AmbientSum     * diffuseColor.rgb + 
                           DiffuseSum.rgb * diffuseColor.rgb  * vec3(light.x) +
                           SpecularSum    * specularColor.rgb * vec3(light.y);
    #else
       vec4 lightDir = vLightDir;
       lightDir.xyz = normalize(lightDir.xyz);
       vec3 viewDir = normalize(vViewDir);
        vec2   light = computeLighting(normal, viewDir, lightDir.xyz) * spotFallOff;
       #ifdef COLORRAMP
           diffuseColor.rgb  *= texture2D(m_ColorRamp, vec2(light.x, 0.0)).rgb;
           specularColor.rgb *= texture2D(m_ColorRamp, vec2(light.y, 0.0)).rgb;
       #endif
 
       // Workaround, since it is not possible to modify varying variables
       vec4 SpecularSum2 = vec4(SpecularSum, 1.0);
       #ifdef USE_REFLECTION
            vec4 refColor = Optics_GetEnvColor(m_EnvMap, refVec.xyz);
 
            // Interpolate light specularity toward reflection color
            // Multiply result by specular map
            specularColor = mix(SpecularSum2 * light.y, refColor, refVec.w) * specularColor;
 
            SpecularSum2 = vec4(1.0);
            light.y = 1.0;
       #endif
 
       color.rgb =  AmbientSum       * diffuseColor.rgb  +
                           DiffuseSum.rgb   * diffuseColor.rgb  * vec3(light.x) +
                           SpecularSum2.rgb * specularColor.rgb * vec3(light.y);
    #endif
     
    gl_FragColor = color;
    /*
    vec4 fogColor = m_FogColor;
    float depth = fog_z / m_FogDistance;
    float LOG2 = 1.442695;
 
    float fogFactor = exp2( -m_FogDensity * m_FogDensity * depth * depth * LOG2 );
    fogFactor = clamp(fogFactor, 0.0, 1.0);
    fogColor *= 0.2;
    fogColor.a = 1.0-fogFactor;
//  gl_FragColor.rgb = mix(fogColor,gl_FragColor,fogFactor).rgb;
    */
//  gl_FragColor = mixFog(m_FogMode, m_FogColor, m_FogStartDistance, m_FogEndDistance, m_FogDensity, m_ExcludeSky, gl_FragColor, fog_z, texCoord);
    gl_FragColor.a = alpha;
}