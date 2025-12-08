#version 120

varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    vec4 color = texColor * v_color;
    gl_FragColor = color;
}
