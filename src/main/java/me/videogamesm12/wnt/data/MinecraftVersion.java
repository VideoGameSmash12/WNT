/*
 * Copyright (c) 2022 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.videogamesm12.wnt.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * <h1>MinecraftVersion</h1>
 * <p>For whatever reason, Mojang fucked with SharedConstants in a way where trying to get the game version in 1.17.1
 * would work, but that same code would throw a NoSuchMethodError in 1.18.x.</p>
 * <p>This serves as a class to store version information in a way that works across multiple versions of the game
 * that can be serialized using GSON without relying on SharedConstants.</p>
 */
@Data
public class MinecraftVersion
{
    private String id;

    private String name;

    @SerializedName("release_target")
    private String releaseTarget;

    @SerializedName("world_version")
    private int worldVersion;

    @SerializedName("series_id")
    private String seriesId;

    @SerializedName("protocol_version")
    private int protocolVersion;

    @SerializedName("pack_version")
    private PackVersion packVersion;

    @Data
    public static class PackVersion
    {
        private int resource;

        private int data;
    }

    @SerializedName("build_time")
    private String buildTime;

    @SerializedName("java_component")
    private String javaComponent;

    @SerializedName("java_version")
    private int javaVersion;

    private boolean stable;
}
