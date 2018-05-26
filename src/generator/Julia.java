package generator;

import blocks.BaseBlock;

public class Julia extends Generator{

  private int maxIterations;

  public Julia(Point start, Point end, int maxIterations){

    super(start, end);
    this.maxIterations = maxIterations;
  }

  protected BaseBlock blockAt(Point p){

    double oldX = p.getX();
    double oldY = p.getY();

    double x = oldX;
    double y = oldY;

    double radius = 1;
    double z = p.getZ();
    double paramX = radius * -Math.cos(z * Math.PI / 2);
    double paramY = radius * -Math.sin(z * Math.PI / 2);

    double tmpX;
    int n = 0;

    while (x*x + y*y < 4 && n < this.maxIterations) {

      tmpX = x;

      x = x*x - y*y + paramX;
      y = 2 * tmpX * y + paramY;

      n++;
    }


    if(n == this.maxIterations){

      return new BaseBlock(95, 15);
    } else if(n > 22) {

      return new BaseBlock(95, 14);
    } else if(n > 18){

      return new BaseBlock(95, 2);
    } else if(n > 14){

      return new BaseBlock(95, 2);
    } else if(n > 10){

      return new BaseBlock(95, 11);
    } else if(n > 9){

      return new BaseBlock(95, 3);
    } else {

      return new BaseBlock(0);
    }
  }
}
