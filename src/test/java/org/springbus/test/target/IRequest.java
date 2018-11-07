package org.springbus.test.target;

public interface IRequest<T> {

      T get(String url);
}
