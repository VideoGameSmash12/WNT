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

package me.videogamesm12.wnt.util;

import com.google.gson.Gson;
import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.data.MinecraftVersion;
import net.minecraft.client.MinecraftClient;

import java.io.InputStreamReader;
import java.util.Objects;

/**
 * <h1>VersionFetcher</h1>
 * <p>Version-independent way of retrieving the current game version.</p>
 */
public class VersionFetcher
{
    private static final Gson gson = new Gson();

    @Getter
    private static final MinecraftVersion version;

    static
    {
        try
        {
            version = gson.fromJson(new InputStreamReader(Objects.requireNonNull(MinecraftClient.class.getClassLoader().getResourceAsStream("version.json"))), MinecraftVersion.class);
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Unable to get the version information, this is extremely likely to cause stability issues.", ex);
            throw ex;
        }
    }
}
