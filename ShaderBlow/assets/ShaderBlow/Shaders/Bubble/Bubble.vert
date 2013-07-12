attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

attribute vec3 inBinormal;

uniform mat3 g_NormalMatrix;


uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform float g_Time;
uniform vec3 g_CameraPosition;


varying vec3 vNdotV;
varying vec2 vTexCoord;

varying vec4 vPosition;
varying vec3 vTransformedNormal;
varying vec3 vNormal;



vec3 waveSpeed = vec3(0.125*2.0, 0.3*2.0, 0.125*2.0);

void main(){
    vTexCoord = inTexCoord;
    vNormal = inNormal;
    
    
    
    vec3 wave = vec3(abs(inPosition.x), abs(inPosition.y), abs(inPosition.z));
    wave *=  vec3(waveSpeed.x*sin(g_Time / 3.0),waveSpeed.y*sin(g_Time / 200.0),waveSpeed.z*sin(g_Time /400.0))*(sin(g_Time)+0.5);
    vec3 newPos = inNormal * wave;
    newPos = inPosition + newPos;

    gl_Position = g_WorldViewProjectionMatrix * vec4(newPos, 1.0);


    vec3 viewVec = normalize(vec4(g_CameraPosition,1.0) - vec4(newPos, 1.0) * g_WorldMatrix).xyz;

    vNdotV = dot(inNormal, viewVec);

    vPosition = vec4(newPos, 1.0) * g_WorldMatrix;
    vTransformedNormal = inNormal*g_NormalMatrix;
}