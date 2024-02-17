package edu.brown.cs.student.testHelp;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class BroadbandProxy {
    public static List<List<String>> createProxy(List<List<String>> target) {
        return (List<List<String>>) Proxy.newProxyInstance(
                BroadbandProxy.class.getClassLoader(),
                new Class<?>[] { List.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // Add custom behavior before or after delegating to the target list
                        // For simplicity, this example just delegates all calls directly to the target list
                        return method.invoke(target, args);
                    }
                }
        );
    }
}
