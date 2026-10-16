package dev.daamky.client.internal;

public class Pair<A, B> {
   public final A left;
   public final B right;

   public Pair(A left, B right) {
      this.left = (A)leftx;
      this.right = (B)rightx;
   }

   public A getLeft() {
      return this.left;
   }

   public B getRight() {
      return this.right;
   }
}
