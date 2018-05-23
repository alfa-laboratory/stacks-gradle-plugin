package ru.alfalab.gradle.platform.stack.application;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class StacksApplicationSpringBootExtensionBuildInfoDynamicActionTest {

  @Test
  public void should_put_all_properties_to_buildinfo_map() {
    //given
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("1", "2");
    propertiesMap.put("3", "3");
    StacksApplicationSpringBootExtensionBuildInfoDynamicAction subject = new StacksApplicationSpringBootExtensionBuildInfoDynamicAction(propertiesMap);

    MyBuildInfoHolder o = new MyBuildInfoHolder();

    //when
    subject.execute(o);

    //then
    Map<Object, Object> objectObjectHashMap = o.myBuildInfo.additional;
    Assert.assertEquals(objectObjectHashMap.get("1"), "2");
    Assert.assertEquals(objectObjectHashMap.get("3"), "3");
  }

  public static class MyBuildInfoHolder extends GroovyObjectSupport {
    public MyBuildInfo myBuildInfo;

    public void buildInfo(@DelegatesTo(MyBuildInfo.class) Closure closure) {
      myBuildInfo = new MyBuildInfo();
      closure.setDelegate(Closure.DELEGATE_FIRST);
      closure.setDelegate(myBuildInfo);
      closure.call();
    }
  }

  public static class MyBuildInfo extends GroovyObjectSupport {
    Map<Object, Object> additional = new HashMap<>();

    public void properties(@DelegatesTo(MyBuildInfo.class) Closure closure) {
      closure.call();
    }
  }

}