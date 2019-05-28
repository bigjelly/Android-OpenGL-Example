uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec4 a_Color;    //顶点颜色
varying  vec4 v_Color;  //用于传递给片元着色器的变量

void main()
{
    gl_Position = u_Matrix * a_Position;
    v_Color = a_Color;//将接收的颜色传递给片元着色器
}