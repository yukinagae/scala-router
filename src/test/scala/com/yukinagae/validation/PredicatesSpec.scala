package com.yukinagae

import org.scalatest.FlatSpec

class PredicatesSpec extends FlatSpec  {

  import com.yukinagae.Predicates._

  "Predicates" should "present?" in {
    assert(present_?("a"))
    assert(!present_?(""))
    assert(!present_?(" "))
    assert(present_?("foo"))
  }

  "Predicates" should "matches" in {
    assert(matches("...")("foo"))
    assert(!matches("...")("foobar"))
  }

  "Predicates" should "max length" in {
    assert(max_length(5)("hello"))
    assert(max_length(5)("hi"))
    assert(!max_length(5)("hello world"))
  }

  "Predicates" should "min length" in {
    assert(min_length(5)("hello"))
    assert(min_length(5)("hello world"))
    assert(!min_length(5)("hi"))
  }

  "Predicates" should "email address?" in {
    assert(email_address_?("foo@example.com"))
    assert(email_address_?("foo+bar@example.com"))
    assert(email_address_?("foo-bar@example.com"))
    assert(email_address_?("foo.bar@example.com"))
    assert(email_address_?("foo@example.co.uk"))
    assert(!email_address_?("foo"))
    assert(!email_address_?("foo@bar"))
    assert(!email_address_?("foo bar@example.com"))
    assert(!email_address_?("foo@foo_bar.com"))
    assert(!email_address_?(null))
    assert(!email_address_?(""))
  }

  "Predicates" should "valid email domain?" in {
    assert(valid_email_domain_?("example@google.com"))
    assert(!valid_email_domain_?("foo@example.com"))
    assert(!valid_email_domain_?("foo@google.com.nospam"))
    assert(!valid_email_domain_?(null))
    assert(!valid_email_domain_?("foo"))
  }

  "Predicates" should "url?" in {
    assert(url_?("http://google.com"))
    assert(!url_?("foobar"))
    assert(!url_?(null))
    assert(!url_?(""))
  }

  "Predicates" should "digits?" in {
    assert(digits_?("01234"))
    assert(!digits_?("04xa"))
    assert(!digits_?(null))
    assert(!digits_?(""))
  }

  "Predicates" should "integer string?" in {
    assert(integer_string_?("10"))
    assert(integer_string_?("-9"))
    assert(integer_string_?("0"))
    assert(integer_string_?(" 8 "))
    assert(integer_string_?("10,000"))
    assert(!integer_string_?("foo"))
    assert(!integer_string_?("10x"))
    assert(!integer_string_?("1.1"))
    assert(!integer_string_?(null))
    assert(!integer_string_?(""))
  }

  "Predicates" should "decimal string?" in {
    assert(decimal_string_?("10"))
    assert(decimal_string_?("-9"))
    assert(decimal_string_?("0"))
    assert(decimal_string_?(" 8 "))
    assert(decimal_string_?("10,000"))
    assert(decimal_string_?("1.1"))
    assert(decimal_string_?("3.14159"))
    assert(!decimal_string_?("foo"))
    assert(!decimal_string_?("10x"))
    assert(!decimal_string_?(null))
    assert(!decimal_string_?(""))
  }

  "Predicates" should "gt" in {
    assert(gt(10)("11"))
    assert(!gt(10)("10"))
    assert(!gt(10)("9"))
    assert(!gt(10)(null))
    assert(!gt(10)(""))
  }

  "Predicates" should "gte" in {
    assert(gte(10)("11"))
    assert(gte(10)("10"))
    assert(!gte(10)("9"))
    assert(!gte(10)(null))
    assert(!gte(10)(""))
  }

  "Predicates" should "lt" in {
    assert(lt(10)("9"))
    assert(!lt(10)("10"))
    assert(!lt(10)("11"))
    assert(!lt(10)(null))
    assert(!lt(10)(""))
  }

  "Predicates" should "lte" in {
    assert(lte(10)("9"))
    assert(lte(10)("10"))
    assert(!lte(10)("11"))
    assert(!lte(10)(null))
    assert(!lte(10)(""))
  }

  "Predicates" should "between" in {
    assert(between(1, 10)("5"))
    assert(between(1, 10)("1"))
    assert(between(1, 10)("10"))
    assert(!between(1, 10)("0"))
    assert(!between(1, 10)("11"))
    assert(!between(1, 10)(null))
    assert(!between(1, 10)(""))
  }

}

