package dev.daamky.client.internal;

public class StaffStore extends JsonListStore {
   public static final StaffStore maubi = new StaffStore();

   @Override
   public String getFriends() {
      return "staffs";
   }

   public static StaffStore getInstance() {
      return maubi;
   }
}
