package dev.daamky.client.internal;

public class FriendsStore extends JsonListStore {
   public static final FriendsStore friendsStore = new FriendsStore();

   @Override
   public String getFriends() {
      return "friends";
   }

   public static FriendsStore getInstance() {
      return friendsStore;
   }
}
