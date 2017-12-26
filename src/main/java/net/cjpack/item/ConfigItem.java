package net.cjpack.item;

import net.cjpack.CustomThingies;
import net.cjpack.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class ConfigItem extends Item {

    public static CreativeTabs tab = new CreativeTabs("ct_items") {
        @Override
        public Item getTabIconItem() {
            return Items.NETHER_STAR;
        }
    };

    public static List<ConfigItem> items = new ArrayList<>();

    public List<String> mods;
    public ConfigCategory c;

    public ConfigItem(ConfigCategory c) {
        this.c = c;
        this.setUnlocalizedName(c.get("unlocalizedName").getString());
        this.setRegistryName(new ResourceLocation(Reference.MODID, c.get("registryName").getString()));
        this.setCreativeTab(c.get("tab") != null ? getTabByName(c.get("tab").getString()) : tab);
        this.setMaxDamage(c.get("durability") != null ? c.get("durability").getInt() : 1);
        this.setMaxStackSize(c.get("size") != null ? c.get("size").getInt() : 64);
        this.mods = c.get("mods") != null ? Arrays.asList(c.get("mods").getStringList()) : new ArrayList<>();
        this.canRepair = mods.contains("repair");
        items.add(this);
    }

    public static CreativeTabs getTabByName(String name) {
        for (CreativeTabs ct : CreativeTabs.CREATIVE_TAB_ARRAY) {
            if (ct.getTabLabel().equalsIgnoreCase(name)) return ct;
        }
        return tab;
    }

    public boolean isDamageable() {
        return this.getMaxDamage() > 0 && (!this.hasSubtypes || this.maxStackSize == 1) && mods.contains("damage");
    }

    public static void loadItems() {
        CustomThingies.i.getCategoryNames().forEach(c -> new ConfigItem(CustomThingies.i.getCategory(c)));
    }

    public boolean hasEffect(ItemStack stack) {
        return mods.contains("effect");
    }

    public boolean isBeaconPayment(ItemStack stack) {
        return mods.contains("beacon");
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return mods.contains("enchant");
    }
}
