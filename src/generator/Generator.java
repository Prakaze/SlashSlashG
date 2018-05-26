package generator;

import tools.SchemSaver;
import blocks.BaseBlock;

public abstract class Generator{

  private Point start;
  private Point end;

  public Generator(Point start, Point end){

    this.start = start;
    this.end = end;
  }

  public void generate(int width, int height, int length, String fileName){

    BaseBlock[][][] blocks = new BaseBlock[width][height][length];

    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        for(int z = 0; z < length; z++){

          Point p = mappedPoint(x, y, z, width, height, length);
          blocks[x][y][z] = blockAt(p);
        }
      }

      float percentage = Math.round((y+1)*1000f / height) / 10f;
      System.out.println("\nGenerated " + percentage + "%");
    }

    System.out.println("\nSuccessfully generated!");

    SchemSaver.saveAsSchem(fileName, blocks, width, height, length);
  }

  protected abstract BaseBlock blockAt(Point p);

  private Point mappedPoint(int x, int y, int z, int width, int height, int length){

    double newX = map(x, width, this.start.getX(), this.end.getX());
    double newY = map(y, height, this.start.getY(), this.end.getY());
    double newZ = map(z, length, this.start.getZ(), this.end.getZ());

    return new Point(newX, newY, newZ);
  }

  private double map(double v, int size, double start, double end){

    if(size >= 2){

      return (v / (size - 1)) * (end - start) + start;
    } else {

      return end + (start - end) / 2;
    }
  }
}
