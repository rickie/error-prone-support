---
title: EmptyMethod
name: EmptyMethod
summary: "Empty method can likely be deleted"
severity: SUGGESTION
tags:
- Simplification
source: error-prone-contrib/src/main/java/tech/picnic/errorprone/bugpatterns/EmptyMethod.java
identification:
- |4
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
- |4
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
replacement:
- |4
     final class A {
    -  void instanceMethod() {}
    -
    -  static void staticMethod() {}
    -
       static void staticMethodWithComment() {
         /* Foo. */
       }
     }
---
