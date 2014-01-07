varying vec3 color;

//K in vec2 texCoord0;

uniform sampler2D sampler;

void main(){
	gl_FragColor = vec4(color,1);
	//K gl_FragColor = texture2D(sampler, texCoord0.xy);
}