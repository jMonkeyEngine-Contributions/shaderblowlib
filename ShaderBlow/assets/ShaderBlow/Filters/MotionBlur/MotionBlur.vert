attribute vec4 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;

void main() {
  texCoord = inTexCoord;
  gl_Position = inPosition * 2.0 - 1.0; // vec4(pos, 0.0, 1.0);
}