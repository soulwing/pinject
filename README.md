Pinject
=======

[![Build Status](https://travis-ci.org/soulwing/pinject.svg?branch=master)](https://travis-ci.org/soulwing/pinject)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.soulwing/pinject/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.soulwing%20a%3Apinject*)

A CDI extension that injects property values into CDI-managed beans.  By
simply adding a `@Property` qualifier to your injection points,
and putting the corresponding property value in a properties file or system
property that can be located by the extension, you can inject properties 
for strings, numbers, URLs, etc.  By providing your own property converters, 
the extension can inject property values of almost any type that has a 
well-defined string representation.  Moreover, by providing your own
custom resolver, you can resolve property values in practically any manner 
that suits the needs of your application. 

Binary Distribution
-------------------

Pinject is available via [Maven Central] 
(http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.soulwing%22). You can
use Pinject in your application by simply setting up your build system (Maven, 
Gradle, Ivy, etc) to include the following dependencies. The syntax shown here
is for Maven, but you can easily adapt this as needed by the build system for
your application.

```
<dependencies>
  ...
  <dependency>
    <groupId>org.soulwing</groupId>
    <artifactId>pinject-api</artifactId>
    <version>1.1.2</version>
  </dependency>
  <dependency>
    <groupId>org.soulwing</groupId>
    <artifactId>pinject-extension</artifactId>
    <version>1.1.2</version>
    <scope>runtime</scope>
  </dependency>
  ...  
</dependencies>
```

Documentation
-------------

There are two main resources for learning and using Pinject.

* [Pinject Wiki] (https://github.com/soulwing/pinject/wiki)
* [API Javadocs] (http://soulwing.github.io/pinject/apidocs/)
