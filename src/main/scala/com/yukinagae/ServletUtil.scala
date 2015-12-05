package com.yukinagae

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._

object ServletUtil {

  def buildRequest(r: HttpServletRequest): Request = {
    Request(
      r.getServerPort,
      r.getServerName,
      r.getRemoteAddr,
      r.getRequestURI,
      r.getQueryString,
      r.getScheme,
      r.getMethod,
      r.getProtocol,
      r.getHeaderNames.asScala.toList.map { name =>
        name -> r.getHeader(name)
      }.toMap,
      r.getContentType,
      r.getContentLength,
      r.getCharacterEncoding,
      r.getInputStream
      )
  }

}
