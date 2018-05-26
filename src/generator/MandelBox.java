package generator;

import com.sk89q.worldedit.blocks.BaseBlock;

public class MandelBox extends Generator{

  private int maxIterations;

  public MandelBox(Point start, Point end, int maxIterations){

    super(start, end);
    this.maxIterations = maxIterations;
  }

  protected BaseBlock blockAt(Point z){

    int iterations = 0;
    Point c = z.copy();

    while(iterations < maxIterations && z.mag() < 10){

      mandelBoxIteration(z, c);
      iterations++;
    }

    if(iterations == this.maxIterations){

      return BaseBlock.STAINED_GLASS_BLACK;
    } else if(iterations > 20) {

      return BaseBlock.STAINED_GLASS_PURPLE;
    } else if(iterations > 19) {

      return BaseBlock.STAINED_GLASS_BLUE;
    } else if(iterations > 18) {

      return BaseBlock.STAINED_GLASS_LIGHT_BLUE;
    } else {

      return BaseBlock.AIR;
    }
  }

  private Point mandelBoxIteration(Point z, Point c){

    float r = .5f;
    float s = 2;
    float f = 1;

    return ballFold(r, boxFold(z).mult(f)).mult(s).add(c);
  }

  private Point ballFold(float r, Point x){

  double m = x.mag();

  if(m < r){

    x.setMag(m / (r*r));
  } else if(m < 1){

    x.setMag(1 / m);
  }

  return x;
}

private Point boxFold(Point x){

  return x.set(foldDim(x.getX()), foldDim(x.getY()), foldDim(x.getZ()));
}

private double foldDim(double x){

  if(x > 1){
    return 2 - x;
  } else if(x < -1){
    return -2 - x;
  } else {
    return x;
  }
}
}
