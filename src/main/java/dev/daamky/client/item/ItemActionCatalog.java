package dev.daamky.client.item;

import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum ItemActionCatalog {
   POTION_INVIS(Items.POTION, "невидимость", "Зелье невидимости", List.of()),
   POTION_STRENGHT(Items.POTION, "сила", "Зелье силы", List.of()),
   POTION_WIN(Items.POTION, "зелье победителя", "Зелье победителя", List.of()),
   EXPLOSIVE_TRAP(Items.PRISMARINE_SHARD, "взрывная трапка", "Взрывная трапка", List.of()),
   FT_TRAP(Items.POPPED_CHORUS_FRUIT, "трапка", "Фт трапка", List.of()),
   STAN(Items.NETHER_STAR, "стан", "Стан", List.of()),
   SNOWBALL(Items.SNOWBALL, "ком снега", "Ком снега (снежок)", List.of()),
   TNT_A(Items.TNT, "динамит а", "Динамит А", List.of()),
   TNT_B(Items.TNT, "динамит б", "Динамит Б", List.of()),
   TNT_C4(Items.TNT, "динамит с4", "Динамит С4", List.of()),
   TNT_WAVE(Items.TNT, "разрывная волна", "Разрывная волна", List.of()),
   TNT_STEALER(Items.TNT, "стиллер", "Стиллер", List.of()),
   TNT_LUCKSTEALER(Items.TNT, "надёжный стиллер", "Надежный стиллер", List.of()),
   EXP(Items.EXPERIENCE_BOTTLE, "опыт", "Бутылочка с опытом", List.of()),
   EXP_30(Items.EXPERIENCE_BOTTLE, "30", "Бутылочка с 30 ур.", List.of()),
   EXP_50(Items.EXPERIENCE_BOTTLE, "50", "Бутылочка с 50 ур.", List.of()),
   EXP_100(Items.EXPERIENCE_BOTTLE, "100", "Бутылочка с 100 ур.", List.of()),
   CHARKA(Items.ENCHANTED_GOLDEN_APPLE, "зачарованное золотое яблоко", "Зачарованное золотое яблоко", List.of()),
   GOLDENAPPLE(Items.GOLDEN_APPLE, "золотое яблоко", "Золотое яблоко", List.of()),
   GOLDENCARROT(Items.GOLDEN_CARROT, "золотая морковь", "Золотая морковь", List.of()),
   HELMET_SUN(Items.GOLDEN_HELMET, "шлем солнца", "Шлем солнца", List.of()),
   HELMET_ETERNITY(Items.NETHERITE_HELMET, "шлем eternity", "Шлем етернити", List.of()),
   HELMET_INFINITY(Items.NETHERITE_HELMET, "шлем infinity", "Шлем инфинити", List.of()),
   CHESTPLATE_ETERNITY(Items.NETHERITE_CHESTPLATE, "нагрудник eternity", "Нагрудник етернити", List.of()),
   CHESTPLATE_INFINITY(Items.NETHERITE_CHESTPLATE, "нагрудник infinity", "Нагрудник инфинити", List.of()),
   LEGGINGS_ETERNITY(Items.NETHERITE_LEGGINGS, "штаны eternity", "Поножи етернити", List.of()),
   LEGGINGS_INFINITY(Items.NETHERITE_LEGGINGS, "штаны infinity", "Поножи инфинити", List.of()),
   BOOTS_ETERNITY(Items.NETHERITE_BOOTS, "ботинки eternity", "Ботинки етернити", List.of()),
   BOOTS_INFINITY(Items.NETHERITE_BOOTS, "ботинки infinity", "Ботинки инфинити", List.of()),
   SWORD_ETERNITY(Items.NETHERITE_SWORD, "меч eternity", "Меч етернити", List.of()),
   PICKAXE_ETERNITY(Items.NETHERITE_PICKAXE, "кирка eternity", "Кирка етернити", List.of()),
   PICKAXE_STINGER(Items.NETHERITE_PICKAXE, "кирка stinger", "Кирка стингера", List.of()),
   SPHERE_CERBER(Items.PLAYER_HEAD, "сфера цербера", "Сфера цербера", List.of()),
   SPHERE_FLESHA(Items.PLAYER_HEAD, "сфера флеша", "Сфера флеша", List.of()),
   FRAGMENT(Items.PRISMARINE_CRYSTALS, "боевые фрагменты", "Боевые фрагменты", List.of()),
   SPAWNER(Items.SPAWNER, "Рассадник", "Спавнер", List.of()),
   GUNPOWDER_BLOCK(Items.CLAY, "взрывчатое вещество", "Взрывчатое вещество (блок пороха)", List.of()),
   BACKPACK_4LVL(Items.MAGENTA_SHULKER_BOX, "рюкзак 4 уровень", "Рюкзак (4 ур.)", List.of()),
   ELYTRA(Items.ELYTRA, "элитры", "Элитры", List.of()),
   UNBREAKING_ELYTRA(Items.ELYTRA, "нерушимые элитры", "Нерушимая элитра", List.of()),
   ARMOR_ELYTRA(Items.ELYTRA, "броневая элитра", "Броневая элитра", List.of()),
   MYTHIC_DAMAGE3(Items.PLAYER_HEAD, "сфера на урон 3", "Мифическая сфера (у3 / б2)", List.of()),
   MYTHIC_ARMOR3(Items.PLAYER_HEAD, "сфера на броня 3", "Мифическая сфера (б3 / у2)", List.of()),
   MYTHIC_SPEED2(Items.PLAYER_HEAD, "сфера на скорость 2", "Мифическая сфера (б3 / с2)", List.of()),
   SPHERE_SHARD(Items.PLAYER_HEAD, "осколок сферы", "Осколок сферы", List.of()),
   SHIP_KEY_1LVL(Items.TRIPWIRE_HOOK, "отмычка", "Отмычка (удача 1)", List.of()),
   SHIP_KEY_2LVL(Items.TRIPWIRE_HOOK, "отмычка", "Отмычка (удача 2)", List.of()),
   SHIP_KEY_SECRET(Items.TRIPWIRE_HOOK, "отмычка", "Отмычка (секретная)", List.of()),
   TOTEM(Items.TOTEM_OF_UNDYING, "тотем бессмертия", "Тотем", List.of()),
   TALISMAN_STINGER(Items.TOTEM_OF_UNDYING, "талисман stinger", "Талисман стингера", List.of()),
   TALISMAN_ETERNITY(Items.TOTEM_OF_UNDYING, "талисман eternity", "Талисман етернити", List.of()),
   TALISMAN_INFINITY(Items.TOTEM_OF_UNDYING, "талисман infinity", "Талисман инфинити", List.of());

   public final Item itemType;
   public final String searchString;
   public final String name;
   public final List<String> tags;

   public Item getItemType() {
      return this.itemType;
   }

   public String getSearchString() {
      return this.searchString;
   }

   public String getName() {
      return this.name;
   }

   public List<String> getTags() {
      return this.tags;
   }

   private ItemActionCatalog(Item itemType, String searchString, String name, List<String> tags) {
      this.itemType = itemType;
      this.searchString = searchString;
      this.name = name;
      this.tags = tags;
   }

   public static ItemActionCatalog[] $values() {
      return new ItemActionCatalog[]{
         POTION_INVIS,
         POTION_STRENGHT,
         POTION_WIN,
         EXPLOSIVE_TRAP,
         FT_TRAP,
         STAN,
         SNOWBALL,
         TNT_A,
         TNT_B,
         TNT_C4,
         TNT_WAVE,
         TNT_STEALER,
         TNT_LUCKSTEALER,
         EXP,
         EXP_30,
         EXP_50,
         EXP_100,
         CHARKA,
         GOLDENAPPLE,
         GOLDENCARROT,
         HELMET_SUN,
         HELMET_ETERNITY,
         HELMET_INFINITY,
         CHESTPLATE_ETERNITY,
         CHESTPLATE_INFINITY,
         LEGGINGS_ETERNITY,
         LEGGINGS_INFINITY,
         BOOTS_ETERNITY,
         BOOTS_INFINITY,
         SWORD_ETERNITY,
         PICKAXE_ETERNITY,
         PICKAXE_STINGER,
         SPHERE_CERBER,
         SPHERE_FLESHA,
         FRAGMENT,
         SPAWNER,
         GUNPOWDER_BLOCK,
         BACKPACK_4LVL,
         ELYTRA,
         UNBREAKING_ELYTRA,
         ARMOR_ELYTRA,
         MYTHIC_DAMAGE3,
         MYTHIC_ARMOR3,
         MYTHIC_SPEED2,
         SPHERE_SHARD,
         SHIP_KEY_1LVL,
         SHIP_KEY_2LVL,
         SHIP_KEY_SECRET,
         TOTEM,
         TALISMAN_STINGER,
         TALISMAN_ETERNITY,
         TALISMAN_INFINITY
      };
   }
}
