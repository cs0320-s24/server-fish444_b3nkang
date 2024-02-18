package edu.brown.cs.student.Proxys;
/**
 * import java.lang.reflect.InvocationHandler; import java.lang.reflect.Method; import
 * java.lang.reflect.Proxy; import java.util.List;
 *
 * <p>public class BroadbandProxy { public static List<List<String>> createProxy(List<List<String>>
 * target) { return (List<List<String>>)
 * Proxy.newProxyInstance(BroadbandProxy.class.getClassLoader(), new Class<?>[] { List.class }, new
 * InvocationHandler() { @Override public Object invoke(Object proxy, Method method, Object[] args)
 * throws Throwable { // Add custom behavior before or after delegating to the target list // For
 * simplicity, this example just delegates all calls directly to the target list return
 * method.invoke(target, args); } }); } }
 */

/** chatGPT helped with implementation of the dynamic proxy */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class BroadbandDynamicProxy {
  @SuppressWarnings("unchecked")
  public static List<List<String>> createProxy(List<List<String>> target) {
    return (List<List<String>>)
        Proxy.newProxyInstance(
            BroadbandDynamicProxy.class.getClassLoader(),
            new Class<?>[] {List.class},
            new InvocationHandler() {
              @Override
              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("get")) {
                  // Intercepting the "get" method
                  System.out.println("Intercepted the 'get' method");
                  // For simplicity, returning null here; you should provide the actual
                  // implementation
                  return target.get(proxy.hashCode());
                } else {
                  // For other methods, delegate to the target list
                  return method.invoke(target, args);
                }
              }
            });
  }

  public static void main(String[] args) {
    // Create a list of lists
    List<List<String>> originalList =
        List.of(
            List.of("apple", "banana", "orange"),
            List.of("cat", "dog", "fish"),
            List.of("red", "green", "blue"));

    // Create a proxy for the list of lists
    List<List<String>> proxy = createProxy(originalList);

    // Test the proxy
    System.out.println(proxy.get(0)); // This will trigger the "get" method and print the message
    System.out.println(proxy.size()); // This will not be intercepted

    // You should provide the actual implementation for the "get" method
  }
}
