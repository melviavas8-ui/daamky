package dev.daamky.client.internal;

import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;

public class FloatValueHelper {
   public static final FloatValueHelper floatValueHelper = new FloatValueHelper();
   public final TaskScheduler<Float> taskScheduler = new TaskScheduler<>();

   public float getFloat() {
      return this.taskScheduler.getPeek() != null ? this.taskScheduler.getPeek() : 1.0F;
   }

   public void initAdd(float timer, Priority taskPriority, Module provider, int ticks) {
      this.taskScheduler.addTask(new TaskScheduler.Task<>(ticks, taskPriority.getPriority(), provider, timer));
   }

   public TaskScheduler<Float> getTaskScheduler() {
      return this.taskScheduler;
   }

   public static FloatValueHelper getInstance() {
      return floatValueHelper;
   }
}
