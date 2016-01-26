package com.minotaur.cache;

/**
 * Created by minotaur on 15/11/17.
 */
public interface CacheDemo {

    public User intUser();

    public User changeUser();

    public User changeUserAge(int age);

    public User getUser(User user);

    public User getUser(String name);

    public User getUser(String name, int age);

    public User getUserThrowEx(String name);

    public User getUserDefaultKey(User user);

    public User getUserNull(String name);

    public User clearUser(String name);

    public User getUser();

    public User getUserMock(int num);

    public User getUserWithoutAnno(int num);

    public User getUserInMem(String name);
}
