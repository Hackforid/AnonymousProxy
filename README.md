# AnonymousProxy

Never NPE check, Never null interface.



## Why

Without optional syntactic sugar like Kotlin, we always need to check whether OBJ is null in Java.

For example in MVP, we detach view when Activity is destoryed, so the ugly code is:

```java
interface View {
    void foo();
}

class Presenter {
    void callView() {
    	if (view != null) {
    		view.foo()
		}
    }
}

```

Why we need waste time everywhere to avoid null in F#*king Java,  Why can't we do it automatic and go somewhere to have a glass of beer？

**Don't worry**, here is the solution.



## Usage

```java
// example interface
interface View {
    void foo();
}

// create a proxy
AnonymousProxy<View> proxy = new AnonymousProxy(View.class);

// this proxy will generate a fake implement of your interface. It will do nothing, but give you a no-null object. So, you will never need check whether it's null or safe.
proxy.get().foo(); 

// you can set the actual implement to it
proxy.set(viewInstance);
proxy.get() == viewInstance;
```

### What Empty Implement is ?

AnonymousProxy use Dynamic Proxy to create a Empty implement of your interface, so when get object from it, you will never get null (except Exception).

So what will Empty Implement do?

```java
interface Example {
    void voidMethod();
    int numberMethod();
    char charMethod();
    boolean booleanMethod();
    Object objectMethod();
}

// clear result 
proxy.get().voidMethod() == void;
proxy.get().numberMethod() == 0;
proxy.get().charMethod() == '0';
proxy.get().booleanMethod() == false;
proxy.get().objectMethod() == null;
```

#### Java 8 or Kotlin default method ???

Sorry,  Because of the limit of AndroidSDK, it can work right below version 24.

### Perfomance

To improve the performance of reflection, Anonymous save every Empty implement it created as cache in an ArrayMap.  So when LOW MEMORY happened, you can free them manual.

```java
class Application {
   void onLowMemory() {
       AnonymousProxy.free();
   }
}
```





License
=======

    Copyright 2017 Kleist Zhou

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.