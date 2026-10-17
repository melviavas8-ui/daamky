package dev.daamky.client.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ArrayBackedIteratorHelper<T> implements List<T> {
   public final T[] array;

   public ArrayBackedIteratorHelper(T[] array) {
      this.array = (T[])array;
   }

   public ArrayBackedIteratorHelper(Collection<T> collection) {
      this((T[])collection.toArray());
   }

   public static <T> ArrayBackedIteratorHelper<T> copy(T[] array) {
      return new ArrayBackedIteratorHelper<>((T[])Arrays.copyOf(array, array.length));
   }

   @Override
   public int size() {
      return this.array.length;
   }

   @Override
   public boolean isEmpty() {
      return this.array.length == 0;
   }

   @Override
   public boolean contains(Object o) {
      for (Object var5 : this.array) {
         if (o == var5) {
            return true;
         }
      }

      return false;
   }

   @Override
   public Iterator<T> iterator() {
      return new ArrayBackedIteratorHelper.ArrayBackedIterator<>(this.array, 0);
   }

   @Override
   public Object[] toArray() {
      return Arrays.copyOf(this.array, this.array.length);
   }

   @Override
   public <T1> T1[] toArray(T1[] a) {
      return (T1[])this.toArray();
   }

   @Override
   public boolean remove(Object o) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      for (Object var3 : c) {
         if (!this.contains(var3)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean addAll(Collection<? extends T> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean addAll(int index, Collection<? extends T> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void clear() {
      throw new UnsupportedOperationException();
   }

   @Override
   public T get(int index) {
      return this.array[index];
   }

   @Override
   public T set(int index, T element) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void add(int index, T element) {
      throw new UnsupportedOperationException();
   }

   @Override
   public T remove(int index) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int indexOf(Object o) {
      for (int var2 = 0; var2 < this.array.length; var2++) {
         if (o == this.array[var2]) {
            return var2;
         }
      }

      return -1;
   }

   @Override
   public int lastIndexOf(Object o) {
      for (int var2 = this.array.length - 1; var2 >= 0; var2--) {
         if (o == this.array[var2]) {
            return var2;
         }
      }

      return -1;
   }

   @Override
   public ListIterator<T> listIterator() {
      return new ArrayBackedIteratorHelper.ArrayBackedIterator<>(this.array, 0);
   }

   @Override
   public ListIterator<T> listIterator(int index) {
      return new ArrayBackedIteratorHelper.ArrayBackedIterator<>(this.array, index);
   }

   @Override
   public List<T> subList(int fromIndex, int toIndex) {
      throw new UnsupportedOperationException();
   }

   private static final class ArrayBackedIterator<T> implements ListIterator<T> {
      public final T[] array;
      public int initialCursor;

      public ArrayBackedIterator(T[] array, int initialCursor) {
         this.array = (T[])array;
         this.initialCursor = initialCursor;
      }

      @Override
      public boolean hasNext() {
         return this.initialCursor < this.array.length;
      }

      @Override
      public boolean hasPrevious() {
         return this.initialCursor > 0;
      }

      @Override
      public T next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.initialCursor++];
         }
      }

      @Override
      public T previous() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return this.array[--this.initialCursor];
         }
      }

      @Override
      public int nextIndex() {
         return this.initialCursor;
      }

      @Override
      public int previousIndex() {
         return this.initialCursor - 1;
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }

      @Override
      public void set(T t) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void add(T t) {
         throw new UnsupportedOperationException();
      }
   }
}
