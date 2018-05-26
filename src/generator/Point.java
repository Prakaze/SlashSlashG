package generator;

public class Point{

  private double x;
  private double y;
  private double z;

  public Point(double x, double y, double z){

    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point set(double x, double y, double z){

    this.x = x;
    this.y = y;
    this.z = z;

    return this;
  }

  public Point mult(double x){

    this.x *= x;
    this.y *= x;
    this.z *= x;

    return this;
  }

  public Point copy(){

    return new Point(this.x, this.y, this.z);
  }

  public Point add(Point p){

    this.x += p.getX();
    this.y += p.getY();
    this.z += p.getZ();

    return this;
  }

  public double mag(){

    return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
  }

  public void setMag(double newMag){

    double ratio = newMag / this.mag();

    this.mult(ratio);
  }

  public double getX(){

    return this.x;
  }

  public double getY(){

    return this.y;
  }

  public double getZ(){

    return this.z;
  }

  public void setX(double x){

    this.x = x;
  }

  public void setY(double y){

    this.y = y;
  }

  public void setZ(double z){

    this.z = z;
  }
}
