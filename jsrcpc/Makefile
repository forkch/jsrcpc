#-------------------------------------------------------------------------
#
# <Makefile>  -  <desc>
#
# begin     : Mon Apr 25 22:20:52 CEST 2005
# copyright : (C)  by Benjamin Mueller 
# email     : akula@akula.ch
# language  : make
# version   : $Id: Makefile,v 1.2 2005-11-23 22:25:14 fork_ch Exp $
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

JAVAMAKE=java -jar ./lib/javamake.jar
EXTRALIBS=
CP=$(CLASSPATH):$(EXTRALIBS):./classes
COMPILER_FLAGS=-classpath $(CP)
JAVA=/usr/bin/java
JAVA_FLAGS=-classpath $(CP)
JAR=/usr/bin/jar
JAR_FLAGS=cvf

BIN_DIR=bin/

JAVA_CLASSES_DIR=classes

JAVA_FILES=$(shell find ./src -name "*.java")

CLEAN_LIST+=./classes javamake.pdb bin/

build:: $(JAVA_CLASSES_DIR) javamake.pdb

jar:: javamake.pdb jsrcpc.jar

%.pdb: $(JAVA_FILES) javamake.recompile 
	@echo "starting java compilation."
	@mkdir -p $(JAVA_CLASSES_DIR)
	$(JAVAMAKE) -pdb $@ \
	    -classpath $(subst $(space),$(colon), $(CP)) \
	    -d $(JAVA_CLASSES_DIR) $(subst ./,,$(JAVA_FILES)) -C-Xlint:unchecked \
	    || (touch javamake.recompile && exit 1)
	@echo "java compilation done."

%.jar: $(JAVA_FILES) javamake.recompile
	@mkdir -p $(BIN_DIR)
	$(JAR) $(JAR_FLAGS) $(BIN_DIR)/$@ -C $(JAVA_CLASSES_DIR) de/

javamake.recompile:
	touch $@

java-build: $(JAVA_CLASSES_DIR) $(JAVAMAKE_PDB)

$(JAVA_CLASSES_DIR):
	mkdir -p classes

clean ::
	$(if $(CLEAN_LIST),rm -rf $(CLEAN_LIST))

test: build
	$(JAVA) $(JAVA_FLAGS) RailControlGUI

.PRECIOUS: javamake.pdb

.PHONY: build clean test

# " vim:ts=4
