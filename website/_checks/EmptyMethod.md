---
title: EmptyMethod
name: EmptyMethod
summary: Empty method can likely be deleted
severity: SUGGESTION
tags:
- Simplification
source: /home/sschroevers/workspace/picnic/error-prone-support/error-prone-contrib/src/main/java/tech/picnic/errorprone/bugpatterns/EmptyMethod.java
identification:
- |
  class A {
    Object m1() {
      return null;
    }

    void m2() {
      System.out.println(42);
    }

    void m3() {}

    // BUG: Diagnostic contains:
    static void m4() {}

    interface F {
      void fun();
    }

    final class MyTestClass {
      void helperMethod() {}
    }
  }
- |
  import org.aspectj.lang.annotation.Pointcut;

  final class B implements A.F {
    @Override
    public void fun() {}

    // BUG: Diagnostic contains:
    void m3() {}

    /** Javadoc. */
    // BUG: Diagnostic contains:
    void m4() {}

    void m5() {
      // Single-line comment.
    }

    void m6() {
      /* Multi-line comment. */
    }

    @Pointcut
    void m7() {}
  }
replacement:
- " final class A {\n-  void instanceMethod() {}\n-\n-  static void staticMethod()\
  \ {}\n-\n   static void staticMethodWithComment() {\n     /* Foo. */\n   }\n }\n\
  \ "
---
