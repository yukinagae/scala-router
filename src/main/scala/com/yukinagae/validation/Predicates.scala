package com.yukinagae

import java.util.Hashtable, javax.naming.NamingException, javax.naming.directory.InitialDirContext
import java.net.{URL, MalformedURLException}
import org.apache.commons.validator.routines.IntegerValidator
import org.apache.commons.validator.routines.DoubleValidator

object Predicates {

  def present_?(s: String): Boolean = if(s == null) false else !s.trim.isEmpty

  def matches(re: String)(s: String): Boolean = s match {
    case re.r(_*) => true
    case _ => false
  }

  def max_length(max: Int)(s: String): Boolean = s.size <= max

  def min_length(min: Int)(s: String): Boolean = s.size >= min

  val email_re = """(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+""" +
                 """(?:\.[a-z0-9!#$%&'*+/=?" "^_`{|}~-]+)*""" +
                 """@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+""" +
                 """[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"""

  def email_address_?(s: String): Boolean = if(!present_?(s)) false else s match {
    case email_re.r(_*) => true
    case _ => false
  }

  def dns_lookup(hostname: String, dnstype: String): Boolean = {
    val env: Hashtable[String, Any] = new Hashtable
    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory")
    val c = new InitialDirContext(env)
    try {
      val a = c.getAttributes(hostname, Array(dnstype))
      a.get(dnstype) != null
    } catch {
      case e: NamingException => false
    }
  }

  val domain_re = """(.*)@(.*)"""

  def valid_email_domain_?(s: String): Boolean = if(!email_address_?(s)) false else s match {
    case domain_re.r(first, domain) => dns_lookup(domain, "MX")
    case _ => false
  }

  def url_?(s: String): Boolean = if(!present_?(s)) false else {
    try {
      new URL(s)
      true
    } catch {
      case e: MalformedURLException => false
    }
  }

  val digits_re = """\d+"""

  def digits_?(s: String): Boolean = if(!present_?(s)) false else s match {
    case digits_re.r(_*) => true
    case _ => false
  }

  val alphanumeric_re = """[A-Za-z0-9]+"""

  def alphanumeric_?(s: String): Boolean = if(!present_?(s)) false else s match {
    case alphanumeric_re.r(_*) => true
    case _ => false
  }

  def integer_string_?(s: String): Boolean = if(!present_?(s)) false else new IntegerValidator().validate(s) != null

  def decimal_string_?(s: String): Boolean = if(!present_?(s)) false else new DoubleValidator().validate(s) != null

  def parse_number(s: String): Double = new DoubleValidator().validate(s)

  def gt(n: Int)(s: String): Boolean = if(!present_?(s)) false else parse_number(s) > n
  def gte(n: Int)(s: String): Boolean = if(!present_?(s)) false else parse_number(s) >= n

  def lt(n: Int)(s: String): Boolean = if(!present_?(s)) false else parse_number(s) < n
  def lte(n: Int)(s: String): Boolean = if(!present_?(s)) false else parse_number(s) <= n

  def over(n: Int)(s: String) = gt(n)(s)
  def under(n: Int)(s: String) = lt(n)(s)

  def at_least(n: Int)(s: String) = gte(n)(s)
  def at_most(n: Int)(s: String) = lte(n)(s)

  def between(min: Int, max: Int)(s: String): Boolean = if(!present_?(s)) false else {
    val x = parse_number(s)
    min <= x && x <= max
  }

}
