# base64

![build](https://github.com/Philippus/base64/workflows/build/badge.svg)

> the 64th base of rfc4648

This is a library for base64 encoding and decoding raw data.

It was forked from [softprops](https://github.com/softprops/base64) to be able to release a Scala 2.12 version.
However you probably want to consider using the [Java 8](#java-8) native implementation.

## Installation

base64 is published for Scala 2.10, 2.11, 2.12, and 2.13. To start using it add the following to your build.sbt:

    libraryDependencies += "nl.gn0s1s" %% "base64" % "0.2.2"

## Usage

This library encodes and decodes Byte Arrays but exposes a [typeclass interface](https://github.com/Philippus/base64/blob/master/src/main/scala/input.scala#L9-L11) for providing input defined as 

```scala
trait Input[T] {
  def bytes: Array[Byte]
}
```

Instances of this type class are defined for `java.nio.ByteBuffer`, `String`, `(String, java.nio.charset.Charset)`, and
`Array[Bytes]`. 

### Standard Encoding

To base64 encode input simply invoke the `Encode` objects `apply` method

```scala
base64.Encode("Man") 
```

This returns a Byte Array. To make this output human readable, you may wish to create a String from its output.

### URL-Safe Encoding

When working with web applications it's a common need to base64 encode information in a urlsafe way. To do so with this
library just invoke `urlSafe` with input on the `Encode` object

```scala
new String(base64.Encode.urlSafe("hello world?")) // aGVsbG8gd29ybGQ_
```

### Multiline Encoding

Fixing the width of base64 encoded data is, in some cases, a desirable property. In these cases, set the `multiline`
flag to true when encoding.

```scala
val in = "Base64 is a group of similar binary-to-text encoding schemes that represent binary data in an ASCII string format by translating it into a radix-64 representation. The term Base64 originates from a specific MIME content transfer encoding."

new String(base64.Encode(in, multiline = true))
```

will produce 

```
QmFzZTY0IGlzIGEgZ3JvdXAgb2Ygc2ltaWxhciBiaW5hcnktdG8tdGV4dCBlbmNvZGluZyBzY2hl
bWVzIHRoYXQgcmVwcmVzZW50IGJpbmFyeSBkYXRhIGluIGFuIEFTQ0lJIHN0cmluZyBmb3JtYXQg
YnkgdHJhbnNsYXRpbmcgaXQgaW50byBhIHJhZGl4LTY0IHJlcHJlc2VudGF0aW9uLiBUaGUgdGVy
bSBCYXNlNjQgb3JpZ2luYXRlcyBmcm9tIGEgc3BlY2lmaWMgTUlNRSBjb250ZW50IHRyYW5zZmVy
IGVuY29kaW5nLg==
```

### Omitting padding

You can omit padding from the output of encodings by setting the `pad` option to false

This will have the following effect on the results


With padding

```scala
new String(base64.Encode("paddington")) // cGFkZGluZ3Rvbg==
```

Without padding

```scala
new String(base64.Encode("paddington", pad = false)) // cGFkZGluZ3Rvbg
```

### Decoding

A dual for each is provided with the `Decode` object.

```scala
new String(base64.Decode.urlSafe(base64.Encode.urlSafe("hello world?")).right.get) // hello world?
```

## Why

Chances are you probably need a base64 codec.

Chances are you probably don't need everything that came with the library you use to base64 encode data.

This library aims to only do one thing. base64 _. That's it.

A secondary goal was to fully understand [rfc4648](http://www.ietf.org/rfc/rfc4648.txt) from first principals.
Implementation is a good learning tool. You should try it.

### Java 8

Java 8 has a supported API in `java.util.Base64` which includes all the [features](https://github.com/Philippus/base64/blob/master/src/test/scala/base64/Java89Base64Spec.scala)
this library provides and is quite [fast](https://github.com/Philippus/base64/blob/master/src/test/scala/base64/Base64Benchmark.scala#L42-L56).

(For fun the two encoders available in Java 6 and 7 (`sun.misc.BASE64Decoder` and `javax.xml.bind.DatatypeConverter`)
are also added to the benchmark.)

## Performance

Performance really depends on your use case, _no matter which library you use_. An attempt was made to compare
the encoding and decoding performance with the same input data against apache commons-codec base64 and
netty 4.0.7.final base64.

For encoding and decoding I found the following general repeating performance patterns
when testing [15,000 runs](https://github.com/softprops/base64/blob/master/src/test/scala/base64/bench.scala#L53) for
each library for each operation.

```
enc apache commons (byte arrays) took 97 ms
enc netty (byte buf)             took 95 ms
enc ours (byte arrays)           took 121 ms
dec apache commons (byte arrays) took 77 ms
dec netty (byte buf)             took 171 ms
dec ours (byte arrays)           took 85 ms
```

Take this with a grain of salt. None of these will be the performance bottle neck of your application. This was
just a simple measurement test to make sure this library was not doing something totally naive.

### inspiration and learning

taken from

* [Robert Harder's public domain](http://iharder.sourceforge.net/current/java/base64/)
* [netty base64](https://github.com/netty/netty/tree/master/codec/src/main/java/io/netty/handler/codec/base64)
* [haskell base64](https://github.com/bos/base64-bytestring/tree/master/Data/ByteString)
* [@tototoshi](https://github.com/tototoshi/scala-base64)

Doug Tangren (softprops) 2013-2017
