package dev.daamky.client.event;

public interface EventListenerProvider<T> {
   EventListener<T>[] getListeners();
}
