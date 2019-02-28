package base64

import org.scalatest.FunSpec

// all expected output generated by apache commons codec
class Base64Spec extends FunSpec {

  describe ("Encode") {

    it ("should allow url unsafe output") {
      assert(str(Encode("hello world?")) === "aGVsbG8gd29ybGQ/")
    }

    it ("should encode short strings") {
      (("f", "Zg==") :: ("fo", "Zm8=") :: ("foo", "Zm9v") :: Nil).foreach {
        case (in, out) => assert(str(Encode(in)) === out)
      }
    }

    it ("should encode with and without padding") {
      val str = "easure."
      def check(pad: Boolean, expect: Array[Byte]) = {
        val enc = Encode(str, pad = pad)
        assert(enc === expect)
        assert(Decode(enc).right.map(new String(_)) === Right(str))
      }
      check(true, "ZWFzdXJlLg==".getBytes)
      check(false, "ZWFzdXJlLg".getBytes)
    }

  }

  describe ("Encode.urlSafe") {

    it ("should escape url unsafe output") {
      assert(str(Encode.urlSafe("hello world?")) === "aGVsbG8gd29ybGQ_")
    }

  }

  describe ("Decode") {

    it ("should not bother decoding pad only input") {
      ("====" :: "===" :: "==" :: "=" :: Nil).foreach {
        p => assert(Decode(p).right.map(_.size) === Right(0))
      }
    }

    it ("should decode url unsafe strs") {
      assert(Decode("aGVsbG8gd29ybGQ/").right.map(str) === Right("hello world?"))
    }

  }

  describe ("Decode.urlSafe") {
    it ("should decode urlsafe strs") {
      assert(Decode.urlSafe("aGVsbG8gd29ybGQ_").right.map(str) === Right("hello world?"))
    }
  }

  def str(bytes: Array[Byte]) = new String(bytes)
}
