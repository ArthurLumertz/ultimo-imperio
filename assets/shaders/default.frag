#version 120

varying vec4 v_color;
varying vec2 v_texCoord;
varying vec4 v_position;

uniform sampler2D u_texture;
uniform vec3 u_ambientLight;

uniform vec2 u_resolution;

const int MAX_LIGHTS = 128;

struct PointLight {
    vec2 position;
    vec4 color;
    float intensity;
    float innerRadius;
    float outerRadius;
};

uniform PointLight u_pointLights[MAX_LIGHTS];
uniform int u_numLights;

vec4 computeLights(vec2 fragPos) {
    vec4 totalLight = vec4(0.0);

    for (int i = 0; i < u_numLights; i++) {

        PointLight light = u_pointLights[i];

        float dist = distance(fragPos, light.position);
        float t = clamp((dist - light.innerRadius) / (light.outerRadius - light.innerRadius), 0.0, 1.0);
        float strength = (1.0 - t) * light.intensity;

        totalLight += light.color * strength;
    }

    return totalLight;
}

vec4 applyVignette(vec4 color) {
    vec2 position = (gl_FragCoord.xy / u_resolution) - vec2(0.5);           
    float dist = length(position * vec2(u_resolution.x/u_resolution.y, 1.0));

    float radius = 2.5;
	float softness = 2.0;
    float vignette = smoothstep(radius, radius - softness, dist);

    color.rgb = color.rgb - (1.0 - vignette);

    return color;
}

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);

    vec2 fragPos = gl_FragCoord.xy;
    vec4 pointLighting = computeLights(fragPos);
    vec4 finalLight = clamp(vec4(u_ambientLight, 1.0) + pointLighting, 0.0, 1.0);

	vec4 color = texColor * finalLight * v_color;
	color = applyVignette(color);
    gl_FragColor = color;
}
