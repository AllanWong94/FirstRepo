


/**
 * Created by Allan Wong on 2017/5/14.
 */
public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP,double yP,double xV,double yV,double m,String img){
        xxPos=xP;
        yyPos=yP;
        xxVel=xV;
        yyVel=yV;
        mass=m;
        imgFileName=img;
    }

    public Planet(Planet p){
        xxPos=p.xxPos;
        yyPos=p.yyPos;
        xxVel=p.xxVel;
        yyVel=p.yyVel;
        mass=p.mass;
        imgFileName=p.imgFileName;
    }
    
    public double calcDistance(Planet X){
    	double dist=0;
    	dist=Math.sqrt((X.xxPos-xxPos)*(X.xxPos-xxPos)+(X.yyPos-yyPos)*(X.yyPos-yyPos));
    	return dist;
    }
    
    public double calcForceExertedBy(Planet X){
    	double dist=calcDistance(X);
    	double G=6.67 * Math.pow(10, -11);
    	double force=0;
    	force=G*mass*X.mass/(dist*dist);
    	return force;
    }
    
    public double calcForceExertedByX(Planet X){
    	double force=calcForceExertedBy(X);
    	double dist=calcDistance(X);
    	double dx=X.xxPos-xxPos;
    	return force*dx/dist;
    }
    
    public double calcForceExertedByY(Planet X){
    	double force=calcForceExertedBy(X);
    	double dist=calcDistance(X);
    	double dy=X.yyPos-yyPos;
    	return force*dy/dist;
    }
    
    public double calcNetForceExertedByX(Planet[] planets){
    	double force=0;
    	for (int i=0;i<planets.length;i++){
    		if (!(this.equals(planets[i]))){
    			force+=calcForceExertedByX(planets[i]);
    		}
    	}
    	return force;
    }
    
    public double calcNetForceExertedByY(Planet[] planets){
    	double force=0;
    	for (int i=0;i<planets.length;i++){
    		if (!(this.equals(planets[i]))){
    			force+=calcForceExertedByY(planets[i]);
    		}
    	}
    	return force;
    }
    
    public void update(double dt, double Fx, double Fy){
    	double ax=Fx/mass;
    	double ay=Fy/mass;
    	xxVel=xxVel+dt*ax;
    	yyVel=yyVel+dt*ay;
    	xxPos+=dt*xxVel;
    	yyPos+=dt*yyVel;
    }
}













