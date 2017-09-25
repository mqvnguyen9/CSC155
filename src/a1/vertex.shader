#version 450
uniform float xOffset;
uniform float yOffset;
uniform float zoomAmount;
out vec4 initialColor;
void main(void) {
	if (gl_VertexID == 0) {
	    gl_Position = vec4(zoomAmount * (0.25 + xOffset), zoomAmount * (-0.25 + yOffset), 0.0, 1.0);
        initialColor = vec4(1.0, 0.0, 0.0, 1.0);
	}
	else if (gl_VertexID == 1) {
	    gl_Position = vec4(zoomAmount * (-0.25 + xOffset), zoomAmount * (-0.25 + yOffset), 0.0, 1.0);
	    initialColor = vec4(0.0, 1.0, 0.0, 1.0);
	}
	else {
	    gl_Position = vec4(zoomAmount * (0.25 + xOffset), zoomAmount * (0.25 + yOffset), 0.0, 1.0);
	    initialColor = vec4(0.0, 0.0, 1.0, 1.0);
	}
}