/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Matrix4f {
    private float[][] m;
    
    public Matrix4f(){
        m = new float[4][4];
    }
    
    public Matrix4f initIdentity(){
        m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = 0;
        m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3] = 0;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3] = 0;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;
        
        return this;
    }
    
    public Matrix4f initProjection(float fov, float width, float height, float zNear, float zFar){
        float ar = width/height; //aspect ratio
        float tanHalfFOV = (float)Math.tan(Math.toRadians(fov/2)); //calculate edge of window to the center of the screen
        float zRange = zNear - zFar; //space we have in the depth
        
        m[0][0] = 1.0f/(tanHalfFOV*ar);    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = 0;
        m[1][0] = 0;    m[1][1] = 1.0f/(tanHalfFOV*ar);    m[1][2] = 0;    m[1][3] = 0;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = (-zNear - zFar)/zRange;    m[2][3] = 2*zFar * zNear / zRange;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 1;    m[3][3] = 0;
        
        return this;
    }
    
    public Matrix4f initTranslation(float x, float y, float z){
        //x             //y             //z             //w
        m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = x; //final x
        m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3] = y; //final y
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3] = z; //final z
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1; //final w
        
        return this;
    }
    
    public Matrix4f initRotation(float x, float y, float z){
        Matrix4f rx = new Matrix4f(); //2d rotation on x
        Matrix4f ry = new Matrix4f(); //y
        Matrix4f rz = new Matrix4f(); //and z
        x = (float)Math.toRadians(x);
        y = (float)Math.toRadians(y);
        z = (float)Math.toRadians(z);
        
        //X-Y ROTATION
        //x             //y             //z             //w
        rz.m[0][0] = (float)Math.cos(z);    rz.m[0][1] = -(float)Math.sin(z);    rz.m[0][2] = 0;    rz.m[0][3] = 0; //final x
        rz.m[1][0] = (float)Math.sin(z);    rz.m[1][1] = (float)Math.cos(z);    rz.m[1][2] = 0;    rz.m[1][3] = 0; //final y
        rz.m[2][0] = 0;    rz.m[2][1] = 0;    rz.m[2][2] = 1;    rz.m[2][3] = 0; //final z
        rz.m[3][0] = 0;    rz.m[3][1] = 0;    rz.m[3][2] = 0;    rz.m[3][3] = 1; //final w
        
        //Y-Z ROATION
        //x             //y             //z             //w
        rx.m[0][0] = 1;    rx.m[0][1] = 0;    rx.m[0][2] = 0;    rx.m[0][3] = 0; //final x
        rx.m[1][0] = 0;    rx.m[1][1] = (float)Math.cos(x);    rx.m[1][2] = -(float)Math.sin(x);    rx.m[1][3] = 0; //final y
        rx.m[2][0] = 0;    rx.m[2][1] = (float)Math.sin(x);    rx.m[2][2] = (float)Math.cos(x);    rx.m[2][3] = 0; //final z
        rx.m[3][0] = 0;    rx.m[3][1] = 0;    rx.m[3][2] = 0;    rx.m[3][3] = 1; //final w
        
        //X-Z ROTATION
                //x             //y             //z             //w
        ry.m[0][0] = (float)Math.cos(y);    ry.m[0][1] = 0;    ry.m[0][2] = -(float)Math.sin(y);    ry.m[0][3] = 0; //final x
        ry.m[1][0] = 0;    ry.m[1][1] = 1;    ry.m[1][2] = 0;    ry.m[1][3] = 0; //final y
        ry.m[2][0] = (float)Math.sin(y);    ry.m[2][1] = 0;    ry.m[2][2] = (float)Math.cos(y);    ry.m[2][3] = 0; //final z
        ry.m[3][0] = 0;    ry.m[3][1] = 0;    ry.m[3][2] = 0;    ry.m[3][3] = 1; //final w
        
        m = rz.mul(ry.mul(rx)).getM(); //multiply all 2D rotations to get overall 3D rotation
        return this;
    }
    
    public Matrix4f initScale(float x, float y, float z){
        //x             //y             //z             //w
        m[0][0] = x;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = 0; //final x
        m[1][0] = 0;    m[1][1] = y;    m[1][2] = 0;    m[1][3] = 0; //final y
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = z;    m[2][3] = 0; //final z
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1; //final w
        
        return this;
    }
    
    public Matrix4f mul(Matrix4f r){
        Matrix4f result = new Matrix4f();
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                result.set(i,j,m[i][0] *r.get(0,j)+
                          m[i][1] *r.get(1,j)+
                          m[i][2] *r.get(2,j)+
                          m[i][3] *r.get(3,j));
            }
        }
        return result;
    }
    
    public float[][] getM(){
        return m;
    }
    
    public float get(int x, int y){
        return m[x][y];
    }
    
    public void setM(float[][] m){
        this.m=m;
    }
    
    public void set(int x, int y, float value){
        m[x][y] = value;
    }
}