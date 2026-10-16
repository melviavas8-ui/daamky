package dev.daamky.client.internal;

import java.util.ArrayList;
import java.util.List;

public class IteratorHelper {
   public static final IteratorHelper iteratorHelper = new IteratorHelper();
   public final List<JsonListStore> list = new ArrayList<>();

   public void runNext() {
      this.handleAdd(FriendsStore.getInstance(), StaffStore.getInstance());

      for (JsonListStore var2 : this.list) {
         var2.initInit();
      }
   }

   public void runNext2() {
      for (JsonListStore var2 : this.list) {
         var2.closeClose();
      }
   }

   public void handleAdd(JsonListStore... files) {
      this.list.addAll(List.of(files));
   }

   public List<JsonListStore> getList() {
      return this.list;
   }

   public static IteratorHelper getInstance() {
      return iteratorHelper;
   }
}
