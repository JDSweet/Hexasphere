attribute vec4 a_position;

uniform mat4 u_projectionViewMatrix;

void main()
{
    gl_Position =  u_projectionViewMatrix * a_position;
}
