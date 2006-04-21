#-------------------------------------------------------------------------
#
# <Makefile>  -  <desc>
#
# begin     : Mon Apr 25 22:20:52 CEST 2005
# copyright : (C)  by Benjamin Mueller 
# email     : akula@akula.ch
# language  : make
# version   : $Id: Makefile,v 1.5 2006-04-21 07:31:33 fork_ch Exp $
#
#-------------------------------------------------------------------------

#-------------------------------------------------------------------------
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
#-------------------------------------------------------------------------

include ~/.make/common.mk

JAR_FILE=lib/jsrcpc.jar

build:: java-build $(JAR_FILE)

test: build
	java -classpath $(CP) de.dermoba.srcp.client.Test
# " vim:ts=4
