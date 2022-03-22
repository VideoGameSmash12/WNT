package me.videogamesm12.w95.toolbox;

/*import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.w95.module.WModule;
import net.minecraft.item.ItemStack;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InvalidObjectException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;*/

public class ItemCrypt /*implements WModule*/
{
    /*private Gson gson = new Gson();
    //--
    private KeyGenerator generator;
    private SecretKey key;
    //--
    private ItemCryptConfig config = null;

    public ItemCrypt() throws NoSuchAlgorithmException
    {
        generator = KeyGenerator.getInstance("AES");
        generator.init(256);
    }

    @Override
    public void initialize()
    {
        AutoConfig.register(ItemCryptConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ItemCryptConfig.class).getConfig();
        //--
        key = generator.generateKey();
        key.getEncoded();
    }

    @Override
    public ModuleConfiguration getConfiguration()
    {
        return config;
    }

    public ItemCryptMeta readMeta(ItemStack encrypted) throws IllegalArgumentException, InvalidObjectException,
            JsonSyntaxException
    {
        if (encrypted.getNbt() == null || !encrypted.getNbt().contains("icrypt.meta")
                || !encrypted.getNbt().contains("icrypt.data"))
        {
            throw new IllegalArgumentException();
        }

        String encoded = encrypted.getNbt().getString("icrypt.meta");
        String decoded;

        try
        {
            decoded = Arrays.toString(Base64.getDecoder().decode(encoded.getBytes()));
        }
        catch (Exception ex)
        {
            throw new InvalidObjectException("Not base64");
        }

        return gson.fromJson(decoded, ItemCryptMeta.class);
    }

    public ItemStack encrypt(ItemStack original)
    {
        if (original.getNbt() != null && (original.getNbt().contains("icrypt.meta")
                || original.getNbt().contains("icrypt.data")))
        {
            throw new IllegalArgumentException();
        }
        //--
        String raw = original.getNbt() != null ? original.getNbt().toString() : "{}";
        //--
        encrypted.getNbt()
    }

    public String encryptString()

    @Config(name = "w95-toolbox")
    public static class ItemCryptConfig extends ModuleConfiguration
    {

    }

    public record ItemCryptMeta(String hash, String itemType)
    {
    }*/
}
