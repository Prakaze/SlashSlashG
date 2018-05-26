package tools;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NamedTag;
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.zip.GZIPOutputStream;

public class SchemSaver{

  public static void saveAsSchem(String fileName, BaseBlock[][][] blockArray, int width, int height, int length){

    System.out.println("\n\n\nSaving ...");

    HashMap<String, Tag> schematic = new HashMap<String, Tag>();
    schematic.put("Width", new ShortTag((short) width));
    schematic.put("Length", new ShortTag((short) length));
    schematic.put("Height", new ShortTag((short) height));
    schematic.put("Materials", new StringTag("Alpha"));
    schematic.put("WEOriginX", new IntTag((int) 0));
    schematic.put("WEOriginY", new IntTag((int) 0));
    schematic.put("WEOriginZ", new IntTag((int) 0));
    schematic.put("WEOffsetX", new IntTag((int) 0));
    schematic.put("WEOffsetY", new IntTag((int) 0));
    schematic.put("WEOffsetZ", new IntTag((int) 0));

    // Copy
    byte[] blocks = new byte[width * height * length];
    byte[] addBlocks = null;
    byte[] blockData = new byte[width * height * length];
    ArrayList<Tag> tileEntities = new ArrayList<Tag>();

    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        for (int z = 0; z < length; ++z) {
          int index = y * width * length + z * width + x;
          BaseBlock block = blockArray[x][y][z];

          // Save 4096 IDs in an AddBlocks section
          if (block.getType() > 255) {
            if (addBlocks == null) { // Lazily create section
              addBlocks = new byte[(blocks.length >> 1) + 1];
            }

            addBlocks[index >> 1] = (byte) (((index & 1) == 0) ?
                  addBlocks[index >> 1] & 0xF0 | (block.getType() >> 8) & 0xF
                  : addBlocks[index >> 1] & 0xF | ((block.getType() >> 8) & 0xF) << 4);
          }

          blocks[index] = (byte) block.getType();
          blockData[index] = (byte) block.getData();

          // Get the list of key/values from the block
          CompoundTag rawTag = block.getNbtData();
          if (rawTag != null) {
            Map<String, Tag> values = new HashMap<String, Tag>();
            for (Entry<String, Tag> entry : rawTag.getValue().entrySet()) {
              values.put(entry.getKey(), entry.getValue());
            }

            values.put("id", new StringTag(block.getNbtId()));
            values.put("x", new IntTag(x));
            values.put("y", new IntTag(y));
            values.put("z", new IntTag(z));

            CompoundTag tileEntityTag = new CompoundTag(values);
            tileEntities.add(tileEntityTag);
          }
        }
      }
    }

    schematic.put("Blocks", new ByteArrayTag(blocks));
    schematic.put("Data", new ByteArrayTag(blockData));
    schematic.put("Entities", new ListTag(CompoundTag.class, new ArrayList<Tag>()));
    schematic.put("TileEntities", new ListTag(CompoundTag.class, tileEntities));
    if (addBlocks != null) {
        schematic.put("AddBlocks", new ByteArrayTag(addBlocks));
    }

    // Build and output
    CompoundTag schematicTag = new CompoundTag(schematic);
    File file = new File("../data/schematics/" + fileName + ".schematic");
    try{
      NBTOutputStream stream = new NBTOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
      stream.writeNamedTag("Schematic", schematicTag);
      stream.close();
      System.out.println("\n\n\nSuccessfully saved!");
    } catch(FileNotFoundException e){

      System.out.println("Error: File data/schematic not found.");
    } catch(IOException e){

      e.printStackTrace();
    }
  }
}
