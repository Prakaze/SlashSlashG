import generator.Generator;
import generator.MandelBox;
import generator.Julia;
import generator.Point;

class SlashSlashG{

  public static void main(String[] args){

    Point start = new Point(1.25, 5.5, 2);
    Point end = new Point(2.75, 6, 3.5);
    Generator g = new MandelBox(start, end, 21);

    int width = 765;
    int height = 255;
    int length = 765;

    g.generate(width, height, length, "mandelBoxD");
  }
}
