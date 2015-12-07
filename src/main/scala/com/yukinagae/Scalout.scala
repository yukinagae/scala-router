package com.yukinagae

import scala.util.parsing.combinator._
import scala.util.matching.Regex

sealed abstract trait RouteParser extends JavaTokenParsers {
	def route: Parser[Any]
	var map: Map[String, String] = Map.empty
}

class BeforeRouteParser extends RouteParser {

	def route: Parser[Any] = (scheme | part) ~ part.*
	def scheme: Parser[String] = """(https?:)?//""".r ^^ {
		case scheme => {
			// map += "scheme" -> scheme
			scheme
		}
	}
	def part: Parser[Any] = literal | escaped | param
	def literal: Parser[String] = """(:[^\p{L}_*{}\\]|[^:*{}\\])+""".r ^^ {
		case a =>
		// println(a + "!")
		a
	}
	def escaped: Parser[String] = """\\.""".r
	def param: Parser[Any] = key ^^ {
		case b =>
		val v = b.toString.replaceAll("[(~)]", "")
		map += v -> v
		b
	}
	def key: Parser[Any] = ":" ~ """([\p{L}_][\p{L}_0-9-]*)""".r
}

class AfterRouteParser(path: String, isAbsolute: Boolean, keys: List[String]) extends RouteParser {

	def route: Parser[Any] = {
		var p = path
		keys.foreach { key =>
			p = p.replace(key, literal)
		}
		p.r ^^ {
			case in => {
				// println(in)
				val paths = path.split("/")
				val ins = in.split("/")
				val zipped = paths.zip(ins)
				zipped.foreach { z =>
					if(keys.contains(z._1)) {
						map += z._1 -> z._2
					}
				}
				in
			}
		}
	}
	val literal = """(:[^\p{L}_*{}\\]|[^:*{}\\])+"""
}

object Main extends App {
	val path = "/foo/:x/:y" // => /foo/bar/baz
  println("input : "+ path)
  val compiled = Scalout.routeCompile(path)
  val inputPath = "/foo/bar/baz"
  val result = compiled.parser.parseAll(compiled.parser.route, inputPath)
  println(result)
  println(compiled.parser.map)
}

case class CompiledRoute(path: String, parser: RouteParser, keys: List[String], isAbsolute: Boolean) {
	def routeMatches(request: Request): Boolean = {
		false
	}
}

object Scalout {

	def routeCompile(path: String): CompiledRoute = {
		val parser = new BeforeRouteParser
		parser.parseAll(parser.route, path)
		val ast = parser.map
		val isAbsolute = absoluteUrl(path)
		val keys = ast.values.toList
		CompiledRoute(path, new AfterRouteParser(path, isAbsolute, keys), keys, isAbsolute)
	}

	def requestUrl(request: Request): String = {
		request.scheme + """://""" + request.headers.get("host").get + request.URI
	}

	def absoluteUrl(path: String): Boolean = {
		val r = """(https?:)?//.*""".r
		path match {
  		case r(_*) => true
  		case _ => false
		}
	}

	def routeMatches(path: String, request: Request): (Boolean, Option[Map[String, String]]) = {
		(false, None)
	}

	def routeMatches(path: String, request: Map[String, String]): (Boolean, Option[Map[String, String]]) = {
		val compiled = routeCompile(path)
  	val result = compiled.parser.parseAll(compiled.parser.route, request.get("URI").get)
  	result match {
  		case compiled.parser.Success(result, _) => (true, if(compiled.parser.map.isEmpty) None else Some(compiled.parser.map))
  		case _ => (false, None)
  	}
	}
}
