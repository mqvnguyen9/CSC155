#version 430
uniform bool isGradient;
in vec4 initialColor;
out vec4 finalColor;
void main(void) {
    if (isGradient) {
        finalColor = initialColor;
    } else finalColor = vec4(0.0, 0.0, 1.0, 1.0);
}