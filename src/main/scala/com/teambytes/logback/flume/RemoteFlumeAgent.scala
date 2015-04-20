package com.teambytes.logback.flume

import org.apache.commons.lang.StringUtils
import org.slf4j.{LoggerFactory, Logger}

object RemoteFlumeAgent {
  private val logger: Logger = LoggerFactory.getLogger(classOf[RemoteFlumeAgent])

  def fromString(input: String): RemoteFlumeAgent = {
    if (StringUtils.isNotEmpty(input)) {
      input.split(":") match {
        case parts if parts.length == 2 =>
          try {
            val port: Int = parts(1).toInt
            new RemoteFlumeAgent(parts(0), port)
          } catch {
            case nfe: NumberFormatException =>
              logger.error("Not a valid int: " + parts(1))
              null
          }

        case _ =>
          logger.error("Not a valid [host]:[port] configuration: " + input)
          null
      }
    } else {
      logger.error("Empty flume agent entry, an extra comma?")
      null
    }
  }
}

case class RemoteFlumeAgent(hostname: String, port: Int) {
  def getHostname: String = hostname
  def getPort: Int = port
}
