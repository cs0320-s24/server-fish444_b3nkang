package edu.brown.cs.student.Proxys;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jetbrains.annotations.NotNull;

public class BroadbandProxy implements List<List<String>> {

  /** user Implement other custom methods as needed */
  private List<List<String>> target;

  public BroadbandProxy(List<List<String>> target) {
    this.target = target;
  }

  // Implement List interface methods
  @Override
  public int size() {
    return target.size();
  }

  @Override
  public boolean isEmpty() {
    return target.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return this.contains(o);
  }

  @NotNull
  @Override
  public Iterator<List<String>> iterator() {
    return this.target.iterator();
  }

  @NotNull
  @Override
  public Object[] toArray() {
    return new Object[0];
  }

  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] a) {
    return this.target.toArray(a);
  }

  @Override
  public boolean add(List<String> strings) {
    return this.target.add(strings);
  }

  @Override
  public boolean remove(Object o) {

    return this.target.remove(o);
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    return this.target.contains(c);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends List<String>> c) {
    return this.target.addAll(c);
  }

  @Override
  /** implement how you want */
  public boolean addAll(int index, @NotNull Collection<? extends List<String>> c) {
    return false;
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    return this.target.removeAll(c);
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    return this.target.retainAll(c);
  }

  @Override
  public void clear() {
    this.target.clear();
  }

  @Override
  public List<String> get(int index) {
    return this.target.get(index);
  }

  @Override
  public List<String> set(int index, List<String> element) {
    return this.set(index, element);
  }

  @Override
  public void add(int index, List<String> element) {
    this.target.add(index, element);
  }

  @Override
  public List<String> remove(int index) {
    return this.target.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return this.target.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return this.target.lastIndexOf(o);
  }

  @NotNull
  @Override
  public ListIterator<List<String>> listIterator() {
    return null;
  }

  @NotNull
  @Override
  public ListIterator<List<String>> listIterator(int index) {
    return this.target.listIterator();
  }

  @NotNull
  @Override
  public List<List<String>> subList(int fromIndex, int toIndex) {
    return this.target.subList(fromIndex, toIndex);
  }

  // Implement other List interface methods as needed
  // Additional methods for proxy behavior
  public void addRow(List<String> row) {
    // Add additional behavior before or after delegating to the target list
    this.target.add(row);
  }

  public List<String> getRow(int index) {
    // Add additional behavior before or after delegating to the target list
    return this.target.get(index);
  }

  @Override
  public String toString() {
    return this.target.toString();
  }
}
