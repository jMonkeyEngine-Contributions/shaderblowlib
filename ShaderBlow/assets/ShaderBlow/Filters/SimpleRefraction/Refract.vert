uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;

attribute vec2 inTexCoord;

varying vec2 texCoord;
varying float fog_z;

void main(){

    texCoord = inTexCoord;
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

    fog_z = gl_Position.z;
}