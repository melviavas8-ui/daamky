package dev.daamky.client.internal;

import dev.daamky.client.module.Module;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskScheduler<T> {
   public int intVal = 0;
   public final PriorityQueue<TaskScheduler.Task<T>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(TaskScheduler::lambda$new$0).reversed());

   public void setDeltaTime(int deltaTime) {
      this.intVal += deltaTime;

      while (!this.priorityQueue.isEmpty() && this.priorityQueue.peek().expiresIn <= this.intVal) {
         TaskScheduler.Task var2 = this.priorityQueue.poll();
         var2.run();
      }
   }

   public void addTask(TaskScheduler.Task<T> task) {
      this.priorityQueue.removeIf(TaskScheduler::lambda$addTask$1);
      task.expiresIn = task.expiresIn + this.intVal;
      this.priorityQueue.add(task);
   }

   public void removeTaskByProvider(Module provider) {
      this.priorityQueue.removeIf(TaskScheduler::lambda$removeTaskByProvider$2);
   }

   public T getPeek() {
      while (!this.priorityQueue.isEmpty() && (this.priorityQueue.peek().expiresIn <= this.intVal || !this.priorityQueue.peek().provider.isEnabled())) {
         this.priorityQueue.poll();
      }

      return this.priorityQueue.isEmpty() ? null : this.priorityQueue.peek().value;
   }

   public static boolean lambda$removeTaskByProvider$2(Module provider, TaskScheduler.Task t) {
      return t.provider == provider;
   }

   public static boolean lambda$addTask$1(TaskScheduler.Task task, TaskScheduler.Task t) {
      return t.provider == task.provider;
   }

   public static int lambda$new$0(Object task) {
      return task.priority;
   }

   public static class Task<T> {
      public int expiresIn;
      public final int priority;
      public final Module provider;
      public final T value;

      public Task(int expiresIn, int priority, Module provider, T value) {
         this.expiresIn = expiresIn;
         this.priority = priority;
         this.provider = provider;
         this.value = (T)value;
      }

      public void run() {
      }

      public String getExpires() {
         return "Task(expiresIn=" + this.expiresIn + ", priority=" + this.priority + ", provider=" + this.provider + ", value=" + this.value + ")";
      }
   }
}
