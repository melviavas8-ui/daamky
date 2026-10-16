package dev.daamky.client.event;

public class PostMotionEvent extends Event<PostMotionEvent.EventPostMotionData> {
   public static final PostMotionEvent postMotionEvent = new PostMotionEvent();
   public static final PostMotionEvent.EventPostMotionData eventPostMotionData = new PostMotionEvent.EventPostMotionData();

   public static PostMotionEvent getInstance() {
      return postMotionEvent;
   }

   public static PostMotionEvent.EventPostMotionData getEventPostMotionData() {
      return eventPostMotionData;
   }

   public static class EventPostMotionData {
   }
}
