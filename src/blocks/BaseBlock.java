/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;

import java.util.Collection;

/**
 * Represents a mutable "snapshot" of a block.
 *
 * <p>An instance of this block contains all the information needed to
 * accurately reproduce the block, provided that the instance was
 * made correctly. In some implementations, it may not be possible to get a
 * snapshot of blocks correctly, so, for example, the NBT data for a block
 * may be missing.</p>
 *
 * <p>This class identifies blocks using an integer ID. However, IDs for
 * a given block may differ between worlds so it is important that users of
 * this class convert the ID from one "world space" to another "world space,"
 * a task that that is assisted with by working with the source and
 * destination {@link WorldData} instances. Numeric IDs are utilized because
 * they are more space efficient to store, and it also implies that internal
 * uses of this class (i.e. history, etc.) do not need to worry about
 * interning block string IDs.</p>
 *
 * <p>A peculiar detail of this class is that it accepts {@code -1} as a
 * valid data value. This is due to legacy reasons: WorldEdit uses -1
 * as a "wildcard" block value, even though a {@link Mask} would be
 * more appropriate.</p>
 */
public class BaseBlock{

    public static final BaseBlock AIR = new BaseBlock(0);
    public static final BaseBlock STAINED_GLASS_LIGHT_BLUE = new BaseBlock(95, 3);
    public static final BaseBlock STAINED_GLASS_BLUE = new BaseBlock(95, 11);
    public static final BaseBlock STAINED_GLASS_PURPLE = new BaseBlock(95, 10);
    public static final BaseBlock STAINED_GLASS_MAGENTA = new BaseBlock(95, 2);
    public static final BaseBlock STAINED_GLASS_RED = new BaseBlock(95, 14);
    public static final BaseBlock STAINED_GLASS_BLACK = new BaseBlock(95, 15);
    public static final BaseBlock PACKED_ICE = new BaseBlock(174);
    public static final BaseBlock STAINED_CLAY_RED = new BaseBlock(159,14);
    /**
     * Indicates the highest possible block ID (inclusive) that can be used.
     * This value is subject to change depending on the implementation, but
     * internally this class only supports a range of 4096 IDs (for space
     * reasons), which coincides with the number of possible IDs that official
     * Minecraft supports as of version 1.7.
     */
    public static final int MAX_ID = 4095;

    /**
     * Indicates the maximum data value (inclusive) that can be used. A future
     * version of Minecraft may abolish block data values.
     */
    public static final int MAX_DATA = 15;

    // Instances of this class should be _as small as possible_ because there will
    // be millions of instances of this object.

    private short id;
    private short data;
    private CompoundTag nbtData;

    /**
     * Construct a block with the given ID and a data value of 0.
     *
     * @param id ID value
     * @see #setId(int)
     */
    public BaseBlock(int id) {
        internalSetId(id);
        internalSetData(0);
    }

    /**
     * Construct a block with the given ID and data value.
     *
     * @param id ID value
     * @param data data value
     * @see #setId(int)
     * @see #setData(int)
     */
    public BaseBlock(int id, int data) {
        internalSetId(id);
        internalSetData(data);
    }

    /**
     * Construct a block with the given ID, data value and NBT data structure.
     *
     * @param id ID value
     * @param data data value
     * @param nbtData NBT data, which may be null
     */
    public BaseBlock(int id, int data, CompoundTag nbtData) {
        setId(id);
        setData(data);
        setNbtData(nbtData);
    }

    /**
     * Create a clone of another block.
     *
     * @param other the other block
     */
    public BaseBlock(BaseBlock other) {
        this(other.getId(), other.getData(), other.getNbtData());
    }

    /**
     * Get the ID of the block.
     *
     * @return ID (between 0 and {@link #MAX_ID})
     */
    public int getId() {
        return id;
    }

    /**
     * Set the block ID.
     *
     * @param id block id (between 0 and {@link #MAX_ID}).
     */
    protected final void internalSetId(int id) {
        if (id > MAX_ID) {
            throw new IllegalArgumentException("Can't have a block ID above "
                    + MAX_ID + " (" + id + " given)");
        }

        if (id < 0) {
            throw new IllegalArgumentException("Can't have a block ID below 0");
        }

        this.id = (short) id;
    }

    /**
     * Set the block ID.
     *
     * @param id block id (between 0 and {@link #MAX_ID}).
     */
    public void setId(int id) {
        internalSetId(id);
    }

    /**
     * Get the block's data value.
     *
     * @return data value (0-15)
     */
    public int getData() {
        return data;
    }

    /**
     * Set the block's data value.
     *
     * @param data block data value (between 0 and {@link #MAX_DATA}).
     */
    protected final void internalSetData(int data) {
        if (data > MAX_DATA) {
            throw new IllegalArgumentException(
                    "Can't have a block data value above " + MAX_DATA + " ("
                            + data + " given)");
        }

        if (data < -1) {
            throw new IllegalArgumentException("Can't have a block data value below -1");
        }

        this.data = (short) data;
    }

    /**
     * Set the block's data value.
     *
     * @param data block data value (between 0 and {@link #MAX_DATA}).
     */
    public void setData(int data) {
        internalSetData(data);
    }

    /**
     * Set both the block's ID and data value.
     *
     * @param id ID value
     * @param data data value
     * @see #setId(int)
     * @see #setData(int)
     */
    public void setIdAndData(int id, int data) {
        setId(id);
        setData(data);
    }

    /**
     * Returns whether the data value is -1, indicating that this block is to be
     * used as a wildcard matching block.
     *
     * @return true if the data value is -1
     */
    public boolean hasWildcardData() {
        return getData() == -1;
    }

    public boolean hasNbtData() {
        return getNbtData() != null;
    }

    public String getNbtId() {
        CompoundTag nbtData = getNbtData();
        if (nbtData == null) {
            return "";
        }
        Tag idTag = nbtData.getValue().get("id");
        if (idTag != null && idTag instanceof StringTag) {
            return ((StringTag) idTag).getValue();
        } else {
            return "";
        }
    }

    public CompoundTag getNbtData() {
        return nbtData;
    }

    public void setNbtData(CompoundTag nbtData) {
        this.nbtData = nbtData;
    }

    /**
     * Get the type of block.
     *
     * @return the type
     */
    public int getType() {
        return getId();
    }

    /**
     * Set the type of block.
     *
     * @param type the type to set
     */
    public void setType(int type) {
        setId(type);
    }

    /**
     * Checks whether the type ID and data value are equal.
     */
    public boolean equals(Object o) {
        if (!(o instanceof BaseBlock)) {
            return false;
        }

        final BaseBlock otherBlock = (BaseBlock) o;

        return getType() == otherBlock.getType() && getData() == otherBlock.getData();

    }

    /**
     * Checks if the type is the same, and if data is the same if only data != -1.
     *
     * @param o other block
     * @return true if equal
     */
    public boolean equalsFuzzy(BaseBlock o) {
        return (getType() == o.getType()) && (getData() == o.getData() || getData() == -1 || o.getData() == -1);
    }


    public int hashCode() {
        int ret = getId() << 3;
        if (getData() != (byte) -1) ret |= getData();
        return ret;
    }

    public String toString() {
        return "Block{ID:" + getId() + ", Data: " + getData() + "}";
    }

}
