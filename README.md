cdi-property-injection
======================

A CDI extension that injects property values into CDI-managed beans.  By
simply adding a `@Property` qualifier to your injection points,
and putting the corresponding property value in a properties file that can be
located by the extension, you can inject properties for strings, numbers, 
URLs, etc.  By providing your own property converters, the extension can 
inject property values into almost any type that has a well-defined string
representation.


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
support for resolving property names by consulting each of three sources for
resolution, in the order in which they appear below.

1.  System properties set using `java.lang.System.setProperty` or by using
    `-Dname=value` arguments when starting the JRE.
2.  If running in a Java EE or Servlet container, the 
    `java:comp/env/beans.properties.location` JNDI environment string can be
    set to a space- or comma- delimited list of URLs (including `classpath:`
    URLs) to properties files that will be used, in the order specified, to
    resolve property values.
3.  All properties files on the classpath named `META-INF/beans.properties` 
    The order in which these properties files will be consulted is arbitrary 
    (due to inherent limitations of the classloader mechanism) so you should 
    not rely on the order in which these files are evaluated when using this 
    mechanism.

Resolution stops with the first resolver that provides a value for the named
property.  If no value is resolved, the `value` attribute specified on
the `@Property` qualifier is used as a last resort default.  If no value is 
resolved and the qualifier does not specify a default, the injection process
will stop with an error indicating that the property value could not be
resolved.


#### Custom Resolvers

You can augment the built-in property resolvers by supplying your own.

1. Create a class that implements 
    `org.soulwing.cdi.properties.spi.PropertyResolver`.
2. Place a file in `META-INF/services` on the classpath with the name
   `org.soulwing.cdi.properties.spi.PropertyResolver` and add the 
   fully-qualified class name(s) of your resolvers(s), one per line.
   
Each resolver has a priority that determines the order in which resolvers
will be consulted during property name resolution -- larger numeric values
of priority will be consulted before smaller values.  The built-in resolvers
have priority values between -10 and -1.  Your resolvers can use any positive
value as their priority to override the built-in resolvers.  If your resolver
uses a value less than -10 it will be consulted *after* the built-in resolvers.

The `init` and `destroy` lifecycle methods of a resolver should be used to
establish and teardown, respectively, whatever resources (e.g. database
connections) your resolver implementation may require.


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

