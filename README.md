Pinject
=======

A CDI extension that injects property values into CDI-managed beans.  By
simply adding a `@Property` qualifier to your injection points,
and putting the corresponding property value in a properties file or system
property that can be located by the extension, you can inject properties 
for strings, numbers, URLs, etc.  By providing your own property converters, 
the extension can inject property values of almost any type that has a 
well-defined string representation.  Moreover, by providing your own
custom resolver, you can resolve property values in practically any manner 
that suits the needs of your application. 


Using @Property
---------------

Apply the `org.soulwing.cdi.properties.@Property` qualifier along with 
`@Inject` on fields, methods, constructors, and producer parameters wherever
you want a property value injected.

By default, the fully-qualified name of the annotated member is the name of
the corresponding property value that will resolved (as described below) and
injected.  For example:

```
package org.example;

import javax.inject.Inject;
import org.soulwing.cdi.properties.Property;

public class MyBean {

  @Inject @Property
  private URL location;
  
  @Inject @Property(name = 'uniqueIdentifier', value = '42')
  private long identifier;

}
```

The `@Property` annotations on this bean will instruct the extension to
resolve a property named `org.example.MyBean.location` and inject it into the
`location` field.  It will also resolve a property named `uniqueIdentifier`
(note that the `name` attribute overrides the default property name) and 
inject it the `identifier` field.  If the `uniqueIdentifier` property cannot
be resolved, the value 42 will be used by default.  Since the `location`
property does not have a default value, CDI bean resolution will fail if the
corresponding property cannot be resolved.

Using the built-in capabilities of the extension, you can inject values into
the bean shown in the above example by creating a `META-INF/beans.properties`
file:

```
org.example.MyBean.location=http://www.google.com
uniqueIdentifier=42
```

If you want to customize how property values are resolved, read the section
on Property Resolution, below.  To learn more about supported types for 
property injection and how to provide converters for your own value types,
read Supported Types, below.


Property Resolution
-------------------

Property resolution is the process by which property names are resolved into
the corresponding (string) property value.  The extension includes built-in 
support for resolving property names using system properties, 
`beans.properties` resources on the classpath, and (if running in a Java EE
or Servlet container) `beans.properties` resources at arbitrary locations
specified via a JNDI environment variable.  Each `beans.properties` resource 
is a file in the format produced by `java.util.Properties`.

#### Placing `beans.properties` Resources on the Classpath

The built-in property resolution takes advantage of the fully-qualified 
inject point names that are used as the default property names wherever
`@Property` is applied, allowing properties to be assembled in `beans.properties`
file at any level of the package hierarchy that makes sense for your needs.
The following example illustrates the concept.

Suppose our application has a couple of beans defined as follows:

```
package org.example.illustrator;

import javax.inject.Inject
import javax.enterprise.context.ApplicationScoped
import org.soulwing.cdi.properties.Property

@ApplicationScoped
public class ApplicationConfig {

  @Inject @Property private String emailAddress;
  
  @Inject @Property private int maxConcurrentUsers;
  
  // ...
}
```

```
package org.example.illustrator.http;

import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.soulwing.cdi.properties.Property;

@SessionScoped
public class RestClientBean {

  @Inject @Property private URL location;
  
  @Inject @Property private String username;
  
  @Inject @Property private String password;
  
  // ...
}
```

We could simply create a `beans.properties` file at the root of the classpath
(or as `META-INF/beans.properties`) and define our properties with the fully
qualified names of our property injection points, like this:

```
org.example.illustrator.ApplicationConfig.emailAddress=help@org.example
org.example.illustrator.ApplicationConfig.maxConcurrentUsers=100
org.example.illustrator.http.RestClientBean.location=http://internal.example.org/appws
org.example.illustrator.http.RestClientBean.username=illustrator
org.example.illustrator.http.RestClientBean.password=s3kr3t
```

While this approach is probably fine for an application with a small number 
of configuration properties, using a flat file and fully qualified names is 
a bit too unwieldy and repetitive when defining and organizing a large number 
of configuration properties.

One immediate improvement we could make would be to put `beans.properties`
files in the corresponding packages on the classpath.  If we did this, we 
would have two files -- one located at `org/example/illustrator/beans.properties`
containing the properties for the `ApplicationConfig` bean:

```
ApplicationConfig.emailAddress=help@org.example
ApplicationConfig.maxConcurrentUsers=100
```

The other file would be located at 
`org/example/illustrator/http/beans.properties` and would contain the 
properties for the `RestClientBean`:

```
RestClientBean.location=http://internal.example.org/appws
RestClientBean.username=illustrator
RestClientBean.password=s3kr3t
```

For an application this small, having the configuration files in two separate
files might be a bit too sophisticated.  But we'd still like to avoid the
long property names we saw in the first approach.  We could instead put
one file at `org/example/illustrator/beans.properties` and put all of the
properties in it, like this:

```
ApplicationConfig.emailAddress=help@org.example
ApplicationConfig.maxConcurrentUsers=100
http.RestClientBean.location=http://internal.example.org/appws
http.RestClientBean.username=illustrator
http.RestClientBean.password=s3kr3t
```

Note how we can just prepend the property names for the `RestClientBean`
with the subpackage name. 

### Overriding Properties during Test Execution

Properties placed on the classpath in `META-INF/beans.properties` override those
specified elsewhere on the classpath.  This allows you to easily target
properties whose values you want to override during tests by putting their 
fully-qualified names in a `META-INF/beans.properties` file with your test 
resources.  For example, in a Maven project you could put your overrides in 
`src/test/resources/META-INF/beans.properties`.

### Overriding Properties in Java EE and Web Applications

For applications deployed in a Java EE or Servlet container, it is often
desirable to override the property values that would be injected with 
properties loaded from resources in the deployment artifact (e.g. properties
files bundled in the Web Archive (WAR) file) with properties that are 
specific to the deployment environment.  For example, we might use different
properties for a production server than we would for a development or
pre-production server.

If the JNDI environment variable `java:comp/env/beans.properties.location`
exists and is of string type, the extension will treat it as a space- or
comma-delimited list of URLs to properties files that will be used, in the 
order specified, to resolve property values. 

If the JNDI environment variable `java:comp/env/beans.properties.root`
exists and is of string type, the extension will treat it as a base URL for
resolving properties from `beans.properties` files on the runtime classpath.
This mechanism allows you to provide a hierarchy of `beans.properties` files
based on package names, outside of the runtime classpath.


### Resolution Order

Property values are resolved by the built-in resolvers in the following order.

1.  System properties set using `java.lang.System.setProperty` or by using
    `-Dname=value` arguments when starting the JRE.
2.  If running in a Java EE or Servlet container, properties defined in 
    properties files located using the URL(s) specified by the 
    `java:comp/env/beans.properties.location` JNDI environment setting.
3.  If running in a Java EE or Servlet container, `beans.properties` defined in 
    properties files located by searching the namespace rooted by the URL
    specified by the `java:comp/env/beans.properties.root` JNDI environment 
    setting.  The search is conducted in the same manner as when searching 
    the classpath.
5.  All properties files at the root of the classpath with the name
    `META-INF/beans.properties`.  The order in which these properties files 
    are consulted is arbitrary (due to inherent limitations of the classloader
    mechanism) so you should not rely on the order in which these files are 
    evaluated when using this mechanism.  
6.  All properties files on the classpath named `beans.properties`, located
    by considering the property name as a package qualified name. The order in 
    which properties files in a given package will be consulted is arbitrary 
    (due to inherent limitations of the classloader mechanism) so you should 
    not rely on the order in which these files are evaluated when using this 
    mechanism.

Resolution stops with the first resolver that provides a value for the named
property.  If no value is resolved, the `value` attribute specified on
the `@Property` qualifier is used as a last resort default.  If no value is 
resolved and the qualifier does not specify a default, the injection process
will stop with an error indicating that the property value could not be
resolved.


Supported Types
---------------

The extension has built-in support for injecting property values into
injection points with any of the following types:

* `java.lang.String`
* All Java primitives (e.g. `int`), and associated wrapper types
* All enumeration types
* `java.util.Calendar` and `java.util.Date` (along with its SQL subtypes) --
  the default format is the full ISO 8601 format with time zone, but you can 
  use any pattern supported by `java.util.SimpleDateFormat` by configuring the 
  `org.soulwing.cdi.properties.converters.DatePropertyConverter.pattern`
  property.    
* `java.net.URL` -- includes support for the `classpath:` pseudo-scheme 
  inspired by the Spring Framework
* `java.net.URI`
* `javax.mail.InternetAddress` -- optional; you must include JavaMail on your 
  classpath


Extending Pinject
-----------------

Pinject is easily extended to meet the specific needs of your application.
You can provide your own resolvers to handle property resolution from
resources other than simple properties files, and you can provide your own
converters to handle injection point data types that are not among those
handled by the built-in converters. 

Your extensions are loaded by Pinject using the `ServiceLoader` mechanism
introduced in JDK 6.

#### Custom Resolvers

You can augment the built-in property resolvers by supplying your own.

1. Create a class that implements 
    `org.soulwing.cdi.properties.spi.PropertyResolver`.
2. Place a file in `META-INF/services` on the classpath with the name
   `org.soulwing.cdi.properties.spi.PropertyResolver` and add the 
   fully-qualified class name(s) of your resolvers(s), one per line.
   
Each resolver has a priority that determines the order in which resolvers
will be consulted during property name resolution -- resolvers with larger 
numeric values of priority will be consulted before those with smaller values.  

The built-in resolvers have priority values between -10 and -1.  Your 
resolver can use any positive value of priority to override the built-in 
resolvers.  If your resolver uses a value less than -10 it will be consulted 
*after* the built-in resolvers, but *before* the last resort use of the
default value specified on the `@Property` qualifier.

The `init` and `destroy` lifecycle methods of a resolver should be used to
establish and teardown, respectively, whatever resources (e.g. database
connections) your resolver implementation may require.

Unfortunately, because your resolver will be participating in the processes
that support bean creation and dependency injection, your resolver cannot be
designed to use the facilities of CDI in its own implementation.

#### Custom Converters

Other value types with well-defined string representations are easily 
supported by defining your own converter extension.

1. Create a class that implements 
   `org.soulwing.cdi.properties.spi.PropertyConverter`.
2. Place a file in `META-INF/services` on the classpath with the name
   `org.soulwing.cdi.properties.spi.PropertyConverter` and add the 
   fully-qualified class name(s) of your converter(s), one per line.
   
Converters are consulted (in an arbitrary order) until one is found which
claims to support the type of the injection target.  

A given converter can support more than one type -- the target type of the 
injection point is provided as context when the converter is called upon to
converter a property value.  The context also provides the means to invoke
other converters, which is useful when converting compound types.

Every converter has a name.  The names of the converters included with 
extension are set to the fully qualified converter class name.  You can
request that an injection point use a specific converter (by name) using the
`converter` attribute of the `@Property` qualifier.  This is useful if you 
want to override one of the built-in converters in some specific case(s).

Unfortunately, because your converter will be participating in the processes
that support bean creation and dependency injection, your converter cannot be
designed to use the facilities of CDI in its own implementation.

#### Optional Resolvers and Converters

The SPI provides the `Optional` interface which can be implemented by resolver 
and converter extensions that depend on libraries or other resources which
may not always be available in the runtime environment.  A resolver or
converter that implements this interface is given an opportunity to inspect
the runtime environment to determine whether it can be used successfully.

Before initializing an `Optional` resolver or converter, Pinject invokes the
`isAvailable` method to allow your extension to check whether its runtime
dependencies can be satisified.  If and only if your extension returns
`true`, it is subsequently initialized and used by Pinject to resolve or
convert property values.


Theory of Operation
-------------------

The extension uses CDI's bean discovery events to participate in the 
dependency injection process.  For each injection point that is qualified
with `@Property`, the extension resolves the corresponding property name to
a string value, and converts the string representation to an instance of the
target type of the injection point.

CDI dependency injection is based on the injection point type, with additional 
hints provided by qualifiers. All injection points of a given type with the 
same qualifiers are considered equivalent -- i.e. the same bean would be 
injected into each such injection point.  This appears to create a problem, 
since a typical property injection point might look like this:

```
@Inject @Property
private String myProperty;
@Inject @Property
private String myOtherProperty;
```

How will CDI be able to distinguish beans of type String that are created by
the extension?  Without further qualification, both `myProperty` and
`myOtherProperty` will be injected with the same bean of type String.

The extension solves this problem by dynamically providing a unique qualifier
to each injection point qualified by `@Property`.  It assigns this same unique
qualifier for the bean that represents the resolved property value.  In this 
way, as far as CDI is concerned, each property value is uniquely qualified 
for injection into the targeted injection point. 

