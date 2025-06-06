#version 120

varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    gl_FragColor = texColor * v_color;
}
